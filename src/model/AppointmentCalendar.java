package model;

import helper.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Model for an Appointment Calendar to keep track of appointments.
 * */
public class AppointmentCalendar {

    private ObservableList<Appointment> allAppointments;
    private static final ZoneId ZONE_ID = ZoneId.of("America/New_York");

    /**
     * Constructor that retrieves all appointments from the database.
     * */
    public AppointmentCalendar() {
        allAppointments = Query.getAllAppointments();
    }

    /**
     * @return all the appointments in the calendar.
     * */
    public ObservableList<Appointment> getAllAppointments() {
        return FXCollections.observableArrayList(allAppointments);
    }

    /**
     * @param newAppointment the new appointment added to the calendar.
     * */
    public boolean addAppointment(Appointment newAppointment) {
        return Query.addAppointment(newAppointment);
    }

    /**
     * @param appointmentToUpdate updates the selected appointment in the database.
     * */
    public boolean updateAppointment(Appointment appointmentToUpdate) {
        return Query.updateAppointment(appointmentToUpdate);
    }

    /**
     * @param appointmentToDelete deletes the selected appointment from the database.
     * @return true if the appointment is deleted, and false if it is not.
     * */
    public boolean deleteAppointment(Appointment appointmentToDelete) {
        if (Query.deleteAppointment(appointmentToDelete.getAppointmentID())) {
            allAppointments = Query.getAllAppointments();
            return true;
        }
        return false;
    }

    /**
     * Checks to see if the start time and end time of the appointment created are inside business hours.
     * @param startTime the selected start time.
     * @param endTime the selected end time.
     * @return true if the appointment is scheduled inside business hours, false if it is outside business hours.
     * */
    public static boolean insideBusinessHours(LocalDateTime startTime, LocalDateTime endTime) {

        ZonedDateTime[] apptTimes = {startTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZONE_ID),
                endTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZONE_ID)};

        for (ZonedDateTime apptTime : apptTimes) {
            if (apptTime.getDayOfWeek().getValue() == 6) {
                return false;
            }
            if (apptTime.getDayOfWeek().getValue() == 7) {
                return false;
            }
            if (apptTime.getHour() < 8) {
                return false;
            }
            if (apptTime.getHour() > 22) {
                return false;
            }
            if (apptTime.getHour() == 22 && apptTime.getMinute() !=0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param dateTime the dateTime.
     * @return the dateTime in Eastern Time zone.
     * */
    public static ZonedDateTime toZONE_ID(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZONE_ID);
    }
}
