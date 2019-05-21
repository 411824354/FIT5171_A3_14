package rockets.mining;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.neo4j.server.plugins.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static rockets.model.Launch.LaunchOutcome.FAILED;
import static rockets.model.Launch.LaunchOutcome.SUCCESSFUL;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();

        lsps = Arrays.asList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe")
        );


        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 1, 2, 2};

        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, lsps.get(lspIndex[i]).getCountry(), lsps.get(lspIndex[i])));

        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 1, 1, 1, 1, 2, 2, 3};


        //Changes here

        //index of lsp of launch
        int[] lspIndex1 = new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 2};

        //index of outcome of launch

        Launch.LaunchOutcome[] outcomes = new Launch.LaunchOutcome[]{FAILED,FAILED,SUCCESSFUL,FAILED,FAILED,FAILED,SUCCESSFUL,FAILED,SUCCESSFUL,SUCCESSFUL};

        //index of payload of launch
        Set<String> set1 = new HashSet<String>();
        set1.add("BepiColombo");
        Set<String> set2 = new HashSet<String>();
        set2.add("Intelsat 37e");
        set2.add("BSAT-4a");
        Set<String> set3 = new HashSet<String>();
        set3.add("IRIS SMEX");
        set3.add("CYGNSS");
        set3.add("IBEX");
        Set<String> set4 = new HashSet<String>();
        set4.add("SpX-DM1");
        Set<String> set5 = new HashSet<String>();
        set5.add("Nusantara Satu (PSN-6)");
        Set<String> set6 = new HashSet<String>();
        set6.add("S5");
        set6.add("CYGNSS2");
        Set<String> set7 = new HashSet<String>();
        set7.add("IRIS SMEX");
        set7.add("CYGNSS");
        set7.add("IBEX");
        set7.add("S5");
        Set<String> set8 = new HashSet<String>();
        set8.add("Intelsat 37e");
        set8.add("BSAT-4a");
        Set<String> set9 = new HashSet<String>();
        set9.add("BepiColombo");
        Set<String> set10 = new HashSet<String>();
        set10.add("SpX-DM1");
        set10.add("CYGNSS");
        set10.add("IBEX");
        ArrayList<Set<String>>payloadArray = new ArrayList<>();
        payloadArray.add(set1);
        payloadArray.add(set2);
        payloadArray.add(set3);
        payloadArray.add(set4);
        payloadArray.add(set5);
        payloadArray.add(set6);
        payloadArray.add(set7);
        payloadArray.add(set8);
        payloadArray.add(set9);
        payloadArray.add(set10);

        //index of lsp of launch
        int[] priceIndex = new int[]{150000, 200000, 250000, 300000, 350000, 400000, 450000, 500000, 550000, 600000};

        // year of each launch
        int[] years = new int[]{2017, 2018, 2017, 2018, 2017, 2018, 2017, 2018, 2017, 2018};

        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(years[i], months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchServiceProvider((lsps.get(lspIndex1[i])));
            l.setPayload(payloadArray.get(i));
            l.setOrbit("LEO");
            l.setLaunchOutcome(outcomes[i]);
            l.setPrice(BigDecimal.valueOf(priceIndex[i]));
            spy(l);
            return l;
        }).collect(Collectors.toList());

        for (int i=0; i<5; i++){
            Set<Launch> templaunches = new HashSet<>();
            for(Launch launch: launches){

                if (launch.getLaunchVehicle()==rockets.get(i)){
                    templaunches.add(launch);
                }
            }
            rockets.get(i).setLaunches(templaunches);
        }

        for (int i=0; i<3; i++){
            Set<Launch> templaunches = new HashSet<>();
            for(Launch launch: launches){

                if (launch.getLaunchServiceProvider()==lsps.get(i)){
                    templaunches.add(launch);
                }
            }
            lsps.get(i).setLaunches(templaunches);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2})
    public void shouldReturnTopMostLaunchedRockets(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        when(dao.loadAll(Rocket.class)).thenReturn(rockets);
        Rocket top1 = rockets.get(1);
        Rocket top2 = rockets.get(0);
        List<Rocket> sortedRocket = new ArrayList<>();
        sortedRocket.add(top1);
        sortedRocket.add(top2);
        List<Rocket> rockets1 = miner.mostLaunchedRockets(k);
        assertEquals(k, rockets1.size());
        assertEquals(sortedRocket.subList(0, k), rockets1);
    }

    @ParameterizedTest
    @ValueSource(ints = {2})
    public void shouldReturnTopmostReliableLaunchServiceProviders(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        when(dao.loadAll(LaunchServiceProvider.class)).thenReturn(lsps);
        List<LaunchServiceProvider> sortedLaunchServiceProvider = new ArrayList<>();
        LaunchServiceProvider top1 = lsps.get(2);
        LaunchServiceProvider top2 = lsps.get(0);
        sortedLaunchServiceProvider.add(top1);
        sortedLaunchServiceProvider.add(top2);
        List<LaunchServiceProvider> lsps1 = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k, lsps1.size());
        assertEquals(sortedLaunchServiceProvider.subList(0, k), lsps1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"LEO"})
    public void shouldReturndominantCountry(String orbit) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        when(dao.loadAll(Rocket.class)).thenReturn(rockets);
        String country = miner.dominantCountry(orbit);
        assertEquals("USA",country);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostExpensiveLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getPrice().compareTo(b.getPrice()));
        List<Launch> loadedLaunches = miner.mostExpensiveLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @CsvSource({"2,2018"})
    public void shouldReturnHighestRevenueLaunchServiceProviders(int k,int year) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        when(dao.loadAll(LaunchServiceProvider.class)).thenReturn(lsps);
        List<LaunchServiceProvider> sortedLaunchServiceProvider = new ArrayList<>();
        sortedLaunchServiceProvider.add(lsps.get(2));
        sortedLaunchServiceProvider.add(lsps.get(1));
        List<LaunchServiceProvider> lsps1 = miner.highestRevenueLaunchServiceProviders(k,year);
        assertEquals(k, lsps1.size());
        assertEquals(sortedLaunchServiceProvider.subList(0, k), lsps1);
    }


    // Provider 0:
    //Laugh 0  15000  2017    rocket0   F  P0
    //Launch 1  20000  2018    rocket0  F   P0
    //Launch2  25000   2017   rocket0   S  P0
    //
    //
    //Provider 1:
    //L3   30000    2018   rocket1 F   P1
    //L4   35000   2017   rocket1 F   P1
    //L5   40000   2018   rocket1 F   P1
    //
    //Provider 2
    //L6  45000    2017   rocket1 S   P2
    //L7. 50000    2018   rocket2  F   P2
    //L8  55000   2017   rocket2   S    P2
    //L9. 60000   2018   rocket3  S   P2



}