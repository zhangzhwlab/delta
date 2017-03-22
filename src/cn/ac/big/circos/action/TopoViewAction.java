package cn.ac.big.circos.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import cn.ac.big.circos.po.ChromatinInter;
import cn.ac.big.circos.po.GFF3Format;
import cn.ac.big.circos.po.PeakBean;
import cn.ac.big.circos.service.IBaseService;
import cn.ac.big.circos.util.BIGWebServiceClientFactory;
import cn.ac.big.circos.util.CategoryTrack;
import cn.ac.big.circos.util.CircosDataset;
import cn.ac.big.circos.util.CircosTrack;
import cn.ac.big.circos.util.ParamsUtil;
import cn.ac.big.circos.util.ParseOutput;




//this used to process topological view action
public class TopoViewAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ChromatinInter> data;
	private List<String> qualityData;
	private List<GFF3Format> gffData;
	
	private int start;
	private int stop;
	private String chrom;
	private int chromLen;
	private String storageFile;
	private String param;
	
	private String speciesJson;  //species json store all the chromosome information
	private String ideogramJson; // species ideogram bandlst json file
	private int threshValue ; // this used to store thresh hold value
	private int histoneBinSize; // histone bin size 
	private String binsize; // resoluztion size

	private String jsonStr;
	private String datatype; //data type such as K562POL2,H3K4me3
	
	private Float minVal; // min value
	private Float maxVal; // max value
	
	private List<CircosDataset> datasetList;
	private String curDataset; // current choosed dataset
	
	
	private String conf; // store user defined configuration file 
	
	private String myFileFileName;
	private File myFile;
	private String myFileContentType;
	
	private CircosTrack circosTrack;
	
	@Resource(name="baseService")
	private IBaseService baseSerivce;
	
	public CircosTrack getCircosTrack() {
		return circosTrack;
	}

	public void setCircosTrack(CircosTrack circosTrack) {
		this.circosTrack = circosTrack;
	}

	private List<CategoryTrack> categoryList;

	
	/*****************************************************
	 * this used to process init topological view action
	 * the circos.conf and the according configure file need to locate in the Root file
	 * @return
	 */
	public String execInitFunc(){
		HttpServletRequest request= ServletActionContext.getRequest() ;
		String path = request.getRealPath("/");
		
		String filepath = path+File.separator+"circos.conf";
		if(conf!= null && conf.length() >0 ){				
			filepath = path+File.separator+"userconf"+File.separator+"circos"+File.separator+conf+File.separator+"circos.conf";				
		}
		System.out.println(filepath);
		try{
			File file = new File(filepath);
			if(file.exists() == true ){
				datasetList = new ArrayList<CircosDataset>();
				BufferedReader br = new BufferedReader(new FileReader(filepath));
				String line = "";
				while((line = br.readLine()) != null){
					if(line.startsWith("#") == true){
						continue;
					}
					if(line.startsWith("[") && line.endsWith("]")){
						
						CircosDataset cdataset = new CircosDataset();
						
						line = br.readLine(); // next line
						if(line.startsWith("name")){
							String [] arrs = line.split("=");
							cdataset.setName(arrs[1]) ;
						}
						line = br.readLine();
						if(line.startsWith("conf")){
							String [] arrs = line.split("=");
							cdataset.setConf(arrs[1]);
						}
						
						datasetList.add(cdataset) ;
					}
				}
				
				br.close();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/***********************************************************************
	 * exec load configuration file for given dataset
	 * then we need to get tracks configuration file and output the result file as a json file
	 * @return
	 */
	
	public String execLoadDatasetConfigFunc(){
		HttpServletRequest request= ServletActionContext.getRequest() ;
		String path = request.getRealPath("/");
		String filepath = path+File.separator+this.curDataset;
		System.out.println(filepath);
		try{
			File file = new File(filepath);
			if(file.exists() == true ){
				BufferedReader br = new BufferedReader(new FileReader(filepath));
				String line = "";
				Map map = new HashMap();
				
				while((line = br.readLine()) != null ){
					if(line.startsWith("#")){
						continue ;
					}
					if(line.startsWith("chroms") == true){
						String [] arr = line.split("=");
						this.speciesJson = arr[1];
					}
					if(line.startsWith("ideogram") == true){
						String [] arr = line.split("=");
						this.ideogramJson = arr[1];
					}
					if(line.startsWith("toomanyFeature") == true){
						String [] arr = line.split("=") ;
						this.threshValue = Integer.parseInt(arr[1]) ;
					}
					
					
					if(line.startsWith("[") && line.endsWith("]")){
						CircosTrack track = new CircosTrack();
						while((line = br.readLine())!=null && (line.length()>0)){
							String [] arrs = line.split("=");
							if(arrs[0].startsWith("feature")){
							   track.setFeature(arrs[1]) ;	
							}else if(arrs[0].startsWith("glyph_type")){
								track.setGlyph(arrs[1]) ;
							}else if(arrs[0].startsWith("storage")){
								track.setStorage(arrs[1]);
							}else if(arrs[0].startsWith("color")){
								track.setColor(arrs[1]) ;
							}else if(arrs[0].startsWith("key")){
								track.setKey(arrs[1]);
							}else if(arrs[0].startsWith("category")){
								track.setCategory(arrs[1]) ;
							}else if(arrs[0].startsWith("height")){
								track.setHeight(arrs[1]);
							}else if(arrs[0].startsWith("pcolor")){
								track.setPcolor(arrs[1]);
							}else if(arrs[0].startsWith("ncolor")){
								track.setNcolor(arrs[1]);
							}else if(arrs[0].startsWith("line_width")){
								track.setLineWidth(arrs[1]);
							}else if(arrs[0].startsWith("statis_file")){
								track.setStatisFile(arrs[1]); // statistics file
							}else if(arrs[0].startsWith("histone_bin")){
								track.setHistoneBin(Integer.parseInt(arrs[1])) ;
							}else if(arrs[0].startsWith("fileClass")){
								//System.out.println("storageclass="+arrs[1]);
								track.setStoreclass(arrs[1]) ;
							}else if(arrs[0].startsWith("toomany")){
								track.setToomany(Integer.parseInt(arrs[1]));
							}else if(arrs[0].startsWith("bin_size")){
								track.setBinsize(arrs[1].trim());
							}else if(arrs[0].startsWith("table")){
								track.setTable(arrs[1].trim());
							}
							else if(arrs[0].startsWith("organism")){
								track.setOrganism(arrs[1]) ;								
							}
						}
						
						if(map.get(track.getCategory()+"_"+track.getOrganism()) == null ){
							CategoryTrack ctrack = new CategoryTrack(track.getCategory(),track.getOrganism());
							ctrack.addTrack(track);
							map.put( track.getCategory()+"_"+track.getOrganism(), ctrack );							
						}else if(map.get(track.getCategory()+"_"+track.getOrganism()) != null){
							CategoryTrack ctrack = (CategoryTrack) map.get(track.getCategory()+"_"+track.getOrganism()) ;
							ctrack.addTrack(track) ;							
						}
						
					}
				}
				
				br.close();
				
				
			    //Iterator Map
				if(map.size()>0){
					categoryList = new ArrayList<CategoryTrack>();
				}
				
				Iterator iter = map.entrySet().iterator();
				while(iter.hasNext()){
					Entry entry = (Entry)iter.next();
					CategoryTrack ctrack = (CategoryTrack) entry.getValue();
					categoryList.add(ctrack) ;
				}
				
				//generate json string
				
				String jsonstr = JSONArray.fromObject(categoryList).toString();
				String outfile = path+File.separator+"json"+File.separator+this.curDataset+".json";
				BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
				bw.write(jsonstr) ;
				bw.close();
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	

	/********************************************
	 * this used to get chromatin data
	 * we use tabix to get the search data
	 * then we need to process the data to json format
	 * when the query scope large then then given range, the statics interaction file(file format will be the same as the custom chromatin interaction data)
	 * will be loaded.
	 * 
	 * @return
	 */
	public String  execChromatinDataFunc(){
		System.out.println("exec get chromation data");
		String target_file = storageFile;
		File file = new File(target_file) ;
		if(file.isFile() == false){
			File [] files = file.listFiles();
			if(files!=null){
				for(File tfile:files){
					String name = tfile.getName();
					String [] namearr = name.split("\\.");
					if(namearr!=null){
						if(namearr[0].equals(chrom) && name.endsWith(".gz")){
							target_file = storageFile+"/"+name;
							break;
						}
					}
				}
			}

		}

		
	//	String cmd = "/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/tabix  "+target_file+" "+chrom+":"+start+"-"+stop;
	//
		try{
			
			Client client = BIGWebServiceClientFactory.getClient();
	        WebResource r = client.resource(ParamsUtil.WS_URL + "/ws/newtask/tabix");
	        FormDataMultiPart fdmp = new FormDataMultiPart();
	        fdmp.bodyPart(new FormDataBodyPart("filepath", target_file));
            fdmp.bodyPart(new FormDataBodyPart("pos",chrom+":"+start+"-"+stop));
	        
	        ClientResponse response =  r.type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class,fdmp);
	        InputStream in   = response.getEntityInputStream();
	        InputStreamReader isr = new InputStreamReader(in);
	        
        //   Runtime rt = Runtime.getRuntime();  
         //  Process proc = rt.exec(cmd);  

        //    InputStreamReader isr = new InputStreamReader(proc.getInputStream());  
            
            
	        data = new ArrayList<ChromatinInter>();
            
            BufferedReader br = new BufferedReader(isr);  
            String line = null;  
            ParseOutput parse = new ParseOutput();
            while ((line = br.readLine()) != null) {  
            	  String [] arrs = line.split("\\s+");
            	  if(arrs.length == 9){
            		  data.add(parse.parseFromCustomGff3Interaction(line));	
            	  }else{
            		  data.add(parse.parseChromatinOutput(line)) ;
            	  }	                  	
            }  
            isr.close();
            br.close();
            
            //get json file
           
            if(file.isFile() == false){
            	String jsonfile = storageFile+File.separator+"peak.json";
            	file = new File(jsonfile);
            	
            	if(file.exists() == true){
            		line = null ;
            		br = new BufferedReader(new FileReader(jsonfile));
            		line = br.readLine();
            		if(line != null){
            			JSONArray array = JSONArray.fromObject(line);
            			List<ChromatinInter> list = new ArrayList<ChromatinInter>();
            			 for(Iterator iter = array.iterator(); iter.hasNext();){      
            		            JSONObject jsonObject = (JSONObject)iter.next();      
            		            ChromatinInter current = (ChromatinInter)JSONObject.toBean(jsonObject, ChromatinInter.class); 
            		            List<ChromatinInter> target =  new ArrayList<ChromatinInter>();
            		            for (Object obj : current.getTarget()) {
            		            	ChromatinInter module = (ChromatinInter) JSONObject.toBean(JSONObject
            		    					.fromObject(obj), ChromatinInter.class);
            		            	target.add(module);
            		            }   
            		            current.setTarget(target);
            		            list.add(current);
            			  }
            			
            			
            				if(list != null && list.size() >0 ){
            			
            				if(data.size() >0 ){
            					for(ChromatinInter datainter:data){
            						
            						for(ChromatinInter listinter: list){
            						
            							if(datainter.getStart()== listinter.getStart() && datainter.getAnchorEnd() == listinter.getAnchorEnd() && datainter.getAnchorChr().equals(datainter.getAnchorChr())){
            								datainter.setTarget(listinter.getTarget());
            								break;
          								
            							}
            						}
            					}            					            					
            				}            				            				
            			}            			           			
            		}
            		br.close();
            		
            	}
            } 
           
            
           
          
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
			
		return "success";
	}
	
	
	
	/**********************************************************
	 * this is used to get interaction from mysql
	 * @return
	 */
	public String  execChromatinDataFromMysqlFunc(){
		System.out.println("exec get chromation data");
	
		
		String line;
	//	String cmd = "/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/tabix  "+target_file+" "+chrom+":"+start+"-"+stop;
	//	
		try{
			Map map = new HashMap();
			map.put("chrom", chrom);
			map.put("start",start);
			map.put("end",stop);
			map.put("table", param);
			if(binsize != null && binsize.equals("-1") == false){
				map.put("binsize", binsize) ;
			}
			List<PeakBean> peaklist = (List<PeakBean>)baseSerivce.findResultList("cn.ac.big.circos.selectPeakList", map);
		
			if(peaklist != null ){
				
				data = new ArrayList<ChromatinInter>();
	            
  
	            ParseOutput parse = new ParseOutput();
	            for(PeakBean peakbean: peaklist) 
	            {  
	            	  line= peakbean.getChrom()+" 0 0 "+peakbean.getStart()+" "+peakbean.getEnd()+" . . . "+peakbean.getNote();
	            	  String [] arrs = line.split("\\s+");
	            	  if(arrs.length == 9){
	            		  data.add(parse.parseFromCustomGff3Interaction(line));	
	            	  }else{
	            		  data.add(parse.parseChromatinOutput(line)) ;
	            	  }	                  	
	            }  
	          
			}
			 
			
            
            //get json file
			String jsonfile = storageFile+File.separator+"peak.json";
			File file = new File(jsonfile);

            if(file.exists() == true){
            		line = null ;
            		BufferedReader br = new BufferedReader(new FileReader(jsonfile));
            		line = br.readLine();
            		if(line != null){
            			JSONArray array = JSONArray.fromObject(line);
            			List<ChromatinInter> list = new ArrayList<ChromatinInter>();
            			 for(Iterator iter = array.iterator(); iter.hasNext();){      
            		            JSONObject jsonObject = (JSONObject)iter.next();      
            		            ChromatinInter current = (ChromatinInter)JSONObject.toBean(jsonObject, ChromatinInter.class); 
            		            List<ChromatinInter> target =  new ArrayList<ChromatinInter>();
            		            for (Object obj : current.getTarget()) {
            		            	ChromatinInter module = (ChromatinInter) JSONObject.toBean(JSONObject
            		    					.fromObject(obj), ChromatinInter.class);
            		            	target.add(module);
            		            }   
            		            current.setTarget(target);
            		            list.add(current);
            			  }
            			
            			
            				if(list != null && list.size() >0 ){
            			
            				if(data.size() >0 ){
            					for(ChromatinInter datainter:data){
            						
            						for(ChromatinInter listinter: list){
            						
            							if(datainter.getStart()== listinter.getStart() && datainter.getAnchorEnd() == listinter.getAnchorEnd() && datainter.getAnchorChr().equals(datainter.getAnchorChr())){
            								datainter.setTarget(listinter.getTarget());
            								break;
          								
            							}
            						}
            					}            					            					
            				}            				            				
            			}            			           			
            		}
            		br.close();
            		
            	}
            
           
            
           
          
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
			
		return "success";
	}
	
	
	
	
	
	/************************************************************
	 * this used to get wiggle data
	 * we use tabix to get the search data
	 * then we need to process the data to json format
	 * @return
	 */
	public String execWiggleDataFunc(){
		//get data quality and transform to json
		System.out.println("exec get quality data");
		String target_file = storageFile;
		try{
				File file = new File(target_file) ;
				if(file.isFile() == false){
					File [] files = file.listFiles();
					for(File tfile:files){
						String name = tfile.getName();
						String [] namearr = name.split("\\.");
						if(namearr!=null){
							if(namearr[0].equals(chrom)){
								
								target_file = storageFile+"/"+name;
								break;
							}
						}
						
					}
				}
				
				
				Client client = BIGWebServiceClientFactory.getClient();
		        WebResource r = client.resource(ParamsUtil.WS_URL + "/ws/newtask/tabix");
		        FormDataMultiPart fdmp = new FormDataMultiPart();
		        fdmp.bodyPart(new FormDataBodyPart("filepath", target_file));
		        fdmp.bodyPart(new FormDataBodyPart("pos",chrom+":"+start+"-"+stop));
		        
		        ClientResponse response =  r.type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class,fdmp);
		        InputStream in   = response.getEntityInputStream();
		        InputStreamReader isr = new InputStreamReader(in);
		        BufferedReader br = new BufferedReader(isr);  
				
			//	String cmd = "/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/tabix "+target_file+" "+chrom+":"+start+"-"+stop;
			//	System.out.println(cmd);
	//
			//	FileOutputStream fos = new FileOutputStream(file);  
	       //     Runtime rt = Runtime.getRuntime();  
	       //     Process proc = rt.exec(cmd);  
	            
	            
	            qualityData = new ArrayList<String>();	           
	           // InputStreamReader isr = new InputStreamReader(proc.getInputStream());  
	          //  BufferedReader br = new BufferedReader(isr);  
	            String line = null;  
	            ParseOutput parse = new ParseOutput();
	            while ((line = br.readLine()) != null) {  	                	                	
	            	qualityData.add(parse.parseDataQuality(line));	                	                	
	            }  
	           isr.close();

			}catch(Exception ex){
				ex.printStackTrace();
			}
		
		return "success";
	}
	
	
	
	/*********************************************************
	 * this used to get histogram statistics data for custom track
	 * the statistics file format 
	 * @return
	 */
	public String execGetHistogramStatisticDataFunc(){
		int interval = this.histoneBinSize ;
		
		float t_min = 0 ; //min  value
		float t_max = 0 ; //max value
		//String path = "/leofs/biocloud/biocloud/his_data/"+datatype+"/"+interval+".txt";
		try{
			String path = this.storageFile ;
		
			File file = new File(path) ;
			if(file.isFile() == false){
				File [] files = file.listFiles();
				for(File tfile:files){
					String name = tfile.getName();
					String [] namearr = name.split("\\.");
					if(namearr!=null){
						if(namearr[0].equals(chrom)){
							
							path = storageFile+"/"+name;
							break;
						}
					}
					
				}
			}
			
			
			int his_begin = start / interval; //200,000
			int his_end = stop / interval;  //200,000
			

			System.out.println(path);

				BufferedReader br = new BufferedReader(new FileReader(path));
				String line = br.readLine();
				if(line != null){
					qualityData = new ArrayList<String>();
					String [] t_arrs = line.split(",");
					t_min =  Float.parseFloat(t_arrs[0]) ;
					t_max = t_min ;
					for(int i=his_begin;i<(his_end+1);i++){
						float t_val = Float.parseFloat(t_arrs[i]);
						if(t_val < t_min){
							t_min = t_val;
						}else if(t_val > t_max){
							t_max = t_val;
						}
						qualityData.add(t_arrs[i]) ;
					}
				}
				br.close();
				
				minVal = t_min;
				maxVal = t_max;

		}catch(Exception ex){
			ex.printStackTrace();
			minVal = t_min;
			maxVal = t_max;
		}
		return SUCCESS;
		
	}
	
	
	/**********************************************************
	 * this used to parse gff3 file which compressed by Tabix
	 * @return
	 */
	public String execParseTabixGff3func(){
		
		if(chrom.startsWith("hs")){
			chrom = chrom.substring(2,chrom.length()) ;
		}
		
		String target_file = storageFile;
		File file = new File(target_file) ;
		if(file.isFile() == false){
			File [] files = file.listFiles();
			if(files!=null){
				for(File tfile:files){
					String name = tfile.getName();
					
					String [] namearr = name.split("\\.");
					if(namearr!=null){
						if(namearr[0].equals(chrom) && name.endsWith(".gz")){							
							target_file = storageFile+"/"+name;
							break;
						}
					}				
				}
			}

		}
		
		
		
		
		
		
		
	//	String cmd = "/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/tabix "+target_file+" "+chrom+":"+start+"-"+stop;
	//	System.out.println(cmd);
		try{
			//	FileOutputStream fos = new FileOutputStream(file);  
	     //       Runtime rt = Runtime.getRuntime();  
	     //       Process proc = rt.exec(cmd);  
				Client client = BIGWebServiceClientFactory.getClient();
		        WebResource r = client.resource(ParamsUtil.WS_URL + "/ws/newtask/tabix");
		        FormDataMultiPart fdmp = new FormDataMultiPart();
		        fdmp.bodyPart(new FormDataBodyPart("filepath", target_file));
		        fdmp.bodyPart(new FormDataBodyPart("pos",chrom+":"+start+"-"+stop));
		        
		        ClientResponse response =  r.type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class,fdmp);
		        InputStream in   = response.getEntityInputStream();
		        InputStreamReader isr = new InputStreamReader(in);
		        BufferedReader br = new BufferedReader(isr);  
	            
		        //here we compute maxval
	            gffData = new ArrayList<GFF3Format>();	           
	         //   InputStreamReader isr = new InputStreamReader(proc.getInputStream());  
	         //   BufferedReader br = new BufferedReader(isr);  
	            String line = null;  
	            ParseOutput parse = new ParseOutput();
	            int count = 0 ;
	            while ((line = br.readLine()) != null) {  
	            	GFF3Format t_gff = parse.parseGff3(line) ;
	            	gffData.add(t_gff);	  
	            	if(count ==0 && t_gff.getScore().equals(".") == false ){
	            		maxVal =Float.parseFloat(t_gff.getScore());
	            		minVal = maxVal;
	            	}else{
	            		if( t_gff.getScore().equals(".") == false ){
	            			float t_q = Float.parseFloat(t_gff.getScore());
		            		if(t_q > maxVal){
		            			maxVal = t_q;
		            		}else if(t_q < minVal){
		            			minVal = t_q;
							}
	            		}
	            		
	            	}
	            	count ++;
	            }  
	           isr.close();
	           
	           List<GFF3Format> templist = parse.formatGFF3File(gffData);
	           
	           gffData = templist;
	           

			}catch(Exception ex){
				ex.printStackTrace();
			}
		return SUCCESS;
	}


	
	/*********************************************************************
	 * this is used to process add track
	 * @return
	 */
	public String execAddTrackFunc(){
		try{
			String targetDirectory = ServletActionContext.getServletContext()
					.getRealPath("/");
			
			String savepath = targetDirectory+"/data/"+circosTrack.getDataset()+"/circlet/uploadtrack";
			int orgflag = 0 ;
			File tempfile = new File(savepath);
			if (tempfile.exists() == false) {
				tempfile.mkdirs();
				
			}else{
				orgflag = 1;
			}
			String targetFileName = myFileFileName;
			if (targetFileName != null) {
				File target = new File(savepath, targetFileName);

				FileUtils.copyFile( myFile, target);
			}
			
			//need to generate configuration file
			String confilepath = targetDirectory+"/userconf/circos/"+circosTrack.getDataset();
			tempfile = new File(confilepath);
			if(tempfile.exists() ==false){
				tempfile.mkdirs();
			}
			String conffile = confilepath+"/circos.conf";
			BufferedWriter bw = new BufferedWriter( new FileWriter(conffile));
			bw.write("[dataset."+circosTrack.getDataset()+"]\n");
			bw.write("name="+circosTrack.getDataset()+"\n");
			bw.write("conf=conf/circlet/"+circosTrack.getDataset()+".conf\n");	
     		bw.close();
     		
     		conffile = targetDirectory+"/conf/circlet/"+circosTrack.getDataset()+".conf";
     		
     		String temp_upfile = targetDirectory+"/pub_template/circlet_template/pubdata_"+circosTrack.getOrganism()+".conf";
     		File tmp_file = new File(temp_upfile);
     		if(tmp_file.exists() == true){
     			File t_confile = new File(conffile);
     			if(t_confile.exists() == false){
     				FileUtils.copyFile( tmp_file, t_confile);
     			}
     		}
     		
     		bw = new BufferedWriter( new FileWriter(conffile,true));
     		     	
     		     		
     		String toomanyfeature="350000000";
     		String statisdata="";
     		String srcfile = savepath+"/"+myFileFileName;
     		//when user upload their own data , we do not do statictics operation for them, we will read data directly
     		
     		bw.write("\n");
     		bw.write("\n");
     		
     		bw.write("["+circosTrack.getName()+"]\n");
     		bw.write("toomanyFeature="+toomanyfeature+"\n");
     		bw.write("fileClass="+circosTrack.getStoreclass()+"\n");
     		bw.write("feature="+circosTrack.getGlyph()+"\n");
     		bw.write("glyph_type="+circosTrack.getGlyph()+"\n");
     		bw.write("storage="+srcfile+".gz\n"); //modify storage
     		bw.write("statis_file="+savepath+"/statics/"+statisdata+"\n"); //statis file
     		bw.write("toomany=1\n");
     		bw.write("color=red\n");
     		bw.write("line_width=1\n");
     		bw.write("key="+circosTrack.getName()+"\n");
     		bw.write("category=Custom Track\n");
     		
     		bw.close();
			
			conf = circosTrack.getDataset();
			
			
			//write tabix index command
			String shellfile = savepath+"/compressdata.sh";
			bw = new BufferedWriter( new FileWriter(shellfile));
			StringBuffer sb = new StringBuffer();
			
			sb.append("/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/bgzip "+srcfile+"\n");//bgzip
			sb.append("/share/disk1/work/bioinformatics/tangbx/hic/software/tabix-master/tabix -p gff -s 1 -b 4 -e 5 "+srcfile+".gz\n");//tabix
			
			bw.write(sb.toString()) ;
			bw.close();
			
			Runtime.getRuntime().exec("chmod 700 "+shellfile);
			
			//call REST to exec the command
			Client client = BIGWebServiceClientFactory.getClient();
            
            WebResource r = client.resource(ParamsUtil.WS_URL+ "/ws/newtask/postshell");
            ClientResponse response = r.type(MediaType.TEXT_PLAIN_TYPE).post(ClientResponse.class, shellfile);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/******************************************************
	 * this is used to download target list of given anchor 
	 */
	public void execDownTargetListFunc(){
		
	}
	

	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getStop() {
		return stop;
	}


	public void setStop(int stop) {
		this.stop = stop;
	}


	public String getChrom() {
		return chrom;
	}


	public void setChrom(String chrom) {
		this.chrom = chrom;
	}


	public void setData(List<ChromatinInter> data) {
		this.data = data;
	}


	public List<ChromatinInter> getData() {
		return data;
	}


	public String getJsonStr() {
		return jsonStr;
	}


	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}


	public List<String> getQualityData() {
		return qualityData;
	}


	public void setQualityData(List<String> qualityData) {
		this.qualityData = qualityData;
	}


	public String getDatatype() {
		return datatype;
	}


	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	
	public Float getMinVal() {
		return minVal;
	}

	public void setMinVal(Float minVal) {
		this.minVal = minVal;
	}

	public Float getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Float maxVal) {
		this.maxVal = maxVal;
	}

	public int getChromLen() {
		return chromLen;
	}


	public void setChromLen(int chromLen) {
		this.chromLen = chromLen;
	}


	public List<CircosDataset> getDatasetList() {
		return datasetList;
	}


	public void setDatasetList(List<CircosDataset> datasetList) {
		this.datasetList = datasetList;
	}

	public String getCurDataset() {
		return curDataset;
	}

	public void setCurDataset(String curDataset) {
		this.curDataset = curDataset;
	}

	public List<CategoryTrack> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryTrack> categoryList) {
		this.categoryList = categoryList;
	}

	public String getStorageFile() {
		return storageFile;
	}

	public void setStorageFile(String storageFile) {
		this.storageFile = storageFile;
	}

	public List<GFF3Format> getGffData() {
		return gffData;
	}

	public void setGffData(List<GFF3Format> gffData) {
		this.gffData = gffData;
	}

	public String getSpeciesJson() {
		return speciesJson;
	}

	public void setSpeciesJson(String speciesJson) {
		this.speciesJson = speciesJson;
	}

	public String getIdeogramJson() {
		return ideogramJson;
	}

	public void setIdeogramJson(String ideogramJson) {
		this.ideogramJson = ideogramJson;
	}

	public int getThreshValue() {
		return threshValue;
	}

	public void setThreshValue(int threshValue) {
		this.threshValue = threshValue;
	}

	public int getHistoneBinSize() {
		return histoneBinSize;
	}

	public void setHistoneBinSize(int histoneBinSize) {
		this.histoneBinSize = histoneBinSize;
	}

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getMyFileContentType() {
		return myFileContentType;
	}

	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getBinsize() {
		return binsize;
	}

	public void setBinsize(String binsize) {
		this.binsize = binsize;
	}
	
	
}
