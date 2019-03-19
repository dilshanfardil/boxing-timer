package lk.avalanche.timer.ui.main;

import android.os.CountDownTimer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public MutableLiveData<String> live_time=new MutableLiveData<>();
    CountDownTimer timer;
    static Long round_time=300000l,pause_time=0l;
    private boolean isPause=false;

    public void startTimer(){
        if(!isPause){
            timer = getTimer(round_time);
        }
        timer.start();
        isPause=false;
    }

    public void pauseTimer(){
        isPause=true;
        timer.cancel();
        timer=getTimer(pause_time);
    }

    public void restartTimer(){
        timer.cancel();
        isPause=false;
        startTimer();
    }
    private CountDownTimer getTimer(Long time) {
        return new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                pause_time=millisUntilFinished;
                String stringTime = getStringTime(millisUntilFinished);
                live_time.setValue(stringTime);
            }

            public void onFinish() {
                live_time.setValue("Done");
            }
        };
    }

     static String getStringTime(long millisUntilFinished) {
        NumberFormat formatter = new DecimalFormat("00");
        long sec = millisUntilFinished / 1000;
        long min = sec / 60;
        long hour=min/60;
        min=min%60;
        sec=sec%60;
        return formatter.format(hour)+":"+formatter.format(min)+":"+formatter.format(sec);
    }

    public static class Model{
        private String time=getStringTime(round_time);

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
