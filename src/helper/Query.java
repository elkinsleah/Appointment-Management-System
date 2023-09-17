package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import static helper.JDBC.connection;

/**
 * Model for Query of the database
 * */
public class Query {

    public static User loginUser;
    private static final DateTimeFormatter zonedDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    private static final DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Checks if the input User ID and Password match the database.
     * Outputs the user, date, time, and if login was successful or not to login_activity.txt.
     * @return true if input matches user, false if not.
     * */
    public static boolean login(String loginUserName, String loginPassword) {

        loginUser = getUser(loginUserName, loginPassword);

        boolean loggedIn = (loginUser != null);

        try {
            File loginActivity = new File("login_activity.txt");
            if (loginActivity.createNewFile()) {
                FileWriter fileWriter = new FileWriter(loginActivity);
                fileWriter.write("USER\tDATE\tTIME\tLOGIN\n");
                fileWriter.close();
            }
            FileWriter fileWriter = new FileWriter(loginActivity, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            String userName;
            if (loginUserName.isEmpty()) {
                userName = "NO_USERID";
            } else {
                userName = loginUserName;
            }
            ZonedDateTime nowDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
            String date = nowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = nowDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss z"));

            printWriter.println(userName + "\t" +date+ "\t" +time+ "\t" + loggedIn);

            printWriter.flush();
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loggedIn;
    }

    /**
     * @return the logged-in user.
     * */
    public static User getLoginUser() {
        return new User(loginUser);
    }

    public static String getDivision(int divisionID) {
        String division = "Test Division";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Division FROM first_level_divisions WHERE Division_ID = " + divisionID);
            resultSet.next();
            division = resultSet.getString("Division");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return division;
    }

    /**
     * @param countryID  the Foreign Key in the Division Table.
     * @return all divisions from the database matching the Foreign Key.
     * */
    public static ObservableList<Division> getAllDivisions(int countryID) {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Division_ID, Division FROM first_level_divisions WHERE Country_ID = " + countryID);
            while (resultSet.next()) {
                Division nextDivision = new Division();
                nextDivision.setDivisionID(resultSet.getInt("Division_ID"));
                nextDivision.setDivision(resultSet.getString("Division"));
                allDivisions.add(nextDivision);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return  allDivisions;
    }

    /**
     * @param divisionID the Primary Key in the Division Table.
     * @return the country name of the division.
     * */
    public static String getCountry(int divisionID) {
        String country = "Test Country";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Country FROM countries WHERE Country_ID = (SELECT Country_ID FROM first_level_divisions WHERE Division_ID = " + divisionID + ")");
            resultSet.next();
            country = resultSet.getString("Country");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return country;
    }

    /**
     * @return all countries from the database.
     * */
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        try {
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT Country_ID, Country FROM countries");
             while(resultSet.next()) {
                 Country nextCountry = new Country();
                 nextCountry.setCountryID(resultSet.getInt("Country_ID"));
                 nextCountry.setCountry(resultSet.getString("Country"));
                 nextCountry.setAllDivisions(getAllDivisions(nextCountry.getCountryID()));
                 allCountries.add(nextCountry);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return allCountries;
    }

    /**
     * @param userName Primary Key in the User Table.
     * @param password Primary Ket in the User Table.
     * @return the user matching the Primary Key.
     * */
    public static User getUser(String userName, String password) {
        try {
            String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userName);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getInt("User_ID"));
                user.setName(resultSet.getString("User_Name"));
                user.setPassword(resultSet.getString("Password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return all users from the database.
     * */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT User_ID, User_Name, Password FROM users");
            while(resultSet.next()) {
                User nextUser = new User();
                nextUser.setId(resultSet.getInt("User_ID"));
                nextUser.setName(resultSet.getString("User_Name"));
                nextUser.setPassword(resultSet.getString("Password"));
                allUsers.add(nextUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * @param contactID Primary Key in the Contact Table.
     * @return the contact with the matching Primary Key.
     * */
    public static Contact getContact(int contactID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Contact_ID, Contact_Name, Email FROM contacts WHERE Contact_ID = " + contactID);
            resultSet.next();

            Contact contact = new Contact();
            contact.setId(resultSet.getInt("Contact_ID"));
            contact.setName(resultSet.getString("Contact_Name"));
            contact.setEmail(resultSet.getString("Email"));
            return contact;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return all contacts from the database.
     * */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Contact_ID, Contact_Name, Email FROM contacts");
            while(resultSet.next()) {
                Contact nextContact = new Contact();
                nextContact.setId(resultSet.getInt("Contact_ID"));
                nextContact.setName(resultSet.getString("Contact_Name"));
                nextContact.setEmail(resultSet.getString("Email"));
                allContacts.add(nextContact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    /**
     * @return all customers from the database.
     * */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers");
            while(resultSet.next()) {
                Customer nextCustomer = new Customer();
                nextCustomer.setId(resultSet.getInt("Customer_ID"));
                nextCustomer.setName(resultSet.getString("Customer_Name"));
                nextCustomer.setAddress(resultSet.getString("Address"));
                nextCustomer.setPostalCode(resultSet.getString("Postal_Code"));
                nextCustomer.setPhone(resultSet.getString("Phone"));
                nextCustomer.setDivisionID(resultSet.getInt("Division_ID"));
                allCustomers.add(nextCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    /**
     * @return all appointments from the database.
     * */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments");
            while(resultSet.next()) {
                Appointment nextAppointment = new Appointment();
                nextAppointment.setAppointmentID(resultSet.getInt("Appointment_ID"));
                nextAppointment.setTitle(resultSet.getString("Title"));
                nextAppointment.setDescription(resultSet.getString("Description"));
                nextAppointment.setLocation(resultSet.getString("Location"));
                nextAppointment.setType(resultSet.getString("Type"));

                String startDateTime = resultSet.getString("Start") + " UTC";
                String endDateTime = resultSet.getString("End") + " UTC";
                ZonedDateTime startZonedDateTime = ZonedDateTime.parse(startDateTime, zonedDateTimeFormat);
                ZonedDateTime endZonedDateTime = ZonedDateTime.parse(endDateTime, zonedDateTimeFormat);
                nextAppointment.setStartDateTime(startZonedDateTime);
                nextAppointment.setEndDateTime(endZonedDateTime);

                nextAppointment.setCustomerID(resultSet.getInt("Customer_ID"));
                nextAppointment.setUserID(resultSet.getInt("User_ID"));
                nextAppointment.setContactID(resultSet.getInt("Contact_ID"));
                allAppointments.add(nextAppointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * @param newCustomer the new customer added to the database.
     * @return true if the customer is successfully added, false if not.
     * */
    public static boolean addCustomer(Customer newCustomer) {
        try {
            Statement statement = connection.createStatement();
            String customerName = newCustomer.getName();
            String address = newCustomer.getAddress();
            String postalCode = newCustomer.getPostalCode();
            String phone = newCustomer.getPhone();
            String user = loginUser.getName();
            int divisionID = newCustomer.getDivisionID();
            statement.execute("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES('"+customerName+"', '"+address+"', '"+postalCode+"', '"+phone+"', NOW(), '"+user+"', NOW(), '"+user+"', "+divisionID+")");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            }
        return true;
    }

    /**
     * @param updateCustomer updates the selected customer in the database.
     * @return true if the customer is successfully updated, false if not.
     * */
    public static boolean updateCustomer(Customer updateCustomer) {
        try {
            Statement statement = connection.createStatement();
            String customerName = updateCustomer.getName();
            String address = updateCustomer.getAddress();
            String postalCode = updateCustomer.getPostalCode();
            String phone = updateCustomer.getPhone();
            String user = loginUser.getName();
            int divisionID = updateCustomer.getDivisionID();
            int customerID = updateCustomer.getId();
            statement.execute("UPDATE customers SET " + "Customer_Name = '" + customerName + "', " + "Address = '" + address + "', " + "Postal_Code = '" + postalCode + "', " +
                    "Phone = '" + phone + "', " + "Last_Update = NOW(), " + "Last_Updated_By = '" + user + "', " + "Division_ID = " + divisionID + " " + "WHERE Customer_ID = " + customerID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            }
        return true;
    }

    /**
     * @param customerID the selected customer to delete from the database.
     * @return true if the customer is deleted from the database, false if not.
     * */
    public static boolean deleteCustomer(int customerID) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM appointments WHERE Customer_ID = " + customerID);
            statement.execute("DELETE FROM customers WHERE Customer_ID = " + customerID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            }
        return true;
    }

    /**
     * @param newAppointment the new appointment to be added to the database.
     * @return true if the appointment is successfully added, false if not.
     * */
    public static boolean addAppointment(Appointment newAppointment) {
        try {
            Statement statement = connection.createStatement();
            String title = newAppointment.getTitle();
            String description = newAppointment.getDescription();
            String location = newAppointment.getLocation();
            String type = newAppointment.getType();

            ZonedDateTime startZoned = newAppointment.getStartDateTimeZoned();
            ZonedDateTime endZoned = newAppointment.getEndDateTimeZoned();
            startZoned = startZoned.withZoneSameInstant(ZoneId.of("UTC"));
            endZoned = endZoned.withZoneSameInstant(ZoneId.of("UTC"));
            String start = startZoned.toLocalDateTime().format(localDateTimeFormat);
            String end = endZoned.toLocalDateTime().format(localDateTimeFormat);

            String user = loginUser.getName();
            int customerID = newAppointment.getCustomerID();
            int userID = newAppointment.getUserID();
            int contactID = newAppointment.getContactID();
            statement.execute("INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES ('"+title+"', '"+description+"', '"+location+"', '"+type+"', '"+start+"', '"+end+"', NOW(), '"+user+"', NOW(), '"+user+"', "+customerID+", "+userID+", "+contactID+")");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            }
        return true;
    }

    /**
     * @param updateAppointment the selected appointment to be updated in the database.
     * @return true if the appointment is successfully updated, false if not.
     * */
    public static boolean updateAppointment(Appointment updateAppointment) {
        try {
            Statement statement = connection.createStatement();
            String title = updateAppointment.getTitle();
            String description = updateAppointment.getDescription();
            String location = updateAppointment.getLocation();
            String type = updateAppointment.getType();

            ZonedDateTime startZoned = updateAppointment.getStartDateTimeZoned();
            ZonedDateTime endZoned = updateAppointment.getEndDateTimeZoned();
            startZoned = startZoned.withZoneSameInstant(ZoneId.of("UTC"));
            endZoned = endZoned.withZoneSameInstant(ZoneId.of("UTC"));
            String start = startZoned.toLocalDateTime().format(localDateTimeFormat);
            String end = endZoned.toLocalDateTime().format(localDateTimeFormat);

            String user = loginUser.getName();
            int customerID = updateAppointment.getCustomerID();
            int userID = updateAppointment.getUserID();
            int contactID = updateAppointment.getContactID();
            int appointmentID = updateAppointment.getAppointmentID();
            statement.execute("UPDATE appointments SET " + "Title = '"+title+"', " + "Description = '"+description+"', " + "Location = '"+location+"', " + "Type = '"+type+"', " + "Start = '"+start+"', " + "End = '"+end+"', " +
                    "Last_Update = NOW(), " + "Last_Updated_By = '"+user+"', " + "Customer_ID = "+customerID+", " + "User_ID = "+userID+", " + "Contact_ID = "+contactID+" " + "WHERE Appointment_ID = "+appointmentID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            }
        return true;
    }

    /**
     * @param appointmentID the selected appointment to be deleted from the database.
     * @return true if the appointment is successfully deleted, false if not.
     * */
    public static boolean deleteAppointment(int appointmentID) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(" DELETE FROM appointments WHERE Appointment_ID = " + appointmentID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            }
        return true;
    }
}


