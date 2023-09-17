package model;

/**
 * Model for User, extends the Individual model.
 * */
public class User extends Individual {

    private String password;

    /**
     * Parameterized constructor.
     * @param id the ID number of the user.
     * @param name the name of the user.
     * @param password the password of the user.
     * */
    public User(int id, String name, String password) {
        super(id, name);
        this.password = password;
    }

    /**
     * Default constructor.
     * @param user the user.
     * */
    public User(User user) {
        super(user.getId(),user.getName());
        this.password = user.password;
    }

    /**
     * Test constructor.
     * Creates a user with test strings and user ID of 0.
     * */
    public User() {
        super(0, "Test Name");
        password = "Test Password";
    }

    /**
     * @return the password.
     * */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set.
     * */
    public void setPassword(String password) {
        this.password = password;
    }
}
