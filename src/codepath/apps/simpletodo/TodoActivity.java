package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {

	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	private EditText etPriority;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        etPriority = (EditText)findViewById(R.id.etPriority);
        etNewItem=(EditText)findViewById(R.id.etNewItem);
        lvItems=(ListView)findViewById(R.id.lvItems);
        readItems();
        todoAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, todoItems);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
		});
	}

	public void onAddedItem(View v){
    	String itemText = etNewItem.getText().toString();
    	if (!itemText.isEmpty()){
    		int priority = todoItems.size();
        	try{
        		priority = Integer.parseInt(etPriority.getText().toString());
        	} catch (Exception e){
        		System.out.println(e);
        	}
        	if (priority > 0 && priority <= todoItems.size()){
        		todoItems.add(priority-1, itemText);
        	} else {
        		todoItems.add(itemText);
        	}
        	todoAdapter.notifyDataSetChanged();
        	etNewItem.setText("");
        	etPriority.setText("");
        	writeItems();
    	}
    	etNewItem.setText("");
    	etPriority.setText("");
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }
    
    private void readItems(){
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir,"todo.txt");
    	try {
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e){
    		todoItems = new ArrayList<String>();
    	}
    }
    
    private void writeItems(){
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir,"todo.txt");
    	try{
    		FileUtils.writeLines(todoFile, todoItems);
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    }
}
