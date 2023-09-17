package model;

/**
 * Abstract model for customer, user, and contact
 * */
public abstract class Individual {

    private int id;
    private String name;

    /**
     * Parameterized constructor.
     * @param id the ID number of the customer, user, and contact.
     * @param name the name of the customer, user, and contact.
     * */
    public Individual(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the ID.
     * */
    public int getId() {
        return id;
    }

    /**
     * @param id the ID to set.
     * */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name.
     * */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set.
     * */
    public void setName(String name) {
        this.name = name;
    }
}
