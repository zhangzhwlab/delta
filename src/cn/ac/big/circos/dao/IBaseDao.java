package cn.ac.big.circos.dao;

import java.util.List;

public interface IBaseDao {
	public abstract Object findObjectByID(String mappername, int id);
	public abstract Object findObjectByName(String name);
	public abstract Object findObjectByObject(String mappername,Object param);
	public abstract List findResultListByWhereLike(String mappername,
			String param);
	public abstract List findResultList(String mappername, Object param);
	public abstract List findResultList(String mappername, Object param,
			int offset, int limit);
	public abstract int getRecordCount(String mappername, Object param);

	// add by tangbx 20120306
	public abstract int insertObjectToTable(String mappername, Object param);
	public abstract int updateObjectToTable(String mappername, Object param);
	public abstract int deleteObjectFromTable(String mappername, Object param);
}
