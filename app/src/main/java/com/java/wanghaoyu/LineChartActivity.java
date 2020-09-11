package com.java.wanghaoyu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class LineChartActivity extends AppCompatActivity {

    private LineChartView lineChartView1, lineChartView2;
    private List<PointValue> confirmedPointValues = new ArrayList<>();
    private List<PointValue> curedPointValues = new ArrayList<>();
    private List<PointValue> deadPointValues = new ArrayList<>();
    private List<AxisValue> axisValues = new ArrayList<>();
    private Date beginDate;
    private SimpleDateFormat simpleDateFormat;
    private EditText regionEditText ;

    private static Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lineChartView1 = (LineChartView) findViewById(R.id.line_chart);
        lineChartView2 = (LineChartView) findViewById(R.id.line_chart2);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        regionEditText = (EditText) findViewById(R.id.regionEditText);
        manager = Manager.getInstance(this);
        manager.getCovidValues(new Manager.CovidDataCallBack() {
            @Override
            public void onError(String data) {
                Log.d("getCovidValues", data);
            }

            @Override
            public void onSuccess(final JSONObject data) {
                try {
                    getPointValues(data.getJSONObject("China"));
                }
                catch (JSONException e){
                    new AlertDialog.Builder(LineChartActivity.this)
                            .setTitle("错误")
                            .setMessage("请输入正确的地区，格式应为国家|省|市")
                            .setPositiveButton("确定", null)
                            .show();
                }
                //Log.d("manager.getCovidValues", confirmedPointValues.size() + " " + curedPointValues.size());
                initLineChart();
                Button button = (Button) findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String region = regionEditText.getText().toString();
                        try {
                            getPointValues(data.getJSONObject(region));
                        }
                        catch (JSONException e){
                            new AlertDialog.Builder(LineChartActivity.this)
                                    .setTitle("错误")
                                    .setMessage("请输入正确的地区，格式应为国家|省|市")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                        initLineChart();
                    }
                });
            }
        }, "China");

    }
    private void getPointValues(JSONObject data){
        axisValues.clear();
        confirmedPointValues.clear();
        curedPointValues.clear();
        deadPointValues.clear();
        try {
            beginDate = simpleDateFormat.parse(data.getString("begin"));
            Date nowDate = new Date();
            long nowTime = nowDate.getTime();
            long beginTime = beginDate.getTime();
            long timeDelta = (nowTime - beginTime)/5;
            axisValues.add(new AxisValue(0).setLabel(simpleDateFormat.format(beginDate)));

            JSONArray jsonArray = data.getJSONArray("data");
            int len = jsonArray.length(), _len = len * 9 / 10;
            for(int i = 1; i < 5; ++i)
            {
                Date date = new Date(beginTime + timeDelta * i);
                axisValues.add(new AxisValue(i * _len / 5).setLabel(simpleDateFormat.format(date)));
            }
            axisValues.add(new AxisValue(_len).setLabel(simpleDateFormat.format(nowDate)));
            for(int i = 0; i < len; ++i){
                JSONArray covidData = jsonArray.getJSONArray(i);
                confirmedPointValues.add(new PointValue(i, covidData.getInt(0)));
                curedPointValues.add(new PointValue(i, covidData.getInt(2)));
                deadPointValues.add(new PointValue(i, covidData.getInt(3)));
            }
        }catch (JSONException e){
            Log.d("getPointValues", e.toString());
        }catch (ParseException e){
            Log.d("getPointValues", e.toString());
        }
    }

    private void initLineChart()
    {
        lineChartView1.clearAnimation();
        Line lineConfirmed = new Line(confirmedPointValues).setColor(Color.RED);//parseColor("#FFCD41"));
        lineConfirmed.setHasLabelsOnlyForSelected(true);
        lineConfirmed.setHasPoints(false);
        lineConfirmed.setFilled(false);

        Line lineCured = new Line(curedPointValues).setColor(Color.GREEN);//.parseColor("#00FF7F"));
        lineCured.setHasLabelsOnlyForSelected(true);
        lineCured.setHasPoints(false);
        lineCured.setFilled(false);

        Line lineDead = new Line(deadPointValues).setColor(Color.BLACK);//.parseColor("#000000"));
        lineDead.setHasLabelsOnlyForSelected(true);
        lineDead.setHasPoints(false);
        lineDead.setFilled(false);

        LineChartData lineChartData = new LineChartData();
        LineChartData lineChartData2 = new LineChartData();
        List<Line> lineList = new ArrayList<>();
        lineList.add(lineConfirmed);
        lineList.add(lineCured);
        lineChartData.setLines(lineList);
        List<Line> lineList2 = new ArrayList<>();
        lineList2.add(lineDead);
        lineChartData2.setLines(lineList2);

        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.GRAY);
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(12);
        axisX.setValues(axisValues);
        lineChartData.setAxisXBottom(axisX);
        lineChartData2.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setName("number");
        axisY.setTextSize(10);
        lineChartData.setAxisYLeft(axisY);
        lineChartData2.setAxisYLeft(axisY);

        lineChartView1.setInteractive(true);
        lineChartView1.setZoomEnabled(true);
        lineChartView1.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView1.setLineChartData(lineChartData);
        lineChartView1.setVisibility(View.VISIBLE);

        lineChartView2.setInteractive(true);
        lineChartView2.setZoomType(ZoomType.HORIZONTAL);
        lineChartView2.setMaxZoom(2);
        lineChartView2.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView2.setLineChartData(lineChartData2);
        lineChartView2.setVisibility(View.VISIBLE);


    }
}