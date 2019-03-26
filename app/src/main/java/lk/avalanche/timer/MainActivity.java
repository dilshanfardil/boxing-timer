package lk.avalanche.timer;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.Navigation;
import lk.avalanche.timer.ListContent.SoundContent;
import lk.avalanche.timer.db.DataRepository;
import lk.avalanche.timer.db.Entity.Data;
import lk.avalanche.timer.ui.main.MainFragment;
import lk.avalanche.timer.ui.main.SoundFragment;

public class MainActivity extends AppCompatActivity implements SoundFragment.OnListFragmentInteractionListener {
    private static String CHANNEL_ID = "100";
    DataRepository dataRepository;
    private int notificationId = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        dataRepository = new DataRepository(this);
        createNotificationChannel();
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.boxing_glove)
//                .setContentTitle("hi")
//                .setContentText("ho are you")
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setCategory(NotificationCompat.CATEGORY_SYSTEM);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        Notification notification = notificationBuilder.build();
//        notificationManager.notify(notificationId,notification);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Navigation.findNavController(this,R.id.main).navigate(R.id.action_main_to_setting);
                //startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(SoundContent.SoundItem item) {
        MediaPlayer player = MediaPlayer.create(this, getResourceId(item.content, "raw", getPackageName()));
        player.start();
        Data initialData = dataRepository.getInitialData();
        if (item.type) {
            initialData.setBellSound(item.content);
        } else {
            initialData.setCountDownSound(item.content);
        }
        dataRepository.updateData(initialData);
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
            CharSequence name = "Test channel";
            String description = "hi";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
