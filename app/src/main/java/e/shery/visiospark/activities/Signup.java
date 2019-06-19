package e.shery.visiospark.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Signup extends AppCompatActivity {

    String uniName,dep;

    Spinner s1,s2;
    Button reg;
    TextView t;
    EditText email,pass,pass1;
    ArrayList<String> departments,universities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        s1 = findViewById(R.id.uni);
        s2 = findViewById(R.id.dept);
        reg = findViewById(R.id.reg);
        t = findViewById(R.id.t1);
        email = findViewById(R.id.new_email);
        pass = findViewById(R.id.pass);
        pass1 = findViewById(R.id.pass1);
        departments = new ArrayList<>();
        universities = new ArrayList<>();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString().trim();
                String p = pass.getText().toString().trim();
                String p1 = pass1.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Email is required");
                    pass.setError("Password required");
                    pass1.setError("Password required");
                    email.requestFocus();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }
                else if (p.isEmpty()) {
                    pass.setError("Password required");
                    pass1.setError("Password required");
                    pass.requestFocus();
                    return;
                }
                else if (p.length() < 6) {
                    pass.setError("Password should be atleast 6 character long");
                    pass.requestFocus();
                    return;
                }
                else{
                    if (p.equals(p1)){
                        register();
                    }
                    else {
                        pass.setText("");
                        pass1.setText("");
                        pass.setError("Password not matched");
                        pass1.setError("Password not matched");
                        pass.requestFocus();
                        return;
                    }
                }
            }
        });

        jsonData();

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uniName = String.valueOf(s1.getSelectedItem());
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dep = String.valueOf(s2.getSelectedItem());
//                String s = String.valueOf(position + 1);
//                Toast.makeText(getApplicationContext(),position,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void register(){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .Register(uniName,email.getText().toString().trim(),pass.getText().toString().trim(),pass1.getText().toString().trim(),dep);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    if (response.code() == 200){
                        s = response.body().string();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email Already Registered",Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                }

                if (s!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        JSONObject json1 = jsonObject.getJSONObject("success");
                        JSONObject json2 = json1.getJSONObject("user");
                        String j = json1.getString("token");
                        String name = json2.getString("name");
                        String userId = json2.getString("id");

                        PreferenceData.saveEmail(email.getText().toString().trim(), Signup.this);
                        PreferenceData.savePassword(pass.getText().toString().trim(), Signup.this);
                        PreferenceData.saveName(name, Signup.this);
                        PreferenceData.saveTOKEN(j, Signup.this);
                        PreferenceData.saveID(userId, Signup.this);
                        PreferenceData.saveUSERTYPE("user", Signup.this);

                        Intent intent = new Intent(Signup.this,LoginActivity.class);

                        Bundle user = new Bundle();
                        user.putString("name",name);
                        user.putString("token",j);
                        user.putString("id",userId);
                        intent.putExtras(user);

                        Signup.this.startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                    catch (JSONException e1) {
                        e1.printStackTrace();
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
                .getData();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                }

                if (s!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("departments");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("universities");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject department = jsonArray.getJSONObject(i);
                            String name = department.getString("name");
                            departments.add(name);
                        }
                        for (int j=0;j<jsonArray1.length();j++){
                            JSONObject uni = jsonArray1.getJSONObject(j);
                            String name = uni.getString("name");
                            universities.add(name);
                        }
                        s1.setAdapter(new ArrayAdapter<String>(Signup.this,android.R.layout.simple_spinner_dropdown_item,universities));
                        s2.setAdapter(new ArrayAdapter<String>(Signup.this,android.R.layout.simple_spinner_dropdown_item,departments));
                    }
                    catch (JSONException e) {
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
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
