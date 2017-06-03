package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Teacher / Staff selection screen
 *
 * @author Kevin Chen
 *
 * Created May 2017, finished June 2017
 * Poolesville High School Client Project
 */
public class SelectTeacher extends AppCompatActivity {

    private Student student;

    private TextView name;
    private Button next;
    private CheckBox substitute;
    private TabHost tabhost;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioGroup rg3;
    private Spinner sp;

    private int selectedTab;
    private int currentTab;
    private View previousView;
    private View currentView;

    private String selectedInstructor;
    private boolean substituteCheck;
    private ArrayList<String[]> teacherInfo;

    private RadioGroup.OnCheckedChangeListener m1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (tabhost.getCurrentTab() == 0) {
                // clear other tabs and set current tab to bold
                selectedTab = 0;
                rg2.clearCheck();
                rg3.clearCheck();
                t1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                t2.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                t3.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                next.setEnabled(true);
                sp.setEnabled(false);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener m2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (tabhost.getCurrentTab() == 1) {
                selectedTab = 1;
                rg1.clearCheck();
                rg3.clearCheck();
                t1.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                t2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                t3.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                next.setEnabled(true);
                sp.setEnabled(false);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener m3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (tabhost.getCurrentTab() == 2) {
                selectedTab = 2;
                rg1.clearCheck();
                rg2.clearCheck();
                t1.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                t2.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                t3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                next.setEnabled(true);

                // enable the spinner when the last radiobutton is pressed, disable if otherwise
                if (((RadioButton) rg3.findViewById(rg3.getCheckedRadioButtonId())).getText().toString().equals(getResources().getString(R.string.other_instructor_dialog))) {
                    sp.setEnabled(true);
                } else {
                    sp.setEnabled(false);
                }
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

        // display the up arrow button
        ab.setDisplayHomeAsUpEnabled(true);

        // get data from previous Activity
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
        substitute.setPadding(6, 0, 0, 0);
        substitute.setGravity(Gravity.TOP);

        // set welcome text with student name
        name.setText(getString(R.string.welcome_message, student.getFullName()));

        initializeTabs();
        addRadioButtons();

        rg1.setOnCheckedChangeListener(m1);
        rg2.setOnCheckedChangeListener(m2);
        rg3.setOnCheckedChangeListener(m3);

        selectedTab = 0;
        currentTab = 0;
        previousView = tabhost.getCurrentView();

        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorLighterGray));

        // set animations when switching tabs
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                currentView = tabhost.getCurrentView();
                if (tabhost.getCurrentTab() > currentTab) {
                    previousView.setAnimation(outToLeftAnimation());
                    currentView.setAnimation(inFromRightAnimation());
                }
                else {
                    previousView.setAnimation(outToRightAnimation());
                    currentView.setAnimation(inFromLeftAnimation());
                }
                previousView = currentView;
                currentTab = tabhost.getCurrentTab();
                for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
                    tabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorBackground));
                }
                tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorLighterGray));
            }
        });
    }

    /**
     * Format the three tabs and add all RadioButtons
     */
    private void initializeTabs() {
        tabhost.setup();

        TabHost.TabSpec spec = tabhost.newTabSpec("sem1");
        spec.setContent(R.id.sem1);
        spec.setIndicator("Semester 1");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("sem2");
        spec.setContent(R.id.sem2);
        spec.setIndicator("Semester 2");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("other");
        spec.setContent(R.id.other);
        spec.setIndicator("Other");
        tabhost.addTab(spec);

        // get TextViews of each tab so the formatting can be edited
        t1 = (TextView) tabhost.getTabWidget().getChildTabViewAt(0).findViewById(android.R.id.title);
        t2 = (TextView) tabhost.getTabWidget().getChildTabViewAt(1).findViewById(android.R.id.title);
        t3 = (TextView) tabhost.getTabWidget().getChildTabViewAt(2).findViewById(android.R.id.title);

        int size = 20;
        t1.setTextSize(size);
        t1.setGravity(Gravity.CENTER);
        t1.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        t2.setTextSize(size);
        t2.setGravity(Gravity.CENTER);
        t2.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        t3.setTextSize(size);
        t3.setGravity(Gravity.CENTER);
        t3.setTextColor(getResources().getColor(android.R.color.primary_text_light));
    }

    /**
     * Add teacher and other staff RadioButtons and add Spinner for all other teachers
     */
    private void addRadioButtons() {
        ArrayList<String> teachers = student.getTeacherNames();
        ArrayList<String> courses = student.getCourses();
        ArrayList<String> semesters = student.getDurations();
        ArrayList<String> extraStaff = new ArrayList<>();
        String counselor = student.getCounselorName();

        for (int i = 0; i < teachers.size(); i++) {
            if (semesters.get(i).equals("S1")) {
                addRadioButton(teachers.get(i) + "\n" + courses.get(i).toUpperCase(), i, rg1, 10);
            } else {
                addRadioButton(teachers.get(i) + "\n" + courses.get(i).toUpperCase(), i, rg2, 10);
            }
        }

        int id = 1;
        if (!counselor.equals(" ")) {
            addRadioButton(counselor + "\nCOUNSELING", id++, rg3, 10);
        }

        // read extra staff file (like counselor and security) and add RadioButtons
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS + "/MediaCenterSignIn/extra_staff.txt";
        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {
            for (String line; (line = br.readLine()) != null;) {
                extraStaff.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] split;
        for (String i: extraStaff) {
            split = i.substring(1, i.length() - 1).split("\",\"", -1);
            addRadioButton(split[1] + ", " + split[0] + "\n" + split[2].toUpperCase(), id++, rg3, 10);
        }
        addRadioButton(getResources().getString(R.string.other_instructor_dialog), id++, rg3, 0);
        setSpinner();
    }

    /**
     * Add individual RadioButton
     *
     * @param str   Text to be displayed
     * @param id    ID of RadioButton
     * @param rg    RadioGroup that the RadioButton will be put in
     * @param pad   Padding between RadioButtons
     */
    private void addRadioButton(String str, int id, RadioGroup rg, int pad) {
        RadioButton rb = new RadioButton(SelectTeacher.this);
        rb.setText(str);
        rb.setId(id);
        rb.setGravity(Gravity.TOP);
        rb.setPadding(20, 0, 0, pad);
        rb.setTextSize(22);
        rb.setTextColor(getResources().getColorStateList(R.color.radio_button_style));
        rg.addView(rb);
        rb.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /**
     * Set the Spinner containing all the teachers / staff
     */
    private void setSpinner() {
        sp = new Spinner(SelectTeacher.this);
        String[] names = getAllTeachers().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, names);
        sp.setAdapter(adapter);
        sp.setPadding(42, 0, 0, 0);
        sp.setEnabled(false);
        rg3.addView(sp);
    }

    /**
     * Read the list of teachers and their emails and send
     * a list of all teachers / staff to setSpinner
     *
     * @return ArrayList containing all the teachers / staff
     */
    private ArrayList<String> getAllTeachers() {
        teacherInfo = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS + "/MediaCenterSignIn/emails.txt";

        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {
            for (String line; (line = br.readLine()) != null;) {
                teacherInfo.add(line.substring(1, line.length() - 1).split("\",\"", -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String[] line: teacherInfo) {
            names.add(line[1] + ", " + line[0]);
        }

        return names;
    }

    /**
     * Custom animation that animates om from the right
     *
     * @return Animation the Animation object
     */
    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(inFromRight);
    }

    /**
     * Custom animation that animates out to the right
     *
     * @return Animation the Animation object
     */
    private Animation outToRightAnimation() {
        Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(outToRight);
    }

    /**
     * Custom animation that animates in from the left
     *
     * @return Animation the Animation object
     */
    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(inFromLeft);
    }

    /**
     * Custom animation that animates out to the left
     *
     * @return Animation the Animation object
     */
    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        return setProperties(outtoLeft);
    }

    /**
     * Helper method that sets some common properties
     *
     * @param animation the animation to give common properties
     * @return the animation with common properties
     */
    private Animation setProperties(Animation animation) {
        animation.setDuration(280);     // the "sweet spot" of animation times
        animation.setInterpolator(new FastOutSlowInInterpolator());
        return animation;
    }

    public void nextButton(View view) {
        substituteCheck = substitute.isChecked();
        RadioButton rb;

        // get the string of the selected teacher
        switch (selectedTab) {
            case 0:
                rb = (RadioButton) rg1.findViewById(rg1.getCheckedRadioButtonId());
                selectedInstructor = rb.getText().toString().split("\n")[0];
                break;
            case 1:
                rb = (RadioButton) rg2.findViewById(rg2.getCheckedRadioButtonId());
                selectedInstructor = rb.getText().toString().split("\n")[0];
                break;
            default:    // case 2
                rb = (RadioButton) rg3.findViewById(rg3.getCheckedRadioButtonId());
                if (rb.getText().toString().equals(getResources().getString(R.string.other_instructor_dialog))) {
                    selectedInstructor = sp.getSelectedItem().toString();
                } else {
                    selectedInstructor = rb.getText().toString().split("\n")[0];
                }
                break;
        }

        // pass student and click data to next Activity
        Intent intent = new Intent(this, PurposeScreen.class);
        intent.putExtra("STUDENT", student);
        intent.putExtra("INSTRUCTOR", selectedInstructor);
        intent.putExtra("SUBSTITUTE", substituteCheck);
        intent.putExtra("INFO", teacherInfo);
        startActivity(intent);
    }
}
