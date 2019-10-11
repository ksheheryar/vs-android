package e.shery.visiospark.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
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

public class EventHead extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    String name,token,userId=null;
    RelativeLayout r1,r2,r3;
    Button b,logout;
    TextView userName,t,ruDetail,vuDetail,detailData;
    PreferenceData data;
    ArrayList<String> winner;
    Spinner first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_head);

        r1 = findViewById(R.id.e_rl1);
        r2 = findViewById(R.id.e_rl3);
        r3 = findViewById(R.id.e_rl4);
        ruDetail = findViewById(R.id.e_u_detail);
        vuDetail = findViewById(R.id.e_vu_detail);
        b = findViewById(R.id.e_passreset);
        logout = findViewById(R.id.e_logout);
        t = findViewById(R.id.e_welcomeText);
        detailData = findViewById(R.id.textdata);
        winner = new ArrayList<>();
        first = findViewById(R.id.fHolder);
        data = new PreferenceData();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headview = navigationView.getHeaderView(0);
        userName = headview.findViewById(R.id.e_user_Name);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        userName.setText(name);

        status();
        t.setVisibility(View.VISIBLE);
        t.setText("Welcome...!!!");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceData.saveEmail(null, EventHead.this);
                PreferenceData.savePassword(null, EventHead.this);
                PreferenceData.saveName(null, EventHead.this);
                PreferenceData.saveTOKEN(null, EventHead.this);
                PreferenceData.saveID(null, EventHead.this);

                Intent intent = new Intent(EventHead.this,MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_reset();
            }
        });

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

    private void status(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .eventHead_data(userId,"application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
//                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
//                    showMessage("VisioSpark",s);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (s!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("event");
                        JSONArray jsonArray = jsonObject.getJSONArray("verifiedUsers");
                        int uni_data = jsonObject.getInt("verifiedUsersCount");
                        int vuni_data = jsonObject.getInt("notVerifiedUsersCount");
                        String eventName = jsonObject1.getString("display_name");

                        ruDetail.setText("Registered Universities  :  "+vuni_data);
                        vuDetail.setText("Verified Universities  :  "+uni_data);

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject member = jsonArray.getJSONObject(i);
                            String name1 = member.getString("mem1");
                            String name2 = member.getString("mem2");
                            String name3 = member.getString("mem3");
                            JSONObject jsonObject2 = member.getJSONObject("user");
                            String nameUni = jsonObject2.getString("name");

                            detailData.append(nameUni+"\n\nMem 1 : "+name1+"\nMem 2 : "+name2+"\nMem 3 : "+name3+"\n\n\n");
                            winner.add(nameUni+"\n\nMem 1 : "+name1+"\nMem 2 : "+name2+"\nMem 3 : "+name3);
                        }
                        first.setAdapter(new ArrayAdapter<String>(EventHead.this,android.R.layout.simple_spinner_dropdown_item,winner));


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

    public void pass_reset(){
        LayoutInflater mDialogView = this.getLayoutInflater();
        View dialog_view = mDialogView.inflate(R.layout.passreset_dialog,null);
        final EditText cpass = dialog_view.findViewById(R.id.c_pass);
        final EditText npass = dialog_view.findViewById(R.id.n_pass);
        final EditText cnpass = dialog_view.findViewById(R.id.cn_pass);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
                .setView(dialog_view)
                .setTitle("Change Password");

        mBuilder.setCancelable(false);
        mBuilder.setNegativeButton("Cancel",null);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String cpassword,npassword,cnfpassword;
                cpassword = cpass.getText().toString().trim();
                npassword = npass.getText().toString().trim();
                cnfpassword = cnpass.getText().toString().trim();

                if (npassword.equals(cnfpassword)){
                    passReset(cpassword,npassword,cnfpassword);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Password Not Matched",Toast.LENGTH_LONG).show();
                }
            }
        });
        mBuilder.show();
    }

    private void passReset(String cp,String np,String cnp){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .pass_reset(userId,cp,np,cnp,"application/json","Bearer "+token);

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
                        String passResult = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(),passResult,Toast.LENGTH_LONG).show();

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

            PreferenceData.saveEmail(null, EventHead.this);
            PreferenceData.savePassword(null, EventHead.this);
            PreferenceData.saveName(null, EventHead.this);
            PreferenceData.saveTOKEN(null, EventHead.this);
            PreferenceData.saveID(null, EventHead.this);

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
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_e_winner) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
        } else if (id == R.id.nav_e_profile) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.VISIBLE);
            t.setVisibility(View.GONE);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK",null);
        builder.show();
    }
}
