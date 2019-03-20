package lk.avalanche.timer.db.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * Developed by Lahiru Muthumal on 3/20/2019.
 * Last modified $file.lastModified.
 *
 * (C) Copyright 2019.year avalanche.lk (Pvt) Limited.
 * All Rights Reserved.
 *
 * These materials are unpublished, proprietary, confidential source code of
 * avalanche.lk (Pvt) Limited and constitute a TRADE SECRET
 * of avalanche.lk (Pvt) Limited.
 *
 * avalanche.lk (Pvt) Limited retains all title to and intellectual
 * property rights in these materials.
 *
 */
@Entity(tableName = "data")
public class Data {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long roundTimeInMilisec=0l;
    private Long intervalTimeInMilisec=0l;
    private Long numberOfRound=5l;
    private String countDownSound="";
    private String bellSound="";

    public Data(Long roundTimeInMilisec, Long intervalTimeInMilisec, Long numberOfRound, String countDownSound, String bellSound) {
        this.roundTimeInMilisec = roundTimeInMilisec;
        this.intervalTimeInMilisec = intervalTimeInMilisec;
        this.numberOfRound = numberOfRound;
        this.countDownSound = countDownSound;
        this.bellSound = bellSound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getRoundTimeInMilisec() {
        return roundTimeInMilisec;
    }

    public void setRoundTimeInMilisec(Long roundTimeInMilisec) {
        this.roundTimeInMilisec = roundTimeInMilisec;
    }

    public Long getIntervalTimeInMilisec() {
        return intervalTimeInMilisec;
    }

    public void setIntervalTimeInMilisec(Long intervalTimeInMilisec) {
        this.intervalTimeInMilisec = intervalTimeInMilisec;
    }

    public Long getNumberOfRound() {
        return numberOfRound;
    }

    public void setNumberOfRound(Long numberOfRound) {
        this.numberOfRound = numberOfRound;
    }

    public String getCountDownSound() {
        return countDownSound;
    }

    public void setCountDownSound(String countDownSound) {
        this.countDownSound = countDownSound;
    }

    public String getBellSound() {
        return bellSound;
    }

    public void setBellSound(String bellSound) {
        this.bellSound = bellSound;
    }
}
