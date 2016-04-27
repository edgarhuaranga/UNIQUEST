package edhuar.home.com.uniquest;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by root on 16/04/16.
 */
public class Sonido {

    MediaPlayer mediaPlayer;
    Context ctx;
    int mactual;


    public Sonido(Context ctx){
        this.ctx=ctx;
        mactual =R.raw.radar;
        mediaPlayer = MediaPlayer.create(this.ctx, mactual);
        mediaPlayer.start();
    }


    public void liberar(int val){
        Log.d("liberar", "liberar: "+mactual+"__"+ctx+"__"+mediaPlayer);
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mactual=val;
    }

    public void Play(int val){
        if(val!=mactual){
            liberar(val);
            Log.d("liberar", "play: "+mactual+"__"+ctx+"__"+mediaPlayer);
            mediaPlayer = MediaPlayer.create(ctx, mactual);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

}