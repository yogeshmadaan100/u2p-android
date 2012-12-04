package com.u2p.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.u2p.ui.adapters.ItemFileAdapter;
import com.u2p.ui.component.ItemFile;
import com.u2p.ui.component.LoginDialogFragment;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_section1),
                                //getString(R.string.title_section2),
                                //getString(R.string.title_section3),
                        }),
                this);
                
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayList<ItemFile> items = obtenerItems();
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

    private ArrayList<ItemFile> obtenerItems() {
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
        DialogFragment newFragment = new LoginDialogFragment();
        newFragment.show(getFragmentManager(), "loginDialog");
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
}
