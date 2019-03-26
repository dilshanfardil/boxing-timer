package lk.avalanche.timer.ui.main;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import lk.avalanche.timer.Constant;
import lk.avalanche.timer.R;
import lk.avalanche.timer.databinding.MainFragmentBinding;
import lk.avalanche.timer.db.Entity.Data;

import static android.app.Notification.*;

public class MainFragment extends Fragment {

    private static final String CHANNEL_ID = "100";
    public static int notificationId = 10;
    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private boolean bool = true;
    MediaPlayer bell;
    MediaPlayer countdown;
    public static NotificationManagerCompat notificationManager;
    public static NotificationCompat.Builder notificationBuilder;
    public static int numMessages = 1;
    private String time = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainFragment.TimerRecever.mainFragment = this;
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        notificationManager = NotificationManagerCompat.from(getContext());
        createNotification();
        Notification notification = notificationBuilder.build();
        binding.btnStart.setOnClickListener(v -> {
            startButton(notificationManager);
        });


        binding.btnReset.setOnClickListener(v -> {
            mViewModel.resetTimer();
            bool = true;
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
        });
        final String[] prevSec = {""};
        mViewModel.getLiveData().observe(this, s -> {
            observeData(model, notificationManager, notification, prevSec, s);
        });

        binding.btnSetting.setOnClickListener(v -> {
            mViewModel.resetTimer();
            notificationManager.cancel(notificationId);
            Navigation.findNavController(v).navigate(R.id.action_main_to_setting);
        });
    }

    void createNotification() {
        Intent pauseReceive = new Intent();
        pauseReceive.setAction(Constant.PAUSE_ACTION);
        int reqCode = 123456;
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(getContext(), reqCode, pauseReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent stopReceive = new Intent();
        stopReceive.setAction(Constant.STOP_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(getContext(), reqCode, stopReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        int icon = bool ? R.drawable.minus : R.drawable.plus;
        notificationBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.boxing_glove);
        notificationBuilder.addAction(icon, "Pause", pausePendingIntent);
        notificationBuilder.addAction(R.drawable.plus, "Next", stopPendingIntent);
        notificationBuilder.setContentTitle("Paused");
        notificationBuilder.setContentText(time);
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1)
        );

    }

    void observeData(MainViewModel.Model model, NotificationManagerCompat notificationManager, Notification notification, String[] prevSec, String s) {
        String[] split = s.split(":");
        String secStr = split[2];
        model.setMin(split[1]);
        model.setSec(secStr);
        model.setMilSec(split[3]);
        model.setRound_state(split[4] + " " + split[5]);
        model.setCurrent_number(split[5]);
        binding.setModel(model);
        time = model.getMin() + ":" + model.getSec();
        if (split[4].equals("Finished")) {
            bool = !bool;
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
            countdown.stop();
            bell.start();
            notification.flags = FLAG_AUTO_CANCEL;
            notificationManager.notify(notificationId, notification);
        }
    }

    public void startButton(NotificationManagerCompat notificationManager) {
        if (bool) {
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.pause_background));
            bool = !bool;
            mViewModel.startTimer(bell, countdown);
            bell.start();
        } else {
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
            bool = !bool;
            mViewModel.pauseTimer();
        }
        createNotification();
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void updateSound(Data data) {
        bell = MediaPlayer.create(getContext(), getResourceId(data.getBellSound(), "raw", getActivity().getPackageName()));
        countdown = MediaPlayer.create(getContext(), getResourceId(data.getCountDownSound(), "raw", getActivity().getPackageName()));
    }


    public int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static class TimerRecever extends BroadcastReceiver {
        public static MainFragment mainFragment;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.PAUSE_ACTION.equals(action)) {
                mainFragment.startButton(mainFragment.notificationManager);
            } else if (Constant.STOP_ACTION.equals(action)) {
                mainFragment.mViewModel.resetTimer();
                MainFragment.notificationManager.cancel(MainFragment.notificationId);
                mainFragment.getActivity().finish();
            }
        }
    }
}
