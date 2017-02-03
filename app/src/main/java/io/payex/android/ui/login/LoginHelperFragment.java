package io.payex.android.ui.login;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;

public class LoginHelperFragment extends Fragment {

    @BindView(R.id.et_bin) AppCompatEditText mBinEditText;
    @BindView(R.id.et_mid) AppCompatEditText mMidEditText;

    private OnFragmentInteractionListener mListener;

    public static LoginHelperFragment newInstance() {
        return new LoginHelperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_helper, container, false);
        ButterKnife.bind(this, view);

        mBinEditText.setText("429313");
        mMidEditText.setText("10000052");

        return view;
    }

    @OnClick(R.id.btn_submit)
    public void resetPassword() {
        // todo reset password
        mListener.onPasswordReset(mBinEditText.getText().toString(), mMidEditText.getText().toString());
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        // todo cancel action
        mListener.onCancelPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
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
        void onPasswordReset(String bin, String mid);
        void onCancelPressed();
    }

}
