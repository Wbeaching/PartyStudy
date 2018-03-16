package cn.edu.xcu.jsj.partystudy.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.edu.xcu.jsj.partystudy.ConfigApp;
import cn.edu.xcu.jsj.partystudy.DB.CachePhotoFile;
import cn.edu.xcu.jsj.partystudy.DB.News;
import cn.edu.xcu.jsj.partystudy.R;

/**
 * Created by Eyre on 2017/12/22.
 */

public class NewsBox extends AppCompatActivity {
    TextView NewsTextView,NewsTitleTextView,NewsMiniTitleTextView;
    ImageView NewsArticlePicture;
    String AllObjectID;
    boolean PictureFlag=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_box);
        getSupportActionBar().hide();// 隐藏ActionBar

        Toast toast = Toast.makeText(NewsBox.this, "极速获取新闻中...", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String ObjectID = bundle.getString("NewsObjectIDIntent");
        AllObjectID=ObjectID;

        NewsTextView = (TextView)findViewById(R.id.Party_text);
        NewsTitleTextView = (TextView)findViewById(R.id.PartyMessage_title);
        NewsMiniTitleTextView = (TextView)findViewById(R.id.Party_miniTitle);
        NewsArticlePicture = (ImageView)findViewById(R.id.articlePicture);

        NewsTitleTextView .setEllipsize(TextUtils.TruncateAt.MARQUEE);
        NewsTitleTextView .setSingleLine(true);
        NewsTitleTextView .setMarqueeRepeatLimit(6);

        Bmob.initialize(this,new ConfigApp().getAppID());
        BmobQuery<News> query = new BmobQuery<News>();
        query.getObject(ObjectID, new QueryListener<News>() {

            @Override
            public void done(News object, BmobException e) {
                if(e==null){
                    String PartyMiniTitle =object.getMiniTitle()+"\n\n"+object.getAuthor()+"\n\n"+object.getTime().getDate();
                    String PartyText =object.getArticle();
                    NewsMiniTitleTextView.setText(PartyMiniTitle);
                    NewsTextView.setText(PartyText);
                    NewsTitleTextView.setText(object.getTitle());
                    if(object.getPicture()==null){
                        PictureFlag=false;
                    }else{
                        PictureFlag=true;
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if((new CachePhotoFile().fileIsExists("/sdcard/PartyStudy/"+AllObjectID+".jpg"))==false) {
                    if(PictureFlag==true){
                        new CachePhotoFile("News", AllObjectID);
                        NewsArticlePicture.setVisibility(View.VISIBLE);
                    }

                }else{
                    NewsArticlePicture.setVisibility(View.VISIBLE);
                }
            }
        }, 1000);//1秒后执行Runnable中的run方法



        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                NewsArticlePicture.setImageURI(Uri.fromFile(new File("/sdcard/PartyStudy/"+AllObjectID+".jpg")));
            }
        }, 2000);//2秒后执行Runnable中的run方法

    }




}
