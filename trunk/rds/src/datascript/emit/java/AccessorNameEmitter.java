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


import datascript.ast.Field;
import datascript.ast.Parameter;



public class AccessorNameEmitter
{
    // TODO: make all methods static
    public AccessorNameEmitter()
    {
    }


    public static String getCheckerName(Field field)
    {
        StringBuffer result = new StringBuffer("is");
        return appendAccessorTail(result, field);
    }


    public static String getIndicatorName(Field field)
    {
        StringBuffer result = new StringBuffer("has");
        return appendAccessorTail(result, field);
    }


    public static String getGetterName(Field field)
    {
        StringBuffer result = new StringBuffer("get");
        return appendAccessorTail(result, field);
    }


    public static String getSetterName(Field field)
    {
        StringBuffer result = new StringBuffer("set");
        return appendAccessorTail(result, field);
    }


    public static String getSetterName(Parameter param)
    {
        StringBuffer result = new StringBuffer("set");
        return appendAccessorTail(result, param.getName());
    }


    public static String getVisitorName(Field field)
    {
        StringBuffer result = new StringBuffer("visit");
        return appendAccessorTail(result, field);
    }


    private static String appendAccessorTail(StringBuffer buffer, Field field)
    {
        return appendAccessorTail(buffer, field.getName());
    }


    private static String appendAccessorTail(StringBuffer buffer, String name)
    {
        buffer.append(name.substring(0, 1).toUpperCase());
        buffer.append(name.substring(1, name.length()));
        return buffer.toString();
    }
}
