package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
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
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;


/**
 * @author Administrator
 * 
 * 管道巡检工作记录_管理
 * 
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BasePlPatrolController {
	
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 管道巡检工作记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/create", method = RequestMethod.GET)
	public String plPatrol_create(Model model, @RequestParam(required=false) String status){
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		
		model.addAttribute("pipeLineList", pipeLineList);
		Prompt prompt = baseService.quertPromptByType(7);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		return "pages/base/pl_patrol_create";
	}
	
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param p_month
	 * @param del
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String plPatrol_list(Model model, HttpServletRequest request,Authentication authentication, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String p_month, @RequestParam(required=false) Integer del) {
		
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("p_month", p_month);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PipelinePatrol> ppList = baseService.queryPipelinePatrol(param, ps);
		
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role =CommonsMethod.getDataByRole(authentication, userService, param);
		// 规格 显示列表
		List<BasePipelineSpec> specList = baseService.querySpecListNew(param);
		model.addAttribute("specList", specList);
		
		model.addAttribute("role", role);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("ppList", ppList);
		return "pages/base/pl_patrol_list";
	}
	
	/**
	 * 管道巡检工作记录保存
	 * 
	 */
	@RequestMapping(value = "/pl_patrol/save", method = RequestMethod.POST)
	public String plPatrol_save(Model model, HttpServletRequest request,@RequestParam(required = false) Integer id,@RequestParam(required=false) Integer[] pld_id,
			@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,@RequestParam(required=false) Integer[] has_annex_id, @RequestParam(required=false) String[] has_annex,
			@RequestParam String jinzhan, @RequestParam String p_month, @RequestParam String[] p_date, @RequestParam String[] work_place, @RequestParam String[] content,
			@RequestParam String[] question, @RequestParam String[] voice_record, @RequestParam String[] worker,
			@RequestParam String[] auditor, @RequestParam String save_jinzhan){
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		PipelinePatrol pp = new PipelinePatrol();
		pp.setJinzhan(jinzhan);
		pp.setP_month(Integer.valueOf(p_month.replace("-", "")));
		pp.setPl_id(pl);
		pp.setPl_section_id(section);
		pp.setPl_spec_id(spec);
		pp.setSave_jinzhan(save_jinzhan);
		pp.setCreater(ud.getId());
		pp.setId(id);
		List<String> imgs = null;
		try {
			imgs = DataUtil.uploadImg(request, "annex");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("msg", "图片上传错误，请检查图片是否正确！");
			return "pages/base/pl_patrol_create";
		}
		
		Map<Integer, String> annexMap = new HashMap<Integer, String>();
		if(has_annex_id != null && has_annex_id.length > 0) {
			int i = 0;
			for(Integer in : has_annex_id) {
				if(in.intValue() > 0) {
					annexMap.put(in, has_annex[i]);
				}
				i++;
			}
		}
		
		List<PipelinePatrolDetail> ppdList = new ArrayList<PipelinePatrolDetail>();
		int i = 0;
		for (String w : work_place) {
			PipelinePatrolDetail ppd = new PipelinePatrolDetail();
			if(!StringUtils.isBlank(w)){
				ppd.setWork_place(w);
				ppd.setAuditor(auditor[i]);
				ppd.setContent(content[i]);
				ppd.setP_date(DateFormatter.stringToDate(p_date[i], "yyyy-MM-dd"));
				ppd.setQuestion(question[i]);
				ppd.setWorker(worker[i]);
				ppd.setVoice_record(voice_record[i]);
				if(pld_id != null) {
					ppd.setAnnex(annexMap.get(pld_id[i]));
				}
				if(!StringUtils.isBlank(imgs.get(i))) {
					ppd.setAnnex(imgs.get(i));
				}
				ppdList.add(ppd);
			}
			i++;
		}
		String status = "0";
		if (ppdList.size() > 0) {
			baseService.savePlPatrol(pp, ppdList);
			status = "1";
		}

		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		return "redirect:/admin/base/pl_patrol/"+ page;
	}
	
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/show", method = RequestMethod.GET)
	public String plPatrol_show(Model model, 
			@RequestParam Integer id, 
			@RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PipelinePatrol pp = baseService.queryPipelinePatrolById(id);
		if(pp == null){
			return "pages/base/pl_patrol_show";
		}
		List<BasePipelineSection> sectionList = baseService.querySection(pp.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pp
				.getPl_section_id());

		model.addAttribute("pl", pp.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pp.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pp.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pp", pp);
		return "pages/base/pl_patrol_show";
	}
	
	/**
	 * 管道巡检工作记录删除
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/del", method = RequestMethod.GET)
	public String plPatrol_del(Model model, @RequestParam Integer id) {
		System.out.println(id);
		baseService.deletePipelinePatrolById(id);
		return "redirect:/admin/base/pl_patrol/list?del=1";
	}
	
	/**
	 * 管道巡检工作记录导出
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param pl
	 * @param section
	 * @param spec
	 * @param p_month
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/exp", method = RequestMethod.GET)
	public String plPatrol_export(Model model, 
			 HttpServletRequest request, 
			 HttpServletResponse response,
			 @RequestParam(required=false) Integer pl,
			 @RequestParam(required=false) Integer section, 
			 @RequestParam(required=false) Integer spec,
			 @RequestParam(required=false) String p_month) {
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
		}
		
		List<PipelinePatrol> ppList = baseService.queryPipelinePatrol(param, null);
		if(ppList.size()==0){
			return "redirect:/admin/base/pl_patrol/list";
		}
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path=fileDir;
		String excelPath = path + sep + "excel" + ".xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try{
			HSSFWorkbook work = new HSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel" + ".xls");
			/* pp.getPl_name() + "_" + pp.getPl_section_name()
				+ pp.getP_month() + "_" + pp.getJinzhan()*/
			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFCell cell;
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			//内容格式1
			CellStyle dataStyle1 = work.createCellStyle();
			//井站格式
			CellStyle titleStyle1 = work.createCellStyle();
			//年月格式
			CellStyle titleStyle2 = work.createCellStyle();
			
			//内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			//dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			//dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			//dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			//dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle1.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle1.setBorderTop(CellStyle.BORDER_THIN);
			
			//居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);
			
			//顶格
			dataStyle1.setAlignment(CellStyle.ALIGN_LEFT);
			dataStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle1.setWrapText(true);
			titleStyle1.setAlignment(CellStyle.ALIGN_LEFT);
			titleStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle1.setWrapText(true);
			titleStyle2.setAlignment(CellStyle.ALIGN_RIGHT);
			titleStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle2.setWrapText(true);
			
			// 标题字体
			Font font = work.createFont();
			// 表头字体
			Font titlefont = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			titlefont.setFontHeightInPoints((short) 10);
			titlefont.setFontName("方正仿宋简体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			//datafont.setColor(HSSFColor.RED.index);

			//把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);
			dataStyle1.setFont(datafont);
			titleStyle1.setFont(titlefont);
			titleStyle2.setFont(titlefont);
			
			//设置列宽度
			sheet1.setColumnWidth(0, 11*256);
			sheet1.setColumnWidth(1, 18*256);
			sheet1.setColumnWidth(2, 18*256);
			sheet1.setColumnWidth(3, 45*256);
			sheet1.setColumnWidth(4, 12*256);
			sheet1.setColumnWidth(5, 12*256);
			sheet1.setColumnWidth(6, 12*256);
			
			int row_index = 0;
			for(PipelinePatrol pp:ppList) {
				//合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index,row_index,0,6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,2,4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,5,6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,0,1));

				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float)26.25);
				//新建单元格
				cell = row.createCell(0);
				//设置内容
				cell.setCellValue("管   线   巡   检   工   作   记   录");
				//设置单元格格式
				cell.setCellStyle(cellStyle);
				//设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float)30.75);
				cell = row.createCell(0);
				cell.setCellValue("井(站) " + pp.getJinzhan());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(2);
				cell.setCellValue("管线名称及规格: " + pp.getPl_name() + "/"
						+ pp.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(5);
				cell.setCellValue(pp.getP_month().toString().substring(0, 4)
						+ "年" + pp.getP_month().toString().substring(4, 6) + "月");
				cell.setCellStyle(titleStyle2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				//查询数据库
				PipelinePatrol ppl = baseService.queryPipelinePatrolById(pp.getId());
				
				Integer sz = 13;
				if(ppl.getDetailList().size() > 13) {
					sz = ppl.getDetailList().size();
				}
				
				//添加边框线
				int end_row = 0;
				for(int rown=0 ; rown < sz + 1 ; rown++){
					row = sheet1.createRow(rown+row_index);
					for(int celln = 0 ; celln < 7 ; celln++){
						cell=row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				sheet1.addMergedRegion(new CellRangeAddress(sz+row_index+1,sz+row_index+1,0,2));
				
				row = sheet1.createRow(sz+row_index+1);
				//row_index++;
				row.setHeightInPoints(30);
				cell = row.createCell(0);
				cell.setCellValue("保存井(站): " + pp.getSave_jinzhan());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(12);
				cell = row.getCell(0);
				cell.setCellValue("日期");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(1);
				cell.setCellValue("工作地段(点)");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(2);
				cell.setCellValue("工作内容");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(3);
				cell.setCellValue("发现问题及处理情况");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(4);
				cell.setCellValue("工作人");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(5);
				cell.setCellValue("审核");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(6);
				cell.setCellValue("附件");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			
				int count = 0;
				//int index = row_index;
				for (PipelinePatrolDetail ppd : ppl.getDetailList()) {
					row = sheet1.getRow(row_index++);
					cell = row.getCell(0);
					CellStyle dataCellStyle = work.createCellStyle();
					//设置日期格式
					short df=work.createDataFormat().getFormat("dd");
					dataCellStyle.setDataFormat(df);
					dataCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
					dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					dataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
					dataCellStyle.setBorderTop(CellStyle.BORDER_THIN);
					dataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
					dataCellStyle.setFont(datafont);
					if(ppd.getP_date()!=null) {
						cell.setCellValue(DateFormatter.dateToString(ppd.getP_date(), "yyyy-MM-dd"));
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(dataCellStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					//填写工作地点
					cell = row.getCell(1);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getWork_place());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					//填写工作内容
					cell = row.getCell(2);
					cell.setCellValue(ppd.getContent());
					cell.setCellStyle(dataStyle1);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					//填写问题
					cell = row.getCell(3);
					cell.setCellValue(ppd.getQuestion());
					cell.setCellStyle(dataStyle1);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					//填写工作人
					cell = row.getCell(4);
					cell.setCellValue(ppd.getWorker());
					cell.setCellStyle(dataStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.createCell(5);
					cell.setCellValue(ppd.getAuditor());
					cell.setCellStyle(dataStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if(!StringUtils.isBlank(ppd.getAnnex())) {
						String[] paths = StringUtils.split(ppd.getAnnex(), sep);
						//System.out.println(paths.length);
						
						int pathIndex = paths.length - 1;
						String newPath = path + sep + "AnnexFile";
						File newFile = new File(newPath);
						if (!newFile.exists()) {
							newFile.mkdirs();
						}
						//FileOutputStream newFileOut = new FileOutputStream(newFile);
						//System.out.println(newPath+"------------");
						String fileDir1 = SettingUtils.getCommonSetting("upload.image.path");
						FileUtils.copyFile(fileDir1 + sep + ppd.getAnnex(),newPath + sep + paths[pathIndex]);
						cell = row.getCell(6);
						cell.setCellValue(paths[pathIndex]);
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						//System.out.println();
						/*cell = row.getCell(13);
						cell.setCellValue("附件" + in);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);*/
					}
					
					
					count++;
				}
				
				row_index = end_row+4;
			}
			//将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			FileUtils.createZip(response, excelPath,path + sep + "AnnexFile", DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 管道巡检记录修改
	 * @param model
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/edit", method = RequestMethod.GET)
	public String plPatrol_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		PipelinePatrol pp = baseService.queryPipelinePatrolById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pp
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pp
				.getPl_section_id());
		
		model.addAttribute("pl", pp.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pp.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pp.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pp", pp);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		return "pages/base/pl_patrol_edit";
	}
	
	/**
	 * 管道巡检审核
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param c_month
	 * @param del
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String plPartrol_verify(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String p_month,
			@RequestParam(required = false) Integer del) {
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("p_month", p_month);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PipelinePatrol> ppList = baseService.queryPipelinePatrol(param, ps);
		
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		
		model.addAttribute("role", role);
		
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("ppList", ppList);
		model.addAttribute("verify", 1);
		return "pages/base/pl_patrol_list";
	}

	/**
	 * 管道巡检审核查看
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/show_verify", method = RequestMethod.GET)
	public String plPatrol_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PipelinePatrol pp = baseService.queryPipelinePatrolById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pp
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pp
				.getPl_section_id());

		model.addAttribute("pl", pp.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pp.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pp.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pp", pp);
		model.addAttribute("verify", 1);
		return "pages/base/pl_patrol_show";
	}
	
	@RequestMapping(value = "/pl_patrol/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plPatrol_verify_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		baseService.updatePipelinePatrolVerifyStatus(id, ud.getId(), status, verify_desc);
		
		return JsonUtil.toJson("OK");
	}
}
	

