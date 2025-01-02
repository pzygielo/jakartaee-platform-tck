package ee.jakarta.tck.persistence.core.annotations.version;

import ee.jakarta.tck.persistence.core.annotations.version.Client4;
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
public class Client4PmservletTest extends ee.jakarta.tck.persistence.core.annotations.version.Client4 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_version_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_version: META-INF/persistence.xml
        jpa_core_annotations_version_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_version_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_version_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_version_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_version_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_version_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_version_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_version_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_version_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_version_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_version_vehicles: 

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_annotations_version_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_version_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_version_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.annotations.version.Client4.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.annotations.version.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_version_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_version_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client4.class.getResource("/com/sun/ts/tests/jpa/core/annotations/version/jpa_core_annotations_version.jar");
            if(warResURL != null) {
              jpa_core_annotations_version_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_annotations_version.jar");
            }
            warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_version_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_annotations_version_pmservlet_vehicle_web, Client4.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_version = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_version.jar");
            // The class files
            jpa_core_annotations_version.addClasses(
                ee.jakarta.tck.persistence.core.annotations.version.Short_Property.class,
                ee.jakarta.tck.persistence.core.annotations.version.Integer_Property.class,
                ee.jakarta.tck.persistence.core.annotations.version.LongClass_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.Timestamp_Property.class,
                ee.jakarta.tck.persistence.core.annotations.version.ShortClass_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.Long_Property.class,
                ee.jakarta.tck.persistence.core.annotations.version.LongClass_Property.class,
                ee.jakarta.tck.persistence.core.annotations.version.Timestamp_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.Short_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.Long_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.ShortClass_Property.class,
                ee.jakarta.tck.persistence.core.annotations.version.Integer_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.Int_Field.class,
                ee.jakarta.tck.persistence.core.annotations.version.Int_Property.class
            );
            // The persistence.xml descriptor
            URL parURL = Client4.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_version.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client4.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_annotations_version.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_annotations_version.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_annotations_version.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_annotations_version, Client4.class, parURL);
            parURL = Client4.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_version.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_annotations_version_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_annotations_version_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_annotations_version_vehicles_ear.addAsModule(jpa_core_annotations_version_pmservlet_vehicle_web);

            jpa_core_annotations_version_vehicles_ear.addAsLibrary(jpa_core_annotations_version);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client4.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_annotations_version_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_annotations_version_vehicles_ear, Client4.class, earResURL);
        return jpa_core_annotations_version_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void timestampFieldTest() throws java.lang.Exception {
            super.timestampFieldTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void timestampPropertyTest() throws java.lang.Exception {
            super.timestampPropertyTest();
        }


}