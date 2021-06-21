package ua.kyiv.mapapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String city;

    @OneToMany(cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<Coordinates> coordinatesList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Coordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<Coordinates> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) && Objects.equals(city, location.city) && Objects.equals(coordinatesList, location.coordinatesList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, coordinatesList);
    }
}
