package io.payex.android.ui.common;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.squareup.timessquare.CalendarPickerView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

public class CalendarFragment extends BottomSheetDialogFragment {

    @BindView(R.id.calendar_view) CalendarPickerView mCalendar;
    private static int mClickCount = 0;

    public static CalendarFragment newInstance() {
//        CalendarFragment fragment = new CalendarFragment();
        return new CalendarFragment();
    }

//    private OnFragmentInteractionListener mListener;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_calendar, null);
        dialog.setContentView(contentView);

        ButterKnife.bind(this, contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallback);
        }

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, -1);

        Date today = new Date();
        mCalendar.init( nextYear.getTime(), today)
                .inMode(RANGE)
//                .withSelectedDate(today)
        ;

        mCalendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
//                Toast.makeText(getContext(), "" + date, Toast.LENGTH_SHORT).show();
//                System.out.println("> " + mCalendar.getSelectedDate());

                mClickCount++;
                if (mClickCount == 2) {
                    mClickCount = 0;

                    Intent i = new Intent();
                    i.putExtra("INPUT", (Serializable)mCalendar.getSelectedDates());
//
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            i);

//                    if (mListener != null) {
//                        mListener.onDateRangeSelected(mCalendar.getSelectedDates());
//                    }
                    dismiss();
                }

            }

            @Override
            public void onDateUnselected(Date date) {
//                Toast.makeText(getContext(), " 1 " + date, Toast.LENGTH_SHORT).show();
//                System.out.println("1 > " + mCalendar.getSelectedDate());
//                System.out.println("onDateUnselected " + mCalendar.getSelectedDates());

            }
        });
    }

    /*@Override
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
        void onDateRangeSelected(List<Date> dates);
    }*/



    /*public static CalendarFragment newInstance() {
//        CalendarFragment fragment = new CalendarFragment();
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }*/

//    private OnCalendarInteractionListener mListener;
//
//    public interface OnCalendarInteractionListener {
//        void onSelectedDateRange(List<Date> dates);
//    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, -1);

        Date today = new Date();
        mCalendar.init( nextYear.getTime(), today)
                .inMode(RANGE)
//                .withSelectedDate(today)
        ;

        mCalendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
//                Toast.makeText(getContext(), "" + date, Toast.LENGTH_SHORT).show();
//                System.out.println("> " + mCalendar.getSelectedDate());

                mClickCount++;
                if (mClickCount == 2) {
                    mClickCount = 0;
//                    System.out.println("onDateSelected " + mCalendar.getSelectedDates());
//                    if (mListener != null) {
//                        mListener.onSelectedDateRange(mCalendar.getSelectedDates());
//                    }

                    Intent i = new Intent();
                    i.putExtra("HELLO", "WORLD");

                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            i);
                    dismiss();
                }

            }

            @Override
            public void onDateUnselected(Date date) {
//                Toast.makeText(getContext(), " 1 " + date, Toast.LENGTH_SHORT).show();
//                System.out.println("1 > " + mCalendar.getSelectedDate());
//                System.out.println("onDateUnselected " + mCalendar.getSelectedDates());

            }
        });


        return view;
    }*/

}
