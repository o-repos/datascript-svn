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

import datascript.antlr.DataScriptParserTokenTypes;

public class StdIntegerType extends IntegerType
{
    boolean isSigned()
    {
        switch (getType())
        {
            case DataScriptParserTokenTypes.INT8:
            case DataScriptParserTokenTypes.INT16:
            case DataScriptParserTokenTypes.INT32:
            case DataScriptParserTokenTypes.INT64:
                return true;

            default:
                return false;
        }
    }

    BigInteger getLowerBound()
    {
        return lowerbounds[getType()];
    }

    BigInteger getUpperBound()
    {
        return upperbounds[getType()];
    }

/*
    public String toString()
    {
        // remove double quotes from "uint8"
        return DataScriptParser._tokenNames[getType()];
    }
*/    
    public boolean equals(Object obj)
    {
        StdIntegerType other = (StdIntegerType)obj;
        return other.getType() == getType();
    }

    public IntegerValue sizeof(Context ctxt)
    {
        switch (getType())
        {
            case DataScriptParserTokenTypes.UINT8:
            case DataScriptParserTokenTypes.INT8:
                return new IntegerValue(8);
            case DataScriptParserTokenTypes.UINT16:
            case DataScriptParserTokenTypes.INT16:
            //case DataScriptParserTokenTypes.LEUINT16:
            //case DataScriptParserTokenTypes.LEINT16:
                return new IntegerValue(16);
            case DataScriptParserTokenTypes.UINT32:
            case DataScriptParserTokenTypes.INT32:
            //case DataScriptParserTokenTypes.LEUINT32:
            //case DataScriptParserTokenTypes.LEINT32:
                return new IntegerValue(32);
            case DataScriptParserTokenTypes.UINT64:
            case DataScriptParserTokenTypes.INT64:
            //case DataScriptParserTokenTypes.LEUINT64:
            //case DataScriptParserTokenTypes.LEINT64:
                return new IntegerValue(64);
            default:
                throw new ComputeError("unknown type in sizeof");
        }
    }

    private static BigInteger lowerbounds[];

    private static BigInteger upperbounds[];

    //private static StdIntegerType builtinTypes[];

