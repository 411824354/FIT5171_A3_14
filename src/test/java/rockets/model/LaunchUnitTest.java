package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchUnitTest {
    private Launch target;

    @BeforeEach
    public void setup(){
        target = new Launch();
    }


    //Launchdate cannot be empty, we ask user to select from drop down box, so don't need to check.
    @DisplayName("should throw exception when pass null to setLaunchDate function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchDateToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchDate(null));
        assertEquals("Invalid launch date", exception.getMessage());
    }

    //For Rocket, it cannot be empty because we ask user to select from drop down box.
    @DisplayName("should throw exception when pass null to setLaunchVehicle function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchVehicleToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchVehicle(null));
        assertEquals("Invalid launch vehicle", exception.getMessage());
    }

    //For provider, it cannot be empty because we ask user to select from drop down box.
    @DisplayName("should throw exception when pass null to setLaunchServiceProvider function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchServiceProviderToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchServiceProvider(null));
        assertEquals("Invalid Launch Service Provider", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty payload to setPayload function")
    @Test
    public void shouldThrowExceptionWhenSetPayloadToEmpty() {
        Set<String> payload = new HashSet<>();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPayload(payload));
        assertEquals("payload cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a null to setPayload function")
    @Test
    public void shouldThrowExceptionWhenSetPayloadToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPayload(null));
        assertEquals("Payload cannot be null.", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty launch site to setLaunchSite function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLaunchSiteToEmpty(String launchsite) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLaunchSite(launchsite));
        assertEquals("LaunchSite cannot be empty or null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchSite function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchSiteToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchSite(null));
        assertEquals("LaunchSite cannot be empty or null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty orbit to setOrbit function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetOrbitToEmpty(String orbit) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setOrbit(orbit));
        assertEquals("Orbit cannot be empty or null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setOrbit function")
    @Test
    public void shouldThrowExceptionWhenSetOrbitToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setOrbit(null));
        assertEquals("Orbit cannot be empty or null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty function to setFunction function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFunctionToEmpty(String function) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFunction(function));
        assertEquals("Function cannot be empty or null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setFunctionfunction")
    @Test
    public void shouldThrowExceptionWhenSetFunctionToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setFunction(null));
        assertEquals("Function cannot be empty or null", exception.getMessage());
    }

    //Assume the default value for price is 0.
    @DisplayName("should throw exception when pass a empty price to setPrice function")
    @ParameterizedTest
    @ValueSource(ints = {0})
    public void shouldThrowExceptionWhenSetPriceToEmpty(int price) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPrice(BigDecimal.valueOf(price)));
        assertEquals("Price cannot be 0 or null.", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a null to setPrice function")
    @Test
    public void shouldThrowExceptionWhenSetPriceToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPrice(null));
        assertEquals("Price cannot be 0 or null.", exception.getMessage());
    }

    //Assume Outcome cannot be emplty, because we ask user to select from a drop down box.
    @DisplayName("should throw exception when pass a null to setLaunchOutcome function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchOutcomeToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchOutcome(null));
        assertEquals("LaunchOutcome cannot be null.", exception.getMessage());
    }

    @DisplayName("should return true when two launch are the same")
    @Test
    public void shouldReturnTrueWhenLaunchesAreSame() {
        LaunchServiceProvider provider = new LaunchServiceProvider("Firefly Aerospace",2017, "United States");

        target.setLaunchDate(LocalDate.of(2019,12,31));
        target.setLaunchVehicle(new Rocket("Alpha","United States",provider));
        target.setLaunchServiceProvider(provider);
        target.setOrbit("LEO");

        Launch anotherLaunch = new Launch();
        anotherLaunch.setLaunchDate(LocalDate.of(2019,12,31));
        anotherLaunch.setLaunchVehicle(new Rocket("Alpha","United States",provider));
        anotherLaunch.setLaunchServiceProvider(provider);
        anotherLaunch.setOrbit("LEO");

        assertTrue(target.equals(anotherLaunch));
    }

    @DisplayName("should return false when two launch are the different")
    @Test
    public void shouldReturnFalseWhenLaunchesAreDifferent() {
        LaunchServiceProvider provider = new LaunchServiceProvider("Firefly Aerospace",2017, "United States");

        target.setLaunchDate(LocalDate.of(2019,12,31));
        target.setLaunchVehicle(new Rocket("Alpha","United States",provider));
        target.setLaunchServiceProvider(provider);
        target.setOrbit("LEO");

        Launch anotherLaunch = new Launch();
        anotherLaunch.setLaunchDate(LocalDate.of(2019,12,29));
        anotherLaunch.setLaunchVehicle(new Rocket("Alpha","United States",provider));
        anotherLaunch.setLaunchServiceProvider(provider);
        anotherLaunch.setOrbit("LEO");

        assertFalse(target.equals(anotherLaunch));
    }
}
