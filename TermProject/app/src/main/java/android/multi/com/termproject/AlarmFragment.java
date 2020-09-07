package android.multi.com.termproject;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {

    Button setAlarm_btn;
    Button releaseAlarm_btn;
    TextView alarm_div_tv;
    TextView alarm_time_tv;

    View rView;
    Intent intent;
    PendingIntent pending;
    AlarmManager am;

    AlertDialog.Builder alert;

    boolean alarmOn = false;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rView = inflater.inflate(R.layout.fragment_alarm, container, false);

        intent = new Intent(getActivity(), AlarmActivity.class);
        pending = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pending = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        alert = new AlertDialog.Builder(getActivity());

        setAlarm_btn = (Button) rView.findViewById(R.id.setAlarm_btn);
        releaseAlarm_btn = (Button) rView.findViewById(R.id.releaseAlarm_btn);
        releaseAlarm_btn.setEnabled(false);
        alarm_div_tv = (TextView) rView.findViewById(R.id.alarm_div_tv);
        alarm_time_tv = (TextView) rView.findViewById(R.id.alarm_time_tv);

        setAlarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmOn = true;

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                Dialog dTime = new TimePickerDialog(getContext(), myTimeSetListener, hour, minute, false);
                dTime.show();
            }
        });

        releaseAlarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setMessage("알람을 해제 하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        release_Alarm();
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        return rView;
        }

        public void set_Alarm ( int hour, int minute){
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Calendar calendar = Calendar.getInstance();
            if ((calendar.get(Calendar.HOUR_OF_DAY) > hour) || ((calendar.get(Calendar.HOUR_OF_DAY) == hour) && (calendar.get(Calendar.MINUTE) >= minute))) {
                calendar.add(Calendar.DATE, 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

            Toast.makeText(getContext(), "알람이 활성화되었습니다", Toast.LENGTH_SHORT).show();

            setAlarm_btn.setEnabled(false);
            releaseAlarm_btn.setEnabled(true);
        }

        public void release_Alarm() {
            alarmOn = false;
            alarm_div_tv.setText("--");
            alarm_time_tv.setText("--  :  --");
            setAlarm_btn.setEnabled(true);
            releaseAlarm_btn.setEnabled(false);
            Toast.makeText(getContext(), "알람이 해제되었습니다", Toast.LENGTH_SHORT).show();
            am.cancel(pending);
        }

        private TimePickerDialog.OnTimeSetListener myTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String div = "오전";
                int myHour, myMinute;

                if (hour > 12) {
                    div = "오후";
                    myHour = hour - 12;
                } else {
                    myHour = hour;
                }
                myMinute = minute;

                alarm_div_tv.setText(div);
                if (myMinute < 10) {
                    alarm_time_tv.setText(myHour + " : 0" + myMinute);
                } else {
                    alarm_time_tv.setText(myHour + " : " + myMinute);
                }

                set_Alarm(hour, minute);
            }
        };

    }
