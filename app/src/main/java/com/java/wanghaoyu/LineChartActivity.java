package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private LineChartView lineChartView;
    private List<PointValue> confirmedPointValues = new ArrayList<>();
    private List<PointValue> curedPointValues = new ArrayList<>();
    private List<PointValue> deadPointValues = new ArrayList<>();
    private List<AxisValue> axisValues = new ArrayList<>();
    private String beginTime;

    private static Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChartView = (LineChartView) findViewById(R.id.line_chart);
        manager = Manager.getInstance(this);
        manager.getCovidValues(new Manager.CovidDataCallBack() {
            @Override
            public void onError(String data) {
                Log.d("getCovidValues", data);
            }

            @Override
            public void onSuccess(JSONObject data) {
                getPointValues(data);
                Log.d("manager.getCovidValues", confirmedPointValues.size() + " " + curedPointValues.size());
                initLineChart();
            }
        }, "China");
    }
    private void getPointValues(JSONObject data){
        try {
            beginTime = data.getString("begin");
            JSONArray jsonArray = data.getJSONArray("data");
            for(int i = 0; i < jsonArray.length(); ++i){
                JSONArray covidData = jsonArray.getJSONArray(i);
                confirmedPointValues.add(new PointValue(i, covidData.getInt(0)));
                curedPointValues.add(new PointValue(i, covidData.getInt(2)));
                deadPointValues.add(new PointValue(i, covidData.getInt(3)));
            }
        }catch (JSONException e){
            Log.d("getPointValues", e.toString());
        }
    }

    private void initLineChart()
    {
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
        List<Line> lineList = new ArrayList<>();
        lineList.add(lineConfirmed);
        lineList.add(lineCured);
        lineList.add(lineDead);
        lineChartData.setLines(lineList);

        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.GRAY);
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(12);

        Axis axisY = new Axis();
        axisY.setName("number");
        axisY.setTextSize(10);
        lineChartData.setAxisYLeft(axisY);

        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom(2);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(lineChartData);
        lineChartView.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChartView.getMaximumViewport());

    }
}