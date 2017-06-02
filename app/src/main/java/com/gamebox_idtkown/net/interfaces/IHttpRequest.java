package com.gamebox_idtkown.net.interfaces;

import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.exception.NullResonseListenerException;
import com.gamebox_idtkown.net.listeners.OnHttpResonseListener;

import java.io.IOException;
import java.util.Map;

import com.gamebox_idtkown.net.entry.UpFileInfo;

/**
 * Created by zhangkai on 16/8/18.
 */
public interface IHttpRequest {

    Response get(String url) throws IOException;

    void aget(String url, final OnHttpResonseListener httpResonseListener) throws IOException, NullResonseListenerException;

    Response post(String url, Map<String, String> params) throws IOException, NullResonseListenerException;

    Response post2(String url, Map<String, String> params, boolean
            encryptResponse) throws IOException, NullResonseListenerException;

    void apost(String url, Map<String, String> params, final OnHttpResonseListener httpResonseListener) throws
            IOException, NullResonseListenerException;

    void apost2(String url, Map<String, String> params, final OnHttpResonseListener httpResonseListener, boolean
            encryptResponse)
            throws
            IOException, NullResonseListenerException;

    Response uploadFile(String url, UpFileInfo upFileInfo, Map<String, String> params
    ) throws IOException;

    void auploadFile(String url, UpFileInfo upFileInfo, Map<String, String> params,
                     OnHttpResonseListener
                             httpResonseListener) throws IOException, NullResonseListenerException;
    
    void cancel(String url);

}
