package rockets.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;
import rockets.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.resource.ClassPathResource;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.apache.logging.log4j.core.util.Closer.closeSilently;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    private static DAO dao;

    public static void setDao(DAO dao) {
        App.dao = dao;
    }

    public static void main(String[] args) throws IOException {
        Properties properties = loadProperties();

        int port = Integer.parseInt(properties.getProperty("spark.port"));
        port(port);

        String dbAddress = properties.getProperty("neo4j.dir");
        if (null == dao) {
            dao = new Neo4jDAO(dbAddress);
        }

        // "/"
        handleGetIndex();

        // "/hello"
        handleGetHello();

        // "/register"
        handleGetRegister();

        // "/register"
        handlePostRegister();

        // "/login"
        handleGetLogin();

        // "/login"
        handlePostLogin();

        // "/logout"
        handleGetLogout();

        // "/user/:id"
        handleGetUserById();

        // "/users"
        handleGetUsers();

        // "/rocket/:id
        handleGetRocket();

        // "/rocket/create
        handleGetCreateRocket();

        // "/rocket/create
        handlePostCreateRocket();

        // "/rockets"
        handleGetRockets();

        // "/launches"
//        handleGetLaunches();
        handleGetLaunches();

        handleGetCreateLaunch();

        handlePostCreateLaunch();

        handleGetLaunch();

        handleGetMining();
    }

    private static void handleGetLaunch() {
        get("/launch/:id", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                String id = req.params(":id");
                Launch launch = dao.load(Launch.class, Long.parseLong(id));
                if (null != launch) {
                    attributes.put("launch", launch);
                } else {
                    attributes.put("errorMsg", "No launch with the ID " + id + ".");
                }
                return new ModelAndView(attributes, "launch.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "launch.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetMining() {
        get("/mining", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "mining.html.ftl");

        }, new FreeMarkerEngine());
    }

    private static void handlePostCreateLaunch() {
        post("/launch/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String rocketName = req.queryParams("rocketName");
            String launchSite = req.queryParams("launchSite");
            String launchOrbit = req.queryParams("launchOrbit");
            LocalDate localDate = LocalDate.now();
            attributes.put("rocketName", rocketName);
            attributes.put("date", localDate);

            logger.info("Create <" + rocketName + ">, " + localDate);
            Launch launch;
            Rocket rocket;
            try {
                rocket = dao.getRocketByName(rocketName);
                launch = new Launch();
                launch.setLaunchVehicle(rocket);
                launch.setOrbit(launchOrbit);
                launch.setLaunchSite(launchSite);
                launch.setLaunchDate(localDate);
                dao.createOrUpdate(launch);
                res.status(301);
                req.session(true);
                //req.session().attribute("rocket", rocket);
                res.redirect("/launches");
                return new ModelAndView(attributes,"launches.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "create_launch.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetCreateLaunch() {
        get("/launch/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("rocketName", "");
            attributes.put("launchSite", "");
            attributes.put("launchOrbit","");

            return new ModelAndView(attributes, "create_launch.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handleGetLaunches() {
        get("/launches", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                attributes.put("launches", dao.loadAll(Launch.class));
                return new ModelAndView(attributes, "launches.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "launches.html.ftl");
            }
        }, new FreeMarkerEngine());

    }

    public static void stop() {
        Spark.stop();
    }

    private static void handleGetUsers() {
        get("/users", (req, res) -> {
            Map<String, Object> attributes = new HashMap<String, Object>();
            try {
                attributes.put("users", dao.loadAll(User.class));
                return new ModelAndView(attributes, "users.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "users.html.ftl");
            }
        }, new FreeMarkerEngine());

    }

    private static void handleGetIndex() {
        get("/", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            User user = getLoggedInUser(req);
            attributes.put("user", user);
            return new ModelAndView(attributes, "base_page.html.ftl");
            //return handleBaseHelloView(req, res, attributes);
        }, new FreeMarkerEngine());
    }

    private static void handleGetRegister() {
        get("/register", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", "");
            attributes.put("firstName", "");
            attributes.put("lastName", "");

            return new ModelAndView(attributes, "register.html.ftl");
        }, new FreeMarkerEngine());
    }


    /**
     * TODO: a serious bug in this method. Fix it (and test to verify)!
     */
    private static void handlePostRegister() {
        post("/register", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String email = req.queryParams("email");
            String password = req.queryParams("password");
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");

            attributes.put("email", email);
            attributes.put("firstName", firstName);
            attributes.put("lastName", lastName);

            logger.info("Registering <" + email + ">, " + password);

            User user;
            try {
                user = new User();
                user.setEmail(email);
                user.setPassword(password);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (checkUserExist(user))
                {
                    res.redirect("/hello");
                    return new ModelAndView(attributes, "base_page.html.ftl");
                }
                dao.createOrUpdate(user);
                res.status(301);
                req.session(true);
                req.session().attribute("user", user);
                res.redirect("/hello");
                return new ModelAndView(attributes, "base_page.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "register.html.ftl");
            }
        }, new FreeMarkerEngine());

    }

   private static Boolean checkUserExist(User user)
    {
        boolean exit = false;
        try {
            Collection<User> users = dao.loadAll(User.class);
            if (users != null) {
                for (User user1 : users) {
                    if (user.equals(user1)) {
                        exit = true;
                    }
                }
            }
        }catch (Exception e)
        {
            e.getMessage();
        }
        return exit;
    }

    private static void handleGetHello() {
        get("/hello", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            User user = getLoggedInUser(req);
            if (null != user) {
                attributes.put("user", user);
            }
            return new ModelAndView(attributes, "base_page.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handleGetLogin() {
        get("/login", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String user_name = req.params("user_name");
            if (null == user_name || user_name.trim().isEmpty()) {
                attributes.put("user_name", "");
            } else {
                attributes.put("user_name", user_name);
            }

            return new ModelAndView(attributes, "login.html.ftl");
        }, new FreeMarkerEngine());
    }

    private static void handlePostLogin() {
        post("/login", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String user_name = req.queryParams("user_name");
            String password = req.queryParams("password");

            logger.info("Logging in <" + user_name + ">, " + password);

            User user = null;
            try {
                user = dao.getUserByEmail(user_name);
            } catch (Exception e) {
                handleException(res, attributes, e, "login.html.ftl");
            }
            if (null != user && user.getPassword().equals(password)) {
                res.status(301);
                req.session(true);
                req.session().attribute("user", user);
                res.redirect("/hello");
                return new ModelAndView(attributes, "base_page.html.ftl");
            } else {
                attributes.put("errorMsg", "Invalid email/password combination.");
                attributes.put("user_name", user_name);
                return new ModelAndView(attributes, "login.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static void handleGetLogout() {
        get("/logout", (req, res) -> {
            User user = getLoggedInUser(req);
            spark.Session session = req.session();
            if (null != session && null != user) {
                session.invalidate();
            }
            res.redirect("/");
            return "";
        });
    }

    private static ModelAndView handleException(Response res, Map<String, Object> attributes, Exception e, String templateName) {
        res.status(500);
        if (e instanceof SQLException && null != e.getCause()) {
            attributes.put("errorMsg", e.getCause().getMessage());
        } else {
            attributes.put("errorMsg", e.getMessage());
        }
        e.printStackTrace();
        return new ModelAndView(attributes, templateName);
    }


    private static User getLoggedInUser(Request req) {
        spark.Session session = req.session();
        User user = null;
        if (null != session) {
            user = session.<User>attribute("user");
        }
        return user;
    }

    private static void handleGetUserById() {
        get("/user/:id", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            User user = getLoggedInUser(req);
            attributes.put("user", user);
            try {
                String id = req.params(":id");
                User person = dao.load(User.class, Long.parseLong(id));
                if (null != person) {
                    attributes.put("user", person);
                } else {
                    attributes.put("errorMsg", "No user with the ID " + id + ".");
                }
                return new ModelAndView(attributes, "user.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "user.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    // TODO: Need to TDD this
    private static void handleGetRocket() {
       get("/rocket/:id", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                String id = req.params(":id");
                Rocket rocket = dao.load(Rocket.class, Long.parseLong(id));
                if (null != rocket) {
                    attributes.put("rocket", rocket);
                } else {
                    attributes.put("errorMsg", "No rocket with the ID " + id + ".");
                }
                return new ModelAndView(attributes, "rocket.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "rocket.html.ftl");
            }
        }, new FreeMarkerEngine());

    }

    // TODO: Need to TDD this
    private static void handlePostCreateRocket() {
        post("/rocket1/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            String rocketName = req.queryParams("rocketName");
            String rocketCountry = req.queryParams("rocketCountry");
            String providerName = req.queryParams("providerName");
            String providerCountry = req.queryParams("providerCountry");
            String providerYear = req.queryParams("providerYear");

            attributes.put("rocketName", rocketName);
            attributes.put("rocketCountry", rocketCountry);

            logger.info("Create <" + rocketName + ">, " + rocketCountry);

            Rocket rocket;
            LaunchServiceProvider launchServiceProvider;
            try {
                launchServiceProvider = new LaunchServiceProvider(providerName,Integer.parseInt(providerYear),providerCountry);
                rocket = new Rocket(rocketName,rocketCountry,launchServiceProvider);
                //if (!checkProviderExist(launchServiceProvider))
                //{
                    dao.createOrUpdate(launchServiceProvider);
               // }
                dao.createOrUpdate(rocket);
                res.status(301);
                req.session(true);
                //req.session().attribute("rocket", rocket);
                res.redirect("/rockets");
                return new ModelAndView(attributes,"rockets.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "create_rocket1.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static boolean checkProviderExist(LaunchServiceProvider launchServiceProvider1) {
        boolean exit = false;
        try {
            Collection<LaunchServiceProvider> launchServiceProviders = dao.loadAll(LaunchServiceProvider.class);
            if (launchServiceProviders != null) {
                for (LaunchServiceProvider launchServiceProvider : launchServiceProviders) {
                    if (launchServiceProvider.equals(launchServiceProvider1)) {
                        exit = true;
                    }
                }
            }
        }catch (Exception e)
        {
            e.getMessage();
        }
        return exit;

    }

    // TODO: Need to TDD this
    private static void handleGetCreateRocket() {
        get("/rocket1/create", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("rocketName", "");
            attributes.put("rocketCountry", "");
            attributes.put("providerName","");
            attributes.put("providerCountry", "");
            attributes.put("providerYear", "");

            return new ModelAndView(attributes, "create_rocket1.html.ftl");
        }, new FreeMarkerEngine());
    }


    private static void handleGetRockets() {
        get("/rockets", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            try {
                attributes.put("rockets", dao.loadAll(Rocket.class));
                return new ModelAndView(attributes, "rockets.html.ftl");
            } catch (Exception e) {
                return handleException(res, attributes, e, "rockets.html.ftl");
            }
        }, new FreeMarkerEngine());
    }

    private static Properties loadProperties() throws IOException {
        ClassPathResource resource = new ClassPathResource("app.properties");
        Properties properties = new Properties();
        InputStream stream = null;
        try { stream = resource.getInputStream();
            properties.load(stream);
            return properties;
        } finally {
            closeSilently(stream);
        }
    }
}
