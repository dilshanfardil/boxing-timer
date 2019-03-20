package lk.avalanche.timer.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import lk.avalanche.timer.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SoundSettingFragment extends Fragment {

    private SoundSettingViewModel mViewModel;

    public static SoundSettingFragment newInstance() {
        return new SoundSettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sound_setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SoundSettingViewModel.class);
        // TODO: Use the ViewModel
    }

}
