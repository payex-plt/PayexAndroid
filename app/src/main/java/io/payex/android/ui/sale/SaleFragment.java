package io.payex.android.ui.sale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
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
import io.payex.android.MyApp;
import io.payex.android.R;
import io.payex.android.ui.MainActivity;
import io.payex.android.util.HapticFeedbackUtil;

public class SaleFragment extends Fragment {

    @BindView(R.id.tv_primary) AppCompatTextView mPrimaryText;
    @BindView(R.id.rv_numpad) RecyclerView mNumpad;
    @BindView(R.id.rv_logo) RecyclerView mLogo;

    private static final int NUMPAD_COL = 3;

    private OnFragmentInteractionListener mListener;

    public static SaleFragment newInstance() {
        return new SaleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        setupLogo(context);
        setupNumPad(context);

        // todo experiment on display text
//        String pattern = getString(R.string.sale_default_amount_pattern);
//        String display = String.format(pattern, "RM", "0", "00");
//        String display = "<sup><small>RM</small></sup>0.00";
//        HtmlCompat.setSpannedText(mPrimaryText, display);

        mPrimaryText.setText(MainActivity.buildAmountText(MyApp.getCurrency(), mCurrentCents));

        return view;
    }

    private void setupLogo(Context context) {
        // todo size of columns need more research. max now is 4 on my tiny phone
        List<IFlexible> logos = getLogos();
        mLogo.setLayoutManager(new GridLayoutManager(context, logos.size()));
        mLogo.setHasFixedSize(true);
        mLogo.setAdapter(new FlexibleAdapter<>(logos));
    }

    private void setupNumPad(final Context context) {
        mNumpad.setLayoutManager(new SpanningGridLayoutManager(context, NUMPAD_COL));
        mNumpad.setHasFixedSize(true);

        Drawable divider = ContextCompat.getDrawable(context, R.drawable.divider);
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
                    MainActivity.setAmount(mCurrentCents);
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
                    mCurrentCents = MIN_CENTS;
                    performHapticFeedback(context);
                    invalidateDisplayedAmount();
                } else { // numpad
                    int pressedDigit = Character.getNumericValue(item.getPrimaryText().charAt(0));
                    if (isExceeded(pressedDigit)) {
//                        mCurrentCents = MAX_CENTS; // fix as per Kevin's request
                        performHapticFeedback(context);
                    } else {
                        mCurrentCents = mCurrentCents * 10 + pressedDigit;
                    }
                    invalidateDisplayedAmount();
                }
                return false;
            }
        });

        mNumpad.setAdapter(adapter);
    }

    private static final String CURRENCY_SYMBOL = MyApp.getCurrency();   // "RM";
    private static final long MAX_CENTS = 9999999;
    private static final long MIN_CENTS = 0;
    private long mCurrentCents = 0;


    private void invalidateDisplayedAmount() {
        DecimalFormat f = new DecimalFormat("#,###,##0.00");
        double d =  (double) mCurrentCents / (double) 100; // not work in cents - so convert to dollar
        String displayText = CURRENCY_SYMBOL + f.format(d);
        mPrimaryText.setText(displayText);
    }

    private boolean isExceeded( int newDigit ) {
        return mCurrentCents * 10 + newDigit > MAX_CENTS;
    }

    private void performHapticFeedback(Context context) {
        HapticFeedbackUtil.vibrate(context);
        HapticFeedbackUtil.shake(mPrimaryText);
    }

    private List<IFlexible> getLogos() {
        List<IFlexible> list = new ArrayList<>();

        int[] logos = { R.drawable.ic_ambank_40dp, R.drawable.ic_visa_40dp, R.drawable.ic_mastercard_40dp };

        int max = logos.length;    //3;
        for (int i = 0 ; i < max ; i++) {

            Drawable d = VectorDrawableCompat.create(getResources(), logos[i], null);
            d = DrawableCompat.wrap(d);

            list.add(new SaleLogoItem(i + 1 + "", d));
        }

        return list;
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

    public interface OnFragmentInteractionListener {
//        void onNumberPressed(String text);
//        void onBackspacePressed(String text);
        void onEnterPressed(String text);
    }
}
