package io.payex.android.ui.about;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;
import io.payex.android.util.HtmlCompat;

public class AboutFragment extends Fragment {

    public static final String TAG = "AboutFragment";

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tv_about) AppCompatTextView mAboutTextView;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        setAboutMessage();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @OnClick(R.id.btn_faq)
    public void openFaqPage() {
        mListener.onLinkClicked(Uri.parse(getString(R.string.FAQ_LINK)));
    }

    @OnClick(R.id.btn_libs)
    public void openLibsPage() {
        mListener.onLinkClicked(Uri.parse(getString(R.string.LIBS_LINK)));
    }

    @OnClick(R.id.btn_terms)
    public void openTermsPage() {
        mListener.onLinkClicked(Uri.parse(getString(R.string.TERMS_LINK)));
    }

    @OnClick(R.id.btn_legal)
    public void openLegalPage() {
        mListener.onLinkClicked(Uri.parse(getString(R.string.LEGAL_LINK)));
    }

    @OnClick(R.id.btn_tutorial)
    public void openTutorial() {
        mListener.onTutorialClicked();
    }

    private void setAboutMessage() {
        String message = String.format(getString(R.string.about_copyright),
                getString(R.string.COMPANY_ABBR),
                getString(R.string.APP_VER),
                getString(R.string.COMPANY),
                Calendar.getInstance().get(Calendar.YEAR) + "");
        HtmlCompat.setSpannedText(mAboutTextView, message);
    }

    public interface OnFragmentInteractionListener {
        void onLinkClicked(Uri uri);
        void onTutorialClicked();
    }
}
