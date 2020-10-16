package com.nicko.core.util.hastebin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.function.Function;

public class HTTPRequest {

    private final URL url;
    private HttpURLConnection httpURLConnection;

    public HTTPRequest(final String s) throws MalformedURLException {
        url = new URL(s);
    }

    public HTTPRequest requestMethod(final String requestMethod) throws ProtocolException {
        httpURLConnection.setRequestMethod(requestMethod);
        return this;
    }

    public HTTPRequest errorStream(final Function<InputStream, ?> function) throws IOException {
        function.apply(httpURLConnection.getErrorStream());
        return this;
    }

    public HTTPRequest outputStream(final HTTPRequestConsumer httpRequestConsumer) throws IOException {
        httpRequestConsumer.accept(httpURLConnection.getOutputStream());
        return this;
    }

    public HTTPRequest open() throws IOException {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        return this;
    }

    public HTTPRequest setRequestProperty(final String s, final String s2) {
        httpURLConnection.setRequestProperty(s, s2);
        return this;
    }

    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    public HTTPRequest useCaches(final boolean useCaches) {
        httpURLConnection.setUseCaches(useCaches);
        return this;
    }

    public HTTPRequest doOutput(final boolean doOutput) {
        httpURLConnection.setDoOutput(doOutput);
        return this;
    }

    public HTTPRequest disconnect() {
        httpURLConnection.disconnect();
        return this;
    }

    public HTTPRequest doInput(final boolean doInput) {
        httpURLConnection.setDoInput(doInput);
        return this;
    }

    public HTTPRequest setReadTimeout(final int readTimeout) {
        httpURLConnection.setReadTimeout(readTimeout);
        return this;
    }

    public HTTPRequest setConnectTimeout(final int connectTimeout) {
        httpURLConnection.setConnectTimeout(connectTimeout);
        return this;
    }

    public HTTPRequest inputStream(final HTTPRequestConsumer httpRequestConsumer) throws IOException {
        httpRequestConsumer.accept(httpURLConnection.getInputStream());
        return this;
    }

    public <T> T disconnectAndReturn(final HTTPRequestFunction httpRequestFunction) throws IOException {
        return (T) httpRequestFunction.apply(this);
    }

    @FunctionalInterface
    public interface HTTPRequestConsumer {

        void accept(final Object p0) throws IOException;
    }

    @FunctionalInterface
    public interface HTTPRequestFunction {

        Object apply(final HTTPRequest p0) throws IOException;
    }
}
