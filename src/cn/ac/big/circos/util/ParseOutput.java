package cn.ac.big.circos.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.ac.big.circos.po.BachxyzBean;
import cn.ac.big.circos.po.ChromatinInter;
import cn.ac.big.circos.po.GFF3Format;

public class ParseOutput {

	/**********************************************************
	 * this washu genome browse format
	 * 1       17400001        17600000        1:17400001-17600000,1   1       +
	 * @param line
	 * @return
	 */
	public ChromatinInter parseChromatinOutput(String line){
	//	while( (line=br.readLine()) != null){
		String []arry = line.split("\\s+");
		ChromatinInter chromtainBean = new ChromatinInter();
		chromtainBean.setAnchorChr(arry[0]);
		chromtainBean.setStart(Integer.parseInt(arry[1]));
		chromtainBean.setStop(Integer.parseInt(arry[2]));
		chromtainBean.setName(arry[3]);
		chromtainBean.setId(Integer.parseInt(arry[4]));
		if(arry[5].equals("-")){
			chromtainBean.setStrand("<");
		}else if(arry[5].equals("+")){
				chromtainBean.setStrand(">");
		}
		
		return chromtainBean;

	  }
	
	
	
	/*******************************************************
	 * this used to parse gff3 format
	 * 1       Interaction     arc     47641826        47641847        3       .       .       ID=780;Name=hs0000780;Note=1:47708795-47708817
	 * @param line
	 * @return
	 */
	public ChromatinInter parseFromGff3(String line){
		ChromatinInter chromtainBean = new ChromatinInter();
		String []arry = line.split("\\s+");
		chromtainBean.setAnchorChr(arry[0]);
		chromtainBean.setStart(Integer.parseInt(arry[3]));
		chromtainBean.setStop(Integer.parseInt(arry[4]));
		
		String[] col9arry = arry[8].split(";");
		int index = col9arry[2].indexOf("=");
		String tchr = col9arry[2].substring(index+1,col9arry[2].length()) ;
		
		
		String score = arry[5];
		if(score ==null || score.length() ==0 ){
			score = "0";
		}
		String name =  tchr+","+score;
		chromtainBean.setName(name);
		
		index = col9arry[0].indexOf("=");
		String idstr =  col9arry[0].substring(index+1,col9arry[0].length() ) ;
		chromtainBean.setId(Integer.parseInt(idstr));
			
		return chromtainBean;
		
	}

	/*******************************************************
	 * this used to parse gff3 format
	 * 1       Interaction     arc     47641826        47641847        3       .       .       ID=780;Name=hs0000780;Note=47641826-47708817:47641847-47641847
	 * @param line
	 * @return
	 */
	
	public ChromatinInter parseFromCustomGff3Interaction(String line){
		ChromatinInter chromtainBean = new ChromatinInter();
		String []arry = line.split("\\s+");
		chromtainBean.setAnchorChr(arry[0]);
		chromtainBean.setStart(Integer.parseInt(arry[3]));//anchor start
		chromtainBean.setStop(Integer.parseInt(arry[4])); //target end
		
		
	
		
		
		String score = arry[5];
		if(score ==null || score.length() ==0 ||score.equals(".")){
			score = "0";
		}
	
		String [] col9arry = arry[8].split(";");
		if(col9arry != null){
			for(String curstr: col9arry){
				String[] arrs = curstr.split("=");
				if(arrs[0].equals("ID") == true){
					chromtainBean.setId(Integer.parseInt(arrs[1]));
				}
				if(arrs[0].equals("Note") == true){ // Note=1:209800748-209801330|14:23249783-23250303
					String[] pos = arrs[1].split("\\|");
					if(pos != null && pos.length ==2){
						String [] anchor = pos[0].split("-");
						if(anchor != null)
						chromtainBean.setAnchorEnd(Integer.parseInt(anchor[1]));
						String [] target = pos[1].split("-");
						if(target != null){
							int targetindex = target[0].indexOf(":");
							String tchr = target[0].substring(0, targetindex) ;
							String name =  tchr+","+score;
							chromtainBean.setName(name);
							String targetstart = target[0].substring(targetindex+1, target[0].length());
							chromtainBean.setTargetStart(Integer.parseInt(targetstart)) ;
						}
					}
				}
			}
		}
		
		return chromtainBean;
		
	}
	
	
	/************************************************************************************
	 * this is used to merge data in case one anchor have mutilple target, and the target list should
	 * be store in target parameter
	 * @param interList
	 * @return
	 */
	
	public List<ChromatinInter> formatGff3Interaction(List<ChromatinInter> interList){
		List<ChromatinInter> resList = null;
		if(interList != null && interList.size() > 0 ){
			Map<String,ChromatinInter> anchorMap = new HashMap<String,ChromatinInter>();
			
			for(ChromatinInter chromInter:interList){
				String anchorkey = chromInter.getStart()+"_"+chromInter.getStop();			
				if(anchorMap.containsKey(anchorkey) == false){
					List<ChromatinInter> targetlist = new ArrayList<ChromatinInter>();
					targetlist.add(chromInter);
					chromInter.setTarget(targetlist);
					anchorMap.put(anchorkey, chromInter) ;
					
				}else{
					ChromatinInter curInter = (ChromatinInter)anchorMap.get(anchorkey);
					if(curInter != null){
						List<ChromatinInter> targetlist = curInter.getTarget();
						if(targetlist != null){
							targetlist.add(chromInter);
						}
						curInter.setTarget(targetlist);
					}
				}
			}
		}
		return resList;
	}
	

