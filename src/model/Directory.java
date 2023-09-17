package model;

import helper.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for Directory of contacts, customers, users and countries.
 * */
public class Directory {

    private ObservableList<Customer> allCustomers;
    private ObservableList<User> allUsers;
    private ObservableList<Contact> allContacts;
    private ObservableList<Country> allCountries;

    /**
     * @return all customers in the directory.
     * */
    public ObservableList<Customer> getAllCustomers() {
        return FXCollections.observableArrayList(allCustomers);
    }

    /**
     * @return all users in the directory.
     * */
    public ObservableList<User> getAllUsers() {
        return FXCollections.observableArrayList(allUsers);
    }

    /**
     * @return all contacts in the directory.
     * */
    public ObservableList<Contact> getAllContacts() {
        return FXCollections.observableArrayList(allContacts);
    }

    /**
     * @return all countries in the directory.
     * */
    public ObservableList<Country> getAllCountries() {
        return FXCollections.observableArrayList(allCountries);
    }

    /**
     * Constructor for getting all contacts, customers, users and countries from the database.
     * */
    public Directory(){
        allCustomers = Query.getAllCustomers();
        allUsers = Query.getAllUsers();
        allContacts = Query.getAllContacts();
        allCountries = Query.getAllCountries();
    }

    /**
     * @param newCustomer the new customer added to the database.
     * @return true if the customer is added, and false if not.
     * */
    public boolean addCustomer(Customer newCustomer) {
        return Query.addCustomer(newCustomer);
    }

    /**
     * @param updateCustomer updates the selected customer in the database.
     * @return true if the customer is updated, and false if not.
     * */
    public boolean updateCustomer(Customer updateCustomer) {
        return Query.updateCustomer(updateCustomer);
    }

    /**
     * @param deleteCustomer deletes the selected customer from the database.
     * @return true if the customer is deleted, and false if not.
     * */
    public boolean deleteCustomer(Customer deleteCustomer) {
        if (Query.deleteCustomer(deleteCustomer.getId())) {
            allCustomers = Query.getAllCustomers();
            return true;
        }
        return false;
    }

    /**
     * @param customerID the customer ID number.
     * @return customer with matching ID number, returns null if no match.
     * */
    public Customer getCustomer(int customerID) {
        for (Customer customer : allCustomers) {
            if (customer.getId() == customerID) {
                return new Customer(customer);
            }
        }
        return null;
    }

    /**
     * @param userID the user ID number.
     * @return user with matching ID number, returns null if no match.
     * */
    public User getUser(int userID) {
        for (User user : allUsers) {
            if (user.getId() == userID) {
                return new User(user);
            }
        }
        return null;
    }

    /**
     * @param contactID the contact ID number.
     * @return contact with matching ID number, return null if no match.
     * */
    public Contact getContact(int contactID) {
        for (Contact contact : allContacts) {
            if (contact.getId() == contactID) {
                return new Contact(contact);
            }
        }
        return null;
    }
}
