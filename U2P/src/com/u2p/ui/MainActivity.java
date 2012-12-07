package com.u2p.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.SQLException;
import android.net.nsd.NsdServiceInfo;
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

import com.u2p.core.comm.Client;
import com.u2p.core.comm.Server;
import com.u2p.core.db.DbDataSource;
import com.u2p.core.db.DbFile;
import com.u2p.core.nsd.NsdHelper;
import com.u2p.events.ActivityEventsGenerator;
import com.u2p.events.ChangesClientEvents;
import com.u2p.events.FileEvent;
import com.u2p.events.ListEvent;
import com.u2p.events.NewClientEvent;
import com.u2p.events.SendFileEvent;
import com.u2p.events.SendListEvent;
import com.u2p.events.ServerEventsListener;
import com.u2p.ui.adapters.ItemFileAdapter;
import com.u2p.ui.component.ItemFile;
import com.u2p.ui.component.LoginDialogFragment;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener, 
LoginDialogFragment.LoginDialogListener, ServerEventsListener{
	private DbDataSource datasource;
	//private HashMap<String, String> typeMap = new HashMap<String,String>();
	private DialogFragment newFragment;
	private NsdHelper mNsdHelper;
	private Server server;
	private ActivityEventsGenerator eventsGenerator;
	private ActionBar actionBar;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final int ADD_FILES = 1;
    private static final int DOWNLOAD_FILE = 2;
    private static final String FILES_TO_UPLOAD = "upload";
    private static final String FILE_TO_DOWNLOAD = "download";
    private static final String RATING = "rating";
    private static final String TAG="MainActivity";
    public static final int PORT=2664;
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
        	Log.d(TAG,"Openning database");
        	datasource.open();
        }catch(SQLException e){
        	Log.e(TAG,"SQL Exception opening database");
        }
        
        if(!datasource.usersExist()){
        	Log.e(TAG,"No default user");
        	loginDialog();
        }
        
        this.refreshGroups();
        //Comunicaciones
		mNsdHelper = new NsdHelper(this);
		mNsdHelper.initializeResolveListener();
		Log.d(TAG,"NSD: Initialize Resolve Listener");
		server=new Server(PORT,datasource,this);
		server.start();

    }

    private void drawItems(String group){
		
        ListView lv = (ListView) findViewById(R.id.listView);
        ItemFileAdapter adapter = new ItemFileAdapter(this, getItems(group));
        lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ItemFile item = (ItemFile) parent.getItemAtPosition(position);
				Intent intent = new Intent(getBaseContext(), FileDetailsActivity.class);
				//Añadir el objeto que llama a la activity al intent
				intent.putExtra("CALLER_ITEM", item);
				//Añadir el nombre de la clase al intent
				intent.putExtra("CALLER_CLASS", getIntent().getComponent().getClassName());
		    	startActivityForResult(intent, DOWNLOAD_FILE);
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
        	
        	actionBar.setListNavigationCallbacks(
                    // Specify a SpinnerAdapter to populate the dropdown list.
                    new ArrayAdapter(
                            actionBar.getThemedContext(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            groupTitle),
                    this);
        }
        // Set up the dropdown list navigation in the action bar.
        
	}
	
    private ArrayList<ItemFile> getItems(String group) {
    	Log.d(TAG, "Getting files from group: "+group);
    	ArrayList<ItemFile> items = new ArrayList<ItemFile>();
    	List<DbFile> files = datasource.getFilesGroup(group);
    	
    	for(DbFile file : files){
    		items.add(new ItemFile(file, datasource.getUser(1).getUser(), datasource.getFileType(file.getType())));
    	}
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
    	startActivityForResult(intent, ADD_FILES);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	//Resultado recibido, ver que tipo de resultado es y si se recibió bien
    	if(requestCode == ADD_FILES && resultCode == RESULT_OK){
    		ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra(FILES_TO_UPLOAD);
    		
    		for(File file : files){
    			List<String> groups=datasource.getAllGroups();
    			String group = groups.get(actionBar.getSelectedNavigationIndex());

    			String fileName = file.getName();
    			String uri = file.getAbsolutePath();
    			
    			datasource.addFileToGroup(group, fileName, uri, 0, 0);
    			drawItems(group);
    		}
    		Log.d(TAG, files.toString());
    	}else if(requestCode == DOWNLOAD_FILE && resultCode == RESULT_OK){
    		ItemFile item = (ItemFile) data.getSerializableExtra(FILE_TO_DOWNLOAD);
    		Log.d(TAG, "Download request for: "+item.getName());
    		int rating = data.getIntExtra(RATING, 0);
    		if(rating != 0)
    			Log.d(TAG, "Rating +"+rating);
    		else
    			Log.d(TAG, "File was not rated");
    	}
    	else
    		Log.d(TAG, "Something went wrong. ReqC: "+requestCode+" ResC: "+resultCode);
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
		
		if(e instanceof NewClientEvent){
			NewClientEvent newClient=(NewClientEvent)e;
			//Pedir lista de ficheros al cliente
			Client client=(Client)server.getActiveClient(newClient.getAddress());
			if(client!=null){
				eventsGenerator.addListener(client);
				server.addGroupCommon(newClient.getAddress(),newClient.getCommons());
				/*List<String> groupsCommons=newClient.getCommons();
				for(String str:groupsCommons){
					ListEvent event=new ListEvent(eventsGenerator,newClient.getAddress());
					event.setGroup(str);
					this.launchEventToClients(event);
				}*/
			}
		}
		
	}
	
	public void launchEventToClients(EventObject event){
		eventsGenerator.addEvent(event);
	}
	
	public void discoverService(MenuItem v){
		if(!mNsdHelper.isRegistered()){
			if(!mNsdHelper.isDiscovering())
				mNsdHelper.initializeDiscoveryListener();	
			Toast.makeText(getApplicationContext(), "Discover service...",Toast.LENGTH_SHORT).show();
			mNsdHelper.discoverServices();
		}
		if(mNsdHelper.isConnected()){
			//refrescar contenido
			
		}
	}
	
	public void connectService(MenuItem v){
		Toast.makeText(getApplicationContext(), "Connect to service...",Toast.LENGTH_SHORT).show();
		NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
		
		if(service!=null){
			Client client=new Client(service.getHost(),service.getPort(),datasource,this);
			Toast.makeText(getApplicationContext(), "Connect to "+service.getServiceName()+" on port "+service.getPort(), Toast.LENGTH_SHORT).show();
			client.start();
		}else{
			Toast.makeText(getApplicationContext(), "Service not found", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void registerService(MenuItem v){
		if(!mNsdHelper.isRegistered()){
			if(mNsdHelper.isDiscovering()) mNsdHelper.stopDiscovery();
			mNsdHelper.initializeRegistrationListener();
			Toast.makeText(getApplicationContext(), "Register service...",Toast.LENGTH_SHORT).show();
			mNsdHelper.registerService(PORT);
			server.setService(true);
			return ;
		}
	}
	
	public boolean onNavigationItemSelected(int position, long id) {
		if(actionBar.getNavigationItemCount() > 0){
			List<String> groups=datasource.getAllGroups();
			String group = groups.get(position);
			drawItems(group);
			return true;
		}else
			return false;
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
	
	@Override
	protected void onPause() {
		if (mNsdHelper != null && mNsdHelper.isDiscovering()) {
			mNsdHelper.stopDiscovery();
			if(mNsdHelper.isDiscovering())
				Log.d(TAG,"OnPause(): no stop");
			else
				Log.d(TAG,"OnPause(): stop");
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		server.close();
		Log.d(TAG,"OnDestroy(): close server");
		if(mNsdHelper.isRegistered()){
			mNsdHelper.tearDown();
			Log.d(TAG,"OnDestroy(): tead down");
		}
		if(mNsdHelper.isDiscovering()){
			mNsdHelper.stopDiscovery();
			Log.d(TAG,"OnDestroy(): stop discovey()");
		}
		
		super.onDestroy();
	}
}
