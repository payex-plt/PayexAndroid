package io.payex.android.ui.sale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.payex.android.R;

import static android.R.attr.onClick;

public class EmailSlipFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.et_email) EditText mEmailEditText;


    @OnClick(R.id.btn_send)
    public void sendEmail() {
        // check non-empty email address
        if (mEmailEditText.getText().length() > 0) {
            ((EmailSlipActivity) getActivity()).setEmail(mEmailEditText.getText().toString());
            mListener.onSendEmailButtonPressed();
        }
        else {
            Toast.makeText(getActivity(), "Email address cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_slip, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EmailSlipFragment.OnFragmentInteractionListener) {
            mListener = (EmailSlipFragment.OnFragmentInteractionListener) context;
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
        void onSendEmailButtonPressed();
    }
}
