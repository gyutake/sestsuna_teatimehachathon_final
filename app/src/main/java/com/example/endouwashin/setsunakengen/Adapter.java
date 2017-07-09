package com.example.endouwashin.setsunakengen;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<ViewHolder>{
    @NonNull
    private final List<Drawable> data;

    public Adapter(@NonNull List<Drawable> data){
        this.data=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        return ViewHolder.create(inflater,parent);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        final Drawable image=data.get(position);
        holder.imageView.setImageDrawable(image);

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    synchronized void addItem(Drawable drawable) {
        data.add(0, drawable);
        notifyDataSetChanged();
    }

    synchronized void removeItem() {
        data.remove(0);
        notifyDataSetChanged();
    }
}
