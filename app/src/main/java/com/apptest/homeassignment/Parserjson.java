package com.apptest.homeassignment;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.lang.Object;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parserjson {

    JSONArray data;
    public ArrayList<HashMap> result =  new ArrayList<>();

    public Parserjson(JSONObject json) throws JSONException{
        this.data =(JSONArray) json.get("results");
        parser();
    }
    public void parser() throws JSONException{
        for (int i = 0; i < data.length(); i++) {
            HashMap<String, String> image = new HashMap<>();
            if (data.getJSONObject(i).has("urls")) {
                Map<String, Object> map_url = new Gson().fromJson(
                        data.getJSONObject(i).getJSONObject("urls").toString(), new TypeToken<HashMap<String, Object>>() {
                        }.getType()
                );
                image.put("url", (String) map_url.get("small"));
            }
            if (data.getJSONObject(i).isNull("description")){
                if (data.getJSONObject(i).isNull("alt_description")) {
                    image.put("description", "");
                }
                else{
                    image.put("description",(String) data.getJSONObject(i).get("alt_description"));
                }
            }
            else{
                image.put("description",(String) data.getJSONObject(i).get("description"));
            }
            if (data.getJSONObject(i).has("user")) {
                Map<String, Object> map_user = new Gson().fromJson(
                        data.getJSONObject(i).getJSONObject("user").toString(), new TypeToken<HashMap<String, Object>>() {
                        }.getType()
                );
                image.put("username", (String) map_user.get("username"));
                image.put("name", (String) map_user.get("name"));
                image.put("bio", (String) map_user.get("bio"));
                ObjectMapper oMapper = new ObjectMapper();
                Map<String, Object> map_profile = oMapper.convertValue(map_user.get("profile_image"), Map.class);
                image.put("profile_image", (String) map_profile.get("large"));
            }
            else{
                image.put("username", "");
                image.put("name","");
                image.put("bio", "");
                image.put("profile_image", "");
            }
            result.add(result.size(), image);
        }
    }
    public ArrayList<HashMap> get_results() {
        return result;
    }

}
