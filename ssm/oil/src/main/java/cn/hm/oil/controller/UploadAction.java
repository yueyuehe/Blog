/**
 * 
 */
package cn.hm.oil.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.util.DataUtil;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadAction {

	@RequestMapping(value = "/ckeditorUpload", method = RequestMethod.POST)
	public void listTdc(HttpServletRequest request, Model model, @RequestParam String CKEditorFuncNum, PrintWriter out) {
		try {
			String fileName = DataUtil.uploadImg(request, "upload").get(0);

			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'" + request.getContextPath() + "/img/" + fileName + "','')");
			out.println("</script>");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
			out.println("</script>");
		}

	}

	/**
	 * 文件上传，用于处理大文件上传
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(Model model, HttpServletRequest request, @RequestParam String name, @RequestParam Integer chunk, @RequestParam Integer chunks, PrintWriter pw) {
		//String sessionId = request.getSession().getId();
		
		name = DataUtil.uploadSegmentedFile(request, name);

		pw.write(name);
		
		/*
		String fileName = sessionId + "_" + name;
		int index = name.lastIndexOf(".");
		String ext = "";
		if (index > 0) {
			ext = name.substring(index);
			name = name.substring(0, index);
		}
		
		DataUtil.uploadSegmentedFile(request, sessionId + "_" + name + "_" + chunk + ext);

		if (chunk.intValue() == (chunks.intValue() - 1)) {
			DataUtil.mergeFile(sessionId + "_" + name + "_", ext, fileName, chunks);
		}

		pw.write(fileName);
		pw.flush();
		*/
	}

	/**
	 * 文件上传，用于处理大文件上传
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/uploadWholeImage", method = RequestMethod.POST)
	public void uploadWholeFile(Model model, HttpServletRequest request, PrintWriter pw) {
		try {
			String fileName = DataUtil.uploadImgToTempDir(request, "file");

			pw.write(fileName);
		} catch (Exception e) {
			pw.write("上传失败！" + e.getMessage());
		}

		pw.flush();
	}
	
	/**
	 * 文件上传，用于处理大文件上传
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/uploadMFile", method = RequestMethod.POST)
	public @ResponseBody List<String> uploadMFile(Model model, HttpServletRequest request) {
		try {
			List<String> fileName = DataUtil.uploadMFile(request, "file");

			return fileName;
		} catch (Exception e) {
			e.getStackTrace();
			
			return null;
		}
			
	}
}
