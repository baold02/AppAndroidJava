
package com.manager.appbanhangandroidjavaAdmin.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.manager.appbanhangandroidjavaAdmin.R;
import com.manager.appbanhangandroidjavaAdmin.Utils.Utils;
import com.manager.appbanhangandroidjavaAdmin.adapters.Chatadapter;
import com.manager.appbanhangandroidjavaAdmin.models.ChatMessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
   RecyclerView rcvChat;
   ImageView imgSend;
   EditText edtMess;
   FirebaseFirestore db;
   Chatadapter chatadapter;
   List<ChatMessageModel> messageModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initControl();
        listenMess();
    }

    private void initControl() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessToFire();
            }
        });
    }

    private void sendMessToFire() {
            String str_mess = edtMess.getText().toString().trim();
            if(TextUtils.isEmpty(str_mess)){

            }else{
                HashMap<String,Object> mess = new HashMap<>();
                mess.put(Utils.SENDID, String.valueOf(Utils.user_current.getId()));
                mess.put(Utils.RECEIVEDID, Utils.ID_RECEIVED);
                mess.put(Utils.MESS,str_mess);
                mess.put(Utils.DATETIME,new Date());
                db.collection(Utils.PATH_CHAT).add(mess);
            }
    }


    private void listenMess(){
        db.collection(Utils.PATH_CHAT).whereEqualTo(Utils.SENDID,String.valueOf(Utils.user_current.getId()))
                .whereEqualTo(Utils.RECEIVEDID,Utils.ID_RECEIVED)
                .addSnapshotListener(eventListener);

        db.collection(Utils.PATH_CHAT).whereEqualTo(Utils.SENDID,Utils.ID_RECEIVED)
                .whereEqualTo(Utils.RECEIVEDID,String.valueOf(Utils.user_current.getId()))
                .addSnapshotListener(eventListener);
    }


    private final EventListener<QuerySnapshot> eventListener =  (value, error) -> {
        if (error != null){
            return;
        }
        if (value != null){
            int count  = messageModelList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.sendId = documentChange.getDocument().getString(Utils.SENDID);
                    chatMessageModel.receivedId = documentChange.getDocument().getString(Utils.RECEIVEDID);
                    chatMessageModel.mess = documentChange.getDocument().getString(Utils.MESS);
                    chatMessageModel.dateObj = documentChange.getDocument().getDate(Utils.DATETIME);
                    chatMessageModel.dateTime = format_date(documentChange.getDocument().getDate(Utils.DATETIME));
                    messageModelList.add(chatMessageModel);
                }
            }
            Collections.sort(messageModelList, (o1, o2) -> o1.dateObj.compareTo(o2.dateObj));
            if(count == 0){
                    chatadapter.notifyDataSetChanged();
            }else {
                     chatadapter.notifyItemRangeInserted(messageModelList.size(),messageModelList.size());
                     rcvChat.smoothScrollToPosition(messageModelList.size() -1);
            }
        }
    };


    private String format_date(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy- hh:mm a", Locale.getDefault()).format(date);
    }


    private void initView() {
        messageModelList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        rcvChat = findViewById(R.id.rcv_chat);
        imgSend = findViewById(R.id.Img_send);
        edtMess = findViewById(R.id.edt_mess);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(this);
        rcvChat.setLayoutManager(layoutManager);
        rcvChat.setHasFixedSize(true);
        chatadapter = new Chatadapter(getApplicationContext(),messageModelList,String.valueOf(Utils.user_current.getId()));
        rcvChat.setAdapter(chatadapter);

    }
}