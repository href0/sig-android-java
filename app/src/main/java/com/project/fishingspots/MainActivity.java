package com.project.fishingspots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public CardView listTokoCv, tokoSaya, map, tentang, logout;

    ActionBar actionBar;

    FirebaseAuth mAuth;

    DatabaseReference database;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CardView
        listTokoCv = findViewById(R.id.listTokoCv);
        map = findViewById(R.id.mapTokoCv);
        tentang = findViewById(R.id.tentangCv);
        tokoSaya = findViewById(R.id.tokosayaCv);
        logout = findViewById(R.id.logoutCv);

        database = FirebaseDatabase.getInstance().getReference("toko");


        mAuth = FirebaseAuth.getInstance();

        actionBar = getSupportActionBar();
        getSupportActionBar().hide();

        listTokoCv.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SpotList.class)));

        map.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapsActivity.class)));

        tentang.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TentangActivity.class)));



        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(getIntent());

        });
        tokoSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = mAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }else{
                    String uid = user.getUid();
                    Query query = database.orderByChild("uid").equalTo(user.getUid());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()){
                                Intent intent = new Intent(MainActivity.this, DetailTokoActivity.class);
                                intent.putExtra("uid",""+ds.child("uid").getValue());
                                intent.putExtra("type","1");

                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user == null){
            logout.setVisibility(View.GONE);
        }
    }
}