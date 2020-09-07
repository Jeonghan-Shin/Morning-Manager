package android.multi.com.termproject;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    Button setTime_btn;
    Button release_btn;
    Button addList_btn;
    ListView list_lv;
    EditText add_et;
    TextView div_tv;
    TextView time_set_tv;
    ArrayList<String> check_list;
    ArrayAdapter<String> check_adp;

    View rView;
    Intent intent;
    PendingIntent pending;
    AlarmManager am;

    AlertDialog.Builder alert;

    int listChecked = 0;
    boolean alarmOn = false;

    public CheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rView = inflater.inflate(R.layout.fragment_check, container, false);

        intent = new Intent(getActivity(), AlarmActivity.class);
        pending = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pending = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        alert = new AlertDialog.Builder(getActivity());

        setTime_btn = (Button) rView.findViewById(R.id.setTime_btn);
        release_btn = (Button) rView.findViewById(R.id.release_btn);
        release_btn.setEnabled(false);
        addList_btn = (Button) rView.findViewById(R.id.addList_btn);
        list_lv = (ListView) rView.findViewById(R.id.list_lv);
        add_et = (EditText) rView.findViewById(R.id.add_et);
        div_tv = (TextView) rView.findViewById(R.id.div_tv);
        time_set_tv = (TextView) rView.findViewById(R.id.time_set_tv);
        check_list = new ArrayList<>();
        check_adp = new CheckAdapter<String>(getActivity(), R.layout.list_row, check_list);

        setTime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_list.size() == 0) {
                    Toast.makeText(getContext(), "항목을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                alarmOn = true;

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                Dialog dTime = new TimePickerDialog(getContext(), myTimeSetListener, hour, minute, false);
                dTime.show();
            }
        });

        release_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setMessage("모든 설정을 초기화 하시겠습니까?");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        check_adp.clear();
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

        addList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = add_et.getText().toString();
                if (s.trim().length() == 0) {
                    Toast.makeText(getContext(), "항목을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                check_adp.add("  " + s);

                add_et.setText("");

                System.out.println(alarmOn);
                System.out.println("size: " + check_list.size());
                System.out.println("listChecked: " + listChecked);
            }
        });


        list_lv.setAdapter(check_adp);

        return rView;
    }

    public void set_Alarm(int hour, int minute) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Calendar calendar = Calendar.getInstance();
        if ((calendar.get(Calendar.HOUR_OF_DAY) > hour) || ((calendar.get(Calendar.HOUR_OF_DAY) == hour) && (calendar.get(Calendar.MINUTE) >= minute))) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

        check_adp.notifyDataSetInvalidated();

        Toast.makeText(getContext(), "알람이 활성화되었습니다", Toast.LENGTH_SHORT).show();

        release_btn.setEnabled(true);
    }

    public void release_Alarm() {
        alarmOn = false;
        listChecked = 0;
        div_tv.setText("--");
        time_set_tv.setText("--  :  --");
        release_btn.setEnabled(false);
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

            div_tv.setText(div);
            if (myMinute < 10) {
                time_set_tv.setText(myHour + " : 0" + myMinute);
            } else {
                time_set_tv.setText(myHour + " : " + myMinute);
            }

            set_Alarm(hour, minute);
        }
    };


    private class CheckAdapter<T> extends ArrayAdapter<String> {
        private final Activity activity;
        private final int list_row;
        private final ArrayList<String> check_list;

        public CheckAdapter(Activity activity, int list_row, ArrayList<String> check_list) {
            super(activity, list_row, check_list);
            this.activity = activity;
            this.list_row = list_row;
            this.check_list = check_list;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(list_row, null);
            }
            String s = check_list.get(position);
            TextView list_tv = (TextView) convertView.findViewById(R.id.list_tv);
            list_tv.setText(s);

            ImageButton list_delete_btn = (ImageButton) convertView.findViewById(R.id.list_delete_btn);

            final CheckBox check_cb = (CheckBox) convertView.findViewById(R.id.check_cb);

            list_delete_btn.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    check_cb.setChecked(false);
                    check_list.remove(position);
                    check_adp.notifyDataSetInvalidated();

                    if (listChecked > 0) {
                        listChecked--;
                    }

                    if (alarmOn == true && check_list.size() == 0) {
                        release_Alarm();
                    }

                }
            });


            if (alarmOn == false) {
                check_cb.setChecked(false);
                check_cb.setEnabled(false);
            } else {
                check_cb.setEnabled(true);
            }

            check_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (alarmOn == true) {

                        if (isChecked) {
                            listChecked++;
                        } else {
                            listChecked--;
                        }

                    }

                    if (listChecked == check_list.size()) {
                        alert.setMessage("모든 리스트가 체크되었습니다.\n리스트를 초기화 하시겠습니까?");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                check_adp.clear();
                            }
                        });

                        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                check_cb.setChecked(false);
                                check_adp.notifyDataSetInvalidated();
                            }
                        });

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();

                        release_Alarm();
                    }

                    System.out.println(alarmOn);
                    System.out.println("size: " + check_list.size());
                    System.out.println("listChecked: " + listChecked);
                }
            });

            return convertView;

        } // end getView

    }

}
