package com.example.ale.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ale.todolist.androidsqlite.DBHelper;


public class NewTask extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
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

        EditText editTitle;
        EditText editInfo;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

            editTitle = (EditText) rootView.findViewById(R.id.editText_title);
            /*
            editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        Toast.makeText(getActivity().getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(getActivity().getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();

                    }
                }
            });*/
            editInfo = (EditText) rootView.findViewById(R.id.editText_info);
            final DatePicker datePicker = (DatePicker)rootView.findViewById(R.id.datePicker);

            Button button = (Button)rootView.findViewById(R.id.buttonOk);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Editable title = editTitle.getText();
                    Editable info = editInfo.getText();
                    String day = String.valueOf(datePicker.getDayOfMonth());
                    String month = String.valueOf(datePicker.getMonth());
                    String year = String.valueOf(datePicker.getYear());
                    /*Toast.makeText(getActivity().getApplicationContext(),
                            title.toString() + "-" + info.toString() + "-"+ day + "-" + month + "-"+
                                    year, Toast.LENGTH_LONG).show();
*/
                    DBHelper DB = new DBHelper(getActivity().getApplicationContext(), "taskDB", null, 1);

                    DB.addTask(title.toString(), info.toString(), day, month, year);

                    Cursor c = DB.findTask(title.toString());

                    c.moveToFirst();

                    Context context = getActivity().getApplicationContext();
                    CharSequence text = c.getString(1);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
              /*
            editInfo = (EditText) rootView.findViewById(R.id.editText_title);
            editInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        Toast.makeText(getActivity().getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                    }
                }
            });*/


            return rootView;
        }
    }
}

/*


        DBHelper DB = new DBHelper(this, "ProductDB", null, 1);
        DB.addProduct("1", "macarrones", "2");

        Cursor c = DB.findProduct("macarrones");

        c.moveToFirst();

        Context context = getApplicationContext();
        CharSequence text = c.getString(1);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


 */