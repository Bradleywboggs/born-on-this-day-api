import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SendHTTPRequestBornOnThisDay {
    public static void main(String[] args) throws IOException, JSONException{
        System.out.println(onThisDay("12","25"));
    }

    public static JSONArray onThisDay(String mm, String dd) throws IOException, JSONException {
        String url = "https://en.wikipedia.org/api/rest_v1/feed/onthisday/births/" + mm + "/" + dd;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection(); //Handle the possible IO E
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("Api-User-Agent", "bradleywboggs@gmail.com");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' Request to URL: " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        JSONObject initJSONObject = new JSONObject(response.toString());
        JSONArray initArray = (JSONArray) initJSONObject.get("births");

        JSONArray myArray = new JSONArray();
        for (int i = 0; i < initArray.length(); i++) {
            JSONObject myObject = new JSONObject();
            JSONObject arrayElement = (JSONObject) initArray.opt(i);

            if(arrayElement.has("year")) {
                Integer year = (Integer) arrayElement.opt("year");
                myObject.put("year", year);
            }

            JSONArray pagesArray = (JSONArray) arrayElement.opt("pages");
            for (int j = 0; j < pagesArray.length(); j++) {
                JSONObject pagesElement = (JSONObject) pagesArray.opt(j);

                if (pagesElement.has("extract")) {
                    String description = (String) pagesElement.opt("extract");
                    myObject.put("description", description);
                }

                if (pagesElement.has("description")) {
                    String tagline = (String) pagesElement.opt("description");
                    myObject.put("tagline",tagline);
                }

                if (pagesElement.has("titles")) {
                    JSONObject titlesObject = (JSONObject) pagesElement.opt("titles");
                    if (titlesObject.has("display")) {
                        String name = (String) titlesObject.opt("display");
                        myObject.put("name", name);
                    }
                }

                if (pagesElement.has("thumbnail")) {
                    JSONObject thumbnailObject = (JSONObject) pagesElement.opt("thumbnail");
                    String imageUrl = (String) thumbnailObject.opt("source");
                    myObject.put("image_url", imageUrl);
                }


            }


            myArray.put(myObject);

        }
        return myArray;
    }
}






