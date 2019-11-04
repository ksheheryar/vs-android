package e.shery.visiospark.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class Admin extends AppCompatActivity {

    SwipeRefreshLayout refresh;
    int count1 = 0;
    boolean doubleBackToExitPressedOnce = false,networkCheck;
    Button b1,b2,b3,b4,b5,b6,b7,passReset,logout;
    TextView regToggleText,onSpotToggleText,onlineHeadText,financeHeadText,registerHeadText;
    ToggleButton regToggle,onSpotToggle;
    String name,token,userId;
    RelativeLayout r1,r2,r3,r4,r5,r6;
    ArrayList<String> plist,vplist,paylist,vuniName,CE_e,CE_uni,CE_p,CE_t;
    ListView l2,l3;
    ArrayAdapter<String> arrayAdapter,a2;
    PreferenceData data;
    PieChartView pieChartView;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;
    Timer timerObj;
    TimerTask timerTaskObj;
    ExpandableListView l,l1;
    ExpandableListAdapter adapter,a1;
    HashMap<String, List<String>> hashMap,hashMap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        b1 = findViewById(R.id.btn1);
        b2 = findViewById(R.id.btn2);
        b3 = findViewById(R.id.btn3);
        b4 = findViewById(R.id.btn4);
        b5 = findViewById(R.id.btn5);
        b6 = findViewById(R.id.btn6);
        b7 = findViewById(R.id.btn7);
        l = findViewById(R.id.admin_rp_list);
        l1 = findViewById(R.id.admin_Verified_list);
        l2 = findViewById(R.id.admin_finance_list);
        l3 = findViewById(R.id.admin_concludedEvents_list);
        registerHeadText = findViewById(R.id.admin_verified_list_text);
        onlineHeadText = findViewById(R.id.admin_rp_list_text);
        financeHeadText = findViewById(R.id.admin_finance_list_text);
        pieChartView = findViewById(R.id.pieChart);
        regToggleText = findViewById(R.id.buser);
        onSpotToggleText = findViewById(R.id.bonspot);
        regToggle = findViewById(R.id.toggle_user);
        onSpotToggle = findViewById(R.id.toggle_onspot);
        passReset = findViewById(R.id.admin_passreset);
        logout = findViewById(R.id.admin_logout);
        refresh = findViewById(R.id.refresh3);
//        expandableListView.setGroupIndicator(null);

        r1 = findViewById(R.id.admin_dashboard);
        r2 = findViewById(R.id.admin_profile);
        r3 = findViewById(R.id.admin_onlineParticipant);
        r4 = findViewById(R.id.admin_finance);
        r5 = findViewById(R.id.admin_verifiedData);
        r6 = findViewById(R.id.admin_concludedEvents);
        data = new PreferenceData();

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");

        networkCheck = isNetworkConnected();
        if (networkCheck != true){
            showMessage("VisioSpark","No Internet...!!!\nConnect to Internet & Refresh App");
        }
        else {
            status();
            financeData();
            participantData();
            VerifiedParticipantData();
            concludedEvent();
//            createNotificationChannel();
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"Refreshing...", Toast.LENGTH_LONG).show();

                if (r1.getVisibility() == View.VISIBLE) {
                    status();
                    financeData();
                    participantData();
                    VerifiedParticipantData();
                    concludedEvent();
                }
                else if (r3.getVisibility() == View.VISIBLE) {
                    participantData();
                    adapter = new ExpandableListAdapter(Admin.this, plist, hashMap);
                    l.setAdapter(adapter);
                }
                else if (r4.getVisibility() == View.VISIBLE) {
                    financeData();
                    listview lView= new listview(Admin.this,vuniName,paylist);
                    l2.setAdapter(lView);
                }
                else if (r5.getVisibility() == View.VISIBLE) {
                    VerifiedParticipantData();
                    a1 = new ExpandableListAdapter(Admin.this, vplist, hashMap1);
                    l1.setAdapter(a1);
                }
                else if (r6.getVisibility() == View.VISIBLE) {
                    concludedEvent();
                    listview2 lView= new listview2(Admin.this,CE_e,CE_p,CE_uni,CE_t);
                    l3.setAdapter(lView);
                }
                refresh.setRefreshing(false);
            }
        });

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(18, getResources().getColor(R.color.green2)).setLabel("Speed Programming : 18"));
        pieData.add(new SliceValue(10, getResources().getColor(R.color.blue2)).setLabel("IT Mushira : 10"));
        pieData.add(new SliceValue(35, getResources().getColor(R.color.green1)).setLabel("Web Designing : 35"));
        pieData.add(new SliceValue(20, getResources().getColor(R.color.design_default_color_primary)).setLabel("E Gaming : 20"));
        pieData.add(new SliceValue(5, getResources().getColor(R.color.grey)).setLabel("ROBO Race : 5"));
        pieData.add(new SliceValue(22, getResources().getColor(R.color.blue1)).setLabel("Logo Designing : 22"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(10);
//        pieChartData.setHasCenterCircle(true).setCenterText1("Competition Registration").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#000000"));
        pieChartView.setPieChartData(pieChartData);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ExpandableListAdapter(Admin.this, plist, hashMap);
                l.setAdapter(adapter);
                if (!plist.isEmpty()){
                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    r3.setVisibility(View.VISIBLE);
                    r4.setVisibility(View.GONE);
                    r5.setVisibility(View.GONE);
                    r6.setVisibility(View.GONE);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1 = new ExpandableListAdapter(Admin.this, vplist, hashMap1);
                l1.setAdapter(a1);
                if (!vplist.isEmpty()){
                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    r3.setVisibility(View.GONE);
                    r4.setVisibility(View.GONE);
                    r5.setVisibility(View.VISIBLE);
                    r6.setVisibility(View.GONE);
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview lView= new listview(Admin.this,vuniName,paylist);
                l2.setAdapter(lView);
                if (!vuniName.isEmpty()){
                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    r3.setVisibility(View.GONE);
                    r4.setVisibility(View.VISIBLE);
                    r5.setVisibility(View.GONE);
                    r6.setVisibility(View.GONE);
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview2 lView= new listview2(Admin.this,CE_e,CE_p,CE_uni,CE_t);
                l3.setAdapter(lView);
                if (!CE_e.isEmpty()){
                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    r3.setVisibility(View.GONE);
                    r4.setVisibility(View.GONE);
                    r5.setVisibility(View.GONE);
                    r6.setVisibility(View.VISIBLE);
                }
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,QrReader.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                intent.putExtras(user);
                Admin.this.startActivity(intent);
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.GONE);
                r4.setVisibility(View.GONE);
                r5.setVisibility(View.GONE);
                r6.setVisibility(View.GONE);
            }
        });

//        notificationManager = NotificationManagerCompat.from(this);
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
//                .setSmallIcon(R.mipmap.ic_launcher3)
//                .setContentTitle("VisioSpark")
//                .setContentText("New Registration..!!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setVisibility(VISIBILITY_PUBLIC)
//                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
//
//        timerObj = new Timer();
//        timerTaskObj = new TimerTask() {
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        check_notification();
//                    }
//                });
//            }
//        };
//        timerObj.schedule(timerTaskObj, 0, 5000);

        regToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    regToggleText.setText("Online Registration is Open");
                    regToggleText.setTextColor(getResources().getColor(R.color.green));
                    set_status("users","false");
                }
                else {
                    regToggleText.setText("Online Registration is Closed");
                    regToggleText.setTextColor(getResources().getColor(R.color.red));
                    set_status("users","true");
                }
            }
        });

        onSpotToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onSpotToggleText.setText("OnSpot Registration is Open");
                    onSpotToggleText.setTextColor(getResources().getColor(R.color.green));
                    set_status("onspot","false");
                }
                else {
                    set_status("onspot","true");
                    onSpotToggleText.setText("OnSpot Registration is Closed");
                    onSpotToggleText.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceData.saveEmail(null, Admin.this);
                PreferenceData.savePassword(null, Admin.this);
                PreferenceData.saveName(null, Admin.this);
                PreferenceData.saveTOKEN(null, Admin.this);
                PreferenceData.saveID(null, Admin.this);

//                if(timerObj != null) {
//                    timerObj.cancel();
//                    timerObj.purge();
////                    timerObj = null;
//                }

                Intent intent = new Intent(Admin.this,MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_reset();
            }
        });

        l.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String uniName = plist.get(groupPosition);
                String email = hashMap.get(plist.get(groupPosition)).get(childPosition);
                String[] s2 = email.split(":");
                email = s2[1].trim();
