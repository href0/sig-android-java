package com.project.fishingspots;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class DetailTokoActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private static final int STORAGE_CODE = 1003;
    private static final int IMAGE_PICK_GALLERY_CODE = 1004;

    TextView namaToko, jadwalBuka, jamBuka, nohp, alamat;
    private TextView editJadwalBuka, editJamBuka, editNohp, editAlamat;
    LinearLayout lihatLokasiLl, simpanGambarLl;
    ImageView fotoToko;
    Button lihatLokasi, simpanGbr, batalSimpan;
    ActionBar actionBar;

    FirebaseAuth mAuth;
    DatabaseReference database;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;

    Bundle bundle;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko);

        bundle = getIntent().getExtras();
        actionBar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        if(bundle.getString("type").equals("0")){
            getSupportActionBar().setTitle("Detail Toko");
        }else{
            getSupportActionBar().setTitle("Toko Saya");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        //TextView
        editJadwalBuka = findViewById(R.id.editJadwalBukaTv);
        editJamBuka = findViewById(R.id.editJamBukaTv);
        editNohp = findViewById(R.id.editNoHpTv);
        editAlamat = findViewById(R.id.editAlamatTv);
        namaToko = findViewById(R.id.namaTokoTv);
        jadwalBuka = findViewById(R.id.jadwalBukaTv);
        jamBuka = findViewById(R.id.jamBukaTv);
        nohp = findViewById(R.id.nohpTv);
        alamat = findViewById(R.id.alamatTv);
        fotoToko = findViewById(R.id.fotoTokoIv);
        lihatLokasi = findViewById(R.id.lihatLokasiBtn);

        // LinearLayout
        simpanGambarLl = findViewById(R.id.simpanGambarLl);
        lihatLokasiLl = findViewById(R.id.lihatLokasiLl);

        // Button
        simpanGbr = findViewById(R.id.simpanGambarBtn);
        batalSimpan = findViewById(R.id.batalSimpanBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance().getReference("toko");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(bundle.getString("type").equals("0")){
            namaToko.setText(bundle.getString("namaToko"));
            jadwalBuka.setText(bundle.getString("jadwalBuka"));
            jamBuka.setText(bundle.getString("jamBuka") + " WIB");
            alamat.setText(bundle.getString("alamat"));
            nohp.setText(bundle.getString("nohp"));
            Picasso.get().load(bundle.getString("image")).into(fotoToko);
            lihatLokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTokoActivity.this, MapsActivity.class);
                    intent.putExtra("latitude",bundle.getDouble("latitude"));
                    intent.putExtra("longitude",bundle.getDouble("longitude"));
                    intent.putExtra("namaToko",bundle.getString("namaToko"));
                    startActivity(intent);
                }
            });
        }else{

            Query query = database.orderByChild("uid").equalTo(bundle.getString("uid"));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Toko toko = dataSnapshot.getValue(Toko.class);
                        namaToko.setText(toko.getNamaToko());
                        jadwalBuka.setText(toko.getBuka());
                        jamBuka.setText(toko.getBuka()+" WIB");
                        alamat.setText(toko.getAlamat());
                        nohp.setText(toko.getNohp());
                        Picasso.get().load(toko.getFotoToko()).into(fotoToko);
                        lihatLokasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailTokoActivity.this, MapsActivity.class);
                                intent.putExtra("latitude",toko.getLatitude());
                                intent.putExtra("longitude",toko.getLongitude());
                                intent.putExtra("namaToko",toko.getNamaToko());
                                startActivity(intent);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        editJadwalBuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "buka";
                String edit = "Jadwal Buka";
                doingEdit(key, edit);
            }
        });
        editJamBuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "jamBuka";
                String edit = "Jam Buka";
                doingEdit(key, edit);
            }
        });

        editAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "alamat";
                String edit = "Alamat";
                doingEdit(key, edit);
            }
        });

        editNohp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "nohp";
                String edit = "No Handphone";
                doingEdit(key, edit);
            }
        });
