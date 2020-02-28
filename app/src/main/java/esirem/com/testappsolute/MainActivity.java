package esirem.com.testappsolute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphql.FeedResultQuery;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.OkHttpClient;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private ApolloClient apolloClient;
    //Url pour obtenir les données suites aux requêtes
    private static final String BASE_URL ="https://rickandmortyapi.com/graphql/";
    private static final String TAG = "MainActivity";
    private TextView textView, more_info;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Dialog popup_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //Connection de notre recyclerView à notre Vue
        recyclerView = findViewById( R.id.recyclerView );
        more_info = findViewById( R.id.more_info );

        popup_info = new Dialog( this );

        //Endroit où sera stocké nos éléments dans a mémoire cache de l'appareil
        File file = new File( getApplicationContext().getFilesDir(), "Rick" );
        long size = 1024 * 1024;

        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore( file, size );

        //Création d'une instance OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        //Construction du client Apollo
        apolloClient = ApolloClient.builder()
                .serverUrl( BASE_URL )
                .httpCache( new ApolloHttpCache( cacheStore ) )
                .okHttpClient( okHttpClient )
                .build();

        //Ici nous allons éffectuer les actions correspondantes en fonction du type de réponse (réussite ou échec)
        apolloClient.query( FeedResultQuery.builder()
                .build())
                .httpCachePolicy( HttpCachePolicy.CACHE_FIRST )
                .enqueue( new ApolloCall.Callback<FeedResultQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<FeedResultQuery.Data> response) {

                        //On va vérifier en console si la connexion avec la base de donnée est un succès et afficher un nom
                        Log.d( TAG, "onResponse:" + response.data().characters().results().get( 0 ).name() );

                        //Affichage du résulat dans le RecyclerView
                        MainActivity.this.runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                myAdapter = new MyAdapter( response.data().characters().results(), getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerView.setLayoutManager( layoutManager );
                                recyclerView.setAdapter( myAdapter );
                                myAdapter.notifyDataSetChanged();
                            }
                        } );

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                } );

        //Lorsque l'on clique sur plus d'information, le popup s'affiche
        more_info.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup_info();
            }
        } );

    }

    //le popu s'affiche à chaque lancement de l'activité
    @Override
    protected void onStart() {
        super.onStart();
        showPopup_info();
    }

    // TODO --------------------------------------------- popup d'information ----------------------------------------------------------------
    private void showPopup_info() {
        popup_info.setContentView( R.layout.popup_info );

        Button button;
        GifImageView gifImageView;

        button = popup_info.findViewById( R.id.button );
        gifImageView = popup_info.findViewById( R.id.gif );

        popup_info.setCanceledOnTouchOutside( true );

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_info.dismiss();
            }
        } );

        popup_info.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        popup_info.show();
    }
    //todo ---------------------------------------------------------------------------------------------------------------------------------------
}

