package io.payex.android.ui.sale.history;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;
import io.payex.android.TransactionJSON;
import io.payex.android.ui.sale.voided.VoidSlipFragment;

public class SaleSlipFragment extends Fragment {

    @BindView(R.id.iv_card_type)
    AppCompatImageView mCardType;
    @BindView(R.id.tv_card_pan)
    AppCompatTextView mCardPAN;
    @BindView(R.id.tv_payment_made_value) AppCompatTextView mPaymentMade;
    @BindView(R.id.tv_amount_value) AppCompatTextView mAmount;
    @BindView(R.id.tv_txn_num_value) AppCompatTextView mTxnNum;
    @BindView(R.id.tv_approval_code_value) AppCompatTextView mApprovalCode;
    @BindView(R.id.tv_payment_voided) AppCompatTextView mPaymentVoided;
    @BindView(R.id.btn_resend) AppCompatButton mButtonResend;

    @OnClick(R.id.btn_resend)
    public void resendSalesSlip() {
        ((SaleSlipActivity) getActivity()).setTransactionId(txn.TransactionId);
        mListener.onResendButtonPressed();
    }


    private SaleSlipFragment.OnFragmentInteractionListener mListener;
    private TransactionJSON txn;

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
            txn = (TransactionJSON) getArguments().getSerializable("SalesItem");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_slip, container, false);
        ButterKnife.bind(this, view);

        DecimalFormat df = new DecimalFormat("###,###.00");

        Drawable d = VectorDrawableCompat.create(getResources(),
                txn.CardBrand.toLowerCase().equals("visa") ? R.drawable.ic_visa_40dp : R.drawable.ic_mastercard_40dp,
                null);
        d = DrawableCompat.wrap(d);


        mCardType.setImageDrawable(d);
        mCardPAN.setText("Ending " + txn.CardNumber.substring(txn.CardNumber.length()-4));
        mPaymentMade.setText(txn.CreateDate.replace("T", "  "));
        mAmount.setText((txn.Currency == null ? "rm" : txn.Currency) + df.format(txn.Amount/100.0));
        mTxnNum.setText(String.valueOf(txn.TxnNumber));
        mApprovalCode.setText(txn.ApprovalCode);

        if (txn.VoidStatus) {
            mPaymentVoided.setText("Yes");
        } else {
            mPaymentVoided.setText("No");
        }
        //mButtonResend.setText("Resend Sales Slip");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaleSlipFragment.OnFragmentInteractionListener) {
            mListener = (SaleSlipFragment.OnFragmentInteractionListener) context;
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
        void onResendButtonPressed();
    }
}
