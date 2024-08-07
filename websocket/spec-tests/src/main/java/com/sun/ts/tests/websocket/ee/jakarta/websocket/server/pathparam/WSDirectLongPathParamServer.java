/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.pathparam;

import java.io.IOException;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/{param1}")
public class WSDirectLongPathParamServer {

	private final static String ERR = "TCK INTENDED ERROR";

	private String p;

	@OnOpen
	public void onOpen(@PathParam("param1") Long p1) {
		p = p1.toString();
	}

	@OnMessage
	public String param(@PathParam("param1") Long p1, String content) throws IOException {
		OPS op = OPS.valueOf(content);
		switch (op) {
		case OPEN:
			content = p;
			break;
		case MESSAGE:
			content = p1.toString();
			break;
		case IOEXCEPTION:
			throw new IOException(ERR);
		case RUNTIMEEXCEPTION:
			throw new RuntimeException(ERR);
		}
		return content;
	}

	@OnError
	public void onError(@PathParam("param1") Long p1, Session session, Throwable t) throws IOException {
		String msg = t.getMessage();
		if (ERR.equals(msg)) {
			session.getBasicRemote().sendText(p1.toString());
		} else {
			t.printStackTrace(); // Write to error log, too
			String message = IOUtil.printStackTrace(t);
			session.getBasicRemote().sendText(message);
		}
	}

	@OnClose
	public void onClose(@PathParam("param1") Long p1) {
		WSOnClosePathParamServer.set(0, p1.toString());
	}
}
