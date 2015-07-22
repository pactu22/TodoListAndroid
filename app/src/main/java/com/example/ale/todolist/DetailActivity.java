package com.example.ale.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ale.todolist.androidsqlite.DBHelper;


public class DetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private String idTask;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
            DBHelper DB = new DBHelper(getActivity().getApplicationContext(), "taskDB", null, 1);
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                idTask = intent.getStringExtra(Intent.EXTRA_TEXT);

            }
            Cursor c = DB.findTaskById(idTask);
            c.moveToFirst();


            ((TextView) rootView.findViewById(R.id.task_name))
                    .setText( c.getString(1));
            ((TextView) rootView.findViewById(R.id.task_descr))
                    .setText( c.getString(2));
            String date = c.getString(3) + "-" + c.getString(4) + "-" +c.getString(5);
            ((TextView) rootView.findViewById(R.id.task_date))
                    .setText(date);
                    c.close();

            Button button = (Button) rootView.findViewById(R.id.buttonDelete);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Toast.makeText(getActivity().getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
                    DBHelper DB = new DBHelper(getActivity().getApplicationContext(), "taskDB", null, 1);
                    int s = DB.deleteTaskByName("task1");
                    Toast.makeText(getActivity().getApplicationContext(), "Rows deleted " + s, Toast.LENGTH_LONG).show();
                }
            });


            return rootView;
        }
    }
}
