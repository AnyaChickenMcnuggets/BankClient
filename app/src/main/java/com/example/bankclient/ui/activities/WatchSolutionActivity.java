package com.example.bankclient.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.Plan;
import com.example.bankclient.util.solution.PlotGenerator;
import com.example.bankclient.util.watchers.DateAsXAxisWatcher;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDateTime;
import java.util.Arrays;

public class WatchSolutionActivity extends AppCompatActivity {
    DatabaseHelper db;
    GraphView graph;
    Plan plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_solution);
        graph = findViewById(R.id.graph);


        // получить план, который открыли
        db = new DatabaseHelper(WatchSolutionActivity.this);
        String id = getIntent().getStringExtra("plan_id");
        Cursor cursor = db.getPlanById(id);
        if (cursor.getCount()==0){
            Toast.makeText(WatchSolutionActivity.this, "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                plan = new Plan(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
            }
        }
        graph.getViewport().setScrollableY(true);

        LineGraphSeries<DataPoint> series = PlotGenerator.getSeriesFromPlot(plan.getPlot(), plan.getDate());
        series.setColor(Color.RED);
        graph.addSeries(series);
        if (!plan.getSolution().equals("")){
            LineGraphSeries<DataPoint> seriesSolution = PlotGenerator.getSeriesFromPlot(plan.getSolution(), plan.getDate());
            seriesSolution.setColor(Color.GREEN);
            graph.addSeries(seriesSolution);
        }


//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisWatcher(WatchSolutionActivity.this));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getViewport().setMaxX(367);
        graph.getViewport().setMinY(0.00);
        graph.getViewport().setMaxY(35000.00);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);


    }
}