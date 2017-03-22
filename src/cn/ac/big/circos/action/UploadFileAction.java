package cn.ac.big.circos.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

/**************************************************
 * this used to process the upload file use struts to store the uploaf file
 * 
 * @author sweeter
 * 
 */
public class UploadFileAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2099886328983155620L;
	private  String UPLOAD_DIRECTORY="/share/disk1/work/bioinformatics/tangbx/webdb/circosweb2016/circosweb/data";
	private File file;

	private String contentType;
	private String fileName;
	private String curjobid;
	private String fileflag;


	/**********************************************************
	 * this used to process upload file action
	 * 
	 * @return
	 * @throws IOException
	 */
	public String executeUploadFileFunc() throws IOException {
		System.out.println("filename=" + fileName);
		HttpServletRequest request = ServletActionContext.getRequest();
		String webpath = request.getRealPath("/");
		String path= webpath + File.separator+"data";
		UPLOAD_DIRECTORY = path;
		
		String respage = "success";
		try {
			
			String uploadpath =  UPLOAD_DIRECTORY +File.separator+curjobid+File.separator+"upload";
			System.out.println("upload path="+uploadpath);
			
			if(fileName!= null ){
				String filename = fileName;
				 String filepath =  uploadpath + File.separator + fileName;
				 
				 File tempfile = new File(uploadpath);
				  if (tempfile.exists() == false) {
						tempfile.mkdirs();
				  }
				   String targetFileName = fileName;
					if (targetFileName != null) {
						File target = new File(uploadpath, targetFileName);

						FileUtils.copyFile(file, target);
					}
				 
				 
				 
				if(filename.endsWith(".tar.gz")){
                    //	 String time = new Date().getTime()+"";
                
                    	 
                    	 
                    	 BufferedWriter bw = new BufferedWriter(new FileWriter(uploadpath+File.separator+fileflag+".sh"));
                    	 String temfile = filename.substring(0,filename.length() - 7) ; // remove .tar.gz suffix
                    	 String unzippath = uploadpath+File.separator+fileflag;
                    	 bw.write("#!/bin/sh\n");
                    	 bw.write("mkdir -p "+unzippath+"\n");
                    	 bw.write("tar -zxf "+filepath+" -C "+unzippath+"\n");
                    	 bw.write("touch "+uploadpath+File.separator+fileflag+".finish\n");
                    	 
                    	 bw.close();
                    	 Runtime.getRuntime().exec("chmod 700 "+uploadpath+File.separator+fileflag+".sh");
                    	 Runtime.getRuntime().exec(uploadpath+File.separator+fileflag+".sh");
                    	 
                    	 
                    	 File finishfile = new File(uploadpath+File.separator+fileflag+".finish");
                    	 while(true){
                    		 if(finishfile.exists()== false){
                    			 Thread.sleep(50);
                    		 }else{
                    			 break;
                    		 }
                    	 }
                    	 
                    	 String unfilename ="";
                    	 File unfilepath = new File(uploadpath+File.separator+fileflag);
                    	 if(unfilepath.exists()){
                    		 File [] listfile = unfilepath.listFiles();
                    		 if(listfile!=null && listfile.length>0){
                    			 for(File cufile:listfile){
                    				 if(cufile.getName().equals(".")==false &&cufile.getName().equals("..")==false){
                    					 unfilename = cufile.getName();
                    					 break;
                    				 }
                    			 }
                    		 }
                    	 }

                    	 Runtime.getRuntime().exec("mv "+unzippath+File.separator+unfilename+" "+uploadpath+File.separator+temfile);
                        
                    	
                     }else if(filename.endsWith(".zip")){
                    	 String temfile = filename.substring(0,filename.length() - 4) ;
                    	
                    	 
                    	 BufferedWriter bw = new BufferedWriter(new FileWriter(uploadpath+File.separator+fileflag+".sh"));
                  
                    	 String unzippath = uploadpath+File.separator+fileflag;
                    	 bw.write("#!/bin/sh\n");
                    	 bw.write("mkdir -p "+unzippath+"\n");
                    	 bw.write("unzip "+filepath+" -d "+unzippath+"\n");
                    	 bw.write("touch "+uploadpath+File.separator+fileflag+".finish\n");
                    	 
                    	 bw.close();
                    	 Runtime.getRuntime().exec("chmod 700 "+uploadpath+File.separator+fileflag+".sh");
                    	 Runtime.getRuntime().exec(uploadpath+File.separator+fileflag+".sh");
                    	 
                    	 
                    	 File finishfile = new File(uploadpath+File.separator+fileflag+".finish");
                    	 while(true){
                    		 if(finishfile.exists()== false){
                    			 Thread.sleep(50);
                    		 }else{
                    			 break;
                    		 }
                    	 }
                    	 
                    	 String unfilename ="";
                    	 File unfilepath = new File(unzippath);
                    	 if(unfilepath.exists()){
                    		 File [] listfile = unfilepath.listFiles();
                    		 if(listfile!=null && listfile.length>0){
                    			 for(File cufile:listfile){
                    				 if(cufile.getName().equals(".")==false &&cufile.getName().equals("..")==false){
                    					 unfilename = cufile.getName();
                    					 break;
                    				 }
                    			 }
                    		 }
                    	 }

                    	 Runtime.getRuntime().exec("mv "+unzippath+File.separator+unfilename+" "+uploadpath+File.separator+temfile);
                        		                      
                     }
			}
			
			
			

		} catch (Exception ex) {
			ex.printStackTrace();

			
		}

		return respage;
	}
	
	

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setFileFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getCurjobid() {
		return curjobid;
	}



	public void setCurjobid(String curjobid) {
		this.curjobid = curjobid;
	}



	public String getFileflag() {
		return fileflag;
	}

	public void setFileflag(String fileflag) {
		this.fileflag = fileflag;
	}

	
}
