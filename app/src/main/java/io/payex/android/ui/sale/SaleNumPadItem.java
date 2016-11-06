package io.payex.android.ui.sale;

import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.payex.android.R;

public class SaleNumPadItem extends AbstractFlexibleItem<SaleNumPadItem.SaleNumPadItemHolder> {

    private String mId;
    private String mPrimaryText;

    public String getPrimaryText() {
        return mPrimaryText;
    }


    SaleNumPadItem(String id, String primary) {
        this.mId = id;
        this.mPrimaryText = primary;
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     * This will be explained in next Wiki page.
     */
    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof SaleNumPadItem) {
            SaleNumPadItem inItem = (SaleNumPadItem) inObject;
            return this.mId.equals(inItem.mId);
        }
        return false;
    }

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sale_numpad_item;
    }

    /**
     * The Adapter is provided, because it will become useful for the SaleHistoryItemHolder.
     * The unique instance of the LayoutInflater is also provided to simplify the
     * creation of the VH.
     */
    @Override
    public SaleNumPadItemHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                 ViewGroup parent) {
        return new SaleNumPadItemHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    /**
     * Also here the Adapter is provided to get more specific information from it.
     * NonNull Payload is provided as well, you should use it more often.
     */
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SaleNumPadItemHolder holder, int position,
                               List payloads) {
        holder.mPrimaryView.setText(mPrimaryText);
    }

    class SaleNumPadItemHolder extends FlexibleViewHolder {

        AppCompatTextView mPrimaryView;

        SaleNumPadItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mPrimaryView = (AppCompatTextView) view.findViewById(R.id.tv_primary);
        }
    }

}
