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

           	////////////////////////////////////////////////////////////
           	// json data : "jeusadmin-result"
           	////////////////////////////////////////////////////////////
        	JsonObject dataObject = (JsonObject) jsonObject.get("jeusadmin-result");
          	System.out.print("message : " + dataObject.get("message"));
           	System.out.print("\n");
          	System.out.print("data : " + dataObject.get("data"));
           	System.out.print("\n");
           	////////////////////////////////////////////////////////////

           	System.out.print("\n");
           	
           	////////////////////////////////////////////////////////////
           	// json data : "jeusadmin-result > data"
           	////////////////////////////////////////////////////////////
           	JsonParser jsonParser_data = new JsonParser();
           	Object object_data = jsonParser_data.parse(dataObject.get("data").toString());
           	JsonArray jsonArray_data = (JsonArray) object_data;

       		JsonObject jsonObj_data = (JsonObject)jsonArray_data.get(0);
       		System.out.print("title : " + jsonObj_data.get("title")+"\n");
       		System.out.print("header : " +jsonObj_data.get("header")+"\n");
       		System.out.print("column-names : " +jsonObj_data.get("column-names")+"\n");
       		System.out.print("rows : " +jsonObj_data.get("rows")+"\n");
           	////////////////////////////////////////////////////////////

           	System.out.print("\n");

           	////////////////////////////////////////////////////////////
           	// json data : "jeusadmin-result > data > rows"
           	////////////////////////////////////////////////////////////
       		JsonParser jsonParse_data_rows = new JsonParser();
           	Object ojbect_data_rows = jsonParse_data_rows.parse(jsonObj_data.get("rows").toString());
           	JsonArray jsonArray_data_rows = (JsonArray) ojbect_data_rows;

           	for(int i=0;i<jsonArray_data_rows.size();i++)
           	{
	       		JsonObject jsonObj_data_rows = (JsonObject)jsonArray_data_rows.get(i);
	       		System.out.print("row-key : " + jsonObj_data_rows.get("row-key")+"\n");
	       		System.out.print("values : " + jsonObj_data_rows.get("values")+"\n");
	       		
	       		JsonParser jsonParse_svr = new JsonParser();
	           	Object ojbect_svr = jsonParse_svr.parse(jsonObj_data_rows.get("values").toString());
	           	JsonArray jsonArray_svr = (JsonArray) ojbect_svr;
	       		System.out.print("array size : " + jsonArray_svr.size()+"\n");
	       		
	           	for(int k=0;k<jsonArray_svr.size();k++)
	           	{	       		
		       		System.out.print("array data : " + k + " : " + jsonArray_svr.get(k)+"\n");

	           	}
	           	System.out.print("\n");
           	}           	
           	////////////////////////////////////////////////////////////
           	
           	
   	
           	
           	
           	
           	
           	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}