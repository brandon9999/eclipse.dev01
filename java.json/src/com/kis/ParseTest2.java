package com.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseTest2
{
	
//    public static final String FILENAME = "C:\\DevHome\\jsontest\\server1.json";
//    public static final String FILENAME = "C:\\DevHome\\jsontest\\server1si.json";
    public static final String FILENAME = "C:\\DevHome\\jsontest\\jsonresponse.json";
    
	
//    public static void printNode() throws ParseException
    public static void main(String[] argv) throws ParseException 	
    {
        JSONParser jsParser = new JSONParser();

        
        
        Object ob;
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
		{
			ob = jsParser.parse(br);
	        System.out.println(ob);

	        JSONObject json = (JSONObject) jsParser.parse(ob.toString());
	        

	        System.out.println("name=" + json.get("jeuadmin-result")); // name=MyNode
	        
            JSONArray msgList =(JSONArray) json.get("values");
            Iterator<String>iterator = msgList.iterator();
            
/*
            Iterator<String>iterator = msgList.iterator();
            while(iterator.hasNext()) 
            {
                   System.out.println(iterator.next());
            }

*/
	        
	        System.out.println("name=" + ""); // name=MyNode

//	        System.out.println("width=" + json.get("width")); // width=200
	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}   
		
/*
        Object ob;
		try 
		{
			ob = jsParser.parse(txt);
	        System.out.println(ob); // {"name":"MyNode", "width":200, "height":100}
	        JSONObject json = (JSONObject) jsParser.parse(txt);
	        System.out.println("name=" + json.get("name")); // name=MyNode
	        System.out.println("width=" + json.get("width")); // width=200
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		
     }
}