package e.shery.visiospark.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class FacultyActivity extends AppCompatActivity {

//    private TextView mTextMessage;

    RelativeLayout r1,r2,r3,r4;
    Button b,logout;
    ListView l;
    String name,token;
    ArrayList plist;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        r1 = findViewById(R.id.rl1);
        r2 = findViewById(R.id.rl2);
        r3 = findViewById(R.id.rl3);
        r4 = findViewById(R.id.rl4);
        userName = findViewById(R.id.userName);
        b = findViewById(R.id.passreset);
        logout = findViewById(R.id.logout);
        l = findViewById(R.id.rp_list);
        plist = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userName.setText(name);

        participantData();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_reset();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                finish();
            }
        });

//        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);

//        Menu menu = navigation.getMenu();
//        MenuItem menuItem = menu.getItem(0);
//        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        r1.setVisibility(View.VISIBLE);
                        r2.setVisibility(View.GONE);
                        r3.setVisibility(View.GONE);
                        r4.setVisibility(View.GONE);

                        item.setChecked(true);
                        break;

                    case R.id.navigation_home:
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.VISIBLE);
                        r3.setVisibility(View.GONE);
                        r4.setVisibility(View.GONE);

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(FacultyActivity.this,R.layout.listview,R.id.listText,plist);
                        l.setAdapter(arrayAdapter);

                        item.setChecked(true);
                        break;

                    case R.id.navigation_notifications:
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.GONE);
                        r3.setVisibility(View.VISIBLE);
                        r4.setVisibility(View.GONE);

                        item.setChecked(true);
                        break;

                    case R.id.navigation_profile:
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.GONE);
                        r3.setVisibility(View.GONE);
                        r4.setVisibility(View.VISIBLE);

                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
    }

    public void pass_reset(){
        LayoutInflater mDialogView = this.getLayoutInflater();
        View dialog_view = mDialogView.inflate(R.layout.passreset_dialog,null);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
                .setView(dialog_view)
                .setTitle("Change Password");

        mBuilder.setCancelable(false);
        mBuilder.setNegativeButton("Cancel",null);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Clicked.!!!",Toast.LENGTH_LONG).show();
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

    private void participantData(){

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


                            plist.add("Name : "+name1+"\n"+"Email : "+email);
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
