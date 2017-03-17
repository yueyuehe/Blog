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
import java.util.Random;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.FacilitiesMaintenance;
import cn.hm.oil.domain.HighConsequence;
import cn.hm.oil.domain.HighConsequenceDetail;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.DrawPic;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * 高后果区管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseHSequelController {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 资料创建(静态)
	 * @param model
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/create", method = RequestMethod.GET)
	public String hSequel_create(Model model, @RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		} else if (StringUtils.equals(status, "2")) {
			model.addAttribute("msg", "无可用信息,请重新填写!");
		}
		Prompt prompt = baseService.quertPromptByType(12);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		return "pages/base/h_sequel_create";
	}
	
	/**
	 * 资料保存
	 * @param model
	 * @param status
	 * @param id
	 * @param pl
	 * @param section
	 * @param spec
	 * @param num
	 * @param start_lat
	 * @param start_lon
	 * @param end_lat
	 * @param end_lon
	 * @param s_start
	 * @param s_end
	 * @param s_length
	 * @param s_soure
	 * @param place_name
	 * @param description
	 * @param u_date
	 * @param recogner
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/save", method = RequestMethod.POST)
	public String hSequel_save(Model model,HttpServletRequest request,
			@RequestParam(required = false) Integer id,@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam Integer[] num,@RequestParam Double[] start_lat,
			@RequestParam Double[] start_lon,@RequestParam Double[] end_lat,
			@RequestParam Double[] end_lon,@RequestParam Integer[] s_start,
			@RequestParam Integer[] s_end,@RequestParam Float[] s_length,
			@RequestParam Integer[] s_soure,@RequestParam String[] place_name,
			@RequestParam String[] description,@RequestParam String[] u_date,@RequestParam String[] recogner) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HighConsequence hc = new HighConsequence();
		hc.setCreater(ud.getId());
		hc.setPl_id(pl);
		hc.setPl_section_id(section);
		hc.setPl_spec_id(spec);
		hc.setId(id);
		List<String> pic1List = null;
		List<String> pic2List = null;
		List<String> pic3List = null;
		List<String> pic4List = null;
		List<String> pic5List = null;
		
		//获取图片1列表
		try {
			pic1List = DataUtil.uploadImg(request, "pic1");
			pic2List = DataUtil.uploadImg(request, "pic2");
			pic3List = DataUtil.uploadImg(request, "pic3");
			pic4List = DataUtil.uploadImg(request, "pic4");
			pic5List = DataUtil.uploadImg(request, "pic5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<HighConsequenceDetail> hcdList = new ArrayList<HighConsequenceDetail>();
		int i = 0;
		for(Integer pn : num) {
			if(pn == null) {
				break;
			}
			HighConsequenceDetail hcd = new HighConsequenceDetail();
			hcd.setNum(num[i]);
			hcd.setStart_lat(start_lat[i]);
			hcd.setEnd_lat(end_lat[i]);
			hcd.setPlace_name(place_name[i]);
			hcd.setStart_lon(start_lon[i]);
			hcd.setEnd_lon(end_lon[i]);
			hcd.setS_start(s_start[i]);
			hcd.setS_end(s_end[i]);
			hcd.setS_length(s_length[i]);
			hcd.setS_soure(s_soure[i]);
			hcd.setDescription(description[i]);
			hcd.setU_date(DateFormatter.stringToDate(u_date[i],
						"yyyy-MM-dd"));
			hcd.setRecogner(recogner[i]);
			hcd.setPic1(pic1List.get(i));
			hcd.setPic2(pic2List.get(i));
			hcd.setPic3(pic3List.get(i));
			hcd.setPic4(pic4List.get(i));
			hcd.setPic5(pic5List.get(i));
			//System.out.println(hcd.getPic1());
			hcdList.add(hcd);
			i++;
		}
		//System.out.println(i);
		String status = "0";
		if (hcdList.size() > 0) {
			baseService.saveHighConse(hc, hcdList);
			status = "1";
		} else if (hcdList.size() == 0) {
			status = "2";
		}
		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		return "redirect:/admin/base/h_sequel/" + page;
	}
	
	/**
	 * 高后果区资料查看
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_time
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String hSequel_list(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time) {
		
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
		if (!StringUtils.isBlank(create_time)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_time", date);
			model.addAttribute("create_time", create_time);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<HighConsequence> hcList = baseService.queryHighConsequence(param, ps);
		//System.out.println(hcList.size());
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("hcList", hcList);
		return "pages/base/h_sequel_list";
	}
	
	@RequestMapping(value = "/h_sequel/show", method = RequestMethod.GET)
	public String hSequel_show(Model model, @RequestParam Integer id, @RequestParam(required = false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		HighConsequence hc = baseService.queryHighConsequenceById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(hc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(hc.getPl_section_id());
		
		model.addAttribute("pl", hc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", hc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", hc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("hc", hc);
		return "pages/base/h_sequel_show";
	}
	
	/**
	 * 高后果区审核列表
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_time
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String hSequel_verify(Model model, HttpServletRequest request, Authentication authentication,@RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String create_time) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		
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
		if (!StringUtils.isBlank(create_time)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_time", date);
			model.addAttribute("create_time", create_time);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<HighConsequence> hcList = baseService.queryHighConsequence(param, ps);
		//System.out.println(hcList.size());
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("hcList", hcList);
		model.addAttribute("verify", 1);
		return "pages/base/h_sequel_list";
	}
	
	/**
	 * 审核查看
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/show_verify", method = RequestMethod.GET)
	public String fMaint_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		HighConsequence hc = baseService.queryHighConsequenceById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(hc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(hc.getPl_section_id());
		
		model.addAttribute("pl", hc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", hc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", hc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("hc", hc);
		model.addAttribute("verify", 1);
		return "pages/base/h_sequel_show";
	}
	
	/**
	 * 高后果区资料审核结果保存
	 * @param model
	 * @param id
	 * @param status
	 * @param verify_desc
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/verify_save", method = RequestMethod.POST)
	public @ResponseBody String h_sequel_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		HighConsequence hig = baseService.queryHighConsequenceById(id);
		baseService.updateHighConsequenceVerifyStatus(id, ud.getId(), status, verify_desc, hig.getCreater());
		
		return JsonUtil.toJson("OK");
	}
	
	/**
	 * 详细信息修改
	 * @param model
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/edit", method = RequestMethod.GET)
	public String hSequel_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		HighConsequence hc = baseService.queryHighConsequenceById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(hc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(hc.getPl_section_id());
		
		model.addAttribute("pl", hc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", hc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", hc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("hc", hc);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		return "pages/base/h_sequel_edit";
	}
	//资料导出
	@RequestMapping(value = "/h_sequel/exp", method = RequestMethod.GET)
	public String hSequel_export(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
		}
		if (!StringUtils.isBlank(create_time) || !StringUtils.equals(create_time, "undefined")) {
			System.out.println(create_time);
			//Date date = null;
			Date date = DateFormatter.stringToDate(create_time,"yyyy-MM-dd");
			param.put("create_time", date);
			model.addAttribute("create_time", create_time);
		}

		List<HighConsequence> hcList = baseService.queryHighConsequence(param, null);
		if (hcList.size() == 0) {
			return "redirect:/admin/base/h_sequel/list";
		}
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.image.path");// 存放文件文件夹名称
		String fileDir1 = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path = fileDir1;
		String excelPath = path + sep + "excel.xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			Random random = new Random();
			//String date = DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd");
			//String excelPath = path;
			dirPath = new File(excelPath);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			String newPath = fileDir1 + sep + "AnnexFile";
			File newFile = new File(newPath);
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
			FileOutputStream fileOut = new FileOutputStream(excelPath);

			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();

			HSSFRow row;
			HSSFCell cell;
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			// 居中
			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 内容字体
			Font datafont = work.createFont();

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			dataStyle.setFont(datafont);

			// 设置列宽度
			sheet1.setColumnWidth(0, 13 * 256);
			sheet1.setColumnWidth(1, 16 * 256);
			sheet1.setColumnWidth(2, 8 * 256);
			sheet1.setColumnWidth(3, 12 * 256);
			sheet1.setColumnWidth(4, 12 * 256);
			sheet1.setColumnWidth(5, 12 * 256);
			sheet1.setColumnWidth(6, 12 * 256);
			sheet1.setColumnWidth(7, 18 * 256);
			sheet1.setColumnWidth(8, 10 * 256);
			sheet1.setColumnWidth(9, 18 * 256);
			sheet1.setColumnWidth(10, 9 * 256);
			sheet1.setColumnWidth(11, 22 * 256);
			sheet1.setColumnWidth(12, 38 * 256);
			sheet1.setColumnWidth(13, 18 * 256);
			sheet1.setColumnWidth(14, 12 * 256);
			sheet1.setColumnWidth(15, 12 * 256);

			int row_index = 0;
			for (HighConsequence pc : hcList) {
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 15));
				
				// 第一行
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints(48);
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue(" ");
				cell.setCellStyle(dataStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				HighConsequence hc = baseService.queryHighConsequenceById(pc.getId());
				// 添加边框线
				Integer sz = hc.getDetailList().size();
				int end_row = 0;
				for (int rown = 0; rown <= sz+1; rown++) {
					row = sheet1.createRow(rown+row_index);
					// row.setHeightInPoints(40);
					for (int celln = 0; celln < 17; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown + row_index;
				}
				// 第二行
				row = sheet1.getRow(row_index);
				row_index++;
				// 设置行高度
				row.setHeightInPoints(48);
				
				cell = row.getCell(0);
				cell.setCellValue("管线名称");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("管段名称 " + " ");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(2);
				cell.setCellValue("高后果区编号 ");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("起点经度 ");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(4);
				cell.setCellValue("起点纬度 ");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(5);
				cell.setCellValue("终点经度 ");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(6);
				cell.setCellValue("终点纬度 ");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(7);
				cell.setCellValue("高后果区起点（m）");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(8);
				cell.setCellValue("高后果区终点（m）");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(9);
				cell.setCellValue("高后果区长度（m）");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(10);
				cell.setCellValue("高后果区得分");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(11);
				cell.setCellValue("地名");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(12);
				cell.setCellValue("高后果区特征描述");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(13);
				cell.setCellValue("识别或更新时间");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(14);
				cell.setCellValue("识别人");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(15);
				cell.setCellValue("现场照片");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 36.75);
				
				cell = row.getCell(3);
				cell.setCellValue("用小数表示，且保留至小数点后4位");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(4);
				cell.setCellValue("用小数表示，且保留至小数点后4位");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(5);
				cell.setCellValue("用小数表示，且保留至小数点后4位");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(6);
				cell.setCellValue("用小数表示，且保留至小数点后4位");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(11);
				cell.setCellValue("XX县XX乡/镇XX村xx社");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(12);
				cell.setCellValue("示例：周边500m内有一座500人的小学……，管道***房地产开发公司正在兴建住宅小区及配套道路，配套道路与管道交叉");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(13);
				cell.setCellValue("yyyy-mm-dd");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				//int index = 3;
				/*
				 * int u = 0; int d = 0;
				 */
				
				CellStyle dataCellStyle = work.createCellStyle();
				//设置日期格式
				short df=work.createDataFormat().getFormat("yyyy-MM-dd");
				dataCellStyle.setDataFormat(df);
				dataCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
				dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				dataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
				dataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
				dataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
				dataCellStyle.setFont(datafont);
				
				for (HighConsequenceDetail pcdd : hc.getDetailList()) {
					row = sheet1.getRow(row_index++);
					// row.setHeightInPoints(24);
					// 管线名称
					cell = row.getCell(0);
					if (hc.getPl_name() != null) {
						cell.setCellValue(hc.getPl_name());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					// 管段名称
					cell = row.getCell(1);
					if (hc.getPl_section_name() != null) {
						cell.setCellValue(hc.getPl_section_name());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					// 高后果区编号
					cell = row.getCell(2);
					if (pcdd.getNum() != null) {
						cell.setCellValue(pcdd.getNum());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					// 起点经度
					cell = row.getCell(3);
					if (pcdd.getStart_lat() != null) {
						cell.setCellValue(pcdd.getStart_lat());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					// 起点纬度
					cell = row.getCell(4);
					if (pcdd.getStart_lon() != null) {
						cell.setCellValue(pcdd.getStart_lon());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 终点经度
					cell = row.getCell(5);
					if (pcdd.getEnd_lat() != null) {
						cell.setCellValue(pcdd.getEnd_lat());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 终点纬度
					cell = row.getCell(6);
					if (pcdd.getEnd_lon() != null) {
						cell.setCellValue(pcdd.getEnd_lon());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 高后果区起点（m）
					cell = row.getCell(7);
					if (pcdd.getS_start() != null) {
						cell.setCellValue(pcdd.getS_start());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 高后果区终点（m）
					cell = row.getCell(8);
					if (pcdd.getS_end() != null) {
						cell.setCellValue(pcdd.getS_end());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 高后果区长度（m）
					cell = row.getCell(9);
					if (pcdd.getS_length() != null) {
						cell.setCellValue(pcdd.getS_length());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 高后果区得分
					cell = row.getCell(10);
					if (pcdd.getS_soure() != null) {
						cell.setCellValue(pcdd.getS_soure());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 地名
					cell = row.getCell(11);
					if (pcdd.getPlace_name() != null) {
						cell.setCellValue(pcdd.getPlace_name());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					// 高后果区特征描述
					cell = row.getCell(12);
					if (pcdd.getDescription() != null) {
						cell.setCellValue(pcdd.getDescription());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					
					// 识别或更新时间
					cell = row.getCell(13);
					if (pcdd.getU_date() != null) {
						cell.setCellValue(pcdd.getU_date());
						cell.setCellStyle(dataCellStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					// 识别人
					cell = row.getCell(14);
					if (pcdd.getRecogner() != null) {
						cell.setCellValue(pcdd.getRecogner());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					List<String> imgPaths = new ArrayList<String>();
					String imgs = "";
					if(!StringUtils.isBlank(pcdd.getPic1())) {
						imgPaths.add(pcdd.getPic1());
					}
					if(!StringUtils.isBlank(pcdd.getPic2())) {
						imgPaths.add(pcdd.getPic2());
					}
					if(!StringUtils.isBlank(pcdd.getPic3())) {
						imgPaths.add(pcdd.getPic3());
					}
					if(!StringUtils.isBlank(pcdd.getPic4())) {
						imgPaths.add(pcdd.getPic4());
					}
					if(!StringUtils.isBlank(pcdd.getPic5())) {
						imgPaths.add(pcdd.getPic5());
					}
					for(String st : imgPaths) {
						String[] paths = StringUtils.split(st, sep);
						int pathIndex = paths.length - 1;
						imgs += paths[pathIndex] + ";";
						//FileOutputStream newFileOut = new FileOutputStream(newFile);
						//System.out.println(newPath+"------------");
						FileUtils.copyFile(fileDir + sep + st,newPath + sep + paths[pathIndex]);
					}
					cell = row.getCell(15);
					cell.setCellValue(imgs);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				
				row_index = end_row + 4;
			}
			// 将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			// 压缩文件夹并下载，下载后删除文件夹
			FileUtils.createZip(response, excelPath,fileDir1 + sep + "AnnexFile", DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
