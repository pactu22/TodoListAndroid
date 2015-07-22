package com.example.ale.todolist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ale.todolist.androidsqlite.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class AllTasks extends ActionBarActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(R.layout.activity_all_tasks);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listTasks);
        // Defined Array values to show in ListView
        DBHelper DB = new DBHelper(getApplicationContext(), "taskDB", null, 1);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        //Cursor c = DB.findTask("prueba");
        Cursor c = DB.allTasks();
        List<String> list = new ArrayList<>();
        c.moveToFirst();
        Toast toast = Toast.makeText(context, "Elements " + c.getCount() +  " c.)" + c.getString(1), duration);
        toast.show();

        while(c.moveToNext()){
            toast = Toast.makeText(context, "asd", duration);
            toast.show();

            list.add(c.getString(1));

        }


        c.close();

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
