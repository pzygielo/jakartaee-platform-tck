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

package com.sun.ts.tests.common.vehicle.stateful3;

import java.util.Properties;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean;
import com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;

@Stateful(name = "Stateful3VehicleBean")
public class Stateful3VehicleBean extends EJB3ShareBaseBean
        implements Stateful3VehicleIF, java.io.Serializable {

    @PersistenceUnit(name = "STATEFUL3EMF", unitName = "CTS-EM")
    EntityManagerFactory emf;

    public Stateful3VehicleBean() {
        super();
    }

    protected String getVehicleType() {
        return EJB3ShareBaseBean.STATEFUL3;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("In PostContruct");
            EntityManagerFactory emf = (EntityManagerFactory) sessionContext.lookup("STATEFUL3EMF");
            setEntityManagerFactory(emf);
        } catch (Exception e) {
            System.out.println("ERROR: " + " In PostConstruct: Exception caught while setting EntityManagerFactory");
            e.printStackTrace();
        }
    }

    // ================== business methods ====================================
    @Remove
    @Transactional(Transactional.TxType.REQUIRED)
    public RemoteStatus runTest(String[] args, Properties props) {
        RemoteStatus retValue;
        props.put("persistence.unit.name", "CTS-EM");
        retValue = super.runTest(args, props);
        return retValue;
    }
    /////////////////////////////////////////////////////////////////////////

    @Resource
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @PersistenceContext(unitName = "CTS-EM")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }

    protected EntityTransaction getEntityTransaction() {
        return new NoopTransactionWrapper();
    }
}
