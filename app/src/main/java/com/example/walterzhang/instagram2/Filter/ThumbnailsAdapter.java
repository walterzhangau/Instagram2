package com.example.walterzhang.instagram2.Filter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walterzhang.instagram2.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.MyViewHolder> {

    private List<ThumbnailItem> thumbnailItemList;
    private ThumbnailsAdapterListener listener;
    private Context mContext;
    private int selectedIndex = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        @BindView(R.id.filter_name)
        TextView filterName;

        public MyViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
        }
    }

    public ThumbnailsAdapter (Context context, List<ThumbnailItem> thumbnailItemList, ThumbnailsAdapterListener listener){
        mContext=context;
        this.thumbnailItemList=thumbnailItemList;
        this.listener=listener;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ThumbnailItem thumbnailItem = thumbnailItemList.get(position);

        holder.thumbnail.setImageBitmap(thumbnailItem.image);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFilterSelected(thumbnailItem.filter);
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });

        holder.filterName.setText(thumbnailItem.filterName);

        if (selectedIndex == position) {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_selected));
        } else {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_normal));
        }
    }

    public int getItemCount() {
        return thumbnailItemList.size();
    }

    public interface ThumbnailsAdapterListener {
        void onFilterSelected(com.zomato.photofilters.imageprocessors.Filter filter);
    }
}


