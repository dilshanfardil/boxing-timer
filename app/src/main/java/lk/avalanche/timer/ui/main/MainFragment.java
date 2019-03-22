package lk.avalanche.timer.ui.main;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import lk.avalanche.timer.R;
import lk.avalanche.timer.databinding.MainFragmentBinding;
import lk.avalanche.timer.db.Entity.Data;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private boolean bool = true;
    private Integer countdown_limit = 6;
    MediaPlayer bell;
    MediaPlayer countdown;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final MainViewModel.Model model = new MainViewModel.Model();
        binding.setModel(mViewModel.getInitilaData());
        mViewModel.liveData.observe(this, data -> {
            mViewModel.changeSetting(data);
            updateSound(data);
        });

        binding.btnStart.setOnClickListener(v -> {
            if(bool){
                binding.btnStart.setBackground(getResources().getDrawable(R.drawable.pause_background));
                bool = !bool;
                mViewModel.startTimer(bell);
            }else{
                binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
                bool = !bool;
                mViewModel.pauseTimer();
            }
        });


        binding.btnReset.setOnClickListener(v -> {
            mViewModel.resetTimer();
            bool = true;
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
        });
        final String[] prevSec = {""};
        mViewModel.getLiveData().observe(this, s -> {
            String[] split = s.split(":");
            String secStr = split[2];
            model.setMin(split[1]);
            model.setSec(secStr);
            model.setMilSec(split[3]);
            model.setRound_state(split[4] + " " + split[5]);
            model.setCurrent_number(split[5]);
            binding.setModel(model);
            playSound(bell, countdown, prevSec, split[3], secStr);
            if (split[4].equals("Finished")) {
                bool = !bool;
                binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
            }
        });

        binding.btnSetting.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_main_to_setting));
    }

    private void updateSound(Data data) {
        bell = MediaPlayer.create(getContext(), getResourceId(data.getBellSound(), "raw", getActivity().getPackageName()));
        countdown = MediaPlayer.create(getContext(), getResourceId(data.getCountDownSound(), "raw", getActivity().getPackageName()));
    }

    private void playSound(MediaPlayer bell, MediaPlayer countdown, String[] prevSec, String s, String secStr) {
        Integer intSec = Integer.valueOf(secStr);
        if (intSec == 0 & Integer.valueOf(s) <= 10) bell.start();
        if (secStr == null | secStr.equals("")) return;
        else if (!prevSec[0].equals(secStr) & intSec <= countdown_limit) {
            if (intSec == 0 & Integer.valueOf(s) <= 30) {
                bell.start();
            } else {
                countdown.start();
            }
        }
        prevSec[0] = secStr;
    }

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
