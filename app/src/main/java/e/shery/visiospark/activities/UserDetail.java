package e.shery.visiospark.activities;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

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

public class UserDetail extends AppCompatActivity {

    SwipeRefreshLayout refresh;
    String name,token,userId,email,uniName;
    TextView textView,list,list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        textView = findViewById(R.id.text);
        list = findViewById(R.id.list);
        list1 = findViewById(R.id.list1);
        refresh = findViewById(R.id.refresh2);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        token = bundle.getString("token");
        userId = bundle.getString("id");
        email = bundle.getString("email");
        uniName = bundle.getString("uniName");

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"Refreshing...", Toast.LENGTH_LONG).show();
                list.setText("");
                list1.setText("");
                email_detail(email);
                refresh.setRefreshing(false);
            }
        });

        textView.setText(uniName);
        email_detail(email);
//        arrayAdapter = new ArrayAdapter<>(UserDetail.this,R.layout.listview4,R.id.listText4,teamDetail);
//        list.setAdapter(arrayAdapter);
    }

    private void email_detail(String emailAddress){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_emailDetail(emailAddress,"application/json","Bearer "+token);

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
                        JSONArray jsonArray1 = jsonObject.getJSONArray("teams");

                        String personName,personEmail,personContact,hostel=null;
                        String m1,m2,m3,m4,m5,eventName;
                        Integer hotel;

                        if (jsonObject.isNull("contactPerson")){
                            personName = "null";
                            personEmail = "null";
                            personContact = "null";
                            hotel = 2;
                        }
                        else {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("contactPerson");
                            personName = jsonObject1.getString("name");
                            personEmail = jsonObject1.getString("email");
                            personContact = jsonObject1.getString("contact");
                            hotel = jsonObject1.getInt("book_hostel");
                        }

                        if (hotel == 0){
                            hostel = "Not Required.!!!";
                        }
                        else if (hotel == 1){
                            hostel = "Required.!!!";
                        }
                        else if (hotel == 2){
                            hostel = "null";
                        }

                        list.append("Contact Person : "+personName+"\nContact : "+personContact+"\nEmail : "+personEmail+"\nHotel : "+hostel);

                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject ev = jsonArray1.getJSONObject(i);

                            eventName = ev.getString("display_name");

                            list1.append("\nCompetition : "+eventName+"\n\n");

                            m1 = ev.getString("mem1");
                            m2 = ev.getString("mem2");
                            m3 = ev.getString("mem3");
                            m4 = ev.getString("mem4");
                            m5 = ev.getString("mem5");

                            if (m1 != "null" && m2 == "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                                list1.append("  Member 1 : "+m1+"\n\n");
                            }
                            else if (m1 != "null" && m2 != "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                                list1.append("  Member 1 : "+m1+"\n"+"  Member 2 : "+m2+"\n\n");
                            }
                            else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 == "null" && m5 == "null"){
                                list1.append("  Member 1 : "+m1+"\n"+"  Member 2 : "+m2+"\n"+"  Member 3 : "+m3+"\n\n");
                            }
                            else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 != "null" && m5 == "null"){
                                list1.append("  Member 1 : "+m1+"\n"+"  Member 2 : "+m2+"\n"+"  Member 3 : "+m3+"\n"+"  Member 4 : "+m4+"\n\n");
                            }
                            else{
                                list1.append(" Member 1 : "+m1+"\n"+"  Member 2 : "+m2+"\n"+"  Member 3 : "+m3+"\n"+"  Member 4 : "+m4+"\n"+"  Member 5 : "+m5+"\n\n");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UserDetail.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
