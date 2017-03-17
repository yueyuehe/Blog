package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.domain.ValvePatrolDetail;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * 阀室、阀井巡检工作记录管理
 * 
 * @author Administrator TODO
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseVPatrolController {
	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/v_patrol/create", method = RequestMethod.GET)
	public String VPartorCreate(Model model, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		// 填写提示，暂未添加本模块的提示
		Prompt prompt = baseService.quertPromptByType(8);
		if (prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/v_patrol_create";
	}

	@RequestMapping(value = "/v_patrol/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		ValvePatrol pc = baseService.queryValvePatrolById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if (pc.getVerify_time() != null) {
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}

	@RequestMapping(value = "/v_patrol/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id, @RequestParam String create_t,
			@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		baseService.updateValvePatrolTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * 列表
	 * 
	 * @param model
	 * @param pl
	 * @param section
	 * @param spec
	 * @param check_date
	 * @param request
	 * @param del
	 * @return
	 */
	@RequestMapping(value = "/v_patrol/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String VPartorList(Model model, @RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section, @RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String check_date, HttpServletRequest request,
			Authentication authentication, @RequestParam(required = false) Integer del) {

		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}

		// 规格查询条件
		Map<String, Object> param = new HashMap<String, Object>();

		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			// 段落 下拉列表
			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			// 规格 下拉列表
			List<BasePipelineSpec> specs = baseService.querySpec(section);
			model.addAttribute("specs", specs);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}

		// 判读用户是否是维护工，维护工只能查看自己的数据
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);
		// 管线 下拉列表
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("pipeLineList", pipeLineList);
		// 规格 显示列表
		List<BasePipelineSpec> specList = baseService.querySpecListNew(param);
		model.addAttribute("specList", specList);

		return "pages/base/v_patrol_list";

		/*
		 * SysUsers ud = (SysUsers)
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		 * ; Integer role = userService.queryRoleIdByUserId(ud.getId());
		 * List<BasePipeline> pipeLineList = baseService.queryPipeLine();
		 * Map<String, Object> param = new HashMap<String, Object>(); if (pl !=
		 * null && pl.intValue() > 0) { param.put("pl_id", pl);
		 * model.addAttribute("pl", pl);
		 * 
		 * List<BasePipelineSection> sectionList = baseService.querySection(pl);
		 * model.addAttribute("sectionList", sectionList); } if (section != null
		 * && section.intValue() > 0) { param.put("pl_section_id", section);
		 * model.addAttribute("section", section);
		 * 
		 * List<BasePipelineSpec> specList = baseService.querySpec(section);
		 * model.addAttribute("specList", specList); } if (spec != null &&
		 * spec.intValue() > 0) { param.put("pl_spec_id", spec);
		 * model.addAttribute("spec", spec); } if
		 * (!StringUtils.isBlank(check_date)) { Date date =
		 * DateFormatter.stringToDate(check_date, "yyyy-MM-dd");
		 * param.put("check_date", date); model.addAttribute("check_date",
		 * check_date); } if (del != null && del.intValue() == 1) {
		 * model.addAttribute("msg", "删除成功！"); } PageSupport ps =
		 * PageSupport.initPageSupport(request); List<ValvePatrol> vpList =
		 * baseService.queryValvePatrol(param, ps); model.addAttribute("vpList",
		 * vpList); model.addAttribute("pipeLineList", pipeLineList);
		 * model.addAttribute("role", role); return "pages/base/v_patrol_list";
		 */
	}

	/**
	 * 审核
	 * 
	 * @param model
	 * @param pl
	 * @param section
	 * @param spec
	 * @param check_date
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/v_patrol/verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String VPartorVerify(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String r_month) {

		Map<String, Object> param = new HashMap<String, Object>();

		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			List<BasePipelineSpec> specList = baseService.querySpec(section);
			model.addAttribute("specList", specList);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}

		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", r_month);
			model.addAttribute("r_month", r_month);
		}

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);

		List<BasePipelineSpec> specList = baseService.queryValvePatrolAuditSpecList(param);
		// 管线 下拉列表
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);

		model.addAttribute("verify", 1);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("specList", specList);

		return "pages/base/v_patrol_list";
	}
	/*
	 * @RequestMapping(value = "/v_patrol/verify", method =
	 * {RequestMethod.GET,RequestMethod.POST}) public String VPartorVerify(Model
	 * model, @RequestParam(required = false) Integer pl, @RequestParam(required
	 * = false) Integer section,
	 * 
	 * @RequestParam(required = false) Integer spec, @RequestParam(required =
	 * false) String check_date,HttpServletRequest request) { SysUsers ud =
	 * (SysUsers)
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	 * List<BasePipeline> pipeLineList = baseService.queryPipeLine(); Integer
	 * role = userService.queryRoleIdByUserId(ud.getId()); Map<String, Object>
	 * param = new HashMap<String, Object>(); if (pl != null && pl.intValue() >
	 * 0) { param.put("pl_id", pl); model.addAttribute("pl", pl);
	 * 
	 * List<BasePipelineSection> sectionList = baseService.querySection(pl);
	 * model.addAttribute("sectionList", sectionList); } if (section != null &&
	 * section.intValue() > 0) { param.put("pl_section_id", section);
	 * model.addAttribute("section", section);
	 * 
	 * List<BasePipelineSpec> specList = baseService.querySpec(section);
	 * model.addAttribute("specList", specList); } if (spec != null &&
	 * spec.intValue() > 0) { param.put("pl_spec_id", spec);
	 * model.addAttribute("spec", spec); } if (!StringUtils.isBlank(check_date))
	 * { Date date = DateFormatter.stringToDate(check_date, "yyyy-MM-dd");
	 * param.put("check_date", date); model.addAttribute("check_date",
	 * check_date); } param.put("verify", "1"); param.put("user_id",
	 * ud.getId()); PageSupport ps = PageSupport.initPageSupport(request);
	 * List<ValvePatrol> vpList = baseService.queryValvePatrol(param, ps);
	 * model.addAttribute("vpList", vpList); model.addAttribute("pipeLineList",
	 * pipeLineList); model.addAttribute("verify", 1);
	 * model.addAttribute("role", role); return "pages/base/v_patrol_list"; }
	 */

	/**
	 * 保存阀室、阀井巡检工作记录
	 * 
	 * @param model
	 * @param valve_name
	 * @param check_date
	 * @param id
	 * @param checker
	 * @param pl
	 * @param section
	 * @param spec
	 * @return
	 */
	@RequestMapping(value = "/v_patrol/save")
	public String VPatorSave(Model model, @RequestParam(required = false) Integer normal1,
			@RequestParam(required = false) Integer normal2, @RequestParam(required = false) Integer normal3,
			@RequestParam(required = false) Integer normal4, @RequestParam(required = false) Integer normal5,
			@RequestParam(required = false) Integer normal6, @RequestParam(required = false) Integer normal7,
			@RequestParam(required = false) Integer normal8, @RequestParam(required = false) Integer normal9,
			@RequestParam(required = false) Integer normal10, @RequestParam(required = false) Integer normal11,
			@RequestParam(required = false) Integer normal12, @RequestParam(required = false) Integer normal13,
			@RequestParam(required = false) String handle1, @RequestParam(required = false) String handle2,
			@RequestParam(required = false) String handle3, @RequestParam(required = false) String handle4,
			@RequestParam(required = false) String handle5, @RequestParam(required = false) String handle6,
			@RequestParam(required = false) String handle7, @RequestParam(required = false) String handle8,
			@RequestParam(required = false) String handle9, @RequestParam(required = false) String handle10,
			@RequestParam(required = false) String handle11, @RequestParam(required = false) String handle12,
			@RequestParam(required = false) String handle13, @RequestParam(required = false) String remark1,
			@RequestParam(required = false) String remark9, @RequestParam(required = false) String remark10,
			@RequestParam(required = false) String remark2, @RequestParam(required = false) String remark8,
			@RequestParam(required = false) String remark11, @RequestParam(required = false) String remark3,
			@RequestParam(required = false) String remark7, @RequestParam(required = false) String remark12,
			@RequestParam(required = false) String remark4, @RequestParam(required = false) String remark6,
			@RequestParam(required = false) String remark13, @RequestParam(required = false) String remark5,
			@RequestParam String valve_name, @RequestParam String check_date,
			@RequestParam(required = false) Integer id, @RequestParam String up_id, @RequestParam String checker,
			@RequestParam Integer pl, @RequestParam(required = false) Integer section, @RequestParam Integer spec) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ValvePatrol vp = new ValvePatrol();
		vp.setId(id);
		vp.setCheck_date(DateFormatter.stringToDate(check_date, "yyyy-MM-dd"));
		vp.setChecker(checker);
		vp.setPl_id(pl);
		vp.setPl_section_id(section);
		vp.setPl_spec_id(spec);
		vp.setCreater(ud.getId());
		vp.setValve_name(valve_name);
		vp.setUp_id(up_id);
		// System.out.println(vp.getCheck_date().toString());
		ValvePatrolDetail vpd = new ValvePatrolDetail();
		vpd.setHandle1(handle1);
		vpd.setHandle10(handle10);
		vpd.setHandle11(handle11);
		vpd.setHandle12(handle12);
		vpd.setHandle13(handle13);
		vpd.setHandle2(handle2);
		vpd.setHandle3(handle3);
		vpd.setHandle4(handle4);
		vpd.setHandle5(handle5);
		vpd.setHandle6(handle6);
		vpd.setHandle7(handle7);
		vpd.setHandle8(handle8);
		vpd.setHandle9(handle9);
		vpd.setNormal1(normal1);
		vpd.setNormal2(normal2);
		vpd.setNormal3(normal3);
		vpd.setNormal4(normal4);
		vpd.setNormal5(normal5);
		vpd.setNormal6(normal6);
		vpd.setNormal7(normal7);
		vpd.setNormal8(normal8);
		vpd.setNormal9(normal9);
		vpd.setNormal10(normal10);
		vpd.setNormal11(normal11);
		vpd.setNormal12(normal12);
		vpd.setNormal13(normal13);
		vpd.setRemark1(remark1);
		vpd.setRemark2(remark2);
		vpd.setRemark3(remark3);
		vpd.setRemark4(remark4);
		vpd.setRemark5(remark5);
		vpd.setRemark6(remark6);
		vpd.setRemark7(remark7);
		vpd.setRemark8(remark8);
		vpd.setRemark9(remark9);
		vpd.setRemark10(remark10);
		vpd.setRemark11(remark11);
		vpd.setRemark12(remark12);
		vpd.setRemark13(remark13);
		String status = "0";
		baseService.saveVPatrol(vp, vpd);
		status = "1";
		String page = "";
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		} else {
			page = "create?status=" + status;
		}
		return "redirect:/admin/base/v_patrol/" + page;
	}

	/**
	 * 阀室、阀井巡检工作记录详细
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/v_patrol/show")
	public String VPatrolShow(Model model, Authentication authentication, @RequestParam Integer id,
			@RequestParam(required = false) String r_month, @RequestParam(required = false) Integer verify,
			@RequestParam Integer offset, @RequestParam(required = false) Integer tips_id) {

		if (tips_id != null && tips_id.intValue() > 0) {
			baseService.deleteTips(tips_id);
		}

		/**
		 * 将pagesize定成1
		 */

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("offset", offset);
		param.put("pageSize", 1);

		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", r_month);
			model.addAttribute("r_month", r_month);
		}
		CommonsMethod.getDataByRole(authentication, userService, param);
		/* ValvePatrol vp = baseService.queryValvePatrolById(id); */
		ValvePatrol vp = baseService.queryValvePatrolByList(param);

		if (vp == null) {
			return "pages/base/v_patrol_show";
		}

		int vpTotal = baseService.queryValvePatrolByListTotal(param);
		String d = DateFormatter.dateToString(vp.getCheck_date(), "yyyy-MM-dd");
		vp.setDate_string(d);

		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		List<BasePipelineSection> sectionList = baseService.querySection(vp.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vp.getPl_section_id());

		// 获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);

		model.addAttribute("vp", vp);
		model.addAttribute("total", vpTotal);
		model.addAttribute("offset", offset);
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);

		return "pages/base/v_patrol_show";
	}

	@RequestMapping(value = "/v_patrol/show_verify")
	public String VPatrolShowVerify(Model model, @RequestParam Integer id) {
		ValvePatrol vp = baseService.queryValvePatrolById(id);
		String d = DateFormatter.dateToString(vp.getCheck_date(), "yyyy-MM-dd");
		vp.setDate_string(d);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());
		List<BasePipelineSection> sectionList = baseService.querySection(vp.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vp.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("vp", vp);
		model.addAttribute("verify", 1);
		return "pages/base/v_patrol_show";
	}

	@RequestMapping(value = "/v_patrol/verify_save", method = RequestMethod.POST)
	public @ResponseBody String VPatrolSaveVerify(Model model, @RequestParam(required = false) String verify_desc,
			@RequestParam Integer status, @RequestParam Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ValvePatrol vp = new ValvePatrol();
		vp.setId(id);
		vp.setStatus(status);
		vp.setVerify_desc(verify_desc);
		vp.setVerifier(ud.getId());
		baseService.saveValveVerify(vp);
		return "Ok";
	}

	@RequestMapping(value = "/v_patrol/edit")
	public String VPatroledit(Model model, @RequestParam Integer id, @RequestParam(required = false) Integer status) {
		ValvePatrol vp = baseService.queryValvePatrolById(id);
		String d = DateFormatter.dateToString(vp.getCheck_date(), "yyyy-MM-dd");
		vp.setDate_string(d);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());
		List<BasePipelineSection> sectionList = baseService.querySection(vp.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vp.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("vp", vp);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		if (status != null && status == 1) {
			model.addAttribute("msg", "修改成功");
		}
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/v_patrol_edit";
	}

	/**
	 * 导出
	 * 
	 * @param model
	 * @param pl
	 * @param section
	 * @param spec
	 * @param check_date
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/v_patrol/exp", method = RequestMethod.GET)
	public String VPatrolExport(Model model, @RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section, @RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String check_date, HttpServletResponse response) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			List<BasePipelineSpec> specList = baseService.querySpec(section);
			model.addAttribute("specList", specList);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		if (!StringUtils.isBlank(check_date)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(check_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			param.put("check_date", date);
			model.addAttribute("check_date", check_date);
		}

		List<ValvePatrol> vpList = baseService.queryValvePatrol(param, null);
		if (vpList.size() == 0) {
			return "redirect:/admin/base/v_patrol/list";
		}
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path = fileDir;
		String excelPath = path + sep + "excel" + ".xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(path + sep + "excel" + ".xls");

			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFCell cell;
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			CellStyle dataStyle1 = work.createCellStyle();
			CellStyle dataStyle2 = work.createCellStyle();
			CellStyle dataStyle3 = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			dataStyle2.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle2.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle2.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle2.setBorderTop(CellStyle.BORDER_THIN);
			dataStyle3.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle3.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle3.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle3.setBorderTop(CellStyle.BORDER_THIN);

			// 居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);
			dataStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle1.setWrapText(true);
			dataStyle3.setAlignment(CellStyle.ALIGN_LEFT);
			dataStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle3.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 16);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			dataStyle.setFont(datafont);
			dataStyle1.setFont(datafont);
			dataStyle2.setFont(datafont);
			// 设置列宽度
			sheet1.setColumnWidth(0, 9 * 256);
			sheet1.setColumnWidth(1, 7 * 256);
			sheet1.setColumnWidth(2, 42 * 256);
			sheet1.setColumnWidth(3, 15 * 256);
			sheet1.setColumnWidth(4, 34 * 256);
			sheet1.setColumnWidth(5, 24 * 256);

			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
			int row_index = 0;
			for (ValvePatrol vp : vpList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 5));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 2, row_index + 3, 0, 0));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 2, row_index + 3, 1, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 2, row_index + 2, 3, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 4, row_index + 11, 0, 0));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 4, row_index + 6, 1, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 7, row_index + 11, 1, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 12, row_index + 14, 0, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 15, row_index + 16, 0, 1));

				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 25.25);
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("阀室、阀井巡检工作记录");
				// 设置单元格格式
				cell.setCellStyle(cellStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 25.75);

				cell = row.createCell(0);
				cell.setCellValue("阀室名称");
				cell.setCellStyle(dataStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(1);
				cell.setCellValue(vp.getValve_name());
				cell.setCellStyle(dataStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(2);
				cell.setCellValue("检查日期：");
				cell.setCellStyle(dataStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(3);
				cell.setCellValue(DateFormatter.dateToString(vp.getCheck_date(), "yyyy-MM-dd"));
				cell.setCellStyle(dataStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(4);
				cell.setCellValue("检查人");
				cell.setCellStyle(dataStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(5);
				cell.setCellValue(vp.getChecker());
				cell.setCellStyle(dataStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				int end_row = 0;
				for (int r = 0; r < 15; r++) {
					row = sheet1.createRow(r + row_index);
					for (int c = 0; c < 6; c++) {
						cell = row.createCell(c);
						cell.setCellStyle(dataStyle);
					}
					end_row = r + end_row;
				}

				ValvePatrol vpa = baseService.queryValvePatrolById(vp.getId());

				row = sheet1.getRow(row_index);

				row.setHeightInPoints((float) 16.50);
				cell = row.getCell(0);
				cell.setCellValue(" \r\t    \r\t  项目 \n 类别　　　　");
				cell.setCellStyle(dataStyle3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 画线(由左上到右下的斜线)
				HSSFClientAnchor a = new HSSFClientAnchor(2, 2, 1023, 255, (short) 0, row_index, (short) 0,
						row_index + 1);
				HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
				shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
				shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
				row_index++;
				cell = row.getCell(1);
				cell.setCellValue("检查项目");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("检查结果");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(5);
				cell.setCellValue("备注");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 16.50);

				cell = row.getCell(3);
				cell.setCellValue("正常（完好）");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("异常及处理情况");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(0);
				cell.setCellValue("阀室");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("设备\r\n检查");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("阀室流程检查");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal1() != null && vpa.getVpd().getNormal1() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle1());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark1());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);

				cell = row.getCell(2);
				cell.setCellValue("压力表、执行机构引压开关开启");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal2() != null && vpa.getVpd().getNormal2() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle2());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark2());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);

				cell = row.getCell(2);
				cell.setCellValue("设备验漏");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal3() != null && vpa.getVpd().getNormal3() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle3());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark3());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);

				cell = row.getCell(1);
				cell.setCellValue("基础设施检查");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("阀室上墙资料是否齐全");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal4() != null && vpa.getVpd().getNormal4() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle4());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark4());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("阀室是否通风，阀室内是否有沉降");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal5() != null && vpa.getVpd().getNormal5() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle5());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark5());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("工用具、材料配置齐备，灭火器配置并有效");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal6() != null && vpa.getVpd().getNormal6() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle6());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark6());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("外墙标识、标语完好，门窗是否完好");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal7() != null && vpa.getVpd().getNormal7() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle7());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark7());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("阀室、放空区清洁卫生");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal8() != null && vpa.getVpd().getNormal8() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle8());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark8());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(0);
				cell.setCellValue("放空区");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("拉锁是否紧固");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				if (vpa.getVpd().getNormal9() != null && vpa.getVpd().getNormal9() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle9());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark9());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("阀室、放空区、阀室至放空区管线是否标识齐全，周边建构筑物是否满足安全距离");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(3);
				if (vpa.getVpd().getNormal10() != null && vpa.getVpd().getNormal10() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle10());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark10());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("放空区除草、清洁");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(3);
				if (vpa.getVpd().getNormal11() != null && vpa.getVpd().getNormal11() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle11());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark11());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(0);
				cell.setCellValue("阀井");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("标志桩、盖板是否完好");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(3);
				if (vpa.getVpd().getNormal12() != null && vpa.getVpd().getNormal12() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle12());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark12());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 24.75);
				cell = row.getCell(2);
				cell.setCellValue("泄漏及锈蚀检查");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(3);
				if (vpa.getVpd().getNormal13() != null && vpa.getVpd().getNormal13() == 1) {
					cell.setCellValue("√");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				cell = row.getCell(4);
				cell.setCellValue(vpa.getVpd().getHandle13());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell = row.getCell(5);
				cell.setCellValue(vpa.getVpd().getRemark13());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row_index = row_index + 3;
			}
			// 将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			// 压缩文件夹并下载，下载后删除文件夹

			FileUtils.createZip(response, excelPath, DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/v_patrol/del", method = RequestMethod.GET)
	public String v_patrol_del(Model model, @RequestParam Integer id) {
		baseService.deleteValvePatrolById(id);
		return "redirect:/admin/base/v_patrol/list?del=1";
	}

}
