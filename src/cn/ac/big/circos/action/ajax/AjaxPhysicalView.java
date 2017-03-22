package cn.ac.big.circos.action.ajax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.ac.big.circos.po.CellHisBean;
import cn.ac.big.circos.po.GFF3Format;
import cn.ac.big.circos.po.PeakBean;
import cn.ac.big.circos.po.PubGeneBean;
import cn.ac.big.circos.service.IBaseService;
import cn.ac.big.circos.util.ParseOutput;


import com.opensymphony.xwork2.ActionSupport;

/*************************************
 * to execute ajax action in physical view
 * @author lenovo
 *
 */
public class AjaxPhysicalView extends ActionSupport{
	
	private List<CellHisBean> cellList;
	
	@Resource(name="baseService")
	private IBaseService baseService;
	
	private String param1;
	private String param2;

	
	private String perbin;
	private String binsize;
	private String track;
	

	
	private List<GFF3Format> gffList;
	private List<PeakBean> peaklist;
	private List<PubGeneBean> genelist;
	
	
	/********************************************************************************
	 * ajax get cell type for given organism
	 * @return
	 */
	public String execGetCellTypeFunc(){
		//search cell
		Map map = new HashMap();
		map.put("orgname", param1);
		cellList= (List<CellHisBean>)baseService.findResultList("cn.ac.big.circos.selectCellTypeByOrg", param1);
		
		return SUCCESS;
	}
	
	
	/*************************************************************************
	 * to get histone mark for given organism and cell type
	 * @return
	 */
	public String execGetHisMarkFunc(){
		Map map = new HashMap();
		map.put("orgname", param1);
		map.put("cellname", param2);
		
		cellList= (List<CellHisBean>)baseService.findResultList("cn.ac.big.circos.selectHismarkByOrgCell", map);
		return SUCCESS;
	}
	
	
	
	
	/******************************************************************
	 * this is used to parse a given gff3 file and return a gff3 list 
	 * both end in the given range
	 * param1  store file
	 * param2  store position
	 * @return
	 */
	public String execParseGFF3File(){
		try{
			if(this.param1 != null){
				
				
				
				File file = new File(this.param1);
				if(file.exists() == true){
					String chrom= null;
					int start =0;
					int end =0 ;
					if (this.param2 != null) {
						String[] arrys = this.param2.split(":");
						if (arrys != null && arrys.length == 2) {
							chrom = arrys[0];
							
							String[] pos = arrys[1].split("\\.+"); // split can not use ..
							if (pos != null && pos.length == 2) {
								start = Integer.parseInt(pos[0]);
								end = Integer.parseInt(pos[1]);							
							}						
						}
					}
					
					gffList = new ArrayList<GFF3Format>();
					
					
					ParseOutput parseout = new ParseOutput();
					
					BufferedReader br = new BufferedReader(new FileReader(this.param1));
					String line = "";
					while((line=br.readLine()) != null){
						GFF3Format gff3line = parseout.parseGff3(line);
						int stat_start = Integer.parseInt(gff3line.getStart()) ;
						int stat_end = Integer.parseInt(gff3line.getEnd()) ;
						if(gff3line.getSeq().equals(chrom) == true){
							if(stat_start >= start && stat_start<=end && stat_end >= start && stat_end<=end){ // filter
								gffList.add(gff3line) ;
							}
						}
					}
					
					br.close();
				}
			}
			
		}catch(Exception ex ){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	/******************************************************************
	 * this is used to parse a given gff3 file and return a gff3 list 
	 * 
	 * one end in the given range
	 * param1  store file
	 * param2  store position
	 * @return
	 */
	public String execGetPeakGFF3File(){
		try{
			if(this.param1 != null){
				
				
				
				File file = new File(this.param1);
				if(file.exists() == true){
					String chrom= null;
					int start =0;
					int end =0 ;
					if (this.param2 != null) {
						String[] arrys = this.param2.split(":");
						if (arrys != null && arrys.length == 2) {
							chrom = arrys[0];
							
							String[] pos = arrys[1].split("\\.+"); // split can not use ..
							if (pos != null && pos.length == 2) {
								start = Integer.parseInt(pos[0]);
								end = Integer.parseInt(pos[1]);							
							}						
						}
					}
					
					gffList = new ArrayList<GFF3Format>();
					peaklist = new ArrayList<PeakBean>();
					genelist = new ArrayList<PubGeneBean>();
					
					ParseOutput parseout = new ParseOutput();
					
					BufferedReader br = new BufferedReader(new FileReader(this.param1));
					String line = "";
					while((line=br.readLine()) != null){
						GFF3Format gff3line = parseout.parseGff3(line);
						PeakBean peakbean = new PeakBean();
						PubGeneBean genebean = new PubGeneBean();
						int stat_start = Integer.parseInt(gff3line.getStart()) ;
						int stat_end = Integer.parseInt(gff3line.getEnd()) ;
						if(gff3line.getSeq().equals(chrom) == true){
							if((stat_start >= start && stat_start<=end) || (stat_end >= start && stat_end<=end)){ // filter
								gffList.add(gff3line) ;
								genebean.setChrom(gff3line.getSeq());
								genebean.setStrand(gff3line.getStrand());
								genebean.setStart(Integer.parseInt(gff3line.getStart()));
								genebean.setEnd(Integer.parseInt(gff3line.getEnd()));
								genebean.setSynonym(gff3line.getId());
								peakbean.setChrom(gff3line.getSeq());
								peakbean.setStart(Integer.parseInt(gff3line.getStart()));
								peakbean.setEnd(Integer.parseInt(gff3line.getEnd()));
								peakbean.setNote(gff3line.getCols9());
								peakbean.setScore(gff3line.getScore());
								peaklist.add(peakbean);
								genelist.add(genebean);
							}
						}
					}
					
					br.close();
				}
			}
			
		}catch(Exception ex ){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/*******************************************************************
	 * this is used to get histone density mark from mysql
	 * @return
	 */
	public String execGetHistoneDensityFromMysqlFunc(){
		try{
			
			String chrom= null;
			int start =0;
			int end =0 ;
			if (this.param2 != null) {
					String[] arrys = this.param2.split(":");
					if (arrys != null && arrys.length == 2) {
						chrom = arrys[0];
						
						String[] pos = arrys[1].split("\\.+"); // split can not use ..
						if (pos != null && pos.length == 2) {
							start = Integer.parseInt(pos[0]);
							end = Integer.parseInt(pos[1]);							
						}						
					}
				}		
			
			String table = this.param1;
			/*if(this.track != null){
				if(this.track.indexOf("_") > -1){
					int idex = this.track.indexOf("_");
					if(idex > -1){
						this.track = this.track.substring(idex+1,this.track.length());
					}
					
				}
				
				if(this.track !=null){
					String keyname = this.track ;
					if(keyname.equals("RNA-seq_minus")){
						keyname = "RnaSeqMinus";
					}else if(keyname.equals("RNA-seq_plus")){
						keyname = "RnaSeqPlus";
					}else if(keyname.equals("DNase-seq")){
						keyname = "Dnase";
					}else if(keyname.equals("RIP-seq")){
						keyname = "RIPinput";
					}else if(keyname.equals("ChIA-PET_CTCF")){
						keyname = "ChiaPetCTCF";
					}else if(keyname.equals("ChIA-PET_Pol2")){
						keyname = "ChiaPetPol2";
					}
					table += "_"+keyname;
				}
			}*/
			
			
			Map map = new HashMap();
			map.put("chrom", chrom);
			map.put("start",start);
			map.put("end",end);
			map.put("perbin",perbin);
			map.put("binsize", binsize);
			map.put("table", table);
			System.out.println("mysql table="+table);
			
			
			 peaklist = (List<PeakBean>)baseService.findResultList("cn.ac.big.circos.selectHistoneList", map);
		
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	
	/*************************************************************
	 * this is used to get peak data from mysql table 
	 * either anchor end in the give scope
	 * @return
	 */
	public String execGetPeakFromMysqlFunc(){
		try{
		
			String chrom= null;
			int start =0;
			int end =0 ;
			if (this.param2 != null) {
					String[] arrys = this.param2.split(":");
					if (arrys != null && arrys.length == 2) {
						chrom = arrys[0];
						
						String[] pos = arrys[1].split("\\.+"); // split can not use ..
						if (pos != null && pos.length == 2) {
							start = Integer.parseInt(pos[0]);
							end = Integer.parseInt(pos[1]);							
						}						
					}
				}
			
			Map map = new HashMap();
			map.put("table", param1);
			if(this.param1 != null && this.param1.equals("tb_k562_ChiaPetCTCF") == false && this.param1.equals("tb_k562_ChiaPetPol2") == false 
					&& this.param1.equals("tb_helas3_ChiaPetPol2") == false){
				if(binsize.equals("-1") == false){
					map.put("binsize", binsize);
				}			
			}
			

			map.put("chrom", chrom);
			map.put("start",start);
			map.put("end",end);
			List<PeakBean> peaklist = (List<PeakBean>)baseService.findResultList("cn.ac.big.circos.selectPeakAnchorList", map);
			gffList = new ArrayList<GFF3Format>();
			
			if(peaklist!= null){
				ParseOutput parseout = new ParseOutput();
				for(PeakBean peakbean:peaklist){
					String line= peakbean.getChrom()+" 0 0 "+peakbean.getStart()+" "+peakbean.getEnd()+" . . . "+peakbean.getNote();
					GFF3Format gff3line = parseout.parseGff3(line);
					
					gffList.add(gff3line) ;
				}
				
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
		
	}
	
	

	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}



	public List<CellHisBean> getCellList() {
		return cellList;
	}



	public void setCellList(List<CellHisBean> cellList) {
		this.cellList = cellList;
	}


	public List<GFF3Format> getGffList() {
		return gffList;
	}


	public void setGffList(List<GFF3Format> gffList) {
		this.gffList = gffList;
	}


	public String getPerbin() {
		return perbin;
	}


	public void setPerbin(String perbin) {
		this.perbin = perbin;
	}


	public String getBinsize() {
		return binsize;
	}


	public void setBinsize(String binsize) {
		this.binsize = binsize;
	}


	public String getTrack() {
		return track;
	}


	public void setTrack(String track) {
		this.track = track;
	}


	public List<PeakBean> getPeaklist() {
		return peaklist;
	}


	public void setPeaklist(List<PeakBean> peaklist) {
		this.peaklist = peaklist;
	}


	public List<PubGeneBean> getGenelist() {
		return genelist;
	}


	public void setGenelist(List<PubGeneBean> genelist) {
		this.genelist = genelist;
	}



	
	
}
