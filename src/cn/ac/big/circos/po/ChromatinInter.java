package cn.ac.big.circos.po;

import java.util.List;

//chromatin interaction entity
public class ChromatinInter {

	private int id;
	private String anchorChr;
	private String name;
	private String strand;
	private int start; // anchor start
	private int stop; // target end
	private int anchorEnd;
	private int targetStart;
	private int targetChr;
	
	private List<ChromatinInter> target;// this is used to store target list of given anchor
	
	
	
	
	public int getTargetChr() {
		return targetChr;
	}
	public void setTargetChr(int targetChr) {
		this.targetChr = targetChr;
	}
	public int getAnchorEnd() {
		return anchorEnd;
	}
	public void setAnchorEnd(int anchorEnd) {
		this.anchorEnd = anchorEnd;
	}
	public int getTargetStart() {
		return targetStart;
	}
	public void setTargetStart(int targetStart) {
		this.targetStart = targetStart;
	}
	public List<ChromatinInter> getTarget() {
		return target;
	}
	public void setTarget(List<ChromatinInter> target) {
		this.target = target;
	}
	
	public String getAnchorChr() {
		return anchorChr;
	}
	public void setAnchorChr(String anchorChr) {
		this.anchorChr = anchorChr;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	
	
}
