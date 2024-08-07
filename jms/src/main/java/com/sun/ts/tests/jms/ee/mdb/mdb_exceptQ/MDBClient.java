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
package com.sun.ts.tests.jms.ee.mdb.mdb_exceptQ;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.jms.commonee.Client;

import jakarta.jms.Queue;

public class MDBClient extends Client {

  // Naming specific member variables
  private Queue cmtQ;

  private Queue cmtQTXNS;

  private Queue bmtQ;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * Test setup:
   * 
   * @class.setup_props: jms_timeout, in milliseconds - how long to wait on
   * synchronous receive; user;password;
   *
   */
  public void setup(String[] args, Properties p) throws Exception {
    props = p;
    super.setup(args, p);
    try {
      cmtQ = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_CMT");
      cmtQTXNS = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUETXNS_CMT");
      bmtQ = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_BMT");
    } catch (Exception e) {
      throw new Exception("Setup Failed!", e);
    }
  }

  /* Run tests */

  /*
   * @testName: Test1
   *
   * @assertion_ids: EJB:SPEC:529; EJB:SPEC:530; EJB:SPEC:547; EJB:SPEC:580;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_QUEUE_BMT. The mdb
   * begins a jakarta.transaction.UserTransaction, then attempts a
   * MessageDrivenContext getRollBackOnly() method. Return verification message
   * that a java.lang.IllegalStateException was thrown.
   */
  public void Test1() throws Exception {
    String TestCase = "expTest1";
    int TestNum = 1;
    try {
      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test1 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test2
   *
   * @assertion_ids: EJB:SPEC:529; EJB:SPEC:530; EJB:SPEC:547; EJB:SPEC:580;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_QUEUE_BMT. The mdb
   * begins a jakarta.transaction.UserTransaction, then attempts a
   * MessageDrivenContext setRollBackOnly() method. Return verification message
   * that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test2() throws Exception {
    String TestCase = "expTest2";
    int TestNum = 2;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test2 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test3
   *
   * @assertion_ids: EJB:SPEC:592; EJB:SPEC:602;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUETXNS_CMT. The mdb
   * attempts a MessageDrivenContext setRollBackOnly() method. Return
   * verification message that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test3() throws Exception {
    String TestCase = "expTest3";
    int TestNum = 3;
    try {
      qSender = session.createSender(cmtQTXNS);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test3 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test4
   *
   * @assertion_ids: EJB:SPEC:592; EJB:SPEC:602;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUETXNS_CMT. The mdb
   * attempts a MessageDrivenContext getRollBackOnly() method. Return
   * verification message that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test4() throws Exception {
    String TestCase = "expTest4";
    int TestNum = 4;
    try {

      qSender = session.createSender(cmtQTXNS);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test4 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test5
   *
   * @assertion_ids: EJB:SPEC:593; EJB:SPEC:603;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUETXNS_CMT The mdb
   * attempts a EJBContext getUserTransaction() method. Return verification
   * message that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test5() throws Exception {
    String TestCase = "expTest5";
    int TestNum = 5;
    try {

      qSender = session.createSender(cmtQTXNS);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test5 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test6
   *
   * @assertion_ids: EJB:SPEC:593; EJB:SPEC:603;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUE_CMT The mdb
   * attempts a EJBContext getUserTransaction() method. Return verification
   * message that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test6() throws Exception {
    String TestCase = "expTest6";
    int TestNum = 6;
    try {

      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test6 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test7
   *
   * @assertion_ids: EJB:SPEC:513;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUE_CMT. The mdb
   * attempts a EJBContext getCallerPrincipal() method.
   */
  public void Test7() throws Exception {
    String TestCase = "expTest7";
    int TestNum = 7;
    try {

      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test7 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test8
   *
   * @assertion_ids: EJB:SPEC:513;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUETXNS_CMT. The mdb
   * attempts a EJBContext getCallerPrincipal() method.
   */
  public void Test8() throws Exception {
    String TestCase = "expTest8";
    int TestNum = 8;
    try {

      qSender = session.createSender(cmtQTXNS);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test8 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test11
   *
   * @assertion_ids: EJB:SPEC:515; EJB:SPEC:531;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUE_CMT. The mdb
   * attempts a EJBContext getEJBHome() method. Return verification message that
   * a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test11() throws Exception {
    String TestCase = "expTest11";
    int TestNum = 11;
    try {

      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test11 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test12
   *
   * @assertion_ids: EJB:SPEC:515; EJB:SPEC:531;
   *
   * @test_Strategy: Invoke an cmt mdb by writing to MDB_QUEUETXNS_CMT. The mdb
   * attempts a EJBContext getEJBHome() method. Return verification message that
   * a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test12() throws Exception {
    String TestCase = "expTest12";
    int TestNum = 12;
    try {

      qSender = session.createSender(cmtQTXNS);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test12 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: Test15
   *
   * @assertion_ids: EJB:SPEC:579;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_QUEUE_BMT. The mdb
   * begins a jakarta.transaction.UserTransaction. The mdb begins another
   * jakarta.transaction.UserTransaction Verify that the container throws a
   * jakarta.transaction.NotSupportedException
   *
   */
  public void Test15() throws Exception {
    String TestCase = "expTest15";
    int TestNum = 15;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test15 - ");
      }
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /* cleanup -- use super cleanup */

}
