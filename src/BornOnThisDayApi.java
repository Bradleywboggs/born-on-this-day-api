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


public class BornOnThisDayApi {

    /**
     * BornOnThisDay.getResponse()
     * Sends HTTP request for 'births' to Wikimedia's 'On this day' api. The response is a JSON object
     * with a value of a JSON Array representing people born on the given day.
     * The response is read in and converted to a String
     *
     * @param mm a two-digit integer representing the two-digit month given by the user to be parsed as a String and appended
     *           to the url path.
     *
     * @param dd a two-digit integer representing the two-digit day given by the user to be parsed as a String and appended
     *           to the url path.
     *
     * @return response.toString()
     *
     * @author Bradley Boggs, bradleywboggs@gmail.com
     */

    public static String getResponse(int mm, int dd) {

        try{

            String url = String.format("https://en.wikipedia.org/api/rest_v1/feed/onthisday/births/%s/%s", mm, dd);
            // Instantiate URL object
            URL obj = new URL(url);

            // Open Connection
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            // Add request header
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

        } catch (MalformedURLException | ProtocolException | IOException e) {
            e.printStackTrace();

            return String.format("Exception: %s", e.toString());
        }

}
    /**
     * BornOnThisDay.parseAndFilterResponse() parses and filters the return value of getResponse()
     * to produce a new JSON Array to be used to render the front end.
     *
     * @param response - the return value of getResponse()
     *
     * @return JSONArray of objects with the following key-value pairs:
     * "name"-name of the person, "year"-the year of their birth, "tagline"--a phrase describing the person,
     * "description" - a 2-3 sentence description of the person, "imageUrl"-the url of a thumbnail image of the person
     *
     * @author Bradley Boggs, bradleywboggs@gmail.com
     */

    public static JSONArray parseAndFilterResponse(String response) {

        try {

            // Parse response string as JSONObject
            JSONObject initJSONObject = new JSONObject(response);
            JSONArray initArray = (JSONArray) initJSONObject.get("births");


            // Instantiate a JSONArray for the return value
            JSONArray returnArray = new JSONArray();

            for (int i = 0; i < initArray.length(); i++) {

                //Instantiate a JSONObject as an accumulator
                JSONObject returnArrayElement = new JSONObject();

                JSONObject arrayElement = (JSONObject) initArray.opt(i);

                // Iterate through the JSON Objects checking for a desired key, and if present, store the value with a new key in the accumulator
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

                // Append the accumulator JSONOBject to the returnArray
                returnArray.put(returnArrayElement);

            }

            return returnArray;

        } catch (JSONException e) {
            e.printStackTrace();
            JSONArray emptyArray = new JSONArray();

            return emptyArray;

        }

    }
}






