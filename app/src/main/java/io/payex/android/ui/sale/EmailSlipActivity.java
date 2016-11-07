package io.payex.android.ui.sale;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import io.payex.android.R;
import io.payex.android.ui.BaseActivity;

public class EmailSlipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_slip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setBackButton();
    }
}
