package lk.avalanche.timer.ui.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

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
import lk.avalanche.timer.Timer.TimerRunner;
import lk.avalanche.timer.databinding.MainFragmentBinding;
import lk.avalanche.timer.db.Entity.Data;

import static android.app.Notification.*;
import static android.content.Context.POWER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

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
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;


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
        createNotificationChannel();
        final MainViewModel.Model model = new MainViewModel.Model();
        binding.setModel(mViewModel.getInitilaData());


        displayAd();
        loadRewardedAd();

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
            displayAd();
            mViewModel.resetTimer();
            notificationManager.cancel(notificationId);
            showRewardedAd(v);
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    void loadRewardedAd() {
        rewardedAd = new RewardedAd(getContext(), "ca-app-pub-3940256099942544/1234567890");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                showRewardedAd(getView());
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    void showRewardedAd(View v) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                public void onRewardedAdClosed() {
                    // Ad closed.
                }

                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    Navigation.findNavController(v).navigate(R.id.action_main_to_setting);
                }

                public void onRewardedAdFailedToShow(int errorCode) {
                    Navigation.findNavController(v).navigate(R.id.action_main_to_setting);
                }
            };
            rewardedAd.show(getActivity(), adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            Navigation.findNavController(v).navigate(R.id.action_main_to_setting);
        }
    }

    void displayAd() {
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void createNotification() {
        Intent pauseReceive = new Intent();
        pauseReceive.setAction(Constant.PAUSE_ACTION);
        int reqCode = 123456;
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(getContext(), reqCode, pauseReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent stopReceive = new Intent();
        stopReceive.setAction(Constant.STOP_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(getContext(), reqCode, stopReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        int icon = bool ? R.drawable.play_dark : R.drawable.pause_dark;
        notificationBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.boxing_glove);
        notificationBuilder.addAction(icon, "Pause", pausePendingIntent);
        notificationBuilder.addAction(R.drawable.cancel_dark, "Close", stopPendingIntent);
        notificationBuilder.setContentTitle("Paused");
        notificationBuilder.setContentText(TimerRunner.time);
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
        model.setRound_state(split[4].toUpperCase() + " " + split[5]);
        model.setCurrent_number(split[5]);
        binding.setModel(model);
        if (split[4].equals(TimerRunner.congratz_msg)) {
            bool = !bool;
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
            countdown.stop();
            bell.start();
            notification.flags = FLAG_AUTO_CANCEL;
            notificationManager.notify(notificationId, notification);
        }
    }

    boolean isCountdownSoundPaused = false;
    public void startButton(NotificationManagerCompat notificationManager) {
        if (bool) {
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.pause_background));
            bool = !bool;
            mViewModel.startTimer(bell, countdown);
            if (isCountdownSoundPaused) {
                countdown.start();
            } else {
                bell.start();
            }

        } else {
            binding.btnStart.setBackground(getResources().getDrawable(R.drawable.play));
            bool = !bool;
            mViewModel.pauseTimer();
            if (countdown.isPlaying()) {
                countdown.pause();
                isCountdownSoundPaused = true;
            }

        }
        createNotification();
        notificationBuilder.setPriority(Notification.FLAG_ONGOING_EVENT);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void updateSound(Data data) {
        bell = MediaPlayer.create(getContext(), getResourceId(data.getBellSound(), "raw", getActivity().getPackageName()));
        countdown = MediaPlayer.create(getContext(), getResourceId(data.getCountDownSound(), "raw", getActivity().getPackageName()));
//        try {
//            bell.prepare();
//            countdown.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    public int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Timer Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(getContext(), NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
