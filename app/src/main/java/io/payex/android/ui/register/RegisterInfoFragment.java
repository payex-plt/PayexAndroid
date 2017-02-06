package io.payex.android.ui.register;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.payex.android.R;
import io.payex.android.util.HtmlCompat;

public class RegisterInfoFragment extends Fragment {

    @BindView(R.id.tv_title)
    AppCompatTextView mTitleTextView;
    @BindView(R.id.tv_caption)
    AppCompatTextView mCaptionTextView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static RegisterInfoFragment newInstance() {
        return new RegisterInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_info, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();

        HtmlCompat.setSpannedText(mTitleTextView, getString(R.string.register_info_desc));
        setupRequestLink();
        setupBankInfo(context);

        return view;
    }

    private void setupRequestLink() {
        String teaser1 = getString(R.string.register_info_teaser1);
        String teaser2 = getString(R.string.register_info_teaser2);

        String displayText = teaser1 + " " + teaser2;
        mCaptionTextView.setText(displayText);

        // Create the link rule to set what text should be linked.
        // can use a specific string or a regex pattern
        Link link = new Link(teaser2)
//                .setTextColor(Color.parseColor("#259B24"))                  // optional, defaults to holo blue
//                .setTextColorOfHighlightedLink(Color.parseColor("#0D3D0C")) // optional, defaults to holo blue
//                .setHighlightAlpha(.4f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
//                .setBold(true)                                              // optional, defaults to false
//                .setOnLongClickListener(new Link.OnLongClickListener() {
//                    @Override
//                    public void onLongClick(String clickedText) {
//                        // long clicked
//                    }
//                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        mListener.onRegisterPressed();
                    }
                })
                ;

        // create the link builder object add the link rule
        LinkBuilder.on(mCaptionTextView)
                .addLink(link)
                .setFindOnlyFirstMatchesForAnyLink(true)
                .build(); // create the clickable links
    }

    private void setupBankInfo(Context context) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);

        //Initialize the Adapter
        FlexibleAdapter<IFlexible> mAdapter = new FlexibleAdapter<>(getData());

        //Initialize the RecyclerView and attach the Adapter to it as usual
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public List<IFlexible> getData() {
        List<IFlexible> list = new ArrayList<>();

        Drawable d;

        Calendar c = Calendar.getInstance();

//        for (int i = 0; i < 25; i++) {
//            c.add(Calendar.DATE, -i);
//
//            list.add(new RegisterInfoItem(
//                    i + 1 + "",
//                    d,
//                    "+1 619-656-54456",
//                    "support@barclay.com"
//            ));
//        }

        d = VectorDrawableCompat.create(getResources(), R.drawable.ic_ambank_logo_60dp, null);
        d = DrawableCompat.wrap(d);
        list.add(new RegisterInfoItem(
                "1",
                d,
                "(+60) 3-2167 7800",
                "support@ambankgroup.com"
        ));
        d = VectorDrawableCompat.create(getResources(), R.drawable.ic_hongleong_60dp, null);
        d = DrawableCompat.wrap(d);
        list.add(new RegisterInfoItem(
                "2",
                d,
                "(+60) 3-2773 0100",
                "support@hlbb.hongleong.com.my"
        ));
        d = VectorDrawableCompat.create(getResources(), R.drawable.ic_hsbclogo_60dp, null);
        d = DrawableCompat.wrap(d);
        list.add(new RegisterInfoItem(
                "3",
                d,
                "(+60) 3 8321 5400",
                "merchant@hsbc.com.my"
        ));
        d = VectorDrawableCompat.create(getResources(), R.drawable.ic_citilogo_60dp, null);
        d = DrawableCompat.wrap(d);
        list.add(new RegisterInfoItem(
                "4",
                d,
                "(+60) 3-2383 0000",
                "merchant@citi.com.my"
        ));
//        d = VectorDrawableCompat.create(getResources(), R.drawable.ic_hsbclogo_60dp, null);
//        d = DrawableCompat.wrap(d);
//        list.add(new RegisterInfoItem(
//                "5",
//                d,
//                "(+60) 3 8321 5400",
//                "merchant@hsbc.com.my"
//        ));
//        d = VectorDrawableCompat.create(getResources(), R.drawable.ic_citilogo_60dp, null);
//        d = DrawableCompat.wrap(d);
//        list.add(new RegisterInfoItem(
//                "6",
//                d,
//                "(+60) 3-2383 0000",
//                "merchant@citi.com.my"
//        ));


        return list;
    }

    @OnClick(R.id.btn_done)
    public void done() {
        getActivity().onBackPressed();
//        mListener.onRegisterOkPressed();
    }

    private  OnFragmentInteractionListener mListener;

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
        void onRegisterPressed();
    }
}
