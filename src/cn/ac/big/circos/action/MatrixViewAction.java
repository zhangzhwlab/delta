package cn.ac.big.circos.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import cn.ac.big.circos.po.ChromatinInter;
import cn.ac.big.circos.util.CategoryTrack;
import cn.ac.big.circos.util.CircosDataset;
import cn.ac.big.circos.util.CircosTrack;
import cn.ac.big.circos.util.MatrixTrack;

import com.opensymphony.xwork2.ActionSupport;

/**********************************************************
 * this used to process hic interaction matrix based on HiCPlotter initially
 * @author lenovo
 *
 */
public class MatrixViewAction  extends ActionSupport{

	private String chrom;
	private String dataset;
	private int binStart;
	private int binEnd;
	private String curDataset; // current choosed dataset
	
	private String image;
	private String jsonStr;
	private List<CircosDataset> datasetList;
	private String speciesJson;  //species json store all the chromosome information
	
	private MatrixTrack matrixTrack;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String execInitFunc(){
		HttpServletRequest request= ServletActionContext.getRequest() ;
		String path = request.getRealPath("/");
		String filepath = path+File.separator+"matrix.conf";
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
	
	
	/************************************************
	 * this used to load each configure file,and get the params
	 * @return
	 */
	public String execLoadDatasetConfigFunc(){
		HttpServletRequest request= ServletActionContext.getRequest() ;
		String path = request.getRealPath("/");
		String filepath = path+File.separator+this.curDataset;
		try{
			File file = new File(filepath);
			if(file.exists() == true ){
				BufferedReader br = new BufferedReader(new FileReader(filepath));
				String line = "";
				matrixTrack = new MatrixTrack();
				
				while((line = br.readLine()) != null ){
					if(line.startsWith("#")){
						continue ;
					}
					if(line.startsWith("chroms") == true){
						String [] arr = line.split("=");
						this.speciesJson = arr[1];
					}
					
					if(line.startsWith("matrix_file")==true){
						String [] arr = line.split("=");
						matrixTrack.setMatrixFile(arr[1]);
					}
					if(line.startsWith("matrix_binsize")==true){
						String [] arr = line.split("=");	
						matrixTrack.setBinsize(Integer.parseInt(arr[1]));
					}
					if(line.startsWith("matrix_resolution")==true){
						String [] arr = line.split("=");
						matrixTrack.setResolution(Integer.parseInt(arr[1]));
					}

				}
				
				br.close();
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	

	public String execDrawMatrixFunc(){
		
		String picpath = "/share_bio/panfs/biocloud/biocloud/webserver/apache-tomcat-6.0.26-circosweb/webapps/circosweb/pages/visualization/matrix/";
		String cmd="python /leofs/bioweb/tangbx/visualization/download_data/HicPlotter/HiCPlotter-master/HiCPlotter.py -f "+this.dataset+chrom+".matrix -n "+dataset+" -chr "+chrom+" -r 40000 -o "+picpath+"/matrix -s "+binStart+" -e "+binEnd+" -fh 0 -hR 0 -ptr 0";
		System.out.println(cmd);
		try{
			 Runtime rt = Runtime.getRuntime();  
	           Process proc = rt.exec(cmd);  
	           
	           

	        InputStreamReader isr = new InputStreamReader(proc.getInputStream());  
	            BufferedReader br = new BufferedReader(isr);  
	            String line = null;  
	            while ((line = br.readLine()) != null) {  
	                
                System.out.println(line);
            }  
            isr.close();
            //matrix-chr21.ofBins(1-1174).40K.png
            String filename = "matrix"+"_"+chrom+".ofBins"+binStart+"_"+binEnd+".40K.png";
            System.out.println(picpath+"/"+filename);
            while(true){
            	File file = new File(picpath+"/"+filename);
            	if(file.exists() == true){
            		break;
            	}
            	Thread.sleep(5);
            }
			 image = "/circosweb/pages/visualization/matrix/"+filename;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return SUCCESS;
	}
	
	
	public String execGetGivenQueryBin(){
		//String filepath = "/leofs/bioweb/tangbx/visualization/download_data/HicPlotter/HiCPlotter-master/data/HiC/Human/hES-nij.chr21.2" ;
		String filepath = "D://hES-nij.chr21.2";
		//binStart = 549;//binstart +1
		binStart++;

		try{
			File file = new File(filepath);
			if(file.exists() == true ){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = "";
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				int count = 0 ;
				while((line = br.readLine())!=null){
					count ++;
					if(count >= this.binStart && count <= this.binEnd ){ // file line start to end
						String [] arrys = line.split("\\s+") ;
						
						sb.append("[");
						for(int i = this.binStart-1;i<= this.binEnd-1;i++ ){
							float df = Float.parseFloat(arrys[i]) ;
							String val = String.format("%.4f", log2(df)) ;
							if(val.equals("-Infinity")){
								val = "0";
							}
							sb.append(val);
							if(i!=(this.binEnd-1)){
								sb.append(",");
							}
						}
						sb.append("]");
						if(count != this.binEnd){
							sb.append(",") ;
						}
					}
				}
				br.close();
				sb.append("]");
				jsonStr = sb.toString();
				
				HttpServletRequest request = ServletActionContext.getRequest();
				String webpath = request.getRealPath("/");
				
				String outpath = webpath+"/json/heatmap.json";
				System.out.println(outpath);
				BufferedWriter bw = new BufferedWriter(new FileWriter(outpath));
				bw.write(sb.toString());
				bw.flush();
				bw.close();
						
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	private static double log2(double value) {
		return Math.log(value) / Math.log(2.0);
	 }

	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}



	public int getBinStart() {
		return binStart;
	}


	public void setBinStart(int binStart) {
		this.binStart = binStart;
	}


	public int getBinEnd() {
		return binEnd;
	}


	public void setBinEnd(int binEnd) {
		this.binEnd = binEnd;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public String getJsonStr() {
		return jsonStr;
	}


	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
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


	public String getSpeciesJson() {
		return speciesJson;
	}


	public void setSpeciesJson(String speciesJson) {
		this.speciesJson = speciesJson;
	}


	public MatrixTrack getMatrixTrack() {
		return matrixTrack;
	}


	public void setMatrixTrack(MatrixTrack matrixTrack) {
		this.matrixTrack = matrixTrack;
	}

	
}
