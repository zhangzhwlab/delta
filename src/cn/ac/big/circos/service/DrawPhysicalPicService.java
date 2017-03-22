package cn.ac.big.circos.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import cn.ac.big.physical.service.GeneratePDBFile;

/******************************************
 * this used to draw physical picture service
 * @author jacky
 *
 */
public class DrawPhysicalPicService {

	/***************************************
	 * cut to get given column cut$'\t'
	 * get given row
	 * @param dataset
	 * @param chr
	 * @param start
	 * @param end
	 * @param time
	 * @return
	 */
	public String drawBACHPic(String dataset,String chr,int start,int end,String time){
		String imagepath = null;
		
		String infile = "/share/disk1/work/bioinformatics/tangbx/visual/input_data/"+dataset+"/"+chr+".cov";
		
		String shellpath="/share/disk1/work/bioinformatics/tangbx/visual/exec_script/";
		
		String outpath = "/share/disk1/work/bioinformatics/tangbx/visual/output_data";
		
		String finalResPath="/share/disk1/work/bioinformatics/tangbx/webserver/apache-tomcat-circosweb/webapps/circosweb/pages/visualization/data/";
		
		String shellfile =shellpath+time+".sh";
		String finishfile = shellfile+".finish";
		try{
		
			
			File outfile = new File(outpath+"/"+time);
			if(outfile.exists() ==false){
				outfile.mkdirs();
			}
		
			
			outfile = new File(finalResPath);
			if(outfile.exists() == false){
				outfile.mkdirs();				
			}
			
			String outcov = outpath+"/"+time+"/"+chr+".cov.change";
			
		
			String inheatmap = "/share/disk1/work/bioinformatics/tangbx/visual/input_data/"+dataset+"/"+chr+".heatmap";
			String outheatmap = outpath+"/"+time+"/"+chr+".heatmap.change";
			
			
			
		
			File interFile = new File(shellfile);
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(shellfile));
			
			String cmd = "sed -n '"+start+","+end+"p' "+infile +">"+outcov;
			
			bw.write("#!/bin/sh\n");
			bw.write("#PBS -q workq\n#PBS -o "+shellpath+time+".out\n#PBS -e "+shellpath+time+".err\n");
			bw.write(cmd+"\n"); //cov
			cmd = "sed -n '"+start+","+end+"p' "+inheatmap +">"+outpath+"/"+time+"/"+chr+".heatmap.tmp";
			bw.write(cmd + "\n");
			cmd = "cut -d$'\\t' -f"+start+"-"+end+" "+outpath+"/"+time+"/"+chr+".heatmap.tmp > "+outheatmap;
			bw.write(cmd + "\n");
			cmd="/home/tangbx/study/BACH_src/BACH -i "+outheatmap+" -v "+outcov+" -K 100 -MP 10 -NG 500 -NT 50 -L 50 -SEED 1 -o "+outpath+"/"+time;
			bw.write(cmd + "\n");
			bw.write("touch "+finishfile);
		
		
			bw.close();
		
			// chmod 700
			Runtime.getRuntime().exec(
					"chmod 700 " + shellfile);
			Thread.sleep(5);

			// submit
			Runtime.getRuntime().exec("qsub " + shellfile);
			
			
			File ffile = new File(finishfile);
			while (true) {
				if (ffile.exists() == true) {
					break;
				}
				Thread.sleep(1000);
			}

			ffile.delete();

			
			GeneratePDBFile genPDB = new GeneratePDBFile(outpath+"/"+time+"/mode_p.txt",finalResPath+"/"+time+".pdb");
			genPDB.transfromBACHResult();
			imagepath="/circosweb/pages/visualization/data/"+time+".pdb";	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
		return imagepath;
		
	}
}
