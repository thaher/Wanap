package labs.bridge.wanap.network;


/**
 * Created by Thaher.m on 01-08-2018.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://wanapserver.herokuapp.com/";

    public static Backend getbackend() {

        return RetrofitClient.getClient(BASE_URL).create(Backend.class);
    }
}
