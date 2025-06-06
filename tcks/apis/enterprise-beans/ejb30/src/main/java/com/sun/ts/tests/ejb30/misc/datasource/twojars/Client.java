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

package com.sun.ts.tests.ejb30.misc.datasource.twojars;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.ejb.EJB;

@DataSourceDefinitions({
    @DataSourceDefinition(name = "java:global/datasource/twojars/appclient/globalds",
            description = "override it with <data-source> in application-client.xml",
            className = "org.apache.derby.jdbc.ClientDataSource",
            portNumber = 8080,
            serverName = "localhost",
            databaseName = "derbyDB",
            user = "cts1",
            password = "cts1",
            transactional = true)
})
public class Client extends EETest {
  private static StringBuilder postConstructRecords = new StringBuilder();

  @EJB(beanName = "DataSourceBean", name = "java:global/ejb3_misc_datasource_twojars/ejb3_misc_datasource_twojars_ejb/DataSourceBean")
  private static AppResRemoteIF dataSourceBean;

  @EJB(lookup = "java:global/ejb3_2standalone_component_ejb/DataSource2Bean")
  private static AppResRemoteIF dataSource2Bean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) {
  }

  public void cleanup() {
  }

  private static void nonPostConstruct() {
    ServiceLocator.lookupShouldFail("java:app/datasource/twojars/2/appds",
        postConstructRecords);
    Helper.getLogger().info(postConstructRecords.toString());

    DataSourceTest.verifyDataSource(postConstructRecords, false,
        "java:global/datasource/twojars/appclient/globalds",
        "java:app/datasource/twojars/appclient/appds",

        "java:global/datasource/twojars/2/globalds");
  }

  /*
   * @testName: clientPostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside appclient
   */
  public void clientPostConstruct() {
    nonPostConstruct();
    Helper.getLogger().info(postConstructRecords.toString());
  }

  /*
   * @testName: ejbPostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside ejb
   */
  public void ejbPostConstruct() {
    Helper.getLogger()
        .info(dataSourceBean.getPostConstructRecords().toString());
  }

  /*
   * @testName: ejb2PostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside standalone ejb module
   */
  public void ejb2PostConstruct() {
    Helper.getLogger()
        .info(dataSource2Bean.getPostConstructRecords().toString());
  }

}
