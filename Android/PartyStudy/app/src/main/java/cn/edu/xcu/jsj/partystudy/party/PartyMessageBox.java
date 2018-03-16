package cn.edu.xcu.jsj.partystudy.party;

import android.content.Intent;
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

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.edu.xcu.jsj.partystudy.ConfigApp;
import cn.edu.xcu.jsj.partystudy.DB.CachePhotoFile;
import cn.edu.xcu.jsj.partystudy.DB.PartyConstitution;
import cn.edu.xcu.jsj.partystudy.R;

/**
 * Created by Eyre on 2017/12/22.
 */

public class PartyMessageBox extends AppCompatActivity {
    TextView PartyTextView,PartyTitleTextView,PartyMiniTitleTextView;
    ImageView PartyArticlePicture;
    String AllObjectID;
    boolean PictureFlag=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_box);
        getSupportActionBar().hide();// 隐藏ActionBar

        Toast toast = Toast.makeText(PartyMessageBox.this, "极速获取学习材料中...", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String ObjectID = bundle.getString("PartyObjectIDIntent");
        AllObjectID=ObjectID;

        PartyTextView = (TextView)findViewById(R.id.Party_text);
        PartyTitleTextView = (TextView)findViewById(R.id.PartyMessage_title);
        PartyMiniTitleTextView = (TextView)findViewById(R.id.Party_miniTitle);
        PartyArticlePicture = (ImageView)findViewById(R.id.articlePicture);

        PartyTitleTextView .setEllipsize(TextUtils.TruncateAt.MARQUEE);
        PartyTitleTextView .setSingleLine(true);
        PartyTitleTextView .setMarqueeRepeatLimit(6);

        Bmob.initialize(this,new ConfigApp().getAppID());
        BmobQuery<PartyConstitution> query = new BmobQuery<PartyConstitution>();
        query.getObject(ObjectID, new QueryListener<PartyConstitution>() {

            @Override
            public void done(PartyConstitution object, BmobException e) {
                if(e==null){
                    String PartyMiniTitle =object.getMiniTitle()+"\n\n"+object.getAuthor();
                    String PartyText =object.getArticle();
                    PartyMiniTitleTextView.setText(PartyMiniTitle);
                    PartyTextView.setText(PartyText);
                    PartyTitleTextView.setText(object.getTitle());
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
                        new CachePhotoFile("PartyConstitution", AllObjectID);
                        PartyArticlePicture.setVisibility(View.VISIBLE);
                    }

                }else{
                    PartyArticlePicture.setVisibility(View.VISIBLE);
                }
            }
        }, 1000);//1秒后执行Runnable中的run方法



        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                PartyArticlePicture.setImageURI(Uri.fromFile(new File("/sdcard/PartyStudy/"+AllObjectID+".jpg")));
            }
        }, 2000);//2秒后执行Runnable中的run方法


    }

}
