package cn.ac.big.circos.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class DownloadFileAction extends ActionSupport
implements ServletResponseAware, ServletRequestAware{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -5934846817146688699L;
	private HttpServletResponse response;
	 private HttpServletRequest request;
	 
	 private int flag; // this is used to identify which file is needed to be downloaded
	 private String jobid;
	 
	 
	 /*******************************************************
	  * download file
	  */
	 public void execDownloadFileFunc(){
		 
		try{
			
			String path = request.getRealPath("/");
			String downloadfile="";
			String filename = "";
			if(flag == 1){ // tadtree
				downloadfile = path+File.separator+"data"+File.separator+jobid+File.separator+"tadtree"+File.separator+"tad.gff3";
				filename = "tad.gff3";
			}else if(flag == 2){ //fasthic
				downloadfile = path+File.separator+"data"+File.separator+jobid+File.separator+"fasthic"+File.separator+"10.gff3.tabix1";
				filename = "tad.gff3";
			}else if(flag == 3){ //bach
				downloadfile = path+File.separator+"data"+File.separator+jobid+File.separator+"bach"+File.separator+"mode_p.txt";
				filename = "mode_p.txt";
			}
			
			 response.setContentType("text/plain");   
			 response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   

		 	FileInputStream fileInputStream=new java.io.FileInputStream(downloadfile);  
			 PrintWriter out = response.getWriter();
			 int i;   
			 while ((i=fileInputStream.read()) != -1) {  
				 out.write(i);   
			 }   
			 fileInputStream.close(); 
			 out.close();

		 }
		 catch(Exception e)
		 {
		     System.out.println(e.getLocalizedMessage());
		 }

	 }
	  
	 
	 
	public void setFlag(int flag) {
		this.flag = flag;
	}



	public void setJobid(String jobid) {
		this.jobid = jobid;
	}


	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
		
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		this.response = arg0;
	}

}
