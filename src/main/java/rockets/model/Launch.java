package rockets.model;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@CompositeIndex(properties = {"launchDate","launchVehicle","launchServiceProvider","orbit"}, unique = true)
public class Launch extends Entity {

    public enum LaunchOutcome {
        FAILED, SUCCESSFUL
    }

    @Property(name = "launchDate")
    private LocalDate launchDate;

    @Relationship(type = "LAUNCHVEHICLE", direction = INCOMING)
    private Rocket launchVehicle;

    @Relationship(type = "LAUNCHSERVICEPROVIDER", direction = INCOMING)
    private LaunchServiceProvider launchServiceProvider;

    @Property(name = "payload")
    private Set<String> payload;

    @Property(name = "launchSite")
    private String launchSite;

    @Property(name = "orbit")
    private String orbit;

    @Property(name = "function")
    private String function;

    @Property(name = "price")
    private BigDecimal price;

    @Property(name = "launchOutcome")
    private LaunchOutcome launchOutcome;

    // add constructor
    public Launch() {
        super();
    }
    public Launch(LocalDate launchDate, Rocket rocket, LaunchServiceProvider provider, String orbit) {
        this.launchDate = launchDate;
        this.launchVehicle = rocket;
        this.launchServiceProvider = provider;
        this.orbit = orbit;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        notNull(launchDate, "Invalid launch date");
         this.launchDate = launchDate;

    }

    public Rocket getLaunchVehicle() {
        return launchVehicle;
    }

    public void setLaunchVehicle(Rocket launchVehicle) {
        notNull(launchVehicle, "Invalid launch vehicle");
        this.launchVehicle = launchVehicle;
    }

    public LaunchServiceProvider getLaunchServiceProvider() {
        return launchServiceProvider;
    }

    public void setLaunchServiceProvider(LaunchServiceProvider launchServiceProvider) {
        notNull(launchVehicle, "Invalid Launch Service Provider");
        this.launchServiceProvider = launchServiceProvider;
    }

    public Set<String> getPayload() {
        return payload;
    }

    public void setPayload(Set<String> payload) {
        notNull(payload,"Payload cannot be null.");
        if (payload.isEmpty()){
            throw new IllegalArgumentException("payload cannot be null or empty");
        }
        else{
            this.payload = payload;
        }

    }

    public String getLaunchSite() {

        return launchSite;
    }

    public void setLaunchSite(String launchSite) {
        notBlank(launchSite, "LaunchSite cannot be empty or null");
        notNull(launchSite,"LaunchSite cannot be empty or null");
        this.launchSite = launchSite;
    }

    public String getOrbit() {
        return orbit;
    }

    public void setOrbit(String orbit) {
        notBlank(orbit, "Orbit cannot be empty or null");
        notNull(orbit,"Orbit cannot be empty or null");
        this.orbit = orbit;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        notBlank(function, "Function cannot be empty or null");
        notNull(function,"Function cannot be empty or null");
        this.function = function;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        notNull(price,"Price cannot be 0 or null.");
        if (price == BigDecimal.valueOf(0)){
            throw new IllegalArgumentException("Price cannot be 0 or null.");
        }
        this.price = price;
    }

    public LaunchOutcome getLaunchOutcome() {
        return launchOutcome;
    }

    public void setLaunchOutcome(LaunchOutcome launchOutcome) {
        notNull(launchOutcome,"LaunchOutcome cannot be null.");
        this.launchOutcome = launchOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(launchDate, launch.launchDate) &&
                Objects.equals(launchVehicle, launch.launchVehicle) &&
                Objects.equals(launchServiceProvider, launch.launchServiceProvider) &&
                Objects.equals(orbit, launch.orbit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(launchDate, launchVehicle, launchServiceProvider, orbit);
    }
}
