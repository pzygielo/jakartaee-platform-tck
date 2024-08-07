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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.localaccess.sbaccesstest;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private TestBean bRef = null;

  private TestBeanHome bHome = null;

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      bHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /* Run test */

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:2.1; EJB:SPEC:26; EJB:SPEC:26.1; EJB:SPEC:26.2
   * 
   * @test_Strategy: A session bean instance has access to the LocalHome and
   * Local Interfaces of a Local Session Bean (SF). Verify local access from
   * Session Bean to a local stateful session bean .
   */

  public void test2() throws Fault {
    logTrace("test2");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Perform Local object access test from SB to Local SB (SF)");
      pass = bRef.test2();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:2.1; EJB:SPEC:4; EJB:SPEC:29; EJB:SPEC:30;
   * EJB:SPEC:32
   * 
   * @test_Strategy: A session bean instance has access to the LocalHome and
   * Local Interfaces of a Local Session Bean (SL). Verify local access from
   * Session Bean to a local stateful session bean.
   */

  public void test4() throws Fault {
    logTrace("test4");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Perform Local object access test from SB to Local SB (SL)");
      pass = bRef.test4();
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test4 failed");
  }
}
