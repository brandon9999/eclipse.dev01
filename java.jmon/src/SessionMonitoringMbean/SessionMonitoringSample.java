package SessionMonitoringMbean;
import java.io.IOException;
import java.util.*;


import javax.management.MBeanServerConnection;

import javax.management.ObjectName;

import javax.naming.Context;


import jeus.management.JMXUtility;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.servlet.SessionContainerCentralMoMBean;
import jeus.management.j2ee.servlet.SessionContainerMoMBean;
import jeus.management.j2ee.servlet.WebEngineMoMBean;

/*  Add 2018.11.12 */
import jeus.sessionmanager.distributed.*;

/**
 * Date : 15. 5. 14.
 *
 * @author seungwook
 */
public class SessionMonitoringSample{	

    public static String split = " | ";
    //public static String url = "rhel01:9736";
    public static String url = "rhel01:19001";
    public static String user = "administrator";
    public static String password = "jeusadmin";
    public static String serverName = "rhel01.svr01";
    //public static String serverName = "adminServer";
    public static WebEngineMoMBean webEngineMoMBean = null;
    public static MBeanServerConnection mbConn;

    public static void main(String[] args) throws Exception {


        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);

        mbConn = RemoteMBeanServerFactory.getMBeanServer( env );
        webEngineMoMBean = null;

        try{
            webEngineMoMBean = getWebEngine( serverName, mbConn);
        }catch( IOException ie ){
            ie.printStackTrace();
        }

        if( webEngineMoMBean == null )          {
            System.out.println("### ERROR : Can't get webEngineMoMBean ");
            return;
        }

        if( args.length == 0 )
        {
            showSessionKeys();
            
            // add 2018. 11. 12
            getDistSvrInfo();
            getP2PConStat();
        }else{
            if( args.length == 2 ){

                if( args[ 0 ].equals( "exist" ) ){
                    existSessionKey( args[ 1 ] );

                }else if( args[ 0 ].equals( "remove" ) ){
                    removeSessionKey( args[ 1 ] );

                }else{
                    System.out.println(" FUCK");
                    printUsage();
                }

            } else{
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
    private static void getDistSvrInfo() throws Exception{

        System.out.println( "[Session Backup Server Name - "+ serverName+" ]" );
        try {
            String[] sessionContainerNames = webEngineMoMBean.getSessionContainers();
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
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
            SessionContainerMoMBean[] sessionContainerMBean =  JMXUtility.getProxy(mbConn, sessionContainerNames, SessionContainerCentralMoMBean.class, false);
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
   
    
    
}