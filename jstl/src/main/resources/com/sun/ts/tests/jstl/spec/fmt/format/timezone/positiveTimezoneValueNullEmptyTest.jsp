<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
    Copyright (c) 2024 Contributors to the Eclipse Foundation

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.Date" %>
<tck:test testName="positiveTimezoneValueNullEmptyTest">
    <%  
        Date date = new Date(883192294202L);
        pageContext.setAttribute("dte", date);
    %>

    <!-- If value is null or empty, the default TZ of GMT
             is used -->
    <fmt:timeZone value='<%= null %>'>
        <fmt:formatDate type="both" dateStyle="full" timeStyle="full" value="${dte}"/>
    </fmt:timeZone>
    <fmt:timeZone value="">
        <fmt:formatDate type="both" dateStyle="full" timeStyle="full" value="${dte}"/>
    </fmt:timeZone>
</tck:test>
