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
package datascript.antlr.util;

import java.io.File;


/**
 * @author HWellmann
 *
 */
public class ToolContext
{
    private String fileName;
    private String pathName;
    private int numWarnings;
    private int numErrors;
    private static ToolContext singleton;
    
    private ToolContext()
    {       
    }
    
    public static ToolContext getInstance()
    {
        if (singleton == null)
        {
            singleton = new ToolContext();
        }
        return singleton;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    public static String getFileName()
    {
        return getInstance().fileName;
    }


    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }


    public static String getPathName()
    {
        return getInstance().pathName;
    }


    public static String getFullName()
    {
    	if (getPathName() == null)
    	{
    		return getFileName();
    	}
    	else
    	{
    		return new File(getPathName(), getFileName()).toString();
    	}
    }


    public int getErrorCount()
    {
        return numErrors;
    }


    public int getWarningCount()
    {
        return numWarnings;
    }


    public void warning(TokenAST token, String text)
    {
        numWarnings++;
        StringBuffer message = new StringBuffer();
        appendLocation(message, token);
        message.append("warning: ");
        message.append(text);
        System.err.println(message);
    }


    public void error(TokenAST token, String text)
    {
        numErrors++;
        StringBuffer message = new StringBuffer();
        appendLocation(message, token);
        message.append(text);
        System.err.println(message);
    }


    public static void logError(TokenAST token, String text)
    {
        getInstance().error(token, text);
    }


    public static void logWarning(TokenAST token, String text)
    {
        getInstance().warning(token, text);
    }


    private void appendLocation(StringBuffer message, TokenAST token)
    {
        if (token != null)
        {
            message.append(token.getFileName());
            message.append(":");
            message.append(token.getLine());
            message.append(":");
            message.append(token.getColumn());
            message.append(": ");
        }
    }
}
