package com.sprout.clipcon.transfer;

/**
 * Created by heejeong on 2017. 9. 14..
 */

import com.sprout.clipcon.server.ServerInfo;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface RetrofitInterface {

    // Path: [Protocol]://[URL]/[Resource Path]/
    public static final String BASE_URL = ServerInfo.SERVER_URL + "/";

    /** upload string data */
    @Multipart
    @Headers({ "User-Agent: androidProgram" })
    @POST("UploadServlet")
    Call<ResponseBody> requestStringDataUpload(@Part("userName") RequestBody username, @Part("groupPK") RequestBody grouppk, @Part("stringData") RequestBody stringdata);

    /** upload captured image data */
    @Multipart
    @Headers({ "User-Agent: androidProgram", "Content-Transfer-Encoding: binary"})
    @POST("UploadServlet")
    Call<ResponseBody> requestImageDataUpload(@Part("userName") RequestBody username, @Part("groupPK") RequestBody grouppk, @Part("imageData") RequestBody imagedata);

    /** upload file data */
    @Multipart
    @Headers({ "User-Agent: androidProgram"/*, "Content-RelativePath: /" */ })
    @POST("UploadServlet")
    Call<ResponseBody> requestFileDataUpload(@Part("userName") RequestBody username, @Part("groupPK") RequestBody grouppk, @Part MultipartBody.Part file);

//    /** upload multipart file data */
//    @Multipart
//    @Headers({ "User-Agent: androidProgram" })
//    @POST("UploadServlet")
//    Call<ResponseBody> requestFileDataUpload(@Part("userName") RequestBody username, @Part("groupPK") RequestBody grouppk, @Part("multipleFileListInfo") RequestBody multiplefileListInfo, @Part MultipartBody.Part file);
//
    /** download */
    @Streaming
    @Headers({ "User-Agent: androidProgram" })
    @GET("DownloadServlet")
    Call<ResponseBody> requestFileDataDownload(@QueryMap Map<String, String> parameters);
//
//    /** send bug messgae */
//    @Multipart
//    @Headers({ "User-Agent: androidProgram" })
//    @POST("BugReportServlet")
//    Call<ResponseBody> sendBugMessage(@Part("bugMessage") RequestBody bugMessage);
}