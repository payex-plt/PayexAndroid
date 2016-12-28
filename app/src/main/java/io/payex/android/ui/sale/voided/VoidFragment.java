package io.payex.android.ui.sale.voided;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.MyApp;
import io.payex.android.R;
import io.payex.android.Transaction;
import io.payex.android.TransactionJSON;
import io.payex.android.ui.common.CalendarFragment;
import io.payex.android.ui.common.ProgressItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class VoidFragment extends Fragment
        implements SearchView.OnQueryTextListener,
        FlexibleAdapter.OnUpdateListener,
        FlexibleAdapter.EndlessScrollListener,
        Callback<List<TransactionJSON>>
{

    @BindView(R.id.rv_sale_history)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view) View mEmptyView;

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



    public static VoidFragment newInstance() {
        return new VoidFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_void, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);

        //final List<IFlexible> saleHistory = getSaleHistory();
        //mSaleHistoryClone = saleHistory;

        getSaleHistory(5);

        return view;
    }

    /*public void createHeadersSectionsDatabase(int size, int headers) {
        databaseType = DatabaseType.HEADERS_SECTIONS;
        HeaderItem header = null;
        mItems.clear();
        int lastHeaderId = 0;
        for (int i = 0; i < size; i++) {
            header = i % Math.round(size / headers) == 0 ? newHeader(++lastHeaderId) : header;
            mItems.add(newSimpleItem(i + 1, header));
        }
    }*/

    public HeaderItem newHeader(String i) {
        HeaderItem header = new HeaderItem(i);
        header.setTitle(i);
        //header is hidden and un-selectable by default!
        return header;
    }

    public void getSaleHistory(int size) {
        Call<List<TransactionJSON>> call = MyApp.payexAPI.getTransactions();
        call.enqueue(this);
    }

    public List<IFlexible> getSaleHistory() {
        List<IFlexible> list = new ArrayList<>();

        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_mastercard_40dp, null);
        d = DrawableCompat.wrap(d);

        Calendar c = Calendar.getInstance();

        HeaderItem header = null;
        int lastHeaderId = 0;

        int size = 25;
        int headerGroupOf = 5;

        for (int i = 0; i < size; i++) {
            c.add(Calendar.DATE, -i);

//            header = i % Math.round(size / headerGroupOf) == 0 ? newHeader(++lastHeaderId) : header;
            header = newHeader(DateUtils.getRelativeTimeSpanString(
                    c.getTimeInMillis(),
                    System.currentTimeMillis(),
                    DateUtils.DAY_IN_MILLIS).toString());

            list.add(new VoidItem(
                    i + 1 + "",
                    d,
                    "03:12",
                    "RM 30.00",
                    c.getTimeInMillis(),
                    header,
                    getString(R.string.sale_history_card_ending),
                    null
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

    @Override
    public void onResponse(Call<List<TransactionJSON>> call, Response<List<TransactionJSON>> response) {
        final List<IFlexible> list = new ArrayList<>();

        ArrayList<TransactionJSON> transactions = new ArrayList<TransactionJSON>();
        transactions.addAll(response.body());

        Log.d(TAG, "size -> " + transactions.size());

        HeaderItem header = null;
        Calendar c = Calendar.getInstance();
        Drawable d;
        Date cd;
        String currentHeader, lastHeader = "";

        DecimalFormat df = new DecimalFormat("###,###.00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

//        int lastHeaderId = 0;
//        int size = 25;
//        int headerGroupOf = 5;
//        int i = 0;

        for (TransactionJSON txn : transactions) {
            Log.d(TAG, "txn -> " + txn.toString());
            Log.d(TAG, "card brand -> " + txn.CardBrand);

            d = VectorDrawableCompat.create(getResources(),
                    txn.CardBrand.toLowerCase().equals("visa") ? R.drawable.ic_visa_40dp : R.drawable.ic_mastercard_40dp,
                    null);
            d = DrawableCompat.wrap(d);

            try {
                cd = sdf.parse(txn.CreateDate);

//            header = i++ % Math.round(size / headerGroupOf) == 0 ? newHeader(Integer.toString(++lastHeaderId)) : header;
                currentHeader = DateUtils.getRelativeTimeSpanString(
                        cd.getTime(),
                        System.currentTimeMillis(),
                        DateUtils.DAY_IN_MILLIS).toString();

                if (!currentHeader.equals(lastHeader)) {
                    header = newHeader(currentHeader);
                    lastHeader = currentHeader;
                }

                list.add(new VoidItem(
                        Long.toString(txn.TransactionId),
                        d,
                        //txn.CreateDate,
                        txn.CreateDate.substring(txn.CreateDate.length()-8),
                        //Long.toString(cd.getTime()),
                        (txn.Currency == null ? "rm" : txn.Currency) + df.format(txn.Amount/100.0),
                        cd.getTime(),
                        header,
                        //txn.CardNumber,
                        "Ending <br/><b>" + txn.CardNumber.substring(txn.CardNumber.length()-4) + "</b>",
                        txn
                ));
            } catch (ParseException pe) {};
        }

        mSaleHistoryClone = list;

        //Initialize the Adapter
        mAdapter = new FlexibleAdapter<>(list);
        mAdapter.initializeListeners(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
                mListener.onVoidItemClicked(list.get(position));
                return false;
            }
        });

        //Initialize the RecyclerView and attach the Adapter to it as usual
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        //EndlessScrollListener - OnLoadMore (v5.0.0)
        mAdapter.setEndlessScrollListener(this, new ProgressItem());
        mAdapter.setEndlessScrollThreshold(1);//Default=1

        mAdapter
//                .setLongPressDragEnabled(true)
//                .setHandleDragEnabled(true)
//                .setSwipeEnabled(true)
                .setUnlinkAllItemsOnRemoveHeaders(true)
                //Show Headers at startUp, 1st call, correctly executed, no warning log message!
                .setDisplayHeadersAtStartUp(true)
                .enableStickyHeaders()
//                //Simulate developer 2nd call mistake, now it's safe, not executed, no warning log message!
//                .setDisplayHeadersAtStartUp(true)
//                //Simulate developer 3rd call mistake, still safe, not executed, warning log message displayed!
//                .showAllHeaders()
        ;


        // init swipe
        mSwipeRefreshLayout.setEnabled(true);
        initializeSwipeToRefresh();

    }

    @Override
    public void onFailure(Call<List<TransactionJSON>> call, Throwable t) {
        Log.d(TAG, "on failure -> " + t.getMessage());
        Toast.makeText(getActivity(), "Failed to retrieve transactions!", Toast.LENGTH_LONG).show();
    }

    public interface OnListFragmentInteractionListener {
        void onVoidItemClicked(IFlexible item);
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
        Log.i("SaleHistory", "onQueryTextSubmit called!");
        return onQueryTextChange(query);
    }


    // ==================== swipe refresh

    private void initializeSwipeToRefresh() {
        //Swipe down to force synchronize
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setDistanceToTriggerSync(390);
        //mSwipeRefreshLayout.setEnabled(true); Controlled by fragments!
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_purple, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Passing true as parameter we always animate the changes between the old and the new data set
//                mAdapter.updateDataSet(getSaleHistory(), true);
                mSwipeRefreshLayout.setEnabled(false);

                // todo refresh here
                mRefreshHandler.sendEmptyMessageDelayed(0, 3000L);//Simulate network time
            }
        });
    }


    private final Handler mRefreshHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0: //Stop
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setEnabled(true);
                    return true;
                case 1: //Start
                    mSwipeRefreshLayout.setRefreshing(true);
                    mSwipeRefreshLayout.setEnabled(false);
                    return true;
                case 2: //Show empty view
                    ViewCompat.animate(mEmptyView).alpha(1);
                    return true;
                default:
                    return false;
            }
        }
    });

    @Override
    public void onUpdateEmptyView(int size) {
        Log.d("SaleHistoryFragment", "onUpdateEmptyView size=" + size);

//        TextView emptyText = (TextView) findViewById(R.id.empty_text);
//        emptyText.setText(getString(R.string.no_items));
        if (size > 0) {
            mRefreshHandler.removeMessages(2);
            mEmptyView.setAlpha(0);
        } else {
            mEmptyView.setAlpha(0);
            mRefreshHandler.sendEmptyMessage(2);
        }
    }


    // ===================== LOAD MORE

    @Override
    public void onLoadMore() {
        //We don't want load more items when searching into the current Collection!
        //Alternatively, for a special filter, if we want load more items when filter is active, the
        // new items that arrive from remote, should be already filtered, before adding them to the Adapter!
        if (mAdapter.hasSearchText()) {
            mAdapter.onLoadMoreComplete(null);
            return;
        }
        Log.i(TAG, "onLoadMore invoked!");
        //Simulating asynchronous call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<IFlexible> newItems = new ArrayList<>();

                //Simulating success/failure
                int count = new SecureRandom().nextInt(5);
                int totalItemsOfType = mAdapter.getItemCountOfTypes(R.layout.fragment_sale_history_item);
                List<IFlexible> fakeItems = getSaleHistory();
                for (int i = 1; i <= count; i++) {
                    newItems.add(fakeItems.get(i));
                }

                //Callback the Adapter to notify the change:
                //- New items will be added to the end of the list
                //- When list is null or empty, ProgressItem will be hidden
                mAdapter.onLoadMoreComplete(newItems);
                for (IFlexible item : newItems) {
                    mAdapter.addItem(mAdapter.getItemCount(), item);
                }

                //Notify user
                String message = (newItems.size() > 0 ?
                        "Simulated: " + newItems.size() + " new items arrived :-)" :
                        "Simulated: No more items to load :-(");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }, 2500);
    }


}

