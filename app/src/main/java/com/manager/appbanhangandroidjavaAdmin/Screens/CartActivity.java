package com.manager.appbanhangandroidjavaAdmin.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.manager.appbanhangandroidjavaAdmin.R;
import com.manager.appbanhangandroidjavaAdmin.Utils.Utils;
import com.manager.appbanhangandroidjavaAdmin.adapters.CartAdapter;
import com.manager.appbanhangandroidjavaAdmin.models.EventBus.SumEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    TextView tvCartNull, tvSumPrice;
    RecyclerView rcv;
    Button btnOrderNow;
    ImageView imgBack;
    CartAdapter cartAdapter;
    long sumMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initControl();
        SumMoney();
    }


    private void SumMoney(){
         sumMoney = 0;
        for(int i = 0; i<Utils.mangMuaHang.size(); i++){
            sumMoney = sumMoney + (Utils.mangMuaHang.get(i).getGiasp() * Utils.mangMuaHang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
         tvSumPrice.setText(decimalFormat.format(sumMoney));

    }
    private void initControl() {
        rcv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        if(Utils.mangGioHang.size() == 0){
            tvCartNull.setVisibility(View.VISIBLE);
        }else {
            cartAdapter = new CartAdapter(getApplicationContext(), Utils.mangGioHang);
            rcv.setAdapter(cartAdapter);
        }


        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPay = new Intent(CartActivity.this,PayImmediatelyActivity.class);
                intentPay.putExtra("sumprice",sumMoney);
                Utils.mangGioHang.clear();
                startActivity(intentPay);
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initView() {
        rcv = findViewById(R.id.rcvCart);
        tvCartNull = findViewById(R.id.tvGioHangTrong);
        tvSumPrice = findViewById(R.id.tvTongTien);
        btnOrderNow = findViewById(R.id.btnOrderNow);
        imgBack = findViewById(R.id.imgBack);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventSum(SumEvent sumEvent){
        if(sumEvent != null){
            SumMoney();
        }
    }
}