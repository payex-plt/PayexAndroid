package io.payex.android.ui.login;

import android.support.design.widget.Snackbar;

import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.MainActivity;

public class LoginActivity extends BaseActivity
        implements LoginFragment.OnFragmentInteractionListener, LoginHelperFragment.OnFragmentInteractionListener {

    @BindView(R.id.root_container) View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, LoginFragment.newInstance());
        }
    }

    @Override
    public void onLoginHelpButtonPressed() {
        changeFragment(R.id.fragment_container, LoginHelperFragment.newInstance());
    }

    @Override
    public void onRegisterButtonPressed() {
        // todo register page
        Snackbar.make(mRootView, "Register page under construction", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onLoginButtonPressed() {
        startActivity(MainActivity.class, true);
    }

    @Override
    public void onPasswordReset() {
        startActivity(MainActivity.class, true);
    }

}

