package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.Student;

/**
 * Main activity of sign in application
 *
 * @author Kevin Chen
 *
 * Created May 2017, finished June 2017
 * Poolesville High School Client Project
 */
public class MainActivity extends AppCompatActivity {

    private Button next;
    private EditText id;

    private TextWatcher m = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues(){
        String s = id.getText().toString();

        if (s.length() == 6) {
            next.setEnabled(true);
        } else {
            next.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        next = (Button) findViewById(R.id.next);
        id = (EditText) findViewById(R.id.enterID);
        id.addTextChangedListener(m);

        // prevents the copy / paste dialog from showing up when highlighting text
        id.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {}

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // add a menu with an about button
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Toast toastMessage = Toast.makeText(this, "Created by Kevin Chen, June 2017\nFourFront Technologies Client Project\n\nSource code:\nhttps://github.com/KChen1250/MediaCenterSignIn", Toast.LENGTH_SHORT);
            toastMessage.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextButton(View view) {
        Student student = new Student(id.getText().toString());

        // go to next Activity if student is found in database, show Toast if otherwise
        if (student.isStudent()) {
            Intent intent = new Intent(this, SelectTeacher.class);
            intent.putExtra("STUDENT", student);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Invalid student ID.", Toast.LENGTH_LONG).show();
        }
    }
}
