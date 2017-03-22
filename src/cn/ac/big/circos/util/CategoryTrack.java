package cn.ac.big.circos.util;

import java.util.ArrayList;
import java.util.List;

/*************************************
 * this used to store the track belong to the same category
 * @author lenovo
 *
 */
public class CategoryTrack {

	private String name;
	private String organism;
	
	private List<CircosTrack> trackList = new ArrayList<CircosTrack>();
		
	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public CategoryTrack(String name,String organism){
		this.name = name ;
		this.organism = organism;
	}
	
	public void addTrack(CircosTrack track){
		trackList.add(track);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CircosTrack> getTrackList() {
		return trackList;
	}

	public void setTrackList(List<CircosTrack> trackList) {
		this.trackList = trackList;
	}
	
	
}
