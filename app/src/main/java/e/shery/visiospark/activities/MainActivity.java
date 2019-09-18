package e.shery.visiospark.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import e.shery.visiospark.utilities.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    RelativeLayout r1,r2;
    Button signup,credit,login;
    ViewFlipper flip;
    TextView spon;
    EditText email,pass;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            flip.setVisibility(View.VISIBLE);
            spon.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.signup);
        credit = findViewById(R.id.credit);
        login = findViewById(R.id.login);
        flip = findViewById(R.id.flip);
        spon = findViewById(R.id.sponsor);
        r1 = findViewById(R.id.rellay1);
        r2 = findViewById(R.id.rellay2);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        PreferenceData data = new PreferenceData();

        if (data.getEmail(MainActivity.this) != null ){
            if (data.getUSERTYPE(MainActivity.this).equals("superadministrator")){
                Intent intent = new Intent(MainActivity.this, SuperAdmin.class);

                Bundle user = new Bundle();
                user.putString("name",data.getNAME(MainActivity.this));
                user.putString("token",data.getTOKEN(MainActivity.this));
                user.putString("id",data.getID(MainActivity.this));
                intent.putExtras(user);

                startActivity(intent);
                finish();
            }
            else if (data.getUSERTYPE(MainActivity.this).equals("user")){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                Bundle user = new Bundle();
                user.putString("name",data.getNAME(MainActivity.this));
                user.putString("token",data.getTOKEN(MainActivity.this));
                user.putString("id",data.getID(MainActivity.this));
                intent.putExtras(user);

                startActivity(intent);
                finish();
            }
        }

        handler.postDelayed(runnable,2000);

        int images[] = {R.drawable.logo1,R.drawable.logo2,R.drawable.logo3,R.drawable.logo4};

        for (int image: images){
            flipImages(image);
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,Credits.class);
                Intent intent = new Intent(MainActivity.this,EventHead.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e = email.getText().toString().trim();
                final String p = pass.getText().toString().trim();

                if (e.isEmpty()) {
                    email.setError("Email is required");
                    pass.setError("Password required");
                    email.requestFocus();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }
                else if (p.isEmpty()) {
                    pass.setError("Password required");
                    pass.requestFocus();
                    return;
                }
                else if (p.length() < 6) {
                    pass.setError("Password should be atleast 6 character long");
                    pass.requestFocus();
                    return;
                }
                else {
                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .userLogin(e,p);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            String s = null;
                            try {
                                if (response.code() == 200)
                                    s = response.body().string();
                                else{
                                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                            }

                            if (s!=null){
                                try {
                                    JSONObject jsonObject = new JSONObject(s);

                                    JSONObject json1 = jsonObject.getJSONObject("success");
                                    JSONObject json2 = jsonObject.getJSONObject("user");
                                    JSONObject json3 = jsonObject.getJSONObject("role");
                                    String j = json1.getString("token");
                                    String name = json2.getString("name");
                                    String userId = json2.getString("id");

                                    String r_name = json3.getString("name");

                                    email.setText("");
                                    pass.setText("");

                                    PreferenceData.saveEmail(e, MainActivity.this);
                                    PreferenceData.savePassword(p, MainActivity.this);
                                    PreferenceData.saveName(name, MainActivity.this);
                                    PreferenceData.saveTOKEN(j, MainActivity.this);
                                    PreferenceData.saveID(userId, MainActivity.this);
                                    PreferenceData.saveUSERTYPE(r_name, MainActivity.this);

                                    if (r_name.equals("superadministrator")){
                                        Intent intent = new Intent(MainActivity.this,SuperAdmin.class);

                                        Bundle user = new Bundle();
                                        user.putString("name",name);
                                        user.putString("token",j);
                                        user.putString("id",userId);
                                        intent.putExtras(user);

                                        MainActivity.this.startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    }
                                    else if (r_name.equals("user")){
                                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);

                                        Bundle user = new Bundle();
                                        user.putString("name",name);
                                        user.putString("token",j);
                                        user.putString("id",userId);
                                        intent.putExtras(user);

                                        MainActivity.this.startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void flipImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);

        flip.addView(imageView);
        flip.setFlipInterval(2000);
        flip.setAutoStart(true);

        flip.setInAnimation(this,android.R.anim.slide_in_left);
        flip.setOutAnimation(this,android.R.anim.slide_out_right);
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
