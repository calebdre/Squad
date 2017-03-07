package com.squad.view.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.squad.view.dashboard.chat.ChatFragment;
import com.squad.view.dashboard.history.HistoryFragment;
import com.squad.view.dashboard.profiles.ProfilesFragment;

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    private enum Fragments {
        CHAT(ChatFragment.newInstance()),
        PROFILES(ProfilesFragment.newInstance()),
        HISTORY(HistoryFragment.newInstance());

        private Fragment fragment;

        Fragments(Fragment fragment) {
            this.fragment = fragment;
        }

        public Fragment fragment(){
            return fragment;
        }

        public static Fragment get(int i) {
            return Fragments.values()[i].fragment();
        }
    }

    DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Fragments.get(position);
    }

    @Override
    public int getCount() {
        return Fragments.values().length;
    }
}
