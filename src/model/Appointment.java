package model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import helper.Query;

/**
 * Model for Appointments
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * Parameterized constructor for appointment.
     * @param appointmentID the ID of the appointment.
     * @param title the title of the appointment.
     * @param description a description of the appointment.
     * @param location the location of the appointment.
     * @param type the type of the appointment.
     * @param startDateTime the start date and time of the appointment.
     * @param endDateTime the end date and time of the appointment.
     * @param customerID the ID of the customer that is associated with the appointment.
     * @param userID the ID of the user that created the appointment.
     * @param contactID the ID of the contact that is associated with the appointment.
     * */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerID, int userID, int contactID) {

        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Default constructor.
     * */
    public Appointment() {
    }

    /**
     * @return the appointment ID.
     * */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @param appointmentID the appointment ID to set.
     * */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return the title.
     * */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set.
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description.
     * */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set.
     * */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location.
     * */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set.
     * */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the type.
     * */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set.
     * */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the startDateTime.
     * */
    public LocalDateTime getStartDateTimeLocal() {
        return startDateTime;
    }

    /**
     * @return the startDateTime in LocalizedDateTime format in format style short.
     * */
    public String getStartDateTime() {
        return startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    /**
     * @return the endDateTime in LocalizedDateTime format in format style short.
     * */
    public String getEndDateTime() {
        return endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    /**
     * @param startDateTimeLocal the startDateTime set to LocalDateTime.
     * */
    public void setStartDateTime(LocalDateTime startDateTimeLocal) {
        this.startDateTime = startDateTimeLocal;
    }

    /**
     * @return the endDateTime.
     * */
    public LocalDateTime getEndDateTimeLocal() {
        return endDateTime;
    }

    /**
     * @param endDateTimeLocal the endDateTime set to LocalDateTime.
     * */
    public void setEndDateTime(LocalDateTime endDateTimeLocal) {
        this.endDateTime = endDateTimeLocal;
    }

    /**
     * @return the customer ID.
     * */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customer ID to set.
     * */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return the user ID.
     * */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the user ID to set.
     * */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the contact ID.
     * */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID the contact ID to set.
     * */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @return the contact name.
     * */
    public String getContactName() {
        return Query.getContact(contactID).getName();
    }

    /**
     * @return the startDateTime with the system's default time zone.
     * */
    public ZonedDateTime getStartDateTimeZoned() {
        return startDateTime.atZone(ZoneId.systemDefault());
    }

    /**
     * @param startDateTimeZoned the startDateTime set to ZonedDateTime.
     * */
    public void setStartDateTime(ZonedDateTime startDateTimeZoned) {
        ZoneId localZoneId = ZoneId.systemDefault();
        LocalDateTime startDateTime = startDateTimeZoned.withZoneSameInstant(localZoneId).toLocalDateTime();
        setStartDateTime(startDateTime);
    }

    /**
     * @return the endDateTime with the system's default time zone.
     * */
    public ZonedDateTime getEndDateTimeZoned() {
        return endDateTime.atZone(ZonedDateTime.now().getZone());
    }

    /**
     * @param endDateTimeZoned the endDateTime set to ZonedDateTime.
     * */
    public void setEndDateTime(ZonedDateTime endDateTimeZoned) {
        ZoneId localZoneId = ZoneId.systemDefault();
        LocalDateTime endDateTimeLocal = endDateTimeZoned.withZoneSameInstant(localZoneId).toLocalDateTime();
        setEndDateTime(endDateTimeLocal);
    }

    /**
     * Checks to see if appointment times overlap with other appointments.
     * Returns true if the appointment times overlap, and false if they do not overlap.
     * @param checkOverlap checks the appointment to see if it overlaps with another appointment.
     * */
    public boolean appointmentOverlap(Appointment checkOverlap) {
        if (this.appointmentID == checkOverlap.appointmentID) return false;
        if (this.userID != checkOverlap.getUserID()) return false;

        LocalDateTime newStartTime = checkOverlap.getStartDateTimeLocal();
        LocalDateTime newEndTime = checkOverlap.getEndDateTimeLocal();

        LocalDateTime thisStartTime = this.getStartDateTimeLocal();
        LocalDateTime thisEndTime = this.getEndDateTimeLocal();

        if (newStartTime.equals(thisStartTime) && newEndTime.equals(thisEndTime)) {
            return true;
        }
        if (newStartTime.compareTo(thisStartTime) * newStartTime.compareTo(thisEndTime) < 0) {
            return true;
        }
        if (newEndTime.compareTo(thisStartTime) * newEndTime.compareTo(thisEndTime) < 0) {
            return true;
        }
        if (thisStartTime.compareTo(newStartTime) * thisStartTime.compareTo(newEndTime) < 0) {
            return true;
        }
        if (thisEndTime.compareTo(newStartTime) * thisEndTime.compareTo(newEndTime) < 0) {
            return true;
        }
        return false;
    }
}

