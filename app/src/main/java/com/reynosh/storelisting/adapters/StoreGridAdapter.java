package com.reynosh.storelisting.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reynosh.storelisting.R;
import com.reynosh.storelisting.models.ContentItem;

import java.util.List;

public class StoreGridAdapter extends BaseAdapter {

    private Context mCtx;
    private List<ContentItem> ListContents;
    private LayoutInflater LayInflater;

    public StoreGridAdapter(Context applicationContext, List<ContentItem> ListContents) {
        this.mCtx = applicationContext;
        this.ListContents = ListContents;
        LayInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ListContents.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayInflater.inflate(R.layout.video_single, null);
        TextView txtHeading = view.findViewById(R.id.txtVideoName);
        ImageView imgPoster = view.findViewById(R.id.imgVideoImage);
        Glide.with(mCtx).load(Uri.parse("file:///android_asset/" + ListContents.get(i).getPosterImage() + "")).placeholder(R.drawable.postermiss).into(imgPoster);
        txtHeading.setText(ListContents.get(i).getName());
        return view;
    }
}
