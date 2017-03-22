import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/*********************************************************************
 * this is used to parse ensembl download gene data 
 * we need to change to gff3 format
 * @author lenovo
 *
 */
public class ParseEnsemblGene {

	
	public static void main(String [] args){
	//	parseEnsemblGene();
	//	processEnsemblTranscript();
		try{
			processGeneFromFTP();
			//processTranscriptFromFTP();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		
	}
	
	//by using biomart get gene data
	public static void parseEnsemblGene(){
		
		try{
			String[] chroms=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y","MT"}; 
			String infile="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\mart_export_gene.txt";
			String outpath="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\gff3";
			
			for(String chrom:chroms){
				BufferedReader br = new BufferedReader(new FileReader(infile));
				String outfile = outpath+"\\"+chrom+".gff3" ;
				
			
				BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			
				String line="";
				line=br.readLine();
				while((line=br.readLine()) != null){
					String [] arrs = line.split(",");
					if(chrom.equals(arrs[1]) == true){
						String strand="";
						StringBuffer sb = new StringBuffer();
						if(arrs[4].equals("1")){
							strand ="+";
						}else if(arrs[4].equals("-1")){
							strand="-";
						}
						
						sb.append(chrom+"\tensembl\tgene\t"+arrs[2]+"\t"+arrs[3]+"\t.\t"+strand+"\t.\tID="+arrs[0]+";Name="+arrs[0]+"\n");
						bw.write(sb.toString());
					}
				}
				
				br.close();
				bw.close();
			}
			
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//by using biomart get the transcript data
	
	public static void processEnsemblTranscript(){
		try{
			String[] chroms=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y","MT"}; 
			String infile="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\mart_export_trans.txt";
			String outpath="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\transgff3";
			
			for(String chrom:chroms){
				BufferedReader br = new BufferedReader(new FileReader(infile));
				String outfile = outpath+"\\"+chrom+".gff3" ;
				
				Map map = new HashMap();
				BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			
				String line="";
				line=br.readLine();
				while((line=br.readLine()) != null){
					String [] arrs = line.split(",");
					if(chrom.equals(arrs[2]) == true){
						
						String trans= arrs[1];
						String strand="";
						StringBuffer sb = new StringBuffer();
						if(arrs[5].equals("1")){
							strand ="+";
						}else if(arrs[5].equals("-1")){
							strand="-";
						}
						if(map.containsKey(trans) == true){
							//exon
							sb.append(chrom+"\tensembl\texon\t"+arrs[7]+"\t"+arrs[8]+"\t.\t"+strand+"\t.\tParent="+trans+";Name="+arrs[6]+"\n");
						}else{
							//transcript
							sb.append(chrom+"\tensembl\ttranscript\t"+arrs[3]+"\t"+arrs[4]+"\t.\t"+strand+"\t.\tID="+arrs[1]+";Name="+arrs[1]+"\n");
							//exon
							sb.append(chrom+"\tensembl\texon\t"+arrs[7]+"\t"+arrs[8]+"\t.\t"+strand+"\t.\tParent="+trans+";Name="+arrs[6]+"\n");
							map.put(trans, trans);
						}
						bw.write(sb.toString());	
				
					}
				}
				
				br.close();
				bw.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	
	//download gene gff3 file from ftp
	public static void processGeneFromFTP() throws Exception{
		String[] chroms=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y","MT"}; 
		String infile="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\FTP\\source";
		String outpath="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\FTP\\genegff3";
		String outpath1="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\FTP\\table";
		for(String chrom:chroms){
			String gfffile = infile+File.separator+chrom+".gff3" ;
			String outfile = outpath+File.separator+chrom+".gff3" ;
			String outfile1= outpath1+File.separator+chrom+".txt" ;
			BufferedReader br = new BufferedReader(new FileReader(gfffile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile)); 
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(outfile1)); 
			String line="";
			while((line=br.readLine()) != null){
				String [] cos9 = line.split("\t") ;
				
				if(cos9.length==9){
					String linechrom = cos9[0];
					if(linechrom.equals(chrom)&&cos9[8].startsWith("ID=gene")){
						bw.write(line+"\n");
						
						String geneid="";
						String genename="";
						String tablestr="";
						String [] notes = cos9[8].split(";");
						if(notes!=null){
							for(int i=0;i<notes.length;i++){
								String note = notes[i];
								if(note.startsWith("ID=")){
									String [] ids = note.split("=");
									if(ids!=null){
										String [] genes = ids[1].split(":");
										if(genes!=null){
											geneid=genes[1];
										}
									}
								}
								if(note.startsWith("Name")){
									String []names = note.split("=");
									if(names!=null){
										genename= names[1];
									}
									
								}
							}
						}
						String strand;
						if(cos9[6].equals("+")){
							strand ="1";
						}else{
							strand = "-1";
						}
						tablestr = geneid+"\t"+cos9[0]+"\t"+cos9[3]+"\t"+cos9[4]+"\t"+strand+"\t"+genename+"\t0";
						bw1.write(tablestr+"\n");
						
					}
				
				}
			}
			br.close();
			bw.close();		
			bw1.close();
			
			
			
			
		}
	} 
	
	//download gene gff3 file from ftp,then parse transcript
	
	public static void processTranscriptFromFTP() throws Exception{
		
		String[] chroms=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y","MT"}; 
		String infile="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\FTP\\source";
		String outpath="E:\\tangbx\\工作日志\\circosweb\\data\\ensembl_gene\\20160826\\FTP\\transgff3";
		for(String chrom:chroms){
			String gfffile = infile+File.separator+chrom+".gff3" ;
			String outfile = outpath+File.separator+chrom+".gff3" ;
			BufferedReader br = new BufferedReader(new FileReader(gfffile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile)); 
			String line="";
			while((line=br.readLine()) != null){
				String [] cos9 = line.split("\t") ;
				
				if(cos9.length==9){
					String linechrom = cos9[0];
					if(linechrom.equals(chrom)&&(cos9[8].startsWith("ID=transcript")||cos9[0].startsWith("Parent=transcript"))){
						String gffline="";
						if(cos9[8].startsWith("ID=transcript")){
							 gffline = cos9[0]+"\t"+cos9[1]+"\t"+cos9[2]+"\t"+cos9[3]+"\t"+cos9[4]+"\t"+cos9[5]+"\t"+cos9[6]+"\t"+cos9[7];
							String[] notes = cos9[8].split(";");
							if(notes!= null && notes.length>0){
								String note = notes[0];
								for(int i=2;i<notes.length;i++){
									note +=";"+notes[i];
								}
								
								gffline +="\t"+note+"\n";
							}
						}else{
							 gffline = line+"\n";
						}
						if(gffline.length() >0){
							bw.write(gffline);
						}
					}
					
				}
			}
			br.close();
			bw.close();
			
		}
		
	}
	
	
}
