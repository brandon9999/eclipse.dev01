package SessionMonitoringMbean;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.naming.Context;


import javax.xml.bind.JAXBException;

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

import java.io.NotSerializableException;


/**
 * Date : 15. 5. 14.
 *
 * @author seungwook
 */
public class SessionMonitoringSample2{	

    public static String split = " | ";
    public static String url = "rhel01:9736";
    public static String serverName = "adminServer";
    public static String user = "administrator";
    public static String password = "jeusadmin";
    public static WebEngineMoMBean webEngineMoMBean = null;
    public static MBeanServerConnection mbConn;

    // Add 2018.11.12
    public static WebEngineMoMBean webEngineMoMBean2 = null;
    public static MBeanServerConnection mbConn2;
    public static J2EEDomainMBean j2eeDomain = null;
    public static J2EEServerMBean j2eeServer = null;
    public static J2EEManagedObjectMBean j2eeMom = null;

    
    
    
    public static void main(String[] args) throws Exception
    {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);

        mbConn = RemoteMBeanServerFactory.getMBeanServer( env );
        
        webEngineMoMBean = null;
        webEngineMoMBean2 = null;

        try
        {
            webEngineMoMBean = getWebEngine( serverName, mbConn);
        }
        catch( IOException ie )
        {
            ie.printStackTrace();
        }

        if( webEngineMoMBean == null )          
        {
            System.out.println("### ERROR : Can't get webEngineMoMBean ");
            return;
        }

