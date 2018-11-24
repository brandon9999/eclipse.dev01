package com.kis;

import java.io.FileReader;
import java.util.Iterator;
 
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
*/

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
/**
 * https://crunchify.com/how-to-read-json-object-from-file-in-java/
 */
 
public class ParseTest4 {
 
	
/*	
빠른주석	ctrl + shift + /   or ctrl + /
주석해제		ctrl + shift + \         or ctrl + /
	
	*/
	
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
 
        try {
        	JsonParser jsonParser = new JsonParser();

        	JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(
                    "D:\\nfs01\\DevHome\\Home.JSON\\curl01\\response.json"));

        	JsonObject dataObject = (JsonObject) jsonObject.get("jeusadmin-result");

          	System.out.print("message : " + dataObject.get("message"));
           	System.out.print("\n");
          	System.out.print("data : " + dataObject.get("data"));
           	System.out.print("\n");

           	
        	JsonParser jsonParser2 = new JsonParser();
           	Object obj2 = jsonParser2.parse(dataObject.get("data").toString());
           	JsonArray jsonArray = (JsonArray) obj2;
           	for(int i=0;i<jsonArray.size();i++)
           	{
           		JsonObject jsonObj = (JsonObject)jsonArray.get(i);
           		System.out.print("title : " + jsonObj.get("title")+"\n");
           		System.out.print("header : " +jsonObj.get("header")+"\n");
           		System.out.print("column-names : " +jsonObj.get("column-names")+"\n");
           		System.out.print("rows : " +jsonObj.get("rows")+"\n");

           		//System.out.print(jsonArray.get(i));
       		}


//           	
//        	System.out.print("\n");
//        	System.out.print("jsonArray size : " + jsonArray.size());
//        	System.out.print("\n");
//        	System.out.print("jsonArray toString: " + jsonArray.toString());
            
/*        	
        	for(int i=0; i<jsonArray.size(); i++)
        	{ 
        		System.out.println("result :: " +jsonArray. get("title")); 
        	}

        	*/
        	
        	/*
        	System.out.print("name : " + dataObject.get("name"));
        	System.out.print("age : " + dataObject.get("age"));
        	System.out.print("birth : " + dataObject.get("birth"));	
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}