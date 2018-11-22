package com.kis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import jeus.management.JMXUtility;
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEManagedObjectMBean;
import jeus.management.j2ee.J2EEServerMBean;

public class ServerInfo 
{
    public static J2EEDomainMBean j2eeDomain = null;
    public static J2EEServerMBean j2eeServer = null;
    public static J2EEManagedObjectMBean j2eeMom = null;
    
    public void ServerInfo() throws Exception 
    {
    }
    public void getServerStateAll(MBeanServerConnection mbCon, String svrName) throws Exception, AttributeNotFoundException 
    {
		System.out.println( "=================================================");
		System.out.println( "[getServerState]" + svrName + " Info ");
		System.out.println( "=================================================");
    	
    	if (svrName.equals("adminServer"))
        {
          	ObjectName objName1 = JMXUtility.queryJ2EEDomain(mbCon, "jeus_domain");
        	ObjectName objName2 = JMXUtility.queryJ2EEServer(mbCon, svrName);
        	
        	j2eeDomain=  JMXUtility.getProxy(mbCon, objName1, J2EEDomainMBean.class, false);
        	j2eeServer=  JMXUtility.getProxy(mbCon, objName2, J2EEServerMBean.class, false);
        	
        	System.out.println( " JEUS Version : " + j2eeServer.getserverVersion());
            System.out.println( " Vendor : " + j2eeServer.getserverVendor());
            System.out.println( " JEUS Home : " + j2eeServer.getJeusHome());
            System.out.println( " Domain Home : " + j2eeServer.getDomainHome());
            System.out.println( " DAS Name : " + j2eeServer.getDomainAdminServerName());
            System.out.println( " Status : " + j2eeServer.getCurrentStatus());
            System.out.println( " Listen Address : " + j2eeServer.getListenAddress());
            System.out.println( " Listen Port : " + j2eeServer.getListenPort());
            System.out.println( " PID : " + j2eeServer.getPID());
            System.out.println( " Server Home : " + j2eeServer.getServerHome());
            System.out.println( " Started Time : " + j2eeServer.getStartedTime());
            System.out.println( " Cluster : " + j2eeServer.getClusterName());
            System.out.println( " ActionOnResourceLeak : " + j2eeServer.getActionOnResourceLeak());

            //getAllSvrs(utility);
            //getAliveSvrs(utility);
        }
        else
        {
        	ObjectName objName2 = JMXUtility.queryJ2EEServer(mbCon, svrName);
        	j2eeServer=  JMXUtility.getProxy(mbCon, objName2, J2EEServerMBean.class, false);
        	
        	System.out.println( " JEUS Version : " + j2eeServer.getserverVersion());
            System.out.println( " Vendor : " + j2eeServer.getserverVendor());
            System.out.println( " JEUS Home : " + j2eeServer.getJeusHome());
            System.out.println( " Domain Home : " + j2eeServer.getDomainHome());
            System.out.println( " DAS Name : " + j2eeServer.getDomainAdminServerName());
            System.out.println( " Status : " + j2eeServer.getCurrentStatus());
            System.out.println( " Listen Address : " + j2eeServer.getListenAddress());
            System.out.println( " Listen Port : " + j2eeServer.getListenPort());
            System.out.println( " PID : " + j2eeServer.getPID());
            System.out.println( " Server Home : " + j2eeServer.getServerHome());
            System.out.println( " Started Time : " + j2eeServer.getStartedTime());
            System.out.println( " Cluster : " + j2eeServer.getClusterName());
            System.out.println( " ActionOnResourceLeak : " + j2eeServer.getActionOnResourceLeak());

			// need for alive server
	        //JVMMBean jm = getJVMInfo(utility, svrName);        
	        //getSesBackupSvr(utility, svrName);     
        }
   
    }


    public String getServerState(MBeanServerConnection mbCon, String svrName) throws Exception, AttributeNotFoundException 
    {
		//System.out.println( "=================================================");
		//System.out.println( "[getServerState]" + svrName + " Info ");
		//System.out.println( "=================================================");
    	
    	if (svrName.equals("adminServer"))
        {
          	ObjectName objName1 = JMXUtility.queryJ2EEDomain(mbCon, "jeus_domain");
        	ObjectName objName2 = JMXUtility.queryJ2EEServer(mbCon, svrName);
        	
        	j2eeDomain=  JMXUtility.getProxy(mbCon, objName1, J2EEDomainMBean.class, false);
        	j2eeServer=  JMXUtility.getProxy(mbCon, objName2, J2EEServerMBean.class, false);
    		
    		System.out.println( " JEUS Version : " + j2eeServer.getserverVersion());
            System.out.println( " Vendor : " + j2eeServer.getserverVendor());
            System.out.println( " JEUS Home : " + j2eeServer.getJeusHome());
            System.out.println( " Domain Home : " + j2eeServer.getDomainHome());
            System.out.println( " DAS Name : " + j2eeServer.getDomainAdminServerName());
            System.out.println( " Status : " + j2eeServer.getCurrentStatus());
            System.out.println( " Listen Address : " + j2eeServer.getListenAddress());
            System.out.println( " Listen Port : " + j2eeServer.getListenPort());
            System.out.println( " PID : " + j2eeServer.getPID());
            System.out.println( " Server Home : " + j2eeServer.getServerHome());
            System.out.println( " Started Time : " + j2eeServer.getStartedTime());
            System.out.println( " Status : " + j2eeServer.getCurrentStatus());

            //getAllSvrs(utility);
            //getAliveSvrs(utility);
        }
        else
        {
        	ObjectName objName2 = JMXUtility.queryJ2EEServer(mbCon, svrName);
        	j2eeServer=  JMXUtility.getProxy(mbCon, objName2, J2EEServerMBean.class, false);
        	/*
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
			*/
        }

        return j2eeServer.getCurrentStatus().toString();
    }
    
    public void getAllServers(MBeanServerConnection mbCon, String svrName) throws Exception
    {
        System.out.println( "=============================" );
        System.out.println( "Get All Server" );
        System.out.println( "=============================" );
    	
    	ObjectName objectName = JMXUtility.queryJ2EEDomain(mbCon, "jeus_domain");
        
        if( objectName == null ) 
        {
            //return null;
        } 
        else 
        {
        	j2eeDomain=  JMXUtility.getProxy(mbCon, objectName, J2EEDomainMBean.class, false);
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
    
    
    public void getAliveServers(MBeanServerConnection mbCon, String svrName) throws Exception
    {
        System.out.println( "=============================" );
        System.out.println( "Get Alive Servers  " );
        System.out.println( "=============================" );
    	
    	ObjectName objectName = JMXUtility.queryJ2EEDomain(mbCon, "jeus_domain");
        
        if( objectName == null ) 
        {
            //return null;
        } 
        else 
        {
        	j2eeDomain=  JMXUtility.getProxy(mbCon, objectName, J2EEDomainMBean.class, false);
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
    
    
}
