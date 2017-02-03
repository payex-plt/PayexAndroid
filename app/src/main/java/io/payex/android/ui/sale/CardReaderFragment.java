package io.payex.android.ui.sale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;
import io.payex.android.util.HapticFeedbackUtil;

public class CardReaderFragment extends Fragment {

    @BindView(R.id.vp_card_reader) ViewPager mViewPager;
    @BindView(R.id.tl_card_reader) TabLayout mTabLayout;

    RecyclerView mNumpad;
    AppCompatTextView mPrimaryText;

    private static final int NUMPAD_COL = 3;
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    private OnFragmentInteractionListener mListener;

    public static CardReaderFragment newInstance() {
        return new CardReaderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_reader, container, false);
        ButterKnife.bind(this, view);

        mNumpad = ((CardReaderActivity) getActivity()).getmNumpad();
        mPrimaryText = ((CardReaderActivity) getActivity()).getmPrimaryText();

        List<AbstractCardReaderFragment> mFragments = new ArrayList<>();
        //mFragments.add(CardReaderQrFragment.newInstance());
        mFragments.add(CardReaderNfcFragment.newInstance());
        //mFragments.add(CardReaderOcrFragment.newInstance());
        PagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        //mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);

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



    ////

    private static final int MAX_VALUE = 9999;
    private static final int MIN_VALUE = 0;
    private int mCurrentValue = 0;

    private void setupNumPad(final Context context) {
        mNumpad.setLayoutManager(new SpanningGridLayoutManager(context, NUMPAD_COL));
        mNumpad.setHasFixedSize(true);

        Drawable divider = ContextCompat.getDrawable(context, R.drawable.bottom_divider);
        mNumpad.addItemDecoration(new GridDividerItemDecoration(divider, divider, NUMPAD_COL));

        final List<IFlexible> numpadItems = getNumPadItems();

        //Initialize the Adapter
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(numpadItems);
        adapter.initializeListeners(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
                SaleNumPadItem item = (SaleNumPadItem) numpadItems.get(position);
                int max = numpadItems.size();

                if (position == max - 1) { // entered
                    setValue(mCurrentValue);
                    mListener.onEnterPressed(item.getPrimaryText());
                } else if (position == max - 3) { // backspace
                    // backspace - not intended as per Kevin's request
                    /*if (mCurrentCents == MIN_CENTS) {
                        performHapticFeedback(context);
                    } else {
                        long afterBal =  mCurrentCents /  10;
                        System.out.println(" afterBal " + afterBal);
                        mCurrentCents = afterBal;
                    }*/
                    // clear instead
                    mCurrentValue = MIN_VALUE;
                    performHapticFeedback(context);
                    invalidateDisplayedValue();
                } else { // numpad
                    int pressedDigit = Character.getNumericValue(item.getPrimaryText().charAt(0));
                    if (isExceeded(pressedDigit)) {
//                        mCurrentCents = MAX_CENTS; // fix as per Kevin's request
                        performHapticFeedback(context);
                    } else {
                        mCurrentValue = mCurrentValue * 10 + pressedDigit;
                    }
                    invalidateDisplayedValue();
                }
                return false;
            }
        });

        mNumpad.setAdapter(adapter);
    }

    private boolean isExceeded( int newDigit ) {
        return mCurrentValue * 10 + newDigit > MAX_VALUE;
    }

    private void performHapticFeedback(Context context) {
        HapticFeedbackUtil.vibrate(context);
        HapticFeedbackUtil.shake(mPrimaryText);
    }

    private void invalidateDisplayedValue() {
        DecimalFormat f = new DecimalFormat("###0");
        String displayText = f.format(mCurrentValue);
        mPrimaryText.setText(displayText);
    }

    public interface OnFragmentInteractionListener {
        //        void onNumberPressed(String text);
//        void onBackspacePressed(String text);
        void onEnterPressed(String text);
    }

    private List<IFlexible> getNumPadItems() {
        List<IFlexible> list = new ArrayList<>();

        int max = 12;
        for (int i = 0 ; i < max ; i++) {
            String displayText = String.valueOf(i+1);
            if (i == max - 3) {
                displayText = "C";
            } else if (i == max - 2) {
                displayText = "0";
            } else if (i == max - 1) {
                displayText = "OK";
            }
            list.add(new SaleNumPadItem(displayText, displayText));
        }
        return list;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
