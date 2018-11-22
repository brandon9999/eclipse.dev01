<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ page import="com.kis.*" %>
    
<%@ page import="jeus.jndi.JNSConstants" %>
<%@ page import="jeus.management.RemoteMBeanServerFactory" %>
<%@ page import="jeus.management.j2ee.*" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.management.MBeanServerConnection" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

<%!

public static String v_domainName = null;
public static String v_user = null;
public static String v_pwd = null;
public static final String FILENAME = "C:\\temp\\mslist.txt";
//public static final String FILENAME = "/nfs01/DevHome/AppHome/webhome/mbean/JeusMonWeb/mslist.txt";


/*    
 * mslist.txt sample
	adminServer||192.168.56.150||9736||jeus_domain||administrator||jeusadmin
	rhel01.svr01||192.168.56.150||19001
	rhel01.svr02||192.168.56.150||19002
	rhel01.svr03||192.168.56.150||19003  
*/
public static ArrayList<String> svrFileList = new ArrayList<String>();
public static Hashtable<String,Object> mbConList = new Hashtable(); 
%>

<%!
 private static void getMBean() throws Exception
    {
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) 
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				svrFileList.add(sCurrentLine);
				System.out.println(sCurrentLine);
			}
			
			

			Iterator iterator = svrFileList.iterator();
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
		        mbCon = RemoteMBeanServerFactory.getMBeanServer( env );

		        mbConList.put(array01[0],mbCon);
				
	            System.out.println( element );
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}       	
    }
%>

<%!

private static void getServerInfo(MBeanServerConnection mbCon, String svrName) throws Exception
{
	ServerInfo serverInfo = new ServerInfo();
    
	if (svrName.equals("adminServer"))
    {
		serverInfo.getAllServers(mbCon, svrName);
		serverInfo.getAliveServers(mbCon, svrName);
        serverInfo.getServerStateAll(mbCon, svrName);
    }
	else
	{
        serverInfo.getServerStateAll(mbCon, svrName);
	}
}  

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>


<% out.println("aaaaaaaaaa"); %>
<%


getMBean();		// server connection
String v_svrname = "adminServer";
//String v_svrname = "rhel01.svr01";
//String v_svrname = args[0]; // default : adminServer


MBeanServerConnection mbeanConn = (MBeanServerConnection)mbConList.get(v_svrname);

getServerInfo(mbeanConn, v_svrname);

%>



</body>
</html>