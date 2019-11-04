package e.shery.visiospark.activities;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    RelativeLayout r1,r2;
    SwipeRefreshLayout refresh;
    String name,token,userId,email,uniName,total;
    TextView textView,list,list1,t1;
    ArrayList<String> event,team,pay;
    ListView l;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        textView = findViewById(R.id.text);
        list = findViewById(R.id.list);
        list1 = findViewById(R.id.list1);
        refresh = findViewById(R.id.refresh2);
        r1 = findViewById(R.id.userDetail_r1);
        r2 = findViewById(R.id.userDetail_r2);
        l = findViewById(R.id.userFinanceDetail_list);
        t1 = findViewById(R.id.userDetail_total);

        Bundle bundle = getIntent().getExtras();
        value = bundle.getInt("value");
        if (value == 0){
            name = bundle.getString("name");
            token = bundle.getString("token");
            userId = bundle.getString("id");
            email = bundle.getString("email");
            uniName = bundle.getString("uniName");

            email_detail(email);
            r1.setVisibility(View.VISIBLE);

        }
        else if (value == 1){
            name = bundle.getString("name");
            token = bundle.getString("token");
            userId = bundle.getString("id");
            uniName = bundle.getString("uniName");
            total = bundle.getString("total");

            r2.setVisibility(View.VISIBLE);
            finance_detail(uniName);
        }

        textView.setText(uniName);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"Refreshing...", Toast.LENGTH_LONG).show();
                if (value == 0){
                    list.setText("");
                    list1.setText("");
                    email_detail(email);
                }
                else{
                    finance_detail(uniName);
                }
                refresh.setRefreshing(false);
            }
        });

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
                Toast.makeText(UserDetail.this,"Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void finance_detail(String universityName){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_UserFinanceDetail(universityName,"application/json","Bearer "+token);

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
                        JSONArray jsonArray1 = jsonObject.getJSONArray("userTeams");

                        event = new ArrayList<String>();
                        team = new ArrayList<String>();
                        pay = new ArrayList<String>();
                        String[] al = new String[jsonArray1.length()];

                        Boolean m;
                        String eventName;
                        int fee,totalTeams;
                        int z=0,f,t;

                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject ev = jsonArray1.getJSONObject(i);

                            eventName = ev.getString("name");

                            m=false;

                            for (int j=0;j<event.size();j++){
                                if (eventName.equals(event.get(j)))
                                    m=true;
                            }
                            if (m == false){
                                event.add(eventName);
                                al[z] = eventName;
                                z++;
                            }
                        }
                        for (int k=0;k<z;k++){
                            f=0;
                            t=0;
                            for (int k1=0;k1<jsonArray1.length();k1++){
                                JSONObject e = jsonArray1.getJSONObject(k1);
                                eventName = e.getString("name");
                                totalTeams = e.getInt("teams");
                                fee = e.getInt("fee");
                                if (eventName.equals(al[k])){
                                    f = f + fee;
                                    t = t + totalTeams;
                                }
                            }
                            team.add("Total Teams : "+t);
                            pay.add("Fees : "+f);
                        }
                        listview1 lView= new listview1(UserDetail.this,event,team,pay);
                        l.setAdapter(lView);
                        t1.setText("\nTotal : "+total+"/-");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UserDetail.this,"Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
