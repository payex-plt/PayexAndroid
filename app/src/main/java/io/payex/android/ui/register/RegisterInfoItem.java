package io.payex.android.ui.register;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.payex.android.R;

public class RegisterInfoItem extends AbstractFlexibleItem<RegisterInfoItem.RegisterInfoItemHolder>
        implements IFilterable
{

    private String mId;
    private Drawable mIcon;
    private String mPrimaryText;
    private String mSecondaryText;

    public String getPrimaryText() {
        return mPrimaryText;
    }

    public String getSecondaryText() {
        return mSecondaryText;
    }


    RegisterInfoItem(String id, Drawable icon, String primary, String secondary) {
        this.mId = id;
        this.mIcon = icon;
        this.mPrimaryText = primary;
        this.mSecondaryText = secondary;
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     * This will be explained in next Wiki page.
     */
    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof RegisterInfoItem) {
            RegisterInfoItem inItem = (RegisterInfoItem) inObject;
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
        return R.layout.fragment_register_info_item;
    }

    /**
     * The Adapter is provided, because it will become useful for the SaleHistoryItemHolder.
     * The unique instance of the LayoutInflater is also provided to simplify the
     * creation of the VH.
     */
    @Override
    public RegisterInfoItemHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                  ViewGroup parent) {
        return new RegisterInfoItemHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    /**
     * Also here the Adapter is provided to get more specific information from it.
     * NonNull Payload is provided as well, you should use it more often.
     */
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RegisterInfoItemHolder holder, int position,
                               List payloads) {
        holder.mIconView.setImageDrawable(mIcon);
        holder.mPrimaryView.setText(mPrimaryText);
        holder.mSecondaryView.setText(mSecondaryText);
    }

    @Override
    public boolean filter(String constraint) {
        return mSecondaryText.contains(constraint);
    }

    class RegisterInfoItemHolder extends FlexibleViewHolder {

        AppCompatImageView mIconView;
        AppCompatTextView mPrimaryView;
        AppCompatTextView mSecondaryView;

        RegisterInfoItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mIconView = (AppCompatImageView) view.findViewById(R.id.iv_icon);
            mPrimaryView = (AppCompatTextView) view.findViewById(R.id.tv_primary);
            mSecondaryView = (AppCompatTextView) view.findViewById(R.id.tv_secondary);
        }
    }

}
