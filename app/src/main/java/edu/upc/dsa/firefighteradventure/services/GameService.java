package edu.upc.dsa.firefighteradventure.services;

import java.util.List;

import edu.upc.dsa.firefighteradventure.models.Game;
import edu.upc.dsa.firefighteradventure.models.GameSettings;
import edu.upc.dsa.firefighteradventure.models.Inventory;
import edu.upc.dsa.firefighteradventure.models.Map;
import edu.upc.dsa.firefighteradventure.models.UpdateParameterRequest;
import edu.upc.dsa.firefighteradventure.models.UserRanking;
import edu.upc.dsa.firefighteradventure.models.User;
import edu.upc.dsa.firefighteradventure.models.UserSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GameService {

    @GET("game/settings")
    Call<GameSettings> getGameSettings();

    @GET("game/map")
    Call<List<Map>> getGameMaps();

    @GET("game/{id}/inventory")
    Call<List<Inventory>> getGameInventory(
            @Path("id") int id
    );

    @DELETE("game/{id}")
    Call<ResponseBody> deleteGameById(
            @Path("id") int id
    );

    @GET("game/{id}")
    Call<Game> getGameById(
            @Path("id") int id
    );

    @PUT("game/{id}/{parameter}")
    Call<ResponseBody> updateParameterById(
            @Path("id") int id,
            @Path("parameter") String parameter,
            @Body UpdateParameterRequest updateParameterRequest
    );

}
