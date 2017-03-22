package cn.ac.big.circos.po;
/***************************************
 * this is a physical model bean
 * @author lenovo
 *
 */
public class PhysicalModelBean {
	private int modelId;
	private String modelName;
	private String binSize;
	private String glyphType;
	private String storage;
	private int startBin;
	private String file;
	private String key;
	private String category;
	private String tadTrack;
	private String interactionTrack;
	private String species;
	
	
	
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public int getModelId() {
		return modelId;
	}
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getBinSize() {
		return binSize;
	}
	public void setBinSize(String binSize) {
		this.binSize = binSize;
	}
	public String getTadTrack() {
		return tadTrack;
	}
	public void setTadTrack(String tadTrack) {
		this.tadTrack = tadTrack;
	}
	public String getInteractionTrack() {
		return interactionTrack;
	}
	public void setInteractionTrack(String interactionTrack) {
		this.interactionTrack = interactionTrack;
	}
	public String getGlyphType() {
		return glyphType;
	}
	public void setGlyphType(String glyphType) {
		this.glyphType = glyphType;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public int getStartBin() {
		return startBin;
	}
	public void setStartBin(int startBin) {
		this.startBin = startBin;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
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
}
