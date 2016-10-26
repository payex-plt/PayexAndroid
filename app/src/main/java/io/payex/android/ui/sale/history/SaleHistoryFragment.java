package io.payex.android.ui.sale.history;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SaleHistoryFragment extends Fragment {

    @BindView(R.id.rv_sale_history) RecyclerView mRecyclerView;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SaleHistoryFragment() {
    }

    @SuppressWarnings("unused")
    public static SaleHistoryFragment newInstance(int columnCount) {
        SaleHistoryFragment fragment = new SaleHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_history, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

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

        for (int i = 0 ; i < 25 ; i++) {
            list.add(new SaleHistoryItem(i + "", "Payment RM8.00 " + i));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(IFlexible item);
    }
}
