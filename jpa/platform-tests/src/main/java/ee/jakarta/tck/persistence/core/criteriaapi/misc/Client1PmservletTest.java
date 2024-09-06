package ee.jakarta.tck.persistence.core.criteriaapi.misc;

import ee.jakarta.tck.persistence.core.criteriaapi.misc.Client1;
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
public class Client1PmservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.misc.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_misc_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriaapi_misc: META-INF/persistence.xml
        jpa_core_criteriaapi_misc_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_misc_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_misc_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_misc_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_misc_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_misc_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_misc_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_misc_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_misc_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_misc_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_misc_vehicles: 

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
            WebArchive jpa_core_criteriaapi_misc_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_misc_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_misc_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            ee.jakarta.tck.persistence.core.criteriaapi.misc.Client1.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_misc_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client1.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_misc_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/misc/jpa_core_criteriaapi_misc.jar");
            if(warResURL != null) {
              jpa_core_criteriaapi_misc_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriaapi_misc.jar");
            }
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_misc_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_misc_pmservlet_vehicle_web, Client1.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_misc = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_misc.jar");
            // The class files
            jpa_core_criteriaapi_misc.addClasses(
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
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_misc.addAsManifestResource(parURL, "persistence.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_misc, Client1.class, parURL);
            // The orm.xml file
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_misc.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriaapi_misc_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_misc_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriaapi_misc_vehicles_ear.addAsModule(jpa_core_criteriaapi_misc_pmservlet_vehicle_web);

            jpa_core_criteriaapi_misc_vehicles_ear.addAsLibrary(jpa_core_criteriaapi_misc);



            // The application.xml descriptor
            URL earResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/misc/");
            if(earResURL != null) {
              jpa_core_criteriaapi_misc_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/misc/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriaapi_misc_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriaapi_misc_vehicles_ear, Client1.class, earResURL);
        return jpa_core_criteriaapi_misc_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void predicateIsNegatedTest() throws java.lang.Exception {
            super.predicateIsNegatedTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void predicateBooleanOperatorTest() throws java.lang.Exception {
            super.predicateBooleanOperatorTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void predicateGetOperatorTest() throws java.lang.Exception {
            super.predicateGetOperatorTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void selectionGetCompoundSelectionItemsIllegalStateExceptionTest() throws java.lang.Exception {
            super.selectionGetCompoundSelectionItemsIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void expressionIsCompoundSelectionTest() throws java.lang.Exception {
            super.expressionIsCompoundSelectionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void expressionGetCompoundSelectionItemsIllegalStateExceptionTest() throws java.lang.Exception {
            super.expressionGetCompoundSelectionItemsIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getRoots() throws java.lang.Exception {
            super.getRoots();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSelection() throws java.lang.Exception {
            super.getSelection();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getGroupList() throws java.lang.Exception {
            super.getGroupList();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void isDistinct() throws java.lang.Exception {
            super.isDistinct();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getResultType() throws java.lang.Exception {
            super.getResultType();
        }


}