package cn.ac.big.circos.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;


import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.ac.big.circos.po.BachxyzBean;
import cn.ac.big.circos.po.GFF3Format;
import cn.ac.big.circos.po.InteractionBean;
import cn.ac.big.circos.po.PhantomBean;
import cn.ac.big.circos.po.PipeBean;
import cn.ac.big.circos.util.BIGWebServiceClientFactory;
import cn.ac.big.circos.util.GenerateCircletConf;
import cn.ac.big.circos.util.GenerateJBrowseConf;
import cn.ac.big.circos.util.GeneratePhyConf;
import cn.ac.big.circos.util.ParamsUtil;
import cn.ac.big.circos.util.ParseOutput;
import cn.ac.big.circos.util.SendEmail;
import cn.ac.big.circos.util.XmlHander;



import com.opensymphony.xwork2.ActionSupport;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PipelineAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PipeBean pipeBean;
	
	private String jobid;
	private String binsize;
	private String chrom;
	private String modelstr;
	
	private String path="";

	private List<GFF3Format> gffList;
	private List<InteractionBean> ibList;
	private List<BachxyzBean> xyzList;

	private int resflag;
	private int fasthicFlag;
	private int bachFlag;
	private String startTime;
	private String endTime;
	

	/**************************************************************
	 * EXEC WHOLE hic pipeline
	 * here we need to store all the parameters into a properties file which can be parse by showResult action
	 * @return
	 */
	public String execHicPipelineFunc(){
		//for TAD tree, need to generate the control file
		//the path is used to store the configuration file
	//	String path="D:\\";
	
		HttpServletRequest request = ServletActionContext.getRequest();
		String webpath = request.getRealPath("/");
		path= webpath + File.separator+"data";
		
		
		
		
		if(pipeBean != null){
			//write tadtree controlfile
	
			jobid = pipeBean.getJobid();
			binsize = pipeBean.getBinsize();
			int startbin = Integer.parseInt(pipeBean.getStartbin());
			int endbin = Integer.parseInt(pipeBean.getEndbin());
			int ibin = Integer.parseInt(binsize);
			int istart = startbin*ibin;
			int iend = endbin*ibin;
			pipeBean.setChromStart(istart);
			pipeBean.setChromEnd(iend) ;
			
			pipeBean.setMaxObserv("0");
			pipeBean.setMinObserv("0");
					
			
			String tadpath = path+File.separator+jobid+File.separator+"tadtree" ;
			
			File file = new File(tadpath);
			if(file.exists() == false){
				file.mkdirs();
			}
			

			try{
				
				int chromlength=0;
			
				StringBuffer sb = new StringBuffer();
				
				//generate jbrowse file
				
				String genomepath = path+File.separator+jobid+File.separator+"genome" ;
				file = new File(genomepath);
				if(file.exists() == false){
					file.mkdirs();
				}
				
				String jbrowsepath = webpath+File.separator+"jbrowse/"+jobid ;
				file = new File(jbrowsepath);
				if(file.exists() == false){
					file.mkdirs();
				}
				
				//find out the chromosome length of organism
				String jsonpath = "";
				jsonpath = ParamsUtil.PUBDATA_PATH+"/jb_template/species_gff/"+pipeBean.getOrganism()+".gff3";

				ParseOutput pout = new ParseOutput();
				BufferedReader br = new BufferedReader(new FileReader(jsonpath));
				String line="";
				while(( line=br.readLine())!=null){
					if(line.startsWith(pipeBean.getChrom())){
						GFF3Format gff = pout.parseGff3(line);
						chromlength = Integer.parseInt(gff.getEnd()) - Integer.parseInt(gff.getStart())+1;
						break;
					}
				}
				
			//	chromlength = ( Integer.parseInt(pipeBean.getEndbin()) - Integer.parseInt(pipeBean.getStartbin()) +1) * Integer.parseInt(pipeBean.getBinsize());
				
				sb.append("#!/bin/sh\n");
				sb.append("export PATH=$PATH:/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514\n");
				sb.append("nohup perl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/module_genome.pl -c ").append(pipeBean.getChrom());
				sb.append(" -l ").append(chromlength+"");
				sb.append(" -o1 ").append(genomepath);
				sb.append(" -s ").append(pipeBean.getOrganism());
				sb.append(" -mn ").append(pipeBean.getMinObserv());
				sb.append(" -mx ").append(pipeBean.getMaxObserv());
				sb.append(" -tpath ").append(webpath);
								
				if(pipeBean.getUseTest() != null && pipeBean.getUseTest().equals("1")){
					sb.append(" -i ").append(pipeBean.getMatrixFile());
				}else{
					sb.append(" -i ").append(path).append(File.separator).append(jobid).append(File.separator).append("upload").append(File.separator).append(pipeBean.getMatrixFile());
					
				}
				sb.append(" -o ").append(jbrowsepath);
				sb.append(" -b ").append(pipeBean.getBinsize());
				sb.append(" -sb ").append(pipeBean.getStartbin());
				sb.append(" -eb ").append(pipeBean.getEndbin());
				sb.append(" -spos ").append(pipeBean.getStartPosition());
				sb.append(" > ").append(genomepath).append(File.separator).append("nohup.out 2>&1 &").append("\n");
				
				
				String shpath = genomepath+File.separator+"process_genome.sh";				
				BufferedWriter bw = new BufferedWriter(new FileWriter(shpath));
				bw.write(sb.toString());
				
				bw.close();
				Runtime.getRuntime().exec("chmod 700 "+shpath); //shell path
				Thread.sleep(4);
				Runtime.getRuntime().exec(shpath);
				
				
				//generate the tad tree shell file
				sb = new StringBuffer();
				
				sb.append("#!/bin/sh\n");
				sb.append("export PATH=$PATH:/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514\n");
				sb.append("nohup perl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/module_tadtree.pl -c ").append(pipeBean.getChrom());
				sb.append(" -s ").append(pipeBean.getMaxbin());
				sb.append(" -g ").append(pipeBean.getGamma());
				sb.append(" -n ").append(pipeBean.getTadnumber());
				sb.append(" -p ").append(pipeBean.getPval());
				sb.append(" -q ").append(pipeBean.getQval());
				if(pipeBean.getUseTest() != null && pipeBean.getUseTest().equals("1")){
					sb.append(" -i ").append(pipeBean.getMatrixFile());
				}else{
				
				sb.append(" -i ").append(path).append(File.separator).append(jobid).append(File.separator).append("upload").append(File.separator).append(pipeBean.getMatrixFile());
				}
				sb.append(" -o ").append(tadpath);
				sb.append(" -o1 ").append(jbrowsepath);
				sb.append(" -b ").append(pipeBean.getBinsize());
				sb.append(" -sb ").append(pipeBean.getStartbin()).append(" -eb ").append(pipeBean.getEndbin());
				sb.append(" -spos ").append(pipeBean.getStartPosition());
				sb.append(" > ").append(tadpath).append(File.separator).append("nohup.out 2>&1 &").append("\n");
				
				
				shpath = tadpath+File.separator+"process_tad.sh";				
				bw = new BufferedWriter(new FileWriter(shpath));
				bw.write(sb.toString());
				
				bw.close();
				Runtime.getRuntime().exec("chmod 700 "+shpath); //shell path
				Thread.sleep(4);
				Runtime.getRuntime().exec(shpath);
				

				
				
				//generate fasthic execution file				
				String fastpath =  path+File.separator+jobid+File.separator+"fasthic";
				file = new File(fastpath);
				if(file.exists() == false){
						file.mkdirs();
				}
				
				sb = new StringBuffer();
				sb.append("#!/bin/sh\n");
				sb.append("export PATH=$PATH:/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514\n");
				
				sb.append("nohup perl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/module_fasthic.pl -c ").append(pipeBean.getChrom());
				sb.append(" -pl ").append("/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/");
				sb.append(" -b ").append(pipeBean.getBinsize());
				sb.append(" -sp ").append(pipeBean.getOrganism());
				if(pipeBean.getUseTest() != null && pipeBean.getUseTest().equals("1")){
					sb.append(" -i ").append(pipeBean.getMatrixFile());
				}else{
				sb.append(" -i ").append(path).append(File.separator).append(jobid).append(File.separator).append("upload").append(File.separator).append(pipeBean.getMatrixFile());
				}
				sb.append(" -o ").append(fastpath);
				sb.append(" -o1 ").append(jbrowsepath);
				sb.append(" -sb ").append(pipeBean.getStartbin());
				sb.append(" -spos ").append(pipeBean.getStartPosition());
				sb.append(" -tpath ").append(webpath);
				sb.append(" -fval ").append(pipeBean.getFastpval());
				sb.append(" > ").append(fastpath).append(File.separator).append("nohup.out 2>&1 &").append("\n");
				
				
				//sb.append("touch ").append(fastpath).append(File.separator).append("fasthic.finish\n");
				
				String fastsh = fastpath+File.separator+"process_fasthic.sh" ;
				bw = new BufferedWriter(new FileWriter(fastsh));
				bw.write(sb.toString());
				bw.close();
				
				Runtime.getRuntime().exec("chmod 700 "+fastsh);	//shell path
				Thread.sleep(4);
				Runtime.getRuntime().exec(fastsh);
				
				if(pipeBean.getPhysicalModel().equals("BACH")){
					//generate bach				
					String bachpath = path+File.separator+jobid+File.separator+"bach";
					file = new File(bachpath);
					if(file.exists() == false){
							file.mkdirs();
					}
					
		
					sb = new StringBuffer();
					sb.append("#!/bin/sh\n");
					sb.append("export PATH=$PATH:/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514\n");
					
					sb.append("nohup perl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/module_bach.pl -i ");
					if(pipeBean.getUseTest() != null && pipeBean.getUseTest().equals("1")){
						sb.append(pipeBean.getMatrixFile());
					}else{
						sb.append(path).append(File.separator).append(jobid).append(File.separator).append("upload").append(File.separator).append(pipeBean.getMatrixFile());
						
					}
					sb.append(" -e ").append(pipeBean.getEnzyme());
					sb.append(" -k ").append(pipeBean.getNumparticle());
					sb.append(" -mp ").append(pipeBean.getNumenrich());
					sb.append(" -ng ").append(pipeBean.getSampleiter());
					sb.append(" -nt ").append(pipeBean.getInterval());
					sb.append(" -l ").append(pipeBean.getStepsize());
					sb.append(" -spos ").append(pipeBean.getChromStart());
					sb.append(" -epos ").append(pipeBean.getChromEnd());
					sb.append(" -rl ").append(pipeBean.getReadslen());
					sb.append(" -seed 1");
					sb.append(" -sp ").append(pipeBean.getOrganism());
					sb.append(" -c ").append(pipeBean.getChrom());
					sb.append(" -jb ").append(jobid );
					sb.append(" -o ").append(bachpath);
					sb.append(" -b ").append(pipeBean.getBinsize());
					sb.append(" -sb ").append(pipeBean.getStartbin());
					sb.append(" -tpath ").append(webpath);
					
					sb.append(" > ").append(bachpath).append(File.separator).append("nohup.out 2>&1 &").append("\n");
					String bachsh = bachpath+File.separator+"process_bach.sh" ;
					bw = new BufferedWriter(new FileWriter(bachsh));
					bw.write(sb.toString());
					bw.close();
					
					Runtime.getRuntime().exec("chmod 700 "+bachsh);	//shell path
					Thread.sleep(4);
					Runtime.getRuntime().exec(bachsh);
					
				}else if(pipeBean.getPhysicalModel().equals("MOGEN")){
					String mogenpath = path+File.separator+jobid+File.separator+"mogen";
					file = new File(mogenpath);
					if(file.exists() == false){
							file.mkdirs();
					}
								
					
					sb = new StringBuffer();
					sb.append("#!/bin/sh\n");
					sb.append("export PATH=$PATH:/share/disk1/work/bioinformatics/tangbx/hic/scripts20160514\n");
					sb.append("nohup perl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514/module_MOGEN.pl -pl /share/disk1/work/bioinformatics/tangbx/hic/scripts20160514");
					sb.append(" -c ").append(pipeBean.getChrom());
					if(pipeBean.getUseTest() != null && pipeBean.getUseTest().equals("1")){
						sb.append(" -obm ").append(pipeBean.getMatrixFile());
					}else{
					sb.append(" -obm ").append(path).append(File.separator).append(jobid).append(File.separator).append("upload").append(File.separator).append(pipeBean.getMatrixFile());
					}
					
					sb.append(" -bin ").append(pipeBean.getBinsize());
					sb.append(" -o ").append(mogenpath);
					sb.append(" -adist ").append(pipeBean.getMogenAdjacentDist());
					sb.append(" -cdist ").append(pipeBean.getMogenContactDist());
					sb.append(" -pmdist ").append(pipeBean.getMogenPosMinDist());
					sb.append(" -nmdist ").append(pipeBean.getMogenNegMaxDist());
					sb.append(" -lrate ").append(pipeBean.getMogenLearnRate());
					sb.append(" -miterate 200000");
					sb.append(" -pmxdw ").append(pipeBean.getMogenPosMaxDistWeight());
					sb.append(" -pmindw ").append(pipeBean.getMogenPosMinDistWeight());
					sb.append(" -nmindw ").append(pipeBean.getMogenNegMinDistWeight());
					sb.append(" -nmxdw ").append(pipeBean.getMogenNegMaxDistWeight());
					sb.append(" -sb ").append(pipeBean.getStartbin());
					sb.append(" -tpath ").append(webpath);
					sb.append(" -jb ").append(jobid );
					sb.append(" -sp ").append(pipeBean.getOrganism());
					sb.append(" > ").append(mogenpath).append(File.separator).append("nohup.out 2>&1 &").append("\n");
					
					String mogensh = mogenpath+File.separator+"process_MOGEN.sh" ;
					bw = new BufferedWriter(new FileWriter(mogensh));
					bw.write(sb.toString());
					bw.close();					
					Runtime.getRuntime().exec("chmod 700 "+mogensh);	//shell path
					Thread.sleep(4);
					Runtime.getRuntime().exec(mogensh);
				}
				
				
				//store all of the parameters
				String configfile =  path+File.separator+jobid+File.separator + jobid+".xml" ;
				XmlHander.writeObject2Xml(configfile,pipeBean);
				
				
				
				Client client = BIGWebServiceClientFactory.getClient();
	             
	            WebResource r = client.resource(ParamsUtil.WS_URL+ "/ws/newtask/post");
	            ClientResponse response = r.type(MediaType.TEXT_PLAIN_TYPE).post(ClientResponse.class, jobid);
	            String output = response.getEntity(String.class);
			
				
			    
			    SendEmail.doSendEmail(jobid, pipeBean.getEmail());
			    
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}	
		return SUCCESS;
	}
	
	
	/******************************************
	 * this is used to process use own data to generate view's configuration file
	 * @return
	 */
	public String execUseOwnDataFunc(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String webpath = request.getRealPath("/");
			jobid = pipeBean.getJobid();
			
			this.binsize = pipeBean.getBinsize();
			int startbin = Integer.parseInt(pipeBean.getStartbin());
			int endbin = Integer.parseInt(pipeBean.getEndbin());
			int ibin = Integer.parseInt(binsize);
			int istart = startbin*ibin;
			int iend = endbin*ibin;
			pipeBean.setChromStart(istart);
			pipeBean.setChromEnd(iend) ;
			
			
			
			String tadfile = "";
			String loopfile= "";
			String modelfile = "";
			String hicmatrixfile = "";
			
			if(pipeBean.getUseTest() != null && pipeBean.getUseTest().equals("1")){
				tadfile = pipeBean.getTadFile();
				loopfile = pipeBean.getMatrixFile();
				modelfile = pipeBean.getFeatureFile();
				hicmatrixfile = pipeBean.getHicMatrixFile();
			}else{
				 tadfile = webpath+"/data/"+this.jobid+"/upload/"+pipeBean.getTadFile();
				 loopfile = webpath+"/data/"+this.jobid+"/upload/"+pipeBean.getMatrixFile();
				 modelfile = webpath+"/data/"+this.jobid+"/upload/"+pipeBean.getFeatureFile();
				 hicmatrixfile = webpath+"/data/"+this.jobid+"/upload/"+pipeBean.getHicMatrixFile();
			}
			System.out.println("tad file ="+tadfile);
			System.out.println("loop file ="+loopfile);
			System.out.println("3dmodel file ="+modelfile);
			System.out.println("matrix file ="+hicmatrixfile);
			//create jbrowse configuration
			
			GenerateJBrowseConf jbconf = new GenerateJBrowseConf(webpath,pipeBean.getOrganism(),this.jobid,pipeBean.getChrom(),tadfile,loopfile,hicmatrixfile,pipeBean);
			jbconf.generateConf();
			
			
			//create circlet configuration
			
			GenerateCircletConf circonf = new GenerateCircletConf(webpath,pipeBean.getOrganism(),this.jobid,pipeBean.getBinsize(),pipeBean.getChrom(),loopfile); 
			circonf.generateConf();
			
			//create physical configuration
			GeneratePhyConf phyconf = new GeneratePhyConf(webpath,this.jobid, pipeBean.getOrganism(),pipeBean.getChrom(),pipeBean.getBinsize(),pipeBean.getStartbin(),modelfile);			
			phyconf.generateConf();
			
			
			
			String configfile =  webpath+File.separator+"data"+File.separator+jobid+File.separator + jobid+".xml" ;
			XmlHander.writeObject2Xml(configfile,pipeBean);
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
		return SUCCESS;
	}
	
	
	
	
	
	
	/********************************************************
	 * this is used to transfer result
	 * @return
	 */
	public String execShowResultFunc(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String webpath = request.getRealPath("/");
		path= webpath + File.separator+"data";
			
			//store all of the parameters
			String configfile =  path+File.separator+jobid+File.separator + jobid+".xml" ;
			this.pipeBean = (PipeBean)XmlHander.xmlString2Object(configfile, "cn.ac.big.circos.po.PipeBean");
			
		
			return SUCCESS;
	}
	
	
	
	public String execShowMyResultFunc(){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String webpath = request.getRealPath("/");
		path= webpath + File.separator+"data";
			
		//store all of the parameters
		String configfile =  path+File.separator+jobid+File.separator + jobid+".xml" ;
		this.pipeBean = (PipeBean)XmlHander.xmlString2Object(configfile, "cn.ac.big.circos.po.PipeBean");
		
		return SUCCESS;
	}
	
	
	
	
	
	/********************************************************************
	 * this is used to show TAD result
	 * @return
	 */
	public String execShowTADResultFunc(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String webpath = request.getRealPath("/");
		path= webpath + File.separator+"data";
		resflag =1;
		//need to check finish file and show result
		try{
			String tadpath = path+File.separator+jobid+File.separator+"tadtree" ;
			File logfile = new File(tadpath+File.separator+"nohup.out");
			if(logfile.exists() == true){ //exist
				BufferedReader logbr = new BufferedReader(new FileReader(tadpath+File.separator+"nohup.out"));
				String cons = logbr.readLine();
				if(cons.endsWith(".manager.local") == true){
					resflag = 2;
				}
			}
			
		
			File file = new File(tadpath+File.separator+"tad.finish");
			if(file.exists() == true){
				resflag = 3;
				String resfile = tadpath+File.separator+"tad.gff3" ;
				System.out.println("tad res file="+resfile);
				
				gffList = new ArrayList<GFF3Format>();
				file = new File(resfile);
				String line="";
				BufferedReader br = new BufferedReader(new FileReader(resfile));
				ParseOutput parseout = new ParseOutput();
				int count = 1 ;
				while((line=br.readLine()) != null){
					if(count < 100){
						GFF3Format gff3res = parseout.parseGff3(line);
						gffList.add(gff3res);
					}
					count ++;
				}
				br.close();
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return SUCCESS;
	}
	

	/***************************************************
	 * ajax tad result
	 * @return
	 */
	public String execAjaxTADResultFunc(){
		resflag =1;
		HttpServletRequest request = ServletActionContext.getRequest();
		String webpath = request.getRealPath("/");
		path= webpath + File.separator+"data";
		//need to check finish file and show result
		try{
			String tadpath = path+File.separator+jobid+File.separator+"tadtree" ;
			File logfile = new File(tadpath+File.separator+"nohup.out");
			if(logfile.exists() == true){ //exist
				BufferedReader logbr = new BufferedReader(new FileReader(tadpath+File.separator+"nohup.out"));
				String cons = logbr.readLine();
				if(cons.endsWith(".manager.local") == true){
					resflag = 2;
				}
			}
			
		
			File file = new File(tadpath+File.separator+"tad.finish");
			if(file.exists() == true){
				resflag = 3;
				file = new File(tadpath+File.separator+"tad.out");
				BufferedReader br = new BufferedReader(new FileReader(tadpath+File.separator+"tad.out"));
				String line="";
				String last = "";
				String start = br.readLine();
				
				while((line=br.readLine()) != null){
					last = line;
				}
				br.close();
				
				long bdate = Long.parseLong(start);
				
				long edate = Long.parseLong(last);
				
				long dural = edate- bdate;
				
				endTime = ParamsUtil.compuateTime(dural);
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
				
		return SUCCESS;
	}
	
	
	
	
	/********************************************************
	 * this is used to show hic interaction result
	 * @return
	 */
	
	public String execShowPeakResultFunc(){
		try{
			resflag = 1;
			HttpServletRequest request = ServletActionContext.getRequest();
			String webpath = request.getRealPath("/");
			path= webpath + File.separator+"data";
			String tadpath = path+File.separator+jobid+File.separator+"fasthic" ;
			
			File logfile = new File(tadpath+File.separator+"nohup.out");
			if(logfile.exists() == true){ //exist
				BufferedReader logbr = new BufferedReader(new FileReader(tadpath+File.separator+"nohup.out"));
				String cons = logbr.readLine();
				if(cons.endsWith(".manager.local") == true){
					resflag = 2;
				}
			}
			
					
			File file = new File(tadpath+File.separator+"fasthic.finish");
			if(file.exists() == true){
				resflag =3;
				String resfile = tadpath+File.separator+pipeBean.getChrom()+".gff3.tabix1" ;
				ibList = new ArrayList<InteractionBean>();
				file = new File(resfile);
				String line="";
				BufferedReader br = new BufferedReader(new FileReader(resfile));
				ParseOutput parseout = new ParseOutput();
				int count =1;
				while((line=br.readLine()) != null){
					if(count < 100){
						String []arrs = line.split("\\s+");
						
						if(arrs.length == 9){
							
							String [] notearry = arrs[8].split(";");
							if(notearry != null){
								for(String attr: notearry){
									
									if(attr.startsWith("Note")){
										
										String [] noteattr = attr.split("=");
										if(noteattr!=null){ //Note=1:1000000-2000000|1:39000000-40000000
											int index1 = noteattr[1].indexOf("|");
											String peakattr = noteattr[1].substring(0,index1);
											String tattr = noteattr[1].substring(index1+1,noteattr[1].length());
											
											
											
												int anindex = peakattr.indexOf(":");
												int anindex1 = peakattr.indexOf("-");
												
												InteractionBean ib = new InteractionBean();
												ib.setAnchorchr(peakattr.substring(0,anindex)) ;
												ib.setAnchorstart(peakattr.substring(anindex+1, anindex1));
												ib.setAnchorend(peakattr.substring(anindex1+1, peakattr.length())) ;

												anindex = tattr.indexOf(":");
												anindex1 = tattr.indexOf("-");
												
												ib.setTargetchr(tattr.substring(0,anindex));
												ib.setTargetstart(tattr.substring(anindex+1, anindex1));											
												ib.setTargetend(tattr.substring(anindex1+1, tattr.length()));
												ibList.add(ib);
											
											
										}
									}
								}
							}
						
							
						}
						
						
						
						
						
					}else{
						break;
					}
					
					
					count ++;
					
				}
				br.close();
				
				//generate configuration file
			
		
				
				String confilepath = webpath+File.separator+"userconf"+File.separator+"circos"+File.separator+jobid;
				file = new File(confilepath);
				if(file.exists() == false){
					file.mkdirs();
				}
				
				
				String conffile = confilepath+File.separator+"circos.conf";
				BufferedWriter bw = new BufferedWriter( new FileWriter(conffile));
				bw.write("[dataset."+jobid+"]\n");
				bw.write("name="+jobid+"\n");
				bw.write("conf=conf/circlet/"+jobid+".conf\n");	
	     		bw.close();
	     		
	     		conffile = webpath+"/conf/circlet/"+jobid+".conf";
	     		String toomanyfeature="";
	     		String statisdata="";
	     		if(pipeBean.getBinsize() !=null){
	     			int ibean = Integer.parseInt(pipeBean.getBinsize());
	     			if(ibean/10000 ==0 ){ // 10kb
	     				toomanyfeature = "1000000";
	     				statisdata = "200000";
	     			}else if(ibean/100000 ==0){ //100kb 
	     				toomanyfeature = "10000000";
	     				statisdata = "2000000";
	     			}else if(ibean/1000000 == 0){ //1000kb
	     				toomanyfeature = "50000000";
	     				statisdata = "10000000";
	     			}else if(ibean/10000000 >= 0){ // 10mb
	     				toomanyfeature = "300000000";
	     				statisdata = "30000000";
	     			}
	     			
	     		}
	     		
	     		bw = new BufferedWriter( new FileWriter(conffile));
	     		if(pipeBean!=null){
	     			
	     				//read public templdate data
		     		BufferedReader br1 = new BufferedReader (new FileReader(webpath+"/pub_template/circlet_template/pubdata_"+pipeBean.getOrganism()+".conf"));
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
	     		}	
	     			     		
	     		bw.write("\n[interaction]\n");
	     		bw.write("feature=arc\n");
	     		bw.write("glyph_type=arc\n");
	     		bw.write("fileClass=GFF3\n");
	     		bw.write("storage="+tadpath+"\n"); //modify storage
	     		bw.write("histone_bin=200000\n");
	     		bw.write("statis_file="+tadpath+"/statics/"+statisdata+"\n"); //statis file
	     		bw.write("color=rgba(128,0,128,0.5)\n");
	     		bw.write("line_width=1\n");
	     		bw.write("height=50\n");
	     		bw.write("key=Interaction\n");
	     		bw.write("category=My track\n");
	     		
	     		bw.close();
				
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	
	/*******************************************************************
	 * ajax peak result
	 * @return
	 */
	public String execAjaxPeakResultFunc(){
		try{
			resflag = 1;
			HttpServletRequest request = ServletActionContext.getRequest();
			String webpath = request.getRealPath("/");
			path= webpath + File.separator+"data";
			String tadpath = path+File.separator+jobid+File.separator+"fasthic" ;
			
			File logfile = new File(tadpath+File.separator+"nohup.out");
			if(logfile.exists() == true){ //exist
				BufferedReader logbr = new BufferedReader(new FileReader(tadpath+File.separator+"nohup.out"));
				String cons = logbr.readLine();
				if(cons.endsWith(".manager.local") == true){
					resflag = 2;
				}
			}
			
					
			File file = new File(tadpath+File.separator+"fasthic.finish");
			if(file.exists() == true){
				resflag =3;
				file = new File(tadpath+File.separator+"fasthic.out");
				BufferedReader br = new BufferedReader(new FileReader(tadpath+File.separator+"fasthic."+this.chrom+".out"));
				String line="";
				String last = "";
				String start = br.readLine();
				
				while((line=br.readLine()) != null){
					last = line;
				}
				br.close();
				//2016年 08月 16日 星期二 17:56:20 HKT
				
				long bdate = Long.parseLong(start);
				
				long edate = Long.parseLong(last);
				
				long dural = edate- bdate;
			   
				endTime = ParamsUtil.compuateTime(dural);
				
				
			
				System.out.println("=======endtime-="+endTime);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	
	/***********************************************************
	 * this is used to show bach result
	 * @return
	 */
	public String execShowBachResultFunc(){
		
		try{
			resflag = 1;
			HttpServletRequest request = ServletActionContext.getRequest();
			String webpath = request.getRealPath("/");
			path= webpath + File.separator+"data";
			
			String finishfile = "";
			String bachpath = "";
			String resfile = "";
		
			if(this.modelstr != null ){
				System.out.println(this.modelstr);
				if(this.modelstr.equals("BACH")){
					bachpath = path+File.separator+jobid+File.separator+"bach";
					finishfile = bachpath +File.separator+"bach.finish";
					resfile = bachpath+File.separator+pipeBean.getChrom()+File.separator+pipeBean.getChrom()+".xyz" ;
				}else if(this.modelstr.equals("MOGEN")){
					bachpath = path+File.separator+jobid+File.separator+"mogen";
					finishfile = bachpath +File.separator+"mogen.finish";
					resfile = bachpath+File.separator+pipeBean.getChrom()+File.separator+pipeBean.getChrom()+".xyz" ;
				}
			}
			
		
			
			File logfile = new File(bachpath+File.separator+"nohup.out");
			if(logfile.exists() == true){ //exist
				BufferedReader logbr = new BufferedReader(new FileReader(bachpath+File.separator+"nohup.out"));
				String cons = logbr.readLine();
				if(cons.endsWith(".manager.local") == true){
					resflag = 2;
				}
			}
			
			System.out.println(resfile);
			System.out.println(finishfile);
			
			File file = new File(finishfile);
			if(file.exists() == true){
				resflag =3; //success
				
				
				xyzList = new ArrayList<BachxyzBean>();
				file = new File(resfile);
				String line="";
				BufferedReader br = new BufferedReader(new FileReader(resfile));
				ParseOutput parseout = new ParseOutput();
				line=br.readLine();
				line=br.readLine();
				
				int count =1;
				while((line=br.readLine()) != null){
					if(count <100){
						BachxyzBean xz = parseout.parseBachXYZ(line);
						xyzList.add(xz);
					}
					
					count ++;
				}
				br.close();
				
				//generate configure file
				
				String confilepath = webpath+File.separator+"userconf"+File.separator+"physical"+File.separator+jobid;
				file = new File(confilepath);
				if(file.exists() == false){
					file.mkdirs();
				}
				
				
				String conffile = confilepath+File.separator+"physical.conf";
				BufferedWriter bw = new BufferedWriter( new FileWriter(conffile));
				bw.write("[dataset."+jobid+"]\n");
				bw.write("name="+jobid+"\n");
				bw.write("conf=conf/physical/"+jobid+".conf\n");	
	     		bw.close();
	     		
	     		
	     		
	     		
	     		/*
	     		bw = new BufferedWriter( new FileWriter(conffile));
	     		if(pipeBean!=null){
	     			if(pipeBean.getOrganism() != null && pipeBean.getOrganism().equals("hsa")){
	     				bw.write("chroms=/circosweb/json/species/human_refseq.json\n");
	     			}else if(pipeBean.getOrganism() != null && pipeBean.getOrganism().equals("mma")){
	     				bw.write("chroms=/circosweb/json/species/mouse_refseq.json\n");
	     			}
	     		}
	     		
	     		bw.write("["+jobid+"]\n");
	     		bw.write("glyph_type=3dmodel\n");
	     		bw.write("storage=xyz\n");
	     		bw.write("bin="+pipeBean.getBinsize()+"\n");
	     		bw.write("startbin="+pipeBean.getStartbin()+"\n");
	     		bw.write("file=/circosweb/data/"+jobid+"/bach/batch.xyz\n");
	     		bw.write("color=white\n");
	     		bw.write("line_width=1\n");
	     		bw.write("key=3dmodel\n");
	     		bw.write("category=My Track\n");
	     		
	     		bw.close();
				*/
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
		return SUCCESS;
	}
	
	
	/***********************************************************
	 * ajax bach result
	 * @return
	 */
	public String execAjaxBachResultFunc(){
		try{
			resflag = 1;
			HttpServletRequest request = ServletActionContext.getRequest();
			String webpath = request.getRealPath("/");
			path= webpath + File.separator+"data";
			String bachpath="";
			String finishfile="";
			String resfile="";
			
			if(this.modelstr != null ){
				System.out.println(this.modelstr);
				if(this.modelstr.equals("BACH")){
					bachpath = path+File.separator+jobid+File.separator+"bach";
					finishfile = bachpath +File.separator+"bach.finish";
					resfile = bachpath+File.separator+"bach."+this.chrom+".out" ;
				}else if(this.modelstr.equals("MOGEN")){
					bachpath = path+File.separator+jobid+File.separator+"mogen";
					finishfile = bachpath +File.separator+"mogen.finish";
					resfile = bachpath+File.separator+this.chrom+File.separator+"MOGEN."+this.chrom+".out" ;
				}
			}
			
			
			
			
			File logfile = new File(bachpath+File.separator+"nohup.out");
			if(logfile.exists() == true){ //exist
				BufferedReader logbr = new BufferedReader(new FileReader(bachpath+File.separator+"nohup.out"));
				String cons = logbr.readLine();
				if(cons.endsWith(".manager.local") == true){
					resflag = 2;
				}
			}
			
			
			
			File file = new File(finishfile);
			if(file.exists() == true){
				resflag =3; //success
				
				//get start time and end time for bach
				file = new File(resfile);
				BufferedReader br = new BufferedReader(new FileReader(resfile));
				String line="";
				String last = "";
				String start = br.readLine();
				
				while((line=br.readLine()) != null){
					last = line;
				}
				br.close();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long bdate = Long.parseLong(start);
				
				long edate = Long.parseLong(last);
				
				long dural = edate- bdate;

				endTime = ParamsUtil.compuateTime(dural);
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	


	public String getBinsize() {
		return binsize;
	}

	public void setBinsize(String binsize) {
		this.binsize = binsize;
	}




	public PipeBean getPipeBean() {
		return pipeBean;
	}

	public void setPipeBean(PipeBean pipeBean) {
		this.pipeBean = pipeBean;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}


	public List<GFF3Format> getGffList() {
		return gffList;
	}


	public void setGffList(List<GFF3Format> gffList) {
		this.gffList = gffList;
	}


	public List<InteractionBean> getIbList() {
		return ibList;
	}


	public void setIbList(List<InteractionBean> ibList) {
		this.ibList = ibList;
	}


	public int getResflag() {
		return resflag;
	}


	public void setResflag(int resflag) {
		this.resflag = resflag;
	}


	public List<BachxyzBean> getXyzList() {
		return xyzList;
	}


	public void setXyzList(List<BachxyzBean> xyzList) {
		this.xyzList = xyzList;
	}


	public int getFasthicFlag() {
		return fasthicFlag;
	}


	public void setFasthicFlag(int fasthicFlag) {
		this.fasthicFlag = fasthicFlag;
	}


	public int getBachFlag() {
		return bachFlag;
	}


	public void setBachFlag(int bachFlag) {
		this.bachFlag = bachFlag;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getChrom() {
		return chrom;
	}


	public void setChrom(String chrom) {
		this.chrom = chrom;
	}


	public String getModelstr() {
		return modelstr;
	}


	public void setModelstr(String modelstr) {
		this.modelstr = modelstr;
	}

	
}
