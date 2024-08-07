/*
 * Copyright (c) 2013, 2024 Oracle and/or its affiliates and others.
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

/*
 * $Id$
 */
package com.sun.ts.tests.websocket.ee.jakarta.websocket.session;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServer")
public class WSTestServer {

	private static final Logger logger = System.getLogger(WSTestServer.class.getName());

	private static final Class<?>[] TEST_ARGS = { String.class, Session.class };

	private static final Class<?>[] TEST_ARGS_BYTEBUFFER = { ByteBuffer.class, Session.class };

	static String testName;

	@OnOpen
	public void init(Session session) throws IOException {
		session.getBasicRemote().sendText("========TCKTestServer opened");
		if (session.isOpen()) {
			session.getBasicRemote().sendText("========session from Server is open=TRUE");
		} else {
			session.getBasicRemote().sendText("========session from Server is open=FALSE");
		}
	}

	@OnMessage
	public void respondString(String message, Session session) {
		logger.log(Logger.Level.INFO,"TCKTestServer got String message: " + message);
    if (message.startsWith("testName=") && message.endsWith("Test")) {
      /*
       * The call to this method was triggered by a WebSocket message. If the container doesn't dispatch message
       * handling to a new thread (and nothing in the Jakarta WebSocket specification requires it to do that) then the
       * processing of the message that triggered this method call will not complete until the test completes. That will
       * cause problems if the test expects to receive additional WebSocket frames as they will not be processed until
       * this method returns.
       *
       * To avoid any issues such as those described above, run the test a separate thread.
       */
  		Runnable test = () -> {
    		try {
  				testName = message.substring(9);
  				Method method = WSTestServer.class.getMethod(testName, TEST_ARGS);
  				method.invoke(this, new Object[] { message, session });
    		} catch (InvocationTargetException ite) {
    			logger.log(Logger.Level.ERROR,"Cannot run method " + testName);
    			ite.printStackTrace();
    		} catch (NoSuchMethodException nsme) {
    			logger.log(Logger.Level.ERROR,"Test: " + testName + " does not exist");
    			nsme.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
  		};
  		new Thread(test).start();
    } else {
      try {
        session.getBasicRemote().sendText("========TCKTestServer received String:" + message);
        session.getBasicRemote().sendText("========TCKTestServer responds, please close your session");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
	}

	@OnMessage
	public void respondByte(ByteBuffer message, Session session) {
		String message_string = IOUtil.byteBufferToString(message);

		logger.log(Logger.Level.INFO,"TCKTestServer got ByteBuffer message: " + message_string);

    if (message_string.startsWith("testName=")) {
      /*
       * The call to this method was triggered by a WebSocket message. If the container doesn't dispatch message
       * handling to a new thread (and nothing in the Jakarta WebSocket specification requires it to do that) then the
       * processing of the message that triggered this method call will not complete until the test completes. That will
       * cause problems if the test expects to receive additional WebSocket frames as they will not be processed until
       * this method returns.
       *
       * To avoid any issues such as those described above, run the test a separate thread.
       */
      Runnable test = () -> {
    		try {
    			testName = message_string.substring(9);
    			Method method = WSTestServer.class.getMethod(testName, TEST_ARGS_BYTEBUFFER);
    			method.invoke(this, new Object[] { message, session });
    		} catch (InvocationTargetException ite) {
    			logger.log(Logger.Level.ERROR,"Cannot run method " + testName);
    			ite.printStackTrace();
    		} catch (NoSuchMethodException nsme) {
    			logger.log(Logger.Level.ERROR,"Test: " + testName + " does not exist");
    			nsme.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
      };
      new Thread(test).start();
    } else {
      ByteBuffer data = ByteBuffer.wrap(("========TCKTestServer received ByteBuffer: ").getBytes());
      ByteBuffer data1 = ByteBuffer.wrap(("========TCKTestServer responds: Message in bytes").getBytes());

      try {
        session.getBasicRemote().sendBinary(data);
        session.getBasicRemote().sendBinary(message);
        session.getBasicRemote().sendBinary(data1);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
	}

	@OnError
	public void onError(Session session, Throwable t) {
		try {
			session.getBasicRemote().sendText("========TCKTestServer onError");
			if (session.isOpen()) {
				session.getBasicRemote().sendText("========onError: session from Server is open=TRUE");
			} else {
				session.getBasicRemote().sendText("========onError: session from Server is open=FALSE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session) {
		logger.log(Logger.Level.INFO,"==From WSTestServer onClose(Session)==");
		try {
			session.getBasicRemote().sendText("========WSTestServer OnClose(Session)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void isOpenTest(String message, Session session) {
		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			if (session.isOpen()) {
				session.getBasicRemote().sendText("========session from Server is still open=TRUE");
			} else {
				session.getBasicRemote().sendText("========session from Server is still open=FALSE");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void getId1Test(String message, Session session) {
		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			session.getBasicRemote().sendText("========TCKTestServer responds: default getId=" + session.getId());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void close1Test(String message, Session session) {
		try {
			session.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void close2Test(String message, Session session) {
		try {
			session.close(new CloseReason(CloseReason.CloseCodes.TOO_BIG, "TCKCloseNowWithReason"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setTimeout1Test(String message, Session session) {
		logger.log(Logger.Level.INFO,"========In setMaxIdleTimeout1Test: TCKTestServer received String:" + message);
		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			session.getBasicRemote().sendText("========TCKTestServer default timeout: " + session.getMaxIdleTimeout());
			String query_string = session.getQueryString();
			int timeout = 0;
			if (query_string.startsWith("timeout")) {
				timeout = Integer.parseInt(query_string.substring(8));
				Thread.sleep(timeout * 8000);
				session.getBasicRemote()
						.sendText("========TCKTestServer second message after sleep " + timeout * 8 + " second");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setTimeout2Test(String message, Session session) {
		logger.log(Logger.Level.INFO,"========TCKTestServer received String:" + message);
		CountDownLatch messageLatch = new CountDownLatch(1);

		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			session.getBasicRemote().sendText("========TCKTestServer default timeout: " + session.getMaxIdleTimeout());

			String query_string = session.getQueryString();
			int timeout = 0;
			if (query_string.startsWith("timeout")) {
				timeout = Integer.parseInt(query_string.substring(8));
				session.setMaxIdleTimeout(timeout * 4000);
				session.getBasicRemote().sendText("========TCKTestServer set timeout to " + timeout * 4 + " second");
				session.getBasicRemote().sendText(
						"========TCKTestServer getTimeout return " + session.getMaxIdleTimeout() + " milliseconds");
				messageLatch.await(timeout * 6, TimeUnit.SECONDS);
				session.getBasicRemote()
						.sendText("========TCKTestServer second message after sleep " + timeout * 6 + " second");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getQueryStringTest(String message, Session session) {
		logger.log(Logger.Level.INFO,"========TCKTestServer received String:" + message);
		String expected_querystring = "test1=value1&test2=value2&test3=value3";

		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			String query_string = session.getQueryString();
			if (expected_querystring.equals(query_string)) {
				session.getBasicRemote()
						.sendText("========TCKTestServer: expected Query String returned" + query_string);
			} else {
				session.getBasicRemote()
						.sendText("========TCKTestServer: unexpected Query String returned" + query_string);
				session.getBasicRemote().sendText("========TCKTestServer: expecting " + expected_querystring);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getRequestURITest(String message, Session session) {
		logger.log(Logger.Level.INFO,"========TCKTestServer received String:" + message);

		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			URI requestURI = session.getRequestURI();
			session.getBasicRemote().sendText("========TCKTestServer: getRequestURI returned=" + requestURI.toString());
			session.getBasicRemote()
					.sendText("========TCKTestServer: getRequestURI returned queryString=" + requestURI.getQuery());
			session.getBasicRemote()
					.sendText("========TCKTestServer: getRequestURI returned scheme=" + requestURI.getScheme());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getProtocolVersionTest(String message, Session session) {
		logger.log(Logger.Level.INFO,"========TCKTestServer received String:" + message);

		try {
			session.getBasicRemote().sendText("========TCKTestServer received String: " + message);
			String protocolVersion = session.getProtocolVersion();
			session.getBasicRemote().sendText("========TCKTestServer: getProtocolVersion returned=" + protocolVersion);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setMaxBinaryMessageBufferSizeTest2(ByteBuffer message_b, Session session) {
		logger.log(Logger.Level.INFO,"In setMaxBinaryMessageBufferSizeTest2");
		String message = "Binary Message over size 64=0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		ByteBuffer data = ByteBuffer.wrap(("========TCKTestServer received ByteBuffer: ").getBytes());
		ByteBuffer data1 = ByteBuffer.wrap(("========TCKTestServer responds:").getBytes());
		ByteBuffer data2 = ByteBuffer.wrap(message.getBytes());

		try {
			session.getBasicRemote().sendBinary(data);
			session.getBasicRemote().sendBinary(message_b);
			session.getBasicRemote().sendBinary(data1);
			session.getBasicRemote().sendBinary(data2);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void setMaxTextMessageBufferSize2Test(String message_r, Session session) {
		logger.log(Logger.Level.INFO,"In setMaxTextMessageBufferSize2Test");
		String message = "String Message over size 64=0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		try {
			session.getBasicRemote().sendText("========TCKTestServer received String Message: ");
			session.getBasicRemote().sendText(message);
			session.getBasicRemote().sendText("========TCKTestServer responded");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void getOpenSessionsTest(String message, Session session) {
		logger.log(Logger.Level.INFO,"In getOpenSessionsTest");

		try {
			session.getBasicRemote().sendText("========TCKTestServer received String Message: ");
			session.getBasicRemote().sendText(message);
			Set<Session> tmp = session.getOpenSessions();
			session.getBasicRemote().sendText("========getOpenSessions=" + tmp.size());
			session.getBasicRemote().sendText("========TCKTestServer responded");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
