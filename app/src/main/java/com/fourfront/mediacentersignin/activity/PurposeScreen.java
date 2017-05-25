package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.Student;

import static com.fourfront.mediacentersignin.R.id.next;

public class PurposeScreen extends AppCompatActivity {

    private Student student;
    private RadioGroup rGroup;
    private Button finish;

    private RadioGroup.OnCheckedChangeListener m = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            finish.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose_screen);

        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("STUDENT");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        finish = (Button) findViewById(R.id.finish);
        rGroup = (RadioGroup) findViewById(R.id.listOfReasons);
        rGroup.setOnCheckedChangeListener(m);

        addRadioButton("Reason 1",  1);
        addRadioButton("Reason 2",  2);
        addRadioButton("Reason 3",  3);
        addRadioButton("Reason 4",  4);
        addRadioButton("Reason 5",  5);
        addRadioButton("Reason 6",  6);
        addRadioButton("Reason 7",  7);
        addRadioButton("Reason 8",  8);
        addRadioButton("Reason 9",  9);
        addRadioButton("Reason 10",  10);
        addRadioButton("Reason 11",  11);
        addRadioButton("Reason 12",  12);
        addRadioButton("Reason 13",  13);
        addRadioButton("Reason 14",  14);
        addRadioButton("Reason 15",  15);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, SelectTeacher.class);
            intent.putExtra("STUDENT", student);
            startActivity(intent);
        }
        return true;
    }

    private void addRadioButton(String str, int id) {
        RadioButton rb = new RadioButton(PurposeScreen.this);
        rb.setText(str);
        rb.setId(id);
        rb.setGravity(Gravity.TOP);
        rb.setPadding(10, 0, 0, 10);
        rb.setTextSize(22);
        rb.setTextColor(getResources().getColorStateList(R.color.radio_button_style));
        rGroup.addView(rb);
    }

    public void sendInfoDone(View view) {
        Toast.makeText(PurposeScreen.this, "Thank you for signing in.", Toast.LENGTH_SHORT).show();
        student.saveToFile("Teacher", "No", "Test reason");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