	public String parseDataQuality(String line){
		//System.out.println(line);
		String []arry = line.split("\\s+");		
		return arry[3];
	}
	
	
	public GFF3Format parseGff3(String line){
		String []arry = line.split("\\s+") ;
		GFF3Format gff3 = new GFF3Format();
		gff3.setSeq(arry[0]);
		gff3.setSource(arry[1]) ;
		gff3.setFeature(arry[2]) ;
		gff3.setStart(arry[3]);
		gff3.setEnd(arry[4]);
		gff3.setScore(arry[5]);
		gff3.setStrand(arry[6]);
		gff3.setPhase(arry[7]);
		gff3.setCols9(arry[8]);
		
		Map<String,String[]> map = new HashMap<String,String[]>();
		
		String [] colarry = arry[8].split(";");
		for(int i=0; i<colarry.length; i++){
			String[] tr = colarry[i].split("=") ;
			if(tr != null && map.containsKey(tr[0])==false){
				String [] attrs = tr[1].split(",") ;
				
				map.put(tr[0], attrs);
			}
			if(tr[0].equals("ID") == true){
				gff3.setId(tr[1]);
			}
			if(tr[0].equals("Name") == true){
				gff3.setName(tr[1]);
			}
		}
		gff3.setAttributes(map) ;
			

		return gff3;
	}
	
	
	/*********************************************************************
	 * this is used to format the gff3 file ,we need to parse the children data from feature
	 * @param gffdata
	 */
	public List<GFF3Format> formatGFF3File(List<GFF3Format> gffdata){
		List<GFF3Format> fmtList = new ArrayList<GFF3Format>();
		try{
			if(gffdata != null && gffdata.size() >0 ){
				Map<String,GFF3Format> toplevelfeature = new HashMap<String,GFF3Format>();
				List<GFF3Format> leaveFeature = new ArrayList<GFF3Format>();
				for(GFF3Format gff:gffdata ){
					if(gff.getAttributes()!= null && gff.getAttributes().containsKey("Parent") == true && gff.getFeature().equals("exon")){
						 // to check the top level feature
						String [] parent = gff.getAttributes().get("Parent");
						if(parent != null ){
							if(toplevelfeature.containsKey(parent[0])){
								GFF3Format curgff = toplevelfeature.get(parent[0]);
								if(curgff.getChildren() == null){
									List<GFF3Format> curchildren = new ArrayList<GFF3Format>();
									curchildren.add(gff);
									curgff.setChildren(curchildren);
								}else{
									List<GFF3Format> curchildren = curgff.getChildren();
									curchildren.add(gff);
									curgff.setChildren(curchildren);
								}
							}else{
								leaveFeature.add(gff);
							}
						}
						
					}
					
					else{
						if(toplevelfeature.containsKey(gff.getId()) == false){
							toplevelfeature.put(gff.getId(), gff);
						}
						
					}					
				}
				
				//because of sort ,some exon feature will be on the top of the transript. for leave feature 
				if(leaveFeature.size() >0){
					for(GFF3Format gff:leaveFeature ){
						String [] parent = gff.getAttributes().get("Parent");
						if(parent != null ){
							if(toplevelfeature.containsKey(parent[0])){
								GFF3Format curgff = toplevelfeature.get(parent[0]);
								if(curgff.getChildren() == null){
									List<GFF3Format> curchildren = new ArrayList<GFF3Format>();
									curchildren.add(gff);
									curgff.setChildren(curchildren);
								}else{
									List<GFF3Format> curchildren = curgff.getChildren();
									curchildren.add(gff);
									curgff.setChildren(curchildren);
								}
							}
						}
					}
				}
				
				
				
				
				if(toplevelfeature != null && toplevelfeature.size() >0 ){
					Iterator it = toplevelfeature.entrySet().iterator();
					while(it.hasNext()){
						Entry set = (Entry)it.next();
						GFF3Format tgff = (GFF3Format)set.getValue();
						if(tgff.getChildren() != null && tgff.getChildren().size()>0){
							Collections.sort(tgff.getChildren(), new Comparator<GFF3Format>(){
							     public int compare(GFF3Format o1, GFF3Format o2){
							    	 int o1end = Integer.parseInt(o1.getEnd());
							    	int o2start = Integer.parseInt(o2.getStart()) ;
							         if( o1end == o2start)
							             return 0;
							         return o1end < o2start ? -1 : 1;
							     }
							});
						}
						
						
						
						fmtList.add(tgff);
					}
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace() ;
		}

		return fmtList;
	}
	
	
	
	/********************************************************
	 * this is used to parse bach xyz result
	 */
	public BachxyzBean parseBachXYZ(String line){
		
		BachxyzBean xyz = new BachxyzBean();
		String []arry = line.split("\\s+") ;
		xyz.setX(arry[1]);
		xyz.setY(arry[2]);
		xyz.setZ(arry[3]);
		
		String pos = arry[4];
		int indx1 = pos.indexOf(":");
		int indx2 = pos.indexOf("..");
		
		xyz.setChr(pos.substring(0,indx1));
		xyz.setStart(pos.substring(indx1+1,indx2)) ;
		xyz.setEnd(pos.substring(indx2+2,pos.length())) ;
		
		
		return xyz;
		
	} 
	
	
	
	
}
