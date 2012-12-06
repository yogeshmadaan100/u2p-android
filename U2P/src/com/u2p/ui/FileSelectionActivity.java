package com.u2p.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileSelectionActivity extends Activity {

	private File[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "//A-U2P-files");
    private static final String TAG = "FileSelection";
    private static final String FILES_TO_UPLOAD = "upload";
    private ArrayList<File> resultFileList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        loadFileList();
        
        ArrayAdapter adapter = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_multiple_choice, mFileList);
        ListView lv = (ListView)findViewById(R.id.fileSelectionList);
        lv.setAdapter(adapter);        
    }
    
    public void onUploadClick(MenuItem item){
    	Log.d(TAG, "Upload clicked, finishing activity");
        
    	ListView lv = (ListView)findViewById(R.id.fileSelectionList);
        resultFileList = new ArrayList<File>();
        
        for(int i = 0 ; i < lv.getCount() ; i++){
        	if(lv.isItemChecked(i)){
        		resultFileList.add((File)lv.getItemAtPosition(i));
        	}
        }
        Log.d(TAG, "Files: "+resultFileList.toString());
        Intent result = this.getIntent();
        result.putExtra(FILES_TO_UPLOAD, resultFileList);
        setResult(Activity.RESULT_OK, result);
    	finish();
    }

    private void loadFileList() {
        try {
        	if(mPath.exists())
        		Log.d(TAG, "Dir exists");
        	else{
        		if(mPath.mkdirs())
        			Log.d(TAG, "Dir created");
        		else
        			Log.d(TAG, "Dir not created");
        	}
        }
        catch(SecurityException e) {
            Log.e(TAG, "unable to write on the sd card " + e.toString());
        }
        if(mPath.exists()) {
        	mFileList = mPath.listFiles();
            Log.d(TAG, "File list created");
        }
        else {
            Log.e(TAG, "File list not created");
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_selection, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
