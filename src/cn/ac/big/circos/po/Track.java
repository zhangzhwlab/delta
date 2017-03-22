package cn.ac.big.circos.po;

/*******************************************************
 * this used to store track information
 * @author jacky
 *
 */
public class Track {
	private String name;
	private String index; // the index of layer
	private int isShow ; // whether show in the page
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
	
}
