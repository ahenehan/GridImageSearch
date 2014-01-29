package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Settings extends Activity {

	private Spinner spImageSize;
	private Spinner spColorFilter;
	private Spinner spImageType;
	private EditText etSiteFilter;
	private SearchParameters searchParameters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
		spImageType = (Spinner) findViewById(R.id.spImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		
		searchParameters = (SearchParameters) getIntent().getSerializableExtra("searchParameters");
		
		if (searchParameters == null) {
			searchParameters = new SearchParameters();
		} 
		setSpinnerToValue(spImageSize, searchParameters.getImageSize());
		setSpinnerToValue(spColorFilter, searchParameters.getColor());
		setSpinnerToValue(spImageType, searchParameters.getImageType());
		etSiteFilter.setText(searchParameters.getSite());
	}

	public void setSpinnerToValue(Spinner spinner, String value) {
	    int index = 0;
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int i = 0; i < adapter.getCount(); i++) {
	    	String item = ((String)adapter.getItem(i)).replace(" ", "");
	        if (item.equalsIgnoreCase(value)) {
	            index = i;
	        }
	    }
	    spinner.setSelection(index);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void onSave(View v) {
		Intent i = new Intent(getApplicationContext(), SearchActivity.class);
		searchParameters.setImageSize(spImageSize.getSelectedItem().toString());
		searchParameters.setColor(spColorFilter.getSelectedItem().toString());
		searchParameters.setImageType(spImageType.getSelectedItem().toString());
		searchParameters.setSite(etSiteFilter.getText().toString());
		
		i.putExtra("searchParameters", searchParameters);
		startActivity(i);
		
	}
	
}
