package research.allfi.movietime.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.File;

public class Movie {
    public String Name;
    public int Id;
    private int curFrameNumber;


    public Movie( String name, int id ){
        Name = name;
        Id = id;
        curFrameNumber = 0;
    }

    public Bitmap getNextFrame(){
        if ( curFrameNumber == Game.FRAMES_COUNT )
            return null;

        Bitmap bitmap = loadBitmap();
        curFrameNumber++;
        return bitmap;
    }

    public boolean hasMore(){
        return curFrameNumber < Game.FRAMES_COUNT;
    }

    private Bitmap loadBitmap(){
        File sdcard = Environment.getExternalStorageDirectory();
        File imgFile = new File(sdcard, "MovieTime/game/movies/movie_" + Id + "/frame_" + curFrameNumber +".jpg");

        if ( !imgFile.exists() )
            return null;

        return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    }
}
