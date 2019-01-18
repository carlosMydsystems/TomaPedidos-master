package com.example.sistemas.tomapedidos.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.R;

import java.util.List;

public class BuscarProductoadapter extends RecyclerView.Adapter<BuscarProductoadapter.ViewHolderdatos> implements View.OnClickListener {

    List<Productos> ListaProductos;

    public BuscarProductoadapter(List<Productos> listaProductos) {
        ListaProductos = listaProductos;
    }

    @NonNull
    @Override
    public BuscarProductoadapter.ViewHolderdatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto,null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolderdatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscarProductoadapter.ViewHolderdatos viewHolderdatos, int i) {

        String codigonombreproducto = ListaProductos.get(i).getCodigo()+" - "+ListaProductos.get(i).getNombre();
        viewHolderdatos.tvcodigonombreproducto.setText(codigonombreproducto);
        viewHolderdatos.etvdscripcion.setText(ListaProductos.get(i).getDescripcion());

    }

    @Override
    public int getItemCount() {
        return ListaProductos.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolderdatos extends RecyclerView.ViewHolder {

        TextView tvcodigonombreproducto, etvdscripcion;
        public ViewHolderdatos(@NonNull View itemView) {
            super(itemView);

            tvcodigonombreproducto = itemView.findViewById(R.id.tvCodigoNombreProducto);
            etvdscripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
