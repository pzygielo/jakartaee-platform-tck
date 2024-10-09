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
 * @(#)dbMetaClient4.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta4;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The dbMetaClient4 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dbMetaClient4EJB extends dbMetaClient4 implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta4";
  
  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
	@Deployment(name = "ejb",  testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta4_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient4EJB.class, dbMetaClient4.class);

		URL resURL = dbMetaClient4EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");
		
		resURL = dbMetaClient4EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta4/dbMeta4_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}


		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta4_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient4EJB.class, dbMetaClient4.class);


		resURL = dbMetaClient4EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta4/dbMeta4_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient4EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta4/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta4_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient4EJB theTests = new dbMetaClient4EJB();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSupportsConvert23
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(STRUCT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert23() throws Exception {
		super.testSupportsConvert23();
  }

  /*
   * @testName: testSupportsConvert24
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TIME, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert24() throws Exception {
		super.testSupportsConvert24();
  }

  /*
   * @testName: testSupportsConvert25
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TIMESTAMP, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert25() throws Exception {
		super.testSupportsConvert25();
  }

  /*
   * @testName: testSupportsConvert26
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TINYINT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert26() throws Exception {
		super.testSupportsConvert26();
  }

  /*
   * @testName: testSupportsConvert27
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(VARBINARY, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert27() throws Exception {
		super.testSupportsConvert27();
  }

  /*
   * @testName: testSupportsConvert28
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIGINT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert28() throws Exception {
		super.testSupportsConvert28();
  }

  /*
   * @testName: testSupportsConvert29
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIT, INTEGER) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert29() throws Exception {
		super.testSupportsConvert29();
  }

  /*
   * @testName: testSupportsConvert30
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DATE, INTEGER) method onn that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert30() throws Exception {
		super.testSupportsConvert30();
  }

  /*
   * @testName: testSupportsConvert31
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DECIMAL, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert31() throws Exception {
		super.testSupportsConvert31();
  }

  /*
   * @testName: testSupportsConvert32
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DOUBLE, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert32() throws Exception {
		super.testSupportsConvert32();
  }

  /*
   * @testName: testSupportsConvert33
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(FLOAT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert33() throws Exception {
		super.testSupportsConvert33();
  }

  /*
   * @testName: testSupportsConvert34
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(NUMERIC, INTEGER) method on that
   * object. It should return a boolean value; true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert34() throws Exception {
		super.testSupportsConvert34();
  }

  /*
   * @testName: testSupportsConvert35
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(REAL, INTEGER) method on that object.
   * It should return a boolean value; either true false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert35() throws Exception {
		super.testSupportsConvert35();
  }

  /*
   * @testName: testSupportsConvert36
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(SMALLINT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert36() throws Exception {
		super.testSupportsConvert36();
  }

  /*
   * @testName: testSupportsConvert37
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TINYINT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsConvert37() throws Exception {
		super.testSupportsConvert37();
  }

  /*
   * @testName: testSupportsTableCorrelationNames
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:884; JDBC:JAVADOC:885;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsTableCorrelationNames() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsTableCorrelationNames() throws Exception {
		super.testSupportsTableCorrelationNames();
  }
	

  /*
   * @testName: testSupportsDifferentTableCorrelationNames
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:886; JDBC:JAVADOC:887;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsDifferentTableCorrelationNames() method on
   * that object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsDifferentTableCorrelationNames() throws Exception {
		super.testSupportsDifferentTableCorrelationNames();
  }

  /*
   * @testName: testSupportsExpressionsInOrderBy
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:888; JDBC:JAVADOC:889;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsExpressionsInOrderBy() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsExpressionsInOrderBy() throws Exception {
		super.testSupportsExpressionsInOrderBy();
  }

  /*
   * @testName: testSupportsOrderByUnrelated
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:890; JDBC:JAVADOC:891;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsOrderByUnrelated() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsOrderByUnrelated() throws Exception {
		super.testSupportsOrderByUnrelated();
  }

  /*
   * @testName: testSupportsGroupBy
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:892; JDBC:JAVADOC:893;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsGroupBy() method on that object. It should
   * return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testSupportsGroupBy() throws Exception {
		super.testSupportsGroupBy();
  }
}