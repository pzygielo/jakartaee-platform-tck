package com.sun.ts.tests.jms.core.objectMsgTopic;

import com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests;
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
public class ObjectMsgTopicTestsEjbTest extends com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "objectMsgTopic_ejb_vehicle";

        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive objectMsgTopic_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "objectMsgTopic_ejb_vehicle_client.jar");
            // The class files
            objectMsgTopic_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests.class
            );
            // The application-client.xml descriptor
            URL resURL = ObjectMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              objectMsgTopic_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ObjectMsgTopicTests.class.getResource("objectMsgTopic_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              objectMsgTopic_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //objectMsgTopic_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ObjectMsgTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            objectMsgTopic_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(objectMsgTopic_ejb_vehicle_client, ObjectMsgTopicTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive objectMsgTopic_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "objectMsgTopic_ejb_vehicle_ejb.jar");
            // The class files
            objectMsgTopic_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ObjectMsgTopicTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              objectMsgTopic_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ObjectMsgTopicTests.class.getResource("objectMsgTopic_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              objectMsgTopic_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(objectMsgTopic_ejb_vehicle_ejb, ObjectMsgTopicTests.class, ejbResURL);

        // Ear
            EnterpriseArchive objectMsgTopic_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "objectMsgTopic_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            objectMsgTopic_ejb_vehicle_ear.addAsModule(objectMsgTopic_ejb_vehicle_ejb);
            objectMsgTopic_ejb_vehicle_ear.addAsModule(objectMsgTopic_ejb_vehicle_client);



        return objectMsgTopic_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void messageObjectCopyTopicTest() throws java.lang.Exception {
            super.messageObjectCopyTopicTest();
        }


}