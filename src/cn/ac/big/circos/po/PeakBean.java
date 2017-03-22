package cn.ac.big.circos.po;

public class PeakBean {
	private int peakId;
	private String chrom;
	private int start;
	private int end;
	private String note;
	private String score;
	private int binsize;
	private int perbin;
	private String source;
	private String type;
	
	
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getBinsize() {
		return binsize;
	}
	public void setBinsize(int binsize) {
		this.binsize = binsize;
	}
	public int getPerbin() {
		return perbin;
	}
	public void setPerbin(int perbin) {
		this.perbin = perbin;
	}
	public int getPeakId() {
		return peakId;
	}
	public void setPeakId(int peakId) {
		this.peakId = peakId;
	}
	public String getChrom() {
		return chrom;
	}
	public void setChrom(String chrom) {
		this.chrom = chrom;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
