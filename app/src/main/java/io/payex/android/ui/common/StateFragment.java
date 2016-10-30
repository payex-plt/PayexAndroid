package io.payex.android.ui.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.payex.android.R;

public class StateFragment extends Fragment {

    @BindView(R.id.iv_icon) AppCompatImageView mIcon;
    @BindView(R.id.tv_title) AppCompatTextView mTitle;
    @BindView(R.id.tv_subtitle) AppCompatTextView mSubtitle;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    @DrawableRes private int mIconRes;
    @StringRes private int mTitleRes;
    @StringRes private int mSubtitleRes;

    private OnFragmentInteractionListener mListener;

    public static StateFragment newInstance(@DrawableRes int iconRes, @StringRes int titleRes,
                                            @StringRes int subtitleRes) {
        StateFragment fragment = new StateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, iconRes);
        args.putInt(ARG_PARAM2, titleRes);
        args.putInt(ARG_PARAM3, subtitleRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIconRes = getArguments().getInt(ARG_PARAM1);
            mTitleRes = getArguments().getInt(ARG_PARAM2);
            mSubtitleRes = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state, container, false);
        ButterKnife.bind(this, view);

        // setup page
        mIcon.setVisibility(mIconRes == 0 ? View.GONE : View.VISIBLE);
        mTitle.setVisibility(mTitleRes == 0 ? View.GONE : View.VISIBLE);
        mSubtitle.setVisibility(mSubtitleRes == 0 ? View.GONE : View.VISIBLE);

        if (mIconRes > 0) {
            mIcon.setImageResource(mIconRes);
        }
        if (mTitleRes > 0) {
            mTitle.setText(mTitleRes);
        }
        if (mSubtitleRes > 0) {
            mSubtitle.setText(mSubtitleRes);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onDoneLoading();
            }
        }, 2000);
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

    /**
     * fixme for demo only
     * use this page as ui/ux only by using addFragment, remove when job done (act as dialog)
     * putting any job / logic here is not recommended
     */
    public interface OnFragmentInteractionListener {
        void onDoneLoading();
    }
}
