package cn.ac.big.circos.util;

import java.util.Properties;

public class ParamsUtil {
	public static String WS_URL="http://192.168.118.15:9656/circoswebREST";
	public static String PUBDATA_PATH="/share/disk1/work/bioinformatics/tangbx/hic";
		
	public static void loadProperties(Properties pro){
		if(pro.getProperty("WS.URL") != null){
			WS_URL = pro.getProperty("WS.URL");
		}		
		
		if(pro.getProperty("PUBDATA.PATH") != null){
			PUBDATA_PATH =  pro.getProperty("PUBDATA.PATH");
		}
	}	
	
	
	public static String compuateTime(long l){
		
		long day=l/(24*60*60);
		long hour=(l/(60*60)-day*24);
		long min=((l/60)-day*24*60-hour*60);
		long s=(l-day*24*60*60-hour*60*60-min*60);
				
		String time = day+" "+hour+":"+min+":"+s;
		return time;
	}
}
