package com.carsonskjerdal.dadjokes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.carsonskjerdal.graphql.Jokes;
import com.google.gson.Gson;

import java.util.List;

import javax.annotation.Nonnull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;

/**
 * Created by Carson on 5/15/2018.
 * <p>
 * Feel free to use code just give credit please :)
 */
public class JokeFetcher {

    public static String jokeText;
    private static List<RetroJoke> jokeList;
    CustomAdapter jokeAdapter = null;


    public void setupJoke() {

        MyApolloClient.setupApollo().query(Jokes.builder().build()).enqueue(new ApolloCall.Callback<Jokes.Data>() {
            @Override
            public void onResponse(@Nonnull Response response) {
                //pull the response and pass into a string
                Jokes.Data responseData = (Jokes.Data) response.data();
                jokeText = responseData.joke().joke();
                Log.e("setupJoke", "Joke Setup:" + jokeText);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                //if failed set text for user to receive
                //attempt user to try different method
                setupRetroJoke();
            }
        });


    }

    public void setupRetroJoke() {

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RetroJoke> call = service.getJoke();
        call.enqueue(new Callback<RetroJoke>() {
            @Override
            public void onResponse(Call<RetroJoke> call, retrofit2.Response<RetroJoke> response) {
                //generateDataList(response.body());

                Log.e("onResponse","Joke: " + response.isSuccessful());

                jokeText = "StudentId  :  " + response.body().toString();
                jokeText = response.body().getJokeString();
                //jokeText = responseData.joke().joke();
                Log.e("On Response","RetroJoke: " + jokeText);


            }

            @Override
            public void onFailure(Call<RetroJoke> call, Throwable t) {
                //progressDoalog.dismiss();
                Log.e("Build List", "List Failed");
                //Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public CustomAdapter getRetroJokeList(final Context context) {
        Log.e("onResponse","Called get Restro Joke List");
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RetroJoke> call = service.searchJoke();
        call.enqueue(new Callback<RetroJoke>() {
            @Override
            public void onResponse(Call<RetroJoke> call, retrofit2.Response<RetroJoke> response) {

                Log.e("onResponse","Joke: " + response.isSuccessful());

                Log.d("DataCheck",new Gson().toJson(response.body()));
                //response.body().getListSize();
                //List<RetroJoke> myList = response.body().;
                Log.e("On Response","RetroJoke: " +  response.body().toString());

                List<RetroJoke> jokes = response.body().getResults();
                /*jokeList = (List<RetroJoke>) response.body();
                CustomAdapter jokeAdapter = new CustomAdapter(context,jokeList);
                RetroJoke retroJoke = response.body();*/
                Log.e("On Response","RetroJoke: " +  jokes.size());
                jokeAdapter = new CustomAdapter(context, jokes);
                Log.e("On Response","RetroJoke: " +  jokeAdapter.getItemCount());

            }

            @Override
            public void onFailure(Call<RetroJoke> call, Throwable t) {
                //progressDoalog.dismiss();
                Log.e("Build List", "List Failed");
                // Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        return jokeAdapter;
    }

    public CustomAdapter getList() {
        return jokeAdapter;
    }
}
