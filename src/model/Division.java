package model;

/**
 * Model for country Division
 * */
public class Division {

    private int divisionID;
    private String division;

    /**
     * Parameterized constructor.
     * @param divisionID the ID number of the division.
     * @param division the name of the division.
     * */
    public Division(int divisionID, String division) {
        this.divisionID = divisionID;
        this.division = division;
    }

    /**
     * Default constructor.
     * */
    public Division() {
    }

    /**
     * @return the division ID.
     * */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID the division ID to set.
     * */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return the division name.
     * */
    public String getDivision() {
        return division;
    }

    /**
     * @param division the division name to set.
     * */
    public void setDivision(String division) {
        this.division = division;
    }
}
