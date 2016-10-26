package io.payex.android.ui.sale;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.payex.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardReaderNfcFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CardReaderNfcFragment extends AbstractCardReaderFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CardReaderNfcFragment.
     */
    public static CardReaderNfcFragment newInstance() {
        CardReaderNfcFragment fragment = new CardReaderNfcFragment();
        return fragment;
    }
    public CardReaderNfcFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_reader_nfc, container, false);
    }

    @Override
    protected String getTitle() {
        return "NFC";
    }
}
