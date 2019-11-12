package e.shery.visiospark.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class Event_head extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBar;
    SwipeRefreshLayout refresh;
    String name,token,userId;
    RelativeLayout r1,r2,r3;
    TextView t1,t2,t3;
    ListView l;
    PreferenceData data;
    Button logout,passwordReset,b1,b2,b3,b4;
    ArrayList uniName,members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_head2);

        r1 = findViewById(R.id.eventHead_dashBoard);
        r2 = findViewById(R.id.eventHead_profile);
        r3 = findViewById(R.id.eventHead_VerifiedUsers);
        t1 = findViewById(R.id.eventHead_tView);
        t2 = findViewById(R.id.eventHead_tView1);
        t3 = findViewById(R.id.eventHead_VerifiedUsers_text);
        b1 = findViewById(R.id.eventHead_btn1);
        b2 = findViewById(R.id.eventHead_btn2);
        b3 = findViewById(R.id.eventHead_btn3);
        b4 = findViewById(R.id.eventHead_btn4);
        l = findViewById(R.id.eventHead_VerifiedUsers_list);
        refresh = findViewById(R.id.refresh4);
        progressBar = findViewById(R.id.progressBar2);
        logout = findViewById(R.id.eventHead_logout);
        passwordReset = findViewById(R.id.eventHead_passreset);
        data = new PreferenceData();

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        status();
        progressBar.setVisibility(View.VISIBLE);


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"Refreshing...", Toast.LENGTH_LONG).show();

                status();
                listview3 lView= new listview3(Event_head.this,uniName,members);
                l.setAdapter(lView);
                refresh.setRefreshing(false);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview3 lView= new listview3(Event_head.this,uniName,members);
                l.setAdapter(lView);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.VISIBLE);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Event_head.this,QrReader.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putInt("value",1);
                intent.putExtras(user);
                Event_head.this.startActivity(intent);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.GONE);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceData.saveEmail(null, Event_head.this);
                PreferenceData.savePassword(null, Event_head.this);
                PreferenceData.saveName(null, Event_head.this);
                PreferenceData.saveTOKEN(null, Event_head.this);
                PreferenceData.saveID(null, Event_head.this);

                Intent intent = new Intent(Event_head.this,MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_reset();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (r1.getVisibility() != View.VISIBLE) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
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
                Toast.makeText(Event_head.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (s!=null){
                    try {
                        uniName = new ArrayList();
                        members = new ArrayList();
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("event");
                        JSONArray jsonArray = jsonObject.getJSONArray("verifiedUsers");
                        int uni_data = jsonObject.getInt("verifiedUsersCount");
                        int vuni_data = jsonObject.getInt("notVerifiedUsersCount");
                        String eventName = jsonObject1.getString("display_name");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject member = jsonArray.getJSONObject(i);
                            String m1 = member.getString("mem1");
                            String m2 = member.getString("mem2");
                            String m3 = member.getString("mem3");
                            String m4 = member.getString("mem4");
                            String m5 = member.getString("mem5");
                            JSONObject jsonObject2 = member.getJSONObject("user");
                            String nameUni = jsonObject2.getString("name");

                            uniName.add(i+1+". "+nameUni);
                            if (m1 != "null" && m2 == "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                                members.add(m1);
                            }
                            else if (m1 != "null" && m2 != "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                                members.add(m1+"\n"+m2);
                            }
                            else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 == "null" && m5 == "null"){
                                members.add(m1+"\n"+m2+"\n"+m3);
                            }
                            else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 != "null" && m5 == "null"){
                                members.add(m1+"\n"+m2+"\n"+m3+"\n"+m4);
                            }
                            else{
                                members.add(m1+"\n"+m2+"\n"+m3+"\n"+m4+"\n"+m5);
                            }
                        }

                        int onlineReg = jsonObject.getJSONObject("registrationForUsers").getInt("value");
                        int onspotReg = jsonObject.getJSONObject("registrationOnSpot").getInt("value");

                        b1.setText("Registered Teams ("+vuni_data+")");
                        t3.setText("Registered Teams ("+vuni_data+")");

                        if (onlineReg == 0){
                            t1.setText("Online Registration is Open");
                            t1.setTextColor(getResources().getColor(R.color.green));
                            t1.setBackgroundColor(getResources().getColor(R.color.green0));
                        }
                        else if (onlineReg == 1){
                            t1.setText("Online Registration is Closed");
                            t1.setTextColor(getResources().getColor(R.color.red));
                            t1.setBackgroundColor(getResources().getColor(R.color.red0));
                        }

                        if (onspotReg == 0){
                            t2.setText("OnSpot Registration is Open");
                            t2.setTextColor(getResources().getColor(R.color.green));
                            t2.setBackgroundColor(getResources().getColor(R.color.green0));
                        }
                        else if (onspotReg == 1){
                            t2.setText("OnSpot Registration is Closed");
                            t2.setTextColor(getResources().getColor(R.color.red));
                            t2.setBackgroundColor(getResources().getColor(R.color.red0));
                        }
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Event_head.this,"Connection Error", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
