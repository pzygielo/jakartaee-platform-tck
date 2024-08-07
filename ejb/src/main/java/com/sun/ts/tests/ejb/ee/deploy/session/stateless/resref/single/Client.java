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
 * @(#)Client.java	1.33 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.resref.single;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private TestBeanHome home = null;

  private TestBean bean = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     mailuser1; webServerHost; webServerPort; mailFrom;
   *                     mailHost;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      nctx = new TSNamingContext();
      home = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
    } catch (Exception e) {
      throw new Fault("Client: Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testDatasource
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a Stateless Session Bean declaring a resource
   *                 reference for a javax.sql.Datasource.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the datasource. - We can use it to open a DB
   *                 connection.
   */
  public void testDatasource() throws Fault {
    boolean pass = false;

    try {
      bean = home.create();
      bean.initLogging(props);

      pass = bean.testDatasource();
      bean.remove();

      if (!pass) {
        throw new Fault("Datasource res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Datasource res-ref test failed: " + e);
      throw new Fault("Datasource res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testSession
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a Stateless Session Bean declaring a resource
   *                 reference for a jakarta.mail.Session.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the mail session. - We can use this factory to send
   *                 a mail.
   */
  public void testSession() throws Fault {
    boolean pass = false;

    try {
      bean = home.create();
      bean.initLogging(props);
      pass = bean.testSession();
      bean.remove();

      if (!pass) {
        throw new Fault("Session ref-res test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Session res-ref test failed: " + e);
      throw new Fault("Session res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testURL
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a Stateless Session Bean declaring a resource
   *                 reference for a java.net.URL.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the URL. - We can use this URL factory to open a
   *                 connection to a HTML page bundled in the application.
   */
  public void testURL() throws Fault {
    boolean pass = false;

    try {
      bean = home.create();
      bean.initLogging(props);
      pass = bean.testURL();
      bean.remove();

      if (!pass) {
        throw new Fault("URL res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: URL res-ref test failed: " + e);
      throw new Fault("URL res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testQueue
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a Stateless Session Bean declaring a resource
   *                 reference for a jakarta.jms.QueueConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Queue Connection Factory.
   */
  public void testQueue() throws Fault {
    boolean pass = false;

    try {
      bean = home.create();
      bean.initLogging(props);
      pass = bean.testQueue();
      bean.remove();

      if (!pass) {
        throw new Fault("JMS Queue res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: JMS Queue Factory res-ref test failed: " + e);
      throw new Fault("Queue factory res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testTopic
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a Stateless Session Bean declaring a resource
   *                 reference for a jakarta.jms.TopicConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Topic Connection Factory.
   */
  public void testTopic() throws Fault {
    boolean pass = false;

    try {
      bean = home.create();
      bean.initLogging(props);
      pass = bean.testTopic();
      bean.remove();

      if (!pass) {
        throw new Fault("JMS Topic res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: JMS Topic Factory res-ref test failed: " + e);
      throw new Fault("Topic factory res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testAll
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a Stateless Session Bean declaring a resource
   *                 reference for all the standard resource manager connection
   *                 factory types.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup all the declared resource factories.
   */
  public void testAll() throws Fault {
    boolean pass = false;

    try {
      bean = home.create();
      bean.initLogging(props);
      pass = bean.testAll();
      bean.remove();

      if (!pass) {
        throw new Fault("All res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: All res-ref test failed: " + e);
      throw new Fault("All res-ref test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup()");
  }
}
