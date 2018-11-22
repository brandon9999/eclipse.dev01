package com.kis.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.management.MBeanServerConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kis.InitMonitor;
import com.kis.ServletThreadInfo;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
// gson library
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


@WebServlet("/ThreadInfoServlet")
public class ThreadInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// gson
	public static Gson gson = null;
	
	public static String v_domainName = null;
	public static String v_user = null;
	public static String v_pwd = null;
	public static final String FILENAME = "C:\\temp\\mslist.txt";
	
	public static ArrayList<String> svrFileList = new ArrayList<String>();
	public static Hashtable<String,Object> mbConList = new Hashtable(); 		
	
    public ThreadInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("dopost");

		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userName = request.getParameter("userName");
		response.getWriter().write(getJSON(userName));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		System.out.println("doGet");
		
		response.getWriter().write(getJSON("test"));
		
//		System.out.println(json);
		
		
		
		
		
	}
	
	public String getJSON(String userName)
	{
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
*/
		
		svrName = "server1";
		svrIP ="192.168.2.31";
		svrPort = "39936";
		svrUser = "administrator";
		svrPwd = "jeusadmin";
		domainName = "jeus_domain";


		ServletThreadInfo servletThreadInfo = new ServletThreadInfo();
		
		
		InitMonitor initMonitor = new InitMonitor();

		if (svrName != null)
		{
			try {
				mbConList = initMonitor.getMBeanConn(svrName, svrIP,  svrPort, svrUser, svrPwd, domainName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}         // MBeanConnection 목록  (alive한 것들만) 
			v_domainName = initMonitor.v_domainName;        // Domain Name
		}
		else
		{
			try {
				svrFileList = initMonitor.getServerListFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  // 모니터링 대상 목록 (mslist.txt)
			try {
				mbConList = initMonitor.getMBeanConn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}         // MBeanConnection 목록  (alive한 것들만) 
			v_domainName = initMonitor.v_domainName;        // Domain Name
		}

		
		StringBuffer result = new StringBuffer("");
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

		    try {
				servletThreadInfo.showInfo((MBeanServerConnection)mbConList.get(key), key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    result.append("{\"result\":[");
			result.append("[{\"value\": \"" + servletThreadInfo.activeThreadCnt + "\"},");
			result.append("{\"value\": \"" + servletThreadInfo.activeThreadCnt + "\"},");
			result.append("{\"value\": \"" + servletThreadInfo.activeThreadCnt + "\"},");
			result.append("{\"value\": \"" + servletThreadInfo.activeThreadCnt + "\"}]");
			result.append("]}");
		    
		    System.out.println("- active thread count          : " 
		            + servletThreadInfo.activeThreadCnt);
		}		
		
		//return result.toString();
		return servletThreadInfo.activeThreadCnt+"";
	}
	
}
