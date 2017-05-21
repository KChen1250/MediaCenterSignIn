package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.Student;

public class SelectTeacher extends AppCompatActivity {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teacher);

        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("STUDENT");

        student.saveToFile("Teacher", "No", "Test reason");

        TextView name = (TextView) findViewById(R.id.nameText);
        name.setText(getString(R.string.welcome_message, student.getFullName()));
    }

    public void nextButton(View view) {
        Intent intent = new Intent(this, PurposeScreen.class);
        intent.putExtra("STUDENT", student);
        startActivity(intent);
    }
}
