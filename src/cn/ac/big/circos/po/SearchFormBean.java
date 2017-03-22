package cn.ac.big.circos.po;

public class SearchFormBean {
	private String organism;
	private String chrom;
	private int start;
	private int end;
	
	private String cellline;
	private String histonemark;
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
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
	public String getCellline() {
		return cellline;
	}
	public void setCellline(String cellline) {
		this.cellline = cellline;
	}
	public String getHistonemark() {
		return histonemark;
	}
	public void setHistonemark(String histonemark) {
		this.histonemark = histonemark;
	}
	
	
}
