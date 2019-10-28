package e.shery.visiospark.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Event_head extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    String name,token,userId;
    RelativeLayout r1,r2;
    PreferenceData data;
    Button logout,passwordReset,b1,b2,b3,b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_head2);

        r1 = findViewById(R.id.eventHead_dashBoard);
        r2 = findViewById(R.id.eventHead_profile);
        b1 = findViewById(R.id.eventHead_btn1);
        b2 = findViewById(R.id.eventHead_btn2);
        b3 = findViewById(R.id.eventHead_btn3);
        b4 = findViewById(R.id.eventHead_btn4);
        logout = findViewById(R.id.eventHead_logout);
        passwordReset = findViewById(R.id.eventHead_passreset);
        data = new PreferenceData();

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {

        if (r1.getVisibility() != View.VISIBLE) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
//            r3.setVisibility(View.GONE);
//            r4.setVisibility(View.GONE);
//            r5.setVisibility(View.GONE);
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
}
