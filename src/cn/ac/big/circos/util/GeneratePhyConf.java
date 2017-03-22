package cn.ac.big.circos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;

/*********************************************
 * this is used to generate physical configuration file
 * @author lenovo
 *
 */
public class GeneratePhyConf {
	private String path;
	private String jobid;
	private String organism;
	private String chrom ;
	private String binsize;
	private String modelfile;
	private String startbin;
	public GeneratePhyConf(String path,String jobid, String organism,String chrom,String binsize,String startbin,String modelfile){
		this.path = path;
		this.jobid = jobid;
		this.organism = organism;
		this.chrom = chrom;
		this.binsize = binsize;
		this.modelfile = modelfile;
		this.startbin = startbin;
	}
	
	
	public int generateConf(){
		try{
			
			String confilepath = path+File.separator+"userconf"+File.separator+"physical"+File.separator+jobid;
			File file = new File(confilepath);
			if(file.exists() == false){
				file.mkdirs();
			}
			
			
			String conffile = confilepath+File.separator+"physical.conf";
			BufferedWriter bw = new BufferedWriter( new FileWriter(conffile));
			bw.write("[dataset."+jobid+"]\n");
			bw.write("name="+jobid+"\n");
			bw.write("conf=conf/physical/"+jobid+".conf\n");	
     		bw.close();
     		
     		
     		//here , we need to generate the jobid file
     		String temppath = "/share/disk1/work/bioinformatics/tangbx/webdb/circosweb2016/circosweb/pub_template/physical_template/";
     		String tempfilepath = "";
     		if(this.organism != null ){
     			tempfilepath = temppath + "temp_"+this.organism+"_physical.conf" ;
     		}
     		
     		
     		
     		String targetfilepath = path+ "/conf/physical/"+jobid+".conf";

     		//here, we need to do some replace operations
     		BufferedReader br = new BufferedReader(new FileReader(tempfilepath));
     		
     		bw = new BufferedWriter(new FileWriter(targetfilepath));

     		String line="";
     		while((line=br.readLine()) != null){
     			line = line.replaceAll("\\{startbin\\}", this.startbin);
     			line = line.replaceAll("\\{modeltype\\}", "bach");
     			line = line.replaceAll("\\{jobid\\}", this.jobid);
     			 			
 				line = line.replaceAll("\\{refseq\\}", this.chrom) ;
 				line = line.replaceAll("\\{modelref\\}", this.chrom) ;		
 				line = line.replaceAll("\\{binsize\\}", binsize) ;
     			
     			bw.write(line+"\n");
     		}
     		
     		
     		br.close();
     		bw.close();
     		
     		
     		//bach file
     		String bachpath = path+"/data/"+jobid+"/bach/"+this.chrom;
     		File bachdir = new File(bachpath);
     		if(bachdir.exists() == false){
     			bachdir.mkdirs();
     		}
     		File sourceFile = new File(modelfile);
     		File targetfile = new File(bachpath+"/"+this.chrom+".xyz");
			FileUtils.copyFile(sourceFile, targetfile) ;
     		
     		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return 0;
	}
	
	
	

}
