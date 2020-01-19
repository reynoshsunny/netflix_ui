package com.reynosh.storelisting.activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.reynosh.storelisting.R;
import com.reynosh.storelisting.adapters.StoreGridAdapter;
import com.reynosh.storelisting.models.ContentItem;
import com.reynosh.storelisting.models.Page;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VideoList extends AppCompatActivity {

    GridView mGridView;
    TextView mTitle;
    Toolbar mToolBar;
    int intPageNumber = 1;
    int intPageSize = 0;
    int intTotalContent = 0;
    JSONObject objJson;
    StoreGridAdapter mAdapter;
    String strPageResponse;
    boolean IsFirstPage = true;
    boolean blIsLoadingFinish = false;
    int intBalanceContent = 0;
    List<ContentItem> ListContentItems = new ArrayList<>();
    ContentItem _SinglePoster;
    boolean blSearching = true;
    String strSearchingWord = "";
    String _Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.videolist_activity);

        mToolBar = findViewById(R.id.tbToolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setBackground(getResources().getDrawable(R.drawable.nav_bar));
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp));
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBar.setTitleMarginTop(-50);
        mTitle = findViewById(R.id.txtTitle);
        mTitle.setOnLongClickListener(v -> {
            throw new RuntimeException("This is a crash");
        });
        SearchView search = findViewById(R.id.search);
        search.setQueryHint("Search Video");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    blSearching = true;
                    IsFirstPage = true;
                    blIsLoadingFinish = false;
                    intPageNumber = 1;
                    strSearchingWord = newText;
                    getJsonFileFromLocally(strSearchingWord, true, intPageNumber);
                } else if(newText.length() == 0) {

                    blSearching = false;
                    IsFirstPage = true;
                    blIsLoadingFinish = false;
                    intPageNumber = 1;
                    strSearchingWord = "";
                    getJsonFileFromLocally(strSearchingWord, false, intPageNumber);
                }
                return false;
            }
        });

        search.setOnCloseListener(() -> {
            mTitle.setVisibility(View.VISIBLE);
            return false;
        });

        search.setOnSearchClickListener(view -> mTitle.setVisibility(View.GONE));

        mGridView = findViewById(R.id.gridView);

        if (this.getWindow().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_0) {
            mGridView.setNumColumns(3);
        } else if (this.getWindow().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90) {
            mGridView.setNumColumns(5);
        } else if (this.getWindow().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_180) {
            mGridView.setNumColumns(5);
        } else if (this.getWindow().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270) {
            mGridView.setNumColumns(5);
        }
        getJsonFileFromLocally("", false, intPageNumber);

        mGridView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            Log.d("reynoshscrollX", String.valueOf(scrollX));
            Log.d("reynosholdScrollX", String.valueOf(oldScrollX));
            Log.d("reynosholdscrollY", String.valueOf(scrollY));
            Log.d("reynosholdoldScrollY", String.valueOf(oldScrollY));
            getJsonFileFromLocally(strSearchingWord, blSearching, intPageNumber);
        });

        AppCenter.start(getApplication(), "0afb8475-3f7d-40db-bda4-dcae36fe0e48",
                Analytics.class, Crashes.class);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Surface.ROTATION_0) {
            mGridView.setNumColumns(3);
        } else if (newConfig.orientation == Surface.ROTATION_90) {
            mGridView.setNumColumns(3);
        } else if (newConfig.orientation == Surface.ROTATION_180) {
            mGridView.setNumColumns(5);
        } else if (newConfig.orientation == Surface.ROTATION_270) {
            mGridView.setNumColumns(5);
        }
    }

    public String LoadJsonFile(String strLoadFile) {
        String json = null;
        try {
            InputStream is = VideoList.this.getAssets().open("page" + strLoadFile + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getJsonFileFromLocally(String strSearchWord, Boolean blIsSearching, int intFileNumber) {
        try {
            Gson gson = new Gson();

            if (!blIsLoadingFinish) {

                objJson = new JSONObject(LoadJsonFile(String.valueOf(intFileNumber)));
                strPageResponse = objJson.getString("page");
                Page _new = gson.fromJson(strPageResponse, Page.class);
                //  mToolBar.setTitle(_new.getTitle());
                mTitle.setText(_new.getTitle());

                if (IsFirstPage) {
                    ListContentItems.clear();
                    intTotalContent = Integer.valueOf(_new.getTotalContentItems());
                    intBalanceContent = intTotalContent;
                }
                intPageSize = Integer.valueOf(_new.getPageSize());
                intBalanceContent -= intPageSize;

                if (intBalanceContent <= 0) {
                    blIsLoadingFinish = true;
                } else {
                    intPageNumber++;
                }

                for (int i = 0; i < _new.getContentItems().getContent().size(); i++) {
                    String df = _new.getContentItems().getContent().get(i).getName();

                    if (blIsSearching) {
                        //Works if searching is activated
                        if (df.toLowerCase().contains(strSearchWord.toLowerCase())) {
                            _SinglePoster = _new.getContentItems().getContent().get(i);
                            ListContentItems.add(_SinglePoster);
                        }

                    } else {
                        _SinglePoster = _new.getContentItems().getContent().get(i);
                        ListContentItems.add(_SinglePoster);
                    }
                }

                if (IsFirstPage) {
                    mGridView.setAdapter(null);
                    LoadGridView(ListContentItems);
                    IsFirstPage = false;
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LoadGridView(List<ContentItem> mList) {
        mAdapter = new StoreGridAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
    }

}
