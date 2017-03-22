package cn.ac.big.circos.util;

/**********************************************************
 * circos track
 * @author lenovo
 *
 */
public class CircosTrack {
	private int trackid;
	private String name;
	private String key;
	private String category;
	private String feature;
	private String glyph;
	private String storage;
	private String storeclass; // file format of storage file
	private String statisFile; // statis file will be showed by histogram
	private String file; // file
	private String color;
	private String pcolor;
	private String ncolor;
	private String height;
	private String lineWidth;
	private int histoneBin; // each bin contains base pair
	
	
	private String dataset;
	private String organism;
	private String trackFile;
	private int toomany = 0 ; // too many feature, feature large than this get statistics hisgram
	
	private String binsize;
	private String startBin; // this is used to store the start bin index
	private String table;
	
	
		
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getStartBin() {
		return startBin;
	}
	public void setStartBin(String startBin) {
		this.startBin = startBin;
	}
	public int getToomany() {
		return toomany;
	}
	public void setToomany(int toomany) {
		this.toomany = toomany;
	}
	public String getStoreclass() {
		return storeclass;
	}
	public void setStoreclass(String storeclass) {
		this.storeclass = storeclass;
	}
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}
	public int getTrackid() {
		return trackid;
	}
	public void setTrackid(int trackid) {
		this.trackid = trackid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getGlyph() {
		return glyph;
	}
	public void setGlyph(String glyph) {
		this.glyph = glyph;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPcolor() {
		return pcolor;
	}
	public void setPcolor(String pcolor) {
		this.pcolor = pcolor;
	}
	public String getNcolor() {
		return ncolor;
	}
	public void setNcolor(String ncolor) {
		this.ncolor = ncolor;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(String lineWidth) {
		this.lineWidth = lineWidth;
	}
	public String getStatisFile() {
		return statisFile;
	}
	public void setStatisFile(String statisFile) {
		this.statisFile = statisFile;
	}
	public int getHistoneBin() {
		return histoneBin;
	}
	public void setHistoneBin(int histoneBin) {
		this.histoneBin = histoneBin;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBinsize() {
		return binsize;
	}
	public void setBinsize(String binsize) {
		this.binsize = binsize;
	}
	
	
}
