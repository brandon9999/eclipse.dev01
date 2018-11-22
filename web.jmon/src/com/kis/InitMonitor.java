package com.kis;

import jeus.jndi.JNSConstants;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEManagedObjectMBean;
import jeus.management.j2ee.J2EEServerMBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.management.MBeanServerConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.j2ee.statistics.JDBCConnectionPoolStats;
import javax.management.j2ee.statistics.JDBCStats;
import javax.management.j2ee.statistics.RangeStatistic;
import javax.management.remote.JMXConnector;
import javax.naming.Context;
import javax.naming.InitialContext;

import jeus.management.JMXConstants;
import jeus.management.JMXUtility;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEManagedObjectMBean;
import jeus.management.j2ee.J2EEServerMBean;
import jeus.management.j2ee.JDBCResourceMBean;
import jeus.management.j2ee.JVMMBean;
import jeus.management.j2ee.servlet.SessionContainerCentralMoMBean;
import jeus.management.j2ee.servlet.SessionContainerMoMBean;
import jeus.management.j2ee.servlet.WebEngineMoMBean;
import jeus.sessionmanager.distributed.DistributedSessionServerInfo;

public class InitMonitor 
{
    //public static String split = " | ";
    //public static String url = "rhel01:9736";
    //public static String serverName = "adminServer";
    //public static String user = "administrator";
    //public static String password = "jeusadmin";
    
    public static String v_domainName = null;
    public static String v_user = null;
    public static String v_pwd = null;

    public static final String FILENAME = "C:\\temp\\mslist.txt";
    /*    
     * mslist.txt sample
    	adminServer||192.168.56.150||9736||jeus_domain||administrator||jeusadmin
    	rhel01.svr01||192.168.56.150||19001
    	rhel01.svr02||192.168.56.150||19002
    	rhel01.svr03||192.168.56.150||19003  
    */
	public static ArrayList<String> svrFileList = new ArrayList<String>();
	public static Hashtable<String,Object> mbConList = new Hashtable(); 
    
    
    public void InitMonitor() throws Exception 
    {
    }
    
    public static ArrayList<String> getServerListFile() throws Exception
    {
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				svrFileList.add(sCurrentLine);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}   
		
		return svrFileList;
    }
       
    
    public static Hashtable<String,Object> getMBeanConn(String svrName, String svrIP, String svrPort, String svrUser, String svrPwd, String domainName) throws Exception
    {
		    v_domainName = domainName;
		    v_user = svrUser;
		    v_pwd = svrPwd;

	    	Hashtable<String, Object> env = new Hashtable<String, Object>();
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
	        env.put(Context.PROVIDER_URL, svrIP+":"+svrPort);
	        env.put(Context.SECURITY_PRINCIPAL, v_user);
	        env.put(Context.SECURITY_CREDENTIALS, v_pwd);

	        MBeanServerConnection mbCon;
	        
	        try
	        {
	        	mbCon = RemoteMBeanServerFactory.getMBeanServer( env );

	        	if(mbCon != null)
	        	{
			        mbConList.put(svrName,mbCon);  // put("servername", mbCon) 
	        	}
	        }
	        catch(jeus.util.JeusRuntimeException e)
	        {
	            e.printStackTrace();
	        }
	        catch(java.lang.NullPointerException  e)
	        {
	            e.printStackTrace();
	        }

			return mbConList;
}    
    
    public static Hashtable<String,Object> getMBeanConn() throws Exception
    {
			//Iterator iterator = svrFileList.iterator();
    		Iterator iterator = getServerListFile().iterator();
    	
			while (iterator.hasNext()) 
			{
				String element = (String) iterator.next();
			    String[] array01 = element.split("\\|\\|");

			    String svrName = array01[0];
			    
		    	if (svrName.equals("adminServer"))
		        {
				    v_domainName = array01[3];
				    v_user = array01[4];
				    v_pwd = array01[5];
		        }
		    	
		        Hashtable<String, Object> env = new Hashtable<String, Object>();
		        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
		        env.put(Context.PROVIDER_URL, array01[1]+":"+array01[2]);
		        env.put(Context.SECURITY_PRINCIPAL, v_user);
		        env.put(Context.SECURITY_CREDENTIALS, v_pwd);

		        MBeanServerConnection mbCon;
		        
		        try
		        {
		        	mbCon = RemoteMBeanServerFactory.getMBeanServer( env );

		        	if(mbCon != null)
		        	{
				        mbConList.put(array01[0],mbCon);  // put("servername", mbCon) 
		        	}
		        }
		        catch(jeus.util.JeusRuntimeException e)
		        {
		            e.printStackTrace();
		        }
		        catch(java.lang.NullPointerException  e)
		        {
		            e.printStackTrace();
		        }
		
			}

			return mbConList;
    }
}
