package com.sun.ts.tests.ejb30.sec.stateless.lsecr;

import com.sun.ts.tests.ejb30.sec.stateless.lsecr.Client;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.sec.stateless.lsecr.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_sec_stateless_lsecr: ear.sun-application.xml
        ejb3_sec_stateless_lsecr_client: META-INF/application-client.xml,jar.sun-application-client.xml
        ejb3_sec_stateless_lsecr_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_client.xml
        /com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_ejb.xml
        /com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_ejb.jar.sun-ejb-jar.xml
        Ear:

        /com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr.ear.sun-application.xml
        /com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr.ear.sun-application.xml
        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_sec_stateless_lsecr", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_sec_stateless_lsecr_client = ShrinkWrap.create(JavaArchive.class, "ejb3_sec_stateless_lsecr_client.jar");
            // The class files
            ejb3_sec_stateless_lsecr_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.sec.stateless.common.lTest.class,
            com.sun.ts.tests.ejb30.sec.stateless.common.SecTestLocal.class,
            com.sun.ts.tests.ejb30.sec.stateless.common.SecTestRoleRefLocal.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.sec.stateless.lsecr.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.sec.stateless.common.SecTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_client.xml");
            if(resURL != null) {
              ejb3_sec_stateless_lsecr_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_sec_stateless_lsecr_client.addAsManifestResource(resURL, "application-client.xml");
            }
            ejb3_sec_stateless_lsecr_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_sec_stateless_lsecr_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_sec_stateless_lsecr_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_sec_stateless_lsecr_ejb.jar");
            // The class files
            ejb3_sec_stateless_lsecr_ejb.addClasses(
                com.sun.ts.tests.ejb30.sec.stateless.common.SecTestRoleRef.class,
                com.sun.ts.tests.ejb30.sec.stateless.common.lTest.class,
                com.sun.ts.tests.ejb30.sec.stateless.common.SecTestLocal.class,
                com.sun.ts.tests.ejb30.sec.stateless.common.SecTestRoleRefLocal.class,
                com.sun.ts.tests.ejb30.sec.stateless.common.SecTestRoleRefEJB.class,
                com.sun.ts.tests.ejb30.sec.stateless.lsecr.lTestEJB.class,
                com.sun.ts.tests.ejb30.sec.stateless.lsecr.SecTestEJB.class,
                com.sun.ts.tests.ejb30.sec.stateless.common.SecTest.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_ejb.xml");
            if(ejbResURL != null) {
              ejb3_sec_stateless_lsecr_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/sec/stateless/lsecr/ejb3_sec_stateless_lsecr_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_sec_stateless_lsecr_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_sec_stateless_lsecr_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_sec_stateless_lsecr_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_sec_stateless_lsecr.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_sec_stateless_lsecr_ear.addAsModule(ejb3_sec_stateless_lsecr_ejb);
            ejb3_sec_stateless_lsecr_ear.addAsModule(ejb3_sec_stateless_lsecr_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/sec/stateless/lsecr/application.xml");
            if(earResURL != null) {
              ejb3_sec_stateless_lsecr_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/sec/stateless/lsecr/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_sec_stateless_lsecr_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_sec_stateless_lsecr_ear, Client.class, earResURL);
        return ejb3_sec_stateless_lsecr_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        public void test3() throws java.lang.Exception {
            super.test3();
        }

        @Test
        @Override
        public void test4() throws java.lang.Exception {
            super.test4();
        }

        @Test
        @Override
        public void test5() throws java.lang.Exception {
            super.test5();
        }

        @Test
        @Override
        public void test2() throws java.lang.Exception {
            super.test2();
        }

        @Test
        @Override
        public void test6() throws java.lang.Exception {
            super.test6();
        }

        @Test
        @Override
        public void test7() throws java.lang.Exception {
            super.test7();
        }

        @Test
        @Override
        public void test8() throws java.lang.Exception {
            super.test8();
        }


}