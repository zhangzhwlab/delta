package cn.ac.big.circos.util;

/**************************************************
 * this class used to enlarge or deduce the size of genome size
 * @author jacky
 *
 */
public class Idogram {
	private int cstart;
	private int cend;
	private int unit;
	
	
	/****************************************************
	 * this used to zoom in
	 * @return
	 */
	public String zoomin(int istart,int iend,float folder){
		int range = (int) ((iend - istart) / folder);

		if (range / 1000000 >= 10) {
			unit = 1000000;
		} else if (range / 10000 >= 10) {
			unit = 10000;
		} else if (range / 1000 >= 10) {
			unit = 1000;
		}

		cstart = (int) ((iend + istart) / 2 - (iend - istart)
				/ (folder * 2));

		cend = (int) ((iend + istart) / 2 + (iend - istart)
				/ (folder * 2));
		
		return null;
	}
	
	
	
	/***************************************
	 * this used to zoom out
	 * @return
	 */
	public String zoomout(int istart,int iend,float folder){
		int range = (int) ((iend - istart) * folder);

		if (range / 1000000 >= 10) {
			unit = 1000000;
		} else if (range / 10000 >= 10) {
			unit = 10000;
		} else if (range / 1000 >= 10) {
			unit = 1000;
		}

		cstart =  (int) ((istart+iend)/2 - (iend-istart)*folder/2 );
		if(cstart <=0){
			cstart = 1;
		}

		cend = (int) ((istart+iend)/2 + (iend-istart)*folder/2 );
		return null; 
	}



	public int getCstart() {
		return cstart;
	}



	public void setCstart(int cstart) {
		this.cstart = cstart;
	}



	public int getCend() {
		return cend;
	}



	public void setCend(int cend) {
		this.cend = cend;
	}



	public int getUnit() {
		return unit;
	}



	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	
	public static void main(String []args){
		//317606..1588032
		int istart = 317606;
		int iend = 1588032;
		float folder = 1.5f ;
		Idogram idogram = new Idogram();
		/*idogram.zoomin(istart, iend, folder);

		istart = idogram.getCstart();
		iend = idogram.getCend();
		System.out.println("zoom in 1.5f, start=" + istart + ",end=" + iend
				+ ",unit=" + idogram.getUnit());

		idogram.zoomin(istart, iend, folder);

		istart = idogram.getCstart();
		iend = idogram.getCend();
		System.out.println("zoom in 1.5f, start=" + istart + ",end=" + iend
				+ ",unit=" + idogram.getUnit());

		idogram.zoomin(istart, iend, folder);
		istart = idogram.getCstart();
		iend = idogram.getCend();
		System.out.println("zoom in 1.5f, start=" + istart + ",end=" + iend
				+ ",unit=" + idogram.getUnit());*/
		
		
		idogram.zoomout(istart, iend, folder);
		istart = idogram.getCstart();
		iend = idogram.getCend();
		System.out.println("zoom out 1.5f, start="+istart+",end="+iend+",unit="+idogram.getUnit());
		
		
		idogram.zoomout(istart, iend, folder);
		istart = idogram.getCstart();
		iend = idogram.getCend();
		System.out.println("zoom out 1.5f, start="+istart+",end="+iend+",unit="+idogram.getUnit());
	}
	
}
