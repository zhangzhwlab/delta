package cn.ac.big.circos.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/*************************************************************
 * this used to draw picture
 * @author jacky
 *
 */
public class DrawPictureService {
	
	/*****************************************************
	 * 
	 * @param dataset
	 * @param trackIdStr
	 * @param chr
	 * @param unit
	 * @param cstart
	 * @param cend
	 * @param time
	 * @param r0
	 * @return picture path
	 */
	public String drawInteraction(String dataset,String trackIdStr,String chr,int unit,int cstart,int cend,String time,String r0){
		String infile = "/leofs/bioweb/tangbx/visualization/download_data/"
				+ dataset
				+ "/"
				+ trackIdStr
				+ "/"
				+ chr
				+ ".txt";
		//need to according to the range position filter the data
		String dcmd = "";
		
		
		
		String outfile = "/leofs/biocloud/biocloud/hic_interaction.conf";

		String interfilestr = "/leofs/biocloud/biocloud/draw_interaction.sh";
		try {
			File interFile = new File(interfilestr);
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(interFile));
			String cmd = "perl /leofs/bioweb/tangbx/visualization/program/interaction_draw.pl -unit "
					+ unit
					+ " -chr "
					+ chr
					+ " -st "
					+ cstart
					+ " -ed "
					+ cend
					+ " -r0 "+r0+" -r1 1r -i "
					+ infile
					+ " -o "
					+ outfile;

			bw.write("#!/bin/sh\n");
			bw.write("#PBS -q genomics\n#PBS -l mem=1gb,walltime=01:00:00,nodes=1:ppn=1\n#HSCHED -s hschedd\n");
			bw.write(cmd + "\n");
			bw.write("perl /software/biosoft/software/circos-0.63-3/bin/circos -conf "
					+ outfile
					+ " -outputdir /leofs/biocloud/biocloud/webserver/apache-tomcat-6.0.26-circosweb/webapps/circosweb/images -outputfile hic_interaction"
					+ time + "\n");
			bw.write("touch /leofs/biocloud/biocloud/draw_interaction.sh.finsh");
			bw.close();

			// chmod 700
			Runtime.getRuntime().exec(
					"chmod 700 " + interfilestr);
			Thread.sleep(5);

			// submit
			Runtime.getRuntime().exec(interfilestr);

			File ffile = new File(
					"/leofs/biocloud/biocloud/draw_interaction.sh.finsh");
			while (true) {
				if (ffile.exists() == true) {
					break;
				}
				Thread.sleep(1000);
			}

			ffile.delete();

			String imagePath = "/circosweb/images/hic_interaction"
					+ time + ".png";
			
			return imagePath;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	/**************************************************************
	 * 
	 * @param dataset
	 * @param track
	 * @param chr
	 * @param trackIndex
	 * @param time
	 * @param unit
	 * @param cstart
	 * @param cend
	 * @param radius
	 * @return picture path
	 */
	public String drawHistogram(String dataset,String track,String chr,int trackIndex,String time,int unit,int cstart,int cend,int radius){
		try {
			String infile = "/leofs/bioweb/tangbx/visualization/download_data/"+dataset+"/"+track+"/"+chr+".txt";
			String outfile = "/leofs/biocloud/biocloud/hic_histogram"+trackIndex+".conf";

			String interfilestr = "/leofs/biocloud/biocloud/draw_histogram"+trackIndex+".sh";
			String outputpic = "hic_histogram"+trackIndex+time;

			File interFile = new File(interfilestr);
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(interFile));
			String cmd = "perl /leofs/bioweb/tangbx/visualization/program/histogram_draw.pl -unit "
					+ unit
					+ " -chr "
					+ chr
					+ " -st "
					+ cstart
					+ " -ed "
					+ cend
					+ " -r0 0.98r -r1 1r -i "
					+ infile
					+ " -o "
					+ outfile + " -radius "+radius+"p";

			bw.write("#!/bin/sh\n");
			bw.write("#PBS -q genomics\n#PBS -l mem=1gb,walltime=01:00:00,nodes=1:ppn=1\n#HSCHED -s hschedd\n");
			bw.write(cmd + "\n");
			bw.write("perl /software/biosoft/software/circos-0.63-3/bin/circos -conf "
					+ outfile
					+ " -outputdir /leofs/biocloud/biocloud/webserver/apache-tomcat-6.0.26-circosweb/webapps/circosweb/images -outputfile "
					+ outputpic + "\n");
			bw.write("touch "+interfilestr+".finsh");
			bw.close();

			// chmod 700
			Runtime.getRuntime().exec(
					"chmod 700 " + interfilestr);
			Thread.sleep(5);

			// submit
			Runtime.getRuntime().exec(interfilestr);

			File ffile = new File(
					interfilestr+".finsh");
			while (true) {
				if (ffile.exists() == true) {
					break;
				}
				Thread.sleep(1000);
			}

			ffile.delete();
			
			String imagestr = "/circosweb/images/"
						+ outputpic + ".png";
			
			return imagestr;
			


		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
