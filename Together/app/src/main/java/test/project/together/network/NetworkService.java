package test.project.together.network;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.model.User;

/**
 * Created by user on 2016-12-31.
 */

public interface NetworkService {

    /**
     * 익명게시글 목록 가져오기 : GET, /posts
     * 익명게시글 상태보기: GET, /posts{id}  =>{id}라고하면 동적으로 변한다는것임
     * 익명게시글 등록하기: POST, /posts
     */

    //////////////

    //시니어:등록
    @POST("/senior/register/volunteerinfo")
    Call<Void> register(@Body Matching matching);
    //시니어:등록했던 모든 봉사 정보
    @GET("/senior/load/all/volunteerinfo")
    Call<ArrayList<Matching>> load_allRegisterInfo(@Query("user_id") int user_id);
    //시니어:리스트에서 클릭한 봉사 정보
    @GET("/senior/load/one/volunteerinfo")
    Call<Matching> load_oneRegisterInfo(@Query("matching_id") int matching_id);
    //시니어:매칭된 상대 정보
    @GET("/senior/load/matchinginfo")
    Call<User> load_matchinginfo(@Query("matching_id") int matching_id);

    /////////////

    //봉사자:자기 위치에서 반경 nkm 있는것만 봉사등록 리스트 받아오기
    @GET("/volunteer/load/volunteerinfo")
    Call<ArrayList<Matching>> load_nearMyLocation(@Query("latitude") double latitude, @Query("longitude") double longitude);

    //SNS 새 글 작성
    @POST("/sns/newposting") //임시로 지었음!
    Call<Void> snsPlus(@Body Posting posting);


}