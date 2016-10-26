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
 * Use the {@link CardReaderQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CardReaderQrFragment extends AbstractCardReaderFragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CardReaderQrFragment.
     */
    public static CardReaderQrFragment newInstance() {
        CardReaderQrFragment fragment = new CardReaderQrFragment();
        return fragment;
    }
    public CardReaderQrFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_reader_qr, container, false);
    }

    @Override
    protected String getTitle() {
        return "QR";
    }
}
