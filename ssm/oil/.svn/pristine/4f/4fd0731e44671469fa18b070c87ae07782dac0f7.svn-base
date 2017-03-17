package cn.hm.oil.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.domain.RunRecordcomprehensiveDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValveMaint;
import cn.hm.oil.domain.ValveMaintDetail;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.DrawPic;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * 阀室、阀井维护保养工作记录管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseVMaintController {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 阀室、阀井维护保养工作记录创建
	 * @param model
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/v_maint/create", method = RequestMethod.GET)
	public String VMaintCreate(Model model, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		//填写提示，暂未添加本模块的提示
		Prompt prompt = baseService.quertPromptByType(9);
		model.addAttribute("prompt", prompt);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/v_maint_create";
	}
	
	
	@RequestMapping(value="/v_maint/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		ValveMaint pc = baseService.queryValveMaintById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if(pc.getVerify_time() != null){
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}
	
	@RequestMapping(value="/v_maint/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id,@RequestParam String create_t,@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		baseService.updateValveMaintTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}
	
	/**
	 * 阀室、阀井维护保养工作记录列表查看
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/v_maint/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String VMaintList(Model model, HttpServletRequest request,Authentication authentication,
							 @RequestParam(required = false) Integer pl,
							 @RequestParam(required = false) Integer section,
							 @RequestParam(required = false) Integer spec,
							 @RequestParam(required = false) String check_date,
							 @RequestParam(required = false) Integer del) {

		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		
		
		//规格查询条件
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			//段落 下拉列表
			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			//规格 下拉列表
			List<BasePipelineSpec> specs = baseService.querySpec(section);
			model.addAttribute("specs", specs);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		
	
		///判读用户是否是维护工，维护工只能查看自己的数据
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);

		//规格 显示列表
		List<BasePipelineSpec> specList = baseService.querySpecListNew(param);
		model.addAttribute("specLists", specList);

		//管线 下拉列表
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("pipeLineList", pipeLineList);

		return "pages/base/v_maint_list";
	}
	/*@RequestMapping(value = "/v_maint/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String VMaintList(Model model, @RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			 @RequestParam(required = false) Integer spec, @RequestParam(required = false) String check_date,HttpServletRequest request,
				@RequestParam(required = false) Integer del) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLine();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
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
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ValveMaint> vmList = baseService.queryValveMaint(param, ps);
		model.addAttribute("vmList", vmList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("role", role);
		return "pages/base/v_maint_list";
	}*/
	
	/**
	 * 阀室、阀井维护保养工作记录审核
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/v_maint/verify", method = {RequestMethod.GET,RequestMethod.POST})
	public String VMaintVerify(Model model, HttpServletRequest request,
								Authentication authentication,
								@RequestParam(required = false) Integer pl, 
								@RequestParam(required = false) Integer section,
								@RequestParam(required = false) Integer spec, 
								@RequestParam(required = false) String r_month) {
		//管线 下拉列表
		

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

		List<BasePipelineSpec> specList = baseService.queryValveMaintAuditSpecList(param);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("verify", 1);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("specList", specList);
		
		return "pages/base/v_maint_list";
	}
	/*@RequestMapping(value = "/v_maint/verify", method = {RequestMethod.GET,RequestMethod.POST})
	public String VMaintVerify(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer pl, 
			@RequestParam(required = false) Integer section,
			 @RequestParam(required = false) Integer spec, 
			 @RequestParam(required = false) String check_date) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine();
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Integer role = userService.queryRoleIdByUserId(ud.getId());
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
		param.put("verify", "1");
		param.put("user_id", ud.getId());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ValveMaint> vmList = baseService.queryValveMaint(param, ps);
		model.addAttribute("vmList", vmList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("verify", 1);
		model.addAttribute("role", role);
		return "pages/base/v_maint_list";
	}*/
	
	/**
	 * 阀室、阀井维护保养工作记录保存
	 * @param model
	 * @param pl_id
	 * @param pl_section_id
	 * @param pl_spec_id
	 * @param valve_name
	 * @param check_date
	 * @param particips
	 * @param content
	 * @param question
	 * @return
	 */
	@RequestMapping(value = "/v_maint/save", method = RequestMethod.POST)
	public String VMaintSave(Model model, @RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam(required = false) String valve_name, @RequestParam(required = false) String check_date, @RequestParam(required = false) Integer id,
			@RequestParam(required = false) String particips,@RequestParam String up_id, @RequestParam(required = false) String content, @RequestParam(required = false) String question
			,@RequestParam(required = false) Integer status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ValveMaintDetail vmd = new ValveMaintDetail();
		ValveMaint vm = new ValveMaint();
		vm.setPl_id(pl);
		vm.setPl_section_id(section);
		vm.setPl_spec_id(spec);
		vm.setValve_name(valve_name);
		vm.setCheck_date(DateFormatter.stringToDate(check_date, "yyyy-MM-dd"));
		vm.setCreater(ud.getId());
		vm.setId(id);
		vm.setUp_id(up_id);
		vm.setStatus(status);
		vmd.setContent(content);
		vmd.setParticips(particips);
		vmd.setQuestion(question);
		vmd.setStatus(status);
		
		String status1 = "0";
		baseService.saveValveMaint(vm, vmd);
		status1 = "1";
		String page = "";
		if(id!=null && id.intValue()>0){
			page = "edit?id="+id+"&status=" + status1;
		} else {
			page = "create?status=" + status1;
		}
		
		return "redirect:/admin/base/v_maint/" + page;
	}
	
	/**
	 * 详情
	 * @param model
	 * @param id
	 * @param tips_id
	 * @return
	 */
	@RequestMapping(value = "/v_maint/show")
	public String VMaintSave(Model model,Authentication authentication,
							@RequestParam Integer id, 
							@RequestParam(required = false) String r_month,
							@RequestParam(required = false) Integer verify,
							@RequestParam(required=false) Integer offset,
							@RequestParam(required=false) Integer tips_id) {
		
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("offset", offset);
		param.put("pageSize", 1);

		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", r_month);
			model.addAttribute("r_month", r_month);
		}
		
		CommonsMethod.getDataByRole(authentication, userService, param);
		ValveMaint vm = baseService.queryValveMaintByList(param);
		
		if(vm==null){
			return "pages/base/v_maint_show";
		}
		
		int vpTotal = baseService.queryValveMaintByListTotal(param);
		String d = DateFormatter.dateToString(vm.getCheck_date(), "yyyy-MM-dd");
		vm.setDate_string(d);
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		List<BasePipelineSection> sectionList = baseService.querySection(vm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vm.getPl_section_id());

		//获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		
		model.addAttribute("vm", vm);
		model.addAttribute("total", vpTotal);
		model.addAttribute("offset", offset);
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/v_maint_show";
	}
	/*@RequestMapping(value = "/v_maint/show")
	public String VMaintSave(Model model, 
							 @RequestParam Integer id,
							 @RequestParam(required=false) Integer tips_id) {
		
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		ValveMaint vm = baseService.queryValveMaintById(id);
		String d = DateFormatter.dateToString(vm.getCheck_date(), "yyyy-MM-dd");
		vm.setDate_string(d);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine();
		List<BasePipelineSection> sectionList = baseService.querySection(vm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("vm", vm);
		return "pages/base/v_maint_show";
	}*/
	
	@RequestMapping(value = "/v_maint/show_verify")
	public String VMaintShowVerify(Model model, @RequestParam Integer id) {
		ValveMaint vm = baseService.queryValveMaintById(id);
		String d = DateFormatter.dateToString(vm.getCheck_date(),"yyyy-MM-dd");
		vm.setDate_string(d);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		List<BasePipelineSection> sectionList = baseService.querySection(vm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("vm", vm);
		model.addAttribute("verify", 1);
		return "pages/base/v_maint_show";
	}
	
	@RequestMapping(value="/v_maint/verify_save")
	public @ResponseBody String VerifySave(Model model, @RequestParam Integer status, @RequestParam(required = false) String verify_desc,
			@RequestParam Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ValveMaint vm = new ValveMaint();
		vm.setId(id);
		vm.setVerify_desc(verify_desc);
		vm.setStatus(status);
		vm.setVerifier(ud.getId());
		baseService.saveVMaintVerify(vm);
		return "OK";
	}
	
	/**
	 * 修改
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/v_maint/edit")
	public String VMaintEdit(Model model, @RequestParam Integer id,@RequestParam(required=false) Integer status){
		ValveMaint vm = baseService.queryValveMaintById(id);
		String d = DateFormatter.dateToString(vm.getCheck_date(), "yyyy-MM-dd");
		vm.setDate_string(d);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		List<BasePipelineSection> sectionList = baseService.querySection(vm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(vm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("vm", vm);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		if(status!=null && status==1){
			model.addAttribute("msg", "修改成功");
		}
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/v_maint_edit";
	}
	
	@RequestMapping(value = "/v_maint/exp", method = RequestMethod.GET)
	public String VMaintExport(Model model, @RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			 @RequestParam(required = false) Integer spec, @RequestParam(required = false) String check_date,HttpServletResponse response) {
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

		List<ValveMaint> vmList = baseService.queryValveMaint(param,
				null);
		if (vmList.size() == 0) {
			return "redirect:/admin/base/v_maint/list";
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
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel" + ".xls");

			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFCell cell;
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			
			// 居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);


			datafont.setFontHeightInPoints((short) 12);
			datafont.setFontName("方正仿宋简体");
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			dataStyle.setFont(datafont);

			// 设置列宽度
			sheet1.setColumnWidth(0, 14 * 256);
			sheet1.setColumnWidth(1, 20 * 256);
			sheet1.setColumnWidth(2, 20 * 256);
			sheet1.setColumnWidth(3, 27 * 256);

			int row_index = 0;
			for (ValveMaint vm : vmList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 3));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 1, 3));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+3, row_index+3, 1, 3));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+4, row_index+4, 1, 3));

				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 48.75);
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("阀室、阀井巡检工作记录");
				// 设置单元格格式
				cell.setCellStyle(cellStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 第二行
				row = sheet1.createRow(row_index);
				row_index++;
				// 设置行高度
				row.setHeightInPoints((float) 30.75);
				cell = row.createCell(0);
				cell.setCellValue("阀室名称");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(1);
				cell.setCellValue(vm.getValve_name());
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(2);
				cell.setCellValue("时间");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(3);
				cell.setCellValue(DateFormatter.dateToString(vm.getCheck_date(), "yyyy-MM-dd"));
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				ValveMaint vma = baseService.queryValveMaintById(vm.getId());

				// 第三行
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 41.25);
				cell = row.createCell(0);
				cell.setCellValue("参加人员");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(1);
				cell.setCellValue(vma.getVmd().getParticips());
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(2);
				cell.setCellStyle(dataStyle);

				cell = row.createCell(3);
				cell.setCellStyle(dataStyle);
				// 第四行
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 315.75);
				cell = row.createCell(0);
				cell.setCellValue("工作内容");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(1);
				cell.setCellValue(vma.getVmd().getContent());
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(2);
				cell.setCellStyle(dataStyle);

				cell = row.createCell(3);
				cell.setCellStyle(dataStyle);
				// 第五行
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 315.75);
				cell = row.createCell(0);
				cell.setCellValue("存在问题说明");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(1);
				cell.setCellValue(vma.getVmd().getQuestion());
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(2);
				cell.setCellStyle(dataStyle);

				cell = row.createCell(3);
				cell.setCellStyle(dataStyle);
				
				row_index = row_index + 2;
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
	
	@RequestMapping(value = "/v_maint/del", method = RequestMethod.GET)
	public String v_maint_del(Model model, @RequestParam Integer id) {
		baseService.deleteValveMaintById(id);
		return "redirect:/admin/base/v_maint/list?del=1";
	}
}
