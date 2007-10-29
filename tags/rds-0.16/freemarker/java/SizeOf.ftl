<#--
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
-->
<#include "FileHeader.inc.ftl">
// DS-Import
${packageImports}


public class __SizeOf extends ${rootPackageName}.__DepthFirstVisitor
{
    private int __bitsize = 0;


    public void alignto(int n)
    {
        __bitsize = ((__bitsize / n) + 1) * n;
    }


    public void visitInt8(byte n, Object arg) 
    {
        __bitsize += 8;
    }


    public void visitInt16(short n, Object arg)
    {
        __bitsize += 16;
    }


    public void visitInt32(int n, Object arg)
    {
        __bitsize += 32;
    }


    public void visitInt64(long n, Object arg)
    {
        __bitsize += 64;
    }


    public void visitUInt8(short n, Object arg)
    {
        __bitsize += 8;
    }


    public void visitUInt16(int n, Object arg)
    {
        __bitsize += 16;
    }


    public void visitUInt32(long n, Object arg)
    {
        __bitsize += 32;
    }


    public void visitUInt64(BigInteger n, Object arg)
    {
        __bitsize += 64;
    }


    public void visitBitField(byte n, int length, Object arg)
    {
        __bitsize += length;
    }


    public void visitBitField(short n, int length, Object arg)
    {
        __bitsize += length;
    }


    public void visitBitField(int n, int length, Object arg)
    {
        __bitsize += length;
    }


    public void visitBitField(long n, int length, Object arg)
    {
        __bitsize += length;
    }


    public void visitBitField(BigInteger n, int length, Object arg)
    {
        __bitsize += length;
    }


    public void visitString(String n, Object arg)
    {
        // do not use n.length()! the result contains only 
        // the count auf characters, not the count of bytes
        __bitsize += (n.getBytes().length + 1) << 3;
    }


    public static int sizeof(${rootPackageName}.__Visitor.Acceptor a) 
    {
        __SizeOf v = new __SizeOf();
        a.accept(v, null);
        if (v.__bitsize % 8 != 0) 
        {
            throw new RuntimeException("sizeof not integer: " + v.__bitsize);
        }
        return v.__bitsize / 8;
    }


    public static int bitsizeof(${rootPackageName}.__Visitor.Acceptor a) 
    {
        __SizeOf v = new __SizeOf();
        a.accept(v, null);
        return v.__bitsize;
    }
