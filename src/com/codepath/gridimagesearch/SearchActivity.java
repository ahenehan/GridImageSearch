package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;
	private SearchParameters searchParameters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		searchParameters = (SearchParameters) getIntent().getSerializableExtra("searchParameters");
		
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadDataFromApi(totalItemsCount);	
			}
			
		});
		gvResults.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int positon, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(positon);
				//i.putExtra("url", imageResult.getFullUrl());
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
	}

	public void customLoadDataFromApi(int totalItemsCount) {
		String query = etQuery.getText().toString();
		AsyncHttpClient client = new AsyncHttpClient();
		String getMe = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
				"start=" + totalItemsCount + "&v=1.0&q=" + Uri.encode(query);
		if (searchParameters != null) {
			getMe = appendSearchFilters(getMe);
		}
		client.get(getMe,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject(
									"responseData").getJSONArray("results");
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageResults.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}
	
	public String appendSearchFilters(String filterMe) {
		String imageSize = searchParameters.getImageSize();
		String colorFilter = searchParameters.getColor();
		String imageType = searchParameters.getImageType();
		String site = searchParameters.getSite();
		
		if (!imageSize.equalsIgnoreCase("all")) {
			filterMe += "&imgsz=" + imageSize;
		}
		
		if (!colorFilter.equalsIgnoreCase("all")) {
			filterMe += "&imgcolor=" + colorFilter;
		}
		
		if (!imageType.equalsIgnoreCase("all")) {
			filterMe += "&imgtype=" + imageType;
		}
		
		if (!site.equalsIgnoreCase("")) {
			filterMe += "&as_sitesearch=" + site;
		}
		
		return filterMe;
	}
	
	public void onImageSearch(View v) {	
		String query = etQuery.getText().toString();
		if (!query.trim().equalsIgnoreCase("")) {
			Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT).show();
			
			AsyncHttpClient client = new AsyncHttpClient();
			String getMe = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
					"start=" + 0 + "&v=1.0&q=" + Uri.encode(query);
			if (searchParameters != null) {
				getMe = appendSearchFilters(getMe);
			}
	
			Log.d("DEBUG", getMe);
			client.get(getMe,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject response) {
							JSONArray imageJsonResults = null;
							try {
								imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
								imageResults.clear();
								imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
								Log.d("DEBUG", imageResults.toString());
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			Toast.makeText(this, "Cannot search for nothing", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onSettingsAction(MenuItem mi) {
		Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, Settings.class);
		i.putExtra("searchParameters", searchParameters);
		startActivity(i);
	}
	
}
