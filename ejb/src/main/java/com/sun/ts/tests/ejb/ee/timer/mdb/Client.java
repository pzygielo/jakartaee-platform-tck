/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.5 03/06/04
 */

package com.sun.ts.tests.ejb.ee.timer.mdb;

import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb.ee.timer.common.TimerImpl;

import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class Client extends EETest {

  private static final String sendQueueLookup = "java:comp/env/jms/SendQueue";

  private static final String recvQueueLookup = "java:comp/env/jms/RecvQueue";

  private static final String factoryLookup = "java:comp/env/jms/MyQueueConnectionFactory";

  private Properties props = new Properties();

  private TSNamingContext jctx;

  private Queue sendQueue, recvQueue;

  private QueueConnection qConnect;

  private QueueConnectionFactory qFactory;

  private QueueSender qSender;

  private QueueReceiver qReceiver;

  private QueueSession session;

  private long ejbTimeout;

  private long messageTimeout;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user; password; harness.log.traceflag; harness.log.port; ejb_timeout;
   * ejb_wait;
   */
  public void setup(String[] args, Properties p) throws Fault {
    logMsg("Setup tests");
    this.props = p;
    logMsg("ejb_timeout is " + props.getProperty("ejb_timeout"));
    logMsg("ejb_wait is " + props.getProperty("ejb_wait"));

    try {

      jctx = new TSNamingContext();
      logMsg("initializing JMS messaging");
      sendQueue = (Queue) jctx.lookup(sendQueueLookup);
      recvQueue = (Queue) jctx.lookup(recvQueueLookup);
      qFactory = (QueueConnectionFactory) jctx.lookup(factoryLookup);

      qConnect = qFactory.createQueueConnection();
      session = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      qConnect.start();

      ejbTimeout = Long.parseLong(TestUtil.getProperty("ejb_timeout"));
      messageTimeout = Long.parseLong(TestUtil.getProperty("ejb_wait"));
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: getInfoStrAndCancelSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:SPEC:10506;
   * EJB:JAVADOC:27; EJB:JAVADOC:214; EJB:JAVADOC:197; EJB:JAVADOC:210;
   * EJB:JAVADOC:198; EJB:JAVADOC:190
   *
   * @test_Strategy: Create a message-driven bean implementing the TimedObject
   * interface. Create a single-event timer for the bean using a String for the
   * info parameter. Call the timer's getInfo() method. Verify that the info
   * returned is identical to that used to create the timer. Call the timer's
   * cancel() method. Verify that no exception occurs. API tested:
   * EJBContext.getTimerService() TimerService.createTimer(long, Serializable)
   * Timer.getHandle() TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoStrAndCancelSingleEventTest() throws Fault {
    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:getInfoStrAndCancel");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT,
          "getInfoStrAndCancel");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TestUtil.getProperty("testName"));

      if (!testResult) {
        logMsg(message);
        throw new Fault("getInfoStrAndCancelSingleEventTest failed");
      } else
        logMsg("getInfoStrAndCancelSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoStrAndCancelSingleEventTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: getInfoClassAndCancelSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:SPEC:10506;
   *
   * @test_Strategy: Create a message-driven bean implementing the TimedObject
   * interface. Create a single-event timer for the bean using an
   * application-specific class for the info parameter. Call the timer's
   * getInfo() method. Verify that the object returned is identical to that used
   * to create the timer. Call the timer's cancel() method. Verify that no
   * exception occurs. API tested: TimerService.createTimer(long, long,
   * Serializable) Timer.getHandle() TimerHandle.getTimer() Timer.getInfo()
   * Timer.cancel()
   */
  public void getInfoClassAndCancelSingleEventTest() throws Fault {
    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:getInfoClassAndCancel");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT,
          "getInfoClassAndCancel");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TestUtil.getProperty("testName"));

      if (!testResult) {
        logMsg(message);
        throw new Fault("getInfoClassAndCancelSingleEventTest failed");
      } else
        logMsg("getInfoClassAndCancelSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoClassAndCancelSingleEventTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: verifyTimeoutCallSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:858; EJB:JAVADOC:189; EJB:SPEC:10506
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, invoke a JMS send. Create a single-event timer. In
   * the application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message from ejbTimeout was received. API
   * tested: TimerService.createTimer(long, Serializable) Timer.getHandle()
   */
  public void verifyTimeoutCallSingleEventTest() throws Fault {

    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:verifyTimeoutCall");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT, "verifyTimeoutCall");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TimerImpl.ACCESS_OK);

      if (!testResult) {
        logMsg(message);
        throw new Fault("verifyTimeoutCallSingleEventTest failed");
      } else
        logMsg("verifyTimeoutCallSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("verifyTimeoutCallSingleEventTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: checkedMethodAccessSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:827; EJB:JAVADOC:189; EJB:SPEC:10506
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface with a
   * run-as security identity. Create a bean with a checked method that permits
   * the identity. In the ejbTimeout method, call the checked method. Report the
   * results of the call with a JMS send(). Create a single-event timer. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Check the result in the message. API tested:
   * TimerService.createTimer(long, Serializable) Timer.getHandle()
   */
  public void checkedMethodAccessSingleEventTest() throws Fault {

    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:checkedMethodAccess");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT,
          "checkedMethodAccess");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TimerImpl.CHKMETH_OK);

      if (!testResult) {
        logMsg(message);
        throw new Fault("checkedMethodAccessSingleEventTest failed");
      } else
        logMsg("checkedMethodAccessSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("checkedMethodAccessSingleEventTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: checkSerializationSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:852.1; EJB:SPEC:10506
   *
   * @test_Strategy: Create a message-driven bean implementing the TimedObject
   * interface. Create a single-event timer and get the timer handle. Verify
   * that the timer handle is serializable. API tested:
   * TimerService.createTimer(long, Serializable) Timer.getHandle()
   */
  public void checkSerializationSingleEventTest() throws Fault {

    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:checkSerialization");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT,
          "checkSerialization");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TestUtil.getProperty("testName"));

      if (!testResult) {
        logMsg(message);
        throw new Fault("checkSerializationSingleEventTest failed");
      } else
        logMsg("checkSerializationSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationSingleEventTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: checkSerializationInEjbTimeoutSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:845; EJB:SPEC:847; EJB:SPEC:852.1; EJB:SPEC:852.2;
   * EJB:JAVADOC:189; EJB:SPEC:10506
   *
   * @test_Strategy: Create a message-driven bean implementing the TimedObject
   * interface. Create a single-event timer. In the ejbTimeout method, get the
   * timer handle. Serialize and deserialize the handle. Use the handle to get a
   * reference to a timer, and verify that the timer is identical to the one
   * created. API tested: TimerService.createTimer(long, Serializable)
   * Timer.getHandle() Timer.equals(Timer)
   */
  public void checkSerializationInEjbTimeoutSingleEventTest() throws Fault {

    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:checkSerializationInEjbTimeout");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT,
          "checkSerializationInEjbTimeout");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TimerImpl.SERIALIZE_OK);

      if (!testResult) {
        logMsg(message);
        throw new Fault("checkSerializationInEjbTimeoutSingleEventTest failed");
      } else
        logMsg("checkSerializationInEjbTimeoutSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationInEjbTimeoutSingleEventTest failed",
          e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: goneOnExpirationSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:863.1; EJB:SPEC:10506; EJB:JAVADOC:189
   * 
   *
   * @test_Strategy: Create a message-driven bean implementing the TimedObject
   * interface. In the ejbTimeout method, send a JMS message. Create a
   * single-event timer. In the application client, block on a JMS receive for a
   * period longer than the timer's duration. Verify that the message is
   * received. Verify that the timer is gone by checking that no timers exist.
   * API tested: TimerService.createTimer(long, Serializable) Note:
   * EJB:SPEC:863.2 is not testable for message- driven beans.
   */
  public void goneOnExpirationSingleEventTest() throws Fault {

    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:initializeTimerAndNotify");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_SINGLEEVENT,
          "initializeTimerAndNotify");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TestUtil.getProperty("testName"));

      if (testResult) {
        message = TimerImpl.getMessage(qReceiver, messageTimeout);
        testResult = message.equals(TimerImpl.ACCESS_OK);

        if (testResult) {
          // Sleep in case container hasn't had time to remove timer
          Thread.currentThread().sleep((messageTimeout - ejbTimeout) / 2);

          sendMdbMessage(qSender, 0, "verifyTimerIsGone");
          message = TimerImpl.getMessage(qReceiver, messageTimeout);
          testResult = message.equals(TimerImpl.NOTIMER_FOUND);
        }
      }

      if (!testResult) {
        logMsg(message);
        throw new Fault("goneOnExpirationSingleEventTest failed");
      } else
        logMsg("goneOnExpirationSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("goneOnExpirationSingleEventTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  /*
   * @testName: goneOnCancellationIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:867.1; EJB:SPEC:10506
   *
   * @test_Strategy: Create a message-driven bean implementing the TimedObject
   * interface. In the ejbTimeout method, send a JMS message. Create an interval
   * timer within a transaction. In a separate transaction, cancel the timer
   * Verify that the timer is gone by checking that no timers exist. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that no message is received. API tested:
   * TimerService.createTimer(long, long, Serializable) Timer.cancel() Note:
   * EJB:SPEC:867.2 is not testable for message- driven beans.
   */
  public void goneOnCancellationIntervalTest() throws Fault {

    try {
      boolean testResult = false;

      logMsg("Execute MsgBean:initializeTimerAndNotify");
      qSender = session.createSender(sendQueue);
      sendMdbMessage(qSender, TimerImpl.TIMER_INTERVAL,
          "initializeTimerAndNotify");
      qReceiver = session.createReceiver(recvQueue);
      String message = TimerImpl.getMessage(qReceiver, messageTimeout);
      testResult = message.equals(TestUtil.getProperty("testName"));

      if (testResult) {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
        Thread.currentThread().sleep(ejbTimeout);
        sendMdbMessage(qSender, 0, "verifyTimerIsGone");
        message = TimerImpl.getMessage(qReceiver, messageTimeout);
        testResult = message.equals(TimerImpl.NOTIMER_FOUND);

        if (testResult) {
          message = TimerImpl.getMessage(qReceiver, messageTimeout);
          testResult = message.equals(TimerImpl.NOMSGRECEIVED);
        }
      }

      if (!testResult) {
        logMsg(message);
        throw new Fault("goneOnCancellationIntervalTest failed");
      } else
        logMsg("goneOnCancellationIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("goneOnCancellationIntervalTest failed", e);
    } finally {
      try {
        sendMdbMessage(qSender, 0, "cancelAllTimers");
      } catch (Exception e) {
        TimerImpl.handleException("Error in finally block", e);
      }
    }
  }

  public void cleanup() throws Fault {

    if (qConnect != null) {
      try {
        logMsg("closing connection");
        qConnect.close();
      } catch (Exception e) {
        TimerImpl.handleException("Error close connection", e);
      }
    }
    try {
      TimerImpl.flushQueue(recvQueue, qFactory);
    } catch (Exception e) {
      TimerImpl.handleException("Error cleanup Queue", e);
    }
    logMsg("cleanup ok");
  }

  private void sendMdbMessage(QueueSender sender, int timerType,
      String message) {

    try {
      TextMessage msg = session.createTextMessage();

      addPropsToMessage(msg, timerType);
      msg.setText(message);
      logMsg(
          "Sending message at " + System.currentTimeMillis() + ": " + message);
      sender.send(msg);
    } catch (Exception e) {
      TimerImpl.handleException("Error sending message", e);
    }
  }

  private void addPropsToMessage(Message message, int timerType) {

    Enumeration e;
    Properties props = null;
    String key = null;
    String notValid = ".";

    try {
      message.setIntProperty("timer_type", timerType);

      props = TestUtil.getProperties();
      message.setStringProperty("harnesshost",
          props.getProperty("harness.host"));
      message.setStringProperty("harnesslogtraceflag",
          props.getProperty("harness.log.traceflag"));
      message.setStringProperty("harnesslogport",
          props.getProperty("harness.log.port"));
      message.setStringProperty("testName", props.getProperty("testName"));
      e = props.propertyNames();
      key = null;
      while (e.hasMoreElements()) {
        key = (String) e.nextElement();
        if ((key.indexOf(notValid) == -1) && (key.indexOf("***") == -1)) {
          message.setStringProperty(key, props.getProperty(key));
        }
      }

    } catch (Exception ee) {
      TestUtil.printStackTrace(ee);
      TestUtil.logMsg("key was: " + key);
      TestUtil.logMsg("props was: " + props.getProperty(key));
      TestUtil.logMsg("Error setting properties");
    }
  }
}
