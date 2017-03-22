import java.io.BufferedWriter;
import java.io.FileWriter;

/**************************************
 * this is used to generate heatmap gff3 data
 * 
 * @author lenovo
 *
 */
public class GenHeatmapData {

	public static void main(String args[]){

		generateMatrix();
		generateTriangleMatrix();
		
	}
	
	
	/*********************************************************************
	 * this is used to generate 6*6 matrix
	 * @return
	 */
	public static int generateMatrix(){
		String outfile="E:\\tangbx\\工作日志\\circosweb\\jbrowse插件\\160126\\heatmap.gff3";
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			int start =1;
			int end= 20000; //20k
			int interal = 20000;//20k
			
			int count = 1;
			for(int i=0;i<12;i++){
				int anchor_start = i*interal +1;
				int anchor_end = anchor_start + interal;
				start = anchor_start+50;
				end = anchor_end;
				for(int j=0;j<12;j++){
					
					int target_start = j*interal +1;
					int target_end = target_start + interal;
					
					/*if(anchor_start >= target_end){
						start = target_start;
						end = anchor_end;
					}else{
						start = anchor_start;
						end = target_end;
					}*/
//chr7	Interaction	heatmap	1	20000	1	+	.	ID=1;Name=chr7;Note=chr7:1-20000
					
					bw.write("chr7\tInteraction\theatmap\t"+start+"\t"+end+"\t"+(j+1)+"\t+\t.\tID="+count+";Name=chr7;Note="+anchor_start+"-"+anchor_end+":"+target_start+"-"+target_end+"\n");
					System.out.println(start+" "+end+"  "+anchor_start +" "+anchor_end+" "+target_start+"  "+target_end);
					count ++;
										
				}

			}
			bw.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return 0;
	}
	
	
	
	
	/********************************************************
	 * TAD
	 * @return
	 */
	public static int generateTriangleMatrix(){
		
		String outfile="E:\\tangbx\\工作日志\\circosweb\\jbrowse插件\\160126\\tad.gff3";
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			int start =1;
			int end= 20000; //20k
			int interal = 20000;//20k
			
			int count = 1;
			for(int i=0;i<12;i++){
				int anchor_start = i*interal +1;
				int anchor_end = anchor_start + interal;
				start = anchor_start;
				end = anchor_end;
				for(int j=i;j<12;j++){
					
					int target_start = j*interal +1;
					int target_end = target_start + interal;
					
					/*if(anchor_start >= target_end){
						start = target_start;
						end = anchor_end;
					}else{
						start = anchor_start;
						end = target_end;
					}*/
//chr7	Interaction	heatmap	1	20000	1	+	.	ID=1;Name=chr7;Note=chr7:1-20000
					
					int iqual = j+1;
				   // float fqual =	(float)(Math.log(iqual)/Math.log(2));
					
					bw.write("chr7\tInteraction\ttad\t"+start+"\t"+end+"\t"+iqual+"\t+\t.\tID="+count+";Name=chr7;Note="+anchor_start+"-"+anchor_end+":"+target_start+"-"+target_end+"\n");
					System.out.println(start+" "+end+"  "+anchor_start +" "+anchor_end+" "+target_start+"  "+target_end);
					count ++;
										
				}

			}
			bw.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return 0;
	}
	
	
	
}
