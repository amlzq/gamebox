package com.gamebox_idtkown.core;

/**
 * Created by zhangkai on 16/9/12.
 */

/**
 * The status of apk
 * */
public class ApkStatus {
    public final static  int INSTALLED = 1;      /** @serialField  The status of apk is installed */
    public final static  int DOWNLOADED = 2;    /** @serialField  The status of apk is downloaded */
    public final static  int DOWNLOADING = 3;    /** @serialField  The status of apk is undownloaded, but is download
    . */
    public final static  int UNDOWNLOAD = 4;     /** @serialField  The status of apk is undownloaded and isn't
    download*/
    public final static  int WAITING = 5;        /** @serialField  The status of apk is waiting for  download*/
    public final static  int Error = 6;        /** @serialField  The status of apk is error in  downloading*/
    public final static  int Stop = 7;        /** @serialField  The status of apk is stop in  downloading*/

    public final static  int WAIT_LISTENER = 8;        /** @serialField  The status of apk is waiting for  download*/



}
