/**
 * Copyright (c) 2015 Medialog Corp. All Rights Reserved.
 *
 * @FileName : RequestVO.java
 * @Package  : com.dmi.smartux.common.vo
 * @Author   : DEOKSAN
 * @Date     : 2016. 6. 15. 오후 4:55:46
 * @Comment  :
 */

package com.dmi.smartux.common.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * <pre>
 * com.dmi.smartux.common.vo
 *    |_ RequestVO.java
 * </pre>
 * @date	: 2016. 6. 15. 오후 4:55:46
 * @version	:
 * @author	: deoksan
 */
/**
 * <PRE>
 * @ClassName : RequestVO
 * @Brief     :  
 * @FileName  : RequestVO.java
 * @Package   : com.dmi.smartux.common.vo
 * @Author    : DEOKSAN
 * @Date      : 2016. 6. 15. 오후 4:55:46
 * @Comment   :
 * </PRE>
 */
public class RequestVO {

    private String mHost;
    private String mUrl;
    private String mProtocolName = "http";
    private int mPort;
    private long mTimeout = 3000;
    private int mRetryCount = 0;
    private String mMethod = "GET";
    private Map<String, String> mParams = new HashMap<String, String>();

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getProtocolName() {
        return mProtocolName;
    }

    public void setProtocolName(String protocolName) {
        mProtocolName = protocolName;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public String getHostAndPort() {
        return mHost + ":" + mPort;
    }

    public long getTimeout() {
        return mTimeout;
    }

    public void setTimeout(long timeout) {
        mTimeout = timeout;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public void setRetryCount(int retryCount) {
        mRetryCount = retryCount;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    public void putParams(String key, String value) {
        if (mParams != null && StringUtils.isNotBlank(key)) {
            mParams.put(key, value);
        }
    }

    public String getFormattedParam() {
        StrBuilder sb = new StrBuilder();

        if (mParams != null) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                sb.appendSeparator("&");
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StrBuilder sb = new StrBuilder();
        sb.append("[").append(mMethod).append("]");
        sb.append("[").append("timeout").append(":").append(mTimeout).append("]");
        sb.append("[").append("retryCount").append(":").append(mRetryCount).appendln("]");
        sb.append(mProtocolName).append("://").append(mHost).append(":").append(mPort).append(mUrl);
        sb.append("?").appendln(getFormattedParam());

        return sb.toString();
    }
}
