package com.example.kanghyun.sstprogram;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class SStWidgetTest extends AppWidgetProvider {

    public static final String ACTION_BTNRECORD = "android.action.BUTTON_CLICK";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        // RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sst_widget_test);
        // views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        // appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
//        Intent intent = new Intent(context, SstProgram.class);
//        intent.setAction("CLICK");

        /*RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.content_sst_program);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,new Intent(ACTION_BTNRECORD),PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.Recordbt,pendingIntent);
*/



        for (int i=0; i< N ; i++) {

            int appWidgetId = appWidgetIds[i];

            System.out.println("===onUpdate===");
            //인텐드와 엑티비티를 연결한다.
            Intent intent3 = new Intent(context, SstProgram.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent3, 0);

            // 버튼이 눌린 후 실행할 엑티비티를 인텐드에 결합한다.
            RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.sst_widget_test);
            views2.setOnClickPendingIntent(R.id.button, pendingIntent2);

            appWidgetManager.updateAppWidget(appWidgetId,views2);
            // updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context,Intent intenet){
        String action = intenet.getAction();
        System.out.println("===CLICK1111==");
        if(action.equals(ACTION_BTNRECORD)){
            Log.e("Test","BUTTONCLICK");
            System.out.println("===CLICK==");
            Intent intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent2.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해요");
            intent2.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
            intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            // startActivityForResult(intent2, 1);
        }
        super.onReceive(context,intenet);
    }


}

