package com.manager.appbanhangandroidjavaAdmin.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.manager.appbanhangandroidjavaAdmin.R;
import com.manager.appbanhangandroidjavaAdmin.Utils.Utils;
import com.manager.appbanhangandroidjavaAdmin.adapters.HistoryAdapter;
import com.manager.appbanhangandroidjavaAdmin.retrofits.APIBanHang;
import com.manager.appbanhangandroidjavaAdmin.retrofits.Retrofitclient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewHistoryActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanHang apiBanHang;

    RecyclerView rcvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        initUi();
        apiBanHang = Retrofitclient.getInstance(Utils.BASE_URL).create(APIBanHang.class);
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            HistoryAdapter historyAdapter = new HistoryAdapter(getApplicationContext(), orderModel.getResult());
                            rcvHistory.setAdapter(historyAdapter    );
                        },
                        throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("error:",throwable.getMessage());
                        }
                )
        );
    }

    private void initUi(){
        rcvHistory = findViewById(R.id.rcvHistory);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        rcvHistory.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}