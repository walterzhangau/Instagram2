package com.example.walterzhang.instagram2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.example.walterzhang.instagram2.Filter.ThumbnailsAdapter;
import com.example.walterzhang.instagram2.Filter.filter;
import com.example.walterzhang.instagram2.R;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import com.zomato.photofilters.utils.ThumbnailsManager;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FiltersListFragment extends Fragment implements ThumbnailsAdapter.ThumbnailsAdapterListener {

    RecyclerView recyclerView;



    ThumbnailsAdapter mAdapter;

    List<ThumbnailItem> thumbnailItemList;

    FiltersListFragmentListener listener;

    public void setListener(FiltersListFragmentListener listener){
        this.listener=listener;
    }


    public FiltersListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_filters_list, container, false);

        ButterKnife.bind(view);
        recyclerView=view.findViewById(R.id.recycler_view);
        thumbnailItemList = new ArrayList<>();
        mAdapter = new ThumbnailsAdapter(getActivity(), thumbnailItemList, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());

        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(mAdapter);

        Bitmap bitmap=getArguments().getParcelable("image");

        prepareThumbnail(bitmap);

        return view;
    }

    /**
     * Renders thumbnails in horizontal list
     * loads default image from Assets if passed param is null
     *
     * @param bitmap
     */

    public void prepareThumbnail(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImage;

                if(bitmap == null){
                    thumbImage = null;
                    Log.d("FiltersFragment", "bitmap is null");
                }
                else{
                    thumbImage= Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }

                if (thumbImage == null)
                    return;

                ThumbnailsManager.clearThumbs();
                thumbnailItemList.clear();

                //add normal bitmap first

                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName = getString(R.string.filter_normal);
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());

                for (Filter filter : filters) {
                    ThumbnailItem tI = new ThumbnailItem();
                    tI.image = thumbImage;
                    tI.filter = filter;
                    tI.filterName = filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }

                thumbnailItemList.addAll(ThumbnailsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        };
        new Thread(r).start();
    }

    @Override
    public void onFilterSelected(Filter filter) {
            if (listener != null){
                listener.onFilterSelected(filter);
            }
    }


    public interface FiltersListFragmentListener {
        void onFilterSelected(Filter filter);
    }
}
