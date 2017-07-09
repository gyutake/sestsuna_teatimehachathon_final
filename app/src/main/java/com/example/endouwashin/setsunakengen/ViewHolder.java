package com.example.endouwashin.setsunakengen;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jeed on 7/8/2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
    private static final int LAYOUT_ID= R.layout.list_item;
    @NonNull
    public static ViewHolder create(@NonNull LayoutInflater inflater, ViewGroup parent){
        return new ViewHolder(inflater.inflate(LAYOUT_ID, parent, false));
    }
    final ImageView imageView;

    private ViewHolder(View itemView){
        super(itemView);
        imageView=(ImageView)itemView.findViewById(R.id.myImageView);
    }

}
