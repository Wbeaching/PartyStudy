package cn.edu.xcu.jsj.partystudy.DB;

import android.util.Log;
import java.io.File;
import java.util.List;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Eyre on 2017/12/22.
 */

public class CachePhotoFile extends BmobObject{

    public CachePhotoFile(){

    }

    public CachePhotoFile(BmobFile bmobfile){
        downloadFile(bmobfile);
    }

    public CachePhotoFile(String table, String ObjectID){
        if(table.equals("PartyConstitution")){
            BmobQuery<PartyConstitution> query = new BmobQuery<PartyConstitution>();
            query.getObject(ObjectID, new QueryListener<PartyConstitution>() {

                @Override
                public void done(PartyConstitution object, BmobException e) {
                    if(e==null){
                        downloadFile(object.getPicture());
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }
        if(table.equals("News")){
            BmobQuery<News> query = new BmobQuery<News>();
            query.getObject(ObjectID, new QueryListener<News>() {

                @Override
                public void done(News object, BmobException e) {
                    if(e==null){
                        downloadFile(object.getPicture());
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }
//        if(table.equals("News")){
//            BmobQuery<News> bmobQuery = new BmobQuery<>();
//            bmobQuery.findObjects(new FindListener<News>() {
//                @Override
//                public void done(List<News> object, BmobException e) {
//                    if(e==null){
//                        for (News tableName : object) {
//                            BmobFile bmobfile = tableName.getPicture();
//                            if(bmobfile!= null){
//                                //调用bmobfile.download方法
//                                downloadFile(bmobfile);
//                            }
//                        }
//                    }else{
//                    }
//                }
//            });
//        }

    }
    private void downloadFile(BmobFile file){
        File saveFile = new File("/sdcard/PartyStudy", file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
//                toast("开始下载...");
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
//                    toast("下载成功,保存路径:"+savePath);
                }else{
//                    toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}
