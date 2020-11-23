//package com.example.my.mqttonenet;
//
//
//
//import java.awt.Font;
//import java.text.SimpleDateFormat;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartFrame;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.StandardChartTheme;
//import org.jfree.chart.axis.DateAxis;
//import org.jfree.chart.axis.DateTickUnit;
//import org.jfree.chart.axis.DateTickUnitType;
//import org.jfree.chart.axis.ValueAxis;
//import org.jfree.chart.labels.StandardXYItemLabelGenerator;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//import org.jfree.data.time.Second;
//import org.jfree.data.time.TimeSeries;
//import org.jfree.data.time.TimeSeriesCollection;
//
///**
// * @author maty
// * @version 创建时间：2018年5月3日 上午10:52:00 类说明 用来练习TimeSeries(时序图)类的使用
// */
//public class Draw_chart {
//    void palyshow(String[] args) {
//        // 首先要解决中文乱码问题
//        // 创建主题样式
//        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
//        // 设置标题字体
//        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
//        // 设置图例的字体
//        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
//        // 设置轴向的字体
//        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
//        // 应用主题样式
//        ChartFactory.setChartTheme(standardChartTheme);
//
//        // 第一步：创建timeseries实例,并将有效数据加入到该timeseries中
//        @SuppressWarnings("deprecation")
//        TimeSeries timeseries = new TimeSeries("CPU使用率", Second.class);
//        for (int i = 0; i < 5; i++) // 循环添加数据,这里会决定在x轴上存在多少个刻度
//        {
//            double temp;
//            System.out.println(i);
//            temp = Math.random() * 100;
//            timeseries.add(new Second(), temp);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 第二步：将timeseries加入到数据集中
//        TimeSeriesCollection dataset = new TimeSeriesCollection();
//        dataset.addSeries(timeseries);
//
//        // 第三步：创建时序图相关的JFreeChart对象
//        JFreeChart seriesChart = ChartFactory.createTimeSeriesChart("CPU使用率", "时间(间隔为5S)", "使用率(%)", dataset, true,
//                true, true);
//
//        // 第四步：对表的绘图区域进行设定
//        XYPlot xyPlot = seriesChart.getXYPlot(); // 时序图的plot为XYPlot
//        // 先对X轴设定
//        DateAxis axis = (DateAxis) xyPlot.getDomainAxis(); // 将ValueAxis强转成DateAxis
//        axis.setDateFormatOverride(new SimpleDateFormat("HH点mm分ss秒"));
//        axis.setAxisLineVisible(true); // 设定x轴可见
//        axis.setAutoRange(true); // 设定x轴的刻度自动调整
//        axis.setAutoTickUnitSelection(true); // x轴的刻度可见
//        axis.setVerticalTickLabels(true); // x轴的标签垂直分布
//        axis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1)); // 这里设置X轴的刻度间隔
//
//        // 在对Y轴设定
//        ValueAxis axis2 = xyPlot.getRangeAxis();
//        axis2.setAutoRange(true); // Y轴可以随着图像的伸缩做数值的变化
//        axis2.setLowerBound(0); // 保证Y轴能够从0开始显示数据
//        axis2.setUpperBound(100); // 设置Y轴显示的最大值
//        axis2.setAutoRange(false);
//
//        // 对折线本身对象进行编辑,折线对象是XYItemRenderer,XYItemRenderer是一个interface,需要将XYItemRenderer转换为实现了它的XYLineAndShapeRenderer
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true); // 使折线图上面带有图例
//        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator()); // 是数字显示在折线上
//        renderer.setBaseItemLabelsVisible(true); // 是数字显示在折线上
//        renderer.setAutoPopulateSeriesFillPaint(true);
//        renderer.setAutoPopulateSeriesOutlineStroke(true);
//
//        // 第五步：将数据防止到chartframe上面进行显示
//        ChartFrame frame = new ChartFrame("我是chartframe的title", seriesChart);
//
//        frame.pack();
//        frame.setDefaultCloseOperation(ChartFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//
//    }
//}
