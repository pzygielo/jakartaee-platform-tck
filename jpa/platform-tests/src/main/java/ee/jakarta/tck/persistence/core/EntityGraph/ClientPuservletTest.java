package ee.jakarta.tck.persistence.core.EntityGraph;

import ee.jakarta.tck.persistence.core.EntityGraph.Client;
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
@Tag("persistence")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientPuservletTest extends ee.jakarta.tck.persistence.core.EntityGraph.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_EntityGraph_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_EntityGraph: META-INF/persistence.xml
        jpa_core_EntityGraph_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_EntityGraph_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_EntityGraph_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_EntityGraph_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_EntityGraph_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_EntityGraph_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_EntityGraph_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_EntityGraph_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_EntityGraph_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_EntityGraph_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_EntityGraph_vehicles: 

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_EntityGraph_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_EntityGraph_puservlet_vehicle_web.war");
            // The class files
            jpa_core_EntityGraph_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.core.EntityGraph.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_EntityGraph_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_EntityGraph_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/EntityGraph/jpa_core_EntityGraph.jar");
            if(warResURL != null) {
              jpa_core_EntityGraph_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_EntityGraph.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_EntityGraph_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_EntityGraph_puservlet_vehicle_web, Client.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_EntityGraph = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph.jar");
            // The class files
            jpa_core_EntityGraph.addClasses(
                ee.jakarta.tck.persistence.core.EntityGraph.Employee2.class,
                ee.jakarta.tck.persistence.core.EntityGraph.Department.class,
                ee.jakarta.tck.persistence.core.EntityGraph.Employee.class,
                ee.jakarta.tck.persistence.core.EntityGraph.Employee3.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_EntityGraph.addAsManifestResource(parURL, "persistence.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_EntityGraph, Client.class, parURL);
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_EntityGraph.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_EntityGraph_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_EntityGraph_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_EntityGraph_vehicles_ear.addAsModule(jpa_core_EntityGraph_puservlet_vehicle_web);

            jpa_core_EntityGraph_vehicles_ear.addAsLibrary(jpa_core_EntityGraph);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/EntityGraph/");
            if(earResURL != null) {
              jpa_core_EntityGraph_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/EntityGraph/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_EntityGraph_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_EntityGraph_vehicles_ear, Client.class, earResURL);
        return jpa_core_EntityGraph_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void addAttributeNodesStringArrayTest() throws java.lang.Exception {
            super.addAttributeNodesStringArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void addAttributeNodesStringArrayIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.addAttributeNodesStringArrayIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void addAttributeNodesAttributeArrayTest() throws java.lang.Exception {
            super.addAttributeNodesAttributeArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void createEntityGraphStringTest() throws java.lang.Exception {
            super.createEntityGraphStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getEntityGraphStringTest() throws java.lang.Exception {
            super.getEntityGraphStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void entityGraphGetNameTest() throws java.lang.Exception {
            super.entityGraphGetNameTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void entityGraphGetNameNoNameExistsTest() throws java.lang.Exception {
            super.entityGraphGetNameNoNameExistsTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getNameTest() throws java.lang.Exception {
            super.getNameTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getEntityGraphStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getEntityGraphStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getEntityGraphsClassTest() throws java.lang.Exception {
            super.getEntityGraphsClassTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void addNamedEntityGraphStringEntityGraphTest() throws java.lang.Exception {
            super.addNamedEntityGraphStringEntityGraphTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getEntityGraphsClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getEntityGraphsClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void annotationsTest() throws java.lang.Exception {
            super.annotationsTest();
        }


}