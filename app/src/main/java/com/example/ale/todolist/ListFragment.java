package com.example.ale.todolist;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ale.todolist.androidsqlite.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ale on 6/14/15.
 */
public class ListFragment extends Fragment {
    private ArrayAdapter<String[]> adapter;
    private List<String[]> taskList;
    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        loadTasksInList();


        Button buttonAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewTask.class);
                    startActivity(intent);
                }
            });


        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        registerForContextMenu(listView);

        adapter = new ArrayAdapter<String[]>(
                getActivity(),
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                taskList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Must always return just a View.
                View view = super.getView(position, convertView, parent);

                // If you look at the android.R.layout.simple_list_item_2 source, you'll see
                // it's a TwoLineListItem with 2 TextViews - text1 and text2.
                //TwoLineListItem listItem = (TwoLineListItem) view;
                String[] entry = taskList.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(entry[1]);
                text2.setText(entry[2]);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String idTask = adapter.getItem(position)[0];
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, idTask);
                startActivity(intent);
            }
        });


        return rootView;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }

            menu.setHeaderTitle("Action:");

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.menu);

       String menuItemName = menuItems[menuItemIndex];
        if(menuItemName.equals("Delete")){

            String[] tasks = taskList.get(info.position);
            Log.d("SIZE", String.valueOf(taskList.size()));
            Log.d("e********e", tasks[0]); //id
            DBHelper DB = new DBHelper(getActivity().getApplicationContext(), "taskDB", null, 1);
            DB.deleteTask(tasks[0]);
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
            return true;
        }
        return false;

    }

    private int remainingDays(int chosenDay, int chosenMonth, int chosenYear) {
        Calendar calendar = Calendar.getInstance();

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Note: zero based!

        int daysLeft = (chosenDay - currentDay) + ((chosenMonth + 1) - currentMonth)*28 +
                (chosenYear - currentYear)*365;

        //int cHour = calendar.get(Calendar.HOUR_OF_DAY);

        //Log.d("currentDay: " , String.valueOf(currentDay));
        //Log.d("currentYear: ", String.valueOf(currentYear));
        //Log.d("currentMonth: " , String.valueOf(currentMonth));
        //Log.d("day: " , String.valueOf(day));
        //Log.d("month: ", String.valueOf(month+1));
        //Log.d("year: ", String.valueOf(year));

        return daysLeft;
    }

    private void loadTasksInList(){
        DBHelper DB = new DBHelper(getActivity().getApplicationContext(), "taskDB", null, 1);

        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        //Cursor c = DB.findTask("prueba");
        Cursor c = DB.allTasks();
        taskList = new ArrayList<>();
        c.moveToFirst();
        Toast toast = Toast.makeText(context, "Elements " + c.getCount() , duration);
        toast.show();

        int day =Integer.parseInt(c.getString(3));
        int month =Integer.parseInt(c.getString(4));
        int year =Integer.parseInt(c.getString(5));

//        Log.d("TIMESTAMP CHOSEN!!!!!!!!!!", c.getString(6));


        taskList.add(new String[]{c.getString(0), c.getString(1), "Days left: " +
                String.valueOf(remainingDays(day,month,year))});
        while(c.moveToNext()){

            day =Integer.parseInt(c.getString(3));
            month =Integer.parseInt(c.getString(4));
            year =Integer.parseInt(c.getString(5));

            taskList.add(new String[]{c.getString(0), c.getString(1), "Days left: " +
                    remainingDays(day,month,year)});
        }
        c.close();
    }


}











