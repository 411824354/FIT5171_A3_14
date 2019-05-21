package rockets.model;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

@NodeEntity
@CompositeIndex(properties = {"email"}, unique = true)
public class User extends Entity {

    @Property(name = "firstName")
    private String firstName;

    @Property(name = "lastName")
    private String lastName;

    @Property(name = "email")
    private String email;

    @Property(name = "password")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    // ext.2. Add nonBlank constraint
    public void setFirstName(String firstName) {
        notBlank(firstName, "firstname cannot be null or empty");
        int len = firstName.length();
        if (len > 50){
            throw new IllegalArgumentException("First name should not be too long!");
        }
        for(int i = 0; i< len; i++){
            if (!Character.isLetter(firstName.charAt(i))){
                throw new IllegalArgumentException("Invalid first name!");
            }
        }

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // ext.3. Add nonBlank constraint
    public void setLastName(String lastName) {
        notBlank(lastName, "lastname cannot be null or empty");
        int len = lastName.length();
        if (len > 50){
            throw new IllegalArgumentException("Last name should not be too long!");
        }
        for(int i = 0; i< lastName.length(); i++){
            if (!Character.isLetter(lastName.charAt(i))){
                throw new IllegalArgumentException("Invalid last name!");
            }
        }
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        notBlank(email, "email cannot be null or empty");

        if (email.contains("@")){
            this.email = email;
        }else{
            throw new IllegalArgumentException("Invalid email");
        }

    }

    public String getPassword() {
        return password;
    }

    // ext.1. Add nonBlank constraint;
    //     2. Add constraint to test if the password is valid or not.
    //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    public void setPassword(String password) {
        notBlank(password, "password cannot be null or empty");
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if (password.matches(pattern)){
            this.password = password;
        }
        else{
            throw new IllegalArgumentException("Invalid password");
        }

    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {
        return this.password.equals(password.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
