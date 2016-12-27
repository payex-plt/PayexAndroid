package io.payex.android.ui.sale;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import io.payex.android.R;
import io.payex.android.ui.BaseActivity;

public class EmailSlipActivity extends BaseActivity implements EmailSlipFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_slip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setBackButton();
    }

    @Override
    public void onSendEmailButtonPressed() {
        //Toast.makeText(this, "send button pressed!", Toast.LENGTH_LONG).show();
        finish();
    }
}
