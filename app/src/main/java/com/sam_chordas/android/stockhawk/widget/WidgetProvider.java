package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Kunal on 12/19/2016.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int id : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_collection);

            Intent intent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            setRemoteAdapter(context, views);

            Intent clickIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent clickPendingIntent = TaskStackBuilder.create(context)
                                .addNextIntentWithParentStack(clickIntent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);

            appWidgetManager.updateAppWidget(id, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    private void setRemoteAdapter(Context context, RemoteViews views) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            views.setRemoteAdapter(R.id.widget_list,
                                   new Intent(context, WidgetRemoteViewsService.class));
        } else {
            views.setRemoteAdapter(0, R.id.widget_list,
                    new Intent(context, WidgetRemoteViewsService.class));
        }
    }


}
