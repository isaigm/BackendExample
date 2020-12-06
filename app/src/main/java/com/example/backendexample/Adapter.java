package com.example.backendexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<User> users;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(users.get(position));
        if(holder.imageView != null){
            Picasso.get().load(users.get(position).getFoto())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.imageView);
        }

    }
    public Adapter(ArrayList<User> users){
        this.users = users;
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    static public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView full_name;
        private TextView age_teln;
        private TextView dir;
        private com.pkmmte.view.CircularImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            full_name = itemView.findViewById(R.id.full_name);
            age_teln = itemView.findViewById(R.id.edad_tel);
            dir = itemView.findViewById(R.id.dir);
            imageView = itemView.findViewById(R.id.image);
        }
        @SuppressLint("SetTextI18n")
        public void setData(User u) {
            full_name.setText(u.getNombre() + " " + u.getApellidos());
            age_teln.setText(u.getEdad() + " " + u.getTelefono());
            dir.setText(u.getDireccion());
        }
    }
}
