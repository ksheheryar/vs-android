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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Admin extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    Button b1,b2,b3,b4,b5,b6,b7,passReset,logout;
    ToggleButton regToggle,onSpotToggle;
    String name,token,userId;
    RelativeLayout r1,r2;

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
        passReset = findViewById(R.id.admin_passreset);
        logout = findViewById(R.id.admin_logout);
        r1 = findViewById(R.id.admin_dashboard);
        r2 = findViewById(R.id.admin_profile);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onBackPressed() {

        if (r1.getVisibility() != View.VISIBLE) {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
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
}
