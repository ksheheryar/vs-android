package e.shery.visiospark.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

    int count1 = 0;
    boolean doubleBackToExitPressedOnce = false;
    Button b1,b2,b3,b4,b5,b6,b7,passReset,logout;
    TextView regToggleText,onSpotToggleText,onlineHeadText,financeHeadText,registerHeadText;
    ToggleButton regToggle,onSpotToggle;
    String name,token,userId;
    RelativeLayout r1,r2,r3,r4,r5;
    ArrayList plist,vplist,teamDetail;
    String paylist[],vuniName[];
    ListView l,l1,l2;
    ArrayAdapter<String> arrayAdapter,a1,a2;
    PreferenceData data;
    PieChartView pieChartView;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;
    Timer timerObj;
    TimerTask timerTaskObj;

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
        registerHeadText = findViewById(R.id.admin_verified_list_text);
        onlineHeadText = findViewById(R.id.admin_rp_list_text);
        financeHeadText = findViewById(R.id.admin_finance_list_text);
        plist = new ArrayList<String>();
        vplist = new ArrayList<String>();
        teamDetail = new ArrayList<String>();
        paylist = new String[100];
        vuniName = new String[100];
        pieChartView = findViewById(R.id.pieChart);
        regToggleText = findViewById(R.id.buser);
        onSpotToggleText = findViewById(R.id.bonspot);
        regToggle = findViewById(R.id.toggle_user);
        onSpotToggle = findViewById(R.id.toggle_onspot);
        passReset = findViewById(R.id.admin_passreset);
        logout = findViewById(R.id.admin_logout);

        r1 = findViewById(R.id.admin_dashboard);
        r2 = findViewById(R.id.admin_profile);
        r3 = findViewById(R.id.admin_onlineParticipant);
        r4 = findViewById(R.id.admin_finance);
        r5 = findViewById(R.id.admin_verifiedData);
        data = new PreferenceData();

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        status();
        financeData();
        participantData();
        VerifiedParticipantData();
        createNotificationChannel();

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(18, getResources().getColor(R.color.green2)).setLabel("Speed Programming : 18"));
        pieData.add(new SliceValue(10, getResources().getColor(R.color.blue2)).setLabel("IT Mushira : 10"));
        pieData.add(new SliceValue(25, getResources().getColor(R.color.green1)).setLabel("Web Designing : 25"));
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
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.GONE);
                r5.setVisibility(View.GONE);
                arrayAdapter = new ArrayAdapter<>(Admin.this,R.layout.listview1,R.id.listText1,plist);
                l.setAdapter(arrayAdapter);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                r4.setVisibility(View.GONE);
                r5.setVisibility(View.VISIBLE);
                a1 = new ArrayAdapter<>(Admin.this,R.layout.listview2,R.id.listText2,vplist);
                l1.setAdapter(a1);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.GONE);
                listview lView= new listview(Admin.this,vuniName,paylist);
                l2.setAdapter(lView);

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

        timerObj = new Timer();
        timerTaskObj = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        check_notification();
                    }
                });
            }
        };
        timerObj.schedule(timerTaskObj, 0, 5000);

        regToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    regToggleText.setText("Registration is unlocked");
                    regToggleText.setTextColor(getResources().getColor(R.color.green));
                    set_status("users","false");
                }
                else {
                    regToggleText.setText("Registration is locked");
                    regToggleText.setTextColor(getResources().getColor(R.color.red));
                    set_status("users","true");
                }
            }
        });

        onSpotToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onSpotToggleText.setText("Registration is unlocked");
                    onSpotToggleText.setTextColor(getResources().getColor(R.color.green));
                    set_status("onspot","false");
                }
                else {
                    set_status("onspot","true");
                    onSpotToggleText.setText("Registration is locked");
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

                if(timerObj != null) {
                    timerObj.cancel();
                    timerObj.purge();
//                    timerObj = null;
                }

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

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = l.getItemAtPosition(position).toString().trim();
                String[] s1 = s.split(":");
                String email,uniName;

                email = s1[3].trim();
                uniName = s1[1].trim();

                Intent intent=new Intent(Admin.this,UserDetail.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putString("email",email);
                user.putString("uniName",uniName);
                intent.putExtras(user);
                Admin.this.startActivity(intent);
            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = l1.getItemAtPosition(position).toString().trim();
                String[] s1 = s.split(":");
                String email,uniName;

                email = s1[3].trim();
                uniName = s1[1].trim();

                Intent intent=new Intent(Admin.this,UserDetail.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putString("email",email);
                user.putString("uniName",uniName);
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
//            Toast.makeText(getApplicationContext(),Integer.toString(data.getCOUNT(SuperAdmin.this)),Toast.LENGTH_LONG).show();
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
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        String name1,email;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            name1 = e.getString("name");
                            email = e.getString("email");

                            count1 = count1 + 1;

                            plist.add(i+1+" : "+name1+"\n"+"  : Email : "+email);
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
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        String name1,email;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            name1 = e.getString("name");
                            email = e.getString("email");

                            vplist.add(i+1+" : "+name1+"\n"+"  : Email : "+email);
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

                        String name1,fee;

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject e = jsonArray.getJSONObject(i);

                            name1 = e.getString("name");
                            fee = e.getString("payment");

                            vuniName[i] = i+1+" : "+name1;
                            paylist[i] = " Rs. "+fee+"/.";


//                            vuniName.add(i+1+name1);
//                            paylist.add(fee);
//                            finance.append(i+1+" : "+name1+"\n     Rs. "+fee+"/.\n\n");

//                            TableRow row = new TableRow(Admin.this);
//                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
//                            row.setLayoutParams(layoutParams);
//                            TextView textView = new TextView(Admin.this);
//                            TextView textView1 = new TextView(Admin.this);
//                            textView.setText(i+1+" : "+name1);
//                            textView1.setText("Rs. "+fee+"/.");
//
//                            textView.setGravity(Gravity.LEFT);
//                            textView.setTextColor(getResources().getColor(R.color.black));
//                            textView.setTypeface(null, Typeface.BOLD);
//                            textView.setTextSize(15);
//                            textView.setPadding(5,0,0,15);
//                            textView1.setGravity(Gravity.RIGHT);
//                            textView1.setTextColor(getResources().getColor(R.color.black));
//                            textView1.setTypeface(null, Typeface.BOLD);
//                            textView1.setTextSize(15);
//                            textView1.setPadding(5,0,0,5);
//
//                            row.addView(textView);
//                            row.addView(textView1);
//
//                            finance.addView(row,i);


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
                Toast.makeText(Admin.this,t.getMessage(), Toast.LENGTH_LONG).show();
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

                        b1.setText("Online/Onspot User's\n("+uni_data+")");
                        b2.setText("Registered User's\n("+vuni_data+")");
                        b3.setText("Finance\n(Rs."+pay+")");
                        b4.setText("Concluded Event's\n("+eventConcluded+")");
                        b5.setText("Meal ("+mealObtain+"/"+mealTotal+")");
                        onlineHeadText.setText("Online/Onspot User's  ("+uni_data+")");
                        financeHeadText.setText("Finance  (Rs."+pay+")");
                        registerHeadText.setText("Registered User's  ("+vuni_data+")");

                        int state_user,state_onspot;

                        state_user = jsonObject1.getInt("value");
                        state_onspot = jsonObject2.getInt("value");

                        if (state_user == 0){
                            regToggle.setChecked(true);
                            regToggleText.setText("Registration is unlocked");
                            regToggleText.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (state_user == 1){
                            regToggle.setChecked(false);
                            regToggleText.setText("Registration is locked");
                            regToggleText.setTextColor(getResources().getColor(R.color.red));
                        }

                        if (state_onspot == 0){
                            onSpotToggle.setChecked(true);
                            onSpotToggleText.setText("Registration is unlocked");
                            onSpotToggleText.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if (state_onspot == 1){
                            onSpotToggle.setChecked(false);
                            onSpotToggleText.setText("Registration is locked");
                            onSpotToggleText.setTextColor(getResources().getColor(R.color.red));
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
}
