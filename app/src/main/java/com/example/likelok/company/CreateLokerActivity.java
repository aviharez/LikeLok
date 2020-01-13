package com.example.likelok.company;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.likelok.R;
import com.example.likelok.models.Loker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreateLokerActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;

    private ImageView bt_back, iv_pic;
    private ImageButton ib_upload;
    private EditText et_name, et_jenis, et_fee, et_deskripsi, et_lowongan, et_tingkat, et_deadline;
    private Button bt_simpan;
    private ProgressBar loading;
    private String email, userId, pic;
    private SharedPreferences pref;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private Boolean upload_pic;

    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_loker);

        pref = getSharedPreferences("likelok", MODE_PRIVATE);
        email = pref.getString("email", null);
        pic = "-";
        upload_pic = false;

        et_name = findViewById(R.id.et_name);
        et_jenis = findViewById(R.id.et_jenis);
        et_fee = findViewById(R.id.et_fee);
        et_deskripsi = findViewById(R.id.et_deskripsi);
        et_lowongan = findViewById(R.id.et_lowongan);
        et_tingkat = findViewById(R.id.et_tingkat);
        et_deadline = findViewById(R.id.et_deadline);

        iv_pic = findViewById(R.id.iv_pic);
        ib_upload = findViewById(R.id.ib_upload);
        bt_simpan = findViewById(R.id.bt_simpan);
        bt_back = findViewById(R.id.bt_back);
        loading = findViewById(R.id.loading);

        databaseReference = FirebaseDatabase.getInstance().getReference("loker");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        et_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateLokerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().isEmpty()
                        || et_jenis.getText().toString().isEmpty()
                        || et_fee.getText().toString().isEmpty()
                        || et_deskripsi.getText().toString().isEmpty()
                        || et_lowongan.getText().toString().isEmpty()
                        || et_tingkat.getText().toString().isEmpty()
                        || et_deadline.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Seluruh isian wajib diisi", Toast.LENGTH_LONG).show();
                } else {
                    loading.setVisibility(View.VISIBLE);
                    if (upload_pic) {
                        uploadImage();
                    } else {
                        insertData(pic);
                    }
                }
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_deadline.setText(sdf.format(myCalendar.getTime()));
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

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref = storageReference.child("loker/" + UUID.randomUUID().toString());

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
                                    insertData(pic);
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

    private void insertData(String picture) {
        loading.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(userId)) {
            userId = databaseReference.push().getKey();
        }

        String name = et_name.getText().toString();
        String jenis = et_jenis.getText().toString();
        String gaji = et_fee.getText().toString();
        String deskripsi = et_deskripsi.getText().toString();
        String lowongan = et_lowongan.getText().toString();
        String tingkat = et_tingkat.getText().toString();
        String deadline = et_deadline.getText().toString();

        Loker loker = new Loker(name, email, picture, jenis, gaji, deskripsi, lowongan, tingkat, deadline);
        databaseReference.child(userId).setValue(loker);
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                Loker lk = dataSnapshot.getValue(Loker.class);
                if (lk == null) {
                    Log.e("TAG", "Loker kosong gan");
                    return;
                }

                Log.d("TAG", "Data masuk gan");
                onBackPressed();
                //Toast.makeText(getApplicationContext(), "Data berhasil diinputkan", Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Log.e("TAG", "Loker gagal gan");
            }
        });
    }

}
