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

import cn.ac.big.circos.po.GFF3Format;
import cn.ac.big.circos.po.PipeBean;

/*****************************************************************
 * this is used to generate jbrowse configuration file
 * @author lenovo
 *
 */
public class GenerateJBrowseConf {
	private String path;
	private String organism;
	private String jobid;
	private String chrom;
	private String tadfile;
	private String loopfile;
	private String matrixfile;
	private PipeBean pipeBean;
	 
	
	
	public GenerateJBrowseConf(String path,String organism,String jobid,String chrom,String tadfile,String loopfile,String matrixfile,PipeBean pipebean){
		this.path = path;
		this.organism = organism;
		this.jobid = jobid;
		this.chrom = chrom;
		this.tadfile = tadfile;
		this.loopfile = loopfile;
		this.matrixfile = matrixfile;
		this.pipeBean = pipebean;
	}
	
	/********************************************************
	 * generate configuration file
	 * @return
	 */
	public int generateConf(){
		String jbrowseconfig = path+"/jbrowse/"+jobid;
		String jbrowsetemplate = "/share/disk1/work/bioinformatics/tangbx/webdb/circosweb2016/circosweb/pub_template/jb_template";
		String jbtracklist = "";
		
		try{
			File sourcefile = new File(tadfile);
			String  targetpath = path+"/data/"+this.jobid+"/tadtree";
			File tempfile = new File(targetpath);
			if(tempfile.exists() == false){
				tempfile.mkdirs();
			}
			
			File targetfile = new File(targetpath+"/tad.gff3");
			FileUtils.copyFile(sourcefile, targetfile) ;
			
			if(this.organism != null ){
				jbtracklist = jbrowsetemplate + "/temp_"+this.organism+"_trackList.json" ;
			}
			
			
		
			File file = new File(jbrowseconfig);
			if(file.exists() == false){
				file.mkdirs();
				
				//track list json
				BufferedReader br = new BufferedReader(new FileReader(jbtracklist));
				BufferedWriter bw = new BufferedWriter(new FileWriter(jbrowseconfig+"/trackList.json"));
				String line="";
				
				while((line = br.readLine()) != null ) {
					
					line = line.replaceAll("jobid", this.jobid);
					
					bw.write(line+"\n") ;
				}
			
				br.close();
				bw.close();
				
				
				//refseq path refSeqs.json
				String refseqpath = jbrowseconfig+"/seq";
				File reffile = new File(refseqpath);
				if(reffile.exists() == false){
					reffile.mkdirs();
				}
				if(this.organism != null ){
					refseqpath = jbrowsetemplate + "/species_seq/"+this.organism+"/seq/refSeqs.json";
				}
				
			
				File jbfile = new File(refseqpath);
				
				File target = new File(reffile+"/refSeqs.json");
				FileUtils.copyFile(jbfile, target);
				
				
				String genomepath = path+"/data"+File.separator+jobid+File.separator+"genome" ;
				
				System.out.println("===========genomepath="+genomepath);
				file = new File(genomepath);
				if(file.exists() == false){
					file.mkdirs();
				}
				
				String jbrowsepath= path+File.separator+"jbrowse/"+jobid;
				file = new File(jbrowsepath);
				if(file.exists() == false){
					file.mkdirs();
				}
				
				String jsonpath = "";
				jsonpath = ParamsUtil.PUBDATA_PATH+"/jb_template/species_gff/"+pipeBean.getOrganism()+".gff3";
				int chromlength = 0 ;
				ParseOutput pout = new ParseOutput();
			    br = new BufferedReader(new FileReader(jsonpath));
		
				while(( line=br.readLine())!=null){
					if(line.startsWith(pipeBean.getChrom())){
						GFF3Format gff = pout.parseGff3(line);
						chromlength = Integer.parseInt(gff.getEnd()) - Integer.parseInt(gff.getStart())+1;
						break;
					}
				}
				
			
				//tracks
				//matrix file
				StringBuffer sb = new StringBuffer();
				sb.append("#!/bin/sh\n");
				sb.append("export PATH=$PATH:/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514\n");
				sb.append("nohup perl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/module_genome.pl -c ").append(pipeBean.getChrom());
				sb.append(" -l ").append(chromlength+"");
				sb.append(" -o1 ").append(genomepath);
				sb.append(" -s ").append(organism);
				sb.append(" -mn 0");
				sb.append(" -mx 0");
				sb.append(" -tpath ").append(path);
								
				sb.append(" -i ").append(matrixfile);
				sb.append(" -o ").append(jbrowsepath);
				sb.append(" -b ").append(pipeBean.getBinsize());
				sb.append(" -sb ").append(pipeBean.getStartbin());
				sb.append(" -eb ").append(pipeBean.getEndbin());
				sb.append(" -spos ").append(pipeBean.getStartPosition());
				sb.append(" > ").append(genomepath).append(File.separator).append("nohup.out 2>&1 &").append("\n");
				
				
				String shpath = genomepath+File.separator+"process_genome.sh";				
				bw = new BufferedWriter(new FileWriter(shpath));
				bw.write(sb.toString());
				
				bw.close();
				Runtime.getRuntime().exec("chmod 700 "+shpath); //shell path
				Thread.sleep(4);
				Runtime.getRuntime().exec(shpath);
				
				
				String configfile =  path+"/data"+File.separator+jobid+File.separator + jobid+".xml" ;
				XmlHander.writeObject2Xml(configfile,pipeBean);
				
				
				
				Client client = BIGWebServiceClientFactory.getClient();
	             
	            WebResource r = client.resource(ParamsUtil.WS_URL+ "/ws/newtask/post");
	            ClientResponse response = r.type(MediaType.TEXT_PLAIN_TYPE).post(ClientResponse.class, jobid);
	            String output = response.getEntity(String.class);
	            //submit process genome
				
				
				
				//tracks/arc/11
				String tadtrackpath = jbrowseconfig+"/tracks/tad/"+this.chrom;
				File tadfilepath = new File(tadtrackpath);
				if(tadfilepath.exists() == false){
					tadfilepath.mkdirs();
					
					File tadfile = new File(this.tadfile);
					File tadtarget = new File(tadtrackpath+"/tad.gff3");
					FileUtils.copyFile(tadfile, tadtarget);
					
				}
				
				
				//tracks/arc/11
				String arcpath = jbrowseconfig+"/tracks/arc/"+this.chrom;
				File arcfilepath = new File(arcpath);
				if(arcfilepath.exists() == false){
					arcfilepath.mkdirs();
					
					File arcfile = new File(this.loopfile);
					File tadtarget = new File(arcpath+"/arc.gff3");
					FileUtils.copyFile(arcfile, tadtarget);
					
				}
				
				//we will also generate a anchor file for this peakfile
				//anchor file
				br = new BufferedReader(new FileReader(this.loopfile));
				bw = new BufferedWriter(new FileWriter(arcpath+"/anchor.gff3")); // 
				int count =0 ;
				while((line = br.readLine()) != null){
					count ++;
					String [] arrs = line.split("\\s+");
					int loopstart = Integer.parseInt(arrs[3]);
					int loopend = Integer.parseInt(arrs[4]) ;
					int anchorend = loopstart + 100;
					bw.write(arrs[0]+"\thic\tarc\t"+loopstart+"\t"+anchorend+"\t.\t.\t.\tID"+count+";Name="+count+";AnchorStart="+loopstart+";AnchorEnd="+loopend+"\n");
				}
				
				bw.close();
				br.close();
				
				//we will matrix file
				File matrix = new File(jbrowseconfig+"/tracks/tadstatic/"+this.chrom);
				if(matrix.exists() == false){
					matrix.mkdirs();
				}
				
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return 0;
	}

}
