package com.manager.appbanhangandroidjavaAdmin.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.appbanhangandroidjavaAdmin.R;
import com.manager.appbanhangandroidjavaAdmin.Utils.Utils;
import com.manager.appbanhangandroidjavaAdmin.models.NotiSenData;
import com.manager.appbanhangandroidjavaAdmin.retrofits.APIBanHang;
import com.manager.appbanhangandroidjavaAdmin.retrofits.PushNotification;
import com.manager.appbanhangandroidjavaAdmin.retrofits.Retrofitclient;
import com.google.gson.Gson;
import com.manager.appbanhangandroidjavaAdmin.retrofits.RetrofitclientFcm;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayImmediatelyActivity extends AppCompatActivity {
    TextView tvSumPrice, tvPhone, tvEmail;
    EditText edtLocation;
    Button btnMuaHang;
    CompositeDisposable  compositeDisposable = new CompositeDisposable();
    APIBanHang apiBanHang;
    int totalItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_immediately);
        initView();
        initControl();
        countItem();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mangMuaHang.size(); i++) {
            totalItem = totalItem + Utils.mangMuaHang.get(i).getSoluong();
        }
    }

    private void  initControl(){
        long sumPrice = getIntent().getLongExtra("sumprice",0);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvSumPrice.setText( decimalFormat.format(sumPrice));
        tvPhone.setText(Utils.user_current.getPhone());
        tvEmail.setText(Utils.user_current.getEmail());
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diaChi = edtLocation.getText().toString().trim();
                if(TextUtils.isEmpty(str_diaChi)){
                    Toast.makeText(PayImmediatelyActivity.this, "Ban phai nhap dia chi", Toast.LENGTH_SHORT).show();
                }else {
                     String str_email = Utils.user_current.getEmail();
                     String str_sdt = Utils.user_current.getPhone();
                     int id = Utils.user_current.getId();
                    compositeDisposable.add(apiBanHang.creatOder(str_email,str_sdt,String.valueOf(sumPrice),id,str_diaChi,2,new  Gson().toJson(Utils.mangMuaHang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        pushNotiToUser();
                                        Toast.makeText(PayImmediatelyActivity.this, "THanh cong", Toast.LENGTH_SHORT).show();
                                        Utils.mangMuaHang.clear();
                                    },
                                    throwable -> {
                                        Toast.makeText(PayImmediatelyActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );
                    //post data
                    Log.d("test", new Gson().toJson(Utils.mangGioHang));
                }
            }
        });

    }

    private void pushNotiToUser() {

        //getToken
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                for (int i = 0;i<userModel.getReslut().size();i++){
                                    Map<String,String> dataPush = new HashMap<>();
                                    dataPush.put("title","Thong bao");
                                    dataPush.put("body","Ban co don hang moi");
                                    NotiSenData notiSenData = new NotiSenData(userModel.getReslut().get(i).getToken(),dataPush);
                                    PushNotification notification = RetrofitclientFcm.getInstance().create(PushNotification.class);
                                    compositeDisposable.add(notification.sendNotification(notiSenData)
                                            .observeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiRespone -> {

                                                    },
                                                    throwable -> {

                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {

                        }
                )
        );

    }


    private void initView(){
        apiBanHang = Retrofitclient.getInstance(Utils.BASE_URL).create(APIBanHang.class);
      tvSumPrice = findViewById(R.id.tvSumprice);
      tvEmail = findViewById(R.id.tvEmail);
      tvPhone =  findViewById(R.id.tvPhone);
      edtLocation = findViewById(R.id.edtLocation);
      btnMuaHang = findViewById(R.id.btnDatHang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}