package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.RunRecord;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.util.PageSupport;

/**
 * 
 * 阴极保护站运行记录_管理
 * 
 */
public interface BaseRunRecordDao {
	
	public void updateRunRecord(RunRecord runRecord);
	
	public void deleteRunRecordDetailByRid(@Param(value="r_id") Integer r_id);
	
	public void deleteRunRecordById(@Param(value="id") Integer id);
	
	public RunRecord queryRunRecordById(@Param(value="id") Integer id);
	
	public void insertRunRecord(RunRecord runRecord);
	
	public void insertRunRecordDetail(RunRecordDetail runRecordDetail);
	
	public List<RunRecord> queryRunRecord(Map<String, Object> param);
	
	public void updateRunRecordVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
}
