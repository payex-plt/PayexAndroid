package io.payex.android.ui.sale.history;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.common.StateFragment;
import io.payex.android.ui.sale.EmailSlipActivity;

public class SaleSlipActivity extends BaseActivity
        implements SaleSlipFragment.OnFragmentInteractionListener,
        SaleVoidFragment.OnFragmentInteractionListener,
        StateFragment.OnFragmentInteractionListener {

    @BindView(R.id.root_container) View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_slip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, SaleSlipFragment.newInstance());
        }
    }

    @Override
    public void onEmailButtonPressed() {
        startActivity(EmailSlipActivity.class, false);
    }

    @Override
    public void onVoidButtonPressed() {
        SaleVoidFragment f = new SaleVoidFragment();
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
}
