package org.revocen.cavmp.network;

import org.revocen.cavmp.util.URLConnectionHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Map;

public class ApplicationVersionConfigDownloader {

    private String downloadUrl;
    private Map<String,String> requestHeaders;

    public ApplicationVersionConfigDownloader(String downloadUrl, Map<String, String> requestHeaders) {
        this.downloadUrl = downloadUrl;
        this.requestHeaders = requestHeaders;
    }

    public String download() throws IOException {

        final StringBuilder jsonContent = new StringBuilder();

        final URLConnection urlConnection = URLConnectionHelper.createConnectionToURL(downloadUrl, requestHeaders);
        final InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
        final BufferedReader bufferedReader = new BufferedReader(streamReader);

        final char data[] = new char[1024];
        int count;
        while ((count = bufferedReader.read(data)) != -1) {
            jsonContent.append(data, 0, count);
        }
        bufferedReader.close();

        return jsonContent.toString();
    }
}
