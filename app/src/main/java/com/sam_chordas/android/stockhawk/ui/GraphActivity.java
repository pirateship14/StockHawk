package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = GraphActivity.class.getSimpleName();
    public static final int CURSOR_ID = 1;
    private Cursor mCursor;
    private List<Entry> entries = new ArrayList<Entry>();

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        String symbolKey = getString(R.string.symbol_name_key);
        Intent intent = getIntent();
        Bundle args = new Bundle();
        args.putString(symbolKey, intent.getStringExtra(symbolKey));

        getLoaderManager().initLoader(CURSOR_ID, args, this);

        chart = (LineChart) findViewById(R.id.chart);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                                 new String[]{QuoteColumns.CREATED, QuoteColumns.BIDPRICE},
                                 QuoteColumns.SYMBOL + " = ?",
                                 new String[]{args.getString(getString(R.string.symbol_name_key))},
                                 null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        refreshChartData(data);
    }

    private void refreshChartData(Cursor data) {
        if(data.moveToFirst()){
            int created = 0;
            do{
                float bid = Float.parseFloat(data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE)));
                entries.add(new Entry(created++, bid));
            }while (data.moveToNext());

            LineDataSet dataSet = new LineDataSet(entries, null);

            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawFilled(true);
            dataSet.setFillAlpha(255);
            dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.white));
            dataSet.setFillColor(ContextCompat.getColor(this, R.color.material_blue_500));

            sylechart();
            chart.setData(new LineData(dataSet));
            chart.invalidate();
        }
    }

    private void sylechart() {
        YAxis y = chart.getAxisLeft();
        XAxis x = chart.getXAxis();

        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);
        x.setDrawLabels(false);

        y.setDrawAxisLine(false);
        y.setTextColor(ContextCompat.getColor(this, R.color.white));
        chart.getAxisRight().setEnabled(false);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
