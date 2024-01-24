package com.newgen.iforms.user;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;

import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.mvcbeans.model.WorkdeskModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//Successfully cloned from github

public class RLOS_Call implements IFormServerEventHandler {
	// private static Logger logger = Logger.getLogger("iFormLogs");


	@Override
	public void beforeFormLoad(FormDef arg0, IFormReference arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public String executeCustomService(FormDef arg0, IFormReference arg1, String arg2, String arg3, String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray executeEvent(FormDef arg0, IFormReference arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeServerEvent(IFormReference formObject, String controlName, String eventType,
			String stringData) {
		
		if(controlName.equalsIgnoreCase("date_diff")){
		System.out.println("I am in");
		
		//String last=(String) formObject.getValue("last_date");
		//System.out.println(last);
		
		String last="5/12/2022";
		System.out.println(last);
		
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        
        Date d=new Date();
        String format = sdf.format(d);
        System.out.println(format);        
        Date d1=null;
		try {
			d1 = sdf.parse(last);
			System.out.print(d1);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Date d2=null;
		try {
			d2 = sdf.parse(format);
			System.out.print(d2);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    long timeDiff = Math.abs(d2.getTime() - d1.getTime());
        long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
    
  
        String datediff = String.valueOf(daysDiff);
        formObject.setValue("gap_days",datediff);
		
		
			
		}
		
		
		if (controlName.equalsIgnoreCase("send_closure_Mail")) {

			String mailTo = (String) formObject.getTableCellValue("table7",0,5);
			String mailSubject="Foreclosure simulation report";
			
			
			String body ="<html>"
					+ "<body>"
					+ "<p>Hello Brijesh,</p>"
					+ "<p>Thanks for availing the Loan services with us.</p>"
					+ "<p>Please find attached Foreclosure simulation as requested by you.</p>"
					+ "<p>\r\n"
					+ "<p>Kindly contact us at 080-49007654 for any further queries. You can also write us on xyz@bank.com.</p>"
				
					+ "<p>Regards,</p>"
					+ "<p>Bank Admin Team</p>"
				
					+ "</body>\r\n"
					+ "</html>";
			
			
			
			String query = "EXEC NG_LOS_MAIL_PROCEDURE '"+body+"','"+mailTo+"','"+mailSubject+"','"+formObject.getObjGeneralData().getM_strProcessInstanceId()+"','"+formObject.getActivityName()+"','Y','C:\\Newgen Products\\jboss-eap-7.2\\bin\\SOF_Config\\Foreclosure_Report.pdf'";
	    	System.out.println(query);
	    	int statusCode =  formObject.saveDataInDB(query);
	    	System.out.println(statusCode);
	    	return "DONE";
		}
		
		if(controlName.equalsIgnoreCase("base64")){
			JSONObject json=new JSONObject();
			json.put("cabinetName","sacom");
			json.put("docIndex",eventType);
			json.put("siteId","1");
			json.put("volumeId","1");
			json.put("userName","supervisor");
			
			json.put("userPassword","supervisor351");
			json.put("local","en_US");
			System.out.println(json);
			String url="http://demo-tytlmsnsg.centralindia.cloudapp.azure.com:8080/OmniDocsRestWS/rest/services/getDocumentJSON";
			System.out.println(url);
			
			//String WI=formObject.getObjGeneralData().getM_strProcessInstanceId()
			String response =verifyData(json,url,formObject);
			System.out.println(response);
			JSONParser jsonParser=new JSONParser();
			try {
				
				JSONObject json1= (JSONObject) jsonParser.parse(response);
				JSONObject json2=(JSONObject) json1.get("NGOGetDocumentBDOResponse");
				
				System.out.println(json2);
				
				String code=(String) json2.get("statusCode");
				System.out.println(code);
				if(code.equalsIgnoreCase("0")){
					String docContent=(String) json2.get("docContent");
					System.out.println(docContent);    // base 64 //
					
					
					//converting base 64 into pdf file //
					byte[] bytes=Base64.getDecoder().decode(docContent);
					System.out.println(bytes);
					System.out.println("Hi this is file");
					//File files=new File()
					FileOutputStream fos=new FileOutputStream("C:\\Newgen Products\\jboss-eap-7.2\\bin\\SOF_Config\\Foreclosure_Report.pdf");
					System.out.println("HELLO");
					fos.write(bytes);
					fos.close();
					
					Send_Mail_base64(formObject);
				}
				
				// TODO: handle exception
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
		    	
	    return "";	
		
	}
	
	
	public String verifyData(JSONObject json,String url1,IFormReference formObject){
		
	HttpURLConnection httpConn = null;
	URLConnection connection = null;
	OutputStream out = null;
    BufferedReader in = null;
    String responseString ="";
    String responseJSon="";
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try {
        URL url = new URL(url1);
        connection = url.openConnection();
         httpConn = (HttpURLConnection) connection;

            httpConn.setRequestProperty("Content-Type", "application/json");
         
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(json.toJSONString().getBytes());
            System.out.println("Response received");
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((responseString = in.readLine()) != null) {
                    responseJSon = responseJSon + responseString;
                }
            } else {
                in = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), "UTF-8"));
                while ((responseString = in.readLine()) != null) {
                    responseJSon = responseJSon + responseString;
                    



                }
            }
    } catch (IOException e) {
        e.printStackTrace();
        responseJSon = "{\"status-code\":\"601\"}";

    }

    return responseJSon;
    
    
    }
	
	
	public String Send_Mail_base64(IFormReference formObject){


		String mailTo = (String) formObject.getTableCellValue("table7",0,5);
		String mailSubject="Foreclosure simulation report";
		
		
		String body ="<html>"
				+ "<body>"
				+ "<p>Hello Brijesh,</p>"
				+ "<p>Thanks for availing the Loan services with us.</p>"
				+ "<p>Please find attached Foreclosure simulation as requested by you.</p>"
				+ "<p>\r\n"
				+ "<p>Kindly contact us at 080-49007654 for any further queries. You can also write us on xyz@bank.com.</p>"
			
				+ "<p>Regards,</p>"
				+ "<p>Bank Admin Team</p>"
			
				+ "</body>\r\n"
				+ "</html>";
		
		
		
		String query = "EXEC NG_LOS_MAIL_PROCEDURE '"+body+"','"+mailTo+"','"+mailSubject+"','"+formObject.getObjGeneralData().getM_strProcessInstanceId()+"','"+formObject.getActivityName()+"','Y','C:\\Newgen Products\\jboss-eap-7.2\\bin\\SOF_Config\\Foreclosure_Report.pdf'";
    	System.out.println(query);
    	int statusCode =  formObject.saveDataInDB(query);
    	System.out.println(statusCode);
    	return "DONE";
	
	}
	


	
	@Override
	public String getCustomFilterXML(FormDef arg0, IFormReference arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setMaskedValue(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public JSONArray validateSubmittedForm(FormDef arg0, IFormReference arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public static String getSelectresponse(String sQuery, String requiredValue) {

		System.out.println("Inside getSelectresponse()....");

		java.sql.Connection con = null;
		java.sql.Statement s = null;
		java.sql.ResultSet rs = null;
		String resultReturned = "";
		String url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=sacom";
		String id = "sa";
		String pass = "system123#";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = java.sql.DriverManager.getConnection(url, id, pass);
			System.out.println("Connection con : " + con);
		} catch (ClassNotFoundException cnfex) {
			cnfex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			s = con.createStatement();
			System.out.println("Statement s : " + s);
			rs = s.executeQuery(sQuery);
			System.out.println("ResultSet rs : " + rs);
			while (rs.next()) {
				resultReturned = rs.getString(requiredValue);
				System.out.println("resultReturned rs : " + resultReturned);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultReturned;
	}

	public static String getWrapperResponse(String inputXMLString) {
		String jtsIP, port, strResponse;
		int jtsPort = 0;
		jtsIP = "127.0.0.1"; // JTSIP
		port = "3333";
		jtsPort = Integer.parseInt(port); // JTSPORT
		strResponse = "";
		try {
			Socket sock = null;
			try {
				sock = new Socket(jtsIP, jtsPort);
				System.out.println("Socket Obj : " + sock);
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
			DataOutputStream oOut = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
			System.out.println("oOut : " + oOut);
			DataInputStream oIn = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
			System.out.println("oIn : " + oIn);
			byte[] SendStream = inputXMLString.getBytes("8859_1");
			System.out.println("SendStream : " + SendStream);
			int strLen = SendStream.length;
			System.out.println("strLen : " + strLen);
			oOut.writeInt(strLen);
			oOut.write(SendStream, 0, strLen);
			oOut.flush();
			int length = 0;
			length = oIn.readInt();
			byte[] readStream = new byte[length];

			oIn.readFully(readStream);
			strResponse = new String(readStream, "8859_1");
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResponse;
	}

	
	public static void main(String[] s) {}

	public static File getOutputFile(String path) {

		System.out.println("reading output file from path:" + path);
		File file = null;
		file = new File(path);
		return file;

	}

	

	

	 public String onChangeEventServerSide(IFormReference ifr, String Control) {
	        String retStr = "";

	        System.out.println("Hi vijay");
	        return retStr;
	    }
	
	@Override
	public String generateHTML(EControl arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String introduceWorkItemInWorkFlow(IFormReference arg0, HttpServletRequest arg1, HttpServletResponse arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String introduceWorkItemInWorkFlow(IFormReference arg0, HttpServletRequest arg1, HttpServletResponse arg2,
			WorkdeskModel arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean introduceWorkItemInSpecificProcess(IFormReference arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String postHookExportToPDF(IFormReference arg0, File arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postHookOnDocumentOperations(IFormReference arg0, String arg1, String arg2, int arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHookOnDocumentUpload(IFormReference arg0, String arg1, String arg2, File arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDataInWidget(IFormReference arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String validateDocumentConfiguration(String arg0, String arg1, File arg2, Locale arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
