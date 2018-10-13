package com.behague.benjamin.go_4_lunch.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.Places.Result;
import com.bumptech.glide.RequestManager;

import java.util.List;

/**
 * Created by Benjamin BEHAGUE on 14/08/2018.
 */
public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private RequestManager glide;
    private List<Result> result;
    private Context context;

    public ListAdapter(List<Result> mResult, RequestManager glide){
        result = mResult;
        this.glide = glide;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recycler_views_item, parent, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, int position){
        viewHolder.updateDatas(result.get(position), glide, context);
    }

    @Override
    public int getItemCount(){
        return result.size();
    }

    public void setData(List<Result> listResult) {
        result.clear();
        result.addAll(listResult);
    }
}
