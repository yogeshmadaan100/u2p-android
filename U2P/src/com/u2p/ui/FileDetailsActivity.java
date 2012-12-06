package com.u2p.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.u2p.ui.component.ItemFile;

public class FileDetailsActivity extends Activity {

	/* El rating solo funciona si le das a descargar para salir
	 * obviamente esto no es lo que queremos */
	
	private static final String TAG = "FileDetailsActivity";
    private static final String FILE_TO_DOWNLAOD = "download";
    private static final String RATING = "rating";
	private ItemFile item;
	private int rating;
	private Intent result;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        rating = 0;
        result = this.getIntent();
        
        item = (ItemFile) getIntent().getSerializableExtra("CALLER_ITEM");
        
        TextView owner = (TextView)findViewById(R.id.fileOwner);
        TextView size = (TextView)findViewById(R.id.fileSize);
        TextView type = (TextView)findViewById(R.id.fileType);
        TextView rating = (TextView)findViewById(R.id.fileRating);
        ImageButton image = (ImageButton)findViewById(R.id.fileImageButton);
        
        this.setTitle(item.getName());
        
        owner.append(" "+item.getUser());
        size.append(" "+item.getSize());
        type.append(" "+".ALGO");
        rating.append(" "+item.getRating());
        
		int imageResource = getResources().getIdentifier(item.getRutaImagen(), null, getPackageName());
        image.setImageDrawable(getResources().getDrawable(imageResource));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_details, menu);
        return true;
    }

    public void onImageClick(View v){
    	result.putExtra(FILE_TO_DOWNLAOD, item);
    	setResult(Activity.RESULT_OK, result);
    	if(rating!=0)
    		result.putExtra(RATING, rating);
    	Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
    	Log.d(TAG, "Download started");
    	finish();
    }
    
    public void onRateGoodClick(MenuItem item){
    	rating = 1;
    	Toast.makeText(getApplicationContext(), "Rate Good action", Toast.LENGTH_SHORT).show();
    }
    
    public void onRateBadClick(MenuItem item){
    	rating = -1;
    	Toast.makeText(getApplicationContext(), "Rate Bad action", Toast.LENGTH_SHORT).show();
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
