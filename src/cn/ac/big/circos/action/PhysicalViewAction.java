package cn.ac.big.circos.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import cn.ac.big.circos.po.HistoneMarkBean;
import cn.ac.big.circos.po.PhysicalModelBean;
import cn.ac.big.circos.po.PubGeneBean;
import cn.ac.big.circos.po.SearchFormBean;
import cn.ac.big.circos.service.DrawPhysicalPicService;
import cn.ac.big.circos.service.IBaseService;
import cn.ac.big.circos.util.CategoryTrack;
import cn.ac.big.circos.util.CircosDataset;
import cn.ac.big.circos.util.CircosTrack;
import cn.ac.big.circos.util.Page;

import com.opensymphony.xwork2.ActionSupport;

/**********************************************************************
 * this used to process physical view operation
 * @author jacky
 *
 */
public class PhysicalViewAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2267862243456324268L;
	
	private String position;
	private String dataset;
	private String organism;
	private String param;
	private String cellname;
	private String histonename;
	private String param1;
	
	
	private List genelist;

	
	
	
	@Resource(name="drawPhysicalService")
	private DrawPhysicalPicService drawPhysicalService;
	@Resource(name="baseService")
	private IBaseService baseSerivce;
	private String imagePath;
	
	private List<CircosDataset> datasetList;
	private List<CategoryTrack> categoryList;

	private CircosTrack circosTrack;
	
	private String conf;// user defined configuration file
	private String curDataset; // current choosed dataset
	
	private String myFileFileName;
	private File myFile;
	private String myFileContentType;
	
	private String speciesJson; 
	private List<HistoneMarkBean> hisList;
	private List<PhysicalModelBean> physicalModelList;
	
	
	
    private int pageSize;
	private int pageNo;
	private int totalCount;
	
	
	private Page page;
	
	
	
	
	/*****************************************************
	 * this used to process init physical view action
	 * the circos.conf and the according configure file need to locate in the Root file
	 * @return
	 */
	public String execInitFunc(){
		HttpServletRequest request= ServletActionContext.getRequest() ;
		String path = request.getRealPath("/");
		String filepath = path+File.separator+"physical.conf";
		if(conf != null && conf.length()>0){
			filepath = path+File.separator+"userconf"+File.separator+"physical"+File.separator+conf+File.separator+"physical.conf";
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
	//	System.out.println(filepath);
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
					
					if(line.startsWith("organism") == true){
						String [] arr = line.split("=");
						organism = arr[1];
						
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
							}else if(arrs[0].startsWith("line_width")){
								track.setLineWidth(arrs[1]);
							}else if(arrs[0].startsWith("file")){
								track.setFile(arrs[1]);
							}else if(arrs[0].startsWith("bin")){
								track.setBinsize(arrs[1]) ; // binsize
							}else if(arrs[0].startsWith("startbin")){
								track.setStartBin(arrs[1]) ;
							}else if(arrs[0].startsWith("organism")){
								track.setOrganism(arrs[1]) ;								
							}
							/*if(organism!=null){
								track.setOrganism(organism);
							}*/
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
				String outfile = path+File.separator+this.curDataset+".json";
				System.out.println(outfile);
				BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
				bw.write(jsonstr) ;
				bw.close();
				
			}
			
			//then we need to read 3dmodel table
			Map map = new HashMap();
			//map.put("species",organism);
			this.physicalModelList = (List<PhysicalModelBean>) this.baseSerivce.findResultList("cn.ac.big.circos.selectAllPhysicalModelList", map);
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	/************************************************
	 * this is used to get bin size of given physical model
	 * @return
	 */
	public String execGetPhysicalModelBinSizeFunc(){
		if(this.param != null ){
			this.physicalModelList = (List<PhysicalModelBean>) this.baseSerivce.findResultList("cn.ac.big.circos.selectGivenPhysicalModelBinList", this.param);
		}
		
		return SUCCESS;
	}
	
	
	/*********************************************************
	 * 
	 */
	public String execGetPhysicalModelByNameAndBinFunc(){
		
		try{
			Map map = new HashMap();
			map.put("modelname", this.param);
			map.put("binsize", this.param1);
				
			this.physicalModelList = (List<PhysicalModelBean>)this.baseSerivce.findResultList("cn.ac.big.circos.selectPhysicalModelByNameandBin", map);
					
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
		
	}
	
	/**************************************************************
	 * used BACH to draw consensus view
	 * used BACH_MIX to draw ensemble view
	 * @return
	 */
	public String execDrawFunc(){

		int bstart = 16;
		int bend = 48;
		int unit = 1000000; //1m
		
		if (position != null) {
			String[] arrys = position.split(":");
			if (arrys != null && arrys.length == 2) {
				String[] pos = arrys[1].split("\\.+"); // split can not use ..
				if (pos != null && pos.length == 2) {
					int start = Integer.parseInt(pos[0]);
					int end = Integer.parseInt(pos[1]);
									
					int from = start/unit -bstart +1;
					int to = end /unit  -bstart ;
					long time = new Date().getTime();
					String timestr = time+"";
					imagePath = drawPhysicalService.drawBACHPic(dataset, arrys[0], from, to, timestr);
					
				}
			}
		}
		
		
		return SUCCESS;
	}


	/***************************************************************
	 * this is used to process user's add track request
	 * @return
	 */
	public String execUploadTrackFunc(){
		
		try{
			String targetDirectory = ServletActionContext.getServletContext()
					.getRealPath("/");
			
			String savepath = targetDirectory+"/data/"+circosTrack.getDataset()+"/physical/uploadtrack";
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
			String confilepath = targetDirectory+"/userconf/physical/"+circosTrack.getDataset();
			tempfile = new File(confilepath);
			if(tempfile.exists() ==false){
				tempfile.mkdirs();
			}
			String conffile = confilepath+"/physical.conf";
			BufferedWriter bw = new BufferedWriter( new FileWriter(conffile));
			bw.write("[dataset."+circosTrack.getDataset()+"]\n");
			bw.write("name="+circosTrack.getDataset()+"\n");
			bw.write("conf=conf/physical/"+circosTrack.getDataset()+".conf\n");	
     		bw.close();
     		
     		conffile = targetDirectory+"/conf/physical/"+circosTrack.getDataset()+".conf";
     		
     		String temp_upfile = targetDirectory+"/pub_template/physical_template/temp_upload_"+circosTrack.getOrganism()+"_physical.conf";
     		File tmp_file = new File(temp_upfile);
     		if(tmp_file.exists() == true){
     			File t_confile = new File(conffile);
     			if(t_confile.exists() == false){
     				FileUtils.copyFile( tmp_file, t_confile);
     			}
     		}
     		
     		
     		bw = new BufferedWriter( new FileWriter(conffile,true));
     		
     		bw.write("\n");
     		bw.write("\n");
     		bw.write("["+circosTrack.getName()+"]\n");
     		bw.write("glyph_type="+circosTrack.getGlyph()+"\n");
     		bw.write("storage="+circosTrack.getStoreclass()+"\n");
     		bw.write("organism="+circosTrack.getOrganism()+"\n");
     		if(circosTrack.getGlyph().equals("3dmodel")){     			
     			bw.write("bin="+circosTrack.getBinsize()+"\n");  //
     			bw.write("startbin="+circosTrack.getStartBin()+"\n");
     			bw.write("file=/circosweb/data/"+circosTrack.getDataset()+"/physical/uploadtrack/"+ myFileFileName+"\n");
     			bw.write("key="+circosTrack.getName()+"_3dmodel\n");
     			bw.write("category="+circosTrack.getCategory()+"\n");
     		}else{
     			//absolute path
     			bw.write("file="+targetDirectory+"/data/"+circosTrack.getDataset()+"/physical/uploadtrack/"+ myFileFileName+"\n");
     			bw.write("key="+circosTrack.getName()+"\n");
     			bw.write("category=Custom Annotated Track\n");
     		}		
     		
     		bw.write("color="+circosTrack.getColor()+"\n");
     		bw.write("line_width=1\n");
     		
     	
     		
     		bw.close();
			
			conf = circosTrack.getDataset();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return SUCCESS;
	}
	
	
	/**********************************************************
	 * this is used to search gene by conditions
	 * @return
	 */
	public String execSearchGeneFunc(){
		
		String table="";
		Map map = new HashMap();
		if (position != null) {
			String[] arrys = position.split(":");
			if (arrys != null && arrys.length == 2) {
				map.put("chr", arrys[0]);
				
				String[] pos = arrys[1].split("\\.+"); // split can not use ..
				if (pos != null && pos.length == 2) {
					int start = Integer.parseInt(pos[0]);
					int end = Integer.parseInt(pos[1]);
					map.put("start", start) ;
					map.put("end", end) ;
				}
				
			}
		}
		
		
		if(organism != null ){
			/*	if(organism.equals("human")||organism.equals("hg19")||organism.equals("hg18")){
					map.put("table", "tb_human_gene");
				}else if(organism.equals("mouse")){
					map.put("table", "tb_mouse_gene");
				}*/
				map.put("table", "tb_"+organism+"_gene");
		}
		
			
		if(this.param != null && this.param.length()>0){
			List<String> genes= new ArrayList<String>();
			if(this.param.contains(",")){
				String [] arrs = this.param.split(",");
				if(arrs != null){
					for(String agene:arrs){
						genes.add(agene);
					}
				}
			}else if(this.param.contains("\r\n")){
				String [] arrs = this.param.split("\r\n");
				if(arrs != null){
					for(String agene:arrs){
						genes.add(agene);
					}
				}
			}else{
				genes.add(this.param) ;
			}
			map.put("genelist", genes);
		}
		
		int offset = 0 ;
		int limit =0;
		if(totalCount == -1){
			PubGeneBean gene = (PubGeneBean)baseSerivce.findObjectByObject("cn.ac.big.circos.selectGeneCount", map);
			if(gene != null){
				totalCount = gene.getGeneCount();
			}
		}
		
		if(totalCount != -1){
			  page = new Page(this.totalCount, this.pageNo, this.pageSize, 0);
			 
			    if (this.page.getPageSize() > this.totalCount){
					offset = this.page.getRowFrom() -1 ;
					limit = this.totalCount ;
				}else {
					offset = this.page.getRowFrom() -1;
					limit = this.page.getPageSize() ;
				}
		}else{
			offset = 0 ;
			limit =10;
		}
		
		map.put("p1", offset) ;
		map.put("p2", limit) ;
		
		genelist = (List<PubGeneBean>)baseSerivce.findResultList("cn.ac.big.circos.selectAllGene", map);
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		 if(session.getAttribute("myPage")!=null){
		     session.removeAttribute("myPage");
		  }
		 
		 session.setAttribute("myPage", page);
		return SUCCESS;
	}
	
	
	
	/***************************************************************
	 * This is used to get gene for given range 
	 * postion: still compose by chr:start..end
	 * @return
	 */
	public String execSearchGeneForGivenRangeFunc(){
		String table="";
		Map map = new HashMap();
		if (position != null) {
			String[] arrys = position.split(":");
			if (arrys != null && arrys.length == 2) {
				map.put("chr", arrys[0]);
				
				String[] pos = arrys[1].split("\\.+"); // split can not use ..
				if (pos != null && pos.length == 2) {
					int start = Integer.parseInt(pos[0]);
					int end = Integer.parseInt(pos[1]);
					map.put("start", start) ;
					map.put("end", end) ;
				}
				
			}
		}
		
		if(organism != null ){
		/*	if(organism.equals("human")||organism.equals("hg19")||organism.equals("hg18")){
				map.put("table", "tb_human_gene");
			}else if(organism.equals("mouse")){
				map.put("table", "tb_mouse_gene");
			}*/
			map.put("table", "tb_"+organism+"_gene");
		}
		
		
		genelist = (List<PubGeneBean>)baseSerivce.findResultList("cn.ac.big.circos.selectGeneForScope", map);
		
		return SUCCESS;
	}
	
	

	
	
	
	/*****************************************************************
	 * this is used to search histone mark
	 * @return
	 */
	public String execSearchHisMarkFunc(){
		Map map = new HashMap();
		
		if (position != null) {
			String[] arrys = position.split(":");
			if (arrys != null && arrys.length == 2) {
				map.put("chrom", arrys[0]);
				
				String[] pos = arrys[1].split("\\.+"); // split can not use ..
				if (pos != null && pos.length == 2) {
					int start = Integer.parseInt(pos[0]);
					int end = Integer.parseInt(pos[1]);
					map.put("start", start) ;
					map.put("end", end) ;
				}
				
			}
		}
		
		if(cellname != null){
			cellname = cellname.toLowerCase();
			map.put("cellname", cellname) ;
		}
		if(histonename != null){
			if(histonename.equals("DNase-seq")){
				histonename = "Dnase";
			}else if(histonename.equals("RNA-seq_minus")){
				histonename = "RnaSeqMinus";
			}else if(histonename.equals("RNA-seq_plus")){
				histonename = "RnaSeqPlus";
			}
			map.put("histonename", histonename) ;
		}
		
		if(organism != null){
			
			if(organism.equals("hg19")||organism.equals("hg18") || organism.equals("human") ){
				map.put("table", "tb_histone_mark");
			}else if(organism.equals("mouse")){
				map.put("table", "tb_mouse_histone_mark");
			}
				map.put("orgname", organism);
		
		}	
		
		hisList = (List<HistoneMarkBean>)this.baseSerivce.findResultList("cn.ac.big.circos.selectHistoneMark", map);
		
		return SUCCESS;
	}
	


	public String getCellname() {
		return cellname;
	}

	public void setCellname(String cellname) {
		this.cellname = cellname;
	}

	public String getHistonename() {
		return histonename;
	}

	public void setHistonename(String histonename) {
		this.histonename = histonename;
	}



	public List<HistoneMarkBean> getHisList() {
		return hisList;
	}



	public void setHisList(List<HistoneMarkBean> hisList) {
		this.hisList = hisList;
	}



	public String getPosition() {
		return position;
	}



	public void setPosition(String position) {
		this.position = position;
	}



	public String getDataset() {
		return dataset;
	}



	public void setDataset(String dataset) {
		this.dataset = dataset;
	}



	public List<CircosDataset> getDatasetList() {
		return datasetList;
	}


	public void setDatasetList(List<CircosDataset> datasetList) {
		this.datasetList = datasetList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<CategoryTrack> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryTrack> categoryList) {
		this.categoryList = categoryList;
	}

	public String getCurDataset() {
		return curDataset;
	}

	public void setCurDataset(String curDataset) {
		this.curDataset = curDataset;
	}

	public String getSpeciesJson() {
		return speciesJson;
	}

	public void setSpeciesJson(String speciesJson) {
		this.speciesJson = speciesJson;
	}

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public CircosTrack getCircosTrack() {
		return circosTrack;
	}

	public void setCircosTrack(CircosTrack circosTrack) {
		this.circosTrack = circosTrack;
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

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}


	public List getGenelist() {
		return genelist;
	}



	public void setGenelist(List genelist) {
		this.genelist = genelist;
	}



	public String getParam() {
		return param;
	}



	public void setParam(String param) {
		this.param = param;
	}



	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<PhysicalModelBean> getPhysicalModelList() {
		return physicalModelList;
	}

	public void setPhysicalModelList(List<PhysicalModelBean> physicalModelList) {
		this.physicalModelList = physicalModelList;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	
}
