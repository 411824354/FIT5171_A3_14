package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LaunchServiceProviderTest {
    private LaunchServiceProvider target1;
    private LaunchServiceProvider target2;

    @BeforeEach
    public void setUp() {
        target1 = new LaunchServiceProvider("", 0, "");
        target2 = new LaunchServiceProvider("rocket", 2018, "country");
    }

    @DisplayName("should return true when parameter is empty")
    @Test
    public void testReturnTrueWhenParamIsEmpty() {
        assertTrue(target1.getName().trim().equals(""));
        assertTrue(target1.getYearFounded() == 0);
        assertTrue(target1.getCountry().trim().equals(""));
    }

    @DisplayName("should return false when parameter is not empty")
    @Test
    public void testReturnFalseWhenParamIsNotEmpty() {

        assertFalse(target2.getName().trim().equals(""));
        assertFalse(target2.getCountry().trim().equals(""));
        assertFalse(target2.getYearFounded() == 0);

    }

    @DisplayName("should return true when two providers are the same")
    @Test
    public void shouldReturnTrueWhenProvidersAreTheSame() {
        LaunchServiceProvider testProvider = new LaunchServiceProvider("rocket", 2018,"country");
        assertTrue(target2.equals(testProvider));
    }

    @DisplayName("should return false when two rockets are different")
    @Test
    public void shouldReturnFalseWhenRocketsAreDifferent() {
        LaunchServiceProvider testProvider2 = new LaunchServiceProvider("rocket1", 2018,"country1");
        assertFalse(target2.equals(testProvider2));
    }

    @DisplayName("should throw exception when pass a empty headquarters to setHeadquarters function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetHeadquartersToEmpty(String headquarters) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target2.setHeadquarters(headquarters));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setHeadquarters function")
    @Test
    public void shouldThrowExceptionWhenSetHeadquartersToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target2.setHeadquarters(null));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setRockets function")
    @Test
    public void shouldThrowExceptionWhenSetRocketsToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target2.setRockets(null));
        assertEquals("rockets cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunches function")
    @Test
    public void shouldThrowExceptionWhenSetLaunchesToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target2.setLaunches(null));
        assertEquals("launches cannot be null", exception.getMessage());
    }


}