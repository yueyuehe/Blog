package cn.hm.oil.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.Layer;

public class DrawPic {

	static String fileDir = SettingUtils
			.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
	//static String path = fileDir + "/pic/";

	// 1创建数据 CategoryDataset dataset = createDataset();
	// 2绘制曲线图 JFreeChart chart = createChart(dataset);

	public static JFreeChart createChart(CategoryDataset categoryDataset) {
		// 创建JFreeChart对象：ChartFactory.createLineChart

		// 创建主题样式
		//StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		//standardChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 20));
		// 设置图例的字体
		//standardChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 16));
		// 设置轴向的字体
		//standardChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 16));
		// 将字体设置给图片

		//ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart jfreechart = ChartFactory.createLineChart("", // 标题
				"\u6d4b\u8bd5\u6869\u7f16\u53f7", // categoryAxisLabel （category轴，横轴，X轴标签）
				"\u7535\u4f4d(-V)", // valueAxisLabel（value轴，纵轴，Y轴的标签）
				categoryDataset, // dataset
				PlotOrientation.VERTICAL, true, // legend
				true, // tooltips
				false); // URLs

		
		/*jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 15));
		jfreechart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
		CategoryAxis domainAxis = jfreechart.getCategoryPlot().getDomainAxis();  
		domainAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 11));
		domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));   
		NumberAxis numberaxis = (NumberAxis) jfreechart.getCategoryPlot().getRangeAxis();   
		numberaxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 12));   
		   ------设置Y轴的标题文字------------ 
		numberaxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));*/
		
		 CategoryPlot plot = jfreechart.getCategoryPlot();

		plot.setForegroundAlpha(0.6F);
		jfreechart.setTitle(new TextTitle(jfreechart.getTitle().getText(),new Font("SimSun", 1, 13)));
		jfreechart.getLegend().setItemFont(new Font("SimSun", 1, 15));
		CategoryAxis domainAxis = plot.getDomainAxis();

		domainAxis.setLabelFont(new Font("SimSun", 1, 12));
		domainAxis.setTickLabelFont(new Font("SimSun", 0, 10));

		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		numberaxis.setTickLabelFont(new Font("SimSun", 0, 12));
				 
		numberaxis.setLabelFont(new Font("SimSun", 0, 12));

		domainAxis.setLowerMargin(0.01D);
		domainAxis.setUpperMargin(0.01D);
		
		//NumberAxis vValueAxis = (NumberAxis) ((CategoryPlot) jfreechart.getPlot()).getRangeAxis();
		numberaxis.setAutoTickUnitSelection(false);
		 //设置纵坐标值的间距为10
		numberaxis.setTickUnit(new NumberTickUnit(0.1d));
		//纵坐标值只能是0-100之间的值
		numberaxis.setRangeWithMargins(0, 1.60);
		
		//p.setRange
		
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setBaseShapesVisible(true);
		lineandshaperenderer.setBaseShapesFilled(true);
		
		double lowpress = 0.847;  
        double uperpress = 0.853;  
        IntervalMarker intermarker = new IntervalMarker(lowpress, uperpress);  
        intermarker.setPaint(Color.decode("#FF0000"));// 域顏色  
          
        intermarker.setLabelFont(new Font("SansSerif", 41, 14));  
        intermarker.setLabelPaint(Color.RED);  
        intermarker.setLabel("");  
  
        categoryplot.addRangeMarker(intermarker, Layer.BACKGROUND);
		
		CategoryPlot p = jfreechart.getCategoryPlot();
		p.setBackgroundPaint(ChartColor.WHITE);
		p.setRangeGridlinePaint(ChartColor.PINK);
		
		//lineandshaperenderer.setSeriesStroke(8, new BasicStroke(5.0F));
		return jfreechart;
	}

	/**
	 * 创建CategoryDataset对象
	 * 
	 */
	public static CategoryDataset createDataset(List<Double> p_earlyList,
			List<Double> p_endList) {

		String[] rowKeys = { "\u4e0a\u65ec\u7535\u4f4d", "\u4e0b\u65ec\u7535\u4f4d" };
		String[] colKeys = new String[p_earlyList.size()];
		double[] p_earlyList1 = new double[p_earlyList.size()];
		double[] p_endList1 = new double[p_endList.size()];
		for (int i = 0; i < p_earlyList.size(); i++) {
			colKeys[i] = "" + (i + 1) + "";
			p_earlyList1[i] = p_earlyList.get(i);
			p_endList1[i] = p_endList.get(i);
		}
		
		double[][] data = {p_earlyList1, p_endList1};

		// 或者使用类似以下代码
		// DefaultCategoryDataset categoryDataset = new
		// DefaultCategoryDataset();
		// categoryDataset.addValue(10, "rowKey", "colKey");

		return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);
	}

	public static String saveAsFile(String charName, JFreeChart chart) {
		FileOutputStream fos_jpg = null;
		try {
			String sep = System.getProperty("file.separator");
			String fileDir = SettingUtils.getCommonSetting("upload.image.path");// 存放文件文件夹名称
			
			
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
			String chartPath = subDir.toString() + sep + charName + ".png";
			isChartPathExist(fileDir + sep + subDir.toString() + sep);
			fos_jpg = new FileOutputStream(fileDir + sep + chartPath);
			ChartUtilities.writeChartAsPNG(fos_jpg, chart, 560, 560, true, 10);
			return chartPath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fos_jpg.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断文件夹是否存在，如果不存在则新建
	 * 
	 * @param chartPath
	 */
	private static void isChartPathExist(String chartPath) {
		File file = new File(chartPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static List<String> outFonts() {

		GraphicsEnvironment ge = GraphicsEnvironment
		.getLocalGraphicsEnvironment();
		String[] b = ge.getAvailableFontFamilyNames();
		System.out.println(b.length);

		for(int i=0;i<b.length;i++){
		System.out.println(b[i]);
		}
		List<String> fonts = new ArrayList<String>();
		Font[] fs = ge.getAllFonts();
		for(int j=0;j<fs.length;j++){
			fonts.add("font:"+fs[j]);
			System.out.println("font:"+fs[j]);
		}
		return fonts;
	}
}
