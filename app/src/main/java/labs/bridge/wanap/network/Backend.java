package labs.bridge.wanap.network;



import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Backend {

    @FormUrlEncoded
    @POST("status")
    Call<String> sendStatus(@Field("deviceToken") String deviceToken, @Field("datas") String datas);

    @FormUrlEncoded
    @POST("event")
    Call<String> sendEvent(@Field("deviceToken") String deviceToken, @Field("event") String event);
}
