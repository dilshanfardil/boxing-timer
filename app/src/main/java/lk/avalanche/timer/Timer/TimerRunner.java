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

import android.app.Notification;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.lifecycle.MutableLiveData;
import lk.avalanche.timer.db.Entity.Data;
import lk.avalanche.timer.ui.main.MainFragment;

import static lk.avalanche.timer.ui.main.MainFragment.notificationBuilder;
import static lk.avalanche.timer.ui.main.MainFragment.notificationId;
import static lk.avalanche.timer.ui.main.MainFragment.numMessages;

public class TimerRunner {
    public static String time = "";
    private Data data;
    private MutableLiveData<String> live_time=new MutableLiveData<>();
    CountDownTimer timer;
    private static Long round_time=300000l;
    private Long interval_time=0l;
    private MediaPlayer bell_sound;
    private MediaPlayer countdown_sound;
    private final Integer countdown_limit = 6;
    private static Long pause_time=0l;
    private boolean isPause=false;
    private int current_round = 0;
    private boolean isFinishedRound = true;
    private int num_of_rounds;
    private int current_interval = 0;
    private Long warm_up_time;

    public static String congratz_msg = "Congratulations you have finished workout";

    public void setDataChange(Data data){
        round_time=data.getRoundTimeInMilisec();
        interval_time=data.getIntervalTimeInMilisec();
        warm_up_time = data.getWarmUpTimeInMilisec();
        num_of_rounds=data.getNumberOfRound().intValue();
    }

//    public Timer(Data data) {
//        setDataChange(data);
//    }

    public static Long getRound_time() {
        return round_time;
    }

    public static void setRound_time(Long round_time) {
        TimerRunner.round_time = round_time;
    }

    public static Long getPause_time() {
        return pause_time;
    }

    public static void setPause_time(Long pause_time) {
        TimerRunner.pause_time = pause_time;
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

    public void startTimer(MediaPlayer bell, MediaPlayer countdown) {
        if(!isPause){
            timer = getTimer(warm_up_time);
        }
        bell_sound = bell;
        countdown_sound = countdown;
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
                    live_time.setValue("00:00:00:00:" + congratz_msg + ": : ");
                    current_round = 0;
                    current_interval = 0;
                    notificationBuilder.setNumber(++numMessages);
                    notificationBuilder.setContentTitle(congratz_msg);
                    MainFragment.notificationManager.notify(notificationId, notificationBuilder.build());
                }

            }
        };
    }

    String[] prevSec = {""};

    String getStringTime(long millisUntilFinished) {
        NumberFormat formatter = new DecimalFormat("00");
        long sec = millisUntilFinished / 1000;
        long milSec=millisUntilFinished%1000;
        long min = sec / 60;
        long hour=min/60;
        min=min%60;
        sec=sec%60;
        Serializable serializable = isFinishedRound ? current_round == 0 ? "Get Ready: " : "Work It:" + current_round : "Rest Now:" + current_interval;

        if (milSec <= 10 & sec == countdown_limit & min < 1)
            countdown_sound.start();
        if (!prevSec[0].equals(String.valueOf(sec))) {
//            notificationBuilder.setOnlyAlertOnce(false);
            notificationBuilder.setNumber(++numMessages);
            time = formatter.format(min) + ":" + formatter.format(sec);
            notificationBuilder.setContentText(time);
            notificationBuilder.setContentTitle(serializable.toString());
            notificationBuilder.setOngoing(true);
            MainFragment.notificationManager.notify(notificationId, notificationBuilder.build());
        }
        prevSec[0] = String.valueOf(sec);
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