//
        if(user != null){
            if(bundle.getString("uid").equals(user.getUid())){
                if(bundle.getString("type").equals("1")){
                    fotoToko.setOnClickListener(v -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailTokoActivity.this);
                        String options[] = {"Kamera", "Galeri"};
                        builder.setTitle("Pilih dari");

                        //set items to dialog
                        builder.setItems(options, (dialog, which) -> {

                            // kamera
                            // if OS is marshmellow
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(DetailTokoActivity.this, Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_DENIED ||
                                        ContextCompat.checkSelfPermission(DetailTokoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                                PackageManager.PERMISSION_DENIED ||
                                        ContextCompat.checkSelfPermission(DetailTokoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                                                PackageManager.PERMISSION_DENIED) {
                                    // permission not enable, request it
                                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
                                    // show popup to request permission
                                    requestPermissions(permission, PERMISSION_CODE);
                                } else {
                                    // permission already granted
                                    if(which == 0){
                                        openCamera();
                                    }else{
                                        openGallery();
                                    }

                                }
                            } else {
                                // system os < marshmellow
                                if(which == 0){
                                    openCamera();
                                }else{
                                    openGallery();
                                }
                            }
                        });
                        builder.create().show();

                    });
                }

            }
        }

    }

    private void openGallery() {
        Intent galeryIntent = new Intent(Intent.ACTION_PICK);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        image_uri = DetailTokoActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        // camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void doingEdit(String key, String edit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailTokoActivity.this);
        builder.setTitle("Edit "+edit);

        final EditText editText = new EditText(DetailTokoActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);

        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, Object> result =new HashMap<>();
                result.put(key, editText.getText().toString());

                user = mAuth.getCurrentUser();
                database.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void aVoid) {
                                Toast.makeText(DetailTokoActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailTokoActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user == null){
            editAlamat.setVisibility(View.GONE);
            editJadwalBuka.setVisibility(View.GONE);
            editJamBuka.setVisibility(View.GONE);
            editNohp.setVisibility(View.GONE);

        }else{
            if(!user.getUid().equals(bundle.getString("uid"))){
                editAlamat.setVisibility(View.GONE);
                editJadwalBuka.setVisibility(View.GONE);
                editJamBuka.setVisibility(View.GONE);
                editNohp.setVisibility(View.GONE);
            }else if(bundle.getString("type").equals("0")){
                editAlamat.setVisibility(View.GONE);
                editJadwalBuka.setVisibility(View.GONE);
                editJamBuka.setVisibility(View.GONE);
                editNohp.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(bundle.getString("type").equals("0")){
            Intent intent = new Intent(DetailTokoActivity.this, SpotList.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(DetailTokoActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    // handling persmission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // this method is called,  when user press allow or deny from persmission request popup
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup was granted
                    openCamera();
                } else {
                    // permission was denied
                    Toast.makeText(DetailTokoActivity.this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // called when image was captured
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // set the image captured to image view

            ProgressDialog pd = new ProgressDialog(DetailTokoActivity.this);
            pd.setMessage("Mohon tunggu...");
            pd.show();
            if (ActivityCompat.checkSelfPermission(DetailTokoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailTokoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                pd.dismiss();
                lihatLokasiLl.setVisibility(View.GONE);
                simpanGambarLl.setVisibility(View.VISIBLE);

                if(requestCode == IMAGE_PICK_GALLERY_CODE){
                    image_uri = data.getData();
                }

//                fotoToko.setImageURI(image_uri);
//                Uri uri = image_uri;

                try {
                    // compres Image
                    Bitmap original = MediaStore.Images.Media.getBitmap(getContentResolver(),image_uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    original.compress(Bitmap.CompressFormat.JPEG,50,stream);
                    fotoToko.setImageBitmap(original);
                    byte[] imageByte = stream.toByteArray();
                    simpanGbr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProgressDialog loading = new ProgressDialog(DetailTokoActivity.this);
                            loading.setMessage("Mengupload...");
                            loading.setCanceledOnTouchOutside(false);
                            loading.show();
                            final String randomKey = UUID.randomUUID().toString();
                            StorageReference mountainsRef = storageReference.child(randomKey);
                            mountainsRef.putBytes(imageByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful());
                                    Uri downloadUri = uriTask.getResult();

                                    // check if image is uploaded or not and url is received
                                    if(uriTask.isSuccessful()){
                                        // image uploaded
                                        HashMap<String, Object> results = new HashMap<>();
                                        results.put("fotoToko", downloadUri.toString());
                                        database.child(user.getUid()).updateChildren(results)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(@NonNull Void aVoid) {
                                                        loading.dismiss();
                                                        Toast.makeText(DetailTokoActivity.this, "berhasil upload", Toast.LENGTH_SHORT).show();
                                                        startActivity(getIntent());
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading.dismiss();
                                                Toast.makeText(DetailTokoActivity.this, "Terjadi kesalahan "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        loading.dismiss();
                                        Toast.makeText(DetailTokoActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailTokoActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }




                batalSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(getIntent());
                    }
                });

                return;
            }
        }
    }

}