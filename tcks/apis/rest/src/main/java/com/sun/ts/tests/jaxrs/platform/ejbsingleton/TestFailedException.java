/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */

package com.sun.ts.tests.jaxrs.platform.ejbsingleton;

import java.io.Serializable;

public class TestFailedException extends Exception implements Serializable {

    private static final long serialVersionUID = 8413549220190649134L;

    public TestFailedException() {
        super();
    }

    /**
     * @param message
     */
    public TestFailedException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public TestFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public TestFailedException(Throwable cause) {
        super(cause);
    }

}
