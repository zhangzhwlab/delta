package cn.ac.big.circos.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	public static String doSendEmail(String jobid, String email) {
		// TODO Auto-generated method stub
	    try{
				
				System.out.println("send email");
				final String username = "tangbx@big.ac.cn";
				final String password = "zse4xdr5";
				
				String from = "tangbx@big.ac.cn";
				String to = email;
				
				
			    Properties props = new Properties();  
		        props.setProperty("mail.debug", "false");  
		        props.setProperty("mail.smtp.auth", "true");  
		        props.setProperty("mail.host", "mail.big.ac.cn");  
		        props.setProperty("mail.transport.protocol", "pop3");  
	
		        Session session = Session.getInstance(props,
		                new javax.mail.Authenticator() {
		                   protected PasswordAuthentication getPasswordAuthentication() {
		                      return new PasswordAuthentication(username, password);
		       	   }
		        });
	
		        
		     
			    // Create a default MimeMessage object.
			    Message message = new MimeMessage(session);
			     	
			   // Set From: header field of the header.
			   message.setFrom(new InternetAddress(from));
			     	
			   // Set To: header field of the header.
			   message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			  
			   message.setSubject("New Task");
			     	
			   StringBuffer sb = new StringBuffer();
			   sb.append("Dear user,\n\n");
			   sb.append("A new task has been submitted to hic browser with JOBID ").append(jobid).append("\n\n");
			   
			   sb.append("The result can be retrieved by URL http://hicp.big.ac.cn/pipeline/showResult.action?jobid=").append(jobid).append("\n\n");
			   
			   sb.append("This email send by the system automatically,please do not reply this directly.\n");
			   sb.append("\n\n");
			   sb.append("-------------------------------------------------------------------------------------------\n\n");
			   
			   sb.append("Best Regards,\n");
			   sb.append("The Hic Browser Team");
			   message.setText(sb.toString());
	
			   // Send message
			   Transport.send(message);
		        
		}catch(Exception ex){
			ex.printStackTrace();
			return "fail";
		}
	
		return "ok";
	}
}
