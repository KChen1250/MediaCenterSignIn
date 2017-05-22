package com.fourfront.mediacentersignin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.fourfront.mediacentersignin.R;
import com.fourfront.mediacentersignin.helper.Student;

public class PurposeScreen extends AppCompatActivity {

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose_screen);

        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("STUDENT");
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
}
