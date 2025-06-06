/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EJBLiteServlet2Filter extends Client implements Filter {
    private static Logger logger = Logger.getLogger(EJBLiteServlet2Filter.class.getName());
    private FilterConfig filterConfig;

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
	response.setContentType("text/plain");
        setInjectionSupported(true);
        String tn = request.getParameter("testName");
        if(logger.isLoggable(Level.FINE)) {
            logger.fine("doFilter testName=" + tn);
        }
        setTestName(tn);
	setModuleName( ((HttpServletRequest) request).getContextPath() );
        String sta = getStatus();  //to trigger the test run
        PrintWriter pw = response.getWriter();
        pw.println(sta + " " + getReason());
        cleanup();  //need to reset all fields since filter instances are shared
        //skip the rest of the chain
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}
