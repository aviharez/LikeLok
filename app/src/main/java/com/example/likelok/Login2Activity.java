package com.example.likelok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likelok.company.CompanyMainActivity;
import com.example.likelok.models.User;
import com.example.likelok.seeker.SeekerMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login2Activity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btLogin;
    private TextView signUpText;
    private ProgressBar loading;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private SharedPreferences pref;

    private String userId, user_type, type_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        auth = FirebaseAuth.getInstance();
        pref = getSharedPreferences("likelok", MODE_PRIVATE);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        btLogin = findViewById(R.id.btn_login);

        signUpText = findViewById(R.id.signUp_text);

        loading = findViewById(R.id.loading);

        type_intent = getIntent().getStringExtra("user_type");

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                loading.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login2Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    loading.setVisibility(View.GONE);
                                    Toast.makeText(Login2Activity.this, "Gagal login :(", Toast.LENGTH_LONG).show();
                                } else {
                                    checkUserType(email);
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login2Activity.this, SignUpActivity.class);
                i.putExtra("user_type", type_intent);
                startActivity(i);
            }
        });

    }

    private void checkUserType(final String email) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        Query query = mFirebaseDatabase.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    user_type = user.getUser_type();
                    Log.d("TAG", email + " dengan tipe " + user_type);
                }

                savePref(email, user_type);

                if (user_type.equals("company")) {
                    startActivity(new Intent(Login2Activity.this, CompanyMainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(Login2Activity.this, SeekerMainActivity.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void savePref(String email, String user_type) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.putString("user_type", user_type);
        editor.putBoolean("login", true);
        editor.apply();
    }

}
