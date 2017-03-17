/**
 * 
 */
package cn.hm.oil.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BaseResourceDao;
import cn.hm.oil.dao.SysRolesDao;
import cn.hm.oil.dao.SysUsersDao;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseResource;
import cn.hm.oil.domain.Infor;
import cn.hm.oil.domain.SysRoles;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.InforService;
import cn.hm.oil.util.AbstractModuleSuport;

/**
 * @author Administrator
 *
 */
@Service(value="inforService")
public class InforServiceImpl extends AbstractModuleSuport implements InforService {

	@Autowired
	private BasePipeLineDao basePipeLineDao;
	
	@Autowired
	private BaseResourceDao baseResourceDao;
	
	@Autowired
	private SysUsersDao sysUsersDao;
	
	@Autowired
	private SysRolesDao sysRolesDao;
	
	/* (non-Javadoc)
	 * @see cn.hm.oil.service.InforService#execute()
	 */
	@Override
	public void saveData() {
		List<Infor> infs = this.getList("cn.hm.oil.dao.InforDao.queryAllInfor");
		for (Infor infor : infs) {
			BaseResource br = new BaseResource();
			
			String plName = infor.getChanelname();
			BasePipeline basePipeline = basePipeLineDao.queryPipeLineByName(plName);
			Integer pl_id = null;
			if (basePipeline != null) {
				pl_id = basePipeline.getId();
			} else {
				basePipeline = new BasePipeline();
				basePipeline.setName(plName);
				basePipeLineDao.inserPipeLine(basePipeline);
				pl_id = basePipeline.getId();
			}
			
			String sectionName = infor.getDuanluo();
			BasePipelineSection basePipelineSection = basePipeLineDao.querySectionByName(sectionName, pl_id);
			Integer pl_section_id = null;
			if (basePipelineSection != null) {
				pl_section_id = basePipelineSection.getId();
			} else {
				basePipelineSection = new BasePipelineSection();
				basePipelineSection.setName(sectionName);
				basePipelineSection.setPl_id(pl_id);
				basePipeLineDao.inserSection(basePipelineSection);
				pl_section_id = basePipelineSection.getId();
			}
			
			String specName = infor.getChanelmodel();
			BasePipelineSpec basePipelineSpec = basePipeLineDao.querySpecByName(specName, pl_section_id);
			Integer pl_spec_id = null;
			if (basePipelineSpec != null) {
				pl_spec_id = basePipelineSpec.getId();
			} else {
				basePipelineSpec = new BasePipelineSpec();
				basePipelineSpec.setPl_id(pl_id);
				basePipelineSpec.setPl_section_id(pl_section_id);
				basePipelineSpec.setName(specName);
				
				basePipeLineDao.inserSpec(basePipelineSpec);
				pl_spec_id = basePipelineSpec.getId();
			}
			
			String user = infor.getManager();
			String phone = infor.getContact();
			
			SysUsers su = sysUsersDao.queryLoginUserInfoByUsername(phone);
			Integer user_id = null;
			if (su != null) {
				user_id = su.getId();
			} else {
				su = new SysUsers();
				su.setUsername(phone);
				su.setName(user);
				PasswordEncoder pe = new BCryptPasswordEncoder();
				String pwd;
				try {
					pwd = phone.substring(phone.length() - 6);
				} catch (Exception e) {
					e.printStackTrace();
					pwd = phone;
				}
				su.setPassword(pe.encode(pwd));
				
				sysUsersDao.insertWorker(su);
				user_id = su.getId();
				sysRolesDao.insertRoleUser(SysRoles.WORKER, user_id);
			}
			
			
			br.setPl_id(pl_id);
			br.setPl_section_id(pl_section_id);
			br.setPl_spec_id(pl_spec_id);
			
			try {
				br.setMpa(Float.valueOf(infor.getPresure()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				br.setMpa(0f);
			}
			try {
				br.setThroughout(Float.valueOf(infor.getShejishuliang()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				br.setThroughout(0f);
			}
			try {
				br.setLength(Float.valueOf(infor.getChangdu()));
			} catch (NumberFormatException e) {
				br.setLength(0f);
				e.printStackTrace();
			}
			if (!StringUtils.isBlank(infor.getFajin()) && !StringUtils.equals(infor.getFajin(), "无"))
				br.setChamber_well(infor.getFajin());
			if (!StringUtils.isBlank(infor.getFangfuzhan()) && !StringUtils.equals(infor.getFangfuzhan(), "无"))
				br.setFangfu_station(infor.getFangfuzhan());
			
			br.setAdmin(user_id);
			br.setDisable(infor.getCat_id());
			
			baseResourceDao.insertBaseResource(br);
		}
	}

}
