package com.example.likelok.seeker;

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
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.likelok.R;
import com.example.likelok.models.SeekerProfile;
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

public class SeekerProfilEditActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;

    private ImageView bt_back;
    private EditText et_name, et_alamat, et_email, et_hp;
    private RadioButton rb_lk, rb_pr;
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
        setContentView(R.layout.activity_seeker_profil_edit);

        pref = getSharedPreferences("likelok", MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("seeker_profile");

        email = pref.getString("email", null);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        upload_pic = false;

        et_name = findViewById(R.id.et_name);
        et_alamat = findViewById(R.id.et_alamat);
        et_email = findViewById(R.id.et_email);
        et_hp = findViewById(R.id.et_hp);

        rb_lk = findViewById(R.id.rb_lk);
        rb_pr = findViewById(R.id.rb_pr);

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
        String hp = et_hp.getText().toString();
        String jk=null;
        if (rb_lk.isChecked()) jk = "Laki-laki";
        if (rb_pr.isChecked()) jk = "Perempuan";
        Log.d("TAG", picture);
        SeekerProfile sp = new SeekerProfile(name, email, picture, alamat, jk, hp);
        databaseReference.child(key).setValue(sp);
        Toast.makeText(getApplicationContext(), "Data profil berhasil diperbarui", Toast.LENGTH_LONG).show();
        //loadData();
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
                    SeekerProfile seekerProfile = postSnapshot.getValue(SeekerProfile.class);

                    pic = seekerProfile.getPic();

                    if (!seekerProfile.getPic().equals("-")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(seekerProfile.getPic())).into(iv_pic);
                    }

                    et_name.setText(seekerProfile.getName());
                    et_alamat.setText(seekerProfile.getAlamat());
                    et_email.setText(seekerProfile.getEmail());
                    et_hp.setText(seekerProfile.getHp());
                    if (seekerProfile.getJk().equals("Laki-laki")) {
                        rb_lk.setChecked(true);
                    } else if (seekerProfile.getJk().equals("Perempuan")) {
                        rb_pr.setChecked(true);
                    }

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

    private void uploadImage(final String key) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref = storageReference.child("seeker/" + UUID.randomUUID().toString());

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

}
