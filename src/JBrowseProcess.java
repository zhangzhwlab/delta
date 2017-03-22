import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;


public class JBrowseProcess {
	public static void main(String [] args){
		
		
		//String file ="E:\\tangbx\\工作日志\\circosweb\\dataprocess\\helas3.signalfile";
	//	generateSignalString(file);
		
		
		
		
		
		String cell="hepg2";
		String [] peaks1={"CTCF","EZH2" ,"H2AZ","H3k4me1","H3k9me3", "H3k27ac", "H3k27me3", "H3k36me3"  ,"H3k4me2" ,"H3k4me3", "H3k79me2" ,"H3k9ac"  ,"H4k20me1","RAD21" ,"SMC3"};
		String [] sipeak1={"CTCF","EZH2" ,"H2AZ","H3k4me1","H3k9me3", "H3k27ac", "H3k27me3" ,"H3k4me2" ,"H3k4me3", "H3k79me2" ,"H3k9ac"  ,"H4k20me1","rnaseq_minus_rep1" ,"rnaseq_minus_rep2",
		"rnaseq_plus_rep1","RNA seq_plus_rep2","Dnaseq","RAD21","SMC3"};
		String [] signals1={"wgEncodeBroadHistoneHepg2CtcfStdSig.bigWig","wgEncodeBroadHistoneHepg2Ezh239875Sig.bigWig","wgEncodeBroadHistoneHepg2H2azStdSig.bigWig",
		"wgEncodeBroadHistoneHepg2H3k04me1StdSig.bigWig","wgEncodeBroadHistoneHepg2H3k09me3Sig.bigWig","wgEncodeBroadHistoneHepg2H3k27acStdSig.bigWig",
		"wgEncodeBroadHistoneHepg2H3k27me3StdSig.bigWig","wgEncodeBroadHistoneHepg2H3k4me2StdSig.bigWig",
		"wgEncodeBroadHistoneHepg2H3k4me3StdSig.bigWig","wgEncodeBroadHistoneHepg2H3k79me2StdSig.bigWig","wgEncodeBroadHistoneHepg2H3k9acStdSig.bigWig",
		"wgEncodeBroadHistoneHepg2H4k20me1StdSig.bigWig","wgEncodeCaltechRnaSeqHepg2R1x75dTh1014UMinusRawRep1V4.bigWig",
		"wgEncodeCaltechRnaSeqHepg2R1x75dTh1014UMinusRawRep2V4.bigWig","wgEncodeCaltechRnaSeqHepg2R1x75dTh1014UPlusRawRep1V4.bigWig",
		"wgEncodeCaltechRnaSeqHepg2R1x75dTh1014UPlusRawRep2V4.bigWig","wgEncodeOpenChromDnaseHepg2Sig.bigWig","wgEncodeSydhTfbsHepg2Rad21IggrabSig.bigWig",
		"wgEncodeSydhTfbsHepg2Smc3ab9263IggrabSig.bigWig"};
	//	generatePeakJsonList(cell,peaks1);
	//	generateSignalJson(cell,sipeak1,signals1);
		//generateCircletViewConfig(cell,peaks1);
	//	generatePhysicalViewConfig(cell,peaks1);
		
		
	//	generatePeakJsonList(cell,peaks2);
	//	generateSignalJson(cell,peaks2,signals2);
	//	generateCircletViewConfig();
		//generatePhysicalViewConfig(cell,peaks2);
		
		//generateSignalTrackListJson();
		//generatePeakTrackListJson();
		
		generateDownloadMetaData();
		
	}
	
	
	//this is used to generate jbrowse track list json
		/*
		 * {
			 "category" : "ENCODE/GM12878/Signal",
	         "label"         : "GM12878_H3k4me1_signal",
	         "key"           : "GM12878_H3k4me1_signal",
	         "storeClass"    : "JBrowse/Store/SeqFeature/BigWig",
	         "urlTemplate"   : "tracks/gm12878/signal/wgEncodeBroadHistoneGm12878H3k04me1StdSigV2.bigWig",
	         "type"          : "JBrowse/View/Track/Wiggle/XYPlot",
	         "variance_band" : false,
			 "min_score"     : -200,
	         "max_score"     : 200,
	         "style": {
	             "pos_color"         : "blue",
	             "neg_color"         : "#005EFF",
	             "clip_marker_color" : "yellow",
	             "height"            : 100
	         }
	      },
		 * GM12878	GM12878_H3k4me1_signal  tracks/gm12878/signal/wgEncodeBroadHistoneGm12878H3k04me1StdSigV2.bigWig
		 * */
		public static void generateSignalTrackListJson(){
			
			try{
				String infile="E:\\tangbx\\工作日志\\circosweb\\pipeline\\15\\dataprocess\\signal\\jbrowse_signal.txt";
				String outfile="E:\\tangbx\\工作日志\\circosweb\\pipeline\\15\\jbrowse\\signaltrack.json";
				
				BufferedReader br = new BufferedReader(new FileReader(infile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
				String line="";
				while((line=br.readLine())!= null){
					String [] arrs = line.split("\t");
					StringBuffer sb = new StringBuffer();
					String key = arrs[0]+"_"+arrs[1]+"_signal";
					sb.append("{\n").append("\"category\" : \"ENCODE/"+arrs[0]+"/Signal\",\n");
					sb.append("\"key\"           : \""+key+"\",\n");
					sb.append("\"label\"         : \""+key+"\",\n");
					sb.append("\"storeClass\"    : \"JBrowse/Store/SeqFeature/BigWig\",\n");
					sb.append("\"urlTemplate\"   : \""+arrs[2]+"\",\n");
					sb.append("\"type\"          : \"JBrowse/View/Track/Wiggle/XYPlot\",\n");
					sb.append("\"variance_band\" : false,\n");
					
					sb.append("\"min_score\"     : 0,\n");
					if(arrs[1].indexOf("DNase-seq") > -1){
						sb.append("\"max_score\"     : 1,\n");
					}else{
						sb.append("\"max_score\"     : 200,\n");
					}
					
					sb.append(" \"style\": {\n");
					sb.append("\"pos_color\"         : \"blue\",\n");
					sb.append("\"neg_color\"         : \"#005EFF\",\n");
					sb.append("\"clip_marker_color\" : \"yellow\",\n");
					sb.append("\"height\"            : 100\n");
					sb.append("}\n");
					sb.append("},\n");
					bw.write(sb.toString());
				}
				
				
				br.close();
				bw.close();
				
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			
		}
		
		//generate jbrowse histon mark json 
		/*
		 * {
			"category" : "ENCODE/HUVEC/peak",
			"label"         : "huvec_EZH2",
			"key"         : "huvec_EZH2",
			"storeClass"    : "JBrowse/Store/SeqFeature/GFF3",
			"urlTemplate"   : "tracks/huvec/EZH2/{refseq}/peak.gff3",
			"menuTemplate" : [
			{
			"label" :"View details"
			},
			{
			"iconClass" : "dijitIconFile",
			"url" : "function(track,feature){var reg = new RegExp('(^|&)data=([^&]*)(&|$)', 'i'); var r = window.location.search.substr(1).match(reg); var data=null;var url='http://hicp.big.ac.cn/pages/visualization/topo_viewm.jsp?tracks=huvec_EZH2,Interaction,ensembl_gene&';if (r != null) { data=unescape(r[2]); }if(data != null){url +='conf='+data+'&'; }url += 'loc={seq_id}:{start}..{end}' ;return url;}",
			"action" : "newWindow",
			"label" : "Circlet View"
			},
			{
			"iconClass" : "dijitIconFile",
			"url" : "function(track,feature){ var reg = new RegExp('(^|&)data=([^&]*)(&|$)', 'i'); var r = window.location.search.substr(1).match(reg); var data=null;var url='http://hicp.big.ac.cn/pages/visualization/physical_view.jsp?tracks=3dmodel,huvec_EZH2&';if (r != null) { data=unescape(r[2]); }if(data != null){url +='conf='+data+'&'; } url += 'loc={seq_id}:{start}..{end}' ;return url;}",
			"action" : "newWindow",
			"label" : "Physical View"
			}
			],
			"type"          : "FeatureTrack",
			"variance_band" : false,
			"min_score"     : -200,
			"max_score"     : 200,
			"style": {
			"pos_color"         :"blue",
			"neg_color"         : "#005EFF",
			"clip_marker_color" : "yellow",
			"height"            : 100
			}
			 },
		 * 
		 *  EZH2  HUVEC
		 * */
		public static void generatePeakTrackListJson(){
			try{
				String infile="E:\\tangbx\\工作日志\\circosweb\\pipeline\\15\\dataprocess\\peak\\jbrowse_peak.txt";
				String outfile="E:\\tangbx\\工作日志\\circosweb\\pipeline\\15\\jbrowse\\peaktrack.json";
				
				BufferedReader br = new BufferedReader(new FileReader(infile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
				String line="";
				while((line=br.readLine())!= null){
					String [] arrs = line.split("\t");
					String key = arrs[1]+"_"+arrs[0];
					String mark = arrs[2];
					StringBuffer sb = new StringBuffer();
					sb.append("{\n");
					sb.append("\"category\" : \"ENCODE/"+arrs[1]+"/peak\",\n");
					sb.append("\"label\"         : \""+key+"\",\n");
					sb.append("\"key\"         : \""+key+"\",\n");
					sb.append("\"storeClass\"    : \"JBrowse/Store/SeqFeature/NCList\",\n");
					sb.append("\"urlTemplate\"   : \"tracks/"+arrs[1].toLowerCase()+"/"+mark+"/{refseq}/trackData.json\",\n");
					sb.append("\"menuTemplate\" : [\n");
					sb.append("{\n");
					sb.append("\"label\" :\"View details\"\n");
					sb.append("},\n");
					sb.append("{\n");
					sb.append("\"iconClass\" : \"dijitIconFile\",\n");
					sb.append("\"url\" : \"function(track,feature){var reg = new RegExp('(^|&)data=([^&]*)(&|$)', 'i'); var r = window.location.search.substr(1).match(reg); var data=null;var url='http://hicp.big.ac.cn/pages/visualization/topo_viewm.jsp?tracks="+key+",Interaction,ensembl_gene&';if (r != null) { data=unescape(r[2]); }if(data != null){url +='conf='+data+'&'; }url += 'loc={seq_id}:{start}..{end}' ;return url;}\",\n");
					sb.append("\"action\" : \"newWindow\",\n");
					sb.append("\"label\" : \"Circlet View\"\n");
					sb.append("},\n");
					sb.append("{\n");
					sb.append("\"iconClass\" : \"dijitIconFile\",\n");
					sb.append("\"url\" : \"function(track,feature){ var reg = new RegExp('(^|&)data=([^&]*)(&|$)', 'i'); var r = window.location.search.substr(1).match(reg); var data=null;var url='http://hicp.big.ac.cn/pages/visualization/physical_view.jsp?tracks=3dmodel,"+key+"&';if (r != null) { data=unescape(r[2]); }if(data != null){url +='conf='+data+'&'; } url += 'loc={seq_id}:{start}..{end}' ;return url;}\",\n");
					sb.append("\"action\" : \"newWindow\",\n");
					sb.append("\"label\" : \"Physical View\"\n");
					sb.append("}],\n");
					sb.append("\"type\"          : \"FeatureTrack\",\n");
					sb.append("\"variance_band\" : false,\n");
					sb.append("\"min_score\"     : 0,\n");
					
					sb.append("\"max_score\"     : 200,\n");
					sb.append("\"style\": {\n");
					sb.append("\"pos_color\"         :\"blue\",\n");
					sb.append("\"neg_color\"         : \"#005EFF\",\n");
					sb.append("\"clip_marker_color\" : \"yellow\",\n");
					sb.append("\"height\"            : 100\n");
					sb.append("}\n");
					sb.append("},\n");
					bw.write(sb.toString());
				}
				
				
				br.close();
				bw.close();
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			
		}
	
	
	public static void generatePeakJsonList(String cell,String []peaks){
		try{
		
			String file ="E:\\tangbx\\工作日志\\circosweb\\dataprocess\\trackjson."+cell;
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			
			for(String peak:peaks){
				StringBuffer sb= new StringBuffer();
				sb.append("{\n").append("\"category\" : \"ENCODE/"+cell.toUpperCase()+"/peak\",\n");
				sb.append("\"label\"         : \""+cell+"_"+peak+"\",\n");
				sb.append("\"key\"         : \""+cell+"_"+peak+"\",\n");
				sb.append("\"storeClass\"    : \"JBrowse/Store/SeqFeature/GFF3\",\n");
				sb.append("\"urlTemplate\"   : \"tracks/"+cell+"/"+peak+"/{refseq}/peak.gff3\",\n");
				sb.append("\"menuTemplate\" : [\n");
				sb.append("{\n\"label\" :\"View details\"\n},\n");
				sb.append("{\n\"iconClass\" : \"dijitIconFile\",\n");
				sb.append("\"url\" : \"function(track,feature){var reg = new RegExp('(^|&)data=([^&]*)(&|$)', 'i'); var r = window.location.search.substr(1).match(reg); var data=null;var url='http://hicp.big.ac.cn/pages/visualization/topo_viewm.jsp?tracks="+cell+"_"+peak+",Interaction,ensembl_gene&';if (r != null) { data=unescape(r[2]); }if(data != null){url +='conf='+data+'&'; }url += 'loc={seq_id}:{start}..{end}' ;return url;}\",\n");
				sb.append("\"action\" : \"newWindow\",\n");
				sb.append("\"label\" : \"Circlet View\"\n},\n");
				sb.append("{\n\"iconClass\" : \"dijitIconFile\",\n");
				sb.append("\"url\" : \"function(track,feature){ var reg = new RegExp('(^|&)data=([^&]*)(&|$)', 'i'); var r = window.location.search.substr(1).match(reg); var data=null;var url='http://hicp.big.ac.cn/pages/visualization/physical_view.jsp?tracks=3dmodel,"+cell+"_"+peak+"&';if (r != null) { data=unescape(r[2]); }if(data != null){url +='conf='+data+'&'; } url += 'loc={seq_id}:{start}..{end}' ;return url;}\",\n");
				sb.append("\"action\" : \"newWindow\",\n");
				sb.append("\"label\" : \"Physical View\"\n}\n],\n");
				sb.append("\"type\"          : \"FeatureTrack\",\n");
				sb.append("\"variance_band\" : false,\n");
				sb.append("\"min_score\"     : -200,\n");
				sb.append("\"max_score\"     : 200,\n");
				sb.append("\"style\": {\n");
				sb.append("\"pos_color\"         :\"blue\",\n");
				sb.append("\"neg_color\"         : \"#005EFF\",\n");
				sb.append("\"clip_marker_color\" : \"yellow\",\n");
				sb.append("\"height\"            : 100\n");
				sb.append("}\n");
				sb.append(" },\n");
				bw.write(sb.toString());
				
			}
			bw.close();
		}catch(Exception ex){
			
		}
	}
	
	/*{
		 "category" : "ENCODE/h1ESC/Signal",
         "label"         : "Broad CTCF",
         "key"           : "Broad CTCF",
         "storeClass"    : "JBrowse/Store/SeqFeature/BigWig",
         "urlTemplate"   : "tracks/h1esc_signal/wgEncodeBroadHistoneH1hescCtcfStdSig.bigWig",
         "type"          : "JBrowse/View/Track/Wiggle/XYPlot",
         "variance_band" : false,
		 "min_score"     : -200,
         "max_score"     : 200,
         "style": {
             "pos_color"         : "blue",
             "neg_color"         : "#005EFF",
             "clip_marker_color" : "yellow",
             "height"            : 100
         }
      },*/
	public static void generateSignalJson(String cell,String [] peaks,String []signals){
		try{
			String file ="E:\\tangbx\\工作日志\\circosweb\\dataprocess\\trackjson_signal."+cell;
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			int count =0 ;
			for(String peak:signals){
				StringBuffer sb= new StringBuffer();
				
				sb.append("{\n");
				sb.append("\"category\" : \"ENCODE/"+cell.toUpperCase()+"/Signal\",\n");
				sb.append("\"label\"         : \""+cell+"_"+peaks[count]+"_signal\",\n");
				sb.append("\"key\"           : \""+cell+"_"+peaks[count]+"_signal\",\n");
				sb.append("\"storeClass\"    : \"JBrowse/Store/SeqFeature/BigWig\",\n");
				sb.append("\"urlTemplate\"   : \"tracks/"+cell+"/signal/"+peak+"\",\n");
				sb.append("\"type\"          : \"JBrowse/View/Track/Wiggle/XYPlot\",\n");
				sb.append("\"variance_band\" : false,\n");
				sb.append("\"min_score\"     : -200,\n");
				sb.append("\"max_score\"     : 200,\n");
				sb.append("\"style\": {\n");
				sb.append("\"pos_color\"         : \"blue\",\n");
				sb.append("\"neg_color\"         : \"#005EFF\",\n");
				sb.append("\"clip_marker_color\" : \"yellow\",\n");
				sb.append(" \"height\"            : 100\n");
				sb.append("}\n");
				sb.append("},\n");
				count++;
				bw.write(sb.toString());
			}
			bw.close();
			
		}catch(Exception ex){
			
		}
		
	}
	
	public static void generateSignalString(String file){
		try{
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line="";
			String signal="";
			while((line=br.readLine()) !=null){
				signal+="\""+line+"\",";
			}
			br.close();
			System.out.println(signal);
		}catch(Exception ex){
			
		}
	}
	
	
	
	//used for circlet view
	/*
	 * [Znf]
glyph_type=histogram
storageClass=GFF3
storage=/share/disk1/work/bioinformatics/tangbx/hic/dataprocess/human/gm12878/peak/srt/Znf/tabix
histone_bin=200000
statis_file=/share/disk1/work/bioinformatics/tangbx/hic/dataprocess/human/gm12878/peak/statics/Znf
line_width=20
color=blue
pcolor=rgb(0,0,255)
ncolor=rgb(255,0,0)
height=50
key=gm12878_Znf
category=ENCODE(GM12878)
	 * */
	public static void generateCircletViewConfig(){
		try{
			String [] colors={"red","blue","green","yellow","orange","purple"};
			Map map = new HashMap();
			map.put("DNase-seq", "red");
			map.put("RNA-seq_minus", "lime");
			map.put("RNA-seq_plus", "blue");
			map.put("CTCF", "orange");
			map.put("EZH2", "yellow");
			map.put("H2AZ", "purple");
			map.put("H3k27ac", "cyan");
			map.put("H3k27me3", "red");
			map.put("H3k36me3", "lime");
			map.put("H3k4me1", "blue");
			map.put("H3k4me2", "orange");
			map.put("H3k4me3", "yellow");
			map.put("H3k79me2", "purple");
			map.put("H3k79me3", "cyan");
			map.put("H3k9ac", "red");
			map.put("H3k9me1", "lime");
			map.put("H3k9me3", "blue");
			map.put("H4k20me1", "orange");
			map.put("RAD21", "yellow");
			map.put("RIP-seq", "purple");
			map.put("SMC3", "cyan");
			map.put("ZNF143", "red");
			
			String infile = "E:\\tangbx\\工作日志\\circosweb\\pipeline\\15\\dataprocess\\signal\\jbrowse_signal.txt";
			String file ="E:\\tangbx\\工作日志\\circosweb\\dataprocess\\circlet\\circlet.conf";
		
			BufferedReader br = new BufferedReader(new FileReader(infile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String line="";
			
			
			
			while((line=br.readLine()) != null){
				String [] arrs = line.split("\\s+") ;
				String cell = arrs[0];
			
				String peak_title = arrs[1];
				String peak = arrs[3];
				String datacell = cell.toLowerCase();
				
				
				if(datacell.equals("h1-hesc")){
					datacell = "h1hESC";
				}else if(datacell.equals("hela-s3")){
					datacell = "helas3";
				}
				
				
			    System.out.println(peak_title);
				String color = map.get(peak_title).toString();
				
				StringBuffer sb= new StringBuffer();
				sb.append("["+cell+"_"+peak_title+"]\n");
				sb.append("glyph_type=histogram\n");
				sb.append("fileClass=GFF3\n");
				sb.append("storage=/share/disk1/work/bioinformatics/tangbx/hic/dataprocess/human/"+datacell+"/signal/srt/"+peak+"/tabix\n");
				sb.append("histone_bin=200000\n");
				sb.append("statis_file=/share/disk1/work/bioinformatics/tangbx/hic/dataprocess/human/"+datacell+"/signal/statics/"+peak+"\n");
				sb.append("line_width=20\n");
				if(peak_title.equals("DNase-seq")){
					sb.append("toomany=500000\n");
				}
				sb.append("color="+color+"\n");
				sb.append("pcolor=rgb(0,0,255)\n");
				sb.append("ncolor=rgb(255,0,0)\n");
				sb.append("height=50\n");
				sb.append("key="+cell+"_"+peak_title+"\n");
				sb.append("category=ENCODE("+cell+")\n\n");
				bw.write(sb.toString());
			
			}
			
				
			br.close();
			bw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}

		
	}
	
	
	//used to generate phsyical view configuration file
	/*
	 * [human_k562_Znf]
glyph_type=circle
storage=GFF3
file=/share/disk1/work/bioinformatics/tangbx/hic/dataprocess/human/k562/peak/srt/Znf/stat_physical/{binsize}/{zoom}/{refseq}/density
color=purple
line_width=1
key=k562_Znf
category=human/k562
	 * 
	 * 
	 * use bigwig file to draw bin quality
	 * **/
	public static void generatePhysicalViewConfig(String cellt,String[] peaks){
		try{
			String [] colors={"red","blue","green","yellow","orange","purple"};
			Map map = new HashMap();
			map.put("DNase-seq", "red");
			map.put("RNA-seq_minus", "lime");
			map.put("RNA-seq_plus", "blue");
			map.put("CTCF", "orange");
			map.put("EZH2", "yellow");
			map.put("H2AZ", "purple");
			map.put("H3k27ac", "cyan");
			map.put("H3k27me3", "red");
			map.put("H3k36me3", "lime");
			map.put("H3k4me1", "blue");
			map.put("H3k4me2", "orange");
			map.put("H3k4me3", "yellow");
			map.put("H3k79me2", "purple");
			map.put("H3k79me3", "cyan");
			map.put("H3k9ac", "red");
			map.put("H3k9me1", "lime");
			map.put("H3k9me3", "blue");
			map.put("H4k20me1", "orange");
			map.put("RAD21", "yellow");
			map.put("RIP-seq", "purple");
			map.put("SMC3", "cyan");
			map.put("ZNF143", "red");
			
			
			String infile = "E:\\tangbx\\工作日志\\circosweb\\pipeline\\15\\dataprocess\\signal\\jbrowse_signal.txt";
			
			
			String file ="E:\\tangbx\\工作日志\\circosweb\\dataprocess\\physical\\physical.conf";
			BufferedReader br = new BufferedReader(new FileReader(infile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			String line="";
	
			while((line=br.readLine()) != null){
				String [] arrs = line.split("\\s+") ;
				String cell = arrs[0];
				
				String peak_title = arrs[1];
				String peak = arrs[3];
				String datacell = cell.toLowerCase();
				
				
				if(datacell.equals("h1-hesc")){
					datacell = "h1hESC";
				}else if(datacell.equals("hela-s3")){
					datacell = "helas3";
				}
				
	
				String color = map.get(peak_title).toString();
				
				StringBuffer sb= new StringBuffer();
				sb.append("["+cell+"_"+peak_title+"]\n");
				sb.append("glyph_type=circle\n");
				sb.append("storage=GFF3\n");
				sb.append("file=/share/disk1/work/bioinformatics/tangbx/hic/dataprocess/human/"+datacell+"/signal/srt/"+peak+"/stat_physical/{binsize}/{zoom}/{refseq}/density\n");
				sb.append("color="+color+"\n");
				sb.append("line_width=1\n");
				

				sb.append("key="+cell+"_"+peak_title+"\n");
				sb.append("category=ENCODE("+cell.toUpperCase()+")\n\n");
				bw.write(sb.toString());
			
			}
			bw.close();
		}catch(Exception ex){
			
		}

	}
	
	
	/*****************************************************************
	 * generate meta data
	 */
	public static void generateDownloadMetaData(){
		try{
			String infile = "E:\\tangbx\\工作日志\\circosweb\\encode_download_data.csv";
			String file ="E:\\tangbx\\工作日志\\circosweb\\encode_table.txt";
		
			BufferedReader br = new BufferedReader(new FileReader(infile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String line="";
			while((line=br.readLine()) != null){
				String [] array = line.split(",");
				bw.write("<tr id=\""+array[0]+"\"><td>"+array[0]+"</td><td>"+array[1]+"</td><td>"+array[2]+"</td><td>"+array[3]+"</td><td>"+array[4]+"</td><td>"+array[5]+"</td><td>"+array[6]+"</td><td>"+array[7]+"</td><td>"+array[8]+"</td><td>"+array[9]+"</td></tr>\r\n");
			}
			
			br.close();
			bw.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
}
