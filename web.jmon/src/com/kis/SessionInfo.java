package com.kis;

import java.io.File;
import java.io.FileOutputStream;
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
import jeus.management.j2ee.J2EEDomainMBean;
import jeus.management.j2ee.J2EEManagedObjectMBean;
import jeus.management.j2ee.J2EEServerMBean;
import jeus.management.j2ee.JDBCResourceMBean;
import jeus.management.j2ee.JVMMBean;
import jeus.management.j2ee.servlet.SessionContainerCentralMoMBean;
import jeus.management.j2ee.servlet.SessionContainerMoMBean;
import jeus.management.j2ee.servlet.WebEngineMoMBean;
import jeus.sessionmanager.distributed.DistributedSessionServerInfo;

public class SessionInfo 
{
    public static J2EEDomainMBean j2eeDomain = null;
    public static J2EEServerMBean j2eeServer = null;
    public static J2EEManagedObjectMBean j2eeMom = null;
    
    public void getSessionInfo(MBeanServerConnection mbCon, String svrName) throws Exception{

		System.out.println( "=================================================");
		System.out.println( "[getSessionInfo]" + svrName + " : ");
		System.out.println( "=================================================");
        
        WebEngineMoMBean webEngineMoMBean = null;
        webEngineMoMBean = getWebEngine( mbCon, svrName );

        try 
        {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean 
                     =  JMXUtility.getProxy(mbCon, sessionContainerNames, 
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
    

    
    public String getSessionBackupInfo(MBeanServerConnection mbCon, String svrName) throws Exception
    {
		//System.out.println( "=================================================");
		//System.out.println( "[getSessionBackInfo]" + svrName + " : ");
		//System.out.println( "=================================================");
        
        WebEngineMoMBean webEngineMoMBean = null;
        webEngineMoMBean = getWebEngine( mbCon, svrName );

        DistributedSessionServerInfo dsvr = null;
        
        try 
        {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean 
                     =  JMXUtility.getProxy(mbCon, sessionContainerNames, 
                    		 SessionContainerCentralMoMBean.class, false);

            for( SessionContainerMoMBean containerMbean : sessionContainerMBean )
            {
                //System.err.println( containerMbean.getobjectName() );

                dsvr = containerMbean.getDistributedServerInfo();
                
                try 
                {
                    //System.out.println( "[Session Backup Name] " + dsvr.getBackupName() );
                }
                catch (NullPointerException e) 
                {
                    e.printStackTrace();
                }
                
            }

         } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return dsvr.getBackupName();
    }    
    
    
    
    
    protected static WebEngineMoMBean getWebEngine(MBeanServerConnection mbCon, String svrName) throws IOException{
        ObjectName objectName = JMXUtility.queryWebEngine( mbCon, svrName );
        if( objectName == null ) {
            return null;
        } else {
            return JMXUtility.getProxy(mbCon, objectName, WebEngineMoMBean.class, false);
        }
    }    
    
}
