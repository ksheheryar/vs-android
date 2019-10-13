package e.shery.visiospark.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Admin extends AppCompatActivity {

    int count1 = 0;
    boolean doubleBackToExitPressedOnce = false;
    Button b1,b2,b3,b4,b5,b6,b7,passReset,logout;
    TextView regToggleText,onSpotToggleText,onlineHeadText;
    ToggleButton regToggle,onSpotToggle;
    String name,token,userId;
    RelativeLayout r1,r2,r3;
    ArrayList plist;
    ListView l;
    ArrayAdapter<String> arrayAdapter;

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
        onlineHeadText = findViewById(R.id.admin_rp_list_text);
        plist = new ArrayList<String>();
        regToggleText = findViewById(R.id.buser);
        onSpotToggleText = findViewById(R.id.bonspot);
        regToggle = findViewById(R.id.toggle_user);
        onSpotToggle = findViewById(R.id.toggle_onspot);
        passReset = findViewById(R.id.admin_passreset);
        logout = findViewById(R.id.admin_logout);
        r1 = findViewById(R.id.admin_dashboard);
        r2 = findViewById(R.id.admin_profile);
        r3 = findViewById(R.id.admin_onlineParticipant);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        status();
        participantData();
        arrayAdapter = new ArrayAdapter<>(Admin.this,R.layout.listview1,R.id.listText1,plist);
        l.setAdapter(arrayAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.VISIBLE);
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.GONE);
            }
        });

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

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = l.getItemAtPosition(position).toString().trim();
                String[] s1 = s.split(":");

//                email_detail(s1[2].trim());

                Toast.makeText(getApplicationContext(),s1[2].trim(),Toast.LENGTH_LONG).show();
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
                        b3.setText("Finance\n("+pay+")");
                        b4.setText("Concluded Event's\n("+eventConcluded+")");
                        b5.setText("Meal ("+mealObtain+"/"+mealTotal+")");
                        onlineHeadText.setText("Online/Onspot User's  ("+uni_data+")");

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
