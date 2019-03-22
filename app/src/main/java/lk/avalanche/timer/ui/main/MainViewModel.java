package lk.avalanche.timer.ui.main;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import lk.avalanche.timer.Timer.Timer;
import lk.avalanche.timer.converter.Converter;
import lk.avalanche.timer.converter.TimeLongToString;
import lk.avalanche.timer.db.DataRepository;
import lk.avalanche.timer.db.Entity.Data;

public class MainViewModel extends AndroidViewModel {
    protected static Timer timer;
    private final DataRepository dataRepository;
    public final LiveData<Data> liveData;
    Data value;
    public MainViewModel(@NonNull Application application) {
        super(application);
        dataRepository=new DataRepository(application);
        value = dataRepository.getInitialData();
        timer=new Timer(value);
        liveData=dataRepository.getData();
    }

    public Model getInitilaData() {
        Model model = new Model();
        Converter converter = new TimeLongToString();
        String[] split = converter.convert(value.getRoundTimeInMilisec()).toString().split(":");
        model.setMin(split[0]);
        model.setSec(split[1]);
        return model;
    }

    public void changeSetting(Data data){
        timer.setDataChange(data);
    }

    public void startTimer(MediaPlayer bell) {
        timer.startTimer(bell);
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
        private String current_number = "";
        private String round_state = "";

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

        public String getCurrent_number() {
            return current_number;
        }

        public void setCurrent_number(String current_number) {
            this.current_number = current_number;
        }

        public String getRound_state() {
            return round_state;
        }

        public void setRound_state(String round_state) {
            this.round_state = round_state;
        }
    }
}
