package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model for displaying the First Report, the amount of appointments by month.
 * */
public class FirstReport {

    private int month;
    private int year;
    private String type;
    private int count;

    /**
     * Parameterized constructor.
     * @param month the month of the first report.
     * @param year the year of the first report.
     * @param type the type of the first report.
     * @param count the amount of appointments for the first report.
     * */
    public FirstReport(int month, int year, String type, int count) {
        this.month = month;
        this.year = year;
        this.type = type;
        this.count = count;
    }

    /**
     * Parameterized constructor.
     * Count is set to 1.
     * */
    public FirstReport(int month, int year, String type) {
        this.month = month;
        this.year = year;
        this.type = type;
        this.count = 1;
    }

    /**
     * @return the month.
     * */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set.
     * */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the year.
     * */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set.
     * */
    public void setYear(int year) {
        this.year = year;
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
     * @return the count.
     * */
    public int getCount() {
        return count;
    }

    /**
     * The count of the appointments.
     * */
    public void incrementCount() {
        ++count;
    }

    /**
     * @return the month and year.
     * */
    public String getMonthYear() {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.format(DateTimeFormatter.ofPattern("MMM yyyy"));
    }
}
