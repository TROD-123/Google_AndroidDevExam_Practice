package com.zn.google_android_dev_exam_practice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zn.google_android_dev_exam_practice.service.AsyncTaskJobService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavView;
    @BindView(R.id.btn_delayedJob)
    Button mButtonDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // This is needed to put the title in the app bar
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        // Syncs the state of the toggle with the drawer being opened or closed
        toggle.syncState();
        mNavView.setNavigationItemSelectedListener(this);

        // https://stackoverflow.com/questions/33560219/in-android-how-to-set-navigation-drawer-header-image-and-name-programmatically-i
        View navHeaderView = mNavView.getHeaderView(0);
        ImageView imageView = navHeaderView.findViewById(R.id.nav_header_logo);
        // https://stackoverflow.com/questions/29982341/using-glide-for-android-how-do-i-load-images-from-asset-and-resources
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/web_hi_res_512.png"))
                .into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Since onCreate() is not called again when activity returns from background, we need to
        // update UI from any sp changes here
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int delay = Integer.parseInt(sp.getString("delay_notifications", "0"));
        mButtonDelay.setText(String.format("Give me a notification in %s secs!", Integer.toString(delay)));

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavView)) {
            // Close the drawer if it's opened
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If the drawer is already closed, then go back as usual
            super.onBackPressed();
        }
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

    public void showDelayedNotification(View view) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getPackageName(), AsyncTaskJobService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(100, componentName)
                .setRequiresCharging(true)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuId = menuItem.getItemId();
        switch (menuId) {
            case R.id.nav_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.nav_share:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Share!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
