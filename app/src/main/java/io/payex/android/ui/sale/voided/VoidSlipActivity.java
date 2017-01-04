package io.payex.android.ui.sale.voided;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.common.StateFragment;
import io.payex.android.ui.sale.EmailSlipActivity;
import io.payex.android.ui.sale.history.SaleResendFragment;
import io.payex.android.ui.sale.history.SaleVoidFragment;

public class VoidSlipActivity extends BaseActivity
        implements VoidSlipFragment.OnFragmentInteractionListener,
        SaleVoidFragment.OnFragmentInteractionListener,
        VoidResendFragment.OnFragmentInteractionListener,
        StateFragment.OnFragmentInteractionListener {

    private long transactionId;

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    @BindView(R.id.root_container) View view;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_slip);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setBackButton();

        if (savedInstanceState == null) {
            VoidSlipFragment fragment = VoidSlipFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putSerializable("VoidItem", getIntent().getExtras().getSerializable("VoidItem"));

            fragment.setArguments(bundle);
            addFragment(R.id.fragment_container, fragment);
        }
    }

    @Override
    public void onVoidButtonPressed() {
        SaleVoidFragment f = new SaleVoidFragment();
        f.show(getSupportFragmentManager(), f.getTag());
    }

    @Override
    public void onResendButtonPressed() {
        VoidResendFragment f = new VoidResendFragment();
        f.show(getSupportFragmentManager(), f.getTag());
    }

    @Override
    public void onConfirmVoidButtonPressed() {
        changeFragment(R.id.fragment_container, StateFragment.newInstance(
                R.drawable.ic_mood_black_72dp, R.string.state_title_loading, 0));
    }

    @Override
    public void onDoneLoading() {
        // fixme start activity as result
        startActivity(EmailSlipActivity.class, true);
    }

    @Override
    public void onConfirmResendButtonPressed() {

        Intent intent = new Intent(this, EmailSlipActivity.class);
        intent.putExtra("emailtype", "voidslip");
        intent.putExtra("transactionid", transactionId);

        startActivity(intent);  finish();
        //startActivity(EmailSlipActivity.class, true);
    }
}
