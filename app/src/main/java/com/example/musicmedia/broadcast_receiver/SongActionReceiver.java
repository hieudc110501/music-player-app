package com.example.musicmedia.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.musicmedia.service.MusicPlayerService;

public class SongActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra("actionMusic", 0);

        Intent intentService = new Intent(context, MusicPlayerService.class);
        intentService.putExtra("action_music_service", actionMusic);

        context.startService(intentService);
    }
}
