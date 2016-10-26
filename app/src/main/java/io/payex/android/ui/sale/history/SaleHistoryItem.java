package io.payex.android.ui.sale.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.payex.android.R;

public class SaleHistoryItem extends AbstractFlexibleItem<SaleHistoryItem.SaleHistoryItemHolder> {

    private String id;
    private String title;

    public String getTitle() {
        return title;
    }

    public SaleHistoryItem(String id, String title) {
        this.id = id;
        this.title = title;
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
            return this.id.equals(inItem.id);
        }
        return false;
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
        holder.mTitle.setText(title);
        //Appear disabled if item is disabled
        holder.mTitle.setEnabled(isEnabled());
    }

    public class SaleHistoryItemHolder extends FlexibleViewHolder {

        public TextView mTitle;

        public SaleHistoryItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mTitle = (TextView) view.findViewById(R.id.content);
        }
    }

}
