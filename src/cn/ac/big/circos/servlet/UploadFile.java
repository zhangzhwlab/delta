package cn.ac.big.circos.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**********************************************
 * this used to do file upload
 * @author lenovo
 *
 */
public class UploadFile extends HttpServlet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private final String UPLOAD_DIRECTORY = "D:\\";
	private final String UPLOAD_DIRECTORY="/share/disk1/work/bioinformatics/tangbx/webdb/circosweb2016/circosweb/data";
	private String filename ;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    		
    		Object obj = request.getParameter("curjobid");
    		 String fileflag = "1";
    		String uploadpath = "";
    		if(obj != null){
    			String jobid = obj.toString();
    			uploadpath =  UPLOAD_DIRECTORY +File.separator+jobid+File.separator+"upload";
    		}
    		
		    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		    // process only if its multipart content
		    if (isMultipart) {
		    	System.out.println("file upload...");
		            // Create a factory for disk-based file items
		            FileItemFactory factory = new DiskFileItemFactory();
		
		            // Create a new file upload handler
		            ServletFileUpload upload = new ServletFileUpload(factory);
		            try {
		                    // Parse the request
		                    List<FileItem> multiparts = upload.parseRequest(request);
		                    //if(multiparts!=null){
		                    	//System.out.println(multiparts.size());
		                   // }
		                    for (FileItem item : multiparts) {
		                    	//System.out.println(item.isFormField());
		                      if (!item.isFormField()) {
		                         String name = new File(item.getName()).getName();
		                         System.out.println(uploadpath + File.separator + name);
		                         item.write(new File(uploadpath + File.separator + name));
		                         filename = name;
		                         String filepath =uploadpath + File.separator + name;
		                         //here we need to identify where this file has been compressed
		                        
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
		                         
		                      }else if(item.isFormField()){
		                    	  String name= item.getFieldName();
		                    	  System.out.println(name);
		                    	  if(name != null && name.equals("curjobid") == true){
		                    			uploadpath =  UPLOAD_DIRECTORY +File.separator+item.getString()+File.separator+"upload";
		                    			System.out.println("formfield.."+uploadpath);
		                    			File curfile = new File(uploadpath);
		                    			if(curfile.exists()==false){
		                    				curfile.mkdirs();
		                    			}
		                    	  }else if( name!= null && name.equals("fileflag") == true){
		                    		  fileflag = item.getString();
		                    	  }
		                      }
		                    }
		                    System.out.println("File upload success");
		            } 
		            catch (Exception e) 
		            {
		              System.out.println("File upload failed");
		            }
		    }
		    
		  //  request.setAttribute("filename", filename);  
		   // RequestDispatcher de=request.getRequestDispatcher("/pages/pipeline/fileupload.jsp");  
		   // de.forward(request, response); 
		
		}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
    
    
    
}


