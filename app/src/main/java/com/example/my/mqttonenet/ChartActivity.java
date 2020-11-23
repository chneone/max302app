package com.example.my.mqttonenet;

/**
 * Created by Administrator on 2020/10/13.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.gson.Gson;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ChartActivity extends Activity {

    private final static String TAG = ChartActivity.class.getSimpleName();

    private LinearLayout chartLyt;
    private LinearLayout chartLytHr;
    private LinearLayout chartLytemper;
    private LinearLayout chartLytHumi;
    private LineChart mLineChart;
    private PieChart mPieChart;
    Typeface mTf; // 自定义显示字体

    private Button getDataBtn;
    private Button returnBtn;


    Handler handler=new Handler();
      //创建一个runnable对象
    private String DataString;
    private String time ="2020-10-6";
    private boolean device_status=false;
    private static final String DeviceID = "632952758";
    private static final String ApiKey = "z4Oo57s9ctWnrAySMfpIfyq1A5M=";
    //    private static final String DeviceID = "637910366";
//    private static final String ApiKey = "XyNSecg=2EC4xie27AUbVzSsJZI=";
    private static final String shumditity="humi,hr,temper,spo2,state";//onenet平台上对应设备的其中一个数据流的名字
    public static  List<Integer> Hrlist =  new ArrayList<Integer>();
    public static  List<Integer> Spolist = new ArrayList<Integer>();
    public static  List<Integer> Humilist =  new ArrayList<Integer>();
    public static  List<Integer> temperlist = new ArrayList<Integer>();
    public static  List<String> data = new ArrayList<String>();
    //创建Timer
    public   boolean pushFlag=false;
    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chartshow);
//        getDataBtn = (Button) findViewById(R.id.getData);
//        returnBtn = (Button) findViewById(R.id.returnmenu);
        chartLyt = (LinearLayout) findViewById(R.id.chart);
        chartLytHr = (LinearLayout) findViewById(R.id.chartHr);
        chartLytHumi = (LinearLayout) findViewById(R.id.chartHumi);
        chartLytemper = (LinearLayout) findViewById(R.id.chartTemper);
        Get_Msg();
        XYMultipleSeriesDataset datasetTemper = getDataSetTemper();
        XYMultipleSeriesDataset datasetHumi = getDataSetHumi();
        XYMultipleSeriesDataset datasetHr =getDataSetHr();
        XYMultipleSeriesDataset datasetSpo2 =getDataSet();
        XYMultipleSeriesRenderer mRendererTemper = getXYMulSeriesRendererTemper();
        XYMultipleSeriesRenderer mRendererHumi = getXYMulSeriesRendererHumi();
        XYMultipleSeriesRenderer mRendererHr =getXYMulSeriesRendererHr();
        XYMultipleSeriesRenderer mRendererSpo2 =getXYMulSeriesRenderer();
        drawTheChart(chartLyt,datasetSpo2,mRendererSpo2);
        drawTheChart(chartLytHr,datasetHr,mRendererHr);
        drawTheChart(chartLytHumi,datasetHumi,mRendererHumi);
        drawTheChart(chartLytemper,datasetTemper,mRendererTemper);
        //drawTheChartByMPAndroid();
        handler.postDelayed(runnable, 2000);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //设置背景风格
        // BACKGROUND_STYLE_STATIC表示静态的
        //BACKGROUND_STYLE_RIPPLE表示涟漪的，也就是可以变化的 ，跟随setActiveColor里面的颜色变化
//        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.setActiveColor("#FF107FFD") //选中颜色
//                .setInActiveColor("#e9e6e6") //未选中颜色
                .setBarBackgroundColor("#e9e6e6");//导航栏背景色


        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_find_replace_white_24dp, "Fresh"))
                .addItem(new BottomNavigationItem(R.drawable.ic_launch_white_24dp, "Exist"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "Movies & TV"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "Games"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                //获得选中状态时触发，可以做fragmengt页面切换
//                Toast.makeText(ChartActivity.this,"当前选中"+position,Toast.LENGTH_SHORT).show();
                if(position==0)// home
                {

                }
                else if(position==1)
                {
                    Get_Msg();
                }
                else if(position==2)//EXIST
                {
                    //页面返回时，弹出提示框，包括确认、取消按钮，提示文字
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
                    builder.setTitle("是否退出登录");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            returnFunc();
                        }
                    });
                    builder.show();

                }
            }
            @Override
            public void onTabUnselected(int position) {
                //当失去焦点不被选中的时候触发
//                Toast.makeText(ChartActivity.this,position+"失去了焦点",Toast.LENGTH_SHORT).show();
//                if(position==0)// home
//                {
//
//                }
//                else if(position==1)
//                {
//                    Get_Msg();
//                }
//                else if(position==2)//EXIST
//                {
//                    //页面返回时，弹出提示框，包括确认、取消按钮，提示文字
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
//                    builder.setTitle("是否退出登录");
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                           returnFunc();
//                        }
//                    });
//                    builder.show();
//
//                }
            }
            @Override
            public void onTabReselected(int position) {
                //触发不了，我也母鸡
            }
        });
    }

    public void drawTheChart(LinearLayout chart,XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer mRenderer) {
        XYSeriesRenderer renderer = getXYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
//        XYMultipleSeriesDataset dataset = getDataSet();
        GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);
        chart.addView(chartView, 0);
    }
    public void drawTheChartHr() {
        XYMultipleSeriesRenderer mRenderer = getXYMulSeriesRendererHr();
        XYSeriesRenderer renderer = getXYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        XYMultipleSeriesDataset dataset = getDataSetHr();
        GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);

        chartLytHr.addView(chartView, 0);
        //chartLyt.invalidate();
    }
    public XYSeriesRenderer getXYSeriesRenderer() {
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        //设置折线宽度
        renderer.setLineWidth(2);
        //设置折线颜色
        renderer.setColor(Color.GRAY);
        renderer.setDisplayBoundingPoints(true);
        //点的样式
        renderer.setPointStyle(PointStyle.CIRCLE);
        //设置点的大小
        renderer.setPointStrokeWidth(3);
        //设置数值显示的字体大小
        renderer.setChartValuesTextSize(30);
        //显示数值
        renderer.setDisplayChartValues(true);
        return renderer;
    }

    public XYMultipleSeriesDataset getDataSet() {
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("血氧");
        for (int i = 0; i < Spolist.size(); i++) {
            barSeries.add(Spolist.get(i));
        }
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }
    public XYMultipleSeriesDataset getDataSetHr() {
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
       CategorySeries barSeries = new CategorySeries("心率");
        for (int i = 0; i < Hrlist.size(); i++) {
            barSeries.add(Hrlist.get(i));
        }
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }
    public XYMultipleSeriesDataset getDataSetHumi() {
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("心率");
        for (int i = 0; i < Humilist.size(); i++) {
            barSeries.add(Humilist.get(i));
        }
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }
    public XYMultipleSeriesDataset getDataSetTemper() {
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("心率");
        for (int i = 0; i < temperlist.size(); i++) {
            barSeries.add(temperlist.get(i));
        }
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }
    public XYMultipleSeriesRenderer getXYMulSeriesRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setMarginsColor(Color.argb(0x00, 0xF3, 0xF3, 0xF3));
        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);

        //设置Title的内容和大小
        renderer.setChartTitle("血氧:%");
        renderer.setChartTitleTextSize(50);
        //图表与四周的边距
        renderer.setMargins(new int[]{80, 80, 50, 50});
        //设置X,Y轴title的内容和大小
        renderer.setXTitle("times");
        renderer.setYTitle("值");
        renderer.setAxisTitleTextSize(30);
        //renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.BLACK);
        //图例文字的大小
        renderer.setLegendTextSize(20);

        // x、y轴上刻度颜色和大小
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setYLabelsPadding(30);

        // 设置X轴的最小数字和最大数字，由于我们的数据是从1开始，所以设置为0.5就可以在1之前让出一部分
        // 有兴趣的童鞋可以删除下面两行代码看一下效果
        renderer.setPanEnabled(false, false);
        //显示网格
        renderer.setShowGrid(true);

        //X,Y轴上的数字数量
        renderer.setXLabels(10);
        renderer.setYLabels(10);

        // 设置X轴的最小数字和最大数字
        renderer.setXAxisMin(1);
        renderer.setXAxisMax(20);
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(100);

        // 设置渲染器显示缩放按钮
        renderer.setZoomButtonsVisible(true);
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(true);
        // 消除锯齿
        renderer.setAntialiasing(true);

        // 刻度线与X轴坐标文字左侧对齐
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y轴与Y轴坐标文字左对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);

        // 允许左右拖动,但不允许上下拖动.
        renderer.setPanEnabled(true, false);

        return renderer;
    }
    public XYMultipleSeriesRenderer getXYMulSeriesRendererHr() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setMarginsColor(Color.argb(0x00, 0xF3, 0xF3, 0xF3));

        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);

        //设置Title的内容和大小
        renderer.setChartTitle("心率");
        renderer.setChartTitleTextSize(50);

        //图表与四周的边距
        renderer.setMargins(new int[]{80, 80, 50, 50});

        //设置X,Y轴title的内容和大小
        renderer.setXTitle("times");
        renderer.setYTitle("值");
        renderer.setAxisTitleTextSize(30);
        //renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.BLACK);

        //图例文字的大小
        renderer.setLegendTextSize(20);

        // x、y轴上刻度颜色和大小
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setYLabelsPadding(30);

        // 设置X轴的最小数字和最大数字，由于我们的数据是从1开始，所以设置为0.5就可以在1之前让出一部分
        // 有兴趣的童鞋可以删除下面两行代码看一下效果
        renderer.setPanEnabled(false, false);

        //显示网格
        renderer.setShowGrid(true);

        //X,Y轴上的数字数量
        renderer.setXLabels(10);
        renderer.setYLabels(10);

        // 设置X轴的最小数字和最大数字
        renderer.setXAxisMin(1);
        renderer.setXAxisMax(20);
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(120);// set max 120

        // 设置渲染器显示缩放按钮
        renderer.setZoomButtonsVisible(true);
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(true);
        // 消除锯齿
        renderer.setAntialiasing(true);

        // 刻度线与X轴坐标文字左侧对齐
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y轴与Y轴坐标文字左对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);

        // 允许左右拖动,但不允许上下拖动.
        renderer.setPanEnabled(true, false);

        return renderer;
    }
    public XYMultipleSeriesRenderer getXYMulSeriesRendererTemper() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setMarginsColor(Color.argb(0x00, 0xF3, 0xF3, 0xF3));

        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);

        //设置Title的内容和大小
        renderer.setChartTitle("温度:℃");
        renderer.setChartTitleTextSize(50);

        //图表与四周的边距
        renderer.setMargins(new int[]{80, 80, 50, 50});

        //设置X,Y轴title的内容和大小
        renderer.setXTitle("times");
        renderer.setYTitle("值");
        renderer.setAxisTitleTextSize(30);
        //renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.BLACK);

        //图例文字的大小
        renderer.setLegendTextSize(20);

        // x、y轴上刻度颜色和大小
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setYLabelsPadding(30);

        // 设置X轴的最小数字和最大数字，由于我们的数据是从1开始，所以设置为0.5就可以在1之前让出一部分
        // 有兴趣的童鞋可以删除下面两行代码看一下效果
        renderer.setPanEnabled(false, false);

        //显示网格
        renderer.setShowGrid(true);

        //X,Y轴上的数字数量
        renderer.setXLabels(10);
        renderer.setYLabels(10);

        // 设置X轴的最小数字和最大数字
        renderer.setXAxisMin(1);
        renderer.setXAxisMax(20);
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(120);// set max 120

        // 设置渲染器显示缩放按钮
        renderer.setZoomButtonsVisible(true);
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(true);
        // 消除锯齿
        renderer.setAntialiasing(true);

        // 刻度线与X轴坐标文字左侧对齐
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y轴与Y轴坐标文字左对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);

        // 允许左右拖动,但不允许上下拖动.
        renderer.setPanEnabled(true, false);

        return renderer;
    }
    public XYMultipleSeriesRenderer getXYMulSeriesRendererHumi() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setMarginsColor(Color.argb(0x00, 0xF3, 0xF3, 0xF3));

        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);

        //设置Title的内容和大小
        renderer.setChartTitle("湿度:%");
        renderer.setChartTitleTextSize(50);

        //图表与四周的边距
        renderer.setMargins(new int[]{80, 80, 50, 50});

        //设置X,Y轴title的内容和大小
        renderer.setXTitle("times");
        renderer.setYTitle("值");
        renderer.setAxisTitleTextSize(30);
        //renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.BLACK);

        //图例文字的大小
        renderer.setLegendTextSize(20);

        // x、y轴上刻度颜色和大小
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setYLabelsPadding(30);

        // 设置X轴的最小数字和最大数字，由于我们的数据是从1开始，所以设置为0.5就可以在1之前让出一部分
        // 有兴趣的童鞋可以删除下面两行代码看一下效果
        renderer.setPanEnabled(false, false);

        //显示网格
        renderer.setShowGrid(true);

        //X,Y轴上的数字数量
        renderer.setXLabels(10);
        renderer.setYLabels(10);

        // 设置X轴的最小数字和最大数字
        renderer.setXAxisMin(1);
        renderer.setXAxisMax(20);
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(120);// set max 120

        // 设置渲染器显示缩放按钮
        renderer.setZoomButtonsVisible(true);
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(true);
        // 消除锯齿
        renderer.setAntialiasing(true);

        // 刻度线与X轴坐标文字左侧对齐
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y轴与Y轴坐标文字左对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);

        // 允许左右拖动,但不允许上下拖动.
        renderer.setPanEnabled(true, false);

        return renderer;
    }
//    public void drawshowclick(View view) {
//        Get_Msg();
//    }
//    public void returnclick(View view) {
//
//
//    }
    private  void returnFunc()
    {
        try {
            Intent intent = new Intent();
//            intent.putExtra("data",address);
            setResult(0,intent);//主ID code
            finish();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void Get() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request;
                    if(time ==null)
                    {
                        request = new Request.Builder().url("http://api.heclouds.com/devices/" + DeviceID + "/datapoints?datastream_id=" + shumditity ).header("api-key", ApiKey).build();
                    }
                    else {
                        request = new Request.Builder().url("http://api.heclouds.com/devices/" + DeviceID + "/datapoints?datastream_id=" + shumditity + ';' + "start="+time+"&limit=3600").header("api-key", ApiKey).build();
                    }
                    Response response = client.newCall(request).execute();
                    DataString= response.body().string();
                    pushFlag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    DataString ="error";

                }
            }
        }).start();

    }
    public void Get_Msg()
    {
        data.clear();
        try {
            Get();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

    private void parseJSONWithGSON(String jsonData) {
        JsonRootBean app = new Gson().fromJson(jsonData, JsonRootBean.class);
        List<Datastreams> streams = app.getData().getDatastreams();


//        List<Datapoints> points;

        try {
            //  int count = app.getData().getCount();//获取数据的数量
            int count=app.getData().getDatastreams().size();
            String [] type = new  String[count];
            String[] value = new  String[count];
            for(int j=0;j<count;j++) {
                type[j] =app.getData().getDatastreams().get(j).getId();//get 那种数据

            }
//                points  = streams.get(j).getDatapoints();
            int  lenth = streams.get(0).getDatapoints().size();
            String [] datatmp= new String [lenth];
            Hrlist.clear();
            Spolist.clear();
            temperlist.clear();
            Humilist.clear();
            //
            for (int i = 0; i < lenth; i++) {
                String timer = streams.get(0).getDatapoints().get(i).getAt();
                datatmp[i] = "";

                for (int j=0;j<count;j++) {
                    try {
                        value[j] = streams.get(j).getDatapoints().get(i).getValue();
                        if(type[j].equals("hr")) {
                            Hrlist.add(Integer.parseInt(value[j]));
                        }
                        else if(type[j].equals("spo2"))
                        {
                            Spolist.add(Integer.parseInt(value[j]));
                        }
                        else if(type[j].equals("temper"))
                        {
                            temperlist.add(Integer.parseInt(value[j]));
                        }
                        else if(type[j].equals("humi"))
                        {
                            Humilist.add(Integer.parseInt(value[j]));
                        }
                        datatmp[i] +=type[j]+":"+value[j];
                    } catch (NumberFormatException e) {

                        e.printStackTrace();

                    }
                }
                timer =timer.substring(5,19);

                datatmp[i] = timer+"-"+datatmp[i];
                data.add(datatmp[i]);
//                    datatmp+= ":"+value;
            }
            //倒叙数据 为了加载的数据在前面
            Collections.reverse(Hrlist);
            Collections.reverse(Spolist);
            Collections.reverse(temperlist);
            Collections.reverse(Humilist);
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }



    }



    Runnable runnable=new Runnable(){
        @Override
        public void run() {
// TODO Auto-generated method stub
//要做的事情
//            while (DataString==null);
            if (!DataString.equals("error")&&!DataString.equals(""))
            {
                parseJSONWithGSON(DataString);
                XYMultipleSeriesDataset datasetTemper = getDataSetTemper();
                XYMultipleSeriesDataset datasetHumi = getDataSetHumi();
                XYMultipleSeriesDataset datasetHr =getDataSetHr();
                XYMultipleSeriesDataset datasetSpo2 =getDataSet();
                XYMultipleSeriesRenderer mRendererTemper = getXYMulSeriesRendererTemper();
                XYMultipleSeriesRenderer mRendererHumi = getXYMulSeriesRendererHumi();
                XYMultipleSeriesRenderer mRendererHr =getXYMulSeriesRendererHr();
                XYMultipleSeriesRenderer mRendererSpo2 =getXYMulSeriesRenderer();
                drawTheChart(chartLyt,datasetSpo2,mRendererSpo2);
                drawTheChart(chartLytHr,datasetHr,mRendererHr);
                drawTheChart(chartLytHumi,datasetHumi,mRendererHumi);
                drawTheChart(chartLytemper,datasetTemper,mRendererTemper);
                DataString = "";
            }

            handler.postDelayed(this, 2000);
        }
    };



}