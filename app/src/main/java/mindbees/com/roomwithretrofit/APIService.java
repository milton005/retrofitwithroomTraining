package mindbees.com.roomwithretrofit;


import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by tony on 14/06/2016.
 */
public interface APIService {
//
//    @GET(Urls.SEARCH_URL)
//    Call<YoutubeModel>searchvideos(@QueryMap HashMap<String, String> params);

    @GET("getlatestfeeds.php")
    Call<List<ModelPost>> getPosts(@QueryMap HashMap<String, String> params);



//    @GET("{user}/videos")
//    Call<ModelFacebookVideos>callvideos(@Path("user") String user, @QueryMap HashMap<String, String> params);
}

