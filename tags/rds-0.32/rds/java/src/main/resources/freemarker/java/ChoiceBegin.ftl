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


@Generated(
        value = "datascript.tools.DataScriptTool",
        date = "${timeStamp}",
        comments = "generated by ${rdsVersion}"
    )
@SuppressWarnings("all")
public class ${className} implements ${rootPackageName}.__Visitor.Acceptor, Writer, SizeOf
{
    long __fpos;
    Object __objectChoice;
    private CallChain __cc;


    public void accept(${rootPackageName}.__Visitor visitor, Object arg)
    {
        __cc = visitor.getCallChain();
        visitor.visit(this, arg);
    }


    public int sizeof() 
    {
        return ${rootPackageName}.__SizeOf.sizeof(this);
    }


    public int bitsizeof() 
    {
        return ${rootPackageName}.__SizeOf.bitsizeof(this);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ${className})
        {
            ${className} that = (${className}) obj;

<#if equalsCanThrowExceptions>
    <#list choiceType.parameters as param>
            if(<#if param.canonicalTypeName == "datascript.ast.EnumType"><#rt>
            <#-- EnumType -->
            <#t>this.${param.name}.getValue() != that.${param.name}.getValue()
        <#elseif (param.canonicalTypeName == "datascript.ast.BitFieldType" && param.bitFieldLength == 0)><#t>
            <#-- Biginteger -->
            <#t>this.${param.name}.compareTo(that.${param.name}) != 0
        <#elseif param.canonicalTypeName == "datascript.ast.ArrayType"><#t>
            <#-- ArrayType -->
            <#t>!this.${param.name}.equalsWithException(that.${param.name})
        <#elseif !param.isSimple><#t>
            <#-- Object -->
            <#t>!this.${param.name}.equals(that.${param.name})
        <#else><#t>
            <#-- simple types -->
            this.${param.name} != that.${param.name}
        <#t></#if>)  /* ${param.canonicalTypeName} */
                throw new DataScriptError("Selector '${param.name}' is not equal!");
    </#list>
            if (this.__objectChoice == null && that.__objectChoice == null)
                return true;
            if ((this.__objectChoice == null && that.__objectChoice != null) ||    
                !this.__objectChoice.equals(that.__objectChoice))
                throw new DataScriptError("Field '__objectChoice' is not equal!");
            return true;
<#else>
            return
    <#list choiceType.parameters as param>
                (<#if param.canonicalTypeName == "datascript.ast.EnumType"><#rt>
            <#-- EnumType -->
            <#t>this.${param.name}.getValue() == that.${param.name}.getValue()
        <#elseif (param.canonicalTypeName == "datascript.ast.BitFieldType" && param.bitFieldLength == 0)><#t>
            <#-- Biginteger -->
            <#t>this.${param.name}.compareTo(that.${param.name}) == 0
        <#elseif !param.isSimple><#t>
            <#-- Object -->
            <#t>this.${param.name}.equals(that.${param.name})
        <#else><#t>
            <#-- simple types -->
            this.${param.name} == that.${param.name}
        <#t></#if>) &&   /* ${param.canonicalTypeName} */
    </#list>
                (this.__objectChoice == null && that.__objectChoice == null) ||
                (this.__objectChoice != null &&
                this.__objectChoice.equals(that.__objectChoice));
</#if>
        }
        return super.equals(obj);
    }

