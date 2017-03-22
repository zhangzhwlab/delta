package cn.ac.big.circos.action;

import cn.ac.big.circos.po.ChromatinInter;
import cn.ac.big.circos.po.PhantomBean;
import cn.ac.big.circos.util.BIGWebServiceClientFactory;
import cn.ac.big.circos.util.ParamsUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

public class ExportAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {
	private static final long serialVersionUID = 1L;

	private HttpServletResponse response;
	private HttpServletRequest request;

	private int anchorStart;
	private int anchorEnd;
	private int viewStart;
	private int viewEnd;
	private String storageFile;
	private String exportURL;
	
	

	private String url;
	private String renderType;
	private String renderSize;
	private String zoomFactor;

	public void execSaveTargetlistToFile() {
		StringBuffer sb = new StringBuffer();

		try {
			// get all the target list of all anchors
			String jsonfile = storageFile + File.separator + "peak.json";
			File file = new File(jsonfile);

			if (file.exists() == true) {
				String line = null;
				sb.append("anchor_chrom\tanchor_start\tanchor_end\ttarget_chrom\ttarget_start\ttarget_end\r\n");
				BufferedReader br = new BufferedReader(new FileReader(jsonfile));
				line = br.readLine();
				if (line != null) {
					JSONArray array = JSONArray.fromObject(line);
					List<ChromatinInter> list = new ArrayList<ChromatinInter>();
					for (Iterator iter = array.iterator(); iter.hasNext();) {
						JSONObject jsonObject = (JSONObject) iter.next();
						ChromatinInter current = (ChromatinInter) JSONObject
								.toBean(jsonObject, ChromatinInter.class);
						List<ChromatinInter> target = new ArrayList<ChromatinInter>();
						for (Object obj : current.getTarget()) {
							ChromatinInter module = (ChromatinInter) JSONObject
									.toBean(JSONObject.fromObject(obj),
											ChromatinInter.class);
							target.add(module);
						}
						current.setTarget(target);
						list.add(current);
					}
					
					
					if (list != null && list.size() > 0) {
						for (ChromatinInter inter : list) {
							if (inter.getStart() == this.anchorStart
									&& inter.getAnchorEnd() == this.anchorEnd) {
								for (ChromatinInter target : inter.getTarget()) {
									// wheter we need to think about plus and
									// minus trand
									if(target.getTargetStart() >= this.viewStart && target.getTargetStart()<= this.viewEnd && target.getStop() >= this.viewStart && target.getStop() <= this.viewEnd){
									}else{
									
										sb.append(target.getAnchorChr()+"\t"+
												this.anchorStart
														+ "\t"
														+ this.anchorEnd
														+ "\t"+target.getAnchorChr()+"\t"
														+ target.getTargetStart()
														+ "\t"
														+ target.getStop())
												.append("\r\n");
									}
									
								}
								break;
							}
						}

					}

				}
				br.close();

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		this.response.reset();
		this.response.setHeader("Content-Disposition",
				"attachment;filename=test.txt");
		this.response.setContentType("application/ms-txt");
		try {
			PrintWriter pr = this.response.getWriter();
			pr.print(sb.toString());
			pr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/****
	 * 
	 * this is used to export the webpage as pdf
	 * we will call a service which located in web server
    */
	public String execExportAsPDFFunc(){
		
		try{
			PhantomBean phtombean = new PhantomBean();
			phtombean.setRenderType(this.renderType);
			phtombean.setRenderSize(this.renderSize);
			phtombean.setUrl(this.url);
			phtombean.setZoomFactor(this.zoomFactor) ;
		
			
			if(phtombean != null){
				
			
				JSONObject input = JSONObject.fromObject(phtombean);
	            String inputstr = input.toString();
	            
	            MultivaluedMap<String, String> param = new MultivaluedMapImpl();
	            param.add("request", inputstr);
				
				Client client = BIGWebServiceClientFactory.getClient();
		        WebResource r = client.resource(ParamsUtil.WS_URL + "/ws/newtask/savepdf");
		        exportURL= r.queryParams(param).type(MediaType.TEXT_PLAIN_TYPE).get(new GenericType<String>() {});
		        
		        int endIndex = request.getRequestURL().length() - request.getPathInfo().length() + 1;
	            String url = request.getRequestURL().substring(0, endIndex);
	            exportURL = url+exportURL;
			}    
			
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return SUCCESS;

	}

	


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getAnchorStart() {
		return anchorStart;
	}

	public void setAnchorStart(int anchorStart) {
		this.anchorStart = anchorStart;
	}

	public int getAnchorEnd() {
		return anchorEnd;
	}

	public void setAnchorEnd(int anchorEnd) {
		this.anchorEnd = anchorEnd;
	}

	public int getViewStart() {
		return viewStart;
	}

	public void setViewStart(int viewStart) {
		this.viewStart = viewStart;
	}

	public int getViewEnd() {
		return viewEnd;
	}

	public void setViewEnd(int viewEnd) {
		this.viewEnd = viewEnd;
	}

	public String getStorageFile() {
		return storageFile;
	}

	public void setStorageFile(String storageFile) {
		this.storageFile = storageFile;
	}

	public String getExportURL() {
		return exportURL;
	}

	public void setExportURL(String exportURL) {
		this.exportURL = exportURL;
	}

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

	public String getRenderSize() {
		return renderSize;
	}

	public void setRenderSize(String renderSize) {
		this.renderSize = renderSize;
	}

	public String getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(String zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	
}