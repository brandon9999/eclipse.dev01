package com.kis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.Context;

import jeus.management.JMXUtility;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEManagedObjectMBean;
import jeus.management.j2ee.J2EEServerMBean;
import jeus.management.j2ee.JVMMBean;
import jeus.management.j2ee.servlet.WebEngineMoMBean;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.naming.Context;


import javax.xml.bind.JAXBException;

import com.sun.org.glassfish.external.statistics.BoundedRangeStatistic;
import com.sun.org.glassfish.external.statistics.CountStatistic;
import com.sun.org.glassfish.external.statistics.RangeStatistic;

import jeus.management.JMXUtility;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.servlet.SessionContainerCentralMoMBean;
import jeus.management.j2ee.servlet.SessionContainerMoMBean;
import jeus.management.j2ee.servlet.WebEngineMoMBean;

/*  Add 2018.11.12 */
import jeus.sessionmanager.distributed.*;
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEServerMBean;
import jeus.management.j2ee.J2EEServer;
import jeus.server.JeusEnvironment;
import jeus.management.j2ee.*;
import jeus.management.*;
import jeus.util.message.JeusMessage_JMXRemote;
import jeus.management.j2ee.JVMMBean;
import jeus.management.j2ee.statistics.JVMStatsImpl;

import java.io.NotSerializableException;

class ServerList 
{ 
  private String name; 
  private String ip; 
  private String baseport;
  
  public void setName(String name)
  {
	  this.name = name;
  }

  public String getName()
  {
	  return name;
  }

  public void setIp(String ip)
  {
	  this.ip = ip;
  }

  public String getIp()
  {
	  return ip;
  }

  public void setBaseport(String baseport)
  {
	  this.baseport = baseport;
  }

  public String getBaseport()
  {
	  return baseport;
  }
  
} 



public class JeusMon 
{
    //public static String split = " | ";
    //public static String url = "rhel01:9736";
    //public static String serverName = "adminServer";
    public static String user = "administrator";
    public static String password = "jeusadmin";
    //public static WebEngineMoMBean webEngineMoMBean = null;
    //public static MBeanServerConnection mbConn;	
    
    
    // Add 2018.11.12
    public static J2EEDomainMBean j2eeDomain = null;
    public static J2EEServerMBean j2eeServer = null;
    public static J2EEManagedObjectMBean j2eeMom = null;
    
    
    
	public static final String FILENAME = "C:\\temp\\mslist.txt";
    public static ArrayList<String> list = new ArrayList<String>();
	
	
	public static void main(String[] args) throws Exception 
	{
		getConf();
	}
	
    private static void getConf() throws Exception
    {
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				list.add(sCurrentLine);
				//System.out.println(sCurrentLine);
			}
			
			

