package com.example.socialmedia.Activity.Notification;

import com.example.socialmedia.Model.NotificationModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationHelper {
    private  static  DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Notifications");

    public static void  createNotifcation(String msg, String uid, String otherUserId, String type, String postId, String token){
        String key = mref.push().getKey();

        NotificationModel model = new NotificationModel(
                key , postId , uid , otherUserId , msg, type , System.currentTimeMillis()/1000
        ) ;

        mref.child(key).setValue(model);


    //   send(token , "test");

    }

    public  static  void  send(String to,  String body) {
        try {
            final String apiKey = "AAAASUoQmlM:APA91bEw9iY6KbKYP4_CtaMDlEGF40nKCMfbUSVwoDb1_x9JhybwaMr4hFUX5KiFXnG0Po1CStIvdVqMZlCVYGjGCN89fG_pgsTG0-0C72a0BUXZXBOrascUhDe89q38hhJoHumbj57E";
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            conn.setDoOutput(true);
            JSONObject message = new JSONObject();
            message.put("to", to);
            message.put("priority", "high");

            JSONObject notification = new JSONObject();
            // notification.put("title", title);
            notification.put("body", body);
            message.put("data", notification);
            message.put("notification", notification);
            OutputStream os = conn.getOutputStream();
            os.write(message.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + message.toString());
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Code : " + conn.getResponseMessage());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void SentNotification() {
    }


}
