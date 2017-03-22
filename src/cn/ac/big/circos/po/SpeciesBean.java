package cn.ac.big.circos.po;
/**********************
 * "length":249250620,"species":"human","name":"1","seqChunkSize":20000,"end":249250620,"start":0
 * @author lenovo
 *
 */
public class SpeciesBean {
	private int length;
	private String species;
	private String name;
	private int seqChunkSize;
	private int start;
	private int end;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeqChunkSize() {
		return seqChunkSize;
	}
	public void setSeqChunkSize(int seqChunkSize) {
		this.seqChunkSize = seqChunkSize;
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
	
}
