package com.kis;

import java.io.FileReader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
/**
 * https://crunchify.com/how-to-read-json-object-from-file-in-java/
 */
 
public class ParseTest3 {
 
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
 
        try {
              Object obj = parser.parse(new FileReader(
                     "C:\\DevHome\\jsontest\\jsonresponse.json"));
            
            JSONObject jsonobj = (JSONObject) obj;
            
              

          	System.out.print(jsonobj.);
              
            /*
            for(int i=0;i<jsonArray.size();i++){

            	JSONObject jsonObj = (JSONObject)jsonArray.get(i);

            	System.out.print(jsonObj.get("1"));

            	}
*/


            
/*
            Object obj = parser.parse(new FileReader(
                    "C:\\DevHome\\jsontest\\file1.json"));
 
            JSONObject jsonObject = (JSONObject) obj;

            String name = (String) jsonObject.get("Name");
            String author = (String) jsonObject.get("Author");
            System.out.println("Name: " + name);
            System.out.println("Author: " + author);

            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
 
            System.out.println("\nCompany List:");
            Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
 */

 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}