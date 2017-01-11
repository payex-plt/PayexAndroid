package io.payex.android.ui.sale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import io.payex.android.MyApp;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.login.LoginActivity;
import io.payex.android.ui.login.LoginFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailSlipActivity extends BaseActivity implements EmailSlipFragment.OnFragmentInteractionListener, Callback<Boolean> {

    private static final String TAG = EmailSlipActivity.class.getSimpleName();

    private String email;
    private String emailType;
    private long transactionId;

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_slip);

        Intent intent = getIntent();
        emailType = intent.getStringExtra("emailtype");
        transactionId = intent.getLongExtra("transactionid", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setBackButton();
    }

    @Override
    public void onSendEmailButtonPressed() {
        Log.d(TAG, ">>> send email button presses <<<");

        if (email.length() > 0 && transactionId != 0) {
            if (emailType.equals("salesslip")) {
                Log.d(TAG, ">>> email sales slip " + transactionId + " to " + email + " <<<");
                Call<Boolean> call = MyApp.payexAPI.emailSalesSlip(email, transactionId);
                call.enqueue(this);

                Toast.makeText(this, "Sending email...", Toast.LENGTH_LONG).show();

            } else if (emailType.equals("voidslip")) {
                Log.d(TAG, ">>> email void slip " + transactionId + " to " + email + " <<<");
                Call<Boolean> call = MyApp.payexAPI.emailVoidSlip(email, transactionId);
                call.enqueue(this);

                Toast.makeText(this, "Sending email...", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "No email sent!", Toast.LENGTH_LONG).show();
            }
        }

        finish();
    }

    @Override
    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        //Toast.makeText(this, "Sending email...", Toast.LENGTH_LONG).show();
        //finish();
    }

    @Override
    public void onFailure(Call<Boolean> call, Throwable t) {
        Toast.makeText(this, "Send email error!", Toast.LENGTH_LONG).show();
        //finish();
    }
}
