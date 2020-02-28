package esirem.com.testappsolute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graphql.FeedResultQuery;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Myviewholder> {

    List<FeedResultQuery.Result> feedResults;
    Context context;

    public MyAdapter(List<FeedResultQuery.Result> feedResults, Context context) {
        this.feedResults = feedResults;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.Myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Nous goflons notre vue avec notre layout
        View view  = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.view_layout, viewGroup, false );

        return new Myviewholder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        //On récupère les données du résultat denotre requête
        FeedResultQuery.Result feed_data = feedResults.get( position );

        //Load animation
        Animation fade = AnimationUtils.loadAnimation(context,
                R.anim.appear_fade);
        Animation Dfade = AnimationUtils.loadAnimation(context,
                R.anim.disappear_fade);
        Animation fade_x2 = AnimationUtils.loadAnimation(context,
                R.anim.appear_fade_x2);

        //Nom
        holder.mName.setText( feed_data.name() );
        //Genre
        holder.mGender.setText( feed_data.gender() );
        //Espèces
        holder.mSpecies.setText( feed_data.species() );
        //Origine
        holder.mOrigine.setText( feed_data.origin().name() );
        //Statut
        holder.mStatut.setText( feed_data.status() );
        //Id
        holder.mId.setText( "Id: " + feed_data.id() );
        //Localisation
        holder.mLocation.setText( feed_data.location().name() );

        holder.mImage.startAnimation( fade_x2 );
        Glide.with( context ).load( feed_data.image() ).into( holder.mImage);

        //Action lors du click sur une vue
        holder.LinearTop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si les info ne sont pa visibles on les rends visibles
                if(!holder.IsVisible){

                    //Animation
                    holder.LinearInfo.startAnimation( fade );
                    holder.LinearInfo.setVisibility( View.VISIBLE );
                    holder.IsVisible = true;

                }else{ //Sinon les les rends invisibles
                    //Animation
                    holder.LinearInfo.startAnimation( Dfade );
                    holder.LinearInfo.setVisibility( View.GONE );
                    holder.IsVisible = false;

                }

            }
        } );

    }

    @Override
    public int getItemCount() {
        return feedResults.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {

        TextView mName, mGender, mSpecies, mOrigine, mStatut,mLocation, mId;
        ImageView mImage;
        LinearLayout LinearTop, LinearInfo;
        private boolean IsVisible = false;
        private ProgressBar progressBar;

        public Myviewholder(@NonNull View itemView) {
            super( itemView );

            //Liaison des éléments de notre vue à nos variables
            mName = itemView.findViewById( R.id.name );
            mGender = itemView.findViewById( R.id.sexe );
            mSpecies = itemView.findViewById( R.id.espece );
            mOrigine = itemView.findViewById( R.id.origine );
            mStatut = itemView.findViewById( R.id.statut );
            mLocation = itemView.findViewById( R.id.location );
            mId = itemView.findViewById( R.id.id_item );
            mImage = itemView.findViewById( R.id.imageView );
            LinearTop = itemView.findViewById( R.id.linear_top );
            LinearInfo = itemView.findViewById( R.id.linear_info );
            progressBar = itemView.findViewById( R.id.progress_bar );

        }
    }
}
