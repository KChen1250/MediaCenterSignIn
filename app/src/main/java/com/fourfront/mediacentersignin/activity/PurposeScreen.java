package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.SendMail;
import com.fourfront.mediacentersignin.helper.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Purpose selection screen
 *
 * @author Kevin Chen
 *
 * Created May 2017, finished June 2017
 * Poolesville High School Client Project
 */
public class PurposeScreen extends AppCompatActivity {

    private Student student;
    private String instructor;
    private boolean substitute;

    private LinearLayout reasons;
    private Button finish;
    private TextView details;
    private ArrayList<String[]> emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getThemeColor() == 0) {
            setTheme(R.style.BlueAndWhite);
        } else {
            setTheme(R.style.BlackAndGold);
        }
        setContentView(R.layout.activity_purpose_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        // display the up arrow button
        ab.setDisplayHomeAsUpEnabled(true);

        // get data from previous Activity
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("STUDENT");
        instructor = intent.getStringExtra("INSTRUCTOR");
        substitute = intent.getBooleanExtra("SUBSTITUTE", false);
        emails = (ArrayList) intent.getStringArrayListExtra("INFO");

        finish = (Button) findViewById(R.id.finish);
        reasons = (LinearLayout) findViewById(R.id.listofreasons);
        details = (TextView) findViewById(R.id.details);
        details.setText(getString(R.string.details_dialog, instructor + (substitute ? " (substitute)" : "")));

        // add purpose check boxes
        addCheckBoxes();
    }

    /**
     * Programatically add list of purposes to ListView
     */
    private void addCheckBoxes() {
        ArrayList<String> purposes = getPurposes();

        int id = 1;
        for (String i: purposes) {
            addCheckBox(i, id++);
        }
    }

    /**
     * Add individual CheckBox
     *
     * @param str   Text to be displayed
     * @param id    ID of CheckBox
     */
    private void addCheckBox(String str, int id) {
        CheckBox cb = new CheckBox(PurposeScreen.this);
        cb.setText(str);
        cb.setId(id);
        cb.setGravity(Gravity.TOP);
        cb.setPadding(20, 0, 0, 10);
        cb.setTextSize(24);
        if (getThemeColor() == 1) cb.setButtonTintList(ContextCompat.getColorStateList(this, R.color.gray));
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = false;
                for (int i = 0; i < reasons.getChildCount(); i ++) {
                    if (((CheckBox) reasons.getChildAt(i)).isChecked()) {
                        checked = true;
                    }
                }
                finish.setEnabled(checked);
            }
        });
        cb.setTextColor(getResources().getColorStateList(getThemeColor() == 0 ? R.color.radio_button_style : R.color.radio_button_style2));
        reasons.addView(cb);
        cb.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /**
     * Read lines from purposes file
     *
     * @return list of purposes
     */
    private ArrayList<String> getPurposes() {
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOCUMENTS + "/MediaCenterSignIn/purposes.txt";
        ArrayList<String> purposes = new ArrayList<>();

        // read all lines from database
        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {
            for (String line; (line = br.readLine()) != null;) {
                purposes.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return purposes;
    }

    /**
     * Return which theme to apply
     *
     * @return id of theme
     */
    private int getThemeColor() {
        SharedPreferences m = getSharedPreferences("ThemeColor", MODE_PRIVATE);
        int theme = m.getInt("theme", 0);
        return theme;
    }

    /**
     * Override onOptionsItemSelected, so that data will be passed to SelectTeacher
     * if user presses the up arrow
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, SelectTeacher.class);
            intent.putExtra("STUDENT", student);
            startActivity(intent);
        }
        return true;
    }

    /**
     * Send a confirmation email to the speficied teacher / staff
     *
     * @param email address to send to
     */
    public void sendEmail(String email) {
        String subject = "Poolesville Media Center Notification";
        Timestamp time = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM/dd");
        String message = "This message is to notify you that your student " + student.getFullName() +
                         " has arrived at the media center at " + sdf.format(time) + ".\n\n" +
                         "If you did not send the student to the library, please reply \"no\" to this email. " +
                         "No action is needed if you allowed the student to go to the media center.\n\n\n" +
                         "This message was automatically sent from the Poolesville Media Center. " +
                         "If you have a question or problem, please contact the Media Center staff.";

        // create a new SendMail object, which sends the actual email
        SendMail sm = new SendMail(email, subject, message);
        sm.execute();
    }

    /**
     * Returns the email corresponding to the selected teacher / staff
     *
     * @return email corresponding to teacher / staff
     */
    public String getEmail() {
        for (String[] i: emails) {
            if ((i[1] + ", " + i[0]).equals(instructor)) {
                return (i[2]);
            }
        }
        return null;
    }

    public void sendInfoDone(View view) {
        ArrayList<String> reason = new ArrayList<>();
        for (int i = 0; i < reasons.getChildCount(); i ++) {
            if (((CheckBox) reasons.getChildAt(i)).isChecked()) {
                reason.add(((CheckBox) reasons.getChildAt(i)).getText().toString());
            }
        }

        String joined = reason.get(0);
        for (int i = 1; i < reason.size(); i ++) {
            joined += "|" + reason.get(i);
        }

        student.saveToFile(instructor, substitute ? "Yes" : "No", joined);

        String email = getEmail();
        if (email != null) {
            // sendEmail(email);
        }
        // sendEmail("kchen1250@gmail.com"); // change for actual email sending

        // display Toast and return to initial screen
        Toast.makeText(PurposeScreen.this, "Thank you for signing in.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
