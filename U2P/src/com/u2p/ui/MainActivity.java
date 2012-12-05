package com.u2p.ui;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.u2p.core.db.DbDataSource;
import com.u2p.events.ActivityEventsGenerator;
import com.u2p.events.ServerEventsListener;
import com.u2p.ui.adapters.ItemFileAdapter;
import com.u2p.ui.component.ItemFile;
import com.u2p.ui.component.LoginDialogFragment;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener, 
LoginDialogFragment.LoginDialogListener, ServerEventsListener{
	private DbDataSource datasource;
	private DialogFragment newFragment;
	private ActivityEventsGenerator eventsGenerator;
	private ActionBar actionBar;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String TAG="MainActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsGenerator=new ActivityEventsGenerator();
        // Set up the action bar.
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        //Creamos db
        datasource=new DbDataSource(this);
        try{
        	datasource.open();
        }catch(SQLException e){
        	Log.e(TAG,"SQL Exception opening database");
        }
        
        if(!datasource.usersExist()){
        	loginDialog();
        }
        
        this.refreshGroups();

        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayList<ItemFile> items = getItems();
        ItemFileAdapter adapter = new ItemFileAdapter(this, items);
                
        lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ItemFile item = (ItemFile) parent.getItemAtPosition(position);
				Intent intent = new Intent(getBaseContext(), FileDetailsActivity.class);
				intent.putExtra("CALLER_ITEM", item);
		    	startActivity(intent);
			}
		});
        
        lv.setAdapter(adapter);
    }
    
    private void refreshGroups(){
        List<String> groups=datasource.getAllGroups();
		drawActionBar(groups);
    }
	private void drawActionBar(List<String> groups){
	      String[] groupTitle;
        if(groups.size()>0){
        	groupTitle=new String[groups.size()];
        	groups.toArray(groupTitle);
        }else{
        	groupTitle=new String[1];
        	groupTitle[0]="No groups";
        }
        
        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        groupTitle),
                this);
	}
    private ArrayList<ItemFile> getItems() {
    	ArrayList<ItemFile> items = new ArrayList<ItemFile>();
    	
    	items.add(new ItemFile(1, "drawable/binary", "archivo.bin", "arianjm", "12Kb", "15/20"));
    	items.add(new ItemFile(2, "drawable/doc", "archiv231o.doc", "almartin", "12Kb", "15/20"));
    	items.add(new ItemFile(3, "drawable/file", "weq.unk", "almartin", "45633", "15/20"));
    	items.add(new ItemFile(4, "drawable/txt", "grgw.txt", "arian", "12Kb", "15/20"));
    	
    	items.add(new ItemFile(5, "drawable/image", "archivo.png", "arianjm", "12Kb", "15/20"));
    	items.add(new ItemFile(6, "drawable/script", "archiv231o.sh", "almartin", "12Kb", "15/20"));
    	items.add(new ItemFile(7, "drawable/xls", "weq.xls", "almartin", "45633", "15/20"));
    	items.add(new ItemFile(8, "drawable/box", "grgw.zip", "arian", "12Kb", "15/20"));
    	
    	items.add(new ItemFile(9, "drawable/box", "archivo.rar", "arianjm", "12Kb", "15/20"));
    	items.add(new ItemFile(10, "drawable/xls", "archiv231o.xls", "almartin", "12Kb", "15/20"));
    	items.add(new ItemFile(11, "drawable/pdf", "weq.pdf", "almartin", "45633", "15/20"));
    	items.add(new ItemFile(12, "drawable/source", "grgw.src", "arian", "12Kb", "15/20"));
    	return items;
    }
    
    public void loginDialog(MenuItem item) {
        newFragment = new LoginDialogFragment();
        newFragment.show(getFragmentManager(), "loginDialog");
    }
    
    public void loginDialog() {
        newFragment = new LoginDialogFragment();
        newFragment.show(getFragmentManager(), "loginDialog");
    }
    
    public void addFiles(MenuItem item){
    	Intent intent = new Intent(getBaseContext(), FileSelectionActivity.class);
    	startActivity(intent);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    

    public boolean onNavigationItemSelected(int position, long id) {
        // When the given tab is selected, show the tab contents in the container
    	Fragment fragment = new DummySectionFragment();
    	Bundle args = new Bundle();
    	args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
    	fragment.setArguments(args);
    	getSupportFragmentManager().beginTransaction()
    		.replace(R.id.container, fragment)
    		.commit();
        return true;
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
           // textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return textView;
        }
    }

	public void onLoginPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		EditText userText=(EditText)dialog.getDialog().findViewById(R.id.dialogUsername);
		EditText groupText=(EditText)dialog.getDialog().findViewById(R.id.dialogGroupName);
		EditText passText=(EditText)dialog.getDialog().findViewById(R.id.dialogPassword);
		
		String user,group,pass;
		
		user=userText.getText().toString();
		group=groupText.getText().toString();
		pass=passText.getText().toString();
		
		if(user==null || group==null || pass==null){
			Log.e(TAG,"Null data user, group or pass");
			Toast.makeText(getApplicationContext(), "Data not valid",Toast.LENGTH_SHORT).show();
			this.loginDialog();
		}else{
			if(datasource.addUser(user,group, pass)){
				Log.d(TAG, "New user add "+user+" "+group+" "+pass);
				this.refreshGroups();
			}else{
				Log.e(TAG, "New user error");
				Toast.makeText(getApplicationContext(), "New user error",Toast.LENGTH_SHORT).show();
				this.loginDialog();
			}
		}
	}

	public void onLoginNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		if(!datasource.usersExist()){
			Log.i(TAG, "Cancel create initial group, closing application");
			finish();
		}
		
	}

	public void handleServerEventsListener(EventObject e) {
		// TODO Auto-generated method stub
		
	}
	
	public void launchEventToClients(EventObject event){
		eventsGenerator.addEvent(event);
	}
}
