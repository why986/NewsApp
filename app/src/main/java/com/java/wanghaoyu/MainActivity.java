package com.java.wanghaoyu;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.java.wanghaoyu.ui.main.NewsListFragment;
import com.java.wanghaoyu.ui.main.ViewPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
new Thread(new Runnable() {
            @Override
            public void run() {

                Manager manager = Manager.getInstance(MainActivity.this);
                List<SimpleNews> news_list = manager.getSimpleNewsList("news", 1, 6);
            }
        }).start();

 */
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String type = "news";
        adapter.addFragment(new NewsListFragment(type), type);
        type = "paper";
        adapter.addFragment(new NewsListFragment(type), type);
//        type = "event";
//        adapter.addFragment(new NewsListFragment(type), type);
//        type = "啊这";
//        adapter.addFragment(new NewsListFragment(type), type);
        viewpager.setAdapter(adapter);


        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type=news&page=1&size=1");
                    HttpsTrustManager.allowAllSSL();
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setAllowUserInteraction(false);
                    connection.setInstanceFollowRedirects(true);
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
// 发起请求
                    connection.connect();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("HTTPS", "[NewsDetailActivity line 80] NOT OK");
                    }
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    reader.close();
                    connection.disconnect();
                    Log.d("READ", builder.toString());
                }catch (Exception e){
                    Log.d("READ", e.toString());
                }
            }
        }).start();
        connectToInterface("https://covid-dashboard.aminer.cn/api/events/list?type=news&page=1&size=1", new Manager.MyCallBack() {
            @Override
            public void timeout() {}
            @Override
            public void error() {}
            @Override
            public void onSuccess(String data) {
                Log.d("callBack onSuccess ", data);
            }
        });

         */
    }
    void connectToInterface(String url, final Manager.MyCallBack myCallBack)
    {
        Log.d("connectToInterface", url);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(20000, TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("connectToInterface", " Failed");
                Log.d("connectToInterface", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    //Log.d("okHttp ", response.body().string());
                    myCallBack.onSuccess(response.body().string());
                }catch (Exception e)
                {
                    Log.d("connectToInterface ", e.toString());
                    e.printStackTrace();
                }
            }
        });

        Log.d("connectToInterface", url);
    }




/*
    private static class Task extends AsyncTask<Void, Void, Task.Result> {
        String url;
        String type;
        int page;
        int size;

        Task(String url, String type, int page, int size)
        {
            this.url = url;
            this.type = type;
            this.page = page;
            this.size = size;
        }
        @Override
        protected Result doInBackground(Void... voids) {
            try {
                URL url = new URL(this.url + "?type=" + this.type + "&page=" + this.page + "size=" + this.size);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while((str = in.readLine()) != null){
                    stringBuilder.append(str);
                    stringBuilder.append("\n");
                }
                in.close();
                httpURLConnection.disconnect();

                return new Result(STATE.OK, stringBuilder.toString());
            }catch (SocketTimeoutException e) {
                return new Result(STATE.TIMEOUT, null);
            }catch (Exception e){
                return new Result(STATE.OTHERS, null);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            switch (result.state){
                case OK:
                    try {
                        manager.dataBase.insertNewsList(type, page, result.data);
                    }catch (JSONException e){

                    }
                    break;
                case TIMEOUT:
                    break;
                case OTHERS:
                    break;
            }
        }

        enum STATE{OK, TIMEOUT, OTHERS}

        class Result {
            STATE state;
            String data;

            Result(STATE state, String data)
            {
                this.state = state;
                this.data = data;
            }
        }
    }

 */
}