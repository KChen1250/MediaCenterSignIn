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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.SendMail;
import com.fourfront.mediacentersignin.helper.Student;

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

    private RadioGroup rgroup;
    private Button finish;
    private TextView test;
    private ArrayList<String[]> emails;

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
        rgroup = (RadioGroup) findViewById(R.id.listOfReasons);
        test = ((TextView) findViewById(R.id.details));
        test.setText(getString(R.string.details_dialog, instructor + (substitute ? " (substitute)" : "")));

        rgroup.setOnCheckedChangeListener(m);

        // add purpose RadioButtons (not done yet)
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

    /**
     * Add individual RadioButton to global RadioGroup rgroup
     *
     * @param str   Text to be displayed
     * @param id    ID of RadioButton
     */
    private void addRadioButton(String str, int id) {
        RadioButton rb = new RadioButton(PurposeScreen.this);
        rb.setText(str);
        rb.setId(id);
        rb.setGravity(Gravity.TOP);
        rb.setPadding(20, 0, 0, 10);
        rb.setTextSize(22);
        rb.setTextColor(getResources().getColorStateList(R.color.radio_button_style));
        rgroup.addView(rb);
        rb.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /**
     * Override onOptionsItemSelected, so that data will be passed to SelectTeacher if user
     * presses the up arrow
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
        String subject = "Poolesville Media Center Notification [Please Reply]";
        Timestamp time = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM/dd");
        String message = "This message is to notify you that your student, " + student.getFullName() +
                         ", was sent to the media center at " + sdf.format(time) + ".\n\n" +
                         "Please reply with \"Yes\" or \"No\" to confirm.\n\n\n" +
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
            if (i[0].equals(instructor)) {
                return i[1];
            }
        }
        return null;
    }

    public void sendInfoDone(View view) {
        RadioButton rb = (RadioButton) rgroup.findViewById(rgroup.getCheckedRadioButtonId());
        student.saveToFile(instructor, substitute ? "Yes" : "No", rb.getText().toString());

        // sendEmail(getEmail()); replace with this line on final version
        sendEmail("kchen1250@gmail.com");

        // display Toast and return to initial screen
        Toast.makeText(PurposeScreen.this, "Thank you for signing in.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
