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
 * @(#)DeploymentClient.java	1.4  03/05/16
 */

package com.sun.ts.tests.connector.deployment;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;

public class DeploymentClient extends EETest {
  // Naming specific member variables
  private TSNamingContext jc = null;

  // Harness requirements
  private Deployment hr = null;

  private StringBuffer logData = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    DeploymentClient theTests = new DeploymentClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * whitebox-embed; rauser1; rapassword1;
   *
   */
  public void setup(String[] args, Properties p) throws Exception {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.

    props = p;

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.

    try {
      jc = new TSNamingContext();
      hr = (Deployment) jc.lookup("java:comp/env/ejb/Deployment", Deployment.class);
      hr.initialize(props);
      logMsg("Setup ok;");
    } catch (Exception e) {
      throw new Exception("Setup Failed!", e);
    }
  }

  /*
   * @testName: testRarInEar
   *
   * @assertion_ids: Connector:SPEC:147; Connector:SPEC:153;
   * 
   * @test_Strategy: Call DataSource.getConnection for the embedded resource
   * adapter. Check whether the connection has been correctly allocated from the
   * ConnectionManager.
   *
   * Use the connection in some interactions with the database.
   *
   *
   */
  public void testRarInEar() throws Exception {
    try {
      hr.initialize(props);

      // invoke method on the EJB
      boolean result = hr.testRarInEar();

      if (result) {
        logMsg("Test Passed");
      } else {
        throw new Exception("Embedded resource adapter test failed");
      }

      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  public void cleanup() throws Exception {
    hr.cleanup();
    logMsg("Cleanup ok;");
  }
}
