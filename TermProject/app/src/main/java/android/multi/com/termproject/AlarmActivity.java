package android.multi.com.termproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.roboid.robot.Device;
import org.roboid.robot.Robot;
import org.smartrobot.android.RobotActivity;

import kr.robomation.physical.Albert;

public class AlarmActivity extends RobotActivity {
//public class AlarmActivity extends BroadcastReceiver {

    int data = 0;

    Button offAlarm_btn;
    private Device leftWheelDevice;
    private Device rightWheelDevice;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                BroadcastReceiver br = new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        while (true) {
//                            String action = intent.getAction();
//                            if ("example.intent.action.MOVEMOVE".equals(action)) {
//                                Log.v("whoho", action);
//                                data = intent.getIntExtra("data", 0);
//                            }
//                        }
//                    }
//                };
//            }
//        });
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_alarm);

        Intent intent = new Intent(getApplicationContext(), PlayAlarm.class);
        startService(intent);

        offAlarm_btn = (Button) findViewById(R.id.offAlarm_btn);
        offAlarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayAlarm.class);
                stopService(intent);
                finish();
            }
        });

    }

    @Override
    public void onInitialized(Robot robot) {
        super.onInitialized(robot);
        leftWheelDevice = robot.findDeviceById(Albert.EFFECTOR_LEFT_WHEEL);
        rightWheelDevice = robot.findDeviceById(Albert.EFFECTOR_RIGHT_WHEEL);
    }

//    @Override
//    public void onExecute() {
//        if (data == 1) {
//            leftWheelDevice.write(40);
//            rightWheelDevice.write(40);
//        } else {
//            leftWheelDevice.write(0);
//            rightWheelDevice.write(0);
//        }
//    }
}
