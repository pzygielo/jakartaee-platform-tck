/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.types.property;


import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.types.common.Grade;

public class Client1 extends PMClientBase {

	

	private DataTypes d1;

	private DataTypes2 d2;

	private final java.util.Date dateId = getPKDate(2006, 04, 15);

	private final java.sql.Date dateValue = getSQLDate(2006, 04, 15);

	public Client1() {
	}

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			removeTestData();
			createTestData();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: propertyTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515;
	 * PERSISTENCE:SPEC:516; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Character
	 */
	
	public void propertyTypeTest1() throws Exception {

		boolean pass = false;
		final Character newChar = 'b';

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getCharacterData().equals('a')) {
					d1.setCharacterData(newChar);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getCharacterData().equals(newChar)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest1 failed");
	}

	/*
	 * @testName: propertyTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Short
	 */
	
	public void propertyTypeTest2() throws Exception {

		boolean pass = false;
		final Short newShort = 101;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getShortData().equals((short) 100)) {
					d1.setShortData(newShort);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getShortData().equals(newShort)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest2 failed");
	}

	/*
	 * @testName: propertyTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Integer
	 */
	
	public void propertyTypeTest3() throws Exception {

		boolean pass = false;
		final Integer newInt = 500;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getIntegerData().equals(500)) {
					d1.setIntegerData(newInt);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getIntegerData().equals(newInt)) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest3 failed");
	}

	/*
	 * @testName: propertyTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Long
	 */
	
	public void propertyTypeTest4() throws Exception {

		boolean pass = false;
		final Long newLong = 600L;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getLongData().equals(300L)) {
					d1.setLongData(newLong);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getLongData().equals(newLong)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest4 failed");
	}

	/*
	 * @testName: propertyTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Double
	 */
	
	public void propertyTypeTest5() throws Exception {

		boolean pass = false;
		final Double newDbl = 80D;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getDoubleData().equals(50D)) {
					d1.setDoubleData(newDbl);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getDoubleData().equals(newDbl)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest5 failed");

	}

	/*
	 * @testName: propertyTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Float
	 */
	
	public void propertyTypeTest6() throws Exception {

		boolean pass = false;
		final Float newFloat = 3.0F;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getFloatData().equals(1.0F)) {
					d1.setFloatData(newFloat);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getFloatData().equals(newFloat)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest6 failed");

	}

	/*
	 * @testName: propertyTypeTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:JAVADOC:72;
	 * PERSISTENCE:JAVADOC:73; PERSISTENCE:SPEC:1976; PERSISTENCE:SPEC:2017
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: enums
	 *
	 * Using the Enumerated annotation, with EnumType.ORDINAL.
	 */
	
	public void propertyTypeTest7() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D1 IN propertyTypeTest7");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting enumData ");
				d1.setEnumData(Grade.C);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "Check results");
				if (d1.getEnumData().equals(Grade.C)) {
					logTrace( "Expected Grade of:" + d1.getEnumData() + "received");
					pass = true;
				} else {
					logErr(
							"Did not get expected results.  Expected C, got: " + d1.getEnumData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest7 failed");
	}

	/*
	 * @testName: propertyTypeTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
	 * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:SPEC:1079;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The primary key should be one of the following types:
	 * java.util.Date
	 * 
	 * The application must not change the value of the primary key. The behavior is
	 * undefined if this occurs.
	 *
	 * Temporal.TemporalType.DATE
	 */
	
	public void propertyTypeTest8() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2 IN propertyTypeTest8");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			logTrace( "Check results");
			if (null != d2) {
				if (d2.getId().equals(dateId)) {
					logTrace( "Got expected PK of:" + d2.getId() + "received");
					pass = true;
				} else {
					logErr(
							"Did not get expected results. " + "Expected " + dateId + ", got: " + d2.getId());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest8 failed");
	}

	/*
	 * @testName: propertyTypeTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: byte[]
	 *
	 */
	
	public void propertyTypeTest9() throws Exception {

		boolean pass = false;
		final byte[] b = { 31, 32, 33, 63, 64, 65 };
		final Byte bv = 5;
		byte[] a = null;

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D1 IN propertyTypeTest9");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting byteData ");
				d1.setByteArrayData(b);
				a = d1.getByteArrayData();
				a[0] = (byte) (a[0] + bv);
				d1.setByteArrayData(b);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "Check results");
				if (Arrays.equals(d1.getByteArrayData(), a)) {
					logTrace( "propertyTypeTest9: Expected results received");
					pass = true;
				} else {
					logErr( "Unexpected result in array comparison.");
					for (int i = 0; i < a.length; i++) {
						logTrace( "Array a in propertyTest9 equals: " + a[i]);
					}
					for (int j = 0; j < b.length; j++) {
						logTrace( "Array b in propertyTest9 equals: " + b[j]);
					}
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest9 failed");
	}

	/*
	 * @testName: propertyTypeTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: char[]
	 *
	 */
	
	public void propertyTypeTest10() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			char[] charData = new char[] { 'C', 'T', 'S' };
			logTrace( "FIND D1 IN propertyTypeTest10");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logTrace( "DataType Entity is not null, setting CharacterData ");
				d1.setCharArrayData(charData);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logTrace( "propertyTypeTest10:  Check results");
				if (Arrays.equals(d1.getCharArrayData(), charData)) {
					logTrace( "propertyTypeTest10: Expected Results Received");
					pass = true;
				} else {
					logErr( "Did not get expected results. " + "Expected "
							+ Arrays.toString(charData) + ", got: " + Arrays.toString(d1.getCharArrayData()));
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest10 failed");
	}

	/*
	 * @testName: propertyTypeTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Time
	 */
	
	public void propertyTypeTest11() throws Exception {

		boolean pass = false;
		final java.sql.Time timeValue = getTimeData(18, 30, 15);

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2 IN propertyTypeTest11");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logTrace( "DataType Entity is not null, setting TimeData ");
				d2.setTimeData(timeValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "propertyTypeTest11:  Check results");
				if (d2.getTimeData().equals(timeValue)) {
					logTrace( "propertyTypeTest11: Expected Time Received");
					pass = true;
				} else {
					logErr( "Did not get expected results. " + " Expected " + timeValue
							+ " , got: " + d2.getTimeData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest11 failed");
	}

	/*
	 * @testName: propertyTypeTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Timestamp
	 */
	
	public void propertyTypeTest12() throws Exception {

		boolean pass = false;
		final java.sql.Timestamp tsValue = getTimestampData(2006, 02, 11);

		try {
			getEntityTransaction().begin();
			logTrace( "FIND D2 IN propertyTypeTest12");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logTrace( "DataType Entity is not null, setting TimestampData ");
				d2.setTsData(tsValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logTrace( "propertyTypeTest12:  Check results");
				if (d2.getTsData().equals(tsValue)) {
					logTrace( "propertyTypeTest12: Expected Timestamp Received");
					pass = true;
				} else {
					logErr(
							"Did not get expected results. " + " Expected " + tsValue + " , got: " + d2.getTsData());
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("propertyTypeTest12 failed");
	}

	/*
	 * @testName: scalarExpressionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2512; PERSISTENCE:SPEC:1643;
	 * 
	 * @test_Strategy: Test various scalar expressions test
	 *
	 */
	
	public void scalarExpressionsTest() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5;
		pass1 = pass2 = pass3 = pass4 = pass5 = false;
		List<DataTypes> p;
		try {
			getEntityTransaction().begin();
			int expected = d1.getIntegerData() + 1;
			logTrace( "Testing arithmetic expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where ((d.integerData + 1) = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() == 1) {
				DataTypes d = p.get(0);
				logTrace( "DataType:" + d.toString());
				int actual = d.getIntegerData() + 1;
				if (actual == expected) {
					logTrace( "Received expected Integer data:" + actual);
					pass1 = true;
				} else {
					logErr(
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}
			} else {
				logErr(
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			getEntityTransaction().begin();
			char expected = 'a';
			logTrace( "Testing string expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where (d.characterData = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() == 1) {
				DataTypes d = p.get(0);
				logTrace( "DataType:" + d.toString());
				Character actual = d.getCharacterData();
				if (actual.equals(expected)) {
					logTrace( "Received expected Character data:" + actual);
					pass2 = true;
				} else {
					logErr(
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}
			} else {
				logErr(
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		try {
			getEntityTransaction().begin();
			Grade expected = Grade.C;
			logTrace( "Testing enum expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where (d.enumData = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() == 1) {
				DataTypes d = p.get(0);
				logTrace( "DataType:" + d.toString());
				Grade actual = d.getEnumData();
				if (actual.equals(expected)) {
					logTrace( "Received expected Enum data:" + actual);
					pass3 = true;
				} else {
					logErr(
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}

			} else {
				logErr(
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			final java.util.Date dateValue = getPKDate(2006, 01, 01);
			java.util.Date expected = dateId;
			getEntityTransaction().begin();
			logTrace( "Testing datetime expression:");
			List<DataTypes2> pp = getEntityManager().createQuery("Select d From DataTypes2 d where (d.dateData > ?1)")
					.setParameter(1, dateValue).getResultList();

			if (pp.size() == 1) {
				DataTypes2 d = pp.get(0);
				logTrace( "DataType2:" + d.toString());
				java.util.Date actual = d.getDateData();
				if (actual.equals(expected)) {
					logTrace( "Received expected Date data:" + actual);
					pass4 = true;
				} else {
					logErr(
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}

			} else {
				logErr(
						"Did not get expected number of results, expected:1, actual:" + pp.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}
		try {
			getEntityTransaction().begin();
			boolean expected = true;
			logTrace( "Testing boolean expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where (d.booleanData = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() > 0) {
				DataTypes d = p.get(0);
				logTrace( "DataType:" + d.toString());
				Boolean actual = d.getBooleanData();
				if (actual.equals(expected)) {
					logTrace( "Received expected Boolean data:" + actual);
					pass5 = true;
				} else {
					logErr(
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}
			} else {
				logErr(
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5)
			throw new Exception("scalarExpressionsTest failed");
	}

	// Methods used for Tests

	public void createTestData() {
		try {
			getEntityTransaction().begin();

			char[] cArray = { 'a' };
			byte[] bArray = { (byte) 100 };
			d1 = new DataTypes(1, 'a', true, (short) 100, 500, 300L, 50D, 1.0F, cArray, bArray);
			d1.setEnumData(Grade.C);

			d2 = new DataTypes2(dateId);
			d2.setDateData(dateValue);

			getEntityManager().persist(d1);
			getEntityManager().persist(d2);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

	}

	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
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
