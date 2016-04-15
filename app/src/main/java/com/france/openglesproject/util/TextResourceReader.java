package com.france.openglesproject.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/4/15.
 * 从资源中加载文本
 */
public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resourceId){
        StringBuilder body = new StringBuilder();//不考虑同步写入，用Builder
        try{
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String nexttLine;
            while((nexttLine=bufferedReader.readLine())!=null){
                body.append(nexttLine);
                body.append('\n');
            }
        }catch (IOException e){
            throw new RuntimeException("不能打开资源："+resourceId,e);
        }catch (Resources.NotFoundException nfe){
            throw new RuntimeException("资源不存在："+resourceId,nfe);
        }
        return body.toString();
    }
}
