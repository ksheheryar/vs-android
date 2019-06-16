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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import e.shery.visiospark.R;

public class FacultyActivity extends AppCompatActivity {

//    private TextView mTextMessage;

    RelativeLayout r1,r2,r3,r4;
    Button b,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        r1 = findViewById(R.id.rl1);
        r2 = findViewById(R.id.rl2);
        r3 = findViewById(R.id.rl3);
        r4 = findViewById(R.id.rl4);
        b = findViewById(R.id.passreset);
        logout = findViewById(R.id.logout);

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

}
