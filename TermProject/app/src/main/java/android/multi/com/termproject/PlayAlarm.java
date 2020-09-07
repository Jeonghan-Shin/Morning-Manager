package android.multi.com.termproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.smartrobot.android.RobotActivity;

public class PlayAlarm extends Service {

    private MediaPlayer mp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "서비스의 onCreate");
        mp = MediaPlayer.create(this, R.raw.alarm4);
        mp.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "서비스의 onStartCommand");
        Intent it = new Intent("example.intent.action.MOVEMOVE");
        it.putExtra("data", 1);
        sendBroadcast(it);
        mp.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        Intent it = new Intent("example.intent.action.MOVEMOVE");
        it.putExtra("data", 0);
        sendBroadcast(it);
        Log.d("test", "서비스의 onDestroy");
    }
}
