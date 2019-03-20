package lk.avalanche.timer.ui.main;

import android.app.Application;
import android.os.CountDownTimer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lk.avalanche.timer.Timer.Timer;
import lk.avalanche.timer.db.DataRepository;
import lk.avalanche.timer.db.Entity.Data;

public class MainViewModel extends AndroidViewModel {
    protected static Timer timer;
    private final DataRepository dataRepository;
    public final LiveData<Data> liveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        dataRepository=new DataRepository(application);
        Data value = dataRepository.getInitialData();
        timer=new Timer(value);
        liveData=dataRepository.getData();
    }

    public void changeSetting(Data data){
        timer.setDataChange(data);
    }

    public void startTimer() {
        timer.startTimer();
    }

    public void pauseTimer() {
        timer.pauseTimer();
    }

    public void resetTimer() {
        timer.resetTimer();
    }

    public LiveData<String> getLiveData() {
        return timer.getLive_time();
    }



    public static class Model{
        private String milSec="00";
        private String sec="00";
        private String min="00";
        private String current_round="1";

        public String getMilSec() {
            return milSec;
        }

        public void setMilSec(String milSec) {
            this.milSec = milSec;
        }

        public String getSec() {
            return sec;
        }

        public void setSec(String sec) {
            this.sec = sec;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getCurrent_round() {
            return current_round;
        }

        public void setCurrent_round(String current_round) {
            this.current_round = current_round;
        }
    }
}
