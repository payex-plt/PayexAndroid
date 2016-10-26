package io.payex.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;
import io.payex.android.ui.sale.history.SaleHistoryFragment;
import io.payex.android.ui.sale.history.SaleHistoryItem;
import io.payex.android.ui.sale.history.SaleSlipActivity;

public class TestActivity extends AppCompatActivity
        implements SaleHistoryFragment.OnListFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onListFragmentInteraction(IFlexible item) {
        if (item instanceof SaleHistoryItem) {
            Log.e("TAG" ,  ((SaleHistoryItem) item).getTitle());
            Intent i = new Intent(this, SaleSlipActivity.class);
            startActivity(i);
        }
    }

    /*@Override
    public void onFragmentInteraction(Uri uri) {

    }*/
}
