package io.payex.android.ui.sale.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;

public class SaleHistoryFragment extends Fragment
//        implements CalendarFragment.OnCalendarInteractionListener
{

    @BindView(R.id.rv_sale_history)
    RecyclerView mRecyclerView;

    private OnListFragmentInteractionListener mListener;

    // todo experiment calendar
    @OnClick(R.id.fab)
    public void fabClick() {
        CalendarFragment f = CalendarFragment.newInstance();

        FragmentManager fm = getFragmentManager();
        f.setTargetFragment(this, DIALOG_FRAGMENT);
        f.show(fm, "fragment_edit_name");

    }

    public static final int DIALOG_FRAGMENT = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:

                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("> " + data.getStringExtra("HELLO"));
                }

                break;
        }
    }

    public static SaleHistoryFragment newInstance() {
        return new SaleHistoryFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.filter_calendar) {
            Toast.makeText(getContext(), "calendar", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_history, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);

        final List<IFlexible> myItems = getDatabaseList();

        //Initialize the Adapter
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(myItems);
        adapter.initializeListeners(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
                mListener.onListFragmentInteraction(myItems.get(position));
                return false;
            }
        });

        //Initialize the RecyclerView and attach the Adapter to it as usual
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    public List<IFlexible> getDatabaseList() {
        List<IFlexible> list = new ArrayList<>();

        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_mastercard_40dp, null);
        d = DrawableCompat.wrap(d);

        Calendar c = Calendar.getInstance();

        for (int i = 0; i < 25; i++) {
            c.add(Calendar.DATE, -i);

            list.add(new SaleHistoryItem(
                    i + 1 + "",
                    d,
                    "Paid RM80.99",
                    "Ending with 1234",
                    DateUtils.getRelativeTimeSpanString(
                            c.getTimeInMillis(),
                            System.currentTimeMillis(),
                            DateUtils.DAY_IN_MILLIS).toString()
            ));
        }
        return list;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onSelectedDateRange(List<Date> dates) {
//        System.out.println("onSelectedDateRange " + dates);
//    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(IFlexible item);
    }
}
