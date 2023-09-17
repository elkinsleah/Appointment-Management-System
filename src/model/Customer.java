package model;

import helper.Query;

/**
 * Model for Customers, extends the Individual model.
 * */
public class Customer extends Individual {

    private String address;
    private String postalCode;
    private String phone;
    private int divisionID;

    /**
     * Parameterized constructor for customer.
     * @param id the ID number of the customer.
     * @param name the name of the customer.
     * @param address the address of the customer.
     * @param postalCode the postal code of the customer.
     * @param phone the phone number of the customer.
     * @param divisionID the division ID of the customer.
     * */
    public Customer(int id, String name, String address, String postalCode, String phone, int divisionID) {
        super(id, name);
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /**
     * Default constructor.
     * @param customer the customer.
     * */
    public Customer(Customer customer){
        super(customer.getId(), customer.getName());
        this.address = customer.address;
        this.postalCode = customer.postalCode;
        this.phone = customer.phone;
        this.divisionID = customer.divisionID;
    }

    /**
     * Test constructor.
     * Creates a customer with test strings and variables set to 0.
     * */
    public Customer() {
        super(0, "Test Name");
        address = "Test Address";
        postalCode = "000000";
        phone = "000-000-0000";
        divisionID = 0;
    }

    /**
     * @return the address.
     * */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set.
     * */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postal code.
     * */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postal code to set.
     * */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phone number.
     * */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set.
     * */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * @return the division ID.
     * */
    public String getDivision() {
        return Query.getDivision(divisionID);
    }

    /**
     * @return the division ID.
     * */
    public String getCountry() {
        return Query.getCountry(divisionID);
    }
}