//                Toast.makeText(getApplicationContext(),uniName, Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(Admin.this,UserDetail.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putString("email",email);
                user.putString("uniName",uniName);
                user.putInt("value",0);
                intent.putExtras(user);
                Admin.this.startActivity(intent);

                return false;
            }
        });
        l.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                refresh.setEnabled(firstVisibleItem == 0);
            }
        });

        l1.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String uniName = vplist.get(groupPosition);
                String email = hashMap1.get(vplist.get(groupPosition)).get(childPosition);
                String[] s2 = email.split(":");
                email = s2[1].trim();

                Intent intent=new Intent(Admin.this,UserDetail.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putString("email",email);
                user.putString("uniName",uniName);
                user.putInt("value",0);
                intent.putExtras(user);
                Admin.this.startActivity(intent);

                return false;
            }
        });
        l1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                refresh.setEnabled(firstVisibleItem == 0);
            }
        });

        l2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int topRowVerticalPosition = (l2== null || l2.getChildCount() == 0) ? 0 : l2.getChildAt(0).getTop();
//                refresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                refresh.setEnabled(firstVisibleItem == 0);
            }
        });
        l2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String n = vuniName.get(position).trim();
                String fee = paylist.get(position).trim();

                Intent intent=new Intent(Admin.this,UserDetail.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putString("uniName",n);
                user.putString("total",fee);
                user.putInt("value",1);
                intent.putExtras(user);
                Admin.this.startActivity(intent);
            }
        });
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

    private void check_notification(){
        check_dataChange();

        if (data.getCOUNT(Admin.this) < count1){
            notificationManager.notify(1, builder.build());
            PreferenceData.saveCOUNT(count1, Admin.this);
        }
    }

    private void check_dataChange(){

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

                        count1 = jsonArray.length();

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

        if (r1.getVisibility() != View.VISIBLE) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
            r4.setVisibility(View.GONE);
            r5.setVisibility(View.GONE);
            r6.setVisibility(View.GONE);
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

    private void participantData(){
        count1 = 0;

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
                        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        hashMap = new HashMap<String, List<String>>();
                        plist = new ArrayList<String>();

                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        String name,name1,email;
                        String match[] = new String[jsonArray.length()];
                        boolean matched;
                        int z=0;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            matched = false;
                            name = e.getString("name");

                            count1 = count1 + 1;

                            for (int j=0;j<plist.size();j++){
                                if (name.equals(plist.get(j)))
                                    matched=true;
                            }
                            if (matched == false){
                                plist.add(name);
                                match[z] = name;
                                z++;
                            }
                        }
                        for (int k=0;k<z;k++){
                            ArrayList list1 = new ArrayList();
                            int k2=1;
                            for (int k1=0;k1<jsonArray.length();k1++){
                                JSONObject e = jsonArray.getJSONObject(k1);
                                name1 = e.getString("name");
                                email = e.getString("email");
                                if (name1.equals(match[k])){
                                    list1.add(k2+" : "+email);
                                    k2++;
                                }
                            }
                            list.add(k,list1);
                        }
                        for (int z1=0;z1<z;z1++){
                            hashMap.put(match[z1],list.get(z1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void concludedEvent(){
        count1 = 0;

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_concludedEvenets("application/json","Bearer "+token);

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
                        JSONArray jsonArray = jsonObject.getJSONArray("events");

                        String eventName,name1,position,m1,m2,m3,m4,m5;
                        CE_e = new ArrayList<>();
                        CE_p = new ArrayList<>();
                        CE_uni = new ArrayList<>();
                        CE_t = new ArrayList<>();

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            eventName = e.getString("event");
                            name1 = e.getString("name");
                            position = e.getString("position");
                            m1 = e.getJSONObject("teams").getString("mem1");
                            m2 = e.getJSONObject("teams").getString("mem2");
                            m3 = e.getJSONObject("teams").getString("mem3");
                            m4 = e.getJSONObject("teams").getString("mem4");
                            m5 = e.getJSONObject("teams").getString("mem5");

                            CE_e.add(eventName);
                            CE_p.add("Position : "+position);
                            CE_uni.add("\n"+name1);
                            if (m1 != "null" && m2 == "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                                CE_t.add(m1);
                            }
                            else if (m1 != "null" && m2 != "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                                CE_t.add(m1+"\n"+m2);
                            }
                            else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 == "null" && m5 == "null"){
                                CE_t.add(m1+"\n"+m2+"\n"+m3);
                            }
                            else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 != "null" && m5 == "null"){
                                CE_t.add(m1+"\n"+m2+"\n"+m3+"\n"+m4);
                            }
                            else{
                                CE_t.add(m1+"\n"+m2+"\n"+m3+"\n"+m4+"\n"+m5);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void VerifiedParticipantData(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Verified_participant("application/json","Bearer "+token);

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
                        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        hashMap1 = new HashMap<String, List<String>>();
                        vplist = new ArrayList<String>();

                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        String name,name1,email;
                        String m[] = new String[jsonArray.length()];
                        boolean matched;
                        int z = 0;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            matched = false;
                            name = e.getString("name");

                            count1 = count1 + 1;

                            for (int j=0;j<vplist.size();j++){
                                if (name.equals(vplist.get(j)))
                                    matched=true;
                            }
                            if (matched == false){
                                vplist.add(name);
                                m[z] = name;
                                z++;
                            }
                        }
                        for (int k=0;k<z;k++){
                            ArrayList list1 = new ArrayList();
                            int k2=1;
                            for (int k1=0;k1<jsonArray.length();k1++){
                                JSONObject e = jsonArray.getJSONObject(k1);
                                name1 = e.getString("name");
                                email = e.getString("email");
                                if (name1.equals(m[k])){
                                    list1.add(k2+" : "+email);
                                    k2++;
                                }
                            }
                            list.add(k,list1);
                        }
                        for (int z1=0;z1<z;z1++){
                            hashMap1.put(m[z1],list.get(z1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void financeData(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_finance("application/json","Bearer "+token);

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
                        Boolean m;
                        String name1;
                        paylist = new ArrayList();
                        vuniName = new ArrayList();
                        String[] al = new String[jsonArray.length()];
                        int z=0,fee,f;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            name1 = e.getString("name");
                            m=false;

                            for (int j=0;j<vuniName.size();j++){
                                if (name1.equals(vuniName.get(j)))
                                    m=true;
                            }
                            if (m == false){
                                vuniName.add(name1);
                                al[z] = name1;
                                z++;
                            }
                        }
                        for (int k=0;k<z;k++){
                            f=0;
                            for (int k1=0;k1<jsonArray.length();k1++){
                                JSONObject e = jsonArray.getJSONObject(k1);
                                name1 = e.getString("name");
                                fee = e.getInt("payment");
                                if (name1.equals(al[k])){
                                    f = f + fee;
                                }
                            }
                            paylist.add("Rs. "+f);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
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
                        int pay = jsonObject.getInt("payments");
                        int mealObtain = jsonObject.getInt("foodProvidedCount");
                        int mealTotal = jsonObject.getInt("foodTotalCount");
                        int eventConcluded = jsonObject.getInt("concludedEvents");

                        b1.setText("Online/Onspot Users\n("+uni_data+")");
                        b2.setText("Registered Users\n("+vuni_data+")");
                        b3.setText("Finance\n(Rs."+pay+")");
                        b4.setText("Concluded Events\n("+eventConcluded+")");
                        b5.setText("Meal ("+mealObtain+"/"+mealTotal+")");
                        onlineHeadText.setText("Online/Onspot Users  ("+uni_data+")");
                        financeHeadText.setText("Finance  (Rs."+pay+")");
                        registerHeadText.setText("Registered Users  ("+vuni_data+")");

                        int state_user,state_onspot;

                        state_user = jsonObject1.getInt("value");
                        state_onspot = jsonObject2.getInt("value");

                        if (state_user == 0){
                            regToggle.setChecked(true);
                            regToggleText.setText("Online Registration is Open");
                            regToggleText.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (state_user == 1){
                            regToggle.setChecked(false);
                            regToggleText.setText("Online Registration is Closed");
                            regToggleText.setTextColor(getResources().getColor(R.color.red));
                        }

                        if (state_onspot == 0){
                            onSpotToggle.setChecked(true);
                            onSpotToggleText.setText("OnSpot Registration is Open");
                            onSpotToggleText.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (state_onspot == 1){
                            onSpotToggle.setChecked(false);
                            onSpotToggleText.setText("OnSpot Registration is Closed");
                            onSpotToggleText.setTextColor(getResources().getColor(R.color.red));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin.this,"Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
