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
package com.sun.ts.tests.jms.core.closedQueueReceiver;

import java.util.ArrayList;
import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.Message;
import jakarta.jms.Queue;

/**
 * JMS TS tests. Testing method calls on closed QueueReceiver objects.
 */
public class ClosedQueueReceiverTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests";

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

  ArrayList queues = null;

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ClosedQueueReceiverTests theTests = new ClosedQueueReceiverTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed receiver for testing. Passes any
   * exceptions up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseReceiver() throws Exception {
    tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
    tool.getDefaultQueueConnection().start();

    logTrace("Closing queue receiver");
    tool.getDefaultQueueReceiver().close();
    logTrace("Receiver closed");
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Queue and/or
   * Topic Connection, as well as a default Queue and Topic. Tests that require
   * multiple Destinations create the extras within the test
   * 
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */

  /**
   * Method Declaration.
   * 
   * 
   * @param args
   * @param p
   *
   * @exception Fault
   *
   * @see
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
        throw new Exception("'user' in ts.jte must not be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      queues = new ArrayList(2);

      // get ready for new test
      logTrace("Getting Administrator and deleting any leftover destinations.");
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
        logMsg("Cleanup: Closing Queue and Topic Connections");
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
   * @testName: closedQueueReceiverCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default receiver and call method on it.
   */

  public void closedQueueReceiverCloseTest() throws Exception {
    try {
      createAndCloseReceiver();
      logTrace("Try to call close again");
      tool.getDefaultQueueReceiver().close();
    } catch (Exception e) {
      throw new Exception("closedQueueReceiverCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueReceiverGetMessageSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:326;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverGetMessageSelectorTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseReceiver();
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
      throw new Exception("closedQueueReceiverGetMessageSelectorTest", e);
    }
  }

  /*
   * @testName: closedQueueReceiverReceiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:332;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverReceiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseReceiver();
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
      throw new Exception("closedQueueReceiverReceiveTest", e);
    }
  }

  /*
   * @testName: closedQueueReceiverReceiveTimeoutTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverReceiveTimeoutTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseReceiver();
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
      throw new Exception("closedQueueReceiverReceiveTimeoutTest", e);
    }
  }

  /*
   * @testName: closedQueueReceiverReceiveNoWaitTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:336;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverReceiveNoWaitTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseReceiver();
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
      throw new Exception("closedQueueReceiverReceiveNoWaitTest", e);
    }
  }

  /*
   * @testName: closedQueueReceiverGetQueueTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:268;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverGetQueueTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseReceiver();
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
      throw new Exception("closedQueueReceiverGetQueueTest", e);
    }
  }

}
