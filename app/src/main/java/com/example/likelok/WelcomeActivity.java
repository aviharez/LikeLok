package com.example.likelok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.likelok.company.CompanyMainActivity;
import com.example.likelok.seeker.SeekerMainActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button bt_hire, bt_find;
    private LinearLayout bt_employee, bt_recruiter;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        pref = getSharedPreferences("likelok", MODE_PRIVATE);

        Boolean login = pref.getBoolean("login", false);
        String user_type = pref.getString("user_type", null);
        if (login) {
            if (user_type.equals("company")) {
                startActivity(new Intent(WelcomeActivity.this, CompanyMainActivity.class));
                finish();
            } else {
                startActivity(new Intent(WelcomeActivity.this, SeekerMainActivity.class));
                finish();
            }
        }

        bt_hire = findViewById(R.id.bt_hire);
        bt_find = findViewById(R.id.bt_find);

        bt_employee = findViewById(R.id.bt_employee);
        bt_recruiter = findViewById(R.id.bt_recruiter);

        bt_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                i.putExtra("user_type", "company");
                startActivity(i);
            }
        });

        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                i.putExtra("user_type", "seeker");
                startActivity(i);
            }
        });

        bt_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, Login2Activity.class);
                i.putExtra("user_type", "seeker");
                startActivity(i);
            }
        });

        bt_recruiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, Login2Activity.class);
                i.putExtra("user_type", "company");
                startActivity(i);
            }
        });

    }
}
