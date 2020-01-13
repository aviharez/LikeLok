package com.example.likelok.company;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.likelok.R;
import com.example.likelok.models.CompanyProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyProfilEditActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;

    private ImageView bt_back;
    private EditText et_name, et_email, et_alamat, et_industri, et_tlp, et_pegawai;
    private ProgressBar loading;
    private Button bt_simpan;
    private ImageButton ib_upload;
    private CircleImageView iv_pic;
    private DatabaseReference databaseReference;
    private SharedPreferences pref;
    private String email, userId, pic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private Boolean upload_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profil_edit);

        pref = getSharedPreferences("likelok", MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("company_profile");

        email = pref.getString("email", null);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        upload_pic = false;

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_alamat = findViewById(R.id.et_alamat);
        et_industri = findViewById(R.id.et_industri);
        et_tlp = findViewById(R.id.et_tlp);
        et_pegawai = findViewById(R.id.et_pegawai);

        loading = findViewById(R.id.loading);
        iv_pic = findViewById(R.id.iv_pic);
        ib_upload = findViewById(R.id.ib_upload);

        bt_back = findViewById(R.id.bt_back);
        bt_simpan = findViewById(R.id.bt_simpan);

        loadData();

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReference.orderByChild("email").equalTo(email);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        loading.setVisibility(View.GONE);
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (upload_pic) {
                                uploadImage(postSnapshot.getKey());
                            } else {
                                updateData(postSnapshot.getKey(), pic);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ib_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void updateData(String key, String picture) {
        String name = et_name.getText().toString();
        String alamat = et_alamat.getText().toString();
        String tlp = et_tlp.getText().toString();
        String pegawai = et_pegawai.getText().toString();
        String industri = et_industri.getText().toString();
        CompanyProfile cp = new CompanyProfile(name, email, picture, alamat, industri, tlp, pegawai);
        databaseReference.child(key).setValue(cp);
        Toast.makeText(getApplicationContext(), "Data profil berhasil diperbarui", Toast.LENGTH_LONG).show();
    }

    private void uploadImage(final String key) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref = storageReference.child("company/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("INI TAG WOY", "onSuccess: uri= "+ uri.toString());
                                    pic = uri.toString();
                                    progressDialog.dismiss();
                                    Log.d("TAG", "Gambar udah di up");
                                    updateData(key, pic);
                                    upload_pic = false;
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }

    private void loadData() {
        loading.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(userId)) {
            userId = databaseReference.push().getKey();
        }
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CompanyProfile companyProfile = postSnapshot.getValue(CompanyProfile.class);

                    pic = companyProfile.getPic();

                    if (!companyProfile.getPic().equals("-")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(companyProfile.getPic())).into(iv_pic);
                    }

                    et_name.setText(companyProfile.getName());
                    et_email.setText(companyProfile.getEmail());
                    et_alamat.setText(companyProfile.getAlamat());
                    et_industri.setText(companyProfile.getIndustri());
                    et_tlp.setText(companyProfile.getTelepon());
                    et_pegawai.setText(companyProfile.getPegawai());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            upload_pic = true;
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_pic.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
