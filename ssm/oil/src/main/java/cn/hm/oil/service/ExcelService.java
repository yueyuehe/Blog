package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.Sexcel;
import cn.hm.oil.util.PageSupport;

public interface ExcelService {
	public void saveExcel(Sexcel excel);
	
	public List<Sexcel> queryExcel(Map<String, Object> param,PageSupport ps); 
	
	public Sexcel queryExcelByid(Integer id);
	
	public void deleteExcelByid(Integer deleter,Integer id);
	
	public Sexcel queryExcelCheck(Integer parent,String filetitle);
}
