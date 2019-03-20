package lk.avalanche.timer.Timer;
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

import android.os.CountDownTimer;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.lifecycle.MutableLiveData;
import lk.avalanche.timer.db.Entity.Data;

public class Timer {
    private Data data;
    private MutableLiveData<String> live_time=new MutableLiveData<>();
    CountDownTimer timer;
    private static Long round_time=300000l;
    private Long interval_time=0l;
    private String countdown_sound="";
    private String bell_sound="";
    private static Long pause_time=0l;
    private boolean isPause=false;

    private int current_round=1;
    private boolean isInRound =true;
    private int num_of_rounds;

    public void setDataChange(Data data){
        round_time=data.getRoundTimeInMilisec();
        interval_time=data.getIntervalTimeInMilisec();
        countdown_sound=data.getCountDownSound();
        bell_sound=data.getBellSound();
        num_of_rounds=data.getNumberOfRound().intValue();
    }

    public Timer(Data data) {
        setDataChange(data);
    }

    public static Long getRound_time() {
        return round_time;
    }

    public static void setRound_time(Long round_time) {
        Timer.round_time = round_time;
    }

    public static Long getPause_time() {
        return pause_time;
    }

    public static void setPause_time(Long pause_time) {
        Timer.pause_time = pause_time;
    }

    public void manageTimer(){
        if(isInRound){
            timer=getTimer(interval_time);
            timer.start();
            isInRound =false;
        }else {
            current_round++;
            timer=getTimer(round_time);
            timer.start();
            isInRound =true;
        }
    }

    public void startTimer(){
        if(!isPause){
            timer = getTimer(getRound_time());
        }else {
            current_round=1;
        }
        timer.start();

        isPause=false;
    }

    public void pauseTimer(){
        isPause=true;
        timer.cancel();
        timer=getTimer(getPause_time());
    }

    public void resetTimer(){
        timer.cancel();
        isPause=false;
        getLive_time().setValue(isInRound ?getStringTime(round_time):getStringTime(interval_time));
    }
    private CountDownTimer getTimer(Long time) {
        return new CountDownTimer(time, 10) {
            public void onTick(long millisUntilFinished) {
                setPause_time(millisUntilFinished);
                String stringTime = getStringTime(millisUntilFinished);
                getLive_time().setValue(stringTime);
            }

            public void onFinish() {
                if(current_round!=num_of_rounds){
                    manageTimer();
                }else {
                    live_time.setValue(":Finished: : : :");
                }

            }
        };
    }

    String getStringTime(long millisUntilFinished) {
        NumberFormat formatter = new DecimalFormat("00");
        long sec = millisUntilFinished / 1000;
        long milSec=millisUntilFinished%1000;
        long min = sec / 60;
        long hour=min/60;
        min=min%60;
        sec=sec%60;
        Serializable serializable = isInRound ? current_round : "Interval";
        return formatter.format(hour)+":"+formatter.format(min)+":"+formatter.format(sec)+":"+ formatter.format(milSec/10)+":"+ serializable.toString();
    }

    public MutableLiveData<String> getLive_time() {
        return live_time;
    }

    public void setLive_time(MutableLiveData<String> live_time) {
        this.live_time = live_time;
    }
}
