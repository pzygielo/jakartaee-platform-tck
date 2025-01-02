/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.patchtests;

import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Properties;
import java.net.URL;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonp.api.common.JsonPTest;
import com.sun.ts.tests.jsonp.api.common.TestResult;

import jakarta.json.JsonPatch;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import tck.arquillian.protocol.common.TargetVehicle;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;

import java.lang.System.Logger;

// $Id$
/*
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 */
@Tag("tck-javatest")
@Tag("platform")
@Tag("web")
@Tag("jsonp")
@ExtendWith(ArquillianExtension.class)
public class PatchJspTest extends ServiceEETest {

  private static final Logger logger = System.getLogger(PatchJspTest.class.getName());

  private static String packagePath = PatchJspTest.class.getPackageName().replace(".", "/");

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "cleanup ok");
  }


  static final String VEHICLE_ARCHIVE = "patchtests_jsp_vehicle";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, testable = true)
  public static WebArchive createJspDeployment() throws IOException {
  
    WebArchive patchtests_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "patchtests_jsp_vehicle_web.war");
    patchtests_jsp_vehicle_web.addClass(PatchJspTest.class)
    .addClass(CommonOperation.class)
    .addClass(PatchCreate.class)
    .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class)
    .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class)
    .addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class)
    .addClass(com.sun.ts.lib.harness.EETest.class)
    .addClass(com.sun.ts.lib.harness.RemoteStatus.class)
    .addClass(com.sun.ts.lib.harness.Status.class)
    .addClass(com.sun.ts.lib.harness.ServiceEETest.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.ArrayBuilder.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.JsonAssert.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.JsonIO.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.JsonPTest.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.JsonValueType.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.MergeRFCObject.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.ObjectBuilder.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.PointerRFCObject.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.SimpleValues.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.TestFail.class)
    .addClass(com.sun.ts.tests.jsonp.api.common.TestResult.class)
    .addClass(com.sun.ts.tests.jsonp.common.JSONP_Util.class);


    // The web.xml descriptor
    URL warResURL = PatchJspTest.class.getClassLoader().getResource(packagePath+"/jsp_vehicle_web.xml");
    if(warResURL != null) {
      patchtests_jsp_vehicle_web.setWebXML(warResURL);
    }

    // Web content
    URL resURL = PatchJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
    patchtests_jsp_vehicle_web.addAsWebResource(resURL, "/jsp_vehicle.jsp");
    resURL = PatchJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
    patchtests_jsp_vehicle_web.addAsWebResource(resURL, "/client.html");

    return patchtests_jsp_vehicle_web;
    // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "patchtests_jsp_vehicle.ear");
    // ear.addAsModule(patchtests_jsp_vehicle_web);

    // return ear;

  }

    /*
     * @class.setup_props:
     * This is needed by the vehicle base classes
     */
  public void setup(String[] args, Properties p) throws Exception {

  }


  /**
   * Test {@link JsonPatch} factory methods added in JSON-P 1.1.
   *
   * @throws Exception
   *           when this test failed.
   *
   * @testName: jsonCreatePatch11Test
   *
   * @assertion_ids: JSONP:JAVADOC:574; JSONP:JAVADOC:579; JSONP:JAVADOC:581;
   *                 JSONP:JAVADOC:653; JSONP:JAVADOC:658; JSONP:JAVADOC:660;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:621;
   *
   * @test_Strategy: Tests JsonPatch API factory methods added in JSON-P 1.1.
   */
  @Test
  @TargetVehicle("jsp")
  public void jsonCreatePatch11Test() throws Exception {
    PatchCreate createTest = new PatchCreate();
    final TestResult result = createTest.test();
    result.eval();
  }

}