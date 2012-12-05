package com.u2p.ui;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileSelectionActivity extends Activity {

	private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "//A-U2P-files");
    private String mChosenFile;
    private static final String TAG = "FileSelection";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        loadFileList();
        
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, mFileList);
        ListView lv = (ListView)findViewById(R.id.fileSelectionList);
        lv.setAdapter(adapter);
        //Para obtener los seleccionados
        //lv.getCheckedItemIds();
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
            mFileList = mPath.list();
            Log.d(TAG, "File list created: "+mPath.list().toString());
        }
        else {
            mFileList= new String[0];
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
