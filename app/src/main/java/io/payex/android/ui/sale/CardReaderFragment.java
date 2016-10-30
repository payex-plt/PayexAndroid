package io.payex.android.ui.sale;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;

public class CardReaderFragment extends Fragment {

    @BindView(R.id.vp_card_reader) ViewPager mViewPager;
    @BindView(R.id.tl_card_reader) TabLayout mTabLayout;

    public static CardReaderFragment newInstance() {
        return new CardReaderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_reader, container, false);
        ButterKnife.bind(this, view);

        List<AbstractCardReaderFragment> mFragments = new ArrayList<>();
        mFragments.add(CardReaderQrFragment.newInstance());
        mFragments.add(CardReaderNfcFragment.newInstance());
        mFragments.add(CardReaderOcrFragment.newInstance());
        PagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);

        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<AbstractCardReaderFragment> mFragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, List<AbstractCardReaderFragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }
}
