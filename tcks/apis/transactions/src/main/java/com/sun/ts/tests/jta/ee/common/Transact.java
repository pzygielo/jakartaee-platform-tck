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
 * @(#)Transact.java	1.16 03/05/16
 */

package com.sun.ts.tests.jta.ee.common;

import com.sun.ts.tests.common.base.ServiceEETest;
// TS Specific Utils
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

public class Transact extends ServiceEETest implements TransactionStatus {
    public static TSNamingContext nctx = null;

    public Transact() {
    }

    private static void prepareTM() throws Exception {
        // Gets Naming Context
        nctx = new TSNamingContext();

    }

    public static final String getStatusString(int status) throws InvalidStatusException {
        try {
            return TransactionStatus.transStatusArray[status];
        } catch (ArrayIndexOutOfBoundsException arryIndex) {
            TestUtil.printStackTrace(arryIndex);
            throw new InvalidStatusException();
        }
    }// End of getStatusString

    // This will be called at the start of every test.
    public static void init() throws InitFailedException {
        try {
            prepareTM();
        } catch (Exception exception) {
            TestUtil.printStackTrace(exception);
            throw new InitFailedException("Test Environment Init" + " Failed ");
        }
    }// End of init

    // This will be called in the cleanup method of Every Test
    // so this version eats the exception.
    public static void free() {
        try {
            // Does nothing for now ...
        } catch (Exception exception) {
            TestUtil.logErr("Fail to free the environment", exception);
        }
    }
}// End of Transact
