package com.example.ale.todolist;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
    private List<String[]> taskList = new ArrayList<String[]>();
    ImageButton imageButton;
    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        imageButton = (ImageButton) rootView.findViewById(R.id.imageButtonAdd);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(getActivity(), NewTask.class);
                startActivity(intent);

            }

        });
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        registerForContextMenu(listView);

        loadTasksInList();

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


    private void createNotification(String id, String name) {
        Log.d("NOTIFICATION", "SSSSSS");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(name)
                        .setContentText("Don't forget today is your last day!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), DetailActivity.class);
        Log.d("ID PASANDO; " , id);
        resultIntent.putExtra(Intent.EXTRA_TEXT,id);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }


    private void loadTasksInList(){
        DBHelper DB = new DBHelper(getActivity().getApplicationContext(), "taskDB", null, 1);

        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Cursor c = DB.allTasks();
        taskList = new ArrayList<>();
        if(!c.moveToFirst()){
            Log.d("NO ELEMENTS", String.valueOf(c.getCount()));

        }
        else{
            Toast toast = Toast.makeText(context, "Elements " + c.getCount() , duration);
            toast.show();
            Log.d("ELEMENT", String.valueOf(c.getCount()));

            int day =Integer.parseInt(c.getString(3));
            int month =Integer.parseInt(c.getString(4));
            int year =Integer.parseInt(c.getString(5));

//        Log.d("TIMESTAMP CHOSEN!!!!!!!!!!", c.getString(6));

            int  daysLeft = remainingDays(day, month, year);
            if (daysLeft == 0) createNotification(c.getString(0), c.getString(1) );
            taskList.add(new String[]{c.getString(0), c.getString(1), "Days left: " +
                    String.valueOf(daysLeft)});
            while(c.moveToNext()){

                day =Integer.parseInt(c.getString(3));
                month =Integer.parseInt(c.getString(4));
                year =Integer.parseInt(c.getString(5));
                daysLeft = remainingDays(day,month,year);
                if (daysLeft == 0) createNotification(c.getString(0), c.getString(1) );

                taskList.add(new String[]{c.getString(0), c.getString(1), "Days left: " +
                        daysLeft});
            }
            c.close();


        }
        }


}











