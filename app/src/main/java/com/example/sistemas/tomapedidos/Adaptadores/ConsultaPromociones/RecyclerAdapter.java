package com.example.sistemas.tomapedidos.Adaptadores.ConsultaPromociones;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolderDatos> {
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolderDatos viewHolderDatos, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
        }

    }
}
