package io.payex.android.ui.sale.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;
import io.payex.android.ui.sale.EmailSlipActivity;

public class SaleSlipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_slip);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // TODO use bottom navigation bar v25.0.0 to handle email/void

    @OnClick(R.id.btn_email)
    public void emailSlip() {
        Intent i = new Intent(this, EmailSlipActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_void)
    public void voidSale() {
        // TODO void sale
    }


}
