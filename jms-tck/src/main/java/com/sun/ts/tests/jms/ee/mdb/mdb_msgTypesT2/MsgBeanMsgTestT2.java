/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT2;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSession;

public class MsgBeanMsgTestT2 implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into
	// the onMessage method.
	private java.util.Properties p = null;

	private TSNamingContext context = null;

	private MessageDrivenContext mdc = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanMsgTestT2.class.getName());

	// JMS
	private QueueConnectionFactory qFactory = null;

	private QueueConnection qConnection = null;

	private Queue queueR = null;

	private QueueSender mSender = null;

	private QueueSession qSession = null;

	private TopicConnectionFactory tFactory = null;

	private TopicConnection tConnection = null;

	private Topic topic = null;

	private TopicPublisher tPublisher = null;

	private TopicSession tSession = null;

	public MsgBeanMsgTestT2() {
		logger.log(Logger.Level.TRACE, "@MsgBeanMsgTest2()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgTypesT2  - @MsgBeanMsgTest2-ejbCreate() !!");
		try {
			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			if (qFactory == null) {
				logger.log(Logger.Level.TRACE, "qFactory error");
			}
			logger.log(Logger.Level.TRACE, "got a qFactory !!");

			queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			if (queueR == null) {
				logger.log(Logger.Level.TRACE, "queueR error");
			}

			tFactory = (TopicConnectionFactory) context.lookup("java:comp/env/jms/MyTopicConnectionFactory");
			if (tFactory == null) {
				logger.log(Logger.Level.TRACE, "tFactory error");
			}
			logger.log(Logger.Level.TRACE, "got a tFactory !!");

			topic = (Topic) context.lookup("java:comp/env/jms/MDB_TOPIC");
			if (topic == null) {
				logger.log(Logger.Level.TRACE, "topic error");
			}

			p = new Properties();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {
		JmsUtil.initHarnessProps(msg, p);
		logger.log(Logger.Level.TRACE, "from jms.ee.mdb.mdb_msgTypesT2 @onMessage!" + msg);

		try {
			logger.log(Logger.Level.TRACE, "TestCase:" + msg.getStringProperty("TestCase"));
			logger.log(Logger.Level.TRACE, "onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
			qConnection = qFactory.createQueueConnection();
			if (qConnection == null) {
				logger.log(Logger.Level.TRACE, "connection error");
			} else {
				qConnection.start();
				qSession = qConnection.createQueueSession(true, 0);
			}

			tConnection = tFactory.createTopicConnection();
			if (tConnection == null) {
				logger.log(Logger.Level.TRACE, "connection error");
			} else {
				tConnection.start();
				tSession = tConnection.createTopicSession(true, 0);
			}

			if (msg.getStringProperty("TestCase").equals("messageObjectCopyTopicTestCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running messageObjectCopyTopicTestCreate - create the message");
				messageObjectCopyTopicTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("messageObjectCopyTopicTest")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running messageObjectCopyTopicTest - read and verify the message");
				messageObjectCopyTopicTest((jakarta.jms.ObjectMessage) msg);
			}

			if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsBooleanCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsBooleanCreate - create the message");
				streamMessageConversionTopicTestsBooleanCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsBoolean")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsBoolean - read and verify the message");
				streamMessageConversionTopicTestsBoolean((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsByteCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsByteCreate - create the message");
				streamMessageConversionTopicTestsByteCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsByte")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsByte - read and verify the message");
				streamMessageConversionTopicTestsByte((jakarta.jms.StreamMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsShortCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsShortCreate - create the message");
				streamMessageConversionTopicTestsShortCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsShort")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsShort - read and verify the message");
				streamMessageConversionTopicTestsShort((jakarta.jms.StreamMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsIntCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsIntCreate - create the message");
				streamMessageConversionTopicTestsIntCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsInt")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsInt - read and verify the message");
				streamMessageConversionTopicTestsInt((jakarta.jms.StreamMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsLongCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsLongCreate - create the message");
				streamMessageConversionTopicTestsLongCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsLong")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsLong - read and verify the message");
				streamMessageConversionTopicTestsLong((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsFloatCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsFloatCreate - create the message");
				streamMessageConversionTopicTestsFloatCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsFloat")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsFloat - read and verify the message");
				streamMessageConversionTopicTestsFloat((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsDoubleCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsDoubleCreate - create the message");
				streamMessageConversionTopicTestsDoubleCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsDouble")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsDouble - read and verify the message");
				streamMessageConversionTopicTestsDouble((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsStringCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsStringCreate - create the message");
				streamMessageConversionTopicTestsStringCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsString")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsString - read and verify the message");
				streamMessageConversionTopicTestsString((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsCharCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsCharCreate - create the message");
				streamMessageConversionTopicTestsCharCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsChar")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsChar - read and verify the message");
				streamMessageConversionTopicTestsChar((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsBytesCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsBytesCreate - create the message");
				streamMessageConversionTopicTestsBytesCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsBytes")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsBytes - read and verify the message");
				streamMessageConversionTopicTestsBytes((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase")
					.equals("streamMessageConversionTopicTestsInvFormatStringCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsInvFormatStringCreate - create the message");
				streamMessageConversionTopicTestsInvFormatStringCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageConversionTopicTestsInvFormatString")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageConversionTopicTestsInvFormatString - read and verify the message");
				streamMessageConversionTopicTestsInvFormatString((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageTopicTestsFullMsgCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageTopicTestsFullMsgCreate - create the message");
				streamMessageTopicTestsFullMsgCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageTopicTestsFullMsg")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageTopicTestsFullMsg - read and verify the message");
				streamMessageTopicTestsFullMsg((jakarta.jms.StreamMessage) msg);
			}

			else if (msg.getStringProperty("TestCase").equals("streamMessageTopicTestNullCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageTopicTestNullCreate - create the message");
				streamMessageTopicTestNullCreate();
			} else if (msg.getStringProperty("TestCase").equals("streamMessageTopicTestNull")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running streamMessageTopicTestNull - read and verify the message");
				streamMessageTopicTestNull((jakarta.jms.StreamMessage) msg);
			} else {
				logger.log(Logger.Level.TRACE, "@onMessage - invalid message type found in StringProperty");
			}
			logger.log(Logger.Level.TRACE, "@onMessage - Finished for this test!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			if (qConnection != null) {
				try {
					qConnection.close();
				} catch (Exception e) {
					TestUtil.printStackTrace(e);
				}
			}
			if (tConnection != null) {
				try {
					tConnection.close();
				} catch (Exception e) {
					TestUtil.printStackTrace(e);
				}
			}
		}

	}

	/*
	 * Description: Create an object message. Write a StringBuffer to the message.
	 * modify the StringBuffer and send the msg, verify that it does not effect the
	 * msg
	 */
	public void messageObjectCopyTopicTestCreate() {
		boolean pass = true;
		try {
			ObjectMessage messageSentObjectMsg = null;
			StringBuffer sBuff = new StringBuffer("This is");
			String initial = "This is";
			messageSentObjectMsg = qSession.createObjectMessage();
			JmsUtil.addPropsToMessage(messageSentObjectMsg, p);
			messageSentObjectMsg.setObject(sBuff);
			sBuff.append("a test ");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "messageObjectCopyTopicTest");
			// set up testcase so onMessage invokes the correct method
			messageSentObjectMsg.setStringProperty("TestCase", "messageObjectCopyTopicTest");

			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSentObjectMsg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage -. use StreamMessage method writeBoolean
	 * to write a boolean to the message. Verify the proper conversion support as in
	 * 3.11.3
	 */
	private void streamMessageConversionTopicTestsBooleanCreate() {
		try {
			StreamMessage messageSent = null;
			boolean abool = true;

			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsBoolean");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for boolean primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsBoolean");
			messageSent.writeBoolean(abool);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage -. use StreamMessage method writeByte to
	 * write a byte. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsByteCreate() {
		try {
			StreamMessage messageSent = null;

			byte bValue = 127;
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsByte");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsByte");

			messageSent.writeByte(bValue);
			// send the message and then get it back
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a StreamMessage -. use StreamMessage method writeShort to
	 * write a short. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsShortCreate() {
		try {
			StreamMessage messageSent = null;
			short sValue = 1;
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsShort");
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsShort");
			messageSent.writeShort(sValue);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a StreamMessage -. use StreamMessage method writeInt to
	 * write an int. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsIntCreate() {
		try {
			StreamMessage messageSent = null;
			int iValue = 6;

			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsInt");
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsInt");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			messageSent.writeInt(iValue);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a StreamMessage -. use StreamMessage method writeLong to
	 * write a long. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsLongCreate() {
		try {
			StreamMessage messageSent = null;
			long lValue = 2;
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsLong");
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsLong");
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			messageSent.writeLong(lValue);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a StreamMessage -. use StreamMessage method writeFloat to
	 * write a float. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsFloatCreate() {
		try {
			StreamMessage messageSent = null;

			float fValue = 5;
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsFloat");
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsFloat");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.writeFloat(fValue);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage -. use StreamMessage method writeDouble
	 * to write a double. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsDoubleCreate() {
		try {
			StreamMessage messageSent = null;

			double dValue = 3;
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsDouble");
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsDouble");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.writeDouble(dValue);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a StreamMessage -. use StreamMessage method writeString
	 * to write a string. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsStringCreate() {
		try {
			StreamMessage messageSent = null;
			String myString = "10";

			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsString");
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.writeString(myString);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a StreamMessage -. use StreamMessage method writeChar to
	 * write a char. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsCharCreate() {
		try {
			StreamMessage messageSent = null;

			char charValue = 'a';
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsChar");
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsChar");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.writeChar(charValue);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage -. use StreamMessage method writeBytes to
	 * write a byte[] to the message. Verify the proper conversion support as in
	 * 3.11.3
	 */
	private void streamMessageConversionTopicTestsBytesCreate() {
		try {
			StreamMessage messageSent = null;
			byte[] bValues = { 1, 2, 3 };
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsBytes");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte[] primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsBytes");

			messageSent.writeBytes(bValues);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage -. use StreamMessage method writeString
	 * to write a text string of "mytest string". Verify NumberFormatException is
	 * thrown Verify that the pointer was not incremented by doing a read string
	 * 
	 */
	private void streamMessageConversionTopicTestsInvFormatStringCreate() {
		try {
			StreamMessage messageSent = null;
			String myString = "mytest string";
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionTopicTestsInvFormatString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageConversionTopicTestsInvFormatString");
			messageSent.writeString(myString);
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage -. write one of each primitive type. Send
	 * the message. Verify the data received was as sent.
	 * 
	 */
	private void streamMessageTopicTestsFullMsgCreate() {
		try {

			StreamMessage messageSent = null;

			byte bValue = 127;
			boolean abool = false;
			byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
			char charValue = 'Z';
			short sValue = 32767;
			long lValue = 9223372036854775807L;
			double dValue = 6.02e23;
			float fValue = 6.02e23f;
			int iValue = 6;
			boolean pass = true;
			String myString = "text";
			String sTesting = "Testing StreamMessages";

			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageTopicTestsFullMsg");

			messageSent.writeBytes(bValues2, 0, bValues.length);
			messageSent.writeBoolean(abool);
			messageSent.writeByte(bValue);
			messageSent.writeBytes(bValues);
			messageSent.writeChar(charValue);
			messageSent.writeDouble(dValue);
			messageSent.writeFloat(fValue);
			messageSent.writeInt(iValue);
			messageSent.writeLong(lValue);
			messageSent.writeObject(sTesting);
			messageSent.writeShort(sValue);
			messageSent.writeString(myString);

			messageSent.writeObject(null);
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageTopicTestsFullMsg");

			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a StreamMessage Use writeString to write a null, then use
	 * readString to read it back.
	 */
	private void streamMessageTopicTestNullCreate() {
		try {
			StreamMessage messageSent = null;
			messageSent = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageTopicTestNull");
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "writeString(null) ");
			// -----------------------------------------------------------------------------
			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "streamMessageTopicTestNull");
			messageSent.writeString(null);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tPublisher = tSession.createPublisher(topic);
			tPublisher.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read ObjectMessage created by messageObjectCopyTopicTestCreate.
	 * verify that modifying the sBuff object after the write does not effect the
	 * msg
	 */
	public void messageObjectCopyTopicTest(jakarta.jms.ObjectMessage messageReceivedObjectMsg) {
		boolean pass = true;
		String testCase = "messageObjectCopyTopicTest";
		String initial = "This is";
		try {
			logger.log(Logger.Level.INFO, "Ensure that changing the object did not change the message");
			StringBuffer s = (StringBuffer) messageReceivedObjectMsg.getObject();
			logger.log(Logger.Level.TRACE, "s is " + s);
			if (s.toString().equals(initial)) {
				logger.log(Logger.Level.TRACE, "Pass: msg was not changed");
			} else {
				logger.log(Logger.Level.TRACE, "Fail: msg was changed!");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 */
	private void streamMessageConversionTopicTestsBoolean(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsBoolean";
		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			boolean pass = true;

			// now test conversions for boolean
			// -----------------------------------------------
			// boolean to boolean - valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a boolean");
			try {
				if (messageReceived.readBoolean() == abool) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to boolean - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a boolean");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals((Boolean.valueOf(abool)).toString())) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to byte[] invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a boolean - expect MessageFormatException");
			int nCount = 0;
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);
			// -----------------------------------------------
			// boolean to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				bValue = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// boolean to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;

			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// boolean to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// boolean to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readLong();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// boolean to float invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;

			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// boolean to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a boolean - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;

			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsByte(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsByte";
		try {
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;
			// now test conversions for byte
			// -----------------------------------------------
			// byte to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a byte");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Byte.toString(bValue))) {
					logger.log(Logger.Level.TRACE, "Pass: byte to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a byte - expect MessageFormatException");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// byte to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a byte");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readByte() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to byte - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a byte");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readShort() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to short - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				pass = false;
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a byte");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to int - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a byte");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsShort(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsShort";
		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// short to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a short - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// short to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a short");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Short.toString(sValue))) {
					logger.log(Logger.Level.TRACE, "Pass: short to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// short to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a short - expect MessageFormatException");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// short to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a short - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// short to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a short");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readShort() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to short - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a short - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// short to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a byte");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to int - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a short");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a short - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// short to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a short - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsInt(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsInt";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// int to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read an int - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// int to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read an int");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Integer.toString(iValue))) {
					logger.log(Logger.Level.TRACE, "Pass: int to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// int to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read an int - expect MessageFormatException");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// int to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read an int - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// int to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read an int");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read an int - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// int to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read an int");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == iValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to int - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read an int");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == iValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read an int - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read an int - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 */
	private void streamMessageConversionTopicTestsLong(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsLong";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// long to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a long - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// long to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a long");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Long.toString(lValue))) {
					logger.log(Logger.Level.TRACE, "Pass: long to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// long to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read  a long - expect MessageFormatException");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// long to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read an long - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// long to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a long");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a long - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// long to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a long");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// long to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a long");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == lValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a long - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read an long - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 */
	private void streamMessageConversionTopicTestsFloat(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsFloat";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// float to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a float  ");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// float to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a float");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Float.toString(fValue))) {
					logger.log(Logger.Level.TRACE, "Pass: float to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read  a float ");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// float to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a float  ");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// float to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a float");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a long  ");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// float to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a float");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// float to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a long");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// float to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a float  ");
			try {
				messageReceived.reset();
				if (messageReceived.readFloat() == fValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to float - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read an float  ");
			try {
				messageReceived.reset();
				if (messageReceived.readDouble() == fValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to double - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsDouble(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsDouble";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// double to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a double  ");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// double to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a double");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Double.toString(dValue))) {
					logger.log(Logger.Level.TRACE, "Pass: double to string");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read  a double ");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// double to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a double  ");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// double to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a double");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a double  ");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// double to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a double");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// double to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a double");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a double  ");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// double to double valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a double  ");
			try {
				messageReceived.reset();
				if (messageReceived.readDouble() == dValue) {
					logger.log(Logger.Level.TRACE, "Pass: double to double ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 */
	private void streamMessageConversionTopicTestsString(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsString";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;
			String myString = "10";

			// now test conversions for String
			// -----------------------------------------------
			// string to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a String");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: string to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// string to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a String");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// String to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a String");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readByte() == Byte.parseByte(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to byte ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// string to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a string");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readShort() == Short.parseShort(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to short ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a String ");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// string to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a String");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == Integer.parseInt(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to int ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a String");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == Long.parseLong(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to long ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a String");
			try {
				messageReceived.reset();
				if (messageReceived.readFloat() == Float.parseFloat(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to float ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// String to double valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a String");
			try {
				messageReceived.reset();
				if (messageReceived.readDouble() == Double.parseDouble(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to double ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a string ");
			try {
				messageReceived.clearBody();
				messageReceived.writeString("true");
				messageReceived.reset();
				if (messageReceived.readBoolean() == abool) {
					logger.log(Logger.Level.TRACE, "Pass: String to boolean ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a string  that is !true ");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				if (b != true) {
					logger.log(Logger.Level.TRACE, "Fail: !true should return false");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: !true returned false");
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 * 
	 */
	private void streamMessageConversionTopicTestsChar(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsChar";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// char to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a char - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// char to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a char");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				String s = messageReceived.readString();
				logger.log(Logger.Level.TRACE, "char returned for \"a\" is : " + s);
				if (s.equals("a")) {
					logger.log(Logger.Level.TRACE, "Pass: char to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a char - expect MessageFormatException");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// char to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a char - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// char to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a char");
			try {
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to char valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a char ");
			try {
				messageReceived.reset();
				if (messageReceived.readChar() == 'a') {
					logger.log(Logger.Level.TRACE, "Pass: char to char - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a char ");
			try {
				messageReceived.reset();
				int i = messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a char");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a char - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// char to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a char - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the proper conversion support as
	 * in 3.11.3
	 */
	private void streamMessageConversionTopicTestsBytes(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsBytes";

		try {

			byte bValue = 127;
			boolean abool = true;
			byte[] bValues2 = { 0, 0, 0 };
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// now test conversions for boolean
			// -----------------------------------------------
			// byte[] to byte[] - valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a byte[] ");
			int nCount = 0;
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readBytes(bValues2) == 3) { // count should be 3.
					logger.log(Logger.Level.TRACE, "Pass: byte[] to byte[] - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: count incorrect");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a byte[]");
			// position to beginning of stream message.
			messageReceived.reset();
			try {
				boolean b = messageReceived.readBoolean();
				logger.log(Logger.Level.TRACE,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to string invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a byte[]");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				String s = messageReceived.readString();
				logger.log(Logger.Level.TRACE,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				char c = messageReceived.readChar();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to float invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// byte[] to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a byte[] - expect MessageFormatException");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Read a StreamMessage -. Verify NumberFormatException is thrown
	 * Verify that the pointer was not incremented by doing a read string
	 * 
	 */
	private void streamMessageConversionTopicTestsInvFormatString(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageConversionTopicTestsInvFormatString";

		try {

			boolean pass = true;
			String myString = "mytest string";

			// -----------------------------------------------
			// String to byte
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a String that is not valid ");
			try {
				byte b = messageReceived.readByte();
				logger.log(Logger.Level.TRACE, "Fail: java.lang.NumberFormatException expected");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// pointer should not have moved
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Verify that the data can be read as a string and pointer did not move");
			try {
				String s = messageReceived.readString();
				logger.log(Logger.Level.TRACE, "message read: " + s);
				if (s.equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: able to read the string");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: string not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to short
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a string that is not valid ");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// string to int
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a String that is not valid ");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// string to long
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a String that is not valid ");
			try {
				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// String to float
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a String that is not valid ");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			// -----------------------------------------------
			// String to double
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a String that is not valid ");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.TRACE,
							"Error: Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage -. Verify the data received was as sent.
	 * 
	 */
	private void streamMessageTopicTestsFullMsg(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageTopicTestsFullMsg";

		try {
			byte bValue = 127;
			boolean abool = false;
			byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
			byte[] bValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			byte[] bValuesReturned2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			char charValue = 'Z';
			short sValue = 32767;
			long lValue = 9223372036854775807L;
			double dValue = 6.02e23;
			float fValue = 6.02e23f;
			int iValue = 6;
			boolean pass = true;
			String myString = "text";
			String sTesting = "Testing StreamMessages";

			try {
				int nCount = bValuesReturned2.length;
				do {
					nCount = messageReceived.readBytes(bValuesReturned2);
					logger.log(Logger.Level.TRACE, "nCount is " + nCount);
					if (nCount != -1) {
						for (int i = 0; i < bValuesReturned2.length; i++) {
							if (bValuesReturned2[i] != bValues2[i]) {
								logger.log(Logger.Level.TRACE, "Fail: byte[] " + i + " is not valid");
								pass = false;
							} else {
								logger.log(Logger.Level.TRACE, "PASS: byte[]" + i + " is valid");
							}
						}
					}
				} while (nCount >= bValuesReturned2.length);
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readBoolean() == abool) {
					logger.log(Logger.Level.TRACE, "Pass: boolean returned ok");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: boolean not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readByte() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: Byte returned ok");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: Byte not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				int nCount = bValuesReturned.length;
				do {
					nCount = messageReceived.readBytes(bValuesReturned);
					logger.log(Logger.Level.TRACE, "nCount is " + nCount);
					if (nCount != -1) {
						for (int i = 0; i < bValuesReturned2.length; i++) {
							if (bValuesReturned2[i] != bValues2[i]) {
								logger.log(Logger.Level.TRACE, "Fail: byte[] " + i + " is not valid");
								pass = false;
							} else {
								logger.log(Logger.Level.TRACE, "PASS: byte[]" + i + " is valid");
							}
						}
					}
				} while (nCount >= bValuesReturned.length);

			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readChar() == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct char");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: char not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readDouble() == dValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct double");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: double not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readFloat() == fValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct float");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: float not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readInt() == iValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct int");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: int not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readLong() == lValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct long");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: long not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readObject().equals(sTesting)) {
					logger.log(Logger.Level.TRACE, "Pass: correct object");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: object not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readShort() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct short");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: short not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readString().equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: correct string");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: string not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readObject() == null) {
					logger.log(Logger.Level.TRACE, "Pass: correct object");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: object not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Read a StreamMessage Use readString to read back a null
	 */
	private void streamMessageTopicTestNull(jakarta.jms.StreamMessage messageReceived) {
		String testCase = "streamMessageTopicTestNull";
		try {
			boolean pass = true;
			try {
				if (messageReceived.readObject() == null) {
					logger.log(Logger.Level.TRACE, "Pass: Read a null");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: null value not returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error trying to read a null object");
				logger.log(Logger.Level.TRACE, "Error: unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: send test results to response queue (MDB_QUEUE_REPLY) for
	 * verification
	 */
	private void sendTestResults(String testCase, boolean results) {
		TextMessage msg = null;

		try {
			// create a msg sender for the response queue
			mSender = qSession.createSender(queueR);
			// and we'll send a text msg
			msg = qSession.createTextMessage();
			msg.setStringProperty("TestCase", testCase);
			msg.setText(testCase);
			if (results)
				msg.setStringProperty("Status", "Pass");
			else
				msg.setStringProperty("Status", "Fail");

			logger.log(Logger.Level.TRACE, "Sending response message");
			mSender.send(msg);
		} catch (JMSException je) {
			TestUtil.printStackTrace(je);
			logger.log(Logger.Level.TRACE, "Error: " + je.getClass().getName() + " was thrown");
		} catch (Exception ee) {
			TestUtil.printStackTrace(ee);
			logger.log(Logger.Level.TRACE, "Error: " + ee.getClass().getName() + " was thrown");
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgTypesT2  In MsgBeanMsgTest2::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgTypesT2  In MsgBeanMsgTest2::remove()!!");
	}
}
