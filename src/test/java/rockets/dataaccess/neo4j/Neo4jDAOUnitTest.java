package rockets.dataaccess.neo4j;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;
import rockets.model.User;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4jDAOUnitTest {
    private DAO dao;
    private Session session;
    private SessionFactory sessionFactory;

    private LaunchServiceProvider esa;
    private LaunchServiceProvider spacex;
    private Rocket rocket;

    @BeforeAll
    public void initializeNeo4j() {
        ServerControls embeddedDatabaseServer = TestServerBuilders.newInProcessBuilder().newServer();
        GraphDatabaseService dbService = embeddedDatabaseServer.graph();
        EmbeddedDriver driver = new EmbeddedDriver(dbService);
        sessionFactory = new SessionFactory(driver, User.class.getPackage().getName());
        session = sessionFactory.openSession();
        dao = new Neo4jDAO(session);
    }

    @BeforeEach
    public void setup() {
        esa = new LaunchServiceProvider("ESA", 1970, "Europe");
        spacex = new LaunchServiceProvider("SpaceX", 2002, "USA");
        rocket = new Rocket("F9", "USA", spacex);
    }

    @Test
    public void shouldCreateNeo4jDAOSuccessfully() {
        assertNotNull(dao);
    }

    @Test
    public void shouldCreateARocketSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");
        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);
        LaunchServiceProvider manufacturer = graphRocket.getManufacturer();
        assertNotNull(manufacturer.getId());
        assertEquals(rocket.getWikilink(), graphRocket.getWikilink());
        assertEquals(spacex, manufacturer);
    }

    @Test
    public void shouldUpdateRocketAttributeSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");

        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);

        String newLink = "http://adifferentlink.com";
        rocket.setWikilink(newLink);
        dao.createOrUpdate(rocket);
        graphRocket = dao.load(Rocket.class, rocket.getId());
        assertEquals(newLink, graphRocket.getWikilink());
    }

    @Test
    public void shouldNotSaveTwoSameRockets() {
        assertNull(spacex.getId());
        Rocket rocket1 = new Rocket("F9", "USA", spacex);
        Rocket rocket2 = new Rocket("F9", "USA", spacex);
        assertEquals(rocket1, rocket2);

        dao.createOrUpdate(rocket1);
        assertNotNull(spacex.getId());
        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
        Collection<LaunchServiceProvider> manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());

        dao.createOrUpdate(rocket2);
        manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
    }

    @Test
    public void shouldLoadAllRockets() {
        Set<Rocket> rockets = Sets.newHashSet(
                new Rocket("Ariane4", "France", esa),
                new Rocket("F5", "USA", spacex),
                new Rocket("BFR", "USA", spacex)
        );

        for (Rocket r : rockets) {
            dao.createOrUpdate(r);
        }

        Collection<Rocket> loadedRockets = dao.loadAll(Rocket.class);
        assertEquals(rockets.size(), loadedRockets.size());
        for (Rocket r : rockets) {
            assertTrue(rockets.contains(r));
        }
    }

    @Test
    public void shouldCreateALaunchSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertFalse(launches.isEmpty());
        assertTrue(launches.contains(launch));
    }


    @Test
    public void shouldUpdateLaunchAttributesSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);

        Launch loadedLaunch = launches.iterator().next();
        assertNull(loadedLaunch.getFunction());

        launch.setFunction("experimental");
        dao.createOrUpdate(launch);
        launches = dao.loadAll(Launch.class);
        assertEquals(1, launches.size());
        loadedLaunch = launches.iterator().next();
        assertEquals("experimental", loadedLaunch.getFunction());
    }

    @Test
    public void shouldDeleteRocketWithoutDeleteLSP() {
        dao.createOrUpdate(rocket);
        assertNotNull(rocket.getId());
        assertNotNull(rocket.getManufacturer().getId());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    @Test
    public void shouldDeleteLaunchWithoutDeleteLSP() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2018, 7, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchServiceProvider(esa);
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertNotNull(launch.getId());
        assertNotNull(launch.getLaunchServiceProvider().getId());
        assertFalse(launches.isEmpty());
        assertTrue(launches.contains(launch));
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());

        dao.delete(launch);
        assertTrue(dao.loadAll(Launch.class).isEmpty());
        Collection<LaunchServiceProvider> prividers = dao.loadAll(LaunchServiceProvider.class);
        assertTrue(prividers.contains(esa));
    }

    @Test
    public void shouldDeleteLaunchWithoutDeleteRocket() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2018, 7, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchServiceProvider(esa);
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertNotNull(launch.getId());
        assertNotNull(launch.getLaunchVehicle().getId());
        assertFalse(launches.isEmpty());
        assertTrue(launches.contains(launch));
        assertFalse(dao.loadAll(Rocket.class).isEmpty());

        dao.delete(launch);
        assertTrue(dao.loadAll(Launch.class).isEmpty());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
    }



    @AfterEach
    public void tearDown() {
        session.purgeDatabase();
    }

    @AfterAll
    public void closeNeo4jSession() {
        session.clear();
        sessionFactory.close();
    }

    @Test
    public void testSaveUniqueLaunchTwiceOnlySavesOne() {
        LaunchServiceProvider provider = new LaunchServiceProvider("ESA", 1970, "Europe");
        Launch launch = new Launch(LocalDate.of(2017,01,01), rocket, provider,"LEO");
        dao.createOrUpdate(launch);
        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertTrue(!launches.isEmpty(),"Exists");
        assertTrue(launches.contains(launch),"contains launch");
        Launch loadedLaunch = launches.iterator().next();
        assertNull(loadedLaunch.getFunction(),"null function");
        assertFalse(dao.loadAll(Rocket.class).isEmpty(),"rocket exists");
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty(),"LSP exists");

        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty(),"rocket no longer exists");
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty(),"LSP still exists");


    }
}