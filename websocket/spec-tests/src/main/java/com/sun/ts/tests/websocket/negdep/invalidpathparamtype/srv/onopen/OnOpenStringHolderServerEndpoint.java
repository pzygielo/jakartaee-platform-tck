/*
 * Copyright (c) 2015, 2023 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.negdep.invalidpathparamtype.srv.onopen;

import java.io.IOException;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.negdep.StringHolder;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/invalid/{arg}")
public class OnOpenStringHolderServerEndpoint {

	private String open;

	@OnMessage
	public String echo(String echo) {
		return open + echo;
	}

	@SuppressWarnings("unused")
	@OnOpen
	public void onOpen(Session session, @PathParam("arg") StringHolder sb) {
		open = sb.toString();
	}

	@OnError
	public void onError(Session session, Throwable thr) throws IOException {
		thr.printStackTrace(); // Write to error log, too
		String message = IOUtil.printStackTrace(thr);
		session.getBasicRemote().sendText(message);
	}
}
