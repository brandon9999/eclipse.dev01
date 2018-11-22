/*
 * $Id: MonitoringClient.java 1952 2010-03-11 08:03:26Z bluewolf $
 * Created: Nov 5, 2008
 *
 * Copyright 2008, TmaxSoft Co., Ltd. All Rights Reserved.
 */
package com.kis;

import jeus.jndi.JNSConstants;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEManagedObjectMBean;
import jeus.management.j2ee.J2EEServerMBean;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.gson.Gson;

import javax.management.MBeanServerConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

//gson library
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MonitoringClient 
{
	// gson
	public static Gson gson = null;
	
	public static String g_domainName = null;
    //public static String g_user = null;
    //public static String g_pwd = null;
    
	public static ArrayList<String> svrFileList = new ArrayList<String>();
	public static Hashtable<String,Object> mbConList = new Hashtable(); 
	
    public static void main(String[] args) throws Exception 
    {
		///////////////////////////////////////////////
    	// GSON Library Test
		///////////////////////////////////////////////
/*
    	gson = new Gson();
		JsonObject object = new JsonObject ();
		object.addProperty("name", "kim");
		object.addProperty("age", "22");
		String json = gson.toJson(object);
		System.out.println(json);
*/
    	
		///////////////////////////////////////////////
    	// TEST MS
		///////////////////////////////////////////////
    	String svrName = null;
    	String svrIP = null;
    	String svrPort = null;
    	String svrUser = null;
    	String svrPwd = null;
    	String domainName = null;

/*    	
    	svrName = "rhel01.svr01";
    	svrIP ="rhel01";
    	svrPort = "19001";
    	svrUser = "administrator";
    	svrPwd = "jeusadmin";
    	domainName = "jeus_domain";

/////////////////////////////////////////////////
*/

		svrName = "adminServer";
		svrIP ="192.168.2.31";
		svrPort = "39736";
		domainName = "jeus_domain";    	
		svrUser = "administrator";
		svrPwd = "jeusadmin";

		System.out.println("Debug Line ~  ");

		
		InitMonitor initMonitor = new InitMonitor();
    	if (svrName != null || svrName != "")
    	{
            
    		mbConList = InitMonitor.getMBeanConn(svrName, svrIP,  svrPort, svrUser, svrPwd, domainName);         // MBeanConnection 목록  (alive한 것들만) 
        	g_domainName = InitMonitor.g_domainName;        // Domain Name
    	}
    	else
    	{
        	svrFileList = InitMonitor.getServerListFile();  // 모니터링 대상 목록 (mslist.txt)
        	mbConList = InitMonitor.getMBeanConn();         // MBeanConnection 목록  (alive한 것들만) 
        	g_domainName = InitMonitor.g_domainName;        // Domain Name
    	}
    	
        Iterator<String> keys = mbConList.keySet().iterator();
        while( keys.hasNext() )
        {
            String key = keys.next();
            //System.out.println( String.format("primary : %s, backup : %s", key, mbConList.get(key)) );
            
            getServerInfo((MBeanServerConnection)mbConList.get(key), key);
    		//getJvmInfo((MBeanServerConnection)mbConList.get(key), key);
    		//getServletThreadInfo((MBeanServerConnection)mbConList.get(key), key);
    		//getThreadPoolInfo((MBeanServerConnection)mbConList.get(key), key);
    		//getDBConnectionPoolInfo((MBeanServerConnection)mbConList.get(key), key);
    		//getServerState();
    		//getSessionBackupTable();

        
        }
		
		
    }
    
    private static void getJvmInfo(MBeanServerConnection mbCon, String svrName) throws Exception
    {
        JVMInfo jvmInfo = new JVMInfo();
        jvmInfo.showInfo(mbCon, svrName);

        JVMInfo2 jvmInfo2 = new JVMInfo2();
        jvmInfo2.showInfo(mbCon, svrName);
    }

    private static void getServletThreadInfo(MBeanServerConnection mbCon, String svrName) throws Exception
    {
        ServletThreadInfo servletThreadInfo = new ServletThreadInfo();
        servletThreadInfo.showInfo(mbCon, svrName);
        
        System.out.println("- active thread count          : " 
                + servletThreadInfo.activeThreadCnt);
    }
    private static void getThreadPoolInfo(MBeanServerConnection mbCon, String svrName) throws Exception
    {
        ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
        threadPoolInfo.showInfo(mbCon, svrName);
    }
    private static void getDBConnectionPoolInfo(MBeanServerConnection mbCon, String svrName) throws Exception
    {
        DBConnectionPoolInfo dbPoolInfo = new DBConnectionPoolInfo();
        dbPoolInfo.showInfo(mbCon, svrName);
    }    

    private static void getServerInfo(MBeanServerConnection mbCon, String svrName) throws Exception
    {
    	ServerInfo serverInfo = new ServerInfo();

    	if (svrName.equals("adminServer"))
        {
    	//	serverInfo.getAllServers(mbCon, svrName);
    	//	serverInfo.getAliveServers(mbCon, svrName);
            serverInfo.getServerStateAll(mbCon, svrName);
        }
    	else
    	{
            serverInfo.getServerStateAll(mbCon, svrName);
    	}
    }    

    private static void getSessionBackupTable() throws Exception
    {
		Hashtable<String,String> sessionTable = new Hashtable<String,String>();   
    	
		Iterator iterator = svrFileList.iterator();
		while (iterator.hasNext()) 
		{
		    String element = (String) iterator.next();
		    String[] array01 = element.split("\\|\\|");
		    String svrName = array01[0];
		    
	    	if (svrName.equals("adminServer"))
	        {
	        }
	    	else
	    	{
			    MBeanServerConnection mbeanConn = (MBeanServerConnection)mbConList.get(svrName);
			    SessionInfo sessionInfo = new SessionInfo();
			    //sessionInfo.getSessionInfo(mbeanConn, svrName);
			    
			    String backupSvrName = "";
			   try
			   {
				   backupSvrName = sessionInfo.getSessionBackupInfo(mbeanConn, svrName);
			   }
			   catch(NullPointerException e)
			   {
				   backupSvrName="null";   
			   }
			    sessionTable.put(svrName, backupSvrName);  
	    	}
    	}

        System.out.println(" ================================= " );
        System.out.println(" Session Backup Table " );
        System.out.println(" ================================= " );
		
        Iterator<String> keys = sessionTable.keySet().iterator();
        while( keys.hasNext() )
        {
            String key = keys.next();
            System.out.println( String.format("primary : %s, backup : %s", key, sessionTable.get(key)) );
        }
	} 

    private static void getServerState() throws Exception
    {
		Hashtable<String,String> stateTable = new Hashtable<String,String>();   
    	
		Iterator iterator = svrFileList.iterator();
		
		while (iterator.hasNext()) 
		{
		    String element = (String) iterator.next();
		    String[] array01 = element.split("\\|\\|");
		    String svrName = array01[0];

	    	if (svrName.equals("adminServer"))
	        {
	        }
	    	else
	    	{
			    MBeanServerConnection mbeanConn = (MBeanServerConnection)mbConList.get(svrName);
		        //System.out.println(mbConList.get(svrName).getClass().getName());
			    
			    ServerInfo serverInfo = new ServerInfo();
			    //sessionInfo.getSessionInfo(mbeanConn, svrName);
			    String serverState = serverInfo.getServerState(mbeanConn, svrName);
			    
			    stateTable.put(svrName, serverState);  
	    	}
    	}

        System.out.println(" ================================= " );
        System.out.println(" Server State " );
        System.out.println(" ================================= " );
		
        Iterator<String> keys = stateTable.keySet().iterator();
        while( keys.hasNext() )
        {
            String key = keys.next();
            System.out.println( String.format("%s (%s)", key, stateTable.get(key)) );

        
        }
	} 


}
