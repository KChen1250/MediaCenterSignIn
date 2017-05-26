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
import android.widget.TabHost;
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
    private Button next;
    private CheckBox substitute;
    private TabHost tabhost;
    private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioGroup rg3;

    private String selectedFirst;
    private String selectedLast;

    private RadioGroup.OnCheckedChangeListener m1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (tabhost.getCurrentTab() == 0) {
                rg2.clearCheck();
                rg3.clearCheck();
                next.setEnabled(true);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener m2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (tabhost.getCurrentTab() == 1) {
                rg1.clearCheck();
                rg3.clearCheck();
                next.setEnabled(true);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener m3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (tabhost.getCurrentTab() == 2) {
                rg1.clearCheck();
                rg2.clearCheck();
                next.setEnabled(true);
            }
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
        rg1 = (RadioGroup) findViewById(R.id.rg1);
        rg2 = (RadioGroup) findViewById(R.id.rg2);
        rg3 = (RadioGroup) findViewById(R.id.rg3);
        tabhost = (TabHost) findViewById(R.id.tabhost);

        substitute.setTextColor(getResources().getColorStateList(R.color.check_box_style));
        name.setText(getString(R.string.welcome_message, student.getFullName()));

        initializeTabs();
        addRadioButtons();

        rg1.setOnCheckedChangeListener(m1);
        rg2.setOnCheckedChangeListener(m2);
        rg3.setOnCheckedChangeListener(m3);
    }

    private void initializeTabs() {
        tabhost.setup();
        int size = 24;

        TabHost.TabSpec spec = tabhost.newTabSpec("sem1");
        spec.setContent(R.id.sem1);
        TextView tab1 = new TextView(this);
        tab1.setTextSize(size);
        tab1.setText("Semester 1");
        tab1.setGravity(Gravity.CENTER);
        spec.setIndicator("Semester 1");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("sem2");
        spec.setContent(R.id.sem2);
        TextView tab2 = new TextView(this);
        tab2.setTextSize(size);
        tab2.setText("Semester 2");
        tab2.setGravity(Gravity.CENTER);
        spec.setIndicator("Semester 2");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("other");
        spec.setContent(R.id.other);
        TextView other = new TextView(this);
        other.setTextSize(size);
        other.setText("Other");
        other.setGravity(Gravity.CENTER);
        spec.setIndicator("Other");
        tabhost.addTab(spec);
    }

    private void addRadioButtons() {
        ArrayList<String> teachers = student.getTeacherNames();
        ArrayList<String> courses = student.getCourses();
        ArrayList<String> semesters = student.getDurations();
        ArrayList<String> extraStaff = new ArrayList<>();
        String counselor = student.getCounselorName();

        int id = 1;
        for (int i = 0; i < teachers.size(); i++) {
            if (semesters.get(i).equals("S1")) {
                addRadioButton(teachers.get(i) + "\n" + courses.get(i).toUpperCase(), id++, rg1);
            } else {
                addRadioButton(teachers.get(i) + "\n" + courses.get(i).toUpperCase(), id++, rg2);
            }
        }
        addRadioButton(counselor + "\nCOUNSELING", id++, rg3);

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
            addRadioButton(split[0] + "\n" + split[1].toUpperCase(), id++, rg3);
        }
    }

    private void addRadioButton(String str, int id, RadioGroup rg) {
        RadioButton rb = new RadioButton(SelectTeacher.this);
        rb.setText(str);
        rb.setId(id);
        rb.setGravity(Gravity.TOP);
        rb.setPadding(10, 0, 0, 10);
        rb.setTextSize(22);
        rb.setTextColor(getResources().getColorStateList(R.color.radio_button_style));
        //rGroup.addView(rb);
        rg.addView(rb);
    }

    public void nextButton(View view) {
        Intent intent = new Intent(this, PurposeScreen.class);
        intent.putExtra("STUDENT", student);
        intent.putExtra("SELFIRST", selectedFirst);
        intent.putExtra("SELLAST", selectedLast);
        startActivity(intent);
    }
}
