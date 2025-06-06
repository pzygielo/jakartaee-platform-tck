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
package com.sun.ts.tests.jms.core.closedQueueConnection;

import java.util.ArrayList;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.MessageTestImpl;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TextMessage;

/**
 * JMS TS tests. Testing method calls on closed QueueConnection objects.
 */
public class ClosedQueueConnectionTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient JmsTool tool = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  boolean jmsOptionalApisSupported;

  ArrayList queues = null;

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ClosedQueueConnectionTests theTests = new ClosedQueueConnectionTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed connection for testing. Passes any
   * exceptions up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseConnection(int type) throws Exception {
    if ((type == JmsTool.QUEUE) || (type == JmsTool.TX_QUEUE)) {
      tool = new JmsTool(type, user, password, mode);
      tool.getDefaultQueueConnection().start();

      logTrace("Closing queue Connection");
      tool.getDefaultQueueConnection().close();
    }
    logTrace("Connection closed");
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default
   * QueueConnection, as well as a default Queue. Tests that require multiple
   * Destinations create the extras within the test
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */

  public void setup(String[] args, Properties p) throws Exception {
    try {

      // get props
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'users' in ts.jte must not be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      queues = new ArrayList(2);

      // get ready for new test
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * Closes the default connections that are created by setup(). Any separate
   * connections made by individual tests should be closed by that test.
   * 
   * @exception Fault
   */

  public void cleanup() throws Exception {
    try {
      if (tool != null) {
        logMsg("Cleanup: Closing QueueConnections");
        tool.doClientQueueTestCleanup(connections, queues);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Exception("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: closedQueueConnectionCommitTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:SPEC:113; JMS:JAVADOC:270;
   * JMS:JAVADOC:526; JMS:JAVADOC:274; JMS:JAVADOC:229;
   *
   * @test_Strategy: Close default connection and call the commit method on
   * session associated with it. Check for IllegalStateException.
   */

  public void closedQueueConnectionCommitTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TX_QUEUE);
      logTrace("Try to call commit with closed connection.");
      try {
        tool.getDefaultQueueSession().commit();
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueConnectionCommitTest");
    }
  }

  /*
   * @testName: closedQueueConnectionGetTransactedTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:SPEC:113; JMS:JAVADOC:270;
   * JMS:JAVADOC:526; JMS:JAVADOC:225; JMS:JAVADOC:274;
   *
   * @test_Strategy: Close default connection and call the getTransacted method
   * on a QueueSession associated with it. Check for IllegalStateException.
   */

  public void closedQueueConnectionGetTransactedTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getTransacted() with closed connection.");
      try {
        boolean b = tool.getDefaultQueueSession().getTransacted();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetTransactedTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionRollbackTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:SPEC:113; JMS:JAVADOC:270;
   * JMS:JAVADOC:526; JMS:JAVADOC:274; JMS:JAVADOC:231;
   *
   * @test_Strategy: Close default connection and call the rollback method on a
   * QueueSession associated with it. Check for IllegalStateException.
   */

  public void closedQueueConnectionRollbackTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call rollback() with closed connection.");
      try {
        tool.getDefaultQueueSession().rollback();
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionRollbackTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionRecoverTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:SPEC:113; JMS:JAVADOC:270;
   * JMS:JAVADOC:526; JMS:JAVADOC:274; JMS:JAVADOC:235;
   *
   * @test_Strategy: Close default connection and call the recover method on a
   * QueueSession associated with it. Check for IllegalStateException.
   */

  public void closedQueueConnectionRecoverTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call recover() with closed connection.");
      try {
        tool.getDefaultQueueSession().recover();
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionRecoverTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:108; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * 
   * @test_Strategy: Close default Connection and call method on it.
   */

  public void closedQueueConnectionCloseTest() throws Exception {
    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call close again");
      tool.getDefaultQueueConnection().close();
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetClientIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:512;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetClientIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getClientID");
      try {
        String foo = tool.getDefaultQueueConnection().getClientID();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetClientIDTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetMetaDataTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:516;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetMetaDataTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getMetaData");
      try {
        ConnectionMetaData foo = tool.getDefaultQueueConnection().getMetaData();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetMetaDataTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionStartTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:522;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionStartTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call start");
      try {
        tool.getDefaultQueueConnection().start();
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionStartTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateQueueSessionTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateQueueSessionTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call createQueueSession");
      try {
        QueueSession foo = tool.getDefaultQueueConnection()
            .createQueueSession(true, Session.AUTO_ACKNOWLEDGE);

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.logErr("Fail: wrong exception was returned", e);
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateQueueSessionTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSessionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:SPEC:114;
   * 
   * @test_Strategy: Close default connection and call method on it.
   */

  public void closedQueueConnectionSessionCloseTest() throws Exception {
    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to close session again.");
      tool.getDefaultQueueSession().close();
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSessionCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateBrowserTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:190;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateBrowserTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create QueueBrowser with closed connection.");
      try {
        QueueBrowser qB = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue());

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateBrowserTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateBrowserMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:192;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateBrowserMsgSelectorTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create QueueBrowser with closed Connection.");
      try {
        QueueBrowser qB = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue(), "TEST = 'test'");

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateBrowserMsgSelectorTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateQueueTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:182;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateQueueTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create queue with closed Connection.");
      try {
        Queue q = tool.getDefaultQueueSession()
            .createQueue("closedQueueConnectionCreateQueueTest");

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateReceiverTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:184;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateReceiverTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create Receiver with closed Connection.");
      try {
        QueueReceiver qR = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue());

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateReceiverTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateReceiverMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:186;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateReceiverMsgSelectorTest()
      throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create Receiver with closed Connection.");
      try {
        QueueReceiver qR = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue(), "TEST = 'test'");

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateReceiverMsgSelectorTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateSenderTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:188;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateSenderTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create Sender with closed Connection.");
      try {
        QueueSender qS = tool.getDefaultQueueSession()
            .createSender(tool.getDefaultQueue());

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateSenderTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateTempQueueTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:194;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateTempQueueTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create TemporaryQueue with closed connection.");
      try {
        TemporaryQueue tQ = tool.getDefaultQueueSession()
            .createTemporaryQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateTempQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:213;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create message with closed connection.");
      try {
        Message m = tool.getDefaultQueueSession().createMessage();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateMessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateBytesMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:209;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateBytesMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create BytesMessage with closed connection.");
      try {
        BytesMessage m = tool.getDefaultQueueSession().createBytesMessage();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateBytesMessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateMapMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:211;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateMapMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create MapMessage with closed connection.");
      try {
        MapMessage m = tool.getDefaultQueueSession().createMapMessage();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateMapMessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateObjectMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:215;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateObjectMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create ObjectMessage with closed connection.");
      try {
        ObjectMessage m = tool.getDefaultQueueSession().createObjectMessage();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateObjectMessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateObject2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:217;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateObject2MessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create ObjectMessage(object) with closed connection.");
      try {
        String s = "Simple object";
        ObjectMessage m = tool.getDefaultQueueSession().createObjectMessage(s);
        if (m != null)
          logTrace("m=" + m);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateObject2MessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateStreamMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:219;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateStreamMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create StreamMessage with closed connection.");
      try {
        StreamMessage m = tool.getDefaultQueueSession().createStreamMessage();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateStreamMessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateTextMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:221;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateTextMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create TextMessage with closed connection.");
      try {
        TextMessage m = tool.getDefaultQueueSession().createTextMessage();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateTextMessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionCreateText2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:SPEC:113; JMS:JAVADOC:223;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionCreateText2MessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to create TextMessage with closed connection.");
      try {
        TextMessage m = tool.getDefaultQueueSession()
            .createTextMessage("test message");

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Caught expected exception");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionCreateText2MessageTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionReceiverCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default receiver and call method on it.
   */

  public void closedQueueConnectionReceiverCloseTest() throws Exception {
    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call close again");
      tool.getDefaultQueueReceiver().close();
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionReceiverCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetMessageSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:326;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetMessageSelectorTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultQueueReceiver().getMessageSelector();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetMessageSelectorTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionReceiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:332;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionReceiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultQueueReceiver().receive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionReceiveTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionReceiveTimeoutTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:334;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionReceiveTimeoutTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultQueueReceiver().receive(timeout);

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionReceiveTimeoutTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionReceiveNoWaitTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:337;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionReceiveNoWaitTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultQueueReceiver().receiveNoWait();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionReceiveNoWaitTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionReceiverGetQueueTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:268;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionReceiverGetQueueTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueReceiver().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionReceiverGetQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSenderCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:315;
   * 
   * @test_Strategy: Close default sender and call method on it.
   */

  public void closedQueueConnectionSenderCloseTest() throws Exception {
    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call close again");
      tool.getDefaultQueueSender().close();
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSenderCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:303;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetDeliveryModeTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultQueueSender().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:295;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetDisableMessageIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:299;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetDisableMessageTimestampTest()
      throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:307;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetPriorityTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultQueueSender().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetPriorityTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:311;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionGetTimeToLiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultQueueSender().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionGetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:301;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSetDeliveryModeTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultQueueSender()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:293;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSetDisableMessageIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultQueueSender().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:297;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSetDisableMessageTimestampTest()
      throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultQueueSender().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:305;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSetPriorityTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultQueueSender().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSetPriorityTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:309;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSetTimeToLiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultQueueSender()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSenderGetQueueTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:196;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSenderGetQueueTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueSender().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSenderGetQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSend1Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:198;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSend1Test() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call send(Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSend1Test", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSend2Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:200;
   * 
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSend2Test() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call send(Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSend2Test", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSend3Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:202;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSend3Test() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call send(Queue,Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSend3Test", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSend4Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:274; JMS:JAVADOC:526;
   * JMS:JAVADOC:204;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueConnectionSend4Test() throws Exception {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call send(Queue,Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedQueueConnectionSend4Test", e);
    }
  }

}
