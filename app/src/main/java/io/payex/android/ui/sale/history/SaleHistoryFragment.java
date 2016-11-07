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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;

public class SaleHistoryFragment extends Fragment
//        implements CalendarFragment.OnCalendarInteractionListener
    implements SearchView.OnQueryTextListener
{

    @BindView(R.id.rv_sale_history)
    RecyclerView mRecyclerView;

    private OnListFragmentInteractionListener mListener;
    private FlexibleAdapter<IFlexible> mAdapter;

    // note: remember to use this for filter as per guideline
    private List<IFlexible> mSaleHistoryClone = new ArrayList<>();

    public static final int DIALOG_FRAGMENT = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("> " + data.getSerializableExtra("INPUT"));
                    List<Date> dates = (List<Date>)data.getSerializableExtra("INPUT");
                    for (Date d : dates) {
                        System.out.println(d.getTime());
                    }

                    // fixme filter by date range

//                    mAdapter.setSearchText(newText);
//                    //Fill and Filter mItems with your custom list and automatically animate the changes
//                    //Watch out! The original list must be a copy
//                    mAdapter.filterItems(mSaleHistoryClone, 200L);
                }
                break;
        }
    }



    public static SaleHistoryFragment newInstance() {
        return new SaleHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_history, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);

        final List<IFlexible> saleHistory = getSaleHistory();
        mSaleHistoryClone = saleHistory;

        //Initialize the Adapter
        mAdapter = new FlexibleAdapter<>(saleHistory);
        mAdapter.initializeListeners(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
                mListener.onListFragmentInteraction(saleHistory.get(position));
                return false;
            }
        });

        //Initialize the RecyclerView and attach the Adapter to it as usual
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public List<IFlexible> getSaleHistory() {
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
                    "Ending with " + (1234 + i),
                    c.getTimeInMillis()
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(IFlexible item);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_calendar) {
//            CalendarFragment f = new CalendarFragment();
//            f.show(getFragmentManager(), f.getTag());


            CalendarFragment f = CalendarFragment.newInstance();
            FragmentManager fm = getFragmentManager();
            f.setTargetFragment(this, DIALOG_FRAGMENT);
            f.show(fm, f.getTag());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sale_history, menu);
        initSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSearchView(final Menu menu) {
        //Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        if (searchItem != null) {
            SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_FULLSCREEN);
            mSearchView.setQueryHint(getString(R.string.sale_history_menu_search));
//            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setOnQueryTextListener(this);
        }
    }


    @Override
    public boolean onQueryTextChange(String newText) {
//        Log.e(TAG, "onQueryTextChange newText: " + newText);
        if (mAdapter.hasNewSearchText(newText)) {
            Log.e("SaleHistory", "onQueryTextChange newText: " + newText);
            mAdapter.setSearchText(newText);
            //Fill and Filter mItems with your custom list and automatically animate the changes
            //Watch out! The original list must be a copy
            mAdapter.filterItems(mSaleHistoryClone, 200L);
        }
        //Disable SwipeRefresh if search is active!!
//        mSwipeRefreshLayout.setEnabled(!mAdapter.hasSearchText());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.v("SaleHistory", "onQueryTextSubmit called!");
        return onQueryTextChange(query);
    }
}
