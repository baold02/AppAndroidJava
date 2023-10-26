package com.manager.appbanhangandroidjavaAdmin.Utils;

import com.manager.appbanhangandroidjavaAdmin.models.GioHang;
import com.manager.appbanhangandroidjavaAdmin.models.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL ="http://192.168.0.102/banHang/";
    public static List<GioHang> mangGioHang;
    public static List<GioHang> mangMuaHang = new ArrayList<>();
    public static User user_current = new User();

    public static  String ID_RECEIVED;
    public static final String SENDID = "idsend";
    public static final String RECEIVEDID = "idreceived";
    public static final String MESS = "message";
    public static final String DATETIME = "datetime";
    public static final String PATH_CHAT = "chat";
}
