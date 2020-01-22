package com.example.likelok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.likelok.models.Company;
import com.example.likelok.models.Loker;
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

public class DetailLokerActivity extends AppCompatActivity {

    private final int PICK_FILE_REQUEST = 22;

    private ImageView bt_back, iv_loker;
    private TextView tv_item_name, tv_office_name,
            tv_address, tv_salary, tv_item_description,
            tv_type_job, tv_lowongan, tv_type_education,
            tv_exp_date, tv_type_industri;
    private Button bt_upload;
    private SharedPreferences pref;
    private String email, userId, type, fileUrl;
    private DatabaseReference databaseReference;
    private Loker loker;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_loker);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_upload);
        dialog.setCancelable(false);

        pref = getSharedPreferences("likelok", MODE_PRIVATE);
        bt_upload = findViewById(R.id.bt_upload);
        bt_back = findViewById(R.id.bt_back);
        iv_loker = findViewById(R.id.iv_loker);
        tv_item_name = findViewById(R.id.tv_item_name);
        tv_office_name = findViewById(R.id.tv_office_name);
        tv_address = findViewById(R.id.tv_address);
        tv_salary = findViewById(R.id.tv_salary);
        tv_item_description = findViewById(R.id.tv_item_description);
        tv_type_job = findViewById(R.id.tv_type_job);
        tv_lowongan = findViewById(R.id.tv_lowongan);
        tv_type_education = findViewById(R.id.tv_type_education);
        tv_exp_date = findViewById(R.id.tv_exp_date);
        tv_type_industri = findViewById(R.id.tv_type_industri);

        fileUrl = "-";

        databaseReference = FirebaseDatabase.getInstance().getReference("company_profile");
        loker = getIntent().getParcelableExtra("data_loker");
        type = getIntent().getStringExtra("type");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (type.equals("company")) {
            bt_upload.setVisibility(View.GONE);
        } else {
            bt_upload.setVisibility(View.VISIBLE);
        }

        loadData();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bt_back.bringToFront();

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Pilih file CV dari..."),
                PICK_FILE_REQUEST);
    }

    private void loadData() {
        if (!loker.getPic().equals("-"))
            Glide.with(getApplicationContext()).load(loker.getPic()).into(iv_loker);

        tv_item_name.setText(loker.getNama_loker());

        tv_salary.setText(loker.getGaji());
        tv_item_description.setText(loker.getDeskripsi());
        tv_type_job.setText(loker.getJenis());
        tv_lowongan.setText(loker.getLowongan());
        tv_type_education.setText(loker.getTingkat());
        tv_exp_date.setText(loker.getDeadline());

        Query query = databaseReference.orderByChild("email").equalTo(loker.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Company cp = postSnapshot.getValue(Company.class);
                    tv_office_name.setText(cp.getName());
                    tv_address.setText(cp.getAlamat());
                    tv_type_industri.setText(cp.getIndustri());
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
        if (requestCode == PICK_FILE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            uploadFile();
        }
    }

    private void uploadFile() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref = storageReference.child("berkas/" + UUID.randomUUID().toString());

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
                                    fileUrl = uri.toString();
                                    progressDialog.dismiss();
                                    showFinishDialog();
                                    Log.d("TAG", "File udah di up");
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

    private void showFinishDialog() {
        Button finish = dialog.findViewById(R.id.bt_selesai);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showDialog();
    }

    private void showDialog(){
        if(!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog(){
        if(dialog.isShowing())
            dialog.dismiss();
    }

}
