package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.RunRecord;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.util.PageSupport;

/**
 * 
 * 阴极保护站运行记录_管理
 * 
 */
public interface BaseRunRecordNewDao {
	
	public void updateRunRecord(RunRecord runRecord);
	
	public void deleteRunRecordDetailByRid(@Param(value="r_id") Integer r_id);
	
	public void deleteRunRecordById(@Param(value="id") Integer id);
	
	public RunRecord queryRunRecordById(@Param(value="id") Integer id);
	
	public void insertRunRecord(RunRecord runRecord);
	
	public void insertRunRecordDetail(RunRecordDetail runRecordDetail);
	public void updateRunRecordDetail(RunRecordDetail runRecordDetail);
	
	public List<RunRecord> queryRunRecord(Map<String, Object> param);
	
	public void updateRunRecordVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
	
	public void updateRunRecordTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);
	
	public List<RunRecord> queryRunRecordMerge(Map<String, Object> param);
	
	public RunRecord queryRunRecordMergeInfo(Map<String, Object> param);

	public RunRecord RunRecordByList(@Param("param") Map<String, Object> param);

	public int RunRecordByListTotal(@Param("param") Map<String, Object> param);

	public List<BasePipelineSpec> queryRunRecordAuditSpecList(@Param("param") Map<String, Object> param);
	
	public List<RunRecordDetail> queryRunRecordDetail(Map<String, Object> param, PageSupport ps);
	
	public void updateResetDetailStatus(@Param(value="id") Integer id);
	public int queryDetailStatus(@Param(value="id") Integer id);
	public void updateChangeDetailStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
	public List<RunRecordDetail> queryRunRecordDetail(Map<String, Object> param);	
	
	public void updateAutoSubmitEveryWeek();
	public List<BasePipeline> queryBasePipelineByAdminRc(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminRc(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminRc(Map<String, Object> param);
}
