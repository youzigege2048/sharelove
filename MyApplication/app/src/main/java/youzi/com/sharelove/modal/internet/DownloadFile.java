package youzi.com.sharelove.modal.internet;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by youzi on 2016/6/10.
 */
public class DownloadFile {

    public static boolean Download(final String UrlAdress, final String fileName) {
        new Thread() {
            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    URL url;
                    try {
                        File destDir = new File(Environment.getExternalStorageDirectory() + "/ShareLoveDir");
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                        File file = new File(Environment.getExternalStorageDirectory() + "/ShareLoveDir/" + fileName);
                        Log.d("!!!!!", file.getPath());
                        if (file.exists()) {
                            return;
                        }
                        url = new URL(UrlAdress);
                        Log.d("!!!!!!", UrlAdress);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(20000);
                        InputStream is = conn.getInputStream();
                        Log.d("!!!!!!", UrlAdress);
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            System.out.println(len);
                        }
                        fos.close();
                        bis.close();
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Download(UrlAdress, name);
                        return;
                    }
                }
            }
        }.start();
        return true;
    }

    public static boolean DownloadTXT(final String UrlAdress, final String fileName) {
        new Thread() {
            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader buffer = null;
                    URL url = null;
                    String line = null;
                    try {
                        //创建一个URL对象
                        url = new URL(UrlAdress);
                        //根据URL对象创建一个Http连接
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        //使用IO读取下载的文件数据
                        buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line = buffer.readLine()) != null) {
                            sb.append(line + "\r\n");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            buffer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String content = sb.toString();
                    FileOutputStream fos = null;
                    try {
                        File saveFile = new File(Environment.getExternalStorageDirectory() + "/ShareLoveDir/", fileName);
                        fos = new FileOutputStream(saveFile);
                        System.out.println(content);
                        fos.write(content.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return true;
    }
}
