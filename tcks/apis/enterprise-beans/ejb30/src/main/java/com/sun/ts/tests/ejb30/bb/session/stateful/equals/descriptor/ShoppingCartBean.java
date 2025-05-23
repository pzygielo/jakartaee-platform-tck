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

package com.sun.ts.tests.ejb30.bb.session.stateful.equals.descriptor;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

//@Stateful(name="shopping-cart-bean")
//@Remote({ShoppingCartIF.class})
//@Local({LocalShoppingCartIF.class})
@TransactionManagement(TransactionManagementType.BEAN)
public class ShoppingCartBean
//  implements CartIF, ShoppingCartIF, LocalCartIF, LocalShoppingCartIF
{
  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  protected SessionContext getSessionContext() {
    return sessionContext;
  }

  public ShoppingCartBean() {
  }

  public void remove() {
  }

}
