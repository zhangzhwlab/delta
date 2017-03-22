package cn.ac.big.circos.po;

/********************************************************
 * this is used to store phantomjs paramters
{url:"http://www.jonathantneal.com/examples/invoice/",renderType:"pdf"}
 * 
 * @author lenovo
 *
 */

public class PhantomBean {
	private String url;
	private String renderType;
	private String zoomFactor; // zoom factor
	private String renderSize; // for png 700px*600px
	private String outname; // the name of output file
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRenderType() {
		return renderType;
	}
	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}
	public String getZoomFactor() {
		return zoomFactor;
	}
	public void setZoomFactor(String zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	public String getRenderSize() {
		return renderSize;
	}
	public void setRenderSize(String renderSize) {
		this.renderSize = renderSize;
	}
	public String getOutname() {
		return outname;
	}
	public void setOutname(String outname) {
		this.outname = outname;
	}
	
}
