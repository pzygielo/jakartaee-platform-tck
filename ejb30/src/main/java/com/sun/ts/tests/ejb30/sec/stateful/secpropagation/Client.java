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

package com.sun.ts.tests.ejb30.sec.stateful.secpropagation;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSLoginContext;
import com.sun.ts.tests.ejb30.sec.stateful.common.Test;

import jakarta.ejb.EJB;

public class Client extends EETest {

  // JNDI names for looking up ejbs
  @EJB(beanName = "TestEJB")
  static private Test ejbref = null;

  // Security role references.
  private static final String emp_secrole_ref = "EMP";

  private static final String admin_secrole_ref = "ADMIN";

  private static final String mgr_secrole_ref = "MGR";

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private Properties props = null;

  private TSLoginContext lc = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: user; password;
   */

  public void setup(String[] args, Properties p) throws Exception {
    props = p;

    try {
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);

      lc = new TSLoginContext();
      lc.login(username, password);

    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create a stateful session bean accessing a method of a
   * second bean. 2. Protect the method within the bean with multiple security
   * roles. 3. Call bean method as a principal who is in one some of the
   * security roles but not in others. 4. Verify call returns successfully.
   */

  public void test1() throws Exception {
    logMsg("Starting Caller authorization test");
    try {
      // ejbref = ejbhome.create(props);
      if (!ejbref.EjbIsAuthz())
        throw new Exception("Caller authorization test failed");
      logMsg("Caller authorization test passed");
    } catch (Exception e) {
      throw new Exception("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4
   *
   * @test_Strategy: 1. Create a stateful session bean accessing a method of a
   * second bean. 2. Protect the method in the bean using a security role
   * (role1). 3. Link a security role ref - emp_secrole_ref - to role1 in the
   * bean. 4. Invoke the method with emp_secrole_ref as parameter. 5. bean calls
   * isCallerInRole(emp_secrole_ref) and returns return value. 6. Verify return
   * value is true.
   */

  public void test3() throws Exception {
    logMsg("Starting Security role reference positive test");
    try {
      if (!ejbref.EjbSecRoleRef(emp_secrole_ref))
        throw new Exception("Security role reference positive test failed");
      logMsg("Security role reference positive test passed");
    } catch (Exception e) {
      throw new Exception("Security role reference positive test failed: ", e);
    }
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:61.8
   *
   * @test_Strategy: 1. Create a stateful session bean accessing a method of a
   * second bean. 2. Protect the method in the bean using a security role
   * (role1). 3. Link a security role ref - emp_secrole_ref - to role1 in the
   * bean. 4. Invoke the method with mgr_secrole_ref as a parameter. 5. bean
   * calls isCallerInRole(mgr_secrole_ref) and returns return value. 6. Verify
   * return value is false.
   */

  public void test4() throws Exception {
    logMsg("Starting Security role reference negative test");
    try {
      if (!ejbref.EjbSecRoleRef1(mgr_secrole_ref))
        throw new Exception("Security role reference negative test failed");
      logMsg("Security role reference negative test passed");
    } catch (Exception e) {
      throw new Exception("Security role reference negative test failed: ", e);
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:799; EJB:SPEC:804
   * 
   * @test_Strategy: 1. Create a stateful session bean accessing two other beans
   * (ejb1 and ejb2). 2. Link security role reference (roleref) to role1 in ejb1
   * and role2 in ejb2. 3. Ensure caller principal is in role1 but not in role2.
   * 4. Invoke method in ejb1 that returns value of isCallerInRole(roleref).
   * Verify return value is true. 5. Invoke method in ejb2 that returns value of
   * isCallerInRole(roleref). Verify return value is false.
   */

  public void test5() throws Exception {
    logMsg("Starting Security role reference scope test");
    try {
      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      if (!ejbref.EjbSecRoleRefScope(emp_secrole_ref))
        throw new Exception("Security role reference scope test failed");
      logMsg("Security role reference scope test passed");
    } catch (Exception e) {
      throw new Exception("Security role reference scope test failed: ", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:811
   * 
   * @test_Strategy: 1. Create a stateful session bean accessing another with
   * overloaded methods: method1 and method2. 2. Call method1 passing
   * emp_secrole_ref. 3. Method1 returns isCallerInRole(emp_secrole_ref) which
   * must be true. 4. Call method2 passing two role references as parameters. 5.
   * Method must return false ( caller not in both security role refs).
   */

  public void test2() throws Exception {
    logMsg("Starting Overloaded security role references test");
    try {
      if (!ejbref.EjbOverloadedSecRoleRefs(emp_secrole_ref, mgr_secrole_ref))
        throw new Exception("Overloaded security role references test failed");

      logMsg("Overloaded security role references test passed");
    } catch (Exception e) {
      throw new Exception("Overloaded security role references test failed", e);
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a stateful session bean accessing another with a
   * method. 2. Protect the second bean's method with multiple security roles.
   * 3. Call bean method as a principal who is not in any of the security roles
   * that protects the method. 4. Verify java.rmi.RemoteException is generated.
   */

  public void test6() throws Exception {
    logMsg("Starting No caller authorization test");
    try {
      if (!ejbref.EjbNotAuthz())
        throw new Exception("No caller authorization test failed");
      logMsg("No authorization test passed");
    } catch (Exception e) {
      throw new Exception("No caller authorization test failed:", e);
    }
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a stateful session bean invokes a remote method
   * of the second bean. 2. Have this method with method permission unchecked 3.
   * Verify that access is allowed.
   */

  public void test7() throws Exception {
    logMsg("Starting unchecked test1");
    try {
      if (!ejbref.checktest1()) {
        logErr("unchecked test returned false");
        throw new Exception("unchecked test1 failed");
      }
      logMsg("unchecked test1 passed.");

    } catch (Exception e) {
      throw new Exception("unchecked test1 failed", e);
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:808
   *
   * @test_Strategy: 1. Create a stateful session bean invokes a remote method
   * of the second bean. 2. Put this method on exclude-list. 3. Verify
   * java.rmi.RemoteException is generated.
   */

  public void test8() throws Exception {
    logMsg("Starting exclude-list test1");
    try {
      if (!ejbref.excludetest1()) {
        logErr("excludetest1 returned false");
        throw new Exception("excludetest1 failed");
      }
      logMsg("exclude-list test1 passed");
    } catch (Exception e) {
      throw new Exception("exclude-list test1 failed:", e);
    }
  }

  public void cleanup() throws Exception {
    logMsg("cleanup ok");
  }
}
