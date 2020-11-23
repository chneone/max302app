package com.example.my.mqttonenet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.system.ErrnoException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.my.mqttonenet.dataapter.Callback;
import com.maxproj.simplewaveform.SimpleWaveform;
import java.util.Random;

public class address extends Activity{

//    private static final String DeviceID = "632952758";
//    private static final String ApiKey = "z4Oo57s9ctWnrAySMfpIfyq1A5M=";
    private static final String DeviceID = "637910366";
    private static final String ApiKey = "XyNSecg=2EC4xie27AUbVzSsJZI=";
    private static final String shumditity="humi,hr,temper,spo2,state";//onenet平台上对应设备的其中一个数据流的名字
    private ListView Viewdata;
    Button control_BT;//定义发送按钮
    Button date_BT;//定义发送按钮
    Button fresh_BT;//定义发送按钮
    Button jump_BT;//定义发送按钮
    TextView device;//定义灯按钮
    EditText Status_ET;//定义信息输出框
    private  Drawable drawable_wifi=null;


    SimpleWaveform simpleWaveform;

    Paint barPencilFirst = new Paint();
    Paint barPencilSecond = new Paint();
    Paint peakPencilFirst = new Paint();
    Paint peakPencilSecond = new Paint();

    Paint xAxisPencil = new Paint();
    //选择日期Dialog
    private DatePickerDialog datePickerDialog;
    //选择时间Dialog
    private TimePickerDialog timePickerDialog;

    private Calendar calendar;
    private List<String> data;
    private String DataString;
    private String time ="2020-10-6";
    private boolean device_status=false;
    public static  List<Integer> Hrlist =  new ArrayList<Integer>();
    public static  List<Integer> Spolist = new ArrayList<Integer>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads().detectDiskWrites().detectNetwork()
//                .penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
//                .penaltyLog().penaltyDeath().build());
//        device = (TextView) findViewById(R.id.show_text);//获得连接按钮对象
        date_BT = (Button) findViewById(R.id.button_date);//获得发送按钮对象
        fresh_BT =(Button) findViewById(R.id.button_fresh);//获得发送按钮对象
        jump_BT =(Button) findViewById(R.id.button_jump);//获得发送按钮对象

        Viewdata=(ListView)findViewById(R.id.listview);
        calendar = Calendar.getInstance();
//        simpleWaveform = (SimpleWaveform) findViewById(R.id.simplewaveform);
        data = new ArrayList<String>();
//        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,
//                itemData);
//        Viewdata.setAdapter(adapter);
    }
    public void tmchoose_Click(View v) {
        datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
                Log.d("测试", time);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        //自动弹出键盘问题解决
        datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public  void fresh_Click(View v)
    {
//        data = new ArrayList<String>();
        data.clear();
        try {
            Get();
            while (DataString==null);
            parseJSONWithGSON(DataString);
            adapter=new ArrayAdapter<String>(address.this,android.R.layout.simple_expandable_list_item_1,data);
            Viewdata.setAdapter(adapter);
            DataString = null;
//            demo1();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }
    public  void jump_Click(View v)
    {

        try {
            Intent intent = new Intent(address.this,ChartActivity.class);
            startActivityForResult(intent, 2);//界面2
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void Get() {
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
                        //time =null;
// request = new Request.Builder().url("http://api.heclouds.com/devices/" + DeviceID + "/datapoints?datastream_id=" + shumditity + ';' + "start="+time+"&limit=100").header("api-key", ApiKey).build();
                    }
                    Response response = client.newCall(request).execute();
                    DataString= response.body().string();
//                    DataString = responseData;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
//    public void Get_msg()
//    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request;
//                    request = new Request.Builder().url("http://api.heclouds.com/devices/" + DeviceID).header("api-key", ApiKey).build();
//                    Response  response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    JSONObject retObj1 = new JSONObject(responseData);
//                    device_status =  retObj1.getJSONObject("data").getBoolean("online");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//
//    }
    private int randomInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
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

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }



    }

}

