package rockets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Objects;
import java.util.ArrayList;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.INCOMING;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;

import java.sql.*;
import java.util.Set;

@NodeEntity
@CompositeIndex(properties = {"name","country","manufacturer"}, unique = true)
public class Rocket extends Entity {

    @Property(name = "name")
    private String name;

    @Property(name = "country")
    private String country;

    @Relationship(type = "MANUFACTURES", direction = INCOMING)
    private LaunchServiceProvider manufacturer;

    @Property(name = "massToLEO")
    private String massToLEO;

    @Property(name = "massToGTO")
    private String massToGTO;

    @Property(name = "massToOther")
    private String massToOther;

    // extension

    @Relationship(type = "LAUNCHVEHICLE", direction = OUTGOING)
    @JsonIgnore
    private Set<Launch> launches;

//    private static ArrayList<LaunchServiceProvider> manufacturerList = new ArrayList<>();

    public Rocket(){
        super();
    }
    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param country
     * @param manufacturer
     */


    public Rocket(String name, String country, LaunchServiceProvider manufacturer) {
        notNull(name);
        notNull(country);
        notNull(manufacturer);

        this.name = name;
        this.country = country;
        this.manufacturer = manufacturer;
        
//        if(manufacturerList.contains(manufacturer)){
//            this.manufacturer = manufacturer;
//        }
//        else{
//            throw new IllegalArgumentException("Invalid manufacture!");
//        }

    }

//    public static ArrayList<LaunchServiceProvider> getAllProviders() throws ClassNotFoundException, SQLException {
//        // DBConnection is the connection with database.
//        Connection conn=DBConnection.getDBConnection().getConnection();
//        Statement stm;
//        stm = conn.createStatement();
//        String sql = "Select * From LaunchServiceProvider";
//        ResultSet rst;
//        rst = stm.executeQuery(sql);
//        while (rst.next()) {
//            LaunchServiceProvider provider = new LaunchServiceProvider(rst.getString("name"), rst.getInt("year"), rst.getString("country"));
//            manufacturerList.add(provider);
//        }
//        return manufacturerList;
//    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public LaunchServiceProvider getManufacturer() {
        return manufacturer;
    }

    public String getMassToLEO() {
        return massToLEO;
    }

    public String getMassToGTO() {
        return massToGTO;
    }

    public String getMassToOther() {
        return massToOther;
    }

    public void setMassToLEO(String massToLEO) {
        notNull(massToLEO, "massToLEO cannot be null");
        this.massToLEO = massToLEO;
    }

    public void setMassToGTO(String massToGTO) {
        notNull(massToLEO, "massToGTO cannot be null");
        this.massToGTO = massToGTO;
    }

    public void setMassToOther(String massToOther) {
        notNull(massToOther,"massToOther cannot be null");
        this.massToOther = massToOther;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return Objects.equals(name, rocket.name) &&
                Objects.equals(country, rocket.country) &&
                Objects.equals(manufacturer, rocket.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, manufacturer);
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", massToLEO='" + massToLEO + '\'' +
                ", massToGTO='" + massToGTO + '\'' +
                ", massToOther='" + massToOther + '\'' +
                '}';
    }

    public Set<Launch> getLaunches(){
        return launches;
    }

    public void setLaunches(Set<Launch> newlaunches){
        notNull(newlaunches,"launches cannot be null");
        this.launches = newlaunches;
    }

}
