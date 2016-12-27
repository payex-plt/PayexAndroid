package io.payex.android.ui.sale.voided;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.viewholders.FlexibleViewHolder;
import io.payex.android.R;

/**
 * This is a simple item with custom layout for headers.
 * <p>A Section should not contain others Sections!</p>
 * Headers are not Sectionable!
 */
public class HeaderItem extends AbstractHeaderItem<HeaderItem.HeaderViewHolder> implements IFilterable {

    private static final long serialVersionUID = -7408637077727563374L;

    private String id;
    private String title;
    private String subtitle;

    public HeaderItem(String id) {
        super();
        this.id = id;
        setDraggable(true);
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof HeaderItem) {
            HeaderItem inItem = (HeaderItem) inObject;
            return this.getId().equals(inItem.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.header_item;
    }

    @Override
    public HeaderViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bindViewHolder(FlexibleAdapter adapter, HeaderViewHolder holder, int position, List payloads) {
        if (payloads.size() > 0) {
            Log.i(this.getClass().getSimpleName(), "HeaderItem " + id + " Payload " + payloads);
        } else {
            holder.mTitle.setText(getTitle());
        }
//        List<ISectionable> sectionableList = adapter.getSectionItems(this);
//        String subTitle = (sectionableList.isEmpty() ? "Empty section" :
//                sectionableList.size() + " section items");
//        holder.mSubtitle.setText(subTitle);
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    static class HeaderViewHolder extends FlexibleViewHolder {

        public TextView mTitle;
//        public TextView mSubtitle;
//        public ImageView mHandleView;

        public HeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true);//True for sticky
            mTitle = (TextView) view.findViewById(R.id.title);
//            mTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("HeaderTitle", "Registered internal click on Header TitleTextView! " + mTitle.getText() + " position=" + getFlexibleAdapterPosition());
//                }
//            });
//            this.mHandleView = (ImageView) view.findViewById(R.id.row_handle);
//            if (adapter.isHandleDragEnabled()) {
//                this.mHandleView.setVisibility(View.VISIBLE);
//                setDragHandleView(mHandleView);
//            } else {
//                this.mHandleView.setVisibility(View.GONE);
//            }

            //Support for StaggeredGridLayoutManager
//            if (itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
//                ((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
//            }
        }
    }

    @Override
    public String toString() {
        return "HeaderItem[id=" + id +
                ", title=" + title + "]";
    }

}
