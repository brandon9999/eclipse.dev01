<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ page import="com.kis.*" %>
    

<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.management.MBeanServerConnection" %>

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




<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

<% out.println("adsfasdfadsfd"); %>

<%
String svrName = null;
String svrIP = null;
String svrPort = null;
String svrUser = null;
String svrPwd = null;
String domainName = null;

svrName = "rhel01.svr01";
svrIP ="rhel01";
svrPort = "19001";
svrUser = "administrator";
svrPwd = "jeusadmin";
domainName = "jeus_domain";

InitMonitor initMonitor = new InitMonitor();

if (svrName != null)
{
	mbConList = initMonitor.getMBeanConn(svrName, svrIP,  svrPort, svrUser, svrPwd, domainName);         // MBeanConnection 목록  (alive한 것들만) 
	v_domainName = initMonitor.v_domainName;        // Domain Name
}
else
{
	svrFileList = initMonitor.getServerListFile();  // 모니터링 대상 목록 (mslist.txt)
	mbConList = initMonitor.getMBeanConn();         // MBeanConnection 목록  (alive한 것들만) 
	v_domainName = initMonitor.v_domainName;        // Domain Name
}

Iterator<String> keys = mbConList.keySet().iterator();
while( keys.hasNext() )
{
    String key = keys.next();
    //System.out.println( String.format("primary : %s, backup : %s", key, mbConList.get(key)) );
    
    //getServerInfo((MBeanServerConnection)mbConList.get(key), key);
	//getJvmInfo((MBeanServerConnection)mbConList.get(key), key);
	//getServletThreadInfo((MBeanServerConnection)mbConList.get(key), key);
	//getThreadPoolInfo((MBeanServerConnection)mbConList.get(key), key);
	//getDBConnectionPoolInfo((MBeanServerConnection)mbConList.get(key), key);
	//getServerState();
	//getSessionBackupTable();

	ServletThreadInfo servletThreadInfo = new ServletThreadInfo();
    servletThreadInfo.showInfo((MBeanServerConnection)mbConList.get(key), key);
    
    out.println("- active thread count          : " 
            + servletThreadInfo.activeThreadCnt);
}

%>



</body>
</html>