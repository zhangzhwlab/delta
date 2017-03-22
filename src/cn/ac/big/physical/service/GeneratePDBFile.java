package cn.ac.big.physical.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;

/*************************************************
 * this used to parse mode_p.txt to pdb file
 * @author jacky
 *
 */
public class GeneratePDBFile {
	private String infilePath;
	private String outfilePath;
	
	public GeneratePDBFile(String infile,String outfile){
			this.infilePath = infile;
			this.outfilePath = outfile;
	}
	
	
	/*****************************************************
	 * we used MCMC5C pdb file to transform BACH mode_p.txt file
	 * @return
	 */
	public int transfromBACHResult(){
		int iret = 0;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(infilePath));
			String line ="";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfilePath));
			int count =0;
			while((line=br.readLine()) != null){
				count ++;
				String [] arrs = line.split("\\s+");
				StringBuffer sb = new StringBuffer();
				sb.append("ATOM  ");
				String scount =  count+"";
				for(int i=0;i<5-scount.length();i++){
					sb.append(" ");
				}
				sb.append(count);
				sb.append(" C    LIG A");
				float x = Float.parseFloat(arrs[0]);
				 DecimalFormat   fnum  =   new  DecimalFormat("##0.000");    
				 String  dx=fnum.format(x);
				 for(int i=0;i<16-dx.length();i++){
					 sb.append(" ");
				 }
				 sb.append(dx);
				 
				 float y = Float.parseFloat(arrs[1]);
				 fnum  =   new  DecimalFormat("##0.000");    
				 String  dy=fnum.format(y);
				 for(int i=0;i<8-dy.length();i++){
					 sb.append(" ");
				 }
				 sb.append(dy);
				 
				 
				 float z = Float.parseFloat(arrs[2]);
				 fnum  =   new  DecimalFormat("##0.000");    
				 String  dz=fnum.format(z);
				 for(int i=0;i<8-dz.length();i++){
					 sb.append(" ");
				 }
				 sb.append(dz);
				 sb.append("  1.00 75.00    \n");
				 
				bw.write(sb.toString());				
			}
			count--;
			for(int i=0;i<count;i++){
				int begin = i+1;
				int end = i+2;
				String bs = begin+"";
				String be = end+"";
				StringBuffer sb = new StringBuffer();
				sb.append("CONECT");
				 for(int j=0;j<5-bs.length();j++){
					 sb.append(" ");
				 }
				 sb.append(bs);
				 
				 for(int k=0;k<4-be.length();k++){
					 sb.append(" ");
				 }
				 sb.append(be).append("\n");;
				
				bw.write(sb.toString());
			}
			bw.write("END\n");
			bw.close();
			br.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
			iret = -1;
		}
		
		return iret;
		
	}
	
	public static void main(String []args){
		String infile = "D:/tangbx/myespace/.metadata/.me_tcat7/webapps/circosweb/pages/visualization/data/mode_p.txt";
		String outfile ="D:/tangbx/myespace/.metadata/.me_tcat7/webapps/circosweb/pages/visualization/data/mode_p.pdb";
		GeneratePDBFile pdb = new GeneratePDBFile(infile,outfile);
		pdb.transfromBACHResult();
		
		
		
		
	}
}
