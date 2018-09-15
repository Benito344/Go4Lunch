package com.behague.benjamin.go_4_lunch.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Benjamin BEHAGUE on 19/01/2018.
 */

public class ItemClickRecyclerView {

    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private int mItemID;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {

        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
        }
    };

    private ItemClickRecyclerView(RecyclerView recyclerView, int itemID) {
        mRecyclerView = recyclerView;
        mItemID = itemID;
        mRecyclerView.setTag(itemID, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickRecyclerView addTo(RecyclerView view, int itemID) {
        ItemClickRecyclerView support = (ItemClickRecyclerView) view.getTag(itemID);
        if (support == null) {
            support = new ItemClickRecyclerView(view, itemID);
        }
        return support;
    }

    public static ItemClickRecyclerView removeFrom(RecyclerView view, int itemID) {
        ItemClickRecyclerView support = (ItemClickRecyclerView) view.getTag(itemID);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public ItemClickRecyclerView setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(mItemID, null);
    }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

}
