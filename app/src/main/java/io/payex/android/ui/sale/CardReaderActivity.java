package io.payex.android.ui.sale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.Date;

import butterknife.ButterKnife;
import fr.devnied.bitlib.BytesUtils;
import io.payex.android.R;
import io.payex.android.Transaction;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.common.StateFragment;
import io.payex.android.util.NFCUtils;
import io.payex.android.util.PayexAPI;
import io.payex.android.util.PayexProvider;
import io.payex.android.util.SimpleAsyncTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardReaderActivity extends BaseActivity
        implements AbstractCardReaderFragment.OnScanListener,
        StateFragment.OnFragmentInteractionListener {
    private static final String TAG = CardReaderActivity.class.getSimpleName();

    private NFCUtils mNfcUtils;
    private PayexProvider mProvider = new PayexProvider();
    private EmvCard mReadCard;
    private byte[] lastAts;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setBackButton();

        // init NfcUtils
        mNfcUtils = new NFCUtils(this);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, CardReaderFragment.newInstance());
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        startActivity(EmailSlipActivity.class, true);
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

                                Log.i(TAG, "card num -> " + mReadCard.getCardNumber());
                                Log.i(TAG, "expiry date -> " + mReadCard.getExpireDate());

                                Transaction txn = new Transaction();
                                txn.setCreateDate(new Timestamp(new Date().getTime()));
                                txn.setCurrency("RM");
                                txn.setAmount(100);
                                txn.setCardNumber(mReadCard.getCardNumber());
                                txn.setCardBrand(mReadCard.getType().getName());
                                txn.setMerchantId(1);
                                txn.setTransactionTypeId(1);

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://payexterminals.payex.io")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                PayexAPI payexAPI = retrofit.create(PayexAPI.class);

                                Call<Transaction> call = payexAPI.newTransaction(txn);
                                call.enqueue(new Callback<Transaction>() {
                                    @Override
                                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Transaction> call, Throwable t) {

                                    }
                                });

                            } else if (mCard.isNfcLocked()) {
                            }
                        } else {
                        }
                    } else {
                    }

                    onDoneLoading();
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
}
