package model;

/**
 * Model for Contacts, extends the Individual model.
 * */
public class Contact extends Individual {

    private String email;

    /**
     * Parameterized constructor for contact.
     * @param id the ID number of the contact.
     * @param name the contact name of the contact.
     * @param email the email of the contact.
     * */
    public Contact(int id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    /**
     * Default constructor.
     * @param contact the contact.
     * */
   public Contact(Contact contact){
        super(contact.getId(), contact.getName());
        this.email = contact.email;
    }

    /**
     * Test constructor.
     * Creates a contact with test strings and ID of 0.
     * */
    public Contact() {
        super(0,"Test Name");
        email = "Test Email";
    }

    /**
     * @param email the email to set.
     * */
    public void setEmail(String email) {
        this.email = email;
    }
}
