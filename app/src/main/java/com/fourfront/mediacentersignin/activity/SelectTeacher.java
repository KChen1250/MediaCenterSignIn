package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SelectTeacher extends AppCompatActivity {

    private Student student;

    private TextView name;
    private RadioGroup rGroup;
    private Button next;
    private CheckBox substitute;

    private String selectedFirst;
    private String selectedLast;

    private RadioGroup.OnCheckedChangeListener m = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            next.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teacher);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("STUDENT");

        name = (TextView) findViewById(R.id.nameText);
        next = (Button) findViewById(R.id.next);
        substitute = (CheckBox) findViewById(R.id.checkBox);
        substitute.setTextColor(getResources().getColorStateList(R.color.check_box_style));

        name.setText(getString(R.string.welcome_message, student.getFullName()));

        addRadioButtons();
        rGroup.setOnCheckedChangeListener(m);
    }

    private void addRadioButtons() {
        rGroup = (RadioGroup) findViewById(R.id.listOfPeople);
        ArrayList<String> teachers = student.getTeacherNames();
        ArrayList<String> courses = student.getCourses();
        ArrayList<String> semesters = student.getDurations();
        ArrayList<String> extraStaff = new ArrayList<>();
        String counselor = student.getCounselorName();

        int id = 1;
        for (int i = 0; i < teachers.size(); i++) {
            addRadioButton(teachers.get(i) + "\n" + courses.get(i).toUpperCase(), id++);
        }
        addRadioButton(counselor + "\nCOUNSELING", id++);

        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS + "/MediaCenterSignIn/extra_staff.txt";
        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {
            for (String line = null; (line = br.readLine()) != null;) {
                extraStaff.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] split;
        for (String i: extraStaff) {
            split = i.substring(1, i.length() - 1).split("\",\"", -1);
            addRadioButton(split[0] + "\n" + split[1].toUpperCase(), id++);
        }
    }

    private void addRadioButton(String str, int id) {
        RadioButton rb = new RadioButton(SelectTeacher.this);
        rb.setText(str);
        rb.setId(id);
        rb.setGravity(Gravity.TOP);
        rb.setPadding(10, 0, 0, 10);
        rb.setTextSize(22);
        rb.setTextColor(getResources().getColorStateList(R.color.radio_button_style));
        rGroup.addView(rb);
    }

    public void nextButton(View view) {
        Intent intent = new Intent(this, PurposeScreen.class);
        intent.putExtra("STUDENT", student);
        intent.putExtra("SELFIRST", selectedFirst);
        intent.putExtra("SELLAST", selectedLast);
        startActivity(intent);
    }
}
