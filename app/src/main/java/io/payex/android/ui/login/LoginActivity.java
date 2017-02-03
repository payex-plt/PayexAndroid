package io.payex.android.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.design.widget.Snackbar;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.Bank;
import io.payex.android.MyApp;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.MainActivity;
import io.payex.android.ui.common.ProgressItem;
import io.payex.android.ui.register.RegisterActivity;
import io.payex.android.util.ConnectivityReceiver;
import io.payex.android.util.PayexAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity
        implements LoginFragment.OnFragmentInteractionListener, LoginHelperFragment.OnFragmentInteractionListener,
        ConnectivityReceiver.ConnectivityReceiverListener,
        SetupFragment.OnFragmentInteractionListener, Callback<Integer>
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

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
        //Snackbar.make(mRootView, "Register page under construction", Snackbar.LENGTH_LONG).show();
        startActivity(RegisterActivity.class);
    }

    @Override
    public void onLoginButtonPressed() {
        Log.d(TAG, ">>> bin <<< " + loginFragment.mBinEditText.getText());
        Log.d(TAG, ">>> mid <<< " + loginFragment.mMidEditText.getText());
        Log.d(TAG, ">>> password <<< " + loginFragment.mPasswordEditText.getText());

        if (loginFragment.mBinEditText.getText().length() > 0 && loginFragment.mMidEditText.getText().length() > 0 && loginFragment.mPasswordEditText.getText().length() > 0) {
            Bank bank = MyApp.findBank(loginFragment.mBinEditText.getText().toString());
            if (bank != null) {
                MyApp.setBank(bank);

                Log.d(TAG, "set bank -> " + bank.getName());

//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://payexterminals.azurewebsites.net")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                PayexAPI payexAPI = retrofit.create(PayexAPI.class);

                if (ConnectivityReceiver.isConnected()) {

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    //Call<Boolean> call = payexAPI.authenticate("429313", "10000052", "123456");
                    Call<Integer> call = MyApp.payexAPI.authenticate(loginFragment.mBinEditText.getText().toString(),
                            loginFragment.mMidEditText.getText().toString(), loginFragment.mPasswordEditText.getText().toString());
                    //asynchronous call
                    call.enqueue(this);

                    //                        startActivity(new Intent(getActivity(), MainActivity.class));
                    //                        getActivity().finish();
                } else {
                    Toast.makeText(this, "No internet connection.", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(this, "BIN, merchant ID and password cannot be empty!", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onForgetPasswordPressed() {
        changeFragment(R.id.fragment_container, LoginHelperFragment.newInstance());
    }

    @Override
    public void onPasswordReset(final String bin, final String mid) {
        //Snackbar.make(mRootView, "Resetting password", Snackbar.LENGTH_LONG).show();
        //Toast.makeText(this, "Resetting password...", Toast.LENGTH_LONG).show();

        new AlertDialog.Builder(this)
                .setTitle("Reset password")
                .setMessage("Do you really want to reset your password?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Call<Boolean> call = MyApp.payexAPI.emailResetPassword(bin, mid);
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {}

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {}
                        });

                        Toast.makeText(getApplicationContext(), "Sending reset password email...", Toast.LENGTH_LONG).show();
                        changeFragment(R.id.fragment_container, LoginFragment.newInstance());
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    @Override
    public void onCancelPressed() {
        changeFragment(R.id.fragment_container, LoginFragment.newInstance());
        //Snackbar.make(mRootView, "Cancel button pressed", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSetupCompleted() {
        startActivity(MainActivity.class, true);
    }

    @Override
    public void onResponse(Call<Integer> call, Response<Integer> response) {
        Log.d(TAG, "on response -> " + response.raw());
        Log.d(TAG, "on response -> " + response.body());

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (response.message().equals("OK")) {
            MyApp.setBIN(loginFragment.mBinEditText.getText().toString());
            MyApp.setMID(loginFragment.mMidEditText.getText().toString());

            changeFragment(R.id.fragment_container, SetupFragment.newInstance());
        } else {
            Toast.makeText(this, "Login error!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<Integer> call, Throwable t) {
        Log.d(TAG, "on failure -> " + t.getMessage());

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        Toast.makeText(this, "Login error!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        String status = isConnected ? "Connectd to internet." : "No internet connection.";
        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }




}

