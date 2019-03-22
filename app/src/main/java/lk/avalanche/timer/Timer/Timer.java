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

import android.media.MediaPlayer;
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
    private MediaPlayer bell_sound;
    private static Long pause_time=0l;
    private boolean isPause=false;
    private int current_round = 0;
    private boolean isFinishedRound = true;
    private int num_of_rounds;
    private int current_interval = 0;

    public void setDataChange(Data data){
        round_time=data.getRoundTimeInMilisec();
        interval_time=data.getIntervalTimeInMilisec();
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
        if (isFinishedRound) {
            current_interval++;
            timer=getTimer(interval_time);
            bell_sound.start();
            timer.start();
            isFinishedRound = false;
        }else {
            current_round++;
            timer=getTimer(round_time);
            bell_sound.start();
            timer.start();
            isFinishedRound = true;
        }
    }

    public void startTimer(MediaPlayer bell) {
        if(!isPause){
            timer = getTimer(getRound_time());
        }else {
            current_round=1;
        }
        bell_sound = bell;
        bell.start();
        timer.start();
        isPause=false;
    }

    public void pauseTimer(){
        isPause=true;
        timer.cancel();
        timer=getTimer(getPause_time());
    }

    public void resetTimer(){
        if (timer == null) return;
        timer.cancel();
        isPause=false;
        getLive_time().setValue(isFinishedRound ? getStringTime(round_time) : getStringTime(interval_time));
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
                    live_time.setValue("00:00:00:00:Finished: : ");
                    current_round = 0;
                    current_interval = 0;
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
        Serializable serializable = isFinishedRound ? current_round == 0 ? "Warm up: " : "Round:" + current_round : "Interval:" + current_interval;
        return formatter.format(hour) + ":" + formatter.format(min) + ":" + formatter.format(sec) + ":" +
                formatter.format(milSec / 10) + ":" + serializable.toString();
    }

    public MutableLiveData<String> getLive_time() {
        return live_time;
    }

    public void setLive_time(MutableLiveData<String> live_time) {
        this.live_time = live_time;
    }
}
