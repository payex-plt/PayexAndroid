package io.payex.android.ui.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;

public class SetupFragment extends Fragment {

    @BindView(R.id.tv_progress) AppCompatTextView mProgressTextView;
    @BindView(R.id.progress_bar) ProgressBar mProgressBarView;

    private OnFragmentInteractionListener mListener;

    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);
        ButterKnife.bind(this, view);

        new DelayTask(this).execute();
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

    public interface OnFragmentInteractionListener {
        void onSetupCompleted();
    }

    public static class DelayTask extends AsyncTask<Void, Integer, String> {
        private int count = 0;
        private WeakReference<SetupFragment> mRef;
        private String mProgressMessage;

        DelayTask(SetupFragment f) {
            this.mRef = new WeakReference<>(f);
            mProgressMessage = f.getString(R.string.setup_progress);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            while (count < 10) {
                SystemClock.sleep(250);
                count++;
                publishProgress(count * 10);
            }
            return "Complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mRef.get().mProgressBarView.setProgress(values[0]);
            mRef.get().mProgressTextView.setText(String.format(mProgressMessage, values[0] + "%"));
        }

        protected void onPostExecute(String result) {
            mRef.get().mListener.onSetupCompleted();
        }
    }
}
