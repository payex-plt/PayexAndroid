package io.payex.android.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;
import io.payex.android.util.HtmlCompat;

public class RegisterFormFragment extends Fragment {

    public static final String TAG = "RegisterForm";

    @BindView(R.id.tv_title)
    AppCompatTextView mTitleTextView;

    public static RegisterFormFragment newInstance() {
        return new RegisterFormFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);
        ButterKnife.bind(this, view);

        HtmlCompat.setSpannedText(mTitleTextView, getString(R.string.register_form_desc));

        return view;
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_done)
    public void done() {
        mListener.onDonePressed();
    }

    private OnFragmentInteractionListener mListener;

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
        void onDonePressed();
    }
}
