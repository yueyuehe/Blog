package cn.hm.oil.dao;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Sexcel;

public interface BaseExcelDao {
	public void saveExcel(Sexcel excel);
	
	public Sexcel queryExcelByid(Integer id);
	
	public void deleteExcelByid(@Param(value="deleter") Integer deleter,@Param(value="id") Integer id);
	
	public Sexcel queryExcelCheck(@Param(value="parent")Integer parent,@Param(value="filetitle") String filetitle);
}
