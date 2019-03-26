package lk.avalanche.timer.ui.main;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import lk.avalanche.timer.R;
import lk.avalanche.timer.databinding.FragmentSettingBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private SettingViewModel mViewModel;
    FragmentSettingBinding binding;

    int sens = 5;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        MutableLiveData<SettingViewModel.DataModel> model = mViewModel.getModel();
        model.observe(this, new Observer<SettingViewModel.DataModel>() {
            @Override
            public void onChanged(SettingViewModel.DataModel dataModel) {
                binding.setModel(dataModel);
            }
        });

        binding.btnMinusRoundTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer sec = Integer.valueOf(binding.textRoundSeconds.getText().toString());
                SettingViewModel.DataModel tempModel = binding.getModel();
                sec -= sens;
                tempModel.setRoundTimeSec(sec);
                binding.setModel(tempModel);
            }
        });
        binding.btnPlusRoundTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sec = Integer.valueOf(binding.textRoundSeconds.getText().toString());
                SettingViewModel.DataModel tempModel = binding.getModel();
                sec += sens;
                tempModel.setRoundTimeSec(sec);
                binding.setModel(tempModel);
            }
        });
        binding.btnMinusInvlTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer sec = Integer.valueOf(binding.textRestSecond.getText().toString());
                SettingViewModel.DataModel tempModel = binding.getModel();
                sec -= sens;
                tempModel.setInvlTimeSec(sec);
                binding.setModel(tempModel);
            }
        });
        binding.btnPlusInvlTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer sec = Integer.valueOf(binding.textRestSecond.getText().toString());
                SettingViewModel.DataModel tempModel = binding.getModel();
                sec += sens;
                tempModel.setInvlTimeSec(sec);
                binding.setModel(tempModel);
            }
        });
        binding.btnPlusNumRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long min = Long.valueOf(binding.textRoundValue.getText().toString());
                SettingViewModel.DataModel tempModel = binding.getModel();
                min++;
                tempModel.setNumOfRound(min);
                binding.setModel(tempModel);
            }
        });
        binding.btnMinusNumRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long min = Long.valueOf(binding.textRoundValue.getText().toString());
                SettingViewModel.DataModel tempModel = binding.getModel();
                min--;
                tempModel.setNumOfRound(min);
                binding.setModel(tempModel);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.updateModel(binding.getModel());
                Navigation.findNavController(v).navigate(R.id.action_setting_to_main);
            }
        });

        binding.bellTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.createList(true);
                Navigation.findNavController(v).navigate(R.id.action_setting_to_soundSelect);
            }
        });

        binding.countdownTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.createList(false);
                Navigation.findNavController(v).navigate(R.id.action_setting_to_soundSelect);
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.setting);
        item.setVisible(false);
    }
}
