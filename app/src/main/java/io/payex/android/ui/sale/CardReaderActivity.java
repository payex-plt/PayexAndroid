package io.payex.android.ui.sale;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;

import butterknife.ButterKnife;
import fr.devnied.bitlib.BytesUtils;
import io.payex.android.R;
import io.payex.android.ui.BaseActivity;
import io.payex.android.ui.common.StateFragment;
import io.payex.android.util.NFCUtils;
import io.payex.android.util.PayexProvider;
import io.payex.android.util.SimpleAsyncTask;

public class CardReaderActivity extends BaseActivity
        implements AbstractCardReaderFragment.OnScanListener ,
        StateFragment.OnFragmentInteractionListener
{
    private static final String TAG = CardReaderActivity.class.getSimpleName();

    private NFCUtils mNfcUtils;
    private PayexProvider mProvider = new PayexProvider();
    private EmvCard mReadCard;
    private byte[] lastAts;

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
}
