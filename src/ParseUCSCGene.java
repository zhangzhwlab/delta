import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/***********************************************************
 * this is used to parse data from ucsc browser
 * @author lenovo
 *
 */
public class ParseUCSCGene {
	
	public static void main(String [] args){
		
		
		parseEnsembleTranscriptToGFF3();
	}
	
	
	/*************************************************
	 * this is used to parse ucsc ensemble gene to gff3 format(Transcript)
	 * 
	 */
	public static void parseEnsembleTranscriptToGFF3(){
		try{
			String[] chroms=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y","M"};
			String path="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\fromucsc\\human\\hg19";
			String outpath = "E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\fromucsc\\human\\other_gff3";
			
			/*String [] chroms = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","X","Y","M"};
			String path = "E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\fromucsc\\mouse\\mm10";
			String outpath = "E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\fromucsc\\mouse\\transcript_gff3";*/
			for(String chrom:chroms){
					BufferedReader br = new BufferedReader(new FileReader(path));
				
					
					
					//Read Ensembl Gene , only get the transcript which exist in the ensembl download gene
					
					String changechrom = chrom;
					if(chrom.equals("M")){
						changechrom = "MT";
					}
					String GeneFile = "E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\gff3\\"+changechrom+".gff3";
					BufferedReader genebr = new BufferedReader(new FileReader(GeneFile));
					String geneline="";
					Map map = new HashMap();
					while((geneline=genebr.readLine()) != null){
						String [] arrs = geneline.split("\\s+");
						String[] col9 = arrs[8].split(";");
						String [] idstr = col9[0].split("=");
						String ID= idstr[1];
						//System.out.println(ID);
						map.put(ID, ID);
					}
					genebr.close();
					
					String outfile = outpath+"\\"+changechrom+".gff3" ;
					BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
				
					String line="";
					line=br.readLine();
					while((line=br.readLine()) != null){
						String [] arrs = line.split("\\s+");
						String tchrom = "chr"+chrom;
						String transcript = arrs[1];
						String gene = arrs[12];
						StringBuffer sb = new StringBuffer();
						
						if(arrs[2].equals(tchrom)){
							
							if(map.containsKey(gene) == true){
								sb.append(changechrom+"\tensembl\ttranscript\t"+arrs[4]+"\t"+arrs[5]+"\t.\t"+arrs[3]+"\t.\tID="+arrs[1]+";Name="+arrs[1]+"\n");
								String [] exonsstart = arrs[9].split(",");
								String [] exonsend = arrs[10].split(",");
								int count = 0 ;
								for(int j=0;j<exonsstart.length;j++){
									count ++;
									String exonstr = "exon"+count;
									String exons = exonsstart[j];
									String exone = exonsend[j];
									sb.append(changechrom+"\tensembl\texon\t"+exons+"\t"+exone+"\t.\t"+arrs[3]+"\t.\tParent="+transcript+";Name="+exonstr+"\n");
								}
								
								
								String sbstr = sb.toString();
								if(sbstr.length() >0 ){
									bw.write(sbstr);
								}
								
								
							}
							
							
							
						}
					}
					
				
					br.close();
					bw.close();
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	/**********************************************************
	 * this is used to filter ensembl gene from Ensembl mart query result 
	 * the gene which come from above transcript file result
	 */
	
	public static void processFilterEnsemblGene(){
		try{
			
			String[] chroms=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","X","Y","M"};
			for(String chrom :chroms){
				String genepath = "";
				String genefilename = "";
				
				
				BufferedReader br2 = new BufferedReader(new FileReader(genefilename));
				//read gene name into a hashmap
				String line="";
				Map map = new HashMap();
				while((line = br2.readLine()) != null){ // store all of the gene
					map.put(line, line) ;
				}
				br2.close();
				
				
				String genegff = "";
				BufferedWriter bw = new BufferedWriter(new FileWriter(genegff));
				//read gene gff file
				BufferedReader br1 = new BufferedReader(new FileReader(genepath));
				while((line=br1.readLine()) != null){
					String [] arrs = line.split("\\s+") ;
					String strand = arrs[4];
					if(strand.equals("1")){
						strand = "+";
					}else {
						strand = "-";
					}
					if(map.containsKey(arrs[0])){
						StringBuffer sb = new StringBuffer();
						sb.append(arrs[1]+"\tensembl\tgene\t"+arrs[2]+"\t"+arrs[3]+"\t.\t"+strand+"\t.\tID="+arrs[0]+";Name="+arrs[0]+"\n");
						bw.write(sb.toString());					
					}				
				}
				br1.close();
				
				bw.close();
			}
			
			
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
