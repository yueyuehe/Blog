package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BaseExcelDao;
import cn.hm.oil.domain.Sexcel;
import cn.hm.oil.service.ExcelService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value = "ExcelService")
public class ExcelServiceImpl extends AbstractModuleSuport implements ExcelService {

	@Autowired
	private BaseExcelDao baseExcelDao;
	
	@Override
	public void saveExcel(Sexcel excel) {
		baseExcelDao.saveExcel(excel);
	}
	
	@Override
	public List<Sexcel> queryExcel(Map<String, Object> param,PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseExcelDao.queryExcel",
				"cn.hm.oil.dao.BaseExcelDao.queryExcel_count", param, ps);
	}
	
	@Override
	public Sexcel queryExcelByid(Integer id){
		return baseExcelDao.queryExcelByid(id);
		
	}
	
	@Override
	public void deleteExcelByid(Integer deleter,Integer id) {
		// TODO Auto-generated method stub
		baseExcelDao.deleteExcelByid(deleter,id);
		
	}

	@Override
	public Sexcel queryExcelCheck(Integer parent, String filetitle) {
		return baseExcelDao.queryExcelCheck(parent,filetitle);
	}
}
