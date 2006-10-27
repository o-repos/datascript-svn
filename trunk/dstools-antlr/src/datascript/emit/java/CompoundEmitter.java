/* BSD License
 *
 * Copyright (c) 2006, Harald Wellmann, Harman/Becker Automotive Systems
 * All rights reserved.
 * 
 * This software is derived from previous work
 * Copyright (c) 2003, Godmar Back.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 * 
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 * 
 *     * Neither the name of Harman/Becker Automotive Systems or
 *       Godmar Back nor the names of their contributors may be used to
 *       endorse or promote products derived from this software without
 *       specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package datascript.emit.java;

import java.io.PrintStream;

import antlr.collections.AST;
import datascript.antlr.DataScriptParserTokenTypes;
import datascript.ast.ArrayType;
import datascript.ast.BitFieldType;
import datascript.ast.CompoundType;
import datascript.ast.EnumType;
import datascript.ast.Expression;
import datascript.ast.Field;
import datascript.ast.IntegerType;
import datascript.ast.Parameter;
import datascript.ast.StdIntegerType;
import datascript.ast.TypeInstantiation;
import datascript.ast.TypeInterface;
import datascript.ast.TypeReference;
import datascript.ast.Value;
import datascript.jet.java.ArrayRead;

abstract public class CompoundEmitter
{
    private static String nl = System.getProperties().getProperty("line.separator");
    private JavaEmitter global;
    private TypeNameEmitter typeNameEmitter;
    private ExpressionEmitter exprEmitter = new ExpressionEmitter();
    private ArrayRead arrayTmpl = new ArrayRead();
    private AccessorNameEmitter ane = new AccessorNameEmitter();
    private StringBuilder buffer;
    private String formalParams;
    private String actualParams;
    protected PrintStream out;
    
    public CompoundEmitter(JavaEmitter j)
    {
        this.global = j;
        this.typeNameEmitter = new TypeNameEmitter();
    }
   
    abstract public CompoundType getCompoundType();
    abstract public FieldEmitter getFieldEmitter();

    public JavaEmitter getGlobal()
    {
        return global;
    }
    
    protected void reset()
    {
        formalParams = null;
        actualParams = null;
    }

    public void setOutputStream(PrintStream out)
    {
        this.out = out;
        getFieldEmitter().setOutputStream(out);
    }
    
    
    public void readFields()
    {
        for (Field field : getCompoundType().getFields())
        {
            readField(field);
        }
    }
    
    public String readField(Field field)
    {
        buffer = new StringBuilder();
        TypeInterface type = field.getFieldType();
        type = TypeReference.resolveType(type);
        if (type instanceof IntegerType)
        {
            readIntegerField(field, (IntegerType)type);
        }
        else if (type instanceof CompoundType)
        {
            readCompoundField(field, (CompoundType)type);
        }
        else if (type instanceof ArrayType)
        {
            readArrayField(field, (ArrayType)type);
        }
        else if (type instanceof EnumType)
        {
            readEnumField(field, (EnumType)type);
        }
        else if (type instanceof TypeInstantiation)
        {
            TypeInstantiation inst = (TypeInstantiation)type;
            CompoundType compound = inst.getBaseType();
            readInstantiatedField(field, compound, inst);
        }
        else
        {
            throw new InternalError("unhandled type: " + type.getClass().getName());
        }
        return buffer.toString();
    }
    
    private void indent()
    {
        buffer.append("                "); // 4*4
    }
    
    private void readIntegerField(Field field, IntegerType type)
    {
        buffer.append(ane.getSetterName(field));
        buffer.append("(");
        readIntegerValue(field, type);
        buffer.append(");");
    }
    
    private void readIntegerValue(Field field, IntegerType type)
    {
        String methodSuffix;
        String cast = "";
        String arg = "";
        switch (type.getType())
        {
            case DataScriptParserTokenTypes.INT8:
                methodSuffix = "Byte";
                break;
            
            case DataScriptParserTokenTypes.INT16:
                methodSuffix = "Short";
                break;
            
            case DataScriptParserTokenTypes.INT32:
                methodSuffix = "Int";
                break;
            
            case DataScriptParserTokenTypes.INT64:
                methodSuffix = "Long";
                break;
            
            case DataScriptParserTokenTypes.UINT8:
                methodSuffix = "UnsignedByte";
                cast = "(short) ";
                break;
            
            case DataScriptParserTokenTypes.UINT16:
                methodSuffix = "UnsignedShort";                
                break;
            
            case DataScriptParserTokenTypes.UINT32:
                methodSuffix = "UnsignedInt";
                break;
            
            case DataScriptParserTokenTypes.UINT64:
                methodSuffix = "BigInteger";
                arg = "64";
                break;

            case DataScriptParserTokenTypes.BIT:
                Expression lengthExpr = ((BitFieldType)type).getLengthExpression();
                Value lengthValue = lengthExpr.getValue();
                if (lengthValue == null)
                {
                    methodSuffix = "BigInteger";
                }
                else
                {
                    int length = lengthValue.integerValue().intValue();
                    if (length < 64)
                    {
                        methodSuffix = "Bits";
                        cast = "(" + typeNameEmitter.getTypeName(type) + ") ";
                    }
                    else
                    {
                        methodSuffix = "BigInteger";
                    }
                }
                arg = exprEmitter.emit(lengthExpr);
                break;
            default:
                throw new InternalError("unhandled type = " + type.getType());
        }
        buffer.append(cast);
        buffer.append("__in.read");
        buffer.append(methodSuffix);
        buffer.append("(");      
        buffer.append(arg);      
        buffer.append(")");
    }
    
    private void readCompoundField(Field field, CompoundType type) 
    {
        buffer.append(ane.getSetterName(field));
        buffer.append("(new ");
        buffer.append(type.getName());
        buffer.append("(__in, __cc));");
    }

    private void readInstantiatedField(Field field, CompoundType type,
                                       TypeInstantiation inst)
    {
        buffer.append(ane.getSetterName(field));
        buffer.append("(new ");
        buffer.append(type.getName());
        buffer.append("(__in, __cc");
        Iterable<Expression> arguments = inst.getArguments();
        if (arguments != null)
        {
            int argIndex = 0;
            for (Expression arg : arguments)
            {
                buffer.append(", ");
                boolean cast = emitTypeCast(type, arg, argIndex);
                String javaArg = exprEmitter.emit(arg);
                buffer.append(javaArg);
                if (cast)
                {
                    buffer.append(")");
                }
                argIndex++;
            }
        }
        buffer.append("));");
    }

    
    
    /**
     * Emits a type cast for passing an argument to a parameterized type.
     * @param type              compound type with parameters
     * @param expr              argument expression in type instantiation
     * @param paramIndex        index of argument in argument list
     */
    private boolean emitTypeCast(CompoundType type, Expression expr, 
                              int paramIndex)
    {
        boolean cast = false;
        Parameter param = type.getParameterAt(paramIndex);
        TypeInterface paramType = TypeReference.resolveType(param.getType());
        if (paramType instanceof StdIntegerType)
        {
            StdIntegerType intType = (StdIntegerType)paramType;
            switch (intType.getType())
            {
                case DataScriptParserTokenTypes.INT8:
                    buffer.append("(byte)(");
                    cast = true;
                    break;
                    
                case DataScriptParserTokenTypes.UINT8:
                case DataScriptParserTokenTypes.INT16:
                    buffer.append("(short)(");
                    cast = true;
                    break;
            }
        }
        return cast;
    }
    
    private void readArrayField(Field field, ArrayType array)
    {
        String elTypeJavaName = typeNameEmitter.getTypeName(array);
        if (elTypeJavaName.startsWith("ObjectArray"))
        {
            String elTypeName = typeNameEmitter.getTypeName(array.getElementType());
            ArrayEmitter arrayEmitter = new ArrayEmitter(field, array, 
                    elTypeName);
            String result = arrayTmpl.generate(arrayEmitter);
            buffer.append(result);            
        }
        else
        {
            Expression length = array.getLengthExpression();
            buffer.append(ane.getSetterName(field));
            buffer.append("(new ");
            buffer.append(elTypeJavaName);
            buffer.append("(__in, (int)(");
            buffer.append(getLengthExpression(length));
            buffer.append(")");
            TypeInterface elType = array.getElementType();
            if (elType instanceof BitFieldType)
            {
                BitFieldType bitField = (BitFieldType) elType;
                Expression numBits = bitField.getLengthExpression();
                buffer.append(", ");
                buffer.append(getLengthExpression(numBits));
            }
            buffer.append("));");
        }
    }
    
    private void readEnumField(Field field, EnumType type)
    {
        IntegerType baseType = (IntegerType) type.getBaseType();
        buffer.append(ane.getSetterName(field));
        buffer.append("(");
        buffer.append(type.getName());
        buffer.append(".toEnum(");
        readIntegerValue(field, baseType);
        buffer.append("));");        
    }
    
    private String getLengthExpression(Expression expr)
    {
        // TODO handle variable length
        return exprEmitter.emit(expr);        
    }
    
    public String getConstraint(Field field)
    {
        String result = null;
        Expression expr = field.getCondition();
        if (expr != null)
        {
            result = exprEmitter.emit(expr);
        }
        else
        {
            expr = field.getInitializer();
            if (expr != null)
            {
                result = field.getName() + " == " + exprEmitter.emit(expr);
            }
        }
        return result;
    }
    
    public String getOptionalClause(Field field)
    {
        String result = null;
        Expression expr = field.getOptionalClause();
        if (expr != null)
        {
            result = exprEmitter.emit(expr);
        }
        return result;
    }
    
    public void buildParameterLists()
    {
        StringBuilder formal = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        CompoundType compound = getCompoundType();
        for (Parameter param : compound.getParameters())
        {
            String paramName = param.getName();
            TypeInterface paramType = TypeReference.resolveType(param.getType());
            
            String typeName = typeNameEmitter.getTypeName(paramType);
            formal.append(", ");
            formal.append(typeName);
            formal.append(" ");
            formal.append(paramName);

            actual.append(", ");
            actual.append(paramName);
        }
        formalParams = formal.toString();
        actualParams = actual.toString();
    }
    
    public String getFormalParameterList()
    {
        if (formalParams == null)
        {
            buildParameterLists();
        }
        return formalParams;
    }

    public String getActualParameterList()
    {
        if (actualParams == null)
        {
            buildParameterLists();
        }
        return actualParams;
    }
    
    public String getLabelExpression(Field field)
    {
        String result = null;
        Expression label = field.getLabel();
        if (label != null)
        {
            StringBuilder buffer = new StringBuilder();
            AST labelBase = label.getNextSibling();
            if (labelBase != null)
            {
                String name = labelBase.getText();
                buffer.append("((");
                buffer.append(name);
                buffer.append(")__cc.find(\"");
                buffer.append(name);
                buffer.append("\")).");
            }
            buffer.append("__fpos + 8*");
            String labelExpr = exprEmitter.emit(label);
            buffer.append(labelExpr);
            result = buffer.toString();
        }
        return result;
    }
}
