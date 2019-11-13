package e.shery.visiospark.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import e.shery.visiospark.R;
import e.shery.visiospark.api.RetrofitClient;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class QrReader extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    String name,token,userId;
    int value,eventId;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        Bundle bundle = getIntent().getExtras();
        value = bundle.getInt("value");
        if (value == 1){
            name = bundle.getString("name");
            token = bundle.getString("token");
            userId = bundle.getString("id");
        }
        else if (value == 2){
            name = bundle.getString("name");
            token = bundle.getString("token");
            userId = bundle.getString("id");
            eventId = bundle.getInt("eventId");
        }
        else {
            name = bundle.getString("name");
            token = bundle.getString("token");
            userId = bundle.getString("id");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
//         Log.v("tag", rawResult.getText()); // Prints scan results
//         Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

//        showMessage("VisioSpark",rawResult.getText());
//        onBackPressed();
        mScannerView.stopCamera();

        String result = rawResult.getText().toString().trim();
        if (value == 0){
            foodData(result);
        }
        else if (value == 2)
        {
            UserEntry(result);
        }
        else
        {
            UserRecord(result);
        }

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    private void UserEntry(String QrCode){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .eventHead_UserCheckIn(QrCode,eventId,"application/json","Bearer "+token);

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
                        String data = jsonObject.getString("message");

                        showMessage("VisioSpark",data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QrReader.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void foodData(String QrCode){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .food_token(QrCode,"application/json","Bearer "+token);

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
                        String data = jsonObject.getString("message");

                        showMessage("VisioSpark",data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QrReader.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void UserRecord(String QrCode){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .admin_UserQrReport(QrCode,"application/json","Bearer "+token);

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
                        String data1 = "";
                        JSONObject jsonObject = new JSONObject(s);
                        String UniName = jsonObject.getString("name");
                        String event = jsonObject.getString("event");
                        int food = jsonObject.getInt("food");
                        String m1,m2,m3,m4,m5,m6;

                        if (food == 0)
                            m6 = "No";
                        else
                            m6 = "Yes";

                        m1 = jsonObject.getJSONObject("team").getString("mem1");
                        m2 = jsonObject.getJSONObject("team").getString("mem2");
                        m3 = jsonObject.getJSONObject("team").getString("mem3");
                        m4 = jsonObject.getJSONObject("team").getString("mem4");
                        m5 = jsonObject.getJSONObject("team").getString("mem5");

                        if (m1 != "null" && m2 == "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                            data1 = "\nUniversity : "+UniName+"\nEvent         : "+event+"\n\nMember 1 : "+m1+"\n\nFood Provided : "+m6;
                        }
                        else if (m1 != "null" && m2 != "null" && m3 == "null" && m4 == "null" && m5 == "null"){
                            data1 = "\nUniversity : "+UniName+"\nEvent         : "+event+"\n\nMember 1 : "+m1+"\nMember 2 : "+m2+"\n\nFood Provided : "+m6;
                        }
                        else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 == "null" && m5 == "null"){
                            data1 = "\nUniversity : "+UniName+"\nEvent         : "+event+"\n\nMember 1 : "+m1+"\nMember 2 : "+m2+"\nMember 3 : "+m3+"\n\nFood Provided : "+m6;
                        }
                        else if (m1 != "null" && m2 != "null" && m3 != "null" && m4 != "null" && m5 == "null"){
                            data1 = "\nUniversity : "+UniName+"\nEvent         : "+event+"\n\nMember 1 : "+m1+"\nMember 2 : "+m2+"\nMember 3 : "+m3+"\nMember 4 : "+m4+"\n\nFood Provided : "+m6;
                        }
                        else{
                            data1 = "\nUniversity : "+UniName+"\nEvent         : "+event+"\n\nMember 1 : "+m1+"\nMember 2 : "+m2+"\nMember 3 : "+m3+"\nMember 4 : "+m4+"\nMember 5 : "+m5+"\n\nFood Provided : "+m6;
                        }

                        showMessage("VisioSpark",data1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QrReader.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        mScannerView.setResultHandler(QrReader.this); // Register ourselves as a handler for scan results.
                        mScannerView.startCamera();          // Start camera on resume
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // User clicked the No button
                        break;
                }
            }
        };
        builder.setPositiveButton("OK",dialogClickListener);
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
//        builder.show();
    }
}
