package edu.upc.dsa.firefighteradventure.services;

import java.util.List;

import edu.upc.dsa.firefighteradventure.models.Credentials.LoginCredentials;
import edu.upc.dsa.firefighteradventure.models.Credentials.RegisterCredentials;
import edu.upc.dsa.firefighteradventure.models.Game;
import edu.upc.dsa.firefighteradventure.models.Orders;
import edu.upc.dsa.firefighteradventure.models.UpdateParameterRequest;
import edu.upc.dsa.firefighteradventure.models.UserProfile;
import edu.upc.dsa.firefighteradventure.models.UserRanking;
import edu.upc.dsa.firefighteradventure.models.UserSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//prueba nuevo repositorio
public interface UserService {

    @POST("user/register")
    Call<ResponseBody> register(
            @Body RegisterCredentials registerCredentials
    );


    @POST("user/login")
    Call<ResponseBody> login(
            @Body LoginCredentials loginCredentials
    );


    @PUT("user/{username}/{parameter}")
    Call<ResponseBody> updateParameterByUsername(
            @Path("username") String username,
            @Path("parameter") String parameter,
            @Body UpdateParameterRequest updateParameterRequest
            );


    @GET("user/settings")
    Call<UserSettings> getUserSettings();


    @GET("user/{username}/profile")
    Call<UserProfile> getUserProfileByUsername(
            @Path("username") String username
    );


    @GET("user/{username}/ranking")
    Call<UserRanking> getUserRankingByUsername(
            @Path("username") String username
    );


    @GET("user/{username}/game")
    Call<List<Game>> getUserGamesByUsername(
            @Path("username") String username
    );

    @GET("user/{username}/orders")
    Call<List<Orders>> getUserOrdersByUsername(
            @Path("username") String username
    );

    @GET("user/ranking")
    Call<List<UserRanking>> getUserRanking();


    @DELETE("user/{username}")
    Call<ResponseBody> deleteUserByUsername(
            @Path("username") String username

    );

    @POST("user/{username}/game")
    Call<ResponseBody> createGameByUsername(
            @Path("username") String username
    );

    @POST("user/{username}/password/new")
    Call<ResponseBody> forgottenPassword(
            @Path("username") String username
    );

}
