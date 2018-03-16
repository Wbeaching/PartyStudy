package cn.edu.xcu.jsj.partystudy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.edu.xcu.jsj.partystudy.DB.CachePhotoFile;
import cn.edu.xcu.jsj.partystudy.DB.News;
import cn.edu.xcu.jsj.partystudy.DB.PartyConstitution;
import cn.edu.xcu.jsj.partystudy.news.NewsBox;
import cn.edu.xcu.jsj.partystudy.party.PartyMessageBox;

public class MainActivity extends AppCompatActivity {

    Button NewsBtn;
    Button PartyStudyBtn;
    ListView NewsListView;
    String[] newsObjectID;
    String[] PartyObjectID;
    ImageView LearnPartPicture;

//    定义打开news还是party的Box类的标识
    public static final int NEWS = 0;
    public static final int PARTY = 1;
    public static int NEWS_OR_PARTY_FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();// 隐藏ActionBar

        Toast toast = Toast.makeText(MainActivity.this, "正在拼命刷新中，请稍后...", Toast.LENGTH_SHORT);
        toast.show();

        findNews();

        /**
         * 初始化AppVersion表
         * SDK提供了初始化自动创建AppVersion表的方法，不再需要开发者手动在web端创建。只需要在你使用自动更新功能的地方调用如下代码
         * initAppVersion方法适合开发者调试自动更新功能时使用，一旦AppVersion表在后台创建成功，建议屏蔽或删除此方法，否则会生成多行记录
         */
//        BmobUpdateAgent.initAppVersion();

        /**
         *最常见的自动更新模式是：当用户进入应用首页后，如果处于wifi环境则检测更新，如果有更新，弹出对话框提示有新版本，
         * 用户点选更新开始下载更新。实现的方法是，在应用程序入口Activity里的OnCreate()方法中调用如下代码：
         */
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(this);


        NewsBtn = (Button)findViewById(R.id.newsButton);
        NewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NEWS_OR_PARTY_FLAG = NEWS;
                findNews();
                getObjectID();
                Toast toast = Toast.makeText(MainActivity.this, "刷新中...", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

//        PartyBtn = (Button)findViewById(R.id.partyBtn);
//        PartyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getPartyID();
//            }
//        });

        getObjectID();

        new CachePhotoFile("PartyConstitution","4Fhk333o");

        PartyStudyBtn = (Button)findViewById(R.id.partyStudyButton);
        PartyStudyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NEWS_OR_PARTY_FLAG = PARTY;
                findPartyStudy();
                getObjectID();
                Toast toast = Toast.makeText(MainActivity.this, "刷新中...", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        LearnPartPicture = (ImageView)findViewById(R.id.learnPartPicture);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LearnPartPicture.setImageURI(Uri.fromFile(new File("/sdcard/PartyStudy/party.jpg")));
            }
        }, 3000);//1秒后执行Runnable中的run方法

    }
    public void getObjectID(){
        NewsListView = (ListView)findViewById(R.id.news_list);
        NewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(NEWS_OR_PARTY_FLAG==NEWS){
                    Intent intent = new Intent(MainActivity.this,NewsBox.class);

                    //Bundle 交换数据
                    Bundle bundle = new Bundle();
                    bundle.putString("NewsObjectIDIntent",newsObjectID[position]);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(NEWS_OR_PARTY_FLAG == PARTY){
                    Intent intent = new Intent(MainActivity.this,PartyMessageBox.class);

                    //Bundle 交换数据
                    Bundle bundle = new Bundle();
                    bundle.putString("PartyObjectIDIntent",PartyObjectID[position]);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public void findNews(){
        Bmob.initialize(this,new ConfigApp().getAppID());
        BmobQuery<News> query = new BmobQuery<>();
        query.order("+time")
                .findObjects(new FindListener<News>() {
                    @Override
                    public void done(List<News> object, BmobException e) {
                        if (e == null) {
//                            Toast toast = Toast.makeText(MainActivity.this, "正在刷新新闻中...", Toast.LENGTH_SHORT);
//                            toast.show();
                            String [] strArry = new String[object.size()];
                            newsObjectID = new String[object.size()];
                            int temp=object.size()-1;
                            for (News c : object){
                                newsObjectID[temp]=c.getObjectId();
                                strArry[temp]=c.getTitle()+"\n"+c.getTime().getDate();
                                temp--;
                            }
                            initAdapter(strArry);
                        }
                        else {
                            Toast toast = Toast.makeText(MainActivity.this, "服务器拥堵，请稍后再试...", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    public void findPartyStudy(){
        Bmob.initialize(this,new ConfigApp().getAppID());
        BmobQuery<PartyConstitution> query = new BmobQuery<>();
        query.order("+createAt")
                .findObjects(new FindListener<PartyConstitution>() {
                    @Override
                    public void done(List<PartyConstitution> object, BmobException e) {
                        if (e == null) {
//                            Toast toast = Toast.makeText(MainActivity.this, "正在刷新学习材料中...", Toast.LENGTH_SHORT);
//                            toast.show();
                            String [] strArry = new String[object.size()];
                            PartyObjectID = new String[object.size()];
                            int temp=object.size()-1;
                            for (PartyConstitution c : object){
                                PartyObjectID[temp]=c.getObjectId();
                                strArry[temp]=c.getTitle();
                                temp--;
                            }
                            initAdapter(strArry);
                        }
                        else {
                            Toast toast = Toast.makeText(MainActivity.this, "服务器拥堵，请稍后再试...", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
    public void initAdapter(String [] strArry){
        ListView listView;
        ArrayAdapter<String> adapter;
        listView = (ListView)findViewById(R.id.news_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArry);
        listView.setAdapter(adapter);
        //设置listView属性
    }
}
