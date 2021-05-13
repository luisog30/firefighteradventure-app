package edu.upc.dsa.firefighteradventure.services;

import java.util.List;

import edu.upc.dsa.firefighteradventure.models.Item;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopService {

    @GET("shop")
    Call<List<Item>> getShopItems();

    @POST("shop/id/{id}/buy")
    Call<ResponseBody> buyItemById(
            @Path("id") int id,
            @Query("id_game") int id_game,
            @Query("quantity") int quantity
    );


    @POST("shop/upgrades/{name}")
    Call<ResponseBody> buyUpgrade(
            @Path("name") String name,
            @Query("id_game") int id_game
    );

}
