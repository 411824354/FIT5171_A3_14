package rockets.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class RocketUnitTest {

    private Rocket target;

    @BeforeEach
    public void setUp() {
//        target1 = new Rocket("", "", null);
        target = new Rocket("rocket", "country", new LaunchServiceProvider("SpaceX", 2002, "USA"));
    }

    @AfterEach
    public void tearDown() {
    }

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

    @DisplayName("should throw exception when given null manufacturer to constructor")
    @Test
    public void shouldThrowExceptionWhenNoManufacturerGiven() {
        String name = "BFR";
        String country = "USA";
        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
    }

    @DisplayName("should set rocket massToLEO value")
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }

    @DisplayName("should throw exception when pass null to setMassToGTO function")
    @Test
    public void shouldThrowExceptionWhenSetMassToGTOToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToGTO(null));
        assertEquals("massToGTO cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setMassToOther function")
    @Test
    public void shouldThrowExceptionWhenSetMassToOtherToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToOther(null));
        assertEquals("massToOther cannot be null", exception.getMessage());
    }

    @DisplayName("should return true when two rockets are the same")
    @Test
    public void shouldReturnTrueWhenRocketsAreTheSame() {
        Rocket testRocket = new Rocket("rocket", "country", new LaunchServiceProvider("SpaceX", 2002, "USA"));
        assertTrue(target.equals(testRocket));
    }

    @DisplayName("should return false when two rockets are different")
    @Test
    public void shouldReturnFalseWhenRocketsAreDifferent() {
        Rocket testRocket = new Rocket("rocket1", "country1", new LaunchServiceProvider("SpaceX1", 2001, "USA"));
        assertFalse(target.equals(testRocket));
    }

    @DisplayName("should throw exception when pass null to setLaunches function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchesToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunches(null));
        assertEquals("launches cannot be null", exception.getMessage());
    }



}