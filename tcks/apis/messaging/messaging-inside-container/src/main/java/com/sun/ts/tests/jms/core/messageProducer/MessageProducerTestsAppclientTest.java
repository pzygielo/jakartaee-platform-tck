package com.sun.ts.tests.jms.core.messageProducer;

import com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MessageProducerTestsAppclientTest extends com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests {
    static final String VEHICLE_ARCHIVE = "messageProducer_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        messageProducer_appclient_vehicle: 
        messageProducer_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageProducer_ejb_vehicle: 
        messageProducer_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageProducer_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        messageProducer_jsp_vehicle: 
        messageProducer_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        messageProducer_servlet_vehicle: 
        messageProducer_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/messageProducer/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive messageProducer_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "messageProducer_appclient_vehicle_client.jar");
            // The class files
            messageProducer_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests.class
            );
            // The application-client.xml descriptor
            URL resURL = MessageProducerTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              messageProducer_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MessageProducerTests.class.getResource("messageProducer_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              messageProducer_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            messageProducer_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MessageProducerTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(messageProducer_appclient_vehicle_client, MessageProducerTests.class, resURL);

        // Ear
            EnterpriseArchive messageProducer_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "messageProducer_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            messageProducer_appclient_vehicle_ear.addAsModule(messageProducer_appclient_vehicle_client);



        return messageProducer_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendQueueTest1() throws java.lang.Exception {
            super.sendQueueTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendQueueTest2() throws java.lang.Exception {
            super.sendQueueTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendQueueTest3() throws java.lang.Exception {
            super.sendQueueTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendTopicTest4() throws java.lang.Exception {
            super.sendTopicTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendTopicTest5() throws java.lang.Exception {
            super.sendTopicTest5();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendTopicTest6() throws java.lang.Exception {
            super.sendTopicTest6();
        }


}