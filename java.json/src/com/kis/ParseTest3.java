package com.kis;

import java.io.FileReader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
/**
 * https://crunchify.com/how-to-read-json-object-from-file-in-java/
 */
 
public class ParseTest3 {
 
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
 
        try {
        /*	
              JsonParser jsonParser = new JsonParser();
              JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(
                      "D:\\nfs01\\DevHome\\Home.JSON\\curl01\\sample01.json"));
              System.out.print("name : " + jsonObject.get("name"));
      */
        	
        	JsonParser jsonParser = new JsonParser();

        	JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(
                    "D:\\nfs01\\DevHome\\Home.JSON\\curl01\\sample01.json"));
        	JsonObject dataObject = (JsonObject) jsonObject.get("data");

        	System.out.print("name : " + dataObject.get("name"));
        	System.out.print("age : " + dataObject.get("age"));
        	System.out.print("birth : " + dataObject.get("birth"));	
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}