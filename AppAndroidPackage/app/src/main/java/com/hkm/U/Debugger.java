package com.hkm.U;


import java.io.PrintWriter;
import java.io.StringWriter;

import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;


//version: 13071801
public class Debugger {
    public long begintime;
    public String record;
    public boolean d = true;
    //public Config config;
    public Debugger(){
        //config=new Config();
        //d=config.debug_mode;
        this.begintime=System.nanoTime();
    }

    public Debugger(boolean d){
        this();
        this.d = d;
        this.begintime=System.nanoTime();
    }

    public void setDebug(boolean d){
        this.d = d;
    }

    public double _get_time_diffs(){
        if(d){
            String time = ((System.nanoTime()-this.begintime)*0.000000001) + " ";
            if(time.indexOf('E') == -1){
                return ((System.nanoTime()-this.begintime)*0.000000001);
            }
            else return 0;
        }
        return 0;
    }
    public void d(Exception e){
        if(d){
            d(e, "easy", null);
        }
    }
    public void d(Object message){
        if(d){
            d(message, "easy", null);
        }
    }
    public void d(Object message, String mode){
        if(d){
            d(message, mode, null);
        }
    }
    public void d(Exception e, String mode){
        if(d){
            d(null, mode, e);
        }
    }
    public void d(Object message, Exception e){
        if(d){
            d(message, "easy", e);
        }
    }
    public void d(Object message, String mode, Exception ie){
        if(d){
            try{
                String content="";
                content+="\n===============\n";
                content+="After begin: "+this._get_time_diffs()+"\n";

                StringWriter sw = new StringWriter();
                new Throwable().printStackTrace(new PrintWriter(sw));
                if(message!=null)
                    content+=message+"\n";
                if(ie!=null)
                    content+=ie+"\n";
                String autorunner=sw.toString();
                String[] store=autorunner.split("\n");
                //Log.v("Debugger", ""+store[2]);
                //content+=autorunner;
                if(mode=="easy"){
                    content+=store[3];
                }else{
                    content+=autorunner;
                }
                content+="\n===============\n";
                record+=content;
                Log.v("Debugger", content);
                //new SendHttpRequest().execute(message);
            }catch(Exception e){
                Log.v("Debugger", "\n===============\n");
                Log.v("Debugger", "Debugger 26: cannot print out the error message");
                Log.v("Debugger", ""+e);
                Log.v("Debugger", "\n===============\n");


            }
        }
    }
    public void report(){
        if(d){
            try{
                new SendHttpRequest().execute(record);
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                String datenow=today.toString();

                Log.v("Debugger", "\n===============\n");
                Log.v("Debugger", "Bug report had been send to Server at "+datenow);
                Log.v("Debugger", "\n===============\n");

            }catch(Exception e){
                Log.v("Debugger", "\n===============\n");
                Log.v("Debugger", "Debugger 79: cannot send out the debug record");
                Log.v("Debugger", ""+e);
                Log.v("Debugger", "\n===============\n");
            }
        }
    }

    //======= assert =============
    public static boolean is_int(Object s) {
        try {
            Integer.parseInt((String)s);
        } catch(Exception e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    public static boolean is_numeric(Object str)
    {
        try
        {
            double d = Double.parseDouble((String)str);
        }
        catch(Exception nfe)
        {
            return false;
        }
        return true;
    }
    public static boolean is_string(Object str)
    {
        try
        {
            if(str==null) return false;
            if( str.getClass().equals(String.class) ) {
                return true;
            }
        }
        catch(Exception nfe)
        {
            return false;
        }
        return true;
    }

    //=======assert ==============

    private class SendHttpRequest extends AsyncTask<Object, Void, HttpResponse>{

        @Override
        protected HttpResponse doInBackground(Object... messager) {
            try{
                //Log.v("user", "MainActivity 37 ");
                HttpClient httpClientDefault1 = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://fenix.it.imusictech.com/reporting/index.php/test_130625");
                //Log.v("user", "MainActivity 40 ");

                //Log.v("user", "MainActivity 45 ");
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("message", (String) messager[0]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                //Log.v("user", "MainActivity 49 ");
                HttpResponse httpRespnse = httpClientDefault1.execute(httpPost);
                //Log.v("user", "MainActivity 51 ");
            }catch(Exception e2){
                String messager2="\n===============\n";
                messager2+="MainActivity 48: cannot send report \n";
                messager2+=e2;
                messager2+="\n=================\n";
                Log.v("user", messager2);
            }

            return null;
        }

        protected void onPostExecute(HttpResponse response) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try{
                String messager2="\n===============\n";
                messager2+="MainActivity 77: Receive the response: \n";
                HttpEntity entity = response.getEntity();
                String result="";
                if(entity!=null) {
                    result = EntityUtils.toString(entity);
                }
                messager2+=result;
                messager2+="\n=================\n";
                Log.v("user", messager2);
            }catch(Exception e2){
                String messager2="\n===============\n";
                messager2+="MainActivity 91: had send out to server but cannot get the response \n";
                messager2+=e2;
                messager2+="\n=================\n";
                Log.v("user", messager2);
            }
        }
    }
}
