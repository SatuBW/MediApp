package com.example.satu.mediapp.Data;

import java.io.Serializable;

import lombok.Getter;

/**
 * Created by Satu on 2016-01-04.
 */
@Getter
public class Medicine implements Serializable {
    int IDMed;
    String name;
    boolean reminder_morning = false;
    boolean reminder_midday = false;
    boolean reminder_evening = false;
    boolean prev_reminder_morning = false;
    boolean prev_reminder_midday = false;
    boolean prev_reminder_evening = false;
    double dose;

    public void setIDMed(int IDMed) {
        this.IDMed = IDMed;
    }

    public void setDose(double dose) {
        this.dose = dose;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReminder_evening(boolean reminder_evening) {
        this.prev_reminder_evening = this.reminder_evening;
        this.reminder_evening = reminder_evening;
    }

    public void setReminder_midday(boolean reminder_midday) {
        this.prev_reminder_midday = this.reminder_midday;
        this.reminder_midday = reminder_midday;
    }

    public void setReminder_morning(boolean reminder_morning) {
        this.prev_reminder_morning = this.reminder_morning;
        this.reminder_morning = reminder_morning;
    }
}
