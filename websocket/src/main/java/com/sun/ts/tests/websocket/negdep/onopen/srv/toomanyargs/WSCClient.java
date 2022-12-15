/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.onopen.srv.toomanyargs;

import com.sun.ts.tests.websocket.negdep.NegativeDeploymentClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 *                     tslib.name;
 */
/**
 * @since 1.11
 */
public class WSCClient extends NegativeDeploymentClient {

  private static final long serialVersionUID = 111L;

  public WSCClient() {
    setContextRoot("wsc_negdep_onopen_srv_toomanyarguments_web");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  /*
   * @testName: tooManyArgumentsOnOpenUndeployOtherEndpointTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.4-3;
   * 
   * @test_Strategy: In both cases, a deployment error raised during the
   * deployment process must halt the deployment of the application, any well
   * formed endpoints deployed prior to the error being raised must be removed
   * from service and no more websocket endpoints from that application may be
   * deployed by the container, even if they are valid.
   * 
   * The value attribute of this annotation must be present otherwise the
   * implementation must throw an error. [WSC-4.4-3]
   * 
   * To verify the test fails when the deployment was successful, remove the
   * String value in @OnOpen of OnOpenServerEndpoint
   */
  public void tooManyArgumentsOnOpenUndeployOtherEndpointTest() throws Exception {
    setProperty(Property.CONTENT, "anything");
    setProperty(Property.REQUEST, buildRequest("echo"));
    setProperty(Property.STATUS_CODE, "-1");
    // logExceptionOnInvocation(false);
    try {
      invoke(false);
    } catch (Exception tfe) {
      // DeploymentException
    }
    String response = getResponseAsString();
    if ("anything".equals(response))
      throwValidEndpointMustBeRemoved();
  }

  /*
   * @testName: tooManyArgumentsOnOpenTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.4-3;
   * 
   * @test_Strategy: In both cases, a deployment error raised during the
   * deployment process must halt the deployment of the application, any well
   * formed endpoints deployed prior to the error being raised must be removed
   * from service and no more websocket endpoints from that application may be
   * deployed by the container, even if they are valid.
   * 
   * Any Java class using this annotation on a method that does not follow these
   * rules, or that uses this annotation on more than one method may not be
   * deployed by the implementation and the error reported to the deployer.
   * [WSC-4.4-3]
   * 
   * To verify the test fails when the deployment was successful, remove the
   * String value in @OnOpen of OnOpenServerEndpoint
   */
  public void tooManyArgumentsOnOpenTest() throws Exception {
    setProperty(Property.CONTENT, "anything");
    setProperty(Property.REQUEST, buildRequest("invalid"));
    setProperty(Property.STATUS_CODE, "-1");
    // logExceptionOnInvocation(false);
    try {
      invoke(false);
    } catch (Exception tfe) {
      // DeploymentException
    }
    String response = getResponseAsString();
    if (response != null && response.contains("anything"))
      throwValidEndpointMustBeRemoved();
  }
}