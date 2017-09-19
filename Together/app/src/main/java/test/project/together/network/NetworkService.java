package test.project.together.network;


import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import test.project.together.model.Matching;
import test.project.together.model.Posting;

/**
 * Created by user on 2016-12-31.
 */

public interface NetworkService {

    /**
     * 익명게시글 목록 가져오기 : GET, /posts
     * 익명게시글 상태보기: GET, /posts{id}  =>{id}라고하면 동적으로 변한다는것임
     * 익명게시글 등록하기: POST, /posts
     */

    @POST("/matching/register")
    Call<Void> register(@Body Matching matching);

    @GET("/matching/registerinfo")
    Call<ArrayList<Matching>> registerInfo(@Query("user_id") int user_id);

    @GET("/matching/load")
    Call<ArrayList<Matching>> load(@Query("latitude") double latitude, @Query("longitude") double longitude);


}