package io.payex.android.ui.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.payex.android.R;
import io.payex.android.ui.sale.SaleFragment;

public class LoginFragment extends Fragment {

    @BindView(R.id.et_bin) AppCompatEditText mBinEditText;
    @BindView(R.id.et_mid) AppCompatEditText mMidEditText;
    @BindView(R.id.et_password) AppCompatEditText mPasswordEditText;

    private OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        mBinEditText.setText("429313");
        mMidEditText.setText("10000052");
        mPasswordEditText.requestFocus();

        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    attemptLogin();
                }
                return false;
            }
        });

        return view;
    }

    @OnClick(R.id.btn_login)
    public void attemptLogin() {
        mListener.onLoginButtonPressed();
    }

    @OnClick(R.id.btn_login_help)
    public void loginHelp() {
        mListener.onLoginHelpButtonPressed();
    }

    @OnClick(R.id.btn_register)
    public void register() {
        mListener.onRegisterButtonPressed();
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

    public interface OnFragmentInteractionListener {
        void onLoginHelpButtonPressed();

        void onRegisterButtonPressed();

        void onLoginButtonPressed();
    }
}
