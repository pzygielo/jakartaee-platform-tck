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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.common;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.Properties;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

import jakarta.ejb.NoSuchEJBException;

abstract public class ClientBase extends EETest {
  protected Properties props;

  private RemoveIF removeBean;

  private Remove2IF removeBean2;

  private RemoveNotRetainIF removeNotRetainBean;

  private TestIF testBean;

  abstract protected RemoveIF getRemoveBean();

  abstract protected Remove2IF getRemoveBean2();

  abstract protected RemoveNotRetainIF getRemoveNotRetainBean();

  abstract protected TestIF getTestBean();

  public void setup(String[] args, Properties p) throws Exception {
    props = p;
    removeBean = getRemoveBean();
    removeBean2 = getRemoveBean2();
    testBean = getTestBean();
    removeNotRetainBean = getRemoveNotRetainBean();
  }

  public void cleanup() throws Exception {
    removeBeans();
  }

  protected void removeBeans() {
    if (removeBean != null) {
      try {
        removeBean.remove();
        TLogger.log("Successfully removed removeBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing removeBean " + e);
      }
    }
    if (removeBean2 != null) {
      try {
        removeBean2.remove();
        TLogger.log("Successfully removed removeBean2.");
      } catch (Exception e) {
        TLogger.log("Exception while removing removeBean2 " + e);
      }
    }
    // no need to remove stateless bean
    // if(testBean != null) {
    // try {
    // testBean.remove();
    // TLogger.log("Successfully removed testBean.");
    // } catch (Exception e) {
    // TLogger.log("Exception while removing testBean " + e);
    // }
    // }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: removeBean
   * 
   * @test_Strategy:
   *
   */
  public void removeBean() throws Exception {
    removeBean.remove();
    try {
      removeBean.remove();
      throw new Exception("Expecting jakarta.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
      removeBean = null;
    }
  }

  /*
   * testName: removeBean2
   * 
   * @test_Strategy:
   *
   */
  public void removeBean2() throws Exception {
    // removeBean2 is of type Remove2IF, which implements java.rmi.Remote
    try {
      removeBean2.remove();
    } catch (RemoteException e) {
      throw new Exception(e);
    }
    try {
      removeBean2.remove();
      throw new Exception(
          "Expecting java.rmi.NoSuchObjectException, but got none.");
    } catch (NoSuchObjectException e) {
      TLogger.log("Got expected exception: " + e.toString());
      removeBean2 = null;
    } catch (RemoteException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanremoveBean
   * 
   * @test_Strategy:
   *
   */
  public void testBeanremoveBean() throws Exception {
    try {
      testBean.removeBean();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanremoveBeanRemote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF.
   *
   */
  public void testBeanremoveBeanRemote() throws Exception {
    try {
      testBean.removeBeanRemote();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanremoveBean2
   * 
   * @test_Strategy:
   *
   */
  public void testBeanremoveBean2() throws Exception {
    try {
      testBean.removeBean2();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanremoveBean2Remote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF2.
   *
   */
  public void testBeanremoveBean2Remote() throws Exception {
    try {
      testBean.removeBean2Remote();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: retainBean
   * 
   * @test_Strategy:
   *
   */
  public void retainBean() throws Exception {
    try {
      removeBean.retain();
    } catch (TestFailedException e) {
      // expected. the bean is not removed
    }
    try {
      removeBean.retain2();
    } catch (AtUncheckedAppException e) {
      // expected. the bean is not removed
    } catch (UncheckedAppException e) {
      // expected. the bean is not removed
    }
    try {
      removeBean.remove();
      TLogger.log("removeBean removed successfully");
    } catch (NoSuchEJBException e) {
      throw new Exception("Failed to remove bean, which should be there when this"
          + "method was called");
    } finally {
      removeBean = null;
    }
  }

  /*
   * testName: retainBeanOverloaded
   * 
   * @test_Strategy:
   *
   */
  public void retainBeanOverloaded() throws Exception {
    removeBean.remove("This is not a remove-method. Not to remove bean.");
    removeBean.hi();
    try {
      removeBean.remove();
      TLogger.log("removeBean removed successfully");
    } catch (NoSuchEJBException e) {
      throw new Exception("Failed to remove bean, which should be there when this"
          + "method was called. But got " + e);
    } finally {
      removeBean = null;
    }
  }

  /*
   * testName: retainBean2
   * 
   * @test_Strategy:
   *
   */
  public void retainBean2() throws Exception {
    // removeBean2.retain(); does not exit any more
    // try {
    // removeBean2.retain();
    // } catch (TestFailedException e) {
    // //expected. the bean is not removed
    // } catch (RemoteException e) {
    // throw new Exception(e);
    // }
    try {
      removeBean2.retain2();
    } catch (AtUncheckedAppException e) {
      // expected. the bean is not removed
    } catch (UncheckedAppException e) {
      // expected. the bean is not removed
    } catch (RemoteException e) {
      throw new Exception(e);
    }
    try {
      removeBean2.remove();
      TLogger.log("removeBean2 removed successfully");
    } catch (RemoteException e) {
      throw new Exception(e);
    } finally {
      removeBean2 = null;
    }
  }

  /*
   * testName: testBeanretainBean
   * 
   * @test_Strategy:
   *
   */
  public void testBeanretainBean() throws Exception {
    try {
      testBean.retainBean();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanretainBeanOverloaded
   * 
   * @test_Strategy:
   *
   */
  public void testBeanretainBeanOverloaded() throws Exception {
    try {
      testBean.retainBeanOverloaded();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanretainBeanRemote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF.
   *
   */
  public void testBeanretainBeanRemote() throws Exception {
    try {
      testBean.retainBeanRemote();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanretainBean2
   * 
   * @test_Strategy:
   *
   */
  public void testBeanretainBean2() throws Exception {
    try {
      testBean.retainBean2();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: testBeanretainBean2Remote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF2.
   *
   */
  public void testBeanretainBean2Remote() throws Exception {
    try {
      testBean.retainBean2Remote();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: removeNotRetainBean
   * 
   * @test_Strategy:
   *
   */
  public void removeNotRetainBean() throws Exception {
    try {
      removeNotRetainBean.remove();
    } catch (TestFailedException e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + "despite the exception");
    }
    try {
      removeNotRetainBean.remove();
      throw new Exception("Expecting jakarta.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } catch (TestFailedException e) {
      throw new Exception("Unexpected exception", e);
    } finally {
      removeNotRetainBean = null;
    }
  }

  /*
   * testName: removeNotRetainBean2
   * 
   * @test_Strategy:
   *
   */
  public void removeNotRetainBean2() throws Exception {
    try {
      removeNotRetainBean.remove2();
    } catch (AtUncheckedAppException e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + "despite the exception");
    } catch (UncheckedAppException e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + "despite the exception");
    }
    try {
      removeNotRetainBean.remove2();
      throw new Exception("Expecting jakarta.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } catch (AtUncheckedAppException e) {
      throw new Exception("Unexpected exception", e);
    } catch (UncheckedAppException e) {
      throw new Exception("Unexpected exception", e);
    } finally {
      removeNotRetainBean = null;
    }
  }

  /*
   * testName: alwaysRemoveAfterSystemException
   * 
   * @test_Strategy: a bean must always be removed after a system exception,
   * even though the remove method is retainIfException true.
   *
   */
  public void alwaysRemoveAfterSystemException() throws Exception {
    try {
      removeNotRetainBean.alwaysRemoveAfterSystemException();
    } catch (Exception e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + " after the system exception: " + e);
    }
    try {
      removeNotRetainBean.alwaysRemoveAfterSystemException();
      throw new Exception("Expecting jakarta.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } finally {
      removeNotRetainBean = null;
    }
  }

}
