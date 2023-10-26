package com.manager.appbanhangandroidjavaAdmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.appbanhangandroidjavaAdmin.R;
import com.manager.appbanhangandroidjavaAdmin.models.ChatMessageModel;

import java.util.List;


public class Chatadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   Context context;
    List<ChatMessageModel> messageModelList;
    private String sendId;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECE = 2;

    public Chatadapter(Context context, List<ChatMessageModel> messageModelList, String sendId) {
        this.context = context;
        this.messageModelList = messageModelList;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_send_mess,parent,false);
            return new SendMessViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_received,parent,false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEND){
            ((SendMessViewHolder)holder).tvMess.setText(messageModelList.get(position).mess);
            ((SendMessViewHolder)holder).tvTime.setText(messageModelList.get(position).dateTime);
        }else {
            ((ReceivedViewHolder)holder).tvMess.setText(messageModelList.get(position).mess);
            ((ReceivedViewHolder)holder).tvTime.setText(messageModelList.get(position).dateTime);
        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).sendId.equals(sendId)){
            return TYPE_SEND;
        }else {
            return TYPE_RECE;
        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder{
       TextView tvMess, tvTime;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMess = itemView.findViewById(R.id.tvSendMess);
            tvTime = itemView.findViewById(R.id.tvTimesend);
        }
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView tvMess, tvTime;
        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMess = itemView.findViewById(R.id.tvMessRece);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
