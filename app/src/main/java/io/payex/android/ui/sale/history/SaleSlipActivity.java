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
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.login.LoginFragment;
import io.payex.android.ui.sale.EmailSlipActivity;

public class SaleSlipActivity extends BaseActivity {


    @BindView(R.id.root_container) View view;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_slip);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, SaleSlipFragment.newInstance());
        }
    }


}
