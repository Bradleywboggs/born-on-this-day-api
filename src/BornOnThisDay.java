import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class BornOnThisDay {

    /**
     * BornOnThisDay.getResponse()
     * Sends HTTP request for 'births' to Wikimedia's 'On this day' api. The response is a JSON object,
     * with a value of a JSON Array representing people born on the given day.
     * The response is read in and converted to a String
     *
     * @param mm a two-character String representing the two digit month given by the user to be appended
     *           to the url path.
     * @param dd a two character String representing the two digit day given by the user to be appended
     *           to the url path
     * @return response.toString()
     * @author Bradley Boggs, bradleywboggs@gmail.com
     */

    public static String getResponse(String mm, String dd) {
        try{
            String url = String.format("https://en.wikipedia.org/api/rest_v1/feed/onthisday/births/%s/%s", mm, dd);
            //Instantiated URL object
            URL obj = new URL(url);
           // Open Connection
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Api-User-Agent", "sean.zicari@gmail.com");
            int responseCode = con.getResponseCode();

            System.out.println("\nSending 'GET' Request to URL: " + url);
            System.out.println("Response Code : " + responseCode);


            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            // Read in line
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);

            }
            in.close();
            // Parse response as String
            return response.toString();

        } catch (MalformedURLException m) {
            m.printStackTrace();
            return String.format("MalformedUrlException: %s", m.toString());
        } catch (ProtocolException p) {
            p.printStackTrace();
            return String.format("ProtocolException: %s", p.toString());
        } catch (IOException i) {
            i.printStackTrace();
            return String.format("IO Exception: %s", i.toString());
        }

}
    /**
     * BornOnThisDay.convertAndFilterResponse() parses and filters the return value of getResponse()
     * to produce a new JSON Array to be used to render the front end.
     *
     * @param response the return value of getResponse()
     *
     * @return JSONArray of objects with the following key-value pairs:
     * "name"-name of the person, "year"-the year of their birth, "tagline"--a phrase describing the person,
     * "description" - a 2-3 sentence description of the person, "imageUrl"-the url of a thumbnail image of the person
     *
     * @author Bradley Boggs, bradleywboggs@gmail.com
     */

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






