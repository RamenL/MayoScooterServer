package com.mayo.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONArray;
import org.json.JSONObject;


public class DAOHandler extends AbstractHandler {
	//private static String JDBC_BASE = "jdbc:mysql:// <ip address>:3306/<database name>?user=root&password=<password>";
	//IPv4 Address: 192.168.1.178
	//database: studentdb
	private static String JDBC_BASE = "jdbc:mysql://localhost:3306/scooterdb?useSSL=false";
	private static boolean usage = false;

	public void handle(String arg0, Request arg1, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("application/json");
		String typeRequest = request.getParameter("type");
		PrintWriter writer = response.getWriter();
		if (typeRequest != null) {
			if (typeRequest.equalsIgnoreCase("available")) {
				boolean status = available(writer);
				if (!status) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} else if(typeRequest.equalsIgnoreCase("update")){
				String id = request.getParameter("id");
				String latitude = request.getParameter("latitude");
				String longitude = request.getParameter("longitude");
				boolean status = update(writer, id, latitude, longitude);
				if (!status) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} else if(typeRequest.equalsIgnoreCase("ride")){
				boolean status = ride(writer);
				if (!status) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} 
			else if(typeRequest.equalsIgnoreCase("startScooter")){
				boolean status = startScooter(writer);
				if (!status) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} 
			else if(typeRequest.equalsIgnoreCase("stopScooter")){
				boolean status = stopScooter(writer);
				if (!status) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	private boolean available(PrintWriter writer) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String password = "xxxxx" //original password replaced
		try {
			con = DriverManager.getConnection(JDBC_BASE, "Ranen", password);
			stmt = con.createStatement();
			JSONArray array = new JSONArray();			
			rs = stmt.executeQuery("select * from scooter");
			JSONObject obj = new JSONObject();
			while(rs.next()){
				//System.out.println(rs.getString("id") + ", " + rs.getString("name") + ", " + rs.getString("gpa"));
				obj.put("id", rs.getString("id"));
				obj.put("availability", rs.getString("availability"));
				obj.put("longitude", rs.getString("longitude"));
				obj.put("latitude", rs.getString("latitude"));
				obj.put("user", rs.getString("user"));
				obj.put("location", rs.getString("location")); 
				array.put(obj);
			}			
			writer.write(obj.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean update(PrintWriter writer, String id, String latitude, String longitude) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(JDBC_BASE, "Ranen", "ranen311");
			stmt = con.createStatement();			
			stmt.executeUpdate("update scooter set latitude = " + latitude + ", longitude = " + longitude + " where id = " + id);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean ride(PrintWriter writer) {
		Connection con = null;
		Statement stmt = null;
		String password = "xxxxx"; //original password replaced 
		try {
			con = DriverManager.getConnection(JDBC_BASE, "Ranen", password);
			stmt = con.createStatement();
			JSONArray array = new JSONArray();			
			//DO ME: script to decide which scooter id to use
			
			stmt.executeUpdate("update scooter set availability = 'unavailable' where id = 1");
			setUsage(true);
			JSONObject obj = new JSONObject();
			obj.put("id", 1);
			array.put(obj);		
			writer.write(obj.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean startScooter(PrintWriter writer) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(JDBC_BASE, "Ranen", "ranen311");
			stmt = con.createStatement();
			JSONArray array = new JSONArray();			
			rs = stmt.executeQuery("select * from scooter where id = 1");
			JSONObject obj = new JSONObject();
			while(rs.next()){
				//System.out.println(rs.getString("id") + ", " + rs.getString("name") + ", " + rs.getString("gpa"));
				if(rs.getString("availability").equals("unavailable") && rs.getString("id") == "1"){
					if(!isUsage()){
						System.out.println("isUsage() error in startScooter()");
					}
					
					obj.put("usage", "true");
					array.put(obj);
					break;
				}
			}			
			writer.write(obj.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean stopScooter(PrintWriter writer) {
		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(JDBC_BASE, "Ranen", "ranen311");
			stmt = con.createStatement();
			JSONArray array = new JSONArray();			
			stmt.executeUpdate("update scooter set availability = 'available' where id = 1");
			setUsage(false);
			JSONObject obj = new JSONObject();
			obj.put("usage", "false");
			array.put(obj);		
			writer.write(obj.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static boolean isUsage() {
		return usage;
	}

	public static void setUsage(boolean usage) {
		DAOHandler.usage = usage;
	}

}
