/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.joincolumn;


import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	private static final long serialVersionUID = 22L;

	public Client() {
	}

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: didTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:91;
	 * 
	 * @test_Strategy: follow up test core/derivedod/ex2a but without @JoinColumns
	 */
	public void didTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			final DID2EmployeeId eId1 = new DID2EmployeeId("Java", "Duke");
			final DID2EmployeeId eId2 = new DID2EmployeeId("C", "foo");
			final DID2Employee employee1 = new DID2Employee(eId1);
			final DID2Employee employee2 = new DID2Employee(eId2);

			final DID2DependentId dId1 = new DID2DependentId("Obama", eId1);
			final DID2DependentId dId2 = new DID2DependentId("Michelle", eId1);
			final DID2DependentId dId3 = new DID2DependentId("John", eId2);

			final DID2Dependent dep1 = new DID2Dependent(dId1, employee1);
			final DID2Dependent dep2 = new DID2Dependent(dId2, employee1);
			final DID2Dependent dep3 = new DID2Dependent(dId3, employee2);

			getEntityManager().persist(employee1);
			getEntityManager().persist(employee2);
			getEntityManager().persist(dep1);
			getEntityManager().persist(dep2);
			getEntityManager().persist(dep3);

			getEntityManager().flush();
			logTrace( "persisted Employees and Dependents");

			// Refresh Dependent
			DID2Dependent newDependent = getEntityManager().find(DID2Dependent.class,
					new DID2DependentId("Obama", new DID2EmployeeId("Java", "Duke")));
			if (newDependent != null) {
				getEntityManager().refresh(newDependent);
			}

			List<?> depList = getEntityManager()
					.createQuery("Select d from DID2Dependent d where d.name='Obama' and d.emp.firstName='Java'")
					.getResultList();
			newDependent = null;
			if (depList.size() > 0) {
				newDependent = (DID2Dependent) depList.get(0);
				if (newDependent == dep1) {
					pass = true;
					logTrace( "Received Expected Dependent");
				} else {
					logErr( "Searched Dependent not found");
				}
			} else {
				logErr( "getEntityManager().createQuery returned null entry");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			getEntityTransaction().rollback();
		}

		if (!pass) {
			throw new Exception("DIDTest failed");
		}
	}

	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM DID2DEPENDENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID2EMPLOYEE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
