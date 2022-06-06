package com.example.socialmedia;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class HelperClass {

     public static  String  getTodayDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }



    public static  void showMsg(Context context, String msg){
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }



    public  static  ProgressDialog createProgressDialog(Context context , String msg ){
         ProgressDialog dialog = new ProgressDialog(context) ;
         dialog.setMessage(msg);
         dialog.setCancelable(false);

         return dialog ;
    }
}
