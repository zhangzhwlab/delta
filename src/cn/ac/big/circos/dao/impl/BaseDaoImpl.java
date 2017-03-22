package cn.ac.big.circos.dao.impl;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.ac.big.circos.dao.IBaseDao;

public class BaseDaoImpl extends SqlSessionDaoSupport implements IBaseDao{
	private SqlSessionFactory sessionFactory;
	
		
	public void setSessionFactory(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	@Override
	public Object findObjectByID(String mappername, int id) {
		// TODO Auto-generated method stub
		return getSqlSessionTemplate().selectOne(mappername, new Integer(id));
	}

	@Override
	public Object findObjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findObjectByObject(String mappername, Object param) {
		// TODO Auto-generated method stub
		return getSqlSessionTemplate().selectOne(mappername, param);
	}

	@Override
	public List findResultListByWhereLike(String mappername, String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findResultList(String mappername, Object param) {
		// TODO Auto-generated method stub
		
		return getSqlSessionTemplate().selectList(mappername, param);
	}

	@Override
	public List findResultList(String mappername, Object param, int offset,
			int limit) {
		// TODO Auto-generated method stub
		return getSqlSessionTemplate().selectList(mappername,param,new RowBounds(offset,limit));
	}

	@Override
	public int getRecordCount(String mappername, Object param) {
		// TODO Auto-generated method stub
		return Integer.parseInt(getSqlSessionTemplate().selectOne(mappername,param).toString());
	}

	/********************************************************************
	 * insert object to table
	 */
	@Override
	public int insertObjectToTable(String mappername, Object param) {
		// TODO Auto-generated method stub
	    int res= getSqlSessionTemplate().insert(mappername, param);
		return res;
	}

	@Override
	public int updateObjectToTable(String mappername, Object param) {
		// TODO Auto-generated method stub	
		return getSqlSessionTemplate().update(mappername, param);
	}

	@Override
	public int deleteObjectFromTable(String mappername, Object param) {
		// TODO Auto-generated method stub
		return getSqlSessionTemplate().delete(mappername, param);
	}
	
}
