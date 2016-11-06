package io.payex.android.ui.sale;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.payex.android.R;

public class SaleLogoItem extends AbstractFlexibleItem<SaleLogoItem.SaleLogoItemHolder> {

    private String mId;
    private Drawable mLogo;

    SaleLogoItem(String id, Drawable logo) {
        this.mId = id;
        this.mLogo = logo;
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     * This will be explained in next Wiki page.
     */
    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof SaleNumPadItem) {
            SaleLogoItem inItem = (SaleLogoItem) inObject;
            return this.mId.equals(inItem.mId);
        }
        return false;
    }

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sale_logo_item;
    }

    /**
     * The Adapter is provided, because it will become useful for the SaleHistoryItemHolder.
     * The unique instance of the LayoutInflater is also provided to simplify the
     * creation of the VH.
     */
    @Override
    public SaleLogoItemHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                 ViewGroup parent) {
        return new SaleLogoItemHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    /**
     * Also here the Adapter is provided to get more specific information from it.
     * NonNull Payload is provided as well, you should use it more often.
     */
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SaleLogoItemHolder holder, int position,
                               List payloads) {
        holder.mLogoView.setImageDrawable(mLogo);
    }

    class SaleLogoItemHolder extends FlexibleViewHolder {

        AppCompatImageView mLogoView;

        SaleLogoItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mLogoView = (AppCompatImageView) view.findViewById(R.id.iv_logo);
        }
    }

}