    static
    {
        int n = DataScriptParserTokenTypes.INT64+1;
        lowerbounds = new BigInteger[n];
        upperbounds = new BigInteger[n];
        lowerbounds[DataScriptParserTokenTypes.UINT8] = BigInteger.ZERO;
        lowerbounds[DataScriptParserTokenTypes.UINT16] = BigInteger.ZERO;
        lowerbounds[DataScriptParserTokenTypes.UINT32] = BigInteger.ZERO;
        lowerbounds[DataScriptParserTokenTypes.UINT64] = BigInteger.ZERO;
/*
        lowerbounds[DataScriptParserTokenTypes.LEUINT16] = BigInteger.ZERO;
        lowerbounds[DataScriptParserTokenTypes.LEUINT32] = BigInteger.ZERO;
        lowerbounds[DataScriptParserTokenTypes.LEUINT64] = BigInteger.ZERO;
*/
        upperbounds[DataScriptParserTokenTypes.UINT8] = BigInteger.ONE.shiftLeft(8);
        upperbounds[DataScriptParserTokenTypes.UINT16] = BigInteger.ONE.shiftLeft(16);
        upperbounds[DataScriptParserTokenTypes.UINT32] = BigInteger.ONE.shiftLeft(32);
        upperbounds[DataScriptParserTokenTypes.UINT64] = BigInteger.ONE.shiftLeft(64);
/*
        upperbounds[DataScriptParserTokenTypes.LEUINT16] = BigInteger.ONE.shiftLeft(16);
        upperbounds[DataScriptParserTokenTypes.LEUINT32] = BigInteger.ONE.shiftLeft(32);
        upperbounds[DataScriptParserTokenTypes.LEUINT64] = BigInteger.ONE.shiftLeft(64);
*/        
        upperbounds[DataScriptParserTokenTypes.INT8] = BigInteger.ONE.shiftLeft(7).subtract(
                BigInteger.ONE);
        upperbounds[DataScriptParserTokenTypes.INT16] = BigInteger.ONE
            .shiftLeft(15).subtract(BigInteger.ONE);
        upperbounds[DataScriptParserTokenTypes.INT32] = BigInteger.ONE
            .shiftLeft(31).subtract(BigInteger.ONE);
        upperbounds[DataScriptParserTokenTypes.INT64] = BigInteger.ONE
        .shiftLeft(63).subtract(BigInteger.ONE);

/*
        upperbounds[DataScriptParserTokenTypes.LEINT16] = upperbounds[DataScriptParserTokenTypes.INT16] = BigInteger.ONE
                .shiftLeft(15).subtract(BigInteger.ONE);
        upperbounds[DataScriptParserTokenTypes.LEINT32] = upperbounds[DataScriptParserTokenTypes.INT32] = BigInteger.ONE
                .shiftLeft(31).subtract(BigInteger.ONE);
        upperbounds[DataScriptParserTokenTypes.LEINT64] = upperbounds[DataScriptParserTokenTypes.INT64] = BigInteger.ONE
                .shiftLeft(63).subtract(BigInteger.ONE);
*/
        lowerbounds[DataScriptParserTokenTypes.INT8] = upperbounds[DataScriptParserTokenTypes.INT8].not();
        lowerbounds[DataScriptParserTokenTypes.INT16] = upperbounds[DataScriptParserTokenTypes.INT16].not();
        lowerbounds[DataScriptParserTokenTypes.INT32] = upperbounds[DataScriptParserTokenTypes.INT32].not();
        lowerbounds[DataScriptParserTokenTypes.INT64] = upperbounds[DataScriptParserTokenTypes.INT64].not();
/*
        lowerbounds[DataScriptParserTokenTypes.LEINT16] = upperbounds[DataScriptParserTokenTypes.INT16].not();
        lowerbounds[DataScriptParserTokenTypes.LEINT32] = upperbounds[DataScriptParserTokenTypes.INT32].not();
        lowerbounds[DataScriptParserTokenTypes.LEINT64] = upperbounds[DataScriptParserTokenTypes.INT64].not();
*/
        /*
        builtinTypes = new BuiltinType[n];
        builtinTypes[DataScriptParserTokenTypes.INT8] = new BuiltinType(DataScriptParserTokenTypes.INT8);
        builtinTypes[DataScriptParserTokenTypes.INT16] = new BuiltinType(DataScriptParserTokenTypes.INT16);
        builtinTypes[DataScriptParserTokenTypes.INT32] = new BuiltinType(DataScriptParserTokenTypes.INT32);
        builtinTypes[DataScriptParserTokenTypes.INT64] = new BuiltinType(DataScriptParserTokenTypes.INT64);
        builtinTypes[DataScriptParserTokenTypes.UINT8] = new BuiltinType(DataScriptParserTokenTypes.UINT8);
        builtinTypes[DataScriptParserTokenTypes.UINT16] = new BuiltinType(DataScriptParserTokenTypes.UINT16);
        builtinTypes[DataScriptParserTokenTypes.UINT32] = new BuiltinType(DataScriptParserTokenTypes.UINT32);
        builtinTypes[DataScriptParserTokenTypes.UINT64] = new BuiltinType(DataScriptParserTokenTypes.UINT64);
        builtinTypes[DataScriptParserTokenTypes.LEINT16] = new BuiltinType(DataScriptParserTokenTypes.LEINT16);
        builtinTypes[DataScriptParserTokenTypes.LEINT32] = new BuiltinType(DataScriptParserTokenTypes.LEINT32);
        builtinTypes[DataScriptParserTokenTypes.LEINT64] = new BuiltinType(DataScriptParserTokenTypes.LEINT64);
        builtinTypes[DataScriptParserTokenTypes.LEUINT16] = new BuiltinType(
                DataScriptParserTokenTypes.LEUINT16);
        builtinTypes[DataScriptParserTokenTypes.LEUINT32] = new BuiltinType(
                DataScriptParserTokenTypes.LEUINT32);
        builtinTypes[DataScriptParserTokenTypes.LEUINT64] = new BuiltinType(
                DataScriptParserTokenTypes.LEUINT64);
*/
    }

/*
    static BuiltinType getTypeByTokenKind(int k)
    {
        return builtinTypes[k];
    }
*/
    /**
     * for builtin types, membership test amounts to testing whether it's an
     * integer (via integerValue()) and then a simple range comparison
     */
    public boolean isMember(Context ctxt, Value val)
    {
        try
        {
            return (lowerbounds[getType()].compareTo(val.integerValue()) != 1 && val
                    .integerValue().compareTo(upperbounds[getType()]) == -1);
        }
        catch (ComputeError _)
        {
            return (false);
        }
    }

    public Value castFrom(Value val)
    {
        return null;
        /* TODO:
        if (isMember(null, val))
        {
            return val;
        }
        throw new CastError("cannot cast " + val + " to " + this);
        */
    }

    static StdIntegerType getBuiltinType(TypeInterface ftype)
    {
        StdIntegerType btype;
        if (ftype instanceof StdIntegerType)
        {
            btype = (StdIntegerType) ftype;
        }
        else
        {
            btype = null; //TODO (BuiltinType) ((SetType) ftype).getType();
        }
        return btype;
    }
    
}
