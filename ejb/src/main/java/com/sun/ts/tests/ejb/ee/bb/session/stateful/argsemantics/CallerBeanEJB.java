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

package com.sun.ts.tests.ejb.ee.bb.session.stateful.argsemantics;

import java.util.Properties;

import com.sun.ts.tests.common.ejb.wrappers.StatefulWrapper;
import com.sun.ts.tests.common.testlogic.ejb.bb.argsemantics.TestLogic;

public class CallerBeanEJB extends StatefulWrapper {

  public boolean testStatefulRemote(Properties props) {
    return TestLogic.testStatefulRemote(nctx, props);
  }

  public boolean testStatefulLocal(Properties props) {
    return TestLogic.testStatefulLocal(nctx, props);
  }

  public boolean testStatefulBoth(Properties props) {
    return TestLogic.testStatefulBoth(nctx, props);
  }

  public void cleanUpBean() {
    TestLogic.cleanUpStatefulBean();
  }

}
