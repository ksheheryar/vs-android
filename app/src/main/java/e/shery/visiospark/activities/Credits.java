package e.shery.visiospark.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import e.shery.visiospark.R;

public class Credits extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;

    Handler h1 = new Handler();
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            t1.setVisibility(View.VISIBLE);
        }
    };
    Handler h2 = new Handler();
    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            t2.setVisibility(View.VISIBLE);
        }
    };
    Handler h3 = new Handler();
    Runnable r3 = new Runnable() {
        @Override
        public void run() {
            t3.setVisibility(View.VISIBLE);
        }
    };
    Handler h4 = new Handler();
    Runnable r4 = new Runnable() {
        @Override
        public void run() {
            t4.setVisibility(View.VISIBLE);
        }
    };
    Handler h5 = new Handler();
    Runnable r5 = new Runnable() {
        @Override
        public void run() {
            t5.setVisibility(View.VISIBLE);
        }
    };
    Handler h6 = new Handler();
    Runnable r6 = new Runnable() {
        @Override
        public void run() {
            t6.setVisibility(View.VISIBLE);
        }
    };
    Handler h7 = new Handler();
    Runnable r7 = new Runnable() {
        @Override
        public void run() {
            t7.setVisibility(View.VISIBLE);
        }
    };
    Handler h8 = new Handler();
    Runnable r8 = new Runnable() {
        @Override
        public void run() {
            t8.setVisibility(View.VISIBLE);
        }
    };
    Handler h9 = new Handler();
    Runnable r9 = new Runnable() {
        @Override
        public void run() {
            t9.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        t1 = findViewById(R.id.cred);
        t2 = findViewById(R.id.n1);
        t3 = findViewById(R.id.n2);
        t4 = findViewById(R.id.n3);
        t5 = findViewById(R.id.n4);
        t6 = findViewById(R.id.n5);
        t7 = findViewById(R.id.n6);
        t8 = findViewById(R.id.n7);
        t9 = findViewById(R.id.n8);

        h1.postDelayed(r1,1000);
        h2.postDelayed(r2,2000);
        h3.postDelayed(r3,3000);
        h4.postDelayed(r4,4000);
        h5.postDelayed(r5,5000);
        h6.postDelayed(r6,6000);
        h7.postDelayed(r7,7000);
        h8.postDelayed(r8,8000);
        h9.postDelayed(r9,9000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
