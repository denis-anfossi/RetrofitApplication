package com.denisanfossi.retrofitapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles")
public class ArticleEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    private String title;

    private String text;

    @Ignore
    public ArticleEntity(Article article) {
        this.id = article.getId();
        this.userId = article.getUserId();
        this.title = article.getTitle();
        this.text = article.getText();
    }

    public ArticleEntity(int id, int userId, String title, String text) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
