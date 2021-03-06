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
package datascript.runtime;

import java.io.DataInput;
import java.io.IOException;

public class UnsignedByteArray implements Array, SizeOf
{
    short[] data; // data is between [offset... offset+length-1]
    int offset;
    int length;

    public UnsignedByteArray(DataInput in, int length) throws IOException
    {
        if (length == -1)
        {
            throw new RuntimeException("variable length " + getClass()
                    + " not implemented");
        }
        else
        {
            this.length = length;
            data = new short[length];
            for (int i = 0; i < length; i++)
            {
                data[i] = (short) in.readUnsignedByte();
            }
            this.offset = 0;
        }
    }

    public UnsignedByteArray(int length)
    {
        this(new short[length], 0, length);
    }

    public UnsignedByteArray(short[] data, int offset, int length)
    {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    public Array map(Mapping m)
    {
        UnsignedByteArray result = new UnsignedByteArray(length);
        for (int i = 0; i < length; i++)
        {
            result.data[i] = ((Byte) m.map(new Short(data[offset + i])))
                    .byteValue();
        }
        return result;
    }

    public Array subRange(int begin, int length)
    {
        if (begin < 0 || begin >= this.length || begin + length > this.length)
            throw new ArrayIndexOutOfBoundsException();
        return new UnsignedByteArray(data, offset + begin, length);
    }

    public void write(java.io.DataOutput out, CallChain cc) throws IOException
    {
        for (int i = offset; i < offset + length; i++)
        {
            out.writeByte(data[i]);
        }
    }

    public short elementAt(int i)
    {
        return data[offset + i];
    }

    public int length()
    {
        return length;
    }

    public int sizeof()
    {
        return length;
    }

    /**
     * Compares this byte array to a given string, assuming all characters are
     * 8bit only (won't work for Unicode)
     * 
     * For small strings only.
     */
    public boolean compare_to_string(String str)
    {
        byte[] sbytes = str.getBytes();
        if (sbytes.length > length)
        {
            return false;
        }

        for (int i = 0; i < sbytes.length; i++)
        {
            if (data[offset + i] != sbytes[i])
            {
                return false;
            }
        }

        return true;
    }
}
