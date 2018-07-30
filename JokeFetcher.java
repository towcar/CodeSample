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
 * This is a class from my Dad Jokes apps that worked as a manager for obtaining joke data from various methods.
 * While the variety was not necessary it was more to flex practicing pull data using Retrofit and GraphQL/
 */
public class JokeFetcher {

    public static String jokeText;
    private static List<RetroJoke> jokeList;
    CustomAdapter jokeAdapter = null;


    public void setupJoke() {

        //obtain joke using GraphQl generated classes
        MyApolloClient.setupApollo().query(Jokes.builder().build()).enqueue(new ApolloCall.Callback<Jokes.Data>() {
            @Override
            public void onResponse(@Nonnull Response response) {
                //pulls the response and passes it into a string
                Jokes.Data responseData = (Jokes.Data) response.data();
                jokeText = responseData.joke().joke();
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                //if failed to get response then retrofit is called to attempt to pull the joke
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

                jokeText = "StudentId  :  " + response.body().toString();
                jokeText = response.body().getJokeString();

            }

            @Override
            public void onFailure(Call<RetroJoke> call, Throwable t) {
  
                Log.e("Build List", "List Failed");
                
            }
        });
    }

    public CustomAdapter getList() {
        return jokeAdapter;
    }
}
