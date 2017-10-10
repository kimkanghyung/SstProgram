package com.example.kanghyun.sstprogram;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SstProgram extends AppCompatActivity implements TextToSpeech.OnInitListener {
    SpeechRecognizer mRecognizer;
    TextView textView;
    Button Recordbt;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    String FindString;
    Boolean webFlag = false;
    Boolean musicFlag = false;
    Boolean stockFlag = false;
    Boolean smsFlag = false;
    Boolean phoneFlag = false;
    Boolean searchFlag = false;
    String smssendYN = "";
    String sendPhoneNum = "";
    String SendMessage = "";
    private TextToSpeech myTTS;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sst_program);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myTTS = new TextToSpeech(this, this);

        /*i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);
        */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);

        }
        Intent intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent2.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해요");
        intent2.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent2, 1);

        textView = (TextView) findViewById(R.id.textView1);
        Recordbt = (Button) findViewById(R.id.Recordbt);
        // openApp ();


        Recordbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final Context context = SstProgram.this;
                AppWidgetManager wmgr = AppWidgetManager.getInstance(context);

                SStWidgetTest.updateAppWidget(context,wmgr,mAppWidgetId);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
                setResult(RESULT_OK,resultValue);
                finish();
                */
                System.out.println("Recordbt-setOnClickListener");

                textView.setText("");
                FindString = "";
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해요");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(intent, 1);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sst_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            /*for (int i = 0; i < results.size(); i++)
            {
                textView.append(results.get(i)+"\n");
            }*/
            textView.append(results.get(0));
            String[] result_data = results.get(0).split(" ");
            System.out.println("results = "+ results.get(0));
            webFlag = false;
            musicFlag = false;
            stockFlag = false;
            smsFlag = false;
            phoneFlag = false ;
            searchFlag = false ;
            ArrayList keyword = new ArrayList();
            String searchWord = "";
            String Findnm = "";
            for(int i = 0 ; i < result_data.length ; i ++){
                int tmp = 0;
                System.out.println("result_data = "+ result_data[i]);
                if(result_data[i].contains("다운로드")){
                    webFlag = true;
                }
                else if (result_data[i].contains("뮤직")){
                    musicFlag = true;
                }else if (result_data[i].contains("주식")){
                    stockFlag = true;
                }
                else if(result_data[i].contains("문자")){
                    smsFlag = true;
                }
                else if(result_data[i].contains("전화")){
                    phoneFlag = true;
                }
                else if (result_data[i].contains("검색")){
                    searchFlag = true;
                }
                else{
                    if(tmp < 4) {
                        keyword.add(tmp,result_data[i]);
                    }
                    tmp ++;
                }
            }
            for(int i = 0 ; i < keyword.size() ; i ++){
                System.out.println("keyword = "+ keyword.get(i));
                System.out.println("keyword(rp) = "+ keyword.get(i).toString().replaceAll("[^\\d]",""));
                String tmpString =  keyword.get(i).toString();
                   if( tmpString.matches(".*[0-9].*")){
                        if(tmpString.replaceAll("[^0-9]", "").length() == 1) {
                            searchWord = "0" + tmpString.replaceAll("[^0-9]", "") + searchWord;
                        }else searchWord = tmpString.replaceAll("[^0-9]", "") + searchWord;
                    }
                    else {
                        if (searchWord == ""){
                            searchWord =tmpString   + "+" + searchWord ;
                        }else{
                            searchWord =tmpString   + "+" + searchWord ;
                        }

                    }
            }
            System.out.println("searchWord = "+ searchWord);

            Findnm = "https://torrentkim10.net/bbs/s.php?k="+searchWord+ "&b=&q=";
            System.out.println("Findnm = "+ Findnm);
            sendPhoneNum ="";
            SendMessage ="";


            if(webFlag){
                webBrowser(Findnm);
            }else if (musicFlag){
                appStart("com.nhn.android.music");
            }else if (stockFlag){
                appStart("kr.co.wowtv.StockTalk");
            }
            else if (smsFlag){
                ArrayList<Contact> arl = new ArrayList();
                arl = getContactList();
                String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

                for(int i = 0 ; i <arl.size() ; i++ ) {
                    System.out.println("getContactList getPhonenum= " + arl.get(i).getPhonenum());
                    System.out.println("getContactList getName = " + arl.get(i).getName());
                    Boolean tag = false;
                    for (int j = 0; j < keyword.size(); j++) {
                        if (keyword.get(j).toString().length() > 2) {
                            if (arl.get(i).getName().replaceAll(match, "").contains(keyword.get(j).toString()) || keyword.get(j).toString().contains(arl.get(i).getName().replaceAll(match, ""))) {
                                sendYN(arl.get(i).getName(), arl.get(i).getPhonenum());
                                sendPhoneNum = arl.get(i).getPhonenum();
                                tag = true;
                                break;
                            }

                        } else {
                            if (arl.get(i).getName().replaceAll(match, "").equals(keyword.get(j).toString())) {
                                sendYN(arl.get(i).getName(), arl.get(i).getPhonenum());
                                sendPhoneNum = arl.get(i).getPhonenum();
                                tag = true;

                                break;

                            }


                        }
                        if (tag) {
                            break;
                        }
                    }
                }

            }else if (phoneFlag){
                ArrayList<Contact> arl = new ArrayList();
                arl = getContactList();
                String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

                for(int i = 0 ; i <arl.size() ; i++ ){
                    System.out.println("getContactList getPhonenum= " + arl.get(i).getPhonenum());
                    System.out.println("getContactList getName = " + arl.get(i).getName());
                    Boolean tag = false;
                    for(int j =0 ; j < keyword.size() ; j++){

                        if(keyword.get(j).toString().length() > 2){
                            if(arl.get(i).getName().replaceAll(match,"").contains(keyword.get(j).toString()) || keyword.get(j).toString().contains(arl.get(i).getName().replaceAll(match,""))){
                                sendPhoneNum = "tel:" + arl.get(i).getPhonenum();
                                tag= true;

                                break;

                            }
                        }else {
                            if(arl.get(i).getName().replaceAll(match,"").equals(keyword.get(j).toString())){
                                sendPhoneNum = "tel:" + arl.get(i).getPhonenum();
                                tag= true;

                                break;

                            }
                        }

                    }
                    if(tag){
                        break;
                    }
                }


                System.out.println("sendPhoneNum = " + sendPhoneNum);

                if(sendPhoneNum != null && sendPhoneNum.length() > 10 ){
                    startActivity(new Intent("android.intent.action.CALL", Uri.parse(sendPhoneNum)));
                }


            } else if (searchFlag){

                Findnm = "https://m.search.naver.com/search.naver?query="+searchWord;
                webBrowser(Findnm);

            }

        }else if (requestCode == 2 && resultCode == RESULT_OK)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textView.append(results.get(0));

            myTTS.speak(results.get(0) + "라고 전송하시겠습니까?", TextToSpeech.QUEUE_FLUSH, null);
            SendMessage = results.get(0);
            send_SMS (SendMessage);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void webBrowser(String URL){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(intent);
        finish();
    }
    public void appStart(String str){
        Intent intent = getPackageManager().getLaunchIntentForPackage(str);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        startActivity(intent);
    }

    public void openApp () {
        boolean isExist = false;

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = packageManager.queryIntentActivities(mIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                System.out.println(mApps.get(i).activityInfo.packageName);
                if(mApps.get(i).activityInfo.packageName.startsWith("com.app.app.app")){
                    isExist = true;
                    break;
                }
            }
        } catch (Exception e) {
            isExist = false;
        }


    }

    private ArrayList<Contact> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }

                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));

                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }

    /*문자 전송 대상 찾기*/

    private void sendSMS (String phoneNumber , String message){
        String SENT = "SMS_SENT";
        String DELIVERD = "SMS_DELIVERD";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0 ,new Intent(SENT) , 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0 ,new Intent(DELIVERD) , 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK :
                        Toast.makeText(getBaseContext(),"문자 전송 완료",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber,null,message,sentPI,deliveredPI);
    }

    public void sendYN (String SendName,String PhoneNum) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        // 제목셋팅
        alertDialogBuilder.setTitle("SMS 전송 여부");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage(SendName + "(" + PhoneNum + ")" + "에게" + " SMS 전송 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("전송",
                        new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int id) {
                                // 프로그램을 종료한다
                                smssendYN = "Y";
                                setSMSMessage();

                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                smssendYN = "N";

                            }
                        });

        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();

        // 다이얼로그 보여주기
        alertDialog.show();

    }

    public void send_SMS (String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        // 제목셋팅
        alertDialogBuilder.setTitle("SMS 전송 여부");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("SMS 전송 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("전송",
                        new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int id) {
                                // 프로그램을 종료한다
                                sendSMS (sendPhoneNum , SendMessage);

                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });

        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();

        // 다이얼로그 보여주기
        alertDialog.show();

    }

    private void setSMSMessage(){
        textView.setText("");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "SMS전송내용 말해요");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 2);
    }


    @Override
    public void onInit(int status) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }

}
