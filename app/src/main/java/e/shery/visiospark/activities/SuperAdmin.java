package e.shery.visiospark.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class SuperAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    int count1=0;
    String name,token,userId;
    RelativeLayout r1,r2,r3,r4;
    Button b,logout,notify;
    ListView l;
    ArrayList plist;
    TextView userName,textViewUser,textViewOnspot,udetail,vudetail,t;
    ToggleButton toggleButton_user,toggleButton_onspot;
    PreferenceData data;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;
    Timer timerObj;
    TimerTask timerTaskObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        r1 = findViewById(R.id.rl1);
        r2 = findViewById(R.id.rl2);
        r3 = findViewById(R.id.rl3);
        r4 = findViewById(R.id.rl4);
        b = findViewById(R.id.passreset);
        logout = findViewById(R.id.logout);
        l = findViewById(R.id.rp_list);
        plist = new ArrayList<String>();
        toggleButton_user = findViewById(R.id.toggle_user);
        toggleButton_onspot = findViewById(R.id.toggle_onspot);
        textViewUser = findViewById(R.id.buser);
        textViewOnspot = findViewById(R.id.bonspot);
        udetail = findViewById(R.id.u_detail);
        vudetail = findViewById(R.id.vu_detail);
        t = findViewById(R.id.welcomeText);
        notify = findViewById(R.id.rl3_button);
        data = new PreferenceData();

        t.setVisibility(View.VISIBLE);
        t.setText("Welcome...!!!");
        participantData();
        status();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SuperAdmin.this,R.layout.listview1,R.id.listText1,plist);
        l.setAdapter(arrayAdapter);
        createNotificationChannel();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headview = navigationView.getHeaderView(0);
        userName = headview.findViewById(R.id.user_Name);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        userName.setText(name);

        toggleButton_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewUser.setText("Registration is unlocked");
                    textViewUser.setTextColor(getResources().getColor(R.color.green));
                    set_status("users","false");
                }
                else {
                    textViewUser.setText("Registration is locked");
                    textViewUser.setTextColor(getResources().getColor(R.color.red));
                    set_status("users","true");
                }
            }
        });

        toggleButton_onspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewOnspot.setText("Registration is unlocked");
                    textViewOnspot.setTextColor(getResources().getColor(R.color.green));
                    set_status("onspot","false");
                }
                else {
                    set_status("onspot","true");
                    textViewOnspot.setText("Registration is locked");
                    textViewOnspot.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_reset();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceData.saveEmail(null, SuperAdmin.this);
                PreferenceData.savePassword(null, SuperAdmin.this);
                PreferenceData.saveName(null, SuperAdmin.this);
                PreferenceData.saveTOKEN(null, SuperAdmin.this);
                PreferenceData.saveID(null, SuperAdmin.this);

                if(timerObj != null) {
                    timerObj.cancel();
                    timerObj.purge();
//                    timerObj = null;
                }

                Intent intent = new Intent(SuperAdmin.this,MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        notificationManager = NotificationManagerCompat.from(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher3)
                .setContentTitle("VisioSpark")
                .setContentText("New Registration..!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(VISIBILITY_PUBLIC)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_notification();
            }
        });

        timerObj = new Timer();
        timerTaskObj = new TimerTask() {
            public void run() {
                check_notification();
            }
        };
        timerObj.schedule(timerTaskObj, 0, 5000);

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

    private void check_notification(){
        count1 = 0;
        participantData();

        if (data.getCOUNT(SuperAdmin.this) < count1){
            notificationManager.notify(1, builder.build());
            PreferenceData.saveCOUNT(count1, SuperAdmin.this);
            Toast.makeText(getApplicationContext(),Integer.toString(data.getCOUNT(SuperAdmin.this)),Toast.LENGTH_LONG).show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "VisioSpark";
//            String description = "this channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
//            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void pass_reset(){
        LayoutInflater mDialogView = this.getLayoutInflater();
        View dialog_view = mDialogView.inflate(R.layout.passreset_dialog,null);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
                .setView(dialog_view)
                .setTitle("Change Password");

        mBuilder.setCancelable(false);
        mBuilder.setNegativeButton("Cancel",null);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Clicked.!!!",Toast.LENGTH_LONG).show();
            }
        });
        mBuilder.show();

        //login button click of custom layout
//        mDialogView.dialogLoginBtn.setOnClickListener {
//            //dismiss dialog
//            mAlertDialog.dismiss()
//            //get text from EditTexts of custom layout
//            val name = mDialogView.dialogNameEt.text.toString()
//            val email = mDialogView.dialogEmailEt.text.toString()
//            val password = mDialogView.dialogPasswEt.text.toString()
//            //set the input text in TextView
//            mainInfoTv.setText("Name:"+ name +"\nEmail: "+ email +"\nPassword: "+ password)

    }

    private void participantData(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Registered_participant("application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (s!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        String name1,email;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            name1 = e.getString("name");
                            email = e.getString("email");

                            count1 = count1 + 1;

                            plist.add("Name : "+name1+"\n"+"Email : "+email+"\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void set_status(String type,String value){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_setStatus(type,value,"application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.code() != 200)
                    Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SuperAdmin.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void status(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_status("application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (s!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("registrationForUsers");
                        JSONObject jsonObject2 = jsonObject.getJSONObject("registrationOnSpot");
                        int uni_data = jsonObject.getInt("users");
                        int vuni_data = jsonObject.getInt("vfdUsers");

                        udetail.setText("Registered Universities  :  "+uni_data);
                        vudetail.setText("Verified Universities  :  "+vuni_data);

                        int state_user,state_onspot;

                        state_user = jsonObject1.getInt("value");
                        state_onspot = jsonObject2.getInt("value");

                        if (state_user == 0){
                            toggleButton_user.setChecked(true);
                            textViewUser.setText("Registration is unlocked");
                            textViewUser.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (state_user == 1){
                            toggleButton_user.setChecked(false);
                            textViewUser.setText("Registration is locked");
                            textViewUser.setTextColor(getResources().getColor(R.color.red));
                        }

                        if (state_onspot == 0){
                            toggleButton_onspot.setChecked(true);
                            textViewOnspot.setText("Registration is unlocked");
                            textViewOnspot.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (state_onspot == 1){
                            toggleButton_onspot.setChecked(false);
                            textViewOnspot.setText("Registration is locked");
                            textViewOnspot.setTextColor(getResources().getColor(R.color.red));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
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
            }, 3000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.super_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            PreferenceData.saveEmail(null, SuperAdmin.this);
            PreferenceData.savePassword(null, SuperAdmin.this);
            PreferenceData.saveName(null, SuperAdmin.this);
            PreferenceData.saveTOKEN(null, SuperAdmin.this);
            PreferenceData.saveID(null, SuperAdmin.this);

            Intent intent = new Intent(SuperAdmin.this,MainActivity.class);
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

        if (id == R.id.nav_dashboard) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_r_participant) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_onspotReg) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.VISIBLE);
            r4.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_profile) {
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
