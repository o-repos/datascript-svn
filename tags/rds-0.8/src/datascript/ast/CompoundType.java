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
package datascript.ast;

import java.util.Stack;
import java.util.Vector;

import antlr.collections.AST;
import datascript.antlr.DataScriptParserTokenTypes;


abstract public class CompoundType extends TokenAST implements TypeInterface
{
    protected Vector<Field> fields = new Vector<Field>();

    private Vector<Parameter> parameters = new Vector<Parameter>();

    // / set of compound types that can contain this type
    private Vector<CompoundType> containers = new Vector<CompoundType>();

    // / one of TypeInterface.NOBYTEORDER, BIGENDIAN, LITTLEENDIAN
    int byteOrder;

    private Scope scope;

    private String name;

    private CompoundType parent;

    abstract public IntegerValue sizeof(Context ctxt);

    abstract public boolean isMember(Context ctxt, Value val);

    protected boolean bfoComputed = false;


    protected CompoundType()
    {
    }
    
    public CompoundType getParent()
    {
        return parent;
    }
/*
    public void addParameter(String param)
    {
        if (parameters == null)
        {
            parameters = new Vector<String>();
        }
        parameters.addElement(param);
    }
*/
    public boolean isParameter(String param)
    {
        return (parameters != null) && parameters.contains(param);
    }

    public Iterable<Parameter> getParameters()
    {
        return parameters;
    }

    public Parameter getParameterAt(int index)
    {
        return parameters.get(index);
    }

    public int getParameterCount()
    {
        return parameters.size();
    }


    public boolean isEmpty()
    {
        return fields.size() == 0;
    }

    public String getName()
    {
        if (name == null)
        {
            AST node = getFirstChild();
            if (node == null)
            {
                name = "<anonymous>";
            }
            else
            {
                name = node.getText();
            }
        }
        return name;
    }

    public String getDocumentation()
    {
        String result = "";
        AST n = getNextSibling();
        if (n != null && n.getType() == DataScriptParserTokenTypes.DOC)
        {
            result = n.getText();
        }
        return result;
    }

    public void addContainer(CompoundType f)
    {
        if (!containers.contains(f))
        {
            containers.addElement(f);
        }
    }

 
    /**
     * @return true if 'this' is contained in compound type 'f'
     */
    public boolean isContainedIn(CompoundType f)
    {
        return isContainedIn(f, new Stack<CompoundType>());
    }

    /**
     * The "is contained" relationship may contain cycles use a stack to avoid
     * them. This is a simple DFS path finding algorithm that finds a path from
     * 'this' to 'f'.
     */
    private boolean isContainedIn(CompoundType f, Stack<CompoundType> seen)
    {
        if (containers.contains(f))
        {
            return true;
        }

        /* check whether any container of 'this' is contained in 'f' */
        for (int i = 0; i < containers.size(); i++)
        {
            CompoundType c = (CompoundType) containers.elementAt(i);
            if (seen.search(c) == -1)
            {
                seen.push(c);
                if (c.isContainedIn(f, seen))
                {
                    return true;
                }
                seen.pop();
            }
        }
        return false;
    }


    public Iterable<Field> getFields()
    {
        return fields;
    }
    
    public int getNumFields()
    {
        return fields.size();
    }

    public void addField(Field f)
    {
        // TODO: Main.assertThat(!fields.contains(f));
        fields.addElement(f);
    }

    public Field getField(int i)
    {
        return fields.elementAt(i);
    }

    public Scope getScope()
    {
        return scope;
    }

    public void setScope(Scope scope)
    {
        this.scope = scope;
        scope.setOwner(this);
    }

    public String toString()
    {
        return name;
    }

    public Value castFrom(Value val)
    {
        throw new Error("casting compounds not implemented");
    }

    
    public void storeParameters()
    {
        AST node = getFirstChild().getNextSibling();
        if (node.getType() == DataScriptParserTokenTypes.PARAMLIST)
        {
            parameters = new Vector<Parameter>(node.getNumberOfChildren());
            AST p = node.getFirstChild();
            while (p != null)
            {
                storeParameter(p);
                p = p.getNextSibling();
            }
        }
    }
    
    private void storeParameter(AST param)
    {
        AST type = param.getFirstChild();
        AST id = type.getNextSibling();
        Parameter p = new Parameter(id.getText(), (TypeInterface)type);
        parameters.add(p);
        scope.setSymbol(id, p);
    }
    
    public int getLength()
    {
        throw new InternalError("not implemented");
    }
    
    public Expression getLengthExpression()
    {
        throw new InternalError("not implemented");
    }
}
