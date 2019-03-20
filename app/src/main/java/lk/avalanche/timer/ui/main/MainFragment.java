package lk.avalanche.timer.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.avalanche.timer.R;
import lk.avalanche.timer.databinding.MainFragmentBinding;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private boolean bool = true;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

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


        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bool){
                    binding.btnStart.setBackground(getResources().getDrawable(R.drawable.pause_background));
                    bool = !bool;
                    /**
                     * Implement the play function here
                     * */

                }else{
                    binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
                    bool = !bool;

                    /**
                     * Implement the pause function here
                     * */


                }


                mViewModel.startTimer();
            }
        });


        binding.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.pauseTimer();
            }
        });


        binding.btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.restartTimer();
            }
        });


        mViewModel.live_time.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                model.setTime(s);
                binding.setModel(model);
            }
        });
    }

}
