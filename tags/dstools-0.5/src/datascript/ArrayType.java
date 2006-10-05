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
package datascript;

import datascript.syntaxtree.Node;
import datascript.syntaxtree.NodeToken;

public class ArrayType implements TypeInterface
{
    TypeInterface elementType;
    Node lowerBound; // null if 0
    Node upperBound;
    Node arrayRange;
    IntegerValue length = null; // null is not known, else value

    ArrayType(TypeInterface elementType, Node lowerBound, Node arrayRange,
            Node upperBound)
    {
        this.elementType = elementType;
        this.arrayRange = arrayRange;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public IntegerValue sizeof(Context ctxt)
    {
        if (length != null)
        {
            return elementType.sizeof(ctxt).multiply(length);
        }
        throw new ComputeError("sizeof array not known");
    }

    public boolean isMember(Context ctxt, Value val)
    {
        throw new ComputeError("ArrayType.isMember not implemented");
    }

    public Value castFrom(Value val)
    {
        throw new ComputeError("cannot cast " + val + " into " + this);
    }

    TypeInterface getElementType()
    {
        return elementType;
    }

    NodeToken getSourceLocation()
    {
        return (ClosestToken.find(arrayRange));
    }

    public String toString()
    {
        return "array of " + elementType;
    }

    void resolveElementType(CompoundType ctype)
    {
        elementType = TypeReference.resolveType(elementType);
        if (elementType instanceof ArrayType)
        {
            ((ArrayType) elementType).resolveElementType(ctype);
        }
        if (elementType instanceof TypeInstantiation)
        {
            ((TypeInstantiation) elementType).resolveBaseType(ctype);
        }

        // try to compute length if known
        if (upperBound == null)
        {
            return;
        }

        try
        {
            ExpressionEvaluator ube = new ExpressionEvaluator(ctype);
            upperBound.accept(ube);
            IntegerValue len = (IntegerValue) ube.value();
            if (lowerBound != null)
            {
                ExpressionEvaluator lbe = new ExpressionEvaluator(ctype);
                lowerBound.accept(lbe);
                len = ((IntegerValue) lbe.value()).subtract(len);
            }
            this.length = len;
        }
        catch (ComputeError e)
        {
            ; // okay, so bad luck, length remains null
        }
    }
}
