package cn.edu.xcu.jsj.partystudy.DB;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Eyre on 2017/12/22.
 */

public class PartyConstitution extends BmobObject{
    private String title;
    private String miniTitle;
    private String author;
    private String article;
    private BmobFile picture;

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMiniTitle() {
        return miniTitle;
    }

    public void setMiniTitle(String miniTitle) {
        this.miniTitle = miniTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

}
