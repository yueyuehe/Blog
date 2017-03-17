package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.domain.RunRecordcomprehensiveDetail;

public interface BaseRunRecordcomprehensiveDao {
	
	public void updateRunRecordcomprehensive(RunRecordcomprehensive runRecordcomprehensive);
	
	public void deleteRunRecordcomprehensiveDetailByCid(@Param(value="c_id") Integer c_id);
	
	public void deleteRunRecordcomprehensiveById(@Param(value="id") Integer id);
	
	public RunRecordcomprehensive queryRunRecordcomprehensiveById(@Param(value="id") Integer id);
	
	public RunRecordcomprehensiveDetail queryRunRecordcomprehensiveDetailById(@Param(value="id") Integer id);
	
	public void insertRunRecordcomprehensive(RunRecordcomprehensive rec);
	
	public void insertRunRecordcomprehensiveDetail(RunRecordcomprehensiveDetail recd);
	
	public List<RunRecordcomprehensive> queryRunRecordcomprehensive(Map<String, Object> param);
	
	public void updateRunRecordcomprehensiveVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
	
	public void updateRunRecordcomprehensiveTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);

	public RunRecordcomprehensive queryRunRecordcomprehensiveByParam(@Param("param") Map<String, Object> param);

	public int queryRunRecordcomprehensiveByParamTotal(@Param("param") Map<String, Object> param);

	public List<BasePipelineSpec> queryRunRecordcomprehensiveAuditSpecList(@Param("param") Map<String, Object> param);
}
