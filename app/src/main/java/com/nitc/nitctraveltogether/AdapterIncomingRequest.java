package com.nitc.nitctraveltogether;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterIncomingRequest extends RecyclerView.Adapter<AdapterIncomingRequest.ViewHolder> {

    private ArrayList<ModelClassIncomingRequest> modelClassList;
    private OnItemClickListener mlistener;


    //inner  interface
    public interface OnItemClickListener {
        void onItemClick(int position);

    }


    // inner class
    static class  ViewHolder extends  RecyclerView.ViewHolder{
        private TextView date, email;
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            email = itemView.findViewById(R.id.email);
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
        private void setData( String temail, String tdate)
        {
            date.setText(tdate);
            email.setText(temail);
        }
    }


    // overridden method of adapter class
    public void setOnItemClickListener(OnItemClickListener listener){

        mlistener = listener;
    }
    public AdapterIncomingRequest(ArrayList<ModelClassIncomingRequest> modelClassList)
    {
        this.modelClassList = modelClassList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,     int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incoming_request_item_layout, parent, false);
        return new ViewHolder(view, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = modelClassList.get(position).getDate();
        String email = modelClassList.get(position).getEmail();
        holder.setData(email, date);
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }




}
