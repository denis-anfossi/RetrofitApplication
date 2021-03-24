package com.denisanfossi.retrofitapplication;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.retrofit_textview);

        ArticleDatabase.getDatabase(getApplicationContext()).getDAO().getArticles()
                .observe(this, new Observer<List<ArticleEntity>>() {
                    @Override
                    public void onChanged(List<ArticleEntity> articleEntities) {
                        mTextView.setText(articleEntities.toString());
                    }
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Article>> listArticle = jsonPlaceHolderApi.getArticles();

        listArticle.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    List<Article> articles = response.body();

                    for (Article article : articles) {
                        String content = "";
                        content += "Id: " + article.getId() + "\n";
                        content += "User Id: " + article.getUserId() + "\n";
                        content += "Title: " + article.getTitle() + "\n";
                        content += "Body: " + article.getText() + "\n\n";

//                        mTextView.append(content);
                    }
                    updateDb(articles);
                } else {
                    mTextView.setText("Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                mTextView.setText(t.getMessage());
            }
        });

        Call<Article> article = jsonPlaceHolderApi.getArticle(42);

        article.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {

            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });

        Call<ResponseBody> fileCall = jsonPlaceHolderApi.downloadFileWithDynamicUrl("https://jsonplaceholder.typicode.com/posts");

        fileCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    writeResponseBodyToDisk(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void updateDb(List<Article> articles) {
        ArticleDatabase articleDb = ArticleDatabase.getDatabase(getApplicationContext());

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Article article : articles) {
                    articleDb.getDAO().insertArticle(new ArticleEntity(article));
                }
            }
        });
    }

    private void writeResponseBodyToDisk(ResponseBody body) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + "testfile.json");

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {

            byte[] fileReader = new byte[4096];

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    String newArticle = ",\n" +
                            "  [\n" +
                            "  {\n" +
                            "    \"userId\": 10,\n" +
                            "    \"id\": 100,\n" +
                            "    \"title\": \"at nam consequatur ea labore ea harum\",\n" +
                            "    \"body\": \"cupiditate quo est a modi nesciunt soluta\\nipsa voluptas error itaque dicta in\\nautem qui minus magnam et distinctio eum\\naccusamus ratione error aut\"\n" +
                            "  }\n" +
                            "  [";

                    outputStream.write(newArticle.getBytes(), 0, newArticle.length());
                    break;
                }

                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}