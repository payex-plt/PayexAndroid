package io.payex.android.ui.sale.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;
import io.payex.android.ui.sale.SaleFragment;

public class SaleSlipFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static SaleSlipFragment newInstance() {
        SaleSlipFragment fragment = new SaleSlipFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_slip, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_email)
    public void sendSlip() {
        mListener.onEmailButtonPressed();
    }

    @OnClick(R.id.btn_void)
    public void voidSale() {
        mListener.onVoidButtonPressed();
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
        void onEmailButtonPressed();
        void onVoidButtonPressed();
    }
}
