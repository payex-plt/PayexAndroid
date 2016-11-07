package io.payex.android.ui.sale;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.common.StateFragment;

public class CardReaderActivity extends BaseActivity
        implements AbstractCardReaderFragment.OnScanListener ,
        StateFragment.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setBackButton();

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, CardReaderFragment.newInstance());
        }
    }

    @Override
    public void onSuccess() {
        changeFragment(R.id.fragment_container, StateFragment.newInstance(
                R.drawable.ic_mood_black_72dp, R.string.state_title_loading, 0));
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onDoneLoading() {
        startActivity(EmailSlipActivity.class, true);
    }
}
