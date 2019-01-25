import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class BornOnThisDay {
    public static void main(String[] args) throws IOException, JSONException {
        System.out.println(convertAndFilterResponse(getResponse("04", "08")));
    }



    public static String getResponse(String mm, String dd) {

        try {
            String url = "https://en.wikipedia.org/api/rest_v1/feed/onthisday/births/" + mm + "/" + dd;
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Api-User-Agent", "sean.zicari@gmail.com");
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
            return response.toString();


        } catch (IOException e) {
            e.printStackTrace();
            // In case of failure, should I retry?
            return "IO Exception: "+ e.toString();
        }

    }


    public static JSONArray convertAndFilterResponse(String response) {
        try {
            JSONObject initJSONObject = new JSONObject(response);
            JSONArray initArray = (JSONArray) initJSONObject.get("births");
            JSONArray returnArray = new JSONArray();

            for (int i = 0; i < initArray.length(); i++) {
                JSONObject returnArrayElement = new JSONObject();
                JSONObject arrayElement = (JSONObject) initArray.opt(i);

                if (arrayElement.has("year")) {
                    Integer year = (Integer) arrayElement.opt("year");
                    returnArrayElement.put("year", year);
                }

                JSONArray pagesArray = (JSONArray) arrayElement.opt("pages");
                for (int j = 0; j < pagesArray.length(); j++) {
                    JSONObject pagesElement = (JSONObject) pagesArray.opt(j);

                    if (pagesElement.has("extract")) {
                        String description = (String) pagesElement.opt("extract");
                        returnArrayElement.put("description", description);
                    }

                    if (pagesElement.has("description")) {
                        String tagline = (String) pagesElement.opt("description");
                        returnArrayElement.put("tagline", tagline);
                    }

                    if (pagesElement.has("titles")) {
                        JSONObject titlesObject = (JSONObject) pagesElement.opt("titles");
                        if (titlesObject.has("display")) {
                            String name = (String) titlesObject.opt("display");
                            returnArrayElement.put("name", name);
                        }
                    }

                    if (pagesElement.has("thumbnail")) {
                        JSONObject thumbnailObject = (JSONObject) pagesElement.opt("thumbnail");
                        String imageUrl = (String) thumbnailObject.opt("source");
                        returnArrayElement.put("imageUrl", imageUrl);
                    }


                }
                
                returnArray.put(returnArrayElement);

            }
            return returnArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;

        }

    }
}






