package com.u2p.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.u2p.ui.component.ItemFile;

public class FileDetailsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        ItemFile item = (ItemFile) getIntent().getSerializableExtra("CALLER_ITEM");
        
        TextView owner = (TextView)findViewById(R.id.fileOwner);
        TextView size = (TextView)findViewById(R.id.fileSize);
        TextView type = (TextView)findViewById(R.id.fileType);
        TextView rating = (TextView)findViewById(R.id.fileRating);
        ImageButton image = (ImageButton)findViewById(R.id.fileImageButton);
        
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
    	Toast.makeText(getApplicationContext(), "Download", Toast.LENGTH_SHORT).show();
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
