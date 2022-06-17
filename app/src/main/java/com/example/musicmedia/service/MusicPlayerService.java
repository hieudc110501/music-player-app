package com.example.musicmedia.service;

import static com.example.musicmedia.application.MyApp.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicmedia.R;
import com.example.musicmedia.activity.MainActivity;
import com.example.musicmedia.broadcast_receiver.SongActionReceiver;
import com.example.musicmedia.model.SongOnline;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class MusicPlayerService extends Service {

    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLOSE = 3;
    public static final int ACTION_START = 4;
    public static final int ACTION_GO_TO_PLAYER = 5;
    public static final int ACTION_PREVIOUS = 6;
    public static final int ACTION_NEXT = 7;

    private int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private boolean isLoop;
    private boolean isFav = false;
    private SongOnline song;
    int pos;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SongOnline song = (SongOnline) bundle.get("song");
            pos = bundle.getInt("pos", 0);
            isLoop = bundle.getBoolean("isLoop", false);
            isFav = bundle.getBoolean("isFav", isFav);

            if (song != null) {
                this.song = song;
                startMusic(this.song);
                sendNotification(this.song);
            }
        }

        int actionMusic = intent.getIntExtra("action_music_service", 0);
        handleActionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    private void startMusic(SongOnline song) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(song.getPath()));


        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
            sendActionToFragment(ACTION_NEXT);
        });
        isPlaying = true;
        sendActionToActivity(ACTION_START);
    }

    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_GO_TO_PLAYER:
                goToPlayer();
                break;
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLOSE:
                stopSelf();
                sendActionToActivity(ACTION_CLOSE);
                break;
            case ACTION_PREVIOUS:
                previousSong();
                break;
            case ACTION_NEXT:
                nextSong();
                break;
        }
    }

    private void goToPlayer() {
//        Object sbservice = getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        try {
//            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
//            if (currentApiVersion <= 16) {
//                Method collapse = statusbarManager.getMethod("collapse");
//                collapse.invoke(sbservice);
//            } else {
//                Method collapse2 = statusbarManager.getMethod("collapsePanels");
//                collapse2.invoke(sbservice);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        sendActionToActivity(ACTION_GO_TO_PLAYER);
    }

    private void pauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(this.song);
            sendActionToActivity(ACTION_PAUSE);
        }
    }

    private void resumeMusic() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(this.song);
            sendActionToActivity(ACTION_RESUME);
        }
    }

    private void previousSong() {
        sendActionToFragment(ACTION_PREVIOUS);
    }

    private void nextSong() {
        sendActionToFragment(ACTION_NEXT);
    }

    private void sendNotification(SongOnline song) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Bitmap bitmap = getBitmapFromURL(song.getImage());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.music_notification);


        remoteViews.setImageViewResource(R.id.img_song_notify, R.drawable.ic_about);
//        Bitmap b = null;
//        try {
//            b = Picasso.with(getBaseContext()).load(song.getImage()).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        remoteViews.setImageViewBitmap(R.id.img_song_notify, b);
        remoteViews.setTextViewText(R.id.textViewName, song.getName());
        remoteViews.setTextViewText(R.id.textViewSinger, song.getArtist());

        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_pause);

        remoteViews.setOnClickPendingIntent(R.id.relativeLayoutMusicService, getPendingIntent(this, ACTION_GO_TO_PLAYER));
        if (isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_pause);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_play);
        }
        remoteViews.setOnClickPendingIntent(R.id.img_close, getPendingIntent(this, ACTION_CLOSE));
        remoteViews.setOnClickPendingIntent(R.id.img_previous, getPendingIntent(this, ACTION_PREVIOUS));
        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(this, ACTION_NEXT));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
//                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, SongActionReceiver.class);
        intent.putExtra("actionMusic", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void sendActionToActivity(int action) {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        bundle.putBoolean("isPlaying", isPlaying);
        bundle.putInt("action", action);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendActionToFragment(int action) {
        Intent intent;
        if (isFav) {
            intent = new Intent("send_data_to_fragment_favorite");
        } else {
            intent = new Intent("send_data_to_fragment_online");
        }

        Bundle bundle = new Bundle();
        bundle.putInt("action", action);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void senDataToFragmentPlayer(int action){
        Intent intent = new Intent("send_data_to_fragment_player");

        Bundle bundle = new Bundle();
        bundle.putBoolean("isPlaying", isPlaying);
        bundle.putInt("action", action);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
