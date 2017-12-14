package test.project.together.application;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import test.project.together.network.NetworkService;


public class ApplicationController extends Application {
    private static volatile ApplicationController instance = null;

    public static int user_id=0;
    public static String token=null;
    public static String name;

    //서버 유알엘
    private static String baseUrl = "http://52.79.105.167:3000";

    private static NetworkService networkService;
    public NetworkService getNetworkService() { return networkService; }

    public static ApplicationController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

//        Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();

        // TODO: 2016. 11. 21. 어플리케이션 초기 실행 시, retrofit을 사전에 build한다.
        buildService();


    }


    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
    // 레트로피부분을 사전에 빌드하도록함
    public void buildService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)) // json 사용할꺼기때문에 json컨버터를 추가적으로 넣어준것임
                .build();
        networkService = retrofit.create(NetworkService.class);

    }

}
