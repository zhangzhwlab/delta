package cn.ac.big.circos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**************************************************************
 * this is used to generate cirlet view configuration file
 * @author lenovo
 *
 */
public class GenerateCircletConf {
	private String path;
	private String jobid;
	private String organism;
	private String loopfile;
	private String chrom;
	private String binsize;
	
	public GenerateCircletConf(String path, String organism,String jobid,String binsize,String chrom,String loopfile){
		this.path = path;
		this.jobid = jobid;
		this.organism = organism;
		this.chrom = chrom;
		this.loopfile = loopfile;
		this.binsize = binsize;
	}
	
	public int generateConf(){
		
		try{
			File sourcefile = new File(loopfile);
			String  targetpath = path+"/data/"+this.jobid+"/fasthic";
			File tempfile = new File(targetpath);
			if(tempfile.exists() == false){
				tempfile.mkdirs();
			}
			
			String targetfilepath = targetpath+"/"+this.chrom+".gff3.tabix1";
			File targetfile = new File(targetpath+"/"+this.chrom+".gff3.tabix1");
	
			FileUtils.copyFile(sourcefile, targetfile) ;

			
			
		 
			//generate tabix index
			
			
						
			String confilepath = path+File.separator+"userconf"+File.separator+"circos"+File.separator+jobid;
			File file = new File(confilepath);
			if(file.exists() == false){
				file.mkdirs();
			}
			
			
			String conffile = confilepath+File.separator+"circos.conf";
			BufferedWriter bw = new BufferedWriter( new FileWriter(conffile));
			bw.write("[dataset."+jobid+"]\n");
			bw.write("name="+jobid+"\n");
			bw.write("conf=conf/circlet/"+jobid+".conf\n");	
     		bw.close();
     		
     		conffile = path+"/conf/circlet/"+jobid+".conf";
     		String toomanyfeature="";
     		String statisdata="";
     		if(binsize !=null){
     			int ibean = Integer.parseInt(binsize);
     			if(ibean/10000 ==0 ){ // 10kb
     				toomanyfeature = "1000000";

     			}else if(ibean/100000 ==0){ //100kb 
     				toomanyfeature = "10000000";
     			}else if(ibean/1000000 == 0){ //1000kb
     				toomanyfeature = "50000000";
     			}else if(ibean/10000000 >= 0){ // 10mb
     				toomanyfeature = "300000000";
     			}
     			
     		}
     		
     		
     		bw = new BufferedWriter( new FileWriter(conffile));
     		
     		
     			
 				//read public templdate data
     		BufferedReader br1 = new BufferedReader (new FileReader(path+"/pub_template/circlet_template/pubdata_"+this.organism+".conf"));
     		String line1="";
     		while((line1 =br1.readLine()) != null){
     					if(line1.startsWith("toomanyFeature") == true){
     						line1 = "toomanyFeature="+toomanyfeature;
     					}
     					if(line1.equals("[interaction]") == true){
     						br1.readLine();br1.readLine();
     						br1.readLine();br1.readLine();
     						br1.readLine();br1.readLine();
     						br1.readLine();br1.readLine();
     						br1.readLine();
     						br1.readLine();
     						br1.readLine();
     					}else{
     						bw.write(line1+"\n");
     					}
     					
     					
     		}
     		br1.close();
 			
     			     		
     		bw.write("\n[interaction]\n");
     		bw.write("feature=arc\n");
     		bw.write("glyph_type=arc\n");
     		bw.write("fileClass=GFF3\n");
     		bw.write("storage="+targetpath+"\n"); //modify storage
     		bw.write("histone_bin=200000\n");
     		bw.write("color=rgba(128,0,128,0.5)\n");
     		bw.write("line_width=1\n");
     		bw.write("height=50\n");
     		bw.write("key=Interaction\n");
     		bw.write("category=My track\n");
     		
     		bw.close();
     		
     		
     		String shellfile = targetpath+ "/process_fasthic.sh";
     		bw = new BufferedWriter(new FileWriter(shellfile));
     		bw.write("sort -n -k3,4 "+targetfilepath+" > "+targetfilepath+".srt\n");
     		bw.write("/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/bgzip "+targetfilepath+".srt\n");
     		bw.write("/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/tabix -p gff -s 1 -b 4 -e 5 "+targetfilepath+".srt.gz\n");
			bw.close();
			
			Runtime.getRuntime().exec("chmod 700 "+shellfile);
			
			Client client = BIGWebServiceClientFactory.getClient();
             
            WebResource r = client.resource(ParamsUtil.WS_URL+ "/ws/newtask/postshell");
            
            ClientResponse response = r.type(MediaType.TEXT_PLAIN_TYPE).post(ClientResponse.class, shellfile);
			
     		
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return 0;
	}

}
