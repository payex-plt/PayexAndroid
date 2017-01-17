package io.payex.android.ui.register;

import android.os.Bundle;

import butterknife.ButterKnife;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;

public class RegisterActivity extends BaseActivity implements
        RegisterInfoFragment.OnFragmentInteractionListener,
        RegisterFormFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, RegisterInfoFragment.newInstance());
        }
    }

    @Override
    public void onDonePressed() {
        finish();
    }

    @Override
    public void onRegisterPressed() {
        startFragment(R.id.fragment_container, RegisterFormFragment.newInstance(), RegisterFormFragment.TAG);
    }
}