        if( args.length == 0 )
        {
            //showSessionKeys();
            
            // add 2018. 11. 12
        	//getSesBackupSvr();
            //getP2PConStat();
        	getAllSvrs(mbConn);
        	getAliveSvrs(mbConn);
        	//getSvrInfo(mbConn, "rhel01.svr01");
        }
        else
        {
            if( args.length == 2 )
            {

                if( args[ 0 ].equals( "exist" ) )
                {
                    existSessionKey( args[ 1 ] );
                }
                else if( args[ 0 ].equals( "remove" ) )
                {
                    removeSessionKey( args[ 1 ] );
                }
                else
                {
                    System.out.println(" FUCK");
                    printUsage();
                }
            } 
            else
            {
                printUsage();
            }
        }
    }

    private static void removeSessionKey( String arg ) throws Exception{
        System.out.println( "[Check Session Exist- "+ serverName+" ]" );
        try {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
            for( SessionContainerMoMBean containerMbean : sessionContainerMBean ){
                System.err.println( containerMbean.getobjectName() );
                boolean result =  containerMbean.invalidateSession( arg );
                if( result ){
                    System.out.println( "\t Result : TRUE - session removal success" );
                }else{
                    System.out.println( "\t Result : FALSE - session removal failed" );
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
    
    protected static WebEngineMoMBean getWebEngine2(String containerName, MBeanServerConnection utility ) throws IOException{
        ObjectName objectName = JMXUtility.queryWebEngine( utility, containerName );
    	
        if( objectName == null ) 
        {
            return null;
        } 
        else 
        {
            return JMXUtility.getProxy(utility, objectName, WebEngineMoMBean.class, false);
        }
    }   
    

    private static void printUsage(){
        System.out.println( "Usage : " );
        System.out.println( "\t SessionMonitoringSample" );
        System.out.println( "\t SessionMonitoringSample exist SessionId" );
        System.out.println( "\t SessionMonitoringSample remove SessionId" );

    }

    private static void existSessionKey( String arg ) throws Exception{
        System.out.println( "[Check Session Exist- "+ serverName+" ]" );
        try {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
            for( SessionContainerMoMBean containerMbean : sessionContainerMBean ){
                System.err.println( containerMbean.getobjectName() );
                boolean result =  containerMbean.isExist( arg );
                if( result ){
                    System.out.println( "\t Result : TRUE - session exist" );
                }else{
                    System.out.println( "\t Result : FALSE - session does not exist" );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showSessionKeys() throws Exception{

        System.out.println( "[List Active Session Keys - "+ serverName+" ]" );
        try {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
            for( SessionContainerMoMBean containerMbean : sessionContainerMBean ){
                System.err.println( containerMbean.getobjectName() );

                List<String> localSessionKeys = containerMbean.getLocalSessionKeys();
                if( localSessionKeys.size() == 0 ){
                    System.out.println( "\t no active session" );
                }else{
                    printMonitorSession(  localSessionKeys );
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static  int printMonitorSession( List<String> list ){

        StringBuilder builder = new StringBuilder(  );
        int count = 1;

        builder.append( " COUNT").append( split );
        builder.append( String.format( "%34s", "ID" ) );

        builder.append( "\n" );
        for( String session : list ){

            builder.append( String.format("%6d", count) ).append(split);
            builder.append( String.format( "%64s", session ) );
            builder.append( "\n" );
            count ++;
        }
            System.out.println( builder.toString() );

        return count;
    }
    
    // Add 2018.11.12
    private static void getSesBackupSvr() throws Exception{

        System.out.println( "[getSesBackup()] Session Backup Server Name - "+ serverName+" ]" );
        try 
        {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean 
                     =  JMXUtility.getProxy(mbConn, sessionContainerNames, 
                    		 SessionContainerCentralMoMBean.class, false);

            for( SessionContainerMoMBean containerMbean : sessionContainerMBean )
            {
                System.err.println( containerMbean.getobjectName() );

                DistributedSessionServerInfo dsvr = containerMbean.getDistributedServerInfo();
                
                try {
                    System.out.println( "\t " + dsvr.getServerName() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
                try {
                    System.out.println( "\t " + dsvr.getBackupName() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
                try {
                    System.out.println( "\t " + dsvr.getManagerScope() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private static void getSesBackupSvr2() throws Exception{

        System.out.println( "[Session Backup Server Name - "+ serverName+" ]" );
        try {
        	
            String[] sessionContainerNames = webEngineMoMBean2.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn2, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
            for( SessionContainerMoMBean containerMbean : sessionContainerMBean ){
                System.err.println( containerMbean.getobjectName() );

                DistributedSessionServerInfo dsvr = containerMbean.getDistributedServerInfo();
                
                try {
                    System.out.println( "\t " + dsvr.getServerName() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
                try {
                    System.out.println( "\t " + dsvr.getBackupName() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
                try {
                    System.out.println( "\t " + dsvr.getManagerScope() );
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    private static void getP2PConStat() throws Exception{

        System.out.println( "[Session getP2PConnectionStatus - "+ serverName+" ]" );
        try {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn2, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
            for( SessionContainerMoMBean containerMbean : sessionContainerMBean ){
                System.err.println( containerMbean.getobjectName() );

                Hashtable<String, Boolean> p2p = containerMbean.getP2PConnectionStatus();
             
                
                if (p2p == null)
                {
                       System.out.println( "\t null "  );
                }
                else
                {
                	   Enumeration<String> en = p2p.keys();

                	   while(en.hasMoreElements())
                 {
                		   String key = en.nextElement().toString();
                		   System.out.println(key + " : "+p2p.get(key));
                 	}
              
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   

    
    private static void getAllSvrs(MBeanServerConnection utility ) throws Exception{
        //ObjectName objectName = JMXUtility.queryWebEngine( utility, containerName );
   
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
        	
        	
        	
        	
            //List<String> getAllSvr = j2eeDomain.getAliveServerNames();
			List<String> getAllSvr = j2eeDomain.getAllServers();

			if( getAllSvr.size() == 0 )
			{
			    System.out.println( "\t no active session" );
			}
			else
			{
					Iterator iterator = getAllSvr.iterator();
					while (iterator.hasNext()) 
					{
					    String element = (String) iterator.next();
			            System.out.println( element );
			            
			            //mbConn = RemoteMBeanServerFactory.getMBeanServer(element);
			            //webEngineMoMBean = getWebEngine( element, mbConn);
			            //getSesBackupSvr2();
			            
			            
			            if (element.equals("adminServer"))
			            {
				            System.out.println( "this is adminServer" );
		            	
			            }
			            else
			            {
			            	//mbConn2 = RemoteMBeanServerFactory.getDedicatedMBeanServer(element, user, password);
			            	//webEngineMoMBean2 = getWebEngine2( element, mbConn2);
			            	
			            }

			            
			            getSvrState(utility, element);
			            
					}
			}
        	
        }
    }

    private static void getAliveSvrs(MBeanServerConnection utility ) throws Exception{
        //ObjectName objectName = JMXUtility.queryWebEngine( utility, containerName );

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
			//List<String> getAllSvr = j2eeDomain.getAllServers();

			if( getAliveSvr.size() == 0 )
			{
			    System.out.println( "\t no active session" );
			}
			else
			{
					Iterator iterator = getAliveSvr.iterator();
					while (iterator.hasNext()) 
					{
					    String element = (String) iterator.next();
			            System.out.println( element );
			            
			            //mbConn = RemoteMBeanServerFactory.getMBeanServer(element);
			            //webEngineMoMBean = getWebEngine( element, mbConn);
			            //getSesBackupSvr2();
			            
					}
			}
        	
        }
    }
  
    
	public static JVMMBean getJVMInfo(MBeanServerConnection mbsc, String instanceName){
		JVMMBean jb = null;
		try{
			Hashtable attr = new Hashtable();	
			attr.put("j2eeType","JVM");
			attr.put("JMXManager", instanceName);
			attr.put("J2EEServer",instanceName);
			attr.put("name",instanceName);
			
			ObjectName objNames = new ObjectName("JEUS",attr);
			
			jb = (JVMMBean)MBeanServerInvocationHandler.newProxyInstance(mbsc, objNames, JVMMBean.class, false );
			

			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return jb;
	}
    
    private static void getSvrState(MBeanServerConnection utility, String svrName) throws Exception, AttributeNotFoundException {
   	
    	//ObjectName objectName = JMXUtility.queryJ2EEDomain(utility, "jeus_domain");
    	//j2eeDomain=  JMXUtility.getProxy(utility, objectName, J2EEDomainMBean.class, false);

    	
    	ObjectName[] objectName = JMXUtility.queryJ2EEServers(utility, arg1)
    	j2eeDomain=  JMXUtility.getProxy(utility, objectName, J2EEDomainMBean.class, false);

                
        JVMMBean jm = getJVMInfo(utility, "adminServer");
		// 그중에서 JVM상태를 담고 있는 JVMStats객체를 얻어온다.
		//jeus.management.j2ee.statistics.JVMStats js = 
			//(jeus.management.j2ee.statistics.JVMStats)jm.getStats();
		
        System.out.println(jm.getAllProperties().toString());

		
        
        
        
       // System.out.println(aaa);
    	
        System.out.println("+++++++++++++++++++");
        
        if (svrName.equals("adminServer"))
        {
        	j2eeServer=  JMXUtility.getProxy(utility, objectName, J2EEServerMBean.class, false);
            System.out.println( " JEUS Version : " + j2eeServer.getserverVersion());
            System.out.println( " Vendor : " + j2eeServer.getserverVendor());
            System.out.println( " Domain Name : " + j2eeDomain.getDomainName());
        	

    
            
            //ObjectName objectName1 =  JMXUtility.makeJ2EEServerObjectName("rhel01.svr01");
            //webEngineMoMBean2 = JMXUtility.getProxy(utility, objectName1, WebEngineMoMBean.class, false);

            

		    
            //String aaa = RemoteMBeanServerFactory.getDedicatedMBeanServer("rhel01.svr01");
		    //System.out.println( aaa );	    
            
            /*
			if( getClusterName.size() == 0 )
			{
			    System.out.println( "\t no active session" );
			}
			else            
			{
				for(Iterator i=getClusterName.iterator(); i.hasNext();)
				{
					System.out.println(i.next());
					
				}
			}
            */
        }
        else
        {    	
        	System.out.println( svrName + " stats : " + j2eeDomain.getServerState(svrName));
        }
   
    }
    
}