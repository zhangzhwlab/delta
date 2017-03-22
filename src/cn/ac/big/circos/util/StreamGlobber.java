package cn.ac.big.circos.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import cn.ac.big.circos.po.ChromatinInter;

public class StreamGlobber extends Thread{
	  private InputStream is;  
	  private  String      type;  
	  private  OutputStream os;  
	  private List<ChromatinInter> data;
 
	    
	  public StreamGlobber(InputStream is, String type, OutputStream redirect,List<ChromatinInter> data) {  
	        this.is = is;  
	        this.type = type;  
	        this.os = redirect;  
	        this.data = data;
	  } 
	  
	  

	  
	  public void run() {  
	        try {  
	            PrintWriter pw = null;  
	            if (os != null)  
	                pw = new PrintWriter(os);  
	            InputStreamReader isr = new InputStreamReader(is);  
	            BufferedReader br = new BufferedReader(isr);  
	            String line = null;  
                ParseOutput parse = new ParseOutput();
	            while ((line = br.readLine()) != null) {  
	                if (pw != null){  
	                    pw.println(line);
	                }
	                	//System.out.println(line);
	                
	                  data.add(parse.parseChromatinOutput(line)) ;
	                	  
	                	
	            }  
	            if (pw != null)  
	                pw.flush();  
	        } catch (Exception ioe) {  
	            ioe.printStackTrace();  
	        }  
	    }  
}
