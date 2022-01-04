package com.project.fishingspots;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.MyViewHolder> {

    Context context;

    ArrayList<Toko> list;

    public TokoAdapter(Context context, ArrayList<Toko> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_row, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Toko toko = list.get(position);
        holder.namaToko.setText(toko.getNamaToko());
        holder.alamat.setText(toko.getAlamat());
        Picasso.get().load(toko.getFotoToko()).into(holder.fotoToko);

        // setOnlickListener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent ke Detail
                Intent intent = new Intent(context, DetailTokoActivity.class);
                intent.putExtra("namaToko",toko.getNamaToko());
                intent.putExtra("alamat",toko.getAlamat());
                intent.putExtra("jadwalBuka",toko.getBuka());
                intent.putExtra("jamBuka",toko.getJamBuka());
                intent.putExtra("image",toko.getFotoToko());
                intent.putExtra("latitude",toko.getLatitude());
                intent.putExtra("longitude",toko.getLongitude());
                intent.putExtra("nohp",toko.getNohp());
                intent.putExtra("uid",toko.getUid());
                intent.putExtra("type","0");

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView namaToko, alamat;
        ImageView fotoToko;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaToko = itemView.findViewById(R.id.txtName);
            alamat = itemView.findViewById(R.id.txtAlamat);
            fotoToko = itemView.findViewById(R.id.imageIv);
            cardView = itemView.findViewById(R.id.card);


        }


    }


}
