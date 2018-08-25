package com.zn.google_android_dev_exam_practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchEditTextWithClear(View view) {
        startActivity(new Intent(MainActivity.this, EditTextWithClearActivity.class));
    }

    public void launchCustomView(View view) {
        startActivity(new Intent(MainActivity.this, CustomFanControllerActivity.class));
    }

    public void launchTaskActivity(View view) {
        startActivity(new Intent(MainActivity.this, TaskListActivity.class));
    }
}
