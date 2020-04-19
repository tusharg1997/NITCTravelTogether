package com.nitc.nitctraveltogether;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ModelClass> modelClassList;
    private OnItemClickListener mlistener;


    //inner  interface
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }


    // inner class
    static class  ViewHolder extends  RecyclerView.ViewHolder{
        private TextView name, email;
        private ImageView img;
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            img = itemView.findViewById(R.id.deletebtn);
            img.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position  = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position  = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        private void setData(String tname, String temail)
        {
            name.setText(tname);
            email.setText(temail);
        }
    }


    // overridden method of adapter class
    public void setOnItemClickListener(OnItemClickListener listener){

        mlistener = listener;
    }
    public Adapter(ArrayList<ModelClass> modelClassList)
    {
        this.modelClassList = modelClassList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,     int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout, parent, false);
       return new ViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name = modelClassList.get(position).getName();
            String email = modelClassList.get(position).getEmail();
            holder.setData(name, email);
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }




}
