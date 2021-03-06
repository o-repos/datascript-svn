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


    public ${className}()
    {
    }


    public ${className}(String __filename${formalParameterList}) throws IOException
    {
        FileBitStreamReader __in = new FileBitStreamReader(__filename);
        read(__in, new CallChain()${actualParameterList});
        __in.close();
    }


    public ${className}(BitStreamReader __in${formalParameterList}) throws IOException
    {
        read(__in, new CallChain()${actualParameterList});
    }


    public ${className}(BitStreamReader __in, CallChain __cc${formalParameterList}) throws IOException 
    {
        read(__in, __cc${actualParameterList});
    }


    public void read(BitStreamReader __in, CallChain __cc${formalParameterList}) throws IOException 
    {
<#list unionType.parameters as param>
        this.${param.name} = ${param.name};

</#list>
        try 
        {
            __cc.push("${className}", this);
            __fpos = __in.getBitPosition();
            while (true)
            {
<#list fields as field>
                try 
                {
    <#if field.constraint??>
                    if (!(${field.constraint}))
                    {
                        throw new IOException("constraint violated");
                    }
    </#if>
                    ${field.readField}
                    break;
                } 
                catch (Exception __exc) 
                {
                    __in.setBitPosition(__fpos);
                }

</#list>
                throw new IOException("no match in union");
            }     
        }
        finally 
        { 
            __cc.pop(); 
        }
    }
