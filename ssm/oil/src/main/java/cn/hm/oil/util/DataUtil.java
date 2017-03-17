/**
 * 
 */
package cn.hm.oil.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author Administrator
 * 
 */
public class DataUtil {

	/**
	 * 每位允许的字符
	 */
	private static final String POSSIBLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 生产一个指定长度的随机字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return
	 */
	public static String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS
					.length())));
		}
		return sb.toString();
	}

	public static String formatData(int number, int width) {
		if (number <= 0)
			return null;
		String sNum = number + "";
		if (sNum.length() < width) {
			int w = width - sNum.length();
			for (int i = 0; i < w; i++) {
				sNum = "0" + sNum;
			}
		}
		return sNum;
	}

	public static String encodeStr(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encodeStrUTF8(String str) {
		if (StringUtils.isBlank(str))
			return null;
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final char[] array = { 'q', 'w', 'e', 'r', 't', 'y', 'u',
			'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
			'x', 'c', 'v', 'b', 'n', 'm', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
			'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V',
			'B', 'N', 'M' };

	public static String _10_to_62(long number) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(array[new Long((rest - (rest / 62) * 62)).intValue()]);
			rest = rest / 62;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.toString();
	}

	public static long _62_to_10(String sixty_str) {
		int multiple = 1;
		long result = 0;
		Character c;
		for (int i = 0; i < sixty_str.length(); i++) {
			c = sixty_str.charAt(sixty_str.length() - i - 1);
			result += _62_value(c) * multiple;
			multiple = multiple * 62;
		}
		return result;
	}

	private static int _62_value(Character c) {
		for (int i = 0; i < array.length; i++) {
			if (c == array[i]) {
				return i;
			}
		}
		return -1;
	}

	public static void deleteByUploadImg(String fileName) {
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.image.path");// 存放文件文件夹名称

		String filePath = fileDir + sep + fileName;
		FileUtils.deleteFile(filePath);

		if (!StringUtils.isBlank(fileName)) {
			String[] parsedName = new FileUtils()
					.getFullFileNameAndExtension(filePath);
			String thumbPath = parsedName[0] + parsedName[1] + "_S"
					+ parsedName[2];
			FileUtils.deleteFile(thumbPath);
		}
	}
	
	public static List<String> uploadMFile(HttpServletRequest req, String name) throws Exception {
		List<String> files = new ArrayList<String>();
		
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

		/*StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(array[random.nextInt(array.length)]);
			sb.append(array[random.nextInt(array.length)]);

			subDir.append(sb.toString());
		}*/

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		List<MultipartFile> mfs = multiRequest.getFiles(name);
		for (MultipartFile mft : mfs) {
			CommonsMultipartFile mf = (CommonsMultipartFile) mft;
			byte[] bytes = mf.getBytes();
			StringBuffer newFileName = new StringBuffer();
			if (bytes.length != 0) {
				String fileTrueName = mf.getOriginalFilename();
				String ext = fileTrueName.substring(fileTrueName
						.lastIndexOf("."));
				/*if (!".jpg/.jpeg/.gif/.bmp/.png".contains(ext)) {
					throw new Exception("格式错误！");
				}*/
				String fname = fileTrueName.substring(0,fileTrueName
						.lastIndexOf("."));
				Date dt = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dt);
				String ct = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY) + "-" +cal.get(Calendar.MINUTE) + "-" +cal.get(Calendar.SECOND) + "-" +cal.get(Calendar.MILLISECOND);
				newFileName.append(fname+"_"+ct);
				newFileName.append(ext);
				String fileName = fileDir+sep
						+ newFileName.toString();

				File uploadedFile = new File(fileName);
				try {
					FileCopyUtils.copy(bytes, uploadedFile);
					
				} catch (IOException e) {
					e.printStackTrace();
				}

				files.add(newFileName.toString());
			} else {
				files.add(null);
			}

		}

		return files;
	}
	

	/**
	 * 上传图片
	 * 
	 * @param req
	 * @param name
	 * @param createThumb
	 * @return
	 * @throws Exception
	 */
	public static List<String> uploadImg(HttpServletRequest req, String name,
			boolean createThumb) throws Exception {
		List<String> files = new ArrayList<String>();
		
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.image.path");// 存放文件文件夹名称

		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(array[random.nextInt(array.length)]);
			sb.append(array[random.nextInt(array.length)]);

			subDir.append(sb.toString());
		}

		File dirPath = new File(fileDir + sep + subDir.toString());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		List<MultipartFile> mfs = multiRequest.getFiles(name);
		for (MultipartFile mft : mfs) {
			CommonsMultipartFile mf = (CommonsMultipartFile) mft;
			byte[] bytes = mf.getBytes();
			StringBuffer newFileName = new StringBuffer();
			if (bytes.length != 0) {
				String fileTrueName = mf.getOriginalFilename();
				String ext = fileTrueName.substring(fileTrueName
						.lastIndexOf("."));
				if (!".jpg/.jpeg/.gif/.bmp/.png".contains(ext)) {
					throw new Exception("格式错误！");
				}
				newFileName.append(System.currentTimeMillis());
				newFileName.append(ext);
				String fileName = fileDir + sep + subDir.toString() + sep
						+ newFileName.toString();

				File uploadedFile = new File(fileName);
				try {
					FileCopyUtils.copy(bytes, uploadedFile);
					if (createThumb) {
						String heightS = SettingUtils
								.getCommonSetting("thumbnailator.height");
						String widthS = SettingUtils
								.getCommonSetting("thumbnailator.width");

						Integer height = !StringUtils.isBlank(heightS) ? Integer
								.valueOf(heightS) : 0;
						Integer width = !StringUtils.isBlank(widthS) ? Integer
								.valueOf(widthS) : 0;

						ImageResizer.resizeImage(fileName, width, height, "_S");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				files.add(subDir.toString() + sep + newFileName.toString());
			} else {
				files.add(null);
			}

		}

		return files;
	}

	/**
	 * 上传图片
	 * 
	 * @param req
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<String> uploadImg(HttpServletRequest req, String name)
			throws Exception {
		return uploadImg(req, name, false);
	}

	/**
	 * 小文件上传
	 * 
	 * @param req
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<String> uploadFile(HttpServletRequest req, String name)
			throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.path");// 存放文件文件夹名称

		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(array[random.nextInt(array.length)]);
			sb.append(array[random.nextInt(array.length)]);

			subDir.append(sb.toString());
		}

		File dirPath = new File(fileDir + sep + subDir.toString());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		List<MultipartFile> mfs = multiRequest.getFiles(name);
		List<String> files = new ArrayList<String>();
		for (MultipartFile mft : mfs) {
			CommonsMultipartFile mf = (CommonsMultipartFile) mft;
			byte[] bytes = mf.getBytes();
			StringBuffer newFileName = new StringBuffer();
			if (bytes.length != 0) {
				String fileTrueName = mf.getOriginalFilename();
				String ext = fileTrueName.substring(fileTrueName
						.lastIndexOf("."));
				String fname = fileTrueName.substring(0,fileTrueName
						.lastIndexOf("."));
				Date dt = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dt);
				String ct = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY) + "-" +cal.get(Calendar.MINUTE) + "-" +cal.get(Calendar.SECOND) + "-" +cal.get(Calendar.MILLISECOND);
				newFileName.append(fname+"_"+ct);
				newFileName.append(ext);
				String fileName = fileDir + sep + subDir.toString() + sep
						+ newFileName.toString();

				File uploadedFile = new File(fileName);
				try {
					FileCopyUtils.copy(bytes, uploadedFile);
				} catch (IOException e) {
					e.printStackTrace();
				}

				files.add(subDir.toString() + sep + newFileName.toString());
			} else {
				files.add(null);
			}
		}

		return files;
	}

	/**
	 * 多个小文件上传
	 * @param req
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<String> uploadFileList(HttpServletRequest req, String name)
			throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.path");// 存放文件文件夹名称

		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(array[random.nextInt(array.length)]);
			sb.append(array[random.nextInt(array.length)]);

			subDir.append(sb.toString());
		}

		File dirPath = new File(fileDir + sep + subDir.toString());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		List<MultipartFile> mfs = multiRequest.getFiles(name);
		List<String> files = new ArrayList<String>();
		for (MultipartFile mft : mfs) {
			CommonsMultipartFile mf = (CommonsMultipartFile) mft;
			byte[] bytes = mf.getBytes();
			StringBuffer newFileName = new StringBuffer();
			if (bytes.length != 0) {
				String fileTrueName = mf.getOriginalFilename();
				String ext = fileTrueName.substring(fileTrueName
						.lastIndexOf("."));
				String fname = fileTrueName.substring(0,fileTrueName
						.lastIndexOf("."));
				Date dt = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dt);
				String ct = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY) + "-" +cal.get(Calendar.MINUTE) + "-" +cal.get(Calendar.SECOND) + "-" +cal.get(Calendar.MILLISECOND);
				newFileName.append(fname+"_"+ct);
				newFileName.append(ext);
				String fileName = fileDir + sep + subDir.toString() + sep
						+ newFileName.toString();

				File uploadedFile = new File(fileName);
				try {
					FileCopyUtils.copy(bytes, uploadedFile);
				} catch (IOException e) {
					e.printStackTrace();
				}

				files.add(subDir.toString() + sep + newFileName.toString());
			} else {
				files.add(null);
			}
		}

		return files;
	}
	
	/**
	 * 用于处理上传分片的文件
	 * 
	 * @param req
	 * @param name
	 */
	public static String uploadSegmentedFile(HttpServletRequest req, String name) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		MultipartFile mf = multiRequest.getFile("file");
		try {
			byte[] src = mf.getBytes();
			String fileTrueName = mf.getOriginalFilename();
			String ext = fileTrueName.substring(fileTrueName
					.lastIndexOf("."));
			String fname = fileTrueName.substring(0,fileTrueName
					.lastIndexOf("."));
			Date dt = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			String ct = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY) + "-" +cal.get(Calendar.MINUTE) + "-" +cal.get(Calendar.SECOND) + "-" +cal.get(Calendar.MILLISECOND);
			name = fname+"_"+ct+ext;
			if (src != null && src.length != 0) {
				File dst = new File(fileDir + sep + name);
				FileUtils.copyByteToFile(src, dst);
				return name;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String moveToDir(String filename, boolean createThumb)
			throws IOException {
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
		String sep = System.getProperty("file.separator");
		String imgFileDir = SettingUtils.getCommonSetting("upload.image.path");

		File srcFile = new File(fileDir + sep + filename);

		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);

			subDir.append(sb.toString());
		}

		File dstDir = new File(imgFileDir + sep + subDir);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}

		org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir,
				true);

		if (createThumb) {
			String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
			String widthS = SettingUtils.getCommonSetting("thumbnailator.width");

			Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 0;
			Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 0;

			ImageResizer.resizeImage(imgFileDir + sep + subDir + sep + filename, width, height, "_S");
		}

		return subDir + sep + filename;
	}
	
	public static String moveToFileDir(String filename) throws IOException {
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
		String sep = System.getProperty("file.separator");
		String imgFileDir = SettingUtils.getCommonSetting("upload.file.path");

		File srcFile = new File(fileDir + sep + filename);

		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);

			subDir.append(sb.toString());
		}

		File dstDir = new File(imgFileDir + sep + subDir);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}

		org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir,
				true);
		return subDir + sep + filename;
	}

	/**
	 * 移动文件到删除的目录
	 * @param filename
	 * @throws IOException
	 */
	public static void moveFileToDeleteFileDir(String filename) throws IOException {
		String fileDir = SettingUtils.getCommonSetting("upload.file.path");
		String sep = System.getProperty("file.separator");
		String imgFileDir = SettingUtils.getCommonSetting("upload.delete.path");

		File srcFile = new File(fileDir + sep + filename);

		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);

			subDir.append(sb.toString());
		}

		
		if(srcFile.exists()){
			File dstDir = new File(imgFileDir + sep + subDir);
			if (!dstDir.exists()) {
				dstDir.mkdirs();
			}
			org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir,
					true);
		}
		//return subDir + sep + filename;
	}
	
	/**
	 * 上传图片至临时目录
	 * 
	 * @param req
	 * @param name
	 * @param createThumb
	 * @return
	 * @throws Exception
	 */
	public static String uploadImgToTempDir(HttpServletRequest req, String name)
			throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		MultipartFile mft = multiRequest.getFile(name);
		CommonsMultipartFile mf = (CommonsMultipartFile) mft;
		byte[] bytes = mf.getBytes();
		StringBuffer newFileName = new StringBuffer();
		if (bytes.length != 0) {
			String fileTrueName = mf.getOriginalFilename();
			String ext = fileTrueName.substring(fileTrueName.lastIndexOf("."));
			if (!".jpg/.jpeg/.gif/.bmp/.png".contains(ext)) {
				throw new Exception("格式错误！");
			}
			newFileName.append(System.currentTimeMillis());
			newFileName.append(ext);
			String fileName = fileDir + sep + newFileName.toString();

			File uploadedFile = new File(fileName);

			try {
				FileCopyUtils.copy(bytes, uploadedFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return newFileName.toString();
	}

	/**
	 * 存放终端上传的图片
	 * 
	 * @param filename
	 * @param image
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String uploadImage(String filename, String image) {
		FileOutputStream fos = null;
		try {
			String dirName = buildDirName();
			String toDir = SettingUtils.getCommonSetting("upload.image.path"); // 存储路径
			toDir += dirName;
			sun.misc.BASE64Decoder a = new sun.misc.BASE64Decoder();
			byte[] buffer = a.decodeBuffer(image); // 对android传过来的图片字符串进行解码
			String type = getImageType(buffer);
			if (!StringUtils.equalsIgnoreCase(type, "jpg")
					&& !StringUtils.equalsIgnoreCase(type, "jpeg")
					&& !StringUtils.equalsIgnoreCase(type, "png")) {
				return "-1";
			}
			File destDir = new File(toDir);
			if (!destDir.exists())
				destDir.mkdirs();
			fos = new FileOutputStream(new File(destDir, filename)); // 保存图片
			fos.write(buffer);
			fos.flush();
			fos.close();
			return dirName + filename;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片格式
	 * 
	 * @param img
	 * @return
	 * @throws IOException
	 */
	public static String getImageType(byte[] img) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(img);
		MemoryCacheImageInputStream is = new MemoryCacheImageInputStream(bais);
		Iterator<ImageReader> it = ImageIO.getImageReaders(is);
		ImageReader r = null;
		while (it.hasNext()) {
			r = it.next();
			break;
		}
		if (r == null) {
			return null;
		}
		return r.getFormatName().toLowerCase();
	}

	public static String buildDirName() {
		String sep = System.getProperty("file.separator");
		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);
			sb.append(DataUtil.array[random.nextInt(DataUtil.array.length)]);

			subDir.append(sb.toString());
		}

		return subDir.toString();
	}

	/**
	 * 作业区资料上传
	 * 
	 * @param req
	 * @param name
	 */
	public static void uploadFileForOpe(HttpServletRequest req, String name) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		MultipartFile mf = multiRequest.getFile("file");

		try {
			byte[] src = mf.getBytes();
			if (src != null && src.length != 0) {
				File dst = new File(fileDir + sep + name);
				copy(src, dst);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件上传过程中的处理方法
	 * 
	 * @param src
	 * @param dst
	 */
	private static void copy(byte[] src, File dst) {
		OutputStream out = null;
		try {
			if (dst.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(dst, true)); // plupload
																					// 配置了chunk的时候新上传的文件appand到文件末尾
			} else {
				out = new BufferedOutputStream(new FileOutputStream(dst));
			}

			out.write(src);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size
	 * @return
	 */

	public static String FormateSize(Integer size) {
		if (size <= 1024)
			return "1KB";
		for (int i = 0;; i++) {
			size = size / 1024;
			if (size <= 1024) {
				switch (i) {
				case 0:
					return size.toString() + "KB";
				case 1:
					return size.toString() + "MB";
				case 2:
					return size.toString() + "GB";
				default:
					return "error";
				}
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	//文件下载方法
	public static void downLoad(String filePath, HttpServletResponse response,
			String isOnLine) {

		String pathsavefile = filePath;// 要下载的文件

		String fileName = isOnLine;// 保存窗口中显示的文件名

		try

		{

			response.reset();

			response.setContentType("APPLICATION/OCTET-STREAM");

			/*
			 * 要显示到客户端的文件名转码是必需的，特别是中文名，
			 * 
			 * 否则可能出现文件名乱码甚至是浏览器显示无法下载的问题
			 */

			fileName = response.encodeURL(new String(fileName.getBytes(),
					"ISO8859_1"));// 转码

			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");

			ServletOutputStream out = response.getOutputStream();

			InputStream inStream = new FileInputStream(pathsavefile);

			// 循环取出流中的数据

			byte[] b = new byte[1024];

			int len;

			while ((len = inStream.read(b)) > 0)

				out.write(b, 0, len);

			response.setStatus(response.SC_OK);

			response.flushBuffer();

			out.close();

			inStream.close();

		}

		catch (Exception e)

		{

			System.out.println(e);

		}

	}
	
	/**
	 * 管道隐患文件上传
	 * @param req
	 * @param name
	 */
	public static void uploadFileForHaz(HttpServletRequest req, String name) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.hazard.path");// 存放文件文件夹名称

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		MultipartFile mf = multiRequest.getFile("file");

		try {
			byte[] src = mf.getBytes();
			if (src != null && src.length != 0) {
				File dst = new File(fileDir + sep + name);
				copy(src, dst);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
