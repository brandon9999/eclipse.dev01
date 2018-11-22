package com.kis.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserInfoServlet")
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserInfoServlet() {
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

		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userName = request.getParameter("userName");
		response.getWriter().write(getJSON(userName));
	}
	
	public String getJSON(String userName)
	{
		if(userName == null) userName = "";
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		result.append("[{\"value\": \"" + "1" + "\"},");
		result.append("{\"value\": \"" + "2" + "\"},");
		result.append("{\"value\": \"" + "3" + "\"},");
		result.append("{\"value\": \"" + "4" + "\"}]");
	
		result.append("]}");
		
		System.out.println("getJson");
		
		return result.toString();
	}
	
}
