package e.shery.visiospark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout r,r1,r2;
    LinearLayout l1,l2,l3;
    Button b;
    String name,token,Sevent,eventId,userId;
    int mTeam;
    TextView userName,t,t1,details;
    EditText t1m1,t1m2,t1m3,t2m1,t2m2,t3m1;
    ToggleButton toggleButton;
    ListView listView;
    Spinner s,s1;
    ArrayList<String> list,event;
    JSONArray jsonArray;
    String EventArray[][],particpant[][];
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        jsonData();

        particpant = new String[3][3];
        EventArray = new String[15][2];
        t = findViewById(R.id.dat);
        t1 = findViewById(R.id.t_list);
        list = new ArrayList<String>();
        event = new ArrayList<String>();
        details = findViewById(R.id.eventdetail);
        listView = findViewById(R.id.list);
        r = findViewById(R.id.register);
        r1 = findViewById(R.id.r_even);
        r2 = findViewById(R.id.acco);
        toggleButton = findViewById(R.id.toggle_acco);
        s = findViewById(R.id.events);
        s1 = findViewById(R.id.R_event);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        b = findViewById(R.id.Ereg);
        t1m1 = findViewById(R.id.t1m1);
        t1m2 = findViewById(R.id.t1m2);
        t1m3 = findViewById(R.id.t1m3);
        t2m1 = findViewById(R.id.t2m1);
        t2m2 = findViewById(R.id.t2m2);
        t3m1 = findViewById(R.id.t3m1);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headview = navigationView.getHeaderView(0);
        userName = headview.findViewById(R.id.userN);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        userName.setText(name);

        t.setVisibility(View.VISIBLE);
        t.setText("Welcome !!!");
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sevent = String.valueOf(s.getSelectedItem());
                for (int i=0;i<jsonArray.length();i++){
                    try {
                       JSONObject e = jsonArray.getJSONObject(i);
                        String n = e.getString("display_name");
                        int mem = e.getInt("max_members");
                        if (n.equals(Sevent)){
                            mTeam = mem;
                            if (mem == 3){
                                l1.setVisibility(View.VISIBLE);
                                l2.setVisibility(View.GONE);
                                l3.setVisibility(View.GONE);
                                b.setVisibility(View.VISIBLE);
                            }
                            else if (mem == 2){
                                l2.setVisibility(View.VISIBLE);
                                l1.setVisibility(View.GONE);
                                l3.setVisibility(View.GONE);
                                b.setVisibility(View.VISIBLE);
                            }
                            else if (mem == 1){
                                l3.setVisibility(View.VISIBLE);
                                l1.setVisibility(View.GONE);
                                l2.setVisibility(View.GONE);
                                b.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<15;i++){
                    if (Sevent.equals(EventArray[i][0])){
                        eventId = EventArray[i][1];
                        if (mTeam == 3){
                            particpant[0][0] = t1m1.getText().toString().trim();
                            particpant[0][1] = t1m2.getText().toString().trim();
                            particpant[0][2] = t1m3.getText().toString().trim();
                            register();
                            t1m1.setText("");
                            t1m2.setText("");
                            t1m3.setText("");
                        }
                        else if (mTeam == 2){
                            particpant[0][0] = t2m1.getText().toString().trim();
                            particpant[0][1] = t2m2.getText().toString().trim();
                            register1();
                            t2m1.setText("");
                            t2m2.setText("");
                        }
                        else if (mTeam == 1){
                            particpant[0][0] = t3m1.getText().toString().trim();
                            register2();
                            t3m1.setText("");
                        }
                    }
                }
            }
        });

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String S_event = String.valueOf(s1.getSelectedItem());
                for (int i=0;i<15;i++){
                    if (S_event.equals(EventArray[i][0])){
                        String i1 = EventArray[i][1];
                        t1.setText(" ");
                        registered_event(i1);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    textViewUser.setText("Registration is unlocked");
//                    textViewUser.setTextColor(getResources().getColor(R.color.green));
//                    set_status("users","false");
                }
                else {
//                    textViewUser.setText("Registration is locked");
//                    textViewUser.setTextColor(getResources().getColor(R.color.red));
//                    set_status("users","true");
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void registered_event(final String iid){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Registered_event(userId,"application/json","Bearer "+token);

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
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myTeams");

                        String e_id,m1,m2,m3,m4,m5;

                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject ev = jsonArray1.getJSONObject(i);

                            e_id = ev.getString("event_id");

                            if (iid.equals(e_id)){

                                m1 = ev.getString("mem1");
                                m2 = ev.getString("mem2");
                                m3 = ev.getString("mem3");
                                m4 = ev.getString("mem4");
                                m5 = ev.getString("mem5");
                                t1.append("Member 1 : "+m1+"\n"+"  Member 2 : "+m2+"\n"+"  Member 3 : "+m3+"\n"+"  Member 4 : "+m4+"\n"+"  Member 5 : "+m5+"\n\n");
                            }
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

    private void register(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Event_Registeration(eventId,token,userId,particpant[0][0],particpant[0][1],particpant[0][2],"application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String message = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
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

    private void register1(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Event_Registeration1(eventId,token,userId,particpant[0][0],particpant[0][1],"application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String message = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
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

    private void register2(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Event_Registeration2(eventId,token,userId,particpant[0][0],"application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String message = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
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

    private void jsonData(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Event("application/json","Bearer "+token);

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
                        jsonArray = jsonObject.getJSONArray("events");

                        int maxTeam,maxMem,fee;
                        String eid,name1 = null;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            eid = e.getString("id");
                            name1 = e.getString("display_name");
                            maxTeam = e.getInt("max_teams");
                            maxMem = e.getInt("max_members");
                            fee = e.getInt("fees");

                            EventArray[i][0] = name1;
                            EventArray[i][1] = eid;


                            list.add(name1+"\n"+"Max Team : "+maxTeam+"  Max Member : "+maxMem+"\n"+"Fees : "+fee);
                            event.add(name1);
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
        getMenuInflater().inflate(R.menu.login, menu);
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

            PreferenceData.saveEmail(null, LoginActivity.this);
            PreferenceData.savePassword(null, LoginActivity.this);
            PreferenceData.saveName(null, LoginActivity.this);
            PreferenceData.saveTOKEN(null, LoginActivity.this);
            PreferenceData.saveID(null, LoginActivity.this);

            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(intent);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_detail) {
            r.setVisibility(View.GONE);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            t.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LoginActivity.this,R.layout.listview,R.id.listText,list);
            listView.setAdapter(arrayAdapter);
        }
        else if (id == R.id.nav_register) {
            t.setVisibility(View.GONE);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            details.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            r.setVisibility(View.VISIBLE);
            s.setAdapter(new ArrayAdapter<String>(LoginActivity.this,android.R.layout.simple_spinner_dropdown_item,event));
        }
        else if (id == R.id.nav_reg) {
            t.setVisibility(View.GONE);
            r.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            details.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            r1.setVisibility(View.VISIBLE);
            s1.setAdapter(new ArrayAdapter<String>(LoginActivity.this,android.R.layout.simple_spinner_dropdown_item,event));
        }
        else if (id == R.id.nav_accommodation) {
            t.setVisibility(View.GONE);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            r.setVisibility(View.GONE);
        }
        else if (id == R.id.nav_send) {
            r1.setVisibility(View.GONE);
            r.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            details.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            t.setVisibility(View.VISIBLE);
            t.setText(R.string.contact);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
