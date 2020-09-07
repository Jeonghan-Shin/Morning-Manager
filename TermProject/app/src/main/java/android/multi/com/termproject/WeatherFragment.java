package android.multi.com.termproject;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private View rView;

    ListView listView;
    ArrayList<String> info_list;
    ArrayAdapter<String> info_adp;
    TextView area_tv;

    private String area = "";

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rView = inflater.inflate(R.layout.fragment_weather, container, false);

        listView = (ListView) rView.findViewById(R.id.info_lv);
        info_list = new ArrayList<>();
        info_adp = new WeatherAdapter(getActivity(), R.layout.weather_row, info_list);
        listView.setAdapter(info_adp);

        area_tv = (TextView) rView.findViewById(R.id.area_tv);

        WeatherAsyncTask wat = new WeatherAsyncTask();
        wat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return rView;
    }

    private class WeatherAsyncTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1135060000";

            try {
                Document doc = Jsoup.connect(url).get();

                area = doc.select("category").get(0).text();

                Elements datas = doc.select("data");

                for (Element el : datas) {
                    String str = "";
                    String temp = el.select("temp").get(0).text(); // 온도
                    String hour = el.select("hour").get(0).text(); // 시간
                    String wfkor = el.select("wfkor").get(0).text(); // 날씨
                    String day = "";
                    switch (el.select("day").get(0).text()) {
                        case "0":
                            day = "오늘:";
                            break;
                        case "1":
                            day = "내일:";
                            break;
                        case "2":
                            day = "모레:";
                            break;
                        default:
                            break;
                    }
                    String sky = el.select("sky").get(0).text();
                    String pty = el.select("pty").get(0).text();
                    int sky_i = new Integer(sky);
                    int pty_i = new Integer(pty);

                    str += day + " " + hour + "시까지: " + temp + "℃:" + wfkor + ":" + sky_i + ":" + pty_i;
                    publishProgress(str); // UI 갱신
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // 어댑터에 데이터추가시 데이터 원본 수정, UI 갱신
            area_tv.setText(area);
            info_adp.add(values[0]);
        }
    }

    private class WeatherAdapter extends ArrayAdapter<String> {

        private int weather_row;
        private ArrayList<String> info_list;

        public WeatherAdapter(@NonNull Context context, int weather_row, @NonNull ArrayList<String> info_list) {
            super(context, weather_row, info_list);
            this.weather_row = weather_row;
            this.info_list = info_list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // position: 보여줄 화면의 순서 번호
            // convertView: 화면에 보여 줄 뷰 객체. 이후 사용하던 객체를 넘겨줌
            // parent: 리스트뷰
            String[] split = info_list.get(position).split(":");// 문자열을 : 으로 나누기

            if (convertView == null) { // 처음 그려준다면
                // xml로 된 디자인화면을 자바 객체로 변환(LayoutInflator). 날씨정보를 저장.
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(weather_row, null);
            }

            // 각 정보를 저장
            TextView tv1 = convertView.findViewById(R.id.weather_row_tv1); // 온도
            tv1.setText(split[2]); // 온도정보가 저장되어 있는 2번 섹션

            TextView tv2 = convertView.findViewById(R.id.weather_row_tv2); // 날씨
            tv2.setText(split[3]); // 날씨정보가 저장되어 있는 3번 섹션

            TextView tv3 = convertView.findViewById(R.id.weather_row_tv3); // 기타
            tv3.setText(split[0] + " " + split[1]); // 기타정보가 저장되어 있는 0, 1번 섹션

            ImageView iv = (ImageView) convertView.findViewById(R.id.weather_icon);

            // 날씨별 아이콘
            if (split[4].equals("1")) // 맑음
                iv.setImageResource(R.drawable.db01_b);
            else if (split[4].equals("2")) // 구름 조금
                iv.setImageResource(R.drawable.db02_b);
            else if (split[4].equals("3")) // 구름 많음
                iv.setImageResource(R.drawable.db03_b);
            else if (split[4].equals("4")) // 흐림
                iv.setImageResource(R.drawable.db04_b);

            if (split[5].equals("1")) // 비
                iv.setImageResource(R.drawable.db05_b);
            else if (split[5].equals("2")) // 비, 눈
                iv.setImageResource(R.drawable.db06_b);
            else if (split[5].equals("3")) // 눈, 비
                iv.setImageResource(R.drawable.db07_b);
            else if (split[5].equals("4")) // 눈
                iv.setImageResource(R.drawable.db08_b);

            return convertView;
        }

    }

}
