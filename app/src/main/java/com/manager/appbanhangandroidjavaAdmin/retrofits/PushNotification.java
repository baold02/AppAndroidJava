package com.manager.appbanhangandroidjavaAdmin.retrofits;


import com.manager.appbanhangandroidjavaAdmin.models.NotiRespone;
import com.manager.appbanhangandroidjavaAdmin.models.NotiSenData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PushNotification {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAlAeD5-c:APA91bHgKtUT2u-ubHv6iUOSrDKbKbai3pgMxsAZDM7kKktn3qsfpQQnHIvUnriEGRBKXHGoyv12vShzm8LkJFT8UdtqhVeMFD3_PzpZaVeyrB4AE6JSBYaRzxRpVmw1sJ0c1g2Gsqcd"
            }
    )

    @POST("fcm/send")
    Observable<NotiRespone> sendNotification(@Body NotiSenData data);
}
