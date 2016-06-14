package youzi.com.sharelove.modal.internet;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class NetConnection {

    public NetConnection(final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final Form form) {

        new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... arg0) {

                final HashMap<String, String> map = new HashMap<String, String>();

                StringBuffer paramsStr = new StringBuffer();
                Iterator iter = form.getForm().entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                try {
                    URL url = new URL(form.url);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    final URLConnection connection;

//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
//                    connection.setRequestMethod("POST");
//
//                    connection.setUseCaches(false);
//                    // 自动处理重定向
//                    connection.setInstanceFollowRedirects(false);
//
//                    connection.connect();
//                    OutputStreamWriter out = new OutputStreamWriter(
//                            connection.getOutputStream(), Config.CHARSET);
//
//                    String data = paramsStr.toString();
//
//                    out.write(data);
//
//                    out.flush();
//                    out.close();
                    switch (method) {
                        case POST:
                            connection = new URL(form.url).openConnection();
                            connection.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            break;
                        default:
                            connection = new URL(url + "?" + paramsStr.toString()).openConnection();
                            break;
                    }
                    //System.out.println(connection.getHeaderFields());
                    HttpURLConnection conn = (HttpURLConnection) connection;
                    //System.out.println(conn.getResponseCode());

                    if (conn.getResponseCode() != 200) {
                        map.put("code", conn.getResponseCode() + "");
                        return map;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "UTF-8"));

                    String line;
                    String result = "";
                    while ((line = reader.readLine()) != null) {
                        result += line;
                       // System.out.println(line);
                    }

                    reader.close();
                    conn.disconnect();
                    map.put("code", conn.getResponseCode() + "");
                    map.put("result", result);
                    return map;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> result) {

                if (result.get("code").equals("200")) {
                    if (successCallback != null) {
                        successCallback.onSuccess(result.get("result"));
                    }
                } else {
                    if (failCallback != null) {
                        failCallback.onFail(result.get("code"));
                    }
                }
                //super.onPostExecute(result);
            }
        }.execute();

    }

    public void temp(final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final Form form) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... arg0) {

                StringBuffer paramsStr = new StringBuffer();
                Iterator iter = form.getForm().entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                try {
                    final URLConnection uc;

                    switch (method) {
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), "UTF-8"));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            break;
                        default:
                            uc = new URL(url + "?" + paramsStr.toString()).openConnection();
                            break;
                    }

                    System.out.println("Request url:" + uc.getURL());
                    System.out.println("Request data:" + paramsStr);

                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
                    String line = null;
                    System.out.println(" xxx " + uc.hashCode());
                    System.out.println(uc.getHeaderFields());

                    StringBuffer result = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println("Result:" + result.toString());
                    return result.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {

                if (result != null) {
                    if (successCallback != null) {
                        successCallback.onSuccess(result);
                    }
                } else {
                    if (failCallback != null) {
                        failCallback.onFail(result);
                    }
                }
                //super.onPostExecute(result);
            }
        }.execute();

    }


    public static interface SuccessCallback {
        void onSuccess(String result);
    }

    public static interface FailCallback {
        void onFail(String statuscode);
    }
}
