package com.example.ale.todolist;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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


        Button buttonAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewTask.class);
                    startActivity(intent);
                }
            });


        ListView listView = (ListView) rootView.findViewById(R.id.listView);
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

    private int remainingDays(int day, int month, int year) {
        //TODO bear in mind the months and years
        int chosenDays = day + month*28 + year *365;
        Calendar calendar = Calendar.getInstance();

        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH); // Note: zero based!

        int currentDays = cDay + (cMonth+1)*28 + cYear*365;
        //int cHour = calendar.get(Calendar.HOUR_OF_DAY);


        Log.d("cDay: " , String.valueOf(cDay));
        Log.d("chosen: ", String.valueOf(day));
        return (day - cDay);
    }


}











