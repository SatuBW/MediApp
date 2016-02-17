package com.example.satu.mediapp.Data;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Satu on 2016-01-04.
 */
@Getter
@Setter
public class Measure implements Serializable {
    int pulse;
    int systole;
    int diastole;
    Date time;

    public Measure(int diastole, int systole, int pulse) {

        this.diastole = diastole;
        this.pulse = pulse;
        this.systole = systole;
    }
}
