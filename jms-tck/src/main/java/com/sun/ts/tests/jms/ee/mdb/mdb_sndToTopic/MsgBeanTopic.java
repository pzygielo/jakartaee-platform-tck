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

package com.sun.ts.tests.jms.ee.mdb.mdb_sndToTopic;

import java.lang.System.Logger;
import java.util.Properties;

import javax.sql.DataSource;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.TopicConnectionFactory;

public class MsgBeanTopic implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into
	// the onMessage method.
	private java.util.Properties p = null;

	private TSNamingContext context = null;

	private MessageDrivenContext mdc = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanTopic.class.getName());

	// JMS
	private QueueConnectionFactory queueConFactory = null;

	private TopicConnectionFactory topicConFactory = null;

	private DataSource dataSource = null;

	private QueueConnectionFactory qcFactory = null;

	private QueueConnection connection = null;

	private Queue replyQueue = null;

	public MsgBeanTopic() {
		logger.log(Logger.Level.TRACE, "@MsgBeanTopic - @MsgBean()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "@MsgBeanTopic - @MsgBean-ejbCreate() !!");
		try {
			TSNamingContext context = new TSNamingContext();
			qcFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			replyQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			p = new Properties();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {

		JmsUtil.initHarnessProps(msg, p);
		logger.log(Logger.Level.TRACE, "@MsgBeanTopic - MsgBeanTopic - onMessage! " + msg);

		try {
			logger.log(Logger.Level.TRACE, "In MsgBeanForTopic::onMessage() : " + msg);
			logger.log(Logger.Level.TRACE, "calling sendReply");
			sendReply(msg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}

	}

	private void sendReply(Message msg) {
		try {
			connection = qcFactory.createQueueConnection();

			// get the reply to queue
			System.out.println("From sendReply");
			connection.start();
			System.out.println("started the connection");

			QueueSession session = connection.createQueueSession(true, 0);
			jakarta.jms.TextMessage reply = session.createTextMessage();
			QueueSender replier = session.createSender(replyQueue);
			reply.setText("MDB Responding to message receipt");
			reply.setStringProperty("MessageType", msg.getStringProperty("MessageType"));

			logger.log(Logger.Level.TRACE, "sending a msg to MDB_QUEUE_REPLY");
			replier.send(reply);
			System.out.println("Sent the message");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ee) {
					TestUtil.printStackTrace(ee);
				}
			}
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "In MsgBean::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "In MsgBean::remove()!!");
	}
}
