package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for Country
 * */
public class Country {

    private int countryID;
    private String country;
    private ObservableList<Division> allDivisions;

    /**
     * Parameterized constructor.
     * @param countryID the ID number of the country.
     * @param country the name of the country.
     * @param allDivisions all the divisions of the country.
     * */
    public Country(int countryID, String country, ObservableList<Division> allDivisions) {
        this.countryID = countryID;
        this.country = country;
        this.allDivisions = FXCollections.observableArrayList(allDivisions);
    }

    /**
     * Default constructor.
     * */
    public Country() {
    }

    /**
     * @return the country ID.
     * */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @param countryID the country ID to set.
     * */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * @return the country name.
     * */
    public String getCountry() {
       return country;
    }

    /**
     * @param country the country name to set.
     * */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return all divisions for the country.
     * */
    public ObservableList<Division> getAllDivisions() {
        return FXCollections.observableArrayList(allDivisions);
    }

    /**
     * @param allDivisions all divisions for the country to set.
     * */
    public void setAllDivisions(ObservableList<Division> allDivisions) {
        this.allDivisions = FXCollections.observableArrayList(allDivisions);
    }
}