			Hashtable<String,Object> h = new Hashtable(); 
			
			
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) 
			{
			    String element = (String) iterator.next();
			    String[] array01 = element.split("\\|\\|");

		        Hashtable<String, Object> env = new Hashtable<String, Object>();
		        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
		        env.put(Context.PROVIDER_URL, array01[1]+":"+array01[2]);
		        env.put(Context.SECURITY_PRINCIPAL, user);
		        env.put(Context.SECURITY_CREDENTIALS, password);

		        MBeanServerConnection mbConn;
		        mbConn = RemoteMBeanServerFactory.getMBeanServer( env );

				h.put(array01[0],mbConn);
				
	            System.out.println( element );
			}

			// AdminServer Info
			MBeanServerConnection mbeanConn = (MBeanServerConnection)h.get("adminServer");
			getSvrState(mbeanConn, "adminServer");

			// ManagedServer Info
			MBeanServerConnection mbeanConn1 = (MBeanServerConnection)h.get("rhel01.svr01");
			getSvrState(mbeanConn1, "rhel01.svr01");
			
			MBeanServerConnection mbeanConn2 = (MBeanServerConnection)h.get("rhel01.svr02");
			getSvrState(mbeanConn2, "rhel01.svr02");
			
			MBeanServerConnection mbeanConn3 = (MBeanServerConnection)h.get("rhel01.svr03");
			getSvrState(mbeanConn3, "rhel01.svr03");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}       	
    }

    private static void getSvrState(MBeanServerConnection utility, String svrName) throws Exception, AttributeNotFoundException 
    {
		System.out.println( "=================================================");
		System.out.println( svrName + " Info ");
		System.out.println( "=================================================");
    	
    	if (svrName.equals("adminServer"))
        {
          	ObjectName objName1 = JMXUtility.queryJ2EEDomain(utility, "jeus_domain");
        	ObjectName objName2 = JMXUtility.queryJ2EEServer(utility, svrName);
        	
        	j2eeDomain=  JMXUtility.getProxy(utility, objName1, J2EEDomainMBean.class, false);
        	j2eeServer=  JMXUtility.getProxy(utility, objName2, J2EEServerMBean.class, false);

    		
    		System.out.println( " JEUS Version : " + j2eeServer.getserverVersion());
            System.out.println( " Vendor : " + j2eeServer.getserverVendor());
            System.out.println( " Domain Name : " + j2eeDomain.getDomainName());
            
            getAllSvrs(utility);
            getAliveSvrs(utility);
        }
        else
        {
        	ObjectName objName2 = JMXUtility.queryJ2EEServer(utility, svrName);
        	j2eeServer=  JMXUtility.getProxy(utility, objName2, J2EEServerMBean.class, false);
        	
        	System.out.println( " JEUS Version : " + j2eeServer.getserverVersion());
            System.out.println( " Vendor : " + j2eeServer.getserverVendor());
            System.out.println( " JEUS Home : " + j2eeServer.getJeusHome());
            System.out.println( " Domain Home : " + j2eeServer.getDomainHome());
            System.out.println( " DAS Name : " + j2eeServer.getDomainAdminServerName());
            System.out.println( " Cluster : " + j2eeServer.getClusterName());
            System.out.println( " Listen Address : " + j2eeServer.getListenAddress());
            System.out.println( " Listen Port : " + j2eeServer.getListenPort());
            System.out.println( " PID : " + j2eeServer.getPID());
            System.out.println( " Server Home : " + j2eeServer.getServerHome());
            System.out.println( " Started Time : " + j2eeServer.getStartedTime());
            System.out.println( " Status : " + j2eeServer.getCurrentStatus());
            System.out.println( " ActionOnResourceLeak : " + j2eeServer.getActionOnResourceLeak());

			// need for alive server
	        JVMMBean jm = getJVMInfo(utility, svrName);        
	        getSesBackupSvr(utility, svrName);     
        }
   
    }
 
    private static void getAllSvrs(MBeanServerConnection utility ) throws Exception
    {
        System.out.println( "=============================" );
        System.out.println( "Get All Server" );
        System.out.println( "=============================" );
    	
    	ObjectName objectName = JMXUtility.queryJ2EEDomain(utility, "jeus_domain");
        
        if( objectName == null ) 
        {
            //return null;
        } 
        else 
        {
        	j2eeDomain=  JMXUtility.getProxy(utility, objectName, J2EEDomainMBean.class, false);
			List<String> getAllSvr = j2eeDomain.getAllServers();

			if( getAllSvr.size() == 0 )
			{
			    System.out.println( "size = 0" );
			}
			else
			{
					Iterator iterator = getAllSvr.iterator();
					while (iterator.hasNext()) 
					{
					    String element = (String) iterator.next();
			            System.out.println( element );
					}
			}
        }
    }

    private static void getAliveSvrs(MBeanServerConnection utility ) throws Exception
    {
        System.out.println( "=============================" );
        System.out.println( "Get Alive Servers" );
        System.out.println( "=============================" );
    	
    	ObjectName objectName = JMXUtility.queryJ2EEDomain(utility, "jeus_domain");
        
        if( objectName == null ) 
        {
            //return null;
        } 
        else 
        {
        	j2eeDomain=  JMXUtility.getProxy(utility, objectName, J2EEDomainMBean.class, false);
            List<String> getAliveSvr = j2eeDomain.getAliveServerNames();

			if( getAliveSvr.size() == 0 )
			{
			    System.out.println( "size = 0" );
			}
			else
			{
					Iterator iterator = getAliveSvr.iterator();
					while (iterator.hasNext()) 
					{
					    String element = (String) iterator.next();
			            System.out.println( element );
					}
			}
        }
    }
    
	public static JVMMBean getJVMInfo(MBeanServerConnection mbsc, String svrName){
		JVMMBean jb = null;
		try{
			Hashtable attr = new Hashtable();	
			attr.put("j2eeType","JVM");
			attr.put("JMXManager", svrName);
			attr.put("J2EEServer",svrName);
			attr.put("name",svrName);
			
			ObjectName objNames = new ObjectName("JEUS",attr);
			
			jb = (JVMMBean)MBeanServerInvocationHandler.newProxyInstance(mbsc, objNames, JVMMBean.class, false );
			
            System.out.println( " Status : " + jb.getjavaVendor());
            System.out.println( " Status : " + jb.getjavaVersion());
            System.out.println( " Status : " + jb.getOSName());
            System.out.println( " Status : " + jb.getOSVersion());
            System.out.println( " Status : " + jb.getAllProperties());
            System.out.println( " Status : " + jb.getSystemInfo().toString());
            System.out.println( " Status : " + jb.getstats());			
            System.out.println( " Status : " + jb.getStats());		
            
            JVMStatsImpl jvmstatsimpl = (JVMStatsImpl) jb.getstats();
            javax.management.j2ee.statistics.RangeStatistic totalSize = jvmstatsimpl.getTotalSize();
            javax.management.j2ee.statistics.BoundedRangeStatistic heapSize = jvmstatsimpl.getHeapSize();
            javax.management.j2ee.statistics.CountStatistic upTime = jvmstatsimpl.getUpTime();
            
            // JVM Total Size
            System.out.println("[Total Size]");
            System.out.println("-unit : " + totalSize.getUnit());
            System.out.println("-current : " + totalSize.getCurrent());
            System.out.println("-min size : " + totalSize.getLowWaterMark());
            System.out.println("-max size : " + totalSize.getHighWaterMark());     
            
            // JVM Heap Size
            System.out.println("[Heap Size]");
            System.out.println("-unit : " + heapSize.getUnit());
            System.out.println("-current : " + heapSize.getCurrent());
            System.out.println("-min Size : " + heapSize.getLowWaterMark());
            System.out.println("-max Size : " + heapSize.getHighWaterMark());
            System.out.println("-lower bound : " + heapSize.getLowerBound());
            System.out.println("-upper bound : " + heapSize.getUpperBound());

            // JVM UpTime
            System.out.println("[Up Time]");
            System.out.println("-unit : " + upTime.getUnit());
            System.out.println("-count : " + upTime.getCount());
            System.out.println("-start time : " + upTime.getStartTime());            
            
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return jb;
	}
    
    private static void getSesBackupSvr(MBeanServerConnection mbsc, String svrName) throws Exception{

        System.out.println( "[getSesBackup()] Session Backup Server Name - "+ svrName+" ]" );
        WebEngineMoMBean webEngineMoMBean = null;
        webEngineMoMBean = getWebEngine( svrName, mbsc);

        try 
        {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean 
                     =  JMXUtility.getProxy(mbsc, sessionContainerNames, 
                    		 SessionContainerCentralMoMBean.class, false);

            for( SessionContainerMoMBean containerMbean : sessionContainerMBean )
            {
                System.err.println( containerMbean.getobjectName() );

                DistributedSessionServerInfo dsvr = containerMbean.getDistributedServerInfo();
                
                try {
                    System.out.println( "[Session Backup Name] " + dsvr.getBackupName() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
                try {
                    System.out.println( "[Session Manager Scope] "  + dsvr.getManagerScope() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    protected static WebEngineMoMBean getWebEngine(String containerName, MBeanServerConnection utility ) throws IOException{
        ObjectName objectName = JMXUtility.queryWebEngine( utility, containerName );
        if( objectName == null ) {
            return null;
        } else {
            return JMXUtility.getProxy(utility, objectName, WebEngineMoMBean.class, false);
        }
    }    
    
}