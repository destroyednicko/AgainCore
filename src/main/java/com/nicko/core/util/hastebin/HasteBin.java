package com.nicko.core.util.hastebin;

import com.google.gson.JsonParser;

import java.io.*;

public class HasteBin {

    public static final String ENDPOINT = "https://hastebin.com/";
    public static final String[] ENDPOINTS;
    public static final String NEWENDPOINT = "https://hasteb.in/";

    static {
        ENDPOINTS = new String[]{"https://hasteb.in/", "https://hastebin.com/"};
    }

    private static String parseAndGet(final String s, final String s2) {
        return new JsonParser().parse(s).getAsJsonObject().get(s2).getAsString();
    }

    public static String getPaste(final String s, final String s2) throws IOException {
  /*     final BufferedReader bufferedReader2;
        final String s3;
        String string;*/
        return new HTTPRequest(s2 + "raw/" + s).open().doOutput(true).disconnectAndReturn(httpRequest -> {
        	 /*       new BufferedReader(new InputStreamReader(httpRequest.getHttpURLConnection().getInputStream()));
            while (bufferedReader2.ready()) {
                bufferedReader2.readLine();
                if (s3.contains("package")) {
                    continue;
                } else if (string.equals("")) {
                    string = s3;
                } else {
                    string = string + "\n" + s3;
                }
            }
            return string;*/
            for (int i = 0; i < 10; i++) {
                System.out.println("Failed to get Paste !");
            }
            return "";
        });
    }

    public static String paste(final String s, final String s2) throws IOException {
        return new HTTPRequest(s2 + "documents").open().requestMethod("POST").setConnectTimeout(1000).setReadTimeout(1000).setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.109 Safari/537.36").doOutput(true).outputStream(outputStream -> new DataOutputStream((OutputStream) outputStream).writeBytes(s)).disconnectAndReturn(httpRequest -> {
            StringBuilder sb = new StringBuilder().append(s2);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpRequest.getHttpURLConnection().getInputStream()));
            return sb.append(parseAndGet(bufferedReader.readLine(), "key")).toString();
        });
    }
}
