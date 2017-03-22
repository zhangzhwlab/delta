package cn.ac.big.circos.po;

public class PubGeneBean {
	
	private int geneId;
	private String organism;
	private String ensGeneId;
	private String synonym;
	private String chrom;
	private int start;
	private int end;
	private String strand;
	private int tss;
	private int geneCount;
	
	
	
	public int getGeneCount() {
		return geneCount;
	}
	public void setGeneCount(int geneCount) {
		this.geneCount = geneCount;
	}
	public int getGeneId() {
		return geneId;
	}
	public void setGeneId(int geneId) {
		this.geneId = geneId;
	}
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}
	public String getEnsGeneId() {
		return ensGeneId;
	}
	public void setEnsGeneId(String ensGeneId) {
		this.ensGeneId = ensGeneId;
	}
	public String getSynonym() {
		return synonym;
	}
	public void setSynonym(String synonym) {
		this.synonym = synonym;
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
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public int getTss() {
		return tss;
	}
	public void setTss(int tss) {
		this.tss = tss;
	}
	
}
