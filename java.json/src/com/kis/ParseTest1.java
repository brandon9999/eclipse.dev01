package com.kis;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseTest1
{
//    public static void printNode() throws ParseException
    public static void main(String[] argv) 	
    {
        String txt = "{\"name\":\"MyNode\", \"width\":200, \"height\":100}";
        JSONParser jsParser = new JSONParser();

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

     }
}