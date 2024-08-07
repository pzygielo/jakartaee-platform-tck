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
 * $Id$
 */

package com.sun.ts.tests.samples.ejb.ee.twobean;

import java.util.Properties;

/**
 * The TwoBeanClient class tests a client calling a bean which 
 * calls another bean.
 * @author   Tester
 * @version  1.2 99/03/01
 */

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;

public class TwoBeanClient extends EETest {

  private static final String testBean1 = "java:comp/env/ejb/TestBean1";

  private static final String testProps = "twobean.properties";

  private static final String testDir = System.getProperty("user.dir");

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private Properties props;

  private TestBean1 ref = null;

  private StringBuffer logData = null;

  /* Run test in standalone mode */

  public static void main(String[] args) {
    TwoBeanClient theTests = new TwoBeanClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */
  public void setup(String[] args, Properties p) throws Exception {

    this.props = p;
    try {
      jc = new TSNamingContext();
      logMsg("Looked up EJB!!");
      ref = (TestBean1) jc.lookup(testBean1, TestBean1.class);
      ref.initialize(props);
      logMsg("Setup ok;");
    } catch (Exception e) {
      throw new Exception("Setup Failed!", e);
    }
  }

  /* Run test */

  /*
   * @testName: twobeanTest1
   * 
   * @assertion: Sucessfully call the "business" method of bean1 which calls a
   * business method in bean2.
   * 
   * @test_Strategy: Create 2 stateful Session EJBeans. Deploy them on the J2EE
   * server. From the EJB client. call the simpletest1() business method. This
   * in turn should call the bean2test() method. The calls should complete
   * without throwing any exceptions.
   * 
   */
  public void twobeanTest1() throws Exception {
    boolean result = false;
    try {
      // invoke method on the EJB
      result = ref.simpleTest1();
      // logData = ref.getServerLogData();
      // logMsg( logData.toString());
      if (result)
        logMsg("Test passed;");
      else
        throw new Exception("Test Failed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: twobeanTest2
   * 
   * @assertion: Sucessfully call the "business" method of bean1.
   * 
   * @test_Strategy: Create 2 stateful Session EJBeans. Deploy them on the J2EE
   * server. From the EJB client, call the simpletest2(n) business method which
   * just sleeps for n seconds. The call should complete without throwing any
   * exceptions.
   * 
   */
  public void twobeanTest2() throws Exception {
    boolean result = false;
    try {
      // invoke method on the EJB
      result = ref.simpleTest2(100);
      // logData = ref.getServerLogData();
      // logMsg( logData.toString());
      if (result)
        logMsg("Test passed;");
      else
        throw new Exception("Test Failed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void cleanup() throws Exception {
    logMsg("Cleanup ok;");
  }

}
