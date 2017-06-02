package com.gamebox_idtkown.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhangkai on 16/11/8.
 */
public class PingUtil {
    public static boolean pinging = false;
    public static boolean ping(String str) {
        if(pinging){
            return true;
        }
        pinging = true;
        boolean resault = false;
        Process p;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
            p = Runtime.getRuntime().exec("ping -c 1 -w 5 " + str);
            int status = p.waitFor();

            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            LogUtil.msg("return ->" + buffer.toString());

            if (status == 0) {
                resault = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pinging = false;
        return resault;
    }
}
