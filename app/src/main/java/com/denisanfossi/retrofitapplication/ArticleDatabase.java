package com.denisanfossi.retrofitapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ArticleEntity.class}, version = 1)
public abstract class ArticleDatabase extends RoomDatabase {

    private static ArticleDatabase INSTANCE;

    public static ArticleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArticleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleDatabase.class, "article_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DAO getDAO();
}
