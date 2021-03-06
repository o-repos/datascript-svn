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

import java.math.BigInteger;

public class BitFieldType extends IntegerType
{
    /** 
     * Number of bits of this bitfield. 
     * When length is known at compile time, this attribute is set to a value
     * > 0. Otherwise, length is set to -1, and the runtime length is
     * indicated by the expression stored in lengthExpr. 
     */
    int length;

    /**
     * Expression indicating the run-time length of this bitfield.
     * If this is null, length must be set to a value > 0. Otherwise,
     * length == -1.
     */
    Expression lengthExpr;

    BigInteger lowerBound, upperBound;

    public int getLength()
    {
        Value value = getLengthExpression().getValue();
        length = (value == null) ? 0 : value.integerValue().intValue();
        return length;
    }
    
    public Expression getLengthExpression()
    {
        if (lengthExpr == null)
        {
            lengthExpr = (Expression) getFirstChild();
        }
        return lengthExpr;
    }    
    
    public boolean isVariable()
    {
        return getLength() != 0;
    }

    public BitFieldType()
    {
    }

    public IntegerValue sizeof(Context ctxt)
    {
        return new IntegerValue(length);
    }

    public boolean isMember(Context ctxt, Value val)
    {
        /** @TODO handle variable length! */
        try
        {
            return (lowerBound.compareTo(val.integerValue()) != 1 && val
                    .integerValue().compareTo(upperBound) == -1);
        }
        catch (ComputeError _)
        {
            return (false);
        }
    }

    public String toString()
    {
        return "BitField"; 
        // We no longer append "/* :" + length + " */", since this causes
        // a nested comment with an array of bitfield.
    }
}
