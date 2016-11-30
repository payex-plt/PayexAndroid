package io.payex.android.ui.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.Bank;
import io.payex.android.MyApp;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.MainActivity;
import io.payex.android.util.PayexAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity
        implements LoginFragment.OnFragmentInteractionListener, LoginHelperFragment.OnFragmentInteractionListener,
SetupFragment.OnFragmentInteractionListener, Callback<Boolean>
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.root_container) View mRootView;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginFragment = LoginFragment.newInstance();
        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, loginFragment);
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
        if (loginFragment.mBinEditText.getText().length() > 0 && loginFragment.mMidEditText.getText().length() > 0 && loginFragment.mPasswordEditText.getText().length() > 0) {
            Bank bank = MyApp.findBank(loginFragment.mBinEditText.getText().toString());
            if (bank != null) {
                MyApp.setBank(bank);

                Log.d(TAG, "set bank -> " + bank.getName());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://payexterminals.azurewebsites.net")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PayexAPI payexAPI = retrofit.create(PayexAPI.class);

                //Call<Boolean> call = payexAPI.authenticate("429313", "10000052", "123456");
                Call<Boolean> call = payexAPI.authenticate(loginFragment.mBinEditText.getText().toString(),
                        loginFragment.mMidEditText.getText().toString(), loginFragment.mPasswordEditText.getText().toString());
                //asynchronous call
                call.enqueue(this);

                //                        startActivity(new Intent(getActivity(), MainActivity.class));
                //                        getActivity().finish();
            }

        } else {
            Toast.makeText(this, "BIN, merchant ID and password cannot be empty!", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPasswordReset() {
        Snackbar.make(mRootView, "Resetting password", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSetupCompleted() {
        startActivity(MainActivity.class, true);
    }

    @Override
    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        Log.d(TAG, "response -> " + response.raw());

        if (response.message().equals("OK")) {
            changeFragment(R.id.fragment_container, SetupFragment.newInstance());
        } else {
            Toast.makeText(this, "Login error!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<Boolean> call, Throwable t) {
        Toast.makeText(this, "Login error!", Toast.LENGTH_LONG).show();
    }
}

