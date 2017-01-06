package io.payex.android.ui.sale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.parser.EmvParser;
import com.github.devnied.emvnfccard.utils.AtrUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import fr.devnied.bitlib.BytesUtils;
import io.payex.android.MyApp;
import io.payex.android.R;
import io.payex.android.Transaction;
import io.payex.android.TransactionJSON;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.MainActivity;
import io.payex.android.ui.common.StateFragment;
import io.payex.android.util.NFCUtils;
import io.payex.android.util.PayexProvider;
import io.payex.android.util.SimpleAsyncTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardReaderActivity extends BaseActivity
        implements AbstractCardReaderFragment.OnScanListener,
        CardReaderFragment.OnFragmentInteractionListener,
        StateFragment.OnFragmentInteractionListener {
    private static final String TAG = CardReaderActivity.class.getSimpleName();

    private NFCUtils mNfcUtils;
    private PayexProvider mProvider = new PayexProvider();
    private EmvCard mReadCard;
    private byte[] lastAts;

    private long transactionId;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @BindView(R.id.tv_primary)
    AppCompatTextView mPrimaryText;
    @BindView(R.id.rv_logo)
    RecyclerView mLogo;
    @BindView(R.id.rv_numpad)
    RecyclerView mNumpad;

    public RecyclerView getmNumpad() {
        return mNumpad;
    }

    public AppCompatTextView getmPrimaryText() {
        return mPrimaryText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setBackButton();

        // get amount from previous page
        String amount = MainActivity.buildAmountText(MyApp.getCurrency(), MainActivity.getAmount());
        if (getIntent() != null) {
            String temp = getIntent().getStringExtra("AMOUNT");
            if (!TextUtils.isEmpty(temp)) {
                amount = temp;
            }
        }
        mPrimaryText.setText(amount);

        setupLogo(this);


        // init NfcUtils
        mNfcUtils = new NFCUtils(this);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, CardReaderFragment.newInstance());
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupLogo(Context context) {
        // todo size of columns need more research. max now is 4 on my tiny phone
        List<IFlexible> logos = getLogos();
        mLogo.setLayoutManager(new GridLayoutManager(context, logos.size()));
        mLogo.setHasFixedSize(true);
        mLogo.setAdapter(new FlexibleAdapter<>(logos));
    }

    private List<IFlexible> getLogos() {
        List<IFlexible> list = new ArrayList<>();

        int[] logos = { R.drawable.ic_ambank_40dp, R.drawable.ic_visa_40dp, R.drawable.ic_mastercard_40dp };

        int max = logos.length;    //3;
        for (int i = 0 ; i < max ; i++) {

            Drawable d = VectorDrawableCompat.create(getResources(), logos[i], null);
            d = DrawableCompat.wrap(d);

            list.add(new SaleLogoItem(i + 1 + "", d));
        }

        return list;
    }

    @Override
    public void onSuccess() {
        changeFragment(R.id.fragment_container, StateFragment.newInstance(
                R.drawable.ic_mood_black_72dp, R.string.state_title_loading, 0));


    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onDoneLoading() {
        Intent intent = new Intent(this, EmailSlipActivity.class);
        intent.putExtra("emailtype", "salesslip");
        intent.putExtra("transactionid", transactionId);

        startActivity(intent);  finish();

        //startActivity(EmailSlipActivity.class, true);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (mTag != null) {

            new SimpleAsyncTask() {

                private IsoDep mTagcomm;
                private EmvCard mCard;

                private boolean mException;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    onSuccess();
                }

                @Override
                protected void doInBackground() {

                    mTagcomm = IsoDep.get(mTag);
                    if (mTagcomm == null) {
                        return;
                    }
                    mException = false;

                    try {
                        mReadCard = null;
                        // Open connection
                        mTagcomm.connect();
                        lastAts = getAts(mTagcomm);

                        mProvider.setmTagCom(mTagcomm);

                        EmvParser parser = new EmvParser(mProvider, true);
                        mCard = parser.readEmvCard();
                        if (mCard != null) {
                            mCard.setAtrDescription(extractAtsDescription(lastAts));
                        }

                    } catch (IOException e) {
                        mException = true;
                    } finally {
                        // close tagcomm
                        IOUtils.closeQuietly(mTagcomm);
                    }
                }

                @Override
                protected void onPostExecute(final Object result) {

                    if (!mException) {
                        if (mCard != null) {
                            if (StringUtils.isNotBlank(mCard.getCardNumber())) {
                                mReadCard = mCard;

                                Log.i(TAG, ">>> card num <<< " + mReadCard.getCardNumber());
                                Log.i(TAG, ">>> expiry date <<< " + mReadCard.getExpireDate());

                                saveTransaction();

                            } else if (mCard.isNfcLocked()) {
                            }
                        } else {
                        }
                    } else {
                    }

                    //onDoneLoading();
                    mNfcUtils.disableDispatch();
                }

            }.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNfcUtils.enableDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcUtils.disableDispatch();
    }

    public Collection<String> extractAtsDescription(final byte[] pAts) {
        return AtrUtils.getDescriptionFromAts(BytesUtils.bytesToString(pAts));
    }

    private byte[] getAts(final IsoDep pIso) {
        byte[] ret = null;
        if (pIso.isConnected()) {
            // Extract ATS from NFC-A
            ret = pIso.getHistoricalBytes();
            if (ret == null) {
                // Extract ATS from NFC-B
                ret = pIso.getHiLayerResponse();
            }
        }
        return ret;
    }

    public byte[] getLastAts() {
        return lastAts;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CardReader Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private String getNextNumber() {
        return String.valueOf(10000 + (int) (Math.random() * ((99999 - 10000)+ 1)));
    }

    private void saveTransaction() {
        Transaction txn = new Transaction();
        txn.setCreateDate(new Timestamp(new Date().getTime()));
        txn.setCurrency(MyApp.getCurrency());
        txn.setAmount(MainActivity.getAmount());
        txn.setCardNumber(mReadCard.getCardNumber());
        txn.setCardBrand(mReadCard.getType().getName());
        txn.setMerchantTxnNumber(getNextNumber());
        txn.setTxnNumber(getNextNumber());
        txn.setApprovalCode(getNextNumber());
        txn.setMerchantId(1);
        txn.setTransactionTypeId(1);

//                                Retrofit retrofit = new Retrofit.Builder()
//                                        .baseUrl("http://payexterminals.payex.io")
//                                        .addConverterFactory(GsonConverterFactory.create())
//                                        .build();
//
//                                PayexAPI payexAPI = retrofit.create(PayexAPI.class);

        Call<TransactionJSON> call = MyApp.payexAPI.newTransaction(txn);
        call.enqueue(new Callback<TransactionJSON>() {
            @Override
            public void onResponse(Call<TransactionJSON> call, Response<TransactionJSON> response) {
                Log.d(TAG, ">>> new txn id <<< " + response.body().TransactionId);
                transactionId = response.body().TransactionId;
                onDoneLoading();
            }

            @Override
            public void onFailure(Call<TransactionJSON> call, Throwable t) {

            }
        });

    }

    @Override
    public void onEnterPressed(String text) {
        // what to do after enter cvv
    }
}
