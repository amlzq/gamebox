package com.gamebox_idtkown.utils;

import java.text.DecimalFormat;

/**
 * Created by zhangkai on 16/10/10.
 */
public class SizeUitl {
    public static String getMKBStr(int size){
        if(size == 0){
            return "0M";
        }
        String str = "0.0M";
        float m = size / 1024.0f / 1024.0f;
        float k = size / 1024.0f;
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String mStr= decimalFormat.format(m);
        String kStr= decimalFormat.format(k);
        if(m > 1.0f){
            str = mStr + "M";
        } else if(k > 1.0f) {
            str = kStr +"KB";
        } else {
            str = size +"bit";
        }
        return str;
    }

    public static String getSpeedStr(int kb){
        String str = "0.0M";
        float m = kb / 1024.0f;
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String mStr= decimalFormat.format(m);
        if(m > 1.0f){
            str = mStr + "M";
        } else {
            str = kb +"KB";
        }
        return str;
    }
}
