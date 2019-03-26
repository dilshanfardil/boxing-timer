package lk.avalanche.timer.ui.main;
/*
 * Developed by Lahiru Muthumal on 3/21/2019.
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

import android.app.Application;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import lk.avalanche.timer.ListContent.SoundContent;
import lk.avalanche.timer.converter.Converter;
import lk.avalanche.timer.converter.TimeLongToString;
import lk.avalanche.timer.converter.TimeStringToLong;
import lk.avalanche.timer.db.DataRepository;
import lk.avalanche.timer.db.Entity.Data;
import lk.avalanche.timer.db.Entity.Sound;

public class SettingViewModel extends AndroidViewModel {
    DataRepository dataRepository;
    Converter converter;
    Data initialData;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
    }

    public MutableLiveData<DataModel> getModel() {
        MutableLiveData<DataModel> objectMutableLiveData = new MutableLiveData<>();
        converter = new TimeLongToString();
        initialData = dataRepository.getInitialData();
        String[] roundTime = converter.convert(initialData.getRoundTimeInMilisec()).toString().split(":");
        String[] intevalTime = converter.convert(initialData.getIntervalTimeInMilisec()).toString().split(":");
        DataModel dataModel = new DataModel(intevalTime[0], intevalTime[1], roundTime[0], roundTime[1], initialData.getBellSound(), initialData.getCountDownSound(), initialData.getNumberOfRound());
        objectMutableLiveData.setValue(dataModel);
        return objectMutableLiveData;
    }

    public void updateModel(DataModel dataModel) {
        converter = new TimeStringToLong();
        Long roundTime = (Long) converter.convert(dataModel.getRoundTimeMin() + ":" + dataModel.getRoundTimeSec());
        Long invlTime = (Long) converter.convert(dataModel.getInvlTimeMin() + ":" + dataModel.getInvlTimeSec());
        Data data = new Data(roundTime, invlTime, dataModel.getNumOfRound(), dataModel.getCountdownSound(), dataModel.bellSound);
        dataRepository.updateData(data);
    }

    public void createList(boolean type) {
        List<Sound> soundByType = dataRepository.getSoundByType(type);
        SoundContent.makeListContent(soundByType);
    }

    public static class DataModel {
        private String invlTimeMin, invlTimeSec;
        private String roundTimeMin, roundTimeSec;
        private String bellSound, countdownSound;
        NumberFormat formatter = new DecimalFormat("00");
        private Long numOfRound;

        public DataModel() {
        }

        public DataModel(String invlTimeMin, String invlTimeSec, String roundTimeMin, String roundTimeSec, String bellSound, String countdownSound, Long numOfRound) {
            this.invlTimeMin = invlTimeMin;
            this.invlTimeSec = invlTimeSec;
            this.roundTimeMin = roundTimeMin;
            this.roundTimeSec = roundTimeSec;
            this.bellSound = bellSound;
            this.countdownSound = countdownSound;
            this.numOfRound = numOfRound;
        }

        public Long getNumOfRound() {
            return numOfRound;
        }

        public void setNumOfRound(Long numOfRound) {
            this.numOfRound = numOfRound <= 1 ? 1 : numOfRound;
        }


        public String getInvlTimeMin() {
            return invlTimeMin;
        }

        public void setInvlTimeMin(Integer integer) {
            integer = integer <= 0 ? 0 : integer;
            String invlTimeMin = formatter.format(integer);
            this.invlTimeMin = invlTimeMin;
        }

        public String getInvlTimeSec() {
            return invlTimeSec;
        }

        public void setInvlTimeSec(Integer invlTimeSec) {
            if (invlTimeSec / 60 >= 1) {
                setInvlTimeMin(Integer.valueOf(getInvlTimeMin()) + 1);
            } else if (invlTimeSec < 0) {
                setInvlTimeMin(Integer.valueOf(getInvlTimeMin()) - 1);
                invlTimeSec = 60 + invlTimeSec;
            }
            this.invlTimeSec = formatter.format(invlTimeSec % 60);
        }

        public String getRoundTimeMin() {
            return roundTimeMin;
        }

        public void setRoundTimeMin(Integer integer) {
            integer = integer <= 0 ? 0 : integer;
            String roundTimeMin = formatter.format(integer);
            this.roundTimeMin = roundTimeMin;
        }

        public String getRoundTimeSec() {
            return roundTimeSec;
        }

        public void setRoundTimeSec(int roundTimeSec) {
            if (roundTimeSec / 60 >= 1) {
                setRoundTimeMin(Integer.valueOf(getRoundTimeMin()) + 1);
            } else if (roundTimeSec < 0) {
                setRoundTimeMin(Integer.valueOf(getRoundTimeMin()) - 1);
                roundTimeSec = 60 + roundTimeSec;
            }
            this.roundTimeSec = formatter.format(roundTimeSec % 60);
        }

        public String getBellSound() {
            return bellSound;
        }

        public void setBellSound(String bellSound) {
            this.bellSound = bellSound;
        }

        public String getCountdownSound() {
            return countdownSound;
        }

        public void setCountdownSound(String countdownSound) {
            this.countdownSound = countdownSound;
        }
    }
}
