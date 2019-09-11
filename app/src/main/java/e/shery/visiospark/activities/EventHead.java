package e.shery.visiospark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import e.shery.visiospark.R;
import e.shery.visiospark.utilities.PreferenceData;

public class EventHead extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    int count1=0;
    String name,token,userId;
    RelativeLayout r1,r2,r3,r4;
    Button b,logout;
    TextView userName,t;
    PreferenceData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_head);

        r1 = findViewById(R.id.e_rl1);
        r2 = findViewById(R.id.e_rl2);
        r3 = findViewById(R.id.e_rl3);
        r4 = findViewById(R.id.e_rl4);
        b = findViewById(R.id.e_passreset);
        logout = findViewById(R.id.e_logout);
        t = findViewById(R.id.e_welcomeText);
        data = new PreferenceData();

        t.setVisibility(View.VISIBLE);
        t.setText("Welcome...!!!");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PreferenceData.saveEmail(null, SuperAdmin.this);
//                PreferenceData.savePassword(null, SuperAdmin.this);
//                PreferenceData.saveName(null, SuperAdmin.this);
//                PreferenceData.saveTOKEN(null, SuperAdmin.this);
//                PreferenceData.saveID(null, SuperAdmin.this);

                Intent intent = new Intent(EventHead.this,MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headview = navigationView.getHeaderView(0);
        userName = headview.findViewById(R.id.e_user_Name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 3000);;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_head, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

//            PreferenceData.saveEmail(null, SuperAdmin.this);
//            PreferenceData.savePassword(null, SuperAdmin.this);
//            PreferenceData.saveName(null, SuperAdmin.this);
//            PreferenceData.saveTOKEN(null, SuperAdmin.this);
//            PreferenceData.saveID(null, SuperAdmin.this);

            Intent intent = new Intent(EventHead.this,MainActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_e_dashboard) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_e_participant) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_e_winner) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.VISIBLE);
            r4.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_e_profile) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.VISIBLE);
            t.setVisibility(View.GONE);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
