package io.payex.android.ui.sale;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class AbstractCardReaderFragment extends Fragment {

    protected abstract String getTitle();

    protected OnScanListener mListener;

    public interface OnScanListener {
        void onSuccess();
        void onFailed();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanListener) {
            mListener = (OnScanListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScanListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}

