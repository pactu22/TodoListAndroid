package com.example.ale.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.List;

/**
 * Created by ale on 6/14/15.
 */
public class ListFragment extends Fragment {
    private ArrayAdapter<String[]> adapter;

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
        final List<String[]> taskList = new ArrayList<>();
        c.moveToFirst();
        Toast toast = Toast.makeText(context, "Elements " + c.getCount() , duration);
        toast.show();
        taskList.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
        while(c.moveToNext()){
            taskList.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
        }
        c.close();


            Button button = (Button) rootView.findViewById(R.id.buttonAdd);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Toast.makeText(getActivity().getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
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
}

/*
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // ListView Item Click Listener

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String task = adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, task);
                    startActivity(intent);
                }
            });

*/
//return rootView;










