package com.ladddd.mylib.netrequest;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ladddd.mylib.config.AppConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 陈伟达 on 2017/3/7.
 */

public class HttpClientProvider {

    //测试用证书
    private static String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb2\n" +
            "9bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6\n" +
            "D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHle\n" +
            "tne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDov\n" +
            "LzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Pt\n" +
            "x1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV\n" +
            "23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQ\n" +
            "og555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void initOkHttpClient() {
        if (null == mOkHttpClient) {
            synchronized (HttpClientProvider.class) {
                if (null == mOkHttpClient) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            // 打印日志
                            Log.d("okhhtp", message);
                        }
                    });

                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    //todolist-> 添加对缓存文件管理
                    Cache cache = new Cache(new File(AppConfig.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    KeyStore keyStore = getKeyStore(CER_12306);
                    TrustManager[] trustManagers = getTrustManagers(keyStore);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
//                            .addInterceptor(new SampleRequestInterceptor()) //请求签名
//                            .addInterceptor(new SampleResponseInterceptor()) //校验服务器时间
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(new StethoInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
//                            .sslSocketFactory(getSSLSocketFactory(trustManagers), transformTrustManager(trustManagers))
//                            .hostnameVerifier(new HostnameVerifier() {
//                                @Override
//                                public boolean verify(String hostname, SSLSession session) {
//                                    return hostname.equals("test.com");
//                                }
//                            })
                            .build();
                }
            }
        }
    }

    //验证自签名证书 证书可以放在asserts中，可以用jni接口提供
    private static KeyStore getKeyStore(String cerString) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            //base64String-->inputStream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(cerString.getBytes());
            keyStore.setCertificateEntry("ce", certificateFactory.generateCertificate(inputStream));
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keyStore;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TrustManager[] getTrustManagers(KeyStore keyStore) {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager transformTrustManager(TrustManager[] trustManagers) {
        if (trustManagers == null || trustManagers.length <= 0) {
            return null;
        }
        final X509TrustManager origTrustManager = (X509TrustManager) trustManagers[0];
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return origTrustManager.getAcceptedIssuers();
            }
        };
    }

    //使用双向证书验证时需要配置客户端证书
    private static KeyManager[] getKeyManagers(String localCerString) {
        try {
            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            ByteArrayInputStream clientInputStream = new ByteArrayInputStream(localCerString.getBytes());
            clientKeyStore.load(clientInputStream, "123456".toCharArray()); //证书名 密码
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, "123456".toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SSLSocketFactory getSSLSocketFactory(TrustManager[] trustManagers) {
        try {

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);

//            sslContext.init(getKeyManagers("123456"), getTrustManagers(keyStore), new SecureRandom());

            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
