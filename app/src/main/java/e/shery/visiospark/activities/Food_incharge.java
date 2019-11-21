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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class Food_incharge extends AppCompatActivity {

    ProgressBar progressBar;
    SwipeRefreshLayout refresh;
    Boolean doubleBackToExitPressedOnce = false;
    Button b1,b2,logout,passreset;
    TextView t1,t2;
    String name,token,userId;
    RelativeLayout r1,r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_incharge);

        r1 = findViewById(R.id.food_dashboard);
        r2 = findViewById(R.id.food_profile);
        t1 = findViewById(R.id.foodt1);
        t2 = findViewById(R.id.foodt2);
        refresh = findViewById(R.id.refresh5);
        passreset = findViewById(R.id.food_passreset);
        logout = findViewById(R.id.food_logout);
        b1 = findViewById(R.id.foodbtn1);
        b2 = findViewById(R.id.foodbtn2);
        progressBar = findViewById(R.id.foodprogressBar);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        foodStatus();
        progressBar.setVisibility(View.VISIBLE);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                foodStatus();
                refresh.setRefreshing(false);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Food_incharge.this,QrReader.class);
                Bundle user = new Bundle();
                user.putString("name",name);
                user.putString("token",token);
                user.putString("id",userId);
                user.putInt("value",0);
                intent.putExtras(user);
                Food_incharge.this.startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceData.saveEmail(null, Food_incharge.this);
                PreferenceData.savePassword(null, Food_incharge.this);
                PreferenceData.saveName(null, Food_incharge.this);
                PreferenceData.saveTOKEN(null, Food_incharge.this);
                PreferenceData.saveID(null, Food_incharge.this);

                Intent intent = new Intent(Food_incharge.this,MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        passreset.setOnClickListener(new View.OnClickListener() {
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

    private void foodStatus(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .foodCount("application/json","Bearer "+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (s!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int foodprovided = jsonObject.getInt("foodProvided");
                        int foodnotprovided = jsonObject.getInt("foodNotProvided");

                        t1.setText("Food Not Provided To : "+foodnotprovided);
                        t2.setText("Food Provided To : "+foodprovided);
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Food_incharge.this,t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
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
                Toast.makeText(Food_incharge.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
