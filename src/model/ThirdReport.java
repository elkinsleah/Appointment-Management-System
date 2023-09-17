package model;

/**
 * Model for displaying the Third Report, the number of appointment hours by customer. Extends the Individual model.
 * */
public class ThirdReport extends Individual {

    private double hours;

    /**
     * Parameterized constructor.
     * @param id the id of the third report.
     * @param name the name of the third report.
     * @param hours the total hours of the third report.
     * */
    public ThirdReport(int id, String name, double hours ) {
        super(id, name);
        this.hours = hours;
    }

    /**
     * @return the hours.
     * */
    public double getHours() {
        return hours;
    }

    /**
     * @param hours the hours to set.
     * */
    public void setHours(double hours) {
        this.hours = hours;
    }

    /**
     * @param addHours the added hours.
     * */
    public void addHours(double addHours) {
        hours += addHours;
    }

    /**
     * @return the name.
     * */
     @Override
    public String getName() {
        return super.getName();
    }
}
