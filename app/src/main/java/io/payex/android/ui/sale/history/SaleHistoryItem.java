package io.payex.android.ui.sale.history;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.payex.android.R;
import io.payex.android.TransactionJSON;
import io.payex.android.ui.sale.HeaderItem;
import io.payex.android.ui.sale.voided.VoidItem;
import io.payex.android.util.HtmlCompat;

public class SaleHistoryItem extends AbstractFlexibleItem<SaleHistoryItem.SaleHistoryItemHolder>
implements IFilterable, ISectionable<SaleHistoryItem.SaleHistoryItemHolder, HeaderItem>
{

    private String mId;
    private Drawable mIcon;
    private String mPrimaryText;
    private String mSecondaryText;
    private long mTimestampMs;
    HeaderItem header;
    private String card;   // ending pan
    private boolean voidStatus;
    private TransactionJSON txn;

    public String getPrimaryText() {
        return mPrimaryText;
    }

    public String getSecondaryText() {
        return mSecondaryText;
    }

    public long getTimestampText() {
        return mTimestampMs;
    }

    SaleHistoryItem(String id, Drawable icon, String primary, String secondary, long timestamp, HeaderItem header, String card, boolean voidStatus, TransactionJSON txn) {
        this.mId = id;
        this.mIcon = icon;
        this.mPrimaryText = primary;
        this.mSecondaryText = secondary;
        this.mTimestampMs = timestamp;
        this.header = header;
        setHidden(false);
        setSelectable(false);
        this.card = card;
        this.voidStatus = voidStatus;
        this.txn = txn;
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     * This will be explained in next Wiki page.
     */
    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof SaleHistoryItem) {
            SaleHistoryItem inItem = (SaleHistoryItem) inObject;
            return this.mId.equals(inItem.mId);
        }
        return false;
    }

    /**
     * Override this method too, when using functionalities like Filter or CollapseAll.
     * FlexibleAdapter is making use of HashSet to improve performance in big list.
     */
    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sale_history_item;
    }

    /**
     * The Adapter is provided, because it will become useful for the SaleHistoryItemHolder.
     * The unique instance of the LayoutInflater is also provided to simplify the
     * creation of the VH.
     */
    @Override
    public SaleHistoryItemHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                  ViewGroup parent) {
        return new SaleHistoryItemHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    /**
     * Also here the Adapter is provided to get more specific information from it.
     * NonNull Payload is provided as well, you should use it more often.
     */
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SaleHistoryItemHolder holder, int position,
                               List payloads) {
        holder.mIconView.setImageDrawable(mIcon);
        holder.mPrimaryView.setText(mPrimaryText);
        holder.mSecondaryView.setText(mSecondaryText);

        HtmlCompat.setSpannedText(holder.mTimestampView, card);

//        holder.mTimestampView.setText(
//                DateUtils.getRelativeTimeSpanString(
//                        mTimestampMs,
//                        System.currentTimeMillis(),
//                        DateUtils.DAY_IN_MILLIS).toString());


//        if (mTimestampMs / 1000 % 5 == 4) {
//            holder.mView.setBackgroundColor(Color.LTGRAY);
//        }
        if (voidStatus) {
            holder.mView.setBackgroundColor(Color.LTGRAY);
        }
    }

    public TransactionJSON getTxn() { return txn; }

    @Override
    public boolean filter(String constraint) {
        return mSecondaryText.contains(constraint);
    }

    @Override
    public HeaderItem getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderItem header) {
        this.header = header;
    }

    class SaleHistoryItemHolder extends FlexibleViewHolder {

        AppCompatImageView mIconView;
        AppCompatTextView mPrimaryView;
        AppCompatTextView mSecondaryView;
        AppCompatTextView mTimestampView;
        View mView;

        SaleHistoryItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mView = view;
            mIconView = (AppCompatImageView) view.findViewById(R.id.iv_icon);
            mPrimaryView = (AppCompatTextView) view.findViewById(R.id.tv_primary);
            mSecondaryView = (AppCompatTextView) view.findViewById(R.id.tv_secondary);
            mTimestampView = (AppCompatTextView) view.findViewById(R.id.tv_timestamp);
        }
    }

}
