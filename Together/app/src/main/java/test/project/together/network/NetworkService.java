package test.project.together.network;


import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import test.project.together.model.Comment;
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

    ////회원가입
    @POST("/all/user/register")
    Call<User> registerUserInfo(@Body User user);

    ////////////////////Matching

    //시니어:등록
    @POST("/senior/volunteerinfo/register")
    Call<Void> register(@Body Matching matching);
    //시니어:등록했던 모든 봉사 정보
    @GET("/senior/volunteerinfo/load/all")
    Call<ArrayList<Matching>> load_allRegisterInfo(@Query("user_id") int user_id);
    //시니어:리스트에서 클릭한 봉사 정보
    @GET("/senior/volunteerinfo/load/one")
    Call<Matching> load_oneRegisterInfo(@Query("matching_id") int matching_id);
    //시니어:매칭된 상대 정보
    @GET("/senior/matchinginfo/load")
    Call<User> load_matchinginfo(@Query("matching_id") int matching_id);

    //////

    //봉사자:자기 위치에서 반경 nkm 있는것만 봉사등록 리스트 받아오기
    @GET("/volunteer/volunteerinfo/load")
    Call<ArrayList<Matching>> load_nearMyLocation(@Query("latitude") double latitude, @Query("longitude") double longitude);

    //봉사자:매칭하기
    @PUT("/volunteer/volunteerinfo/matching")
    Call<Void> matching(@Query("matching_id") int matching_id, @Query("user_id") int user_id);

    //봉사자 : 내가 매칭한 리스트들 받아오기
    @GET("/volunteer/volunteerinfo/mine")
    Call<ArrayList<Matching>> getMyMatchingList(@Query("user_id") int user_id);

    /////////////////////SNS

    //SNS 새 글 작성
    @Multipart
    @POST("/all/sns/newposting") //임시로 지었음!
    Call<Void> snsPlus(@Part MultipartBody.Part file,
                                           @Part("user_id") RequestBody user_id,
                                           @Part("content") RequestBody content,
                                           @Part("date") RequestBody date);

    //SNS 모든 글 로드
    @GET("/all/sns/load")
    Call<ArrayList<Posting>> getAllSNS();

    //SNS 해당 유저에 관한 포스팅 로드
    @GET("/all/sns/load/my")
    Call<ArrayList<Posting>> load_myPosting(@Query("user_id") int user_id);

    //SNS 해당 포스팅에 관한 댓글들 로드
    @GET("/all/comment/load")
    Call<ArrayList<Comment>> load_comment(@Query("post_id") int post_id);

    //SNS 해당 포스팅에 관한 댓글 등록
    @POST("/all/comment/register")
    Call<ArrayList<Comment>> register_comment(@Body Comment comment);

    //SNS 좋아요 수 증가시키기
    @GET("/senior/volunteerinfo/load/all")
    Call<Void> increase_likeCount(@Query("post_id") int post_id);

}