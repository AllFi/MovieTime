package research.allfi.movietime.game;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import research.allfi.movietime.users.AppUsersInfo;

public class Game {
    public enum Choice{
        TOP(0), BOTTOM(1), LEFT(2), RIGHT(3);

        int value;
        Choice(int choice){
            this.value = choice;
        }

        public int toInt(){
            return value;
        }

        public static Choice fromInt(int choice){
            switch (choice){
                case 0:
                    return TOP;
                case 1:
                    return BOTTOM;
                case 2:
                    return LEFT;
                case 3:
                    return RIGHT;
            }
            return TOP;
        }
    }

    public static int FRAMES_COUNT = 12;

    private int level;
    private String nickname;
    private ArrayList<Movie> allMovies;

    private Movie movieTop;
    private Movie movieBottom;
    private Movie movieLeft;
    private Movie movieRight;
    private Choice lastChoice;
    private int count = 0;
    private int correctCount = 0;

    public Game(String nickname){
        this.nickname = nickname;
        this.level = AppUsersInfo.getUsersLevel(nickname);
        initCommonData();
        initLevel();
    }

    public boolean hasMore(){
        return count < FRAMES_COUNT * 4;
    }

    public Bitmap getNextFrame(){
        Random rand = new Random();
        Movie movie = getMovieByRandomChoice( rand );
        if ( movie == null )
            return null;

        count++;
        return movie.getNextFrame();
    }

    public Choice getCorrectChoice(){
        return lastChoice;
    }

    public boolean answerIsCorrect( Choice choice ){
        if ( choice == lastChoice )
            correctCount++;
        return choice == lastChoice;
    }

    public int getAllCount(){
        return count;
    }

    public int getCorrectCount(){
        return correctCount;
    }

    public String getMovieTopName(){
        return movieTop.Name;
    }

    public String getMovieBottomName(){
        return movieBottom.Name;
    }

    public String getMovieLeftName(){
        return movieLeft.Name;
    }

    public String getMovieRightName(){
        return movieRight.Name;
    }

    private Movie getMovieByRandomChoice( Random rand ){
        if ( !movieTop.hasMore() && !movieBottom.hasMore() && !movieLeft.hasMore() && !movieRight.hasMore() )
            return null;

        Movie movie = movieTop;
        do{
            int choiсe = rand.nextInt( 4 );
            switch ( choiсe ){
                case 0:
                    movie = movieTop;
                    lastChoice = Choice.TOP;
                    break;
                case 1:
                    movie = movieBottom;
                    lastChoice = Choice.BOTTOM;
                    break;
                case 2:
                    movie = movieLeft;
                    lastChoice = Choice.LEFT;
                    break;
                case 3:
                    movie = movieRight;
                    lastChoice = Choice.RIGHT;
                    break;
            }
        } while ( !movie.hasMore() );
        return movie;
    }

    private void initCommonData(){
        File sdcard = Environment.getExternalStorageDirectory();
        File infoFile = new File(sdcard, "MovieTime/game/movies.info");

        if ( !infoFile.exists() )
            return;

        allMovies = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infoFile)));
            String line;
            while ( (line = reader.readLine()) != null ){
                String[] params = line.split(";");
                if ( params != null && params.length >= 2 ) {
                    Movie movie = new Movie( params[0], Integer.parseInt(params[1]) );
                    allMovies.add(movie);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void initLevel(){
        Random rand = new Random( this.nickname.hashCode() );

        ArrayList<Integer> sequence = new ArrayList<>();

        int i = 0;
        do{
            movieTop = allMovies.get( getUniqueMovieId(sequence, rand) );
            movieBottom = allMovies.get( getUniqueMovieId(sequence, rand) );
            movieLeft = allMovies.get( getUniqueMovieId(sequence, rand) );
            movieRight = allMovies.get( getUniqueMovieId(sequence, rand) );
            i++;
        } while ( i < level );
    }

    private int getUniqueMovieId( ArrayList<Integer> sequence, Random rand ){
        int id;
        do{
            id = rand.nextInt(allMovies.size());
        } while ( sequence.contains( id ) );
        sequence.add( id );
        return id;
    }
}
