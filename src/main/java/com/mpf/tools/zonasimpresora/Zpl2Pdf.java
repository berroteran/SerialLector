package com.mpf.tools.zonasimpresora;


import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * zpl command to pdf
 */
public class Zpl2Pdf {

    public static void main(String[] args) {
        File file = new File("label.pdf");

        String zpl = "CT~~CD,~CC^~CT~^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR3,3~SD28^JUS^LRN^CI0^XZ^XA^MMT^PW695^LL0280^LS0^FO352,160^GFA,01152,01152,00012,:Z64:eJztj7FtwzAQRT9pGRJYqUjgxgZdCq5TptAIatJnBBWC1RggRxFcETdFVsgmLl0IUOTk7qQNUiS/enwk/5HAf34nxwW3b/VKLxs1MuUIK7g5lCbKwiUIZz7UUlRYGqSoNOHeav0piW/xUj6pr1Do2Ffsdax68/DZyvODTPQjcvGO1GPXN+pBSc+Xu1F9AfXwWPVAez7W3kY4eY95XzwG9TeExrMfQIP41oRO/nW011QxX/xnc2GuXERiPj/Pd5hP310/8RH5jdnNYyNz3iHUzJZA4s00TZAQkXLfd8pWjz8u4K/kCwjBNFw=:F8AD^FO544,160^GFA,00768,00768,00008,:Z64:eJztkbFtwzAQRT/p0AoEB1CTLkBUGtwhAEdw4y6BV3Dh0gY4CkvhptAIaoJUKryBAri1Zd6XMoHbHAh8PH7e8XgE/mOOOMnr9g+5YYBK1QLPqqtDWNL3yapUp6tRrb1ETQhuPLdMl5TZtPjuAjmhZrkBe5YjO+Vq5nzBEocBb8qe/ISvPpBLifTdOJBrm+jvzeSv2WCBI6b8Errl8DKzjcpFWFz0SCah35prq/3EUlJS3v38Drnf5KOy6T7huszNWnOA4waFvs/npfwRcdL3lykXzPF+KW6cl0jD+fV9x3mJJOpuDFTbTHM2m4d/7oG4AwAhQRY=:C6CC^FO192,160^GFA,00768,00768,00008,:Z64:eJzlkTFqAzEQRf8oGyuocrFkUxjSBlcpU65v4Ca4tI/ggGFL6ygqlzmFr2AI5BoOBFyFzfwdN2kDqfwZ+DwN+oxGwNUpXPx1eUE/qCAt/Q7I9LcjpmNfeQQszu81PauSBc0w8evKgAniKjpnBlS4yXJgnHON+Isf5MM56MhT6XawAZ6gigK8YDXIlpwT+VkOtyAHA6uZnGBl/X7kGjvIhvnJ+3hcMj8h9My/Rzw7z9UmQCPDOhqHrCEZx8UWDecvhQmQzxZ7vrcfC/LFMs0ZaNof1yd6Ui302HWt70vHfcrw7XtOxb1q//Jb/6wfQRs9bQ==:060F^FO0,192^GFA,01152,01152,00012,:Z64:eJztjzFOAzEQRb83axFppTXFbkMRpYxygihIsEdwYV8HuUQUHCFyiahSRpti9yiUOQJCaIMRM+McASSmen4z+jMG/mhdZyxe44XOjSUK4Q6KUT0/YuSHdqlFMWaIS+JSe8tBcwyfG4lvHfsNbvq5+AalrF2glrXZx+SLC88HjfUOs0A+3cM+VMcX8cpbmccwiS/hZL7GKudAct5MyPmqg+Z7VMweVvwJD8GQt2g79gfcH/hfVnnXsK8+pjviRtuG/bYK6Ilb/Z31U+YJeCfW6Rz2V3uYSKxcWkw8m8J5JIb3nhG3x1649U64Op+EkceBNf7rl9UXFGw0lw==:0DFA^FT412,135^BQN,2,4^FH\\^FDLA,7150000036^FS^FT574,127^BQN,2,4^FH\\^FDLA,7150000036^FS^FT522,124^A0B,14,14^FH\\^FD1234567891123^FS^FT234,136^BQN,2,4^FH\\^FDLA,7150000036^FS^FT685,117^A0B,14,14^FH\\^FD1234567891123^FS^FT54,133^BQN,2,4^FH\\^FDLA,7150000036^FS^FT350,128^A0B,14,14^FH\\^FD1234567891123^FS^FT173,126^A0B,14,14^FH\\^FD1234567891123^FS^PQ1,0,1,Y^XZ";

        String url = "http://api.labelary.com/v1/printers/8dpmm/labels/3.42x1.37/0/";

        for (int i = 0; i < 2; i++) {
            //getPdf(url,file,zpl);
            url = "http://api.labelary.com/v1/printers/8dpmm/labels/3.42x1.37/1/";
        }

    }


    public static List<InputStream> bitchGetPdf(String url, List<String> zpls) {
        return bitchSend(url,zpls);
    }

    private static List<InputStream> bitchSend(String url0, List<String> zpls) {
        List<InputStream> inputStreamList = new ArrayList<>(zpls.size());
        zpls.forEach(zpl -> inputStreamList.add(send(url0,zpl)));
        return inputStreamList;
    }

    public static InputStream getPdf(String url, String zpl) {
        return send(url,zpl);
        /*try {
                         // Get http client
            CloseableHttpClient client = HttpClients.createDefault();
            // get method
            HttpPost httpPost = new HttpPost(url);

//            URIBuilder uriBuilder = new URIBuilder(url);
                         // The first form of adding parameters
//            uriBuilder.addParameter("name", "root");
//            uriBuilder.addParameter("password", "123456");
                         // The second form of adding parameters
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("mode", "raw");
            BasicNameValuePair param2 = new BasicNameValuePair("raw", zpl);
            list.add(param1);
            list.add(param2);

            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
//            uriBuilder.setParameters(list);
                         // Realize our get request through httpget
//            HttpPost httpPost = new HttpPost(uriBuilder.build());

                         // Type of transmission
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
//            httpPost.addHeader("Content-Length", String.valueOf(zpl.length()));
            httpPost.addHeader("Accept", "application/pdf");
                         // Call the execute method through the client, and get our execution result as a response, all data is encapsulated in the response

            CloseableHttpResponse response = client.execute(httpPost);

            // HttpEntity
                         // It is an intermediate bridge. In httpClient, it is an intermediate bridge connecting our request and response. All request parameters are carried through HttpEntity
                         // All response data is also encapsulated in HttpEntity
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                return entity.getContent();
            } else {
                throw new RuntimeException(response.getStatusLine().toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new CommonException(ex);
        }*/
    }

    private static InputStream send(String url0, String param) {
        URL url;
        HttpURLConnection connection;
        try {
            //Create connection
            url = new URL(url0);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept","application/pdf");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            //POST request
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject body = new JSONObject();
            body.put("mode", "raw");
            body.put("raw", param);

            out.write(body.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
            return connection.getInputStream();
            //Read response
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();*/
        } catch (IOException e) {
            throw new RuntimeException("Network timeout, please try again:" + e.toString());
        }
    }

    public static String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }

    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (byte[] value : values) {
            length_byte += value.length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

}
