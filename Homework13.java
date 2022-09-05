package org.example;

import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.*;

public class Homework13 {

    final String mainURL = "https://jsonplaceholder.typicode.com";

    // допоміжні методи
    public String jsonMaker(String id, String name, String username, String email, String street, String suite,
                            String city, String zip, String lat, String lng, String phone, String website,
                            String compName, String catchphrase, String bs) {

        Map<String, Object> hashmap = new LinkedHashMap<>();
        Map<String, Object> addressHashmap = new LinkedHashMap<>();
        Map<String, Object> geoHashmap = new LinkedHashMap<>();
        Map<String, Object> companyHashmap = new LinkedHashMap<>();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        geoHashmap.put("lat", lat);
        geoHashmap.put("lng", lng);

        addressHashmap.put("street", street);
        addressHashmap.put("suite", suite);
        addressHashmap.put("city", city);
        addressHashmap.put("zip", zip);
        addressHashmap.put("geo", geoHashmap);

        companyHashmap.put("name", compName);
        companyHashmap.put("catchphrase", catchphrase);
        companyHashmap.put("bs", bs);

        hashmap.put("id", id);
        hashmap.put("name", name);
        hashmap.put("username", username);
        hashmap.put("email", email);
        hashmap.put("address", addressHashmap);
        hashmap.put("phone", phone);
        hashmap.put("website", website);
        hashmap.put("company", companyHashmap);

        return gson.toJson(hashmap);
    }
    public String getUsersTodos(int userID) throws IOException {

        BufferedReader reader;
        String line;
        StringBuilder responseAllUserInfo = new StringBuilder();

        URL url = new URL(mainURL + "/users/" + userID + "/todos");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int status = con.getResponseCode();

        if (status > 300) {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        else {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        while ((line = reader.readLine()) != null) {
            responseAllUserInfo.append(line);
        }
        reader.close();
        con.disconnect();

        return responseAllUserInfo.toString();

    }

    //головні методи
    public void create(String jsonToInput) throws IOException {
        URL createUrl = new URL(mainURL + "/users/");
        HttpURLConnection createCon = (HttpURLConnection) createUrl.openConnection();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        createCon.setRequestMethod("POST");
        createCon.setConnectTimeout(10000);
        createCon.setReadTimeout(10000);
        createCon.setRequestProperty("Content-Type", "application/json");
        createCon.setRequestProperty("Accept", "application/json");

        createCon.setDoOutput(true);

        OutputStream os = createCon.getOutputStream();
            byte[] input = jsonToInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);


        BufferedReader br = new BufferedReader(
                new InputStreamReader(createCon.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        System.out.println("created " + response);
        System.out.println("created with code " + createCon.getResponseCode());

        createCon.disconnect();
    }
    public void update(String jsonToUpdate) throws IOException{

        URL updateUrl = new URL(mainURL + "/users/");
        HttpURLConnection updateCon = (HttpURLConnection) updateUrl.openConnection();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        updateCon.setRequestMethod("POST");
        updateCon.setConnectTimeout(10000);
        updateCon.setReadTimeout(10000);
        updateCon.setRequestProperty("Content-Type", "application/json");

        updateCon.setDoOutput(true);

        OutputStream os = updateCon.getOutputStream();
        byte[] input = jsonToUpdate.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        System.out.println("updated with code " + updateCon.getResponseCode());

        updateCon.disconnect();
    }
    public void delete(int userID) throws  IOException{
        URL deleteUrl = new URL(mainURL + "/users/" + userID);
        HttpURLConnection deleteCon = (HttpURLConnection) deleteUrl.openConnection();
        deleteCon.setRequestMethod("DELETE");
        deleteCon.setConnectTimeout(10000);
        deleteCon.setReadTimeout(10000);

        System.out.println("deleted with code " + deleteCon.getResponseCode());

        deleteCon.disconnect();
    }
    public String getAllUsersInfo() throws IOException {

        BufferedReader reader;
        String line;
        StringBuilder responseAllUserInfo = new StringBuilder();

        URL url = new URL(mainURL + "/users");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int status = con.getResponseCode();

        if (status > 300) {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        else {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        while ((line = reader.readLine()) != null) {
            responseAllUserInfo.append(line);
        }
        reader.close();
        System.out.println("completed with code " + con.getResponseCode());
        con.disconnect();

        return responseAllUserInfo.toString();
    }
    public String getUserInfoByID(int idValue) throws IOException {

        String allUsersInfo = getAllUsersInfo();
        String infoByID = null;

        JsonParser parser = new JsonParser();
        JsonElement jElement = parser.parse(allUsersInfo);
        JsonArray jArray = jElement.getAsJsonArray();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject json = jArray.get(i).getAsJsonObject();
            if (json.get("id").getAsInt() == idValue) {
                infoByID = String.valueOf(json.getAsJsonObject());
            }
        }

        return infoByID;
    }
    public String getInfoByUsername(String username) throws IOException {

        String allUsersInfo = getAllUsersInfo();
        String infoByUserName = null;

        JsonParser parser = new JsonParser();
        JsonElement jElement = parser.parse(allUsersInfo);
        JsonArray jArray = jElement.getAsJsonArray();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject json = jArray.get(i).getAsJsonObject();
            if(json.get("username").getAsString().equals(username)) {
                infoByUserName = json.toString();
            }
        }

        return infoByUserName;
    }
    public void allCommentsToLastPostOfUserByID (int userID) throws IOException {

        BufferedReader reader1;
        String line1;
        StringBuilder allUserPosts = new StringBuilder();

        URL url1 = new URL(mainURL + "/users/" + userID + "/posts");
        HttpURLConnection con = (HttpURLConnection)url1.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int status = con.getResponseCode();

        if (status > 300) {
            reader1 = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        else {
            reader1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        while ((line1 = reader1.readLine()) != null) {
            allUserPosts.append(line1);
        }
        reader1.close();
        con.disconnect();
        String allUserPost = String.valueOf(allUserPosts);


        JsonObject postByID = null;

        JsonParser parser = new JsonParser();
        JsonElement jElement = parser.parse(allUserPost);
        JsonArray jArray = jElement.getAsJsonArray();

        JsonObject firstPost = jArray.get(0).getAsJsonObject();
        int firstPostID = firstPost.get("id").getAsInt();
        int temp = firstPostID-1;

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject json = jArray.get(i).getAsJsonObject();
            if (json.get("id").getAsInt() != temp) {
                postByID = json.getAsJsonObject();
            }
        }

        BufferedReader reader2;
        String line2;
        StringBuilder allCommentsToPost = new StringBuilder();

        assert postByID != null;
        URL url2 = new URL(mainURL + "/posts/" + postByID.get("id") + "/comments");
        HttpURLConnection con2 = (HttpURLConnection)url2.openConnection();
        con2.setRequestMethod("GET");
        con2.setConnectTimeout(10000);
        con2.setReadTimeout(10000);

        int status2 = con2.getResponseCode();

        if (status2 > 300) {
            reader2 = new BufferedReader(new InputStreamReader(con2.getErrorStream()));
        }
        else {
            reader2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
        }
        while ((line2 = reader2.readLine()) != null) {
            allCommentsToPost.append(line2);
        }
        reader2.close();
        con2.disconnect();

        String s = FileSystems.getDefault().getSeparator();

        File file = new File("." + s + "homework_13" + s + "src" + s + "main" + s + "resources" + s + "user-" + userID + "-post-" + postByID.get("id") + "-comments.json");
        Writer writer = new FileWriter("user-" + userID + "-post-" + postByID.get("id") + "-comments.json");
        String result = String.valueOf(allCommentsToPost);
        writer.write(result);
        writer.close();

    }
    public void allUnfinishedTodosForUser(int userID) throws IOException {

        String allUserTodos = getUsersTodos(userID);
        String uncompletedTodos;

        JsonParser parser = new JsonParser();
        JsonElement jElement = parser.parse(allUserTodos);
        JsonArray jArray = jElement.getAsJsonArray();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject json = jArray.get(i).getAsJsonObject();
            if (!json.get("completed").getAsBoolean()) {
                System.out.println(uncompletedTodos = String.valueOf(json.getAsJsonObject()));
            }
        }

    }

}
