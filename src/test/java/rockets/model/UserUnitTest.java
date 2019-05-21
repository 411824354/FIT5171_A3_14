package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTest {
    private User target;

    @BeforeEach
    public void setUp() {
        target = new User();
    }


    @DisplayName("should throw exception when pass a empty first name to setFirstName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFirstNameToEmpty(String firstname) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFirstName(firstname));
        assertEquals("firstname cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setFirstName function")
    @Test
    public void shouldThrowExceptionWhenSetFirstNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setFirstName(null));
        assertEquals("firstname cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when first name is invalid")
    @ParameterizedTest
    @ValueSource(strings = "ygao0018")
    public void shouldThrowExceptionWhenFirstNameIsInvalid(String firstname){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFirstName(firstname));
        assertEquals("Invalid first name!", exception.getMessage());
    }

    @DisplayName("should throw exception when first name is too long!")
    @ParameterizedTest
    @ValueSource(strings = "ygao0018jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj")
    public void shouldThrowExceptionWhenFirstNameIsTooLong(String firstname){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFirstName(firstname));
        assertEquals("First name should not be too long!", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty last name to setLastName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLastNameToEmpty(String lastname) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLastName(lastname));
        assertEquals("lastname cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to set last name function")
    @Test
    public void shouldThrowExceptionWhenSetLastNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLastName(null));
        assertEquals("lastname cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when last name is invalid")
    @ParameterizedTest
    @ValueSource(strings = "ygao0018")
    public void shouldThrowExceptionWhenLastNameIsInvalid(String lastname){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLastName(lastname));
        assertEquals("Invalid last name!", exception.getMessage());
    }

    @DisplayName("should throw exception when last name is too long!")
    @ParameterizedTest
    @ValueSource(strings = "ygao0018jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj")
    public void shouldThrowExceptionWhenLastNameIsTooLong(String lastname){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLastName(lastname));
        assertEquals("Last name should not be too long!", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetEmailToEmpty(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setEmail function")
    @Test
    public void shouldThrowExceptionWhenSetEmailToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setEmail(null));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should return true when two users have the same email")
    @Test
    public void shouldReturnTrueWhenUsersHaveSameEmail() {
        String email = "abc@example.com";
        target.setEmail(email);
        User anotherUser = new User();
        anotherUser.setEmail(email);
        assertTrue(target.equals(anotherUser));
    }


    @DisplayName("should return false when two users have different emails")
    @Test
    public void shouldReturnFalseWhenUsersHaveDifferentEmails() {
        target.setEmail("abc@example.com");
        User anotherUser = new User();
        anotherUser.setEmail("def@example.com");
        assertFalse(target.equals(anotherUser));
    }

    @DisplayName("should throw exception when email is invalid")
    @ParameterizedTest
    @ValueSource(strings = "ygao0018student.monash.edu")
    public void shouldThrowExceptionWhenEmallIsInvalid(String email){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("Invalid email", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty password to setPassword function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetPasswordToEmpty(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPassword(password));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exceptions when pass a null password to setPassword function")
    @Test
    public void shouldThrowExceptionWhenSetPasswordToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPassword(null));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    //    (?=.*[0-9]) a digit must occur at least once
    //    (?=.*[a-z]) a lower case letter must occur at least once
    //    (?=.*[A-Z]) an upper case letter must occur at least once
    //    (?=.*[@#$%^&+=]) a special character must occur at least once
    //    (?=\\S+$) no whitespace allowed in the entire string
    //    .{8,} at least 8 characters

    @DisplayName("should throw exception when the password has right pattern.")
    @ParameterizedTest
    @ValueSource(strings = {"aaZZa4@"})
    public void shouldThrowExceptionWhenSetPasswordToRightPattern(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPassword(password));
        assertEquals("Invalid password", exception.getMessage());
    }
}