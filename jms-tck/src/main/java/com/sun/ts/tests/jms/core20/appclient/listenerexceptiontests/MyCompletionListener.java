/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests;

import java.lang.System.Logger;
import java.util.ArrayList;

import jakarta.jms.CompletionListener;
import jakarta.jms.Connection;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class MyCompletionListener implements CompletionListener {

	private String name = null;

	private Message message = null;

	private Connection connection = null;

	private Session session = null;

	private ArrayList<Message> messages = new ArrayList<Message>();

	private Exception exception = null;

	private int numMessages = 1;

	private int countMessages = 0;

	boolean complete = false;

	boolean gotCorrectException = false;

	boolean gotException = false;

	private static final Logger logger = (Logger) System.getLogger(MyCompletionListener.class.getName());

	public MyCompletionListener() {
		this("MyCompletionListener");
	}

	public MyCompletionListener(String name) {
		this.name = name;
	}

	public MyCompletionListener(int numMessages) {
		this.numMessages = numMessages;
		messages.clear();
	}

	public MyCompletionListener(Connection connection) {
		this.connection = connection;
	}

	public MyCompletionListener(Session session) {
		this.session = session;
	}

	// getters/setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Message getMessage() {
		return message;
	}

	public Message getMessage(int index) {
		return messages.get(index);
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean isComplete() {
		return complete;
	}

	public boolean gotCorrectException() {
		return gotCorrectException;
	}

	public boolean gotException() {
		return gotException;
	}

	public boolean gotAllMsgs() {
		return (messages.size() == numMessages) ? true : false;
	}

	public boolean haveMsg(int i) {
		return (messages.size() > i) ? true : false;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void onCompletion(Message message) {
		try {
			logger.log(Logger.Level.INFO, "onCompletion(): Got Message: " + ((TextMessage) message).getText());
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
		}
		this.message = message;
		messages.add(message);
		if (message instanceof TextMessage) {
			TextMessage tMsg = (TextMessage) message;
			try {
				if (tMsg.getText().equals("Call connection close method")) {
					logger.log(Logger.Level.INFO, "Calling Connection.close() MUST throw IllegalStateException");
					if (connection != null)
						connection.close();
				} else if (tMsg.getText().equals("Call session close method")) {
					logger.log(Logger.Level.INFO, "Calling Session.close() MUST throw IllegalStateException");
					if (session != null)
						session.close();
				}
			} catch (jakarta.jms.IllegalStateException e) {
				logger.log(Logger.Level.INFO, "onCompletion(): Caught expected IllegalStateException");
				gotCorrectException = true;
				gotException = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "onCompletion(): Caught unexpected exception: " + e);
				gotCorrectException = false;
				gotException = true;
				exception = e;
			}
		}
		complete = true;
		logger.log(Logger.Level.INFO, "onCompletion(): Leaving");
	}

	public void onException(Message message, Exception exception) {
		logger.log(Logger.Level.INFO, "Got Exception: " + exception);
		logger.log(Logger.Level.INFO, "With Message: " + message);
		this.exception = exception;
		this.message = message;
		complete = true;
	}

}
