package cn.ac.big.circos.service.impl;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cn.ac.big.circos.dao.IBaseDao;
import cn.ac.big.circos.service.IBaseService;


public class BaseServiceImpl implements IBaseService{
	private IBaseDao baseDao;
	
	
	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public Object findObjectByID(String mappername, int id) {
		// TODO Auto-generated method stub
		return baseDao.findObjectByID(mappername, id);
	}

	@Override
	public Object findObjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findObjectByObject(String mappername, Object param) {
		// TODO Auto-generated method stub
		return baseDao.findObjectByObject(mappername, param);
	}

	@Override
	public List findResultListByWhereLike(String mappername, String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findResultList(String mappername, Object param) {
		// TODO Auto-generated method stub
		return baseDao.findResultList(mappername, param);
	}

	@Override
	public List findResultList(String mappername, Object param, int offset,
			int limit) {
		// TODO Auto-generated method stub
		return baseDao.findResultList(mappername, param, offset, limit);
	}

	@Override
	public int getRecordCount(String mappername, Object param) {
		// TODO Auto-generated method stub
		return baseDao.getRecordCount(mappername, param);
	}

	@Override
	public int insertObjectToTable(String mappername, Object param) {
		// TODO Auto-generated method stub
		return baseDao.insertObjectToTable(mappername, param);
	}

	@Override
	public int updateObjectToTable(String mappername, Object param) {
		// TODO Auto-generated method stub		
		return baseDao.updateObjectToTable(mappername, param);
	}

	@Override
	public int deleteObjectFromTable(String mappername, Object param) {
		// TODO Auto-generated method stub
		return baseDao.deleteObjectFromTable(mappername, param);
	}

	

}
