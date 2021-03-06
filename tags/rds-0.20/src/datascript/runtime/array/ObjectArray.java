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


package datascript.runtime.array;


import java.util.ArrayList;
import java.util.List;

import datascript.runtime.CallChain;
import datascript.runtime.Mapping;
import datascript.runtime.io.BitStreamWriter;
import datascript.runtime.io.Writer;



public class ObjectArray<E> implements Array<E>, SizeOf
{
    // using a Vector for now means we're subject to the limitations
    // Vectors are (i.e., max 2^31-1 elements
    List<E> data;


    public ObjectArray(int length)
    {
        this(new ArrayList<E>(length));
    }


    public ObjectArray(List<E> data)
    {
        this.data = data;
    }


    public ObjectArray<E> typedMap(Mapping<E> m)
    {
        List<E> result = new ArrayList<E>(data.size());
        for (int i = 0; i < data.size(); i++)
        {
            result.add(i, m.map(data.get(i)));
        }
        return new ObjectArray<E>(result);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ObjectArray)
        {
            ObjectArray<?> that = (ObjectArray<?>) obj;
            return (this.data.equals(that.data));
        }
        return super.equals(obj);
    }


    public boolean equalsWithException(Object obj)
    {
        if (obj instanceof ObjectArray)
        {
            ObjectArray<?> that = (ObjectArray<?>) obj;
            // not necessary to loop the array two times
//	        if (that.sizeof() != this.sizeof())
//	            throw new RuntimeException("size of arrays are different.");
            int thatLenght = that.data.size();
            int thisLength = this.data.size();
	        if (thatLenght != thisLength)
	            throw new RuntimeException(
	            		"count of elements in arrays are different.");
	
	        for (int i = 0; i < this.data.size(); i++)
	        {
	        	try
	        	{
		            if (!this.data.get(i).equals(that.data.get(i)))
		                throw new RuntimeException("index " + i + " do not match.");
	        	}
	        	catch (Exception e)
	        	{
	                throw new RuntimeException("Exception on index " + i, e);
	        	}
	        }
	        return true;
        }
        return super.equals(obj);
    }


    public Array<E> map(Mapping<E> m)
    {
        List<E> result = new ArrayList<E>(data.size());
        for (int i = 0; i < data.size(); i++)
        {
            result.add(i, m.map(data.get(i)));
        }
        return new ObjectArray<E>(result);
    }


    public Array<E> subRange(int begin, int length)
    {
        return new ObjectArray<E>(data.subList(begin, begin + length));
    }


    public int length()
    {
        return data.size();
    }


    public void write(BitStreamWriter out, CallChain cc) throws Exception
    {
        for (int i = 0; i < data.size(); i++)
        {
            ((Writer) data.get(i)).write(out, cc);
        }
    }


    public E elementAt(int i)
    {
        return data.get(i);
    }


    public void setElementAt(E value, int i)
    {
        data.set(i, value);
    }


    public int sizeof()
    {
        int sz = 0;
        for (int i = 0; i < data.size(); i++)
        {
            sz += ((SizeOf) data.get(i)).sizeof();
        }
        return sz;
    }


    public int bitsizeof()
    {
        int sz = 0;
        for (int i = 0; i < data.size(); i++)
        {
            sz += ((SizeOf) data.get(i)).bitsizeof();
        }
        return sz;
    }


    public void remove(Object obj)
    {
        data.remove(obj);
    }


    public List<E> getData()
    {
        return data;
    }
}
