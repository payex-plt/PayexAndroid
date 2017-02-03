package io.payex.android.ui.sale.voided;

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
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;
import io.payex.android.TransactionJSON;
import io.payex.android.ui.sale.history.SaleSlipActivity;

public class VoidSlipFragment extends Fragment {

    @BindView(R.id.iv_card_type) AppCompatImageView mCardType;
    @BindView(R.id.tv_card_pan) AppCompatTextView mCardPAN;
    @BindView(R.id.tv_payment_made_value) AppCompatTextView mPaymentMade;
    @BindView(R.id.tv_time_payment_made_value) AppCompatTextView mTimePaymentMade;
    @BindView(R.id.tv_amount_value) AppCompatTextView mAmount;
    @BindView(R.id.tv_txn_num_value) AppCompatTextView mTxnNum;
    @BindView(R.id.tv_approval_code_value) AppCompatTextView mApprovalCode;
    @BindView(R.id.tv_payment_voided) AppCompatTextView mPaymentVoided;
    @BindView(R.id.btn_void) AppCompatButton mButtonVoid;
    @BindView(R.id.btn_resend) AppCompatButton mButtonResend;
//    @BindView(R.id.tv_void_slip_title) AppCompatTextView mVoidSlipTitle;

    @OnClick(R.id.btn_void)
    public void voidSale() {
        mListener.onVoidButtonPressed();
    }

    @OnClick(R.id.btn_resend)
    public void resendSale() {
        ((VoidSlipActivity) getActivity()).setTransactionId(txn.TransactionId);
        mListener.onResendButtonPressed();
    }


    private OnFragmentInteractionListener mListener;
    private TransactionJSON txn;

    public static VoidSlipFragment newInstance() {
        VoidSlipFragment fragment = new VoidSlipFragment();
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
            txn = (TransactionJSON) getArguments().getSerializable("VoidItem");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_void_slip, container, false);
        ButterKnife.bind(this, view);

        DecimalFormat df = new DecimalFormat("###,##0.00");

        Drawable d = VectorDrawableCompat.create(getResources(),
                txn.CardBrand.toLowerCase().equals("visa") ? R.drawable.ic_visa_75dp : R.drawable.ic_mastercard_75dp,
                null);
        d = DrawableCompat.wrap(d);


        mCardType.setImageDrawable(d);
        mCardPAN.setText("Ending " + txn.CardNumber.substring(txn.CardNumber.length()-4));

        String[] dates = txn.CreateDate.split("T");
//        mPaymentMade.setText(txn.CreateDate.replace("T", "  "));
        mPaymentMade.setText(dates[0]);
        mTimePaymentMade.setText(dates[1]);

        mAmount.setText((txn.Currency == null ? "rm" : txn.Currency) + (" " + df.format(txn.Amount/100.0)));
        if (txn.VoidStatus) {
            //mVoidSlipTitle.setText("Resend Void Slip?");
            mTxnNum.setText(txn.VoidTxnNumber);
            mApprovalCode.setText(txn.VoidApprovalCode);
            mPaymentVoided.setText("Void");
            mButtonVoid.setVisibility(View.GONE);
            mButtonResend.setVisibility(View.VISIBLE);
        } else {
            //mVoidSlipTitle.setText("Confirm Void?");
            mTxnNum.setText(String.valueOf(txn.TxnNumber));
            mApprovalCode.setText(txn.ApprovalCode);
            mPaymentVoided.setText("N/A");
            mButtonVoid.setVisibility(View.VISIBLE);
            mButtonResend.setVisibility(View.GONE);

        }

        return view;
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
        void onVoidButtonPressed();
        void onResendButtonPressed();
    }
}
