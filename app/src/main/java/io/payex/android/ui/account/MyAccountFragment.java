package io.payex.android.ui.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;
import io.payex.android.util.HtmlCompat;

public class MyAccountFragment extends Fragment {

    @BindView(R.id.tv_merchant) AppCompatTextView mMerchantTextView;
    @BindView(R.id.tv_acquirer) AppCompatTextView mAcquirerTextView;

    public static MyAccountFragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, view);
        setMerchantInfo();
        setAcquirerInfo();
        return view;
    }

    private void setMerchantInfo() {
        String message = String.format(getString(R.string.account_merchant),
                "Starbucks Malaysia",
                "7946328",
                "12943864");
        HtmlCompat.setSpannedText(mMerchantTextView, message);
    }

    private void setAcquirerInfo() {
        String message = String.format(getString(R.string.account_acquirer),
                "Ambank Malaysia",
                "(60)3-2132 6532",
                "(60)2-5653 0964",
                "info@ambank.com.my");
        HtmlCompat.setSpannedText(mAcquirerTextView, message);
    }

}
