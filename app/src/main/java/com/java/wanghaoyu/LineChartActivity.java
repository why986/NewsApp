package com.java.wanghaoyu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends AppCompatActivity {

    private LineChartView lineChartView;
    private List<PointValue> confirmedPointValues = new ArrayList<>();
    private List<PointValue> curedPointValues = new ArrayList<>();
    private List<PointValue> deadPointValues = new ArrayList<>();
    private List<AxisValue> axisValues = new ArrayList<>();

    private static Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChartView = (LineChartView) findViewById(R.id.line_chart);
        manager = Manager.getInstance(this);
        manager.getPointValues(confirmedPointValues, curedPointValues, deadPointValues, "[CHINA]");
    }

    private void initLineChart()
    {
        Line lineConfirmed = new Line(confirmedPointValues).setColor(Color.parseColor("#FFCD41"));
        lineConfirmed.setHasLabelsOnlyForSelected(true);
        lineConfirmed.setHasPoints(false);
        lineConfirmed.setFilled(false);

    }
}