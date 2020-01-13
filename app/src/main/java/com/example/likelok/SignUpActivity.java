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

import com.example.likelok.models.CompanyProfile;
import com.example.likelok.models.SeekerProfile;
import com.example.likelok.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText etEmail, etPassword;
    private Button btSignUp;
    private TextView signInText;
    private ProgressBar loading;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase, drSeeker, drCompany;
    private FirebaseDatabase mFirebaseInstance;

    private String user_type, userId;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        pref = getSharedPreferences("likelok", MODE_PRIVATE);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        btSignUp = findViewById(R.id.bt_sign_up);

        signInText = findViewById(R.id.signIn_text);

        loading = findViewById(R.id.loading);

        user_type = getIntent().getStringExtra("user_type");

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        drSeeker = mFirebaseInstance.getReference("seeker_profile");
        drCompany = mFirebaseInstance.getReference("company_profile");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("LikeLok");

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                loading.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    loading.setVisibility(View.GONE);
                                    Log.e("Gagal euy", task.getException().toString());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String name = "user-" + getSaltString();
                                    createUser(email, password, user_type);
                                    if (user_type.equals("company")) {
                                        createCompanyProfile(name, email, "-", "-", "-", "-", "-");
                                    } else {
                                        createSeekerProfile(name, email, "-", "-", "-", "-");
                                    }
                                    loading.setVisibility(View.GONE);
                                    startActivity(new Intent(SignUpActivity.this, Login2Activity.class));
                                    finish();
                                }
                            }
                        });


            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, Login2Activity.class);
                startActivity(i);
            }
        });

    }



    private void createCompanyProfile(String name, String email, String pic, String alamat, String industri, String telepon, String pegawai) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        CompanyProfile companyProfile = new CompanyProfile(name, email, pic, alamat, industri, telepon, pegawai);

        drCompany.child(userId).setValue(companyProfile);

        drCompany.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CompanyProfile cp = dataSnapshot.getValue(CompanyProfile.class);
                if (cp == null) {
                    Log.e(TAG, "Company kosong gan");
                    return;
                }

                Log.d(TAG, "Data company masuk gan");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Company gagal gan");
            }
        });

    }

    private void createSeekerProfile(String name, String email, String pic, String alamat, String jk, String hp) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        SeekerProfile seekerProfile = new SeekerProfile(name, email, pic, alamat, jk, hp);

        drSeeker.child(userId).setValue(seekerProfile);

        drSeeker.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SeekerProfile sp = dataSnapshot.getValue(SeekerProfile.class);
                if (sp == null) {
                    Log.e(TAG, "Seeker kosong gan");
                    return;
                }

                Log.d(TAG, "Data seeker masuk gan");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Seeker gagal gan");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.setVisibility(View.GONE);
    }

    private void createUser(String email, String password, String user_type) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(email, password, user_type);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();

    }

    private void addUserChangeListener() {

        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + ", " + user.email);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
