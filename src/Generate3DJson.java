import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Random;


public class Generate3DJson {
	
	class Atom{
		String name;
		float x;
		float y;
		float z;
		int chr;
		int start;
		int end;
	}
	
	
	public static void main(String []args){
		try{
			
			//int start =88;
			//int atomnum = 33;
			//int binsize=50000;
			//String chrom="11";
			//String infile="E:\\tangbx\\工作日志\\circosweb\\data\\physical\\bach.xyz";
			//String outfile="E:\\tangbx\\工作日志\\circosweb\\data\\physical\\test_50k_pos.xyz";
			
		//	generateXYZ(start,atomnum,binsize,chrom, infile, outfile );
			
		//	String jsonfile = "E:\\tangbx\\工作日志\\circosweb\\data\\physical\\1k_pos.json";
		//	generateJson(outfile,jsonfile);
			
			
			generateAnalog3DmodelFeatureData();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	// used to generate analog xyz data
	//10	1.6e+07	1.7e+07	389	0.4511	0.8831
	//10	1.7e+07	1.8e+07	272	0.4534	0.8951 
	public static void generateAnalog3DmodelFeatureData(){
		
		try{
			String outfile = "E:\\tangbx\\工作日志\\circosweb\\data\\physical\\50k.feature";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			int enzymemin = 200;
			int enzymemax = 600;
			
			float gcmin = 0;
			float gcmax = 1;
			
			
			float mapmin = 0.8f;
			float mapmax = 1;
			
			int startpos = 100000;
			int startbin = 88;
			int endbin = 128;
			int binsize = 50000;
			
			for(int i=startbin;i<=endbin;i++){
				
				int t_startpos = startpos + i*binsize;
				int t_endpos = t_startpos + binsize;
				
				BigDecimal bd = new BigDecimal(t_startpos+"");
				String bdstr = bd.stripTrailingZeros().toString();
				String [] bdarrs = bdstr.split("\\+");
				int bdarrse = Integer.parseInt(bdarrs[1]);
				String s_bd = bdarrs[0];
				if(bdarrse < 10){
					s_bd += "+0"+bdarrse;
				}else{
					s_bd = bdstr;
				}
				
				BigDecimal bd1 = new BigDecimal(t_endpos+"");
				String bd1str = bd1.stripTrailingZeros().toString();
				String [] bdarrs1 = bd1str.split("\\+");
				int bdarrse1 = Integer.parseInt(bdarrs1[1]);
				String s_bd1 = bdarrs1[0];
				if(bdarrse1 < 10){
					s_bd1 += "+0"+bdarrse1;
				}else{
					s_bd1 = bd1str;
				}
				
				Random r = new Random();
			    int enzyme_randomValue = (int)(enzymemin + ( enzymemax - enzymemin) * r.nextFloat() );
			     
			    r= new Random();
			    float gc_rv = gcmin +(gcmax - gcmin) * r.nextFloat();
			    
			    r = new Random();
			    
			    float map_rv = mapmin + ( mapmax- mapmin) *r.nextFloat() ;
			   // System.out.println(map_rv);
			    bw.write("11\t"+s_bd+"\t"+s_bd1+"\t"+enzyme_randomValue+"\t"+gc_rv+"\t"+map_rv+"\n");
			}
			
			
		    
			
			
			
			bw.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	
	
	/************************************
	 * int start = 1;
			int atomnum = 33;
			int binsize = 50000;
	 */
	public static void generateXYZ(int start,int atomnum,int binsize,String chrom,String infile,String outfile ){
		
		try{

			String brline="";
			BufferedReader br = new BufferedReader(new FileReader(infile));
			brline = br.readLine();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			bw.write(brline+"\n");
			brline = br.readLine();
			bw.write(brline+"\n");
			for(int i=1;i<=atomnum;i++){
				int astart = start+(i-1)*binsize;
				if(i > 1){
					astart -=1;
				}
				int aend= astart + binsize;
				if(i == 1){
					aend -=1;
				}
				brline = br.readLine();
				if(brline != null){
					String outlien = brline+" "+chrom+":"+astart+".."+aend+"\n";
					bw.write(outlien);
				}
				
				
			}
			
			bw.close();
			br.close();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public static void generateJson(String path,String outfile){
		//String path = "E:\\tangbx\\工作日志\\circosweb\\data\\physical\\batch_pos.xyz";
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line="";
			line = br.readLine();
					line = br.readLine();
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			while((line = br.readLine()) != null){
				
				
				String [] arrs = line.split("\\s+");
				
				if(arrs != null){
					String pos = arrs[4];
					int index1 = pos.indexOf(":");
					 int index2 = pos.indexOf(".");
					 String chrtmp = pos.substring(0,index1);
					 String pos_starttmp = pos.substring(index1+1,index2);
					 String pos_endtmp = pos.substring(index2+2,pos.length());
					
					sb.append("{\"name\":\"").append(arrs[0]).append("\",\"chr\":\"").append(chrtmp).append("\",\"start\":").append(pos_starttmp);
					sb.append(",\"end\":").append(pos_endtmp).append(",\"x\":").append(arrs[1]).append(",\"y\":").append(arrs[2]).append(",\"z\":").append(arrs[3]).append("},");
					
				}
			}
			
			sb.append("]");
			br.close();
			//path = "E:\\tangbx\\工作日志\\circosweb\\data\\physical\\batch_pos.json";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			bw.write(sb.toString());
			bw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
}
