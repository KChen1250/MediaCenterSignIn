package com.fourfront.mediacentersignin.helper;

import java.net.URLEncoder;

/**
 * Helper class to send data to Google sheets
 *
 * Source:
 * https://github.com/FoamyGuy/GoogleFormUploadExample
 *
 * @author Kevin Chen
 *
 * Created May 2017, finished June 2017
 * Poolesville High School Client Project
 */
public class SendData {
    public static void postData(String col1, String col2, String col3, String col4, String col5) {
        String fullUrl = Config.FORM_LINK;
        HttpRequest mReq = new HttpRequest();

        String data = Config.ENTRY_ID + URLEncoder.encode(col1) + "&" +
                      Config.ENTRY_NAME + URLEncoder.encode(col2) + "&" +
                      Config.ENTRY_SENDER + URLEncoder.encode(col3) + "&" +
                      Config.ENTRY_SUBSTITUTE + URLEncoder.encode(col4) + "&" +
                      Config.ENTRY_REASON + URLEncoder.encode(col5);
        mReq.sendPost(fullUrl, data);
    }
}
