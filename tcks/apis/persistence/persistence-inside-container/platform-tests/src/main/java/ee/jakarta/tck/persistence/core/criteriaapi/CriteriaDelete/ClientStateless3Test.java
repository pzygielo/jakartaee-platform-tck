package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaDelete;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientStateless3Test extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaDelete.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle";

    public static void main(String[] args) {
      ClientStateless3Test theTests = new ClientStateless3Test();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriapia_CriteriaDelete: META-INF/persistence.xml
        jpa_core_criteriapia_CriteriaDelete_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaDelete_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaDelete_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaDelete_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaDelete_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriapia_CriteriaDelete_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriapia_CriteriaDelete_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaDelete_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaDelete_vehicles: 

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaDelete_vehicles_client.jar");
            // The class files
            jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
            EETest.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class,
            com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.criteriaapi.CriteriaDelete.Client.class,
            ClientStateless3Test.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.core.criteriaapi.CriteriaDelete.Client.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb, Client.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaDelete = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaDelete.jar");
            // The class files
            jpa_core_criteriapia_CriteriaDelete.addClasses(
                ee.jakarta.tck.persistence.common.schema30.Department.class,
                ee.jakarta.tck.persistence.common.schema30.Address_.class,
                ee.jakarta.tck.persistence.common.schema30.Department_.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard.class,
                ee.jakarta.tck.persistence.common.schema30.Info.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem_.class,
                ee.jakarta.tck.persistence.common.schema30.Phone.class,
                ee.jakarta.tck.persistence.common.schema30.Customer_.class,
                ee.jakarta.tck.persistence.common.schema30.Employee_.class,
                ee.jakarta.tck.persistence.common.schema30.Trim_.class,
                ee.jakarta.tck.persistence.common.schema30.Order_.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife.class,
                ee.jakarta.tck.persistence.common.schema30.Phone_.class,
                ee.jakarta.tck.persistence.common.schema30.Address.class,
                ee.jakarta.tck.persistence.common.schema30.Info_.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Country_.class,
                ee.jakarta.tck.persistence.common.schema30.Alias_.class,
                ee.jakarta.tck.persistence.common.schema30.Trim.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard_.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Product.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse_.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem.class,
                ee.jakarta.tck.persistence.common.schema30.Employee.class,
                ee.jakarta.tck.persistence.common.schema30.Product_.class,
                ee.jakarta.tck.persistence.common.schema30.Customer.class,
                ee.jakarta.tck.persistence.common.schema30.Alias.class,
                ee.jakarta.tck.persistence.common.schema30.Order.class,
                ee.jakarta.tck.persistence.common.schema30.LineItemException.class,
                ee.jakarta.tck.persistence.common.schema30.Country.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaDelete.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaDelete.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaDelete.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaDelete.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriapia_CriteriaDelete, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaDelete.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriapia_CriteriaDelete_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriapia_CriteriaDelete_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriapia_CriteriaDelete_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_ejb);
            jpa_core_criteriapia_CriteriaDelete_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaDelete_stateless3_vehicle_client);

            jpa_core_criteriapia_CriteriaDelete_vehicles_ear.addAsLibrary(jpa_core_criteriapia_CriteriaDelete);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaDelete_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriapia_CriteriaDelete_vehicles_ear, Client.class, earResURL);
        return jpa_core_criteriapia_CriteriaDelete_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void fromClassTest() throws java.lang.Exception {
            super.fromClassTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void fromEntityTypeTest() throws java.lang.Exception {
            super.fromEntityTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void getRootTest() throws java.lang.Exception {
            super.getRootTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void whereExpressionTest() throws java.lang.Exception {
            super.whereExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void wherePredicateArrayTest() throws java.lang.Exception {
            super.wherePredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void subquery() throws java.lang.Exception {
            super.subquery();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void modifiedQueryTest() throws java.lang.Exception {
            super.modifiedQueryTest();
        }

}
