package lk.avalanche.timer.ui.main;

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

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private boolean bool = true;

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
        binding.setModel(model);

        mViewModel.liveData.observe(this, data -> mViewModel.changeSetting(data));

        binding.btnStart.setOnClickListener(v -> {

            if(bool){
                binding.btnStart.setBackground(getResources().getDrawable(R.drawable.pause_background));
                bool = !bool;
                mViewModel.startTimer();
            }else{
                binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
                bool = !bool;
                mViewModel.pauseTimer();
            }
        });


        binding.btnReset.setOnClickListener(v -> mViewModel.resetTimer());

        mViewModel.getLiveData().observe(this, s -> {
            String[] split = s.split(":");
            model.setMin(split[1]);
            model.setSec(split[2]);
            model.setMilSec(split[3]);
            model.setCurrent_round(split[4]);
            binding.setModel(model);
        });

        binding.btnSetting.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_main_to_setting));
    }

}
