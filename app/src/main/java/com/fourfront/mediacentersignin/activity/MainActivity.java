package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button next;
    private EditText id;
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MediaCenterSignIn";
    FileOutputStream outputStream;

    private TextWatcher m = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
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

        next = (Button) findViewById(R.id.next);
        id = (EditText) findViewById(R.id.enterID);
        id.addTextChangedListener(m);

        id.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    public void nextButton(View view) {
        Student student = new Student(id.getText().toString());
        if (student.isStudent()) {
            Intent intent = new Intent(this, SelectTeacher.class);
            intent.putExtra("STUDENT", student);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Invalid student ID.", Toast.LENGTH_LONG).show();
        }
    }
}
