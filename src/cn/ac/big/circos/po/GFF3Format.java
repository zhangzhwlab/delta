package cn.ac.big.circos.po;

import java.util.List;
import java.util.Map;

public class GFF3Format {
	private String seq; // chrom
	private String source;
	private String feature;
	private String start;
	private String end;
	private String strand;
	private String phase;
	private String score;
	private String id;
	private String name;

	private Map<String,String[]> attributes;
	
	
	private List<GFF3Format> children ;
	
	

	public List<GFF3Format> getChildren() {
		return children;
	}
	public void setChildren(List<GFF3Format> children) {
		this.children = children;
	}
	public Map<String, String[]> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String[]> attributes) {
		this.attributes = attributes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String cols9;
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCols9() {
		return cols9;
	}
	public void setCols9(String cols9) {
		this.cols9 = cols9;
	}
	
	
}
