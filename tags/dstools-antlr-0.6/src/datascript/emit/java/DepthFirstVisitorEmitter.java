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

import antlr.collections.AST;
import datascript.antlr.DataScriptParserTokenTypes;
import datascript.ast.ArrayType;
import datascript.ast.BitFieldType;
import datascript.ast.EnumType;
import datascript.ast.Expression;
import datascript.ast.Field;
import datascript.ast.IntegerType;
import datascript.ast.SequenceType;
import datascript.ast.TypeInterface;
import datascript.ast.TypeReference;
import datascript.ast.UnionType;
import datascript.jet.java.DepthFirstEnumeration;
import datascript.jet.java.DepthFirstSequence;
import datascript.jet.java.DepthFirstUnion;
import datascript.jet.java.DepthFirstVisitor;
import datascript.jet.java.SequenceEnd;

public class DepthFirstVisitorEmitter extends JavaEmitter
{
    private DepthFirstVisitor visitorTmpl = new DepthFirstVisitor();
    private DepthFirstSequence sequenceTmpl = new DepthFirstSequence();
    private DepthFirstUnion unionTmpl = new DepthFirstUnion();
    private DepthFirstEnumeration enumerationTmpl = new DepthFirstEnumeration();
    private SequenceEnd endTmpl = new SequenceEnd();
    private SequenceType sequence;
    private UnionType union;
    protected EnumType enumeration;
    protected ExpressionEmitter exprEmitter = new ExpressionEmitter();
    private TypeNameEmitter typeEmitter = new TypeNameEmitter();

    public void beginTranslationUnit()
    {
        openOutputFile("__DepthFirstVisitor");
        String result = visitorTmpl.generate(this);
        out.print(result);
    }

    public void endTranslationUnit()
    {
        String result = endTmpl.generate(this);
        out.print(result);
        out.close();
    }

    public void beginSequence(AST s)
    {
        sequence = (SequenceType) s;
        String result = sequenceTmpl.generate(this);
        out.print(result);
    }

    public void endSequence(AST s)
    {
    }

    public void beginUnion(AST u)
    {
        union = (UnionType) u;
        String result = unionTmpl.generate(this);
        out.print(result);
    }

    public void endUnion(AST u)
    {
    }

    public void beginEnumeration(AST e)
    {
        enumeration = (EnumType) e;
        String result = enumerationTmpl.generate(this);
        out.print(result);
    }

    public void endEnumeration(AST e)
    {
    }
    
    public SequenceType getSequenceType()
    {
        return sequence;
    }
    
    public UnionType getUnionType()
    {
        return union;
    }
    
    public EnumType getEnumerationType()
    {
        return enumeration;
    }
    
    public String getOptionalClause(Field field)
    {
        SequenceEmitter e = new SequenceEmitter(this, sequence);
        return e.getOptionalClause(field);
    }
    
    public String getVisitor(Field field)
    {
        TypeInterface type = field.getFieldType();
        return getVisitor(type, "node." + AccessorNameEmitter.getGetterName(field) + "()");
    }
    
    public String getVisitor(TypeInterface type, String nodeName)
    {
        type = TypeReference.resolveType(type);
        Expression length = null;
        StringBuilder buffer = new StringBuilder();
        if (type instanceof IntegerType)
        {
            buffer.append("visit");
            IntegerType itype = (IntegerType)type;
            switch (itype.getType())
            {
                case DataScriptParserTokenTypes.INT8:
                    buffer.append("Int8");
                    break;
                case DataScriptParserTokenTypes.UINT8:
                    buffer.append("UInt8");
                    break;
                case DataScriptParserTokenTypes.INT16:
                    buffer.append("Int16");
                    break;
                case DataScriptParserTokenTypes.UINT16:
                    buffer.append("UInt16");
                    break;
                case DataScriptParserTokenTypes.INT32:
                    buffer.append("Int32");
                    break;
                case DataScriptParserTokenTypes.UINT32:
                    buffer.append("UInt32");
                    break;
                case DataScriptParserTokenTypes.INT64:
                    buffer.append("Int64");
                    break;
                case DataScriptParserTokenTypes.UINT64:
                    buffer.append("UInt64");
                    break;
                case DataScriptParserTokenTypes.BIT:
                    BitFieldType bftype = (BitFieldType)itype;
                    length = bftype.getLengthExpression();
                    buffer.append("BitField");
                    break;
            }
            buffer.append("(");
            buffer.append(nodeName);
            if (length != null)
            {
                buffer.append(", ");
                buffer.append(exprEmitter.emit(length, "node"));                    
            }
            buffer.append(", arg)");
        }
        else
        {
            /*
            String typeName = typeEmitter.getTypeName(type);
            buffer.append("visit");
            buffer.append(typeName.substring(0, 1).toUpperCase());
            buffer.append(typeName.substring(1, typeName.length()));
            
            buffer.append("(");
            buffer.append(nodeName);
            buffer.append(", arg)");
            */
            buffer.append(nodeName);
            buffer.append(".accept(this, arg)");
        }
        return buffer.toString();
    }
    
    
    public String getElementType(Field field)
    {
        String result = null;
        TypeInterface type = field.getFieldType();
        if (type instanceof ArrayType)
        {
            ArrayType array = (ArrayType)type;            
            result = typeEmitter.getTypeName(array.getElementType());
        }
        return result;
    }
    
    public String getElementVisitor(Field field)
    {
        String result = null;
        TypeInterface type = field.getFieldType();
        if (type instanceof ArrayType)
        {
            ArrayType array = (ArrayType)type;            
            TypeInterface elemType = array.getElementType();
            result = getVisitor(elemType, "__elem"); 
        }
        
        return result;
    }    
    
    public String startType()
    {
        return null;
    }
    
    public String endType()
    {
        return null;
    }
}
