<#--
/* BSD License
 *
 * Copyright (c) 2006, Harald Wellmann, Henrik Wedekind, Harman/Becker Automotive Systems
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

import java.sql.*;


@Generated(
        value = "datascript.tools.DataScriptTool",
        date = "${timeStamp}",
        comments = "generated by ${rdsVersion}"
    )
@SuppressWarnings("all")
public class ${name}
{
    private SqlDatabase db;
    @SuppressWarnings("unused")
    private String tableName;
<#--    private PreparedStatement insertRow;-->


    public ${name}(SqlDatabase db) throws SQLException
    {
        this.db = db;
    }


    public void createTable(String __tableName) throws SQLException
    {
        this.tableName = __tableName;

        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(__tableName);
<#list fields as field>
        query.append("<#if field_index == 0>(<#else>, </#if><#rt>
    ${field.name} ${field.sqlType}<#t>
    <#lt> NOT NULL ${field.sqlConstraint}");
</#list>
<#assign constraint = sqlConstraint>
<#if constraint?has_content>
        query.append(", ${constraint})");
<#else>
        query.append(")");
</#if>

        Statement st = db.getConnection().createStatement();
        st.executeUpdate(query.toString());
    }


    @SuppressWarnings("unused")
    public void validate(String __tableName, ValidationListener vListener, ParameterListener pListener)
    {
<#assign blobFieldList = "">
<#list fields as field>
    <#if field.sqlType == "BLOB">
        <#if blobFieldList != "">
            <#assign blobFieldList = blobFieldList + ", ">
        </#if>
        <#assign blobFieldList = blobFieldList + field.name>
    </#if>
</#list>
<#if blobFieldList?has_content>
        String pkName = "";
        Connection dbc = db.getConnection();
        try
        {
            DatabaseMetaData metaData = dbc.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(dbc.getCatalog(), "", __tableName);
            while (rs.next())
            {
                pkName = rs.getString("COLUMN_NAME");
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // selects all rows from the table
        StringBuilder query = new StringBuilder("SELECT ${blobFieldList}");
        if (pkName.length() > 0)
            query.append(", " + pkName);
        query.append(" FROM " + __tableName);

        int primaryKey = -1;
        try
        {
            Statement st = dbc.createStatement();
            ResultSet resultSet = st.executeQuery(query.toString());

            while (resultSet.next())
            {
                primaryKey = resultSet.getInt(pkName);
    <#list fields as field>
            <#-- // SQLType for "${field.name}": ${field.sqlType} -->
        <#if field.sqlType == "BLOB">
            <#if field.compoundType?has_content>

                try
                {
                <#assign cTypeName = field.compoundType.name>
                    byte[] ${field.name}Blob = resultSet.getBytes("${field.name}");
                <#assign paramList = "">
                <#list field.typeParameter as param>
                    Object ${param.name}Param = pListener.getParameterValue(${cTypeName}.class, "${param.name}", primaryKey);
                    <#assign paramList = paramList + ", ${param.name}Param">
                </#list>
                    ${cTypeName} ${field.name}Data = DataScriptIO.read(${cTypeName}.class, ${field.name}Blob${paramList});
                }
                catch (Throwable t)
                {
                    vListener.onError(__tableName, primaryKey, t);
                }
            <#else>
                // RDS compile error: "${field.name}" has no or is no CompoundType
            </#if>
        </#if>
    </#list>
            }
        }
        catch (SQLException t)
        {
            vListener.onError(__tableName, primaryKey, t);
        }
<#else>
        // There is nothing to validate...
</#if>
    }

