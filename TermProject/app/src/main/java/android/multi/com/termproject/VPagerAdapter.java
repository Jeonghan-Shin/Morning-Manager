package android.multi.com.termproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class VPagerAdapter extends FragmentStatePagerAdapter {

    int tabNum;

    public VPagerAdapter(FragmentManager fm, int tabNum) {
        super(fm);
        this.tabNum = tabNum;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                AlarmFragment tab1 = new AlarmFragment();
                return tab1;
            case 1:
                CheckFragment tab2 = new CheckFragment();
                return tab2;
            case 2:
                WeatherFragment tab3 = new WeatherFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNum;
    }
}
