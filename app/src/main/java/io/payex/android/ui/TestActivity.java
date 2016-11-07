package io.payex.android.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;
import io.payex.android.ui.sale.history.SaleHistoryFragment;

public class TestActivity extends BaseActivity
implements
        SaleHistoryFragment.OnListFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onListFragmentInteraction(IFlexible item) {

    }
}
