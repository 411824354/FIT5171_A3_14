package rockets.mining;
//GaoYu changes

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Ordering;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {

        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        logger.debug("Getting all rockets, total = " + rockets.size());

        ListMultimap<Integer, Rocket> maps = MultimapBuilder.treeKeys().arrayListValues().build();
        for (Rocket rocket: rockets) {
            Set<Launch> launches = rocket.getLaunches();
            if (null!= launches){
                maps.put(launches.size(),rocket);
            }
            else{
                maps.put(0,rocket);
            }
        }
        List<Integer> sortKeys = Lists.newArrayList(maps.keySet());
        Collections.sort(sortKeys, Ordering.natural().reverse());

        List<Rocket> result = Lists.newArrayList();
        for (Integer key : sortKeys) {
            List<Rocket> list = maps.get(key);
            if (result.size() >= k) {
                break;
            }
            else if(result.size() + list.size() >= k) {
                int addition = k - result.size();
                for (int i = 0; i < addition; i++){
                    result.add(list.get(i));
                }
            }else {
                result.addAll(list);
            }
        }
        return result;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
        Collection<LaunchServiceProvider> providers = dao.loadAll(LaunchServiceProvider.class);
        logger.debug("Getting all providers, total = " + providers.size());

        ListMultimap<Double, LaunchServiceProvider> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        for (LaunchServiceProvider p: providers) {
            Set<Launch> launches = p.getLaunches();
            double percent;
            Set<Launch> successLaunches = new HashSet<>();
            for (Launch launch: launches){
                if (launch.getLaunchOutcome() == Launch.LaunchOutcome.valueOf("SUCCESSFUL")){
                    successLaunches.add(launch);
                }
            }
            percent = successLaunches.size() * 1.0 /launches.size();

            if (null!= launches){
                multimap.put(percent,p);
            }
            else{
                multimap.put(0.00,p);
            }
        }

        List<Double> sortedKeys = Lists.newArrayList(multimap.keySet());
        Collections.sort(sortedKeys, Ordering.natural().reverse());

        List<LaunchServiceProvider> result = Lists.newArrayList();
        for (Double key : sortedKeys) {
            List<LaunchServiceProvider> list = multimap.get(key);
            if (result.size() >= k) {
                break;
            }
            else if(result.size() + list.size() >= k) {
                int newAddition = k - result.size();
                for (int i = 0; i < newAddition; i++){
                    result.add(list.get(i));
                }
            }else {
                result.addAll(list);
            }
        }
        return result;
    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */

    public String dominantCountry(String orbit) {
        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        logger.debug("Getting all rockets, total = " + rockets.size());


        String country = "";
        int tempPayload = 0;
        HashMap<String, Integer> payloadCounts = new HashMap<>();
        Set<String> countryList = new HashSet<String>();
        for (Rocket r : rockets)
            countryList.add(r.getCountry());
        for (String s : countryList) {
            for (Rocket r : rockets) {
                if (s.toString().equals(r.getCountry())) {
                    Set<Launch> launches = r.getLaunches();
                    for (Launch launch : launches) {
                        //check country, orbit & outcome
                        if (launch.getOrbit().equals(orbit) && launch.getLaunchOutcome().toString().equals("SUCCESSFUL")) {
                            // add number of payloads
                            tempPayload += launch.getPayload().size();
                        }
                    }
                }
            }
            payloadCounts.put(s.toString(), tempPayload);
            tempPayload = 0;
        }
        country = Collections.max(countryList);
        return  country;

    }


    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
        logger.info("find most expensive " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchPriceComparator = (a, b) -> -a.getPrice().compareTo(b.getPrice());
        return launches.stream().sorted(launchPriceComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) {

        Collection<LaunchServiceProvider> providers = dao.loadAll(LaunchServiceProvider.class);
        logger.debug("Getting all providers, total = " + providers.size());

        ListMultimap<BigDecimal, LaunchServiceProvider> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        for (LaunchServiceProvider p: providers) {
            Set<Launch> launches = p.getLaunches();
            BigDecimal salesRevenue = BigDecimal.valueOf(0);
            for (Launch launch: launches){
                if (launch.getLaunchDate().getYear() == year){
                    salesRevenue = salesRevenue.add(launch.getPrice());
                }
            }
            multimap.put(salesRevenue,p);
        }

        List<BigDecimal> sortedKeys = Lists.newArrayList(multimap.keySet());
        Collections.sort(sortedKeys, Ordering.natural().reverse());

        List<LaunchServiceProvider> result = Lists.newArrayList();
        for (BigDecimal key : sortedKeys) {
            List<LaunchServiceProvider> list = multimap.get(key);
            if (result.size() >= k) {
                break;
            }
            else if(result.size() + list.size() >= k) {
                int addition = k - result.size();
                for (int i = 0; i < addition; i++){
                    result.add(list.get(i));
                }
            }else {
                result.addAll(list);
            }
        }
        return result;
    }
}
