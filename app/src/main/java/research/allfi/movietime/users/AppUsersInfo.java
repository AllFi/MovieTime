package research.allfi.movietime.users;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppUsersInfo {
    private static String cacheFileName = "MovieTime/users.txt";

    private static Map<User, Integer> usersInfo = new HashMap<>();

    public static boolean registerUser( String name, String surname, Gender gender, String nickname ){
        if ( !cacheFileExist() && !createCacheFile() )
            return false;

        User user = new User( name, surname, gender, nickname );
        if ( !appendToCacheFile( user, 1 ) )
            return false;

        usersInfo.put( user, 1 );
        return true;
    }

    public static boolean checkNicknameUniqueness( String nickname ){
        boolean unique = true;
        for (Map.Entry<User, Integer> pair : usersInfo.entrySet()) {
            if ( pair.getKey().Nickname.toLowerCase().equals(nickname.toLowerCase()) )
                unique = false;
        }
        return unique;
    }

    public static void init(){
        usersInfo = new HashMap<>();
        if (!cacheFileExist())
            return;

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, cacheFileName);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ( (line = reader.readLine()) != null ){
                String[] params = line.split(";");
                if ( params != null && params.length >= 5 ) {
                    User user = new User(params[0], params[1], Gender.fromString(params[2]), params[3]);
                    int level = Integer.parseInt( params[4] );
                    usersInfo.put(user,level);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static String[] getUsers(){
        ArrayList<String> users = new ArrayList<>();
        for (Map.Entry<User, Integer> pair : usersInfo.entrySet()) {
            users.add( pair.getKey().Nickname );
        }

        String[] result = new String[users.size()];
        result = users.toArray(result);
        return result;
    }

    public static int getUsersLevel( String nickname ){
        for (Map.Entry<User, Integer> pair : usersInfo.entrySet()) {
            if ( pair.getKey().Nickname.toLowerCase().equals(nickname.toLowerCase()) )
                return pair.getValue();
        }
        return 1;
    }

    public static void inrementUsersLevel( String nickname ){
        for (Map.Entry<User, Integer> pair : usersInfo.entrySet()) {
            if ( pair.getKey().Nickname.toLowerCase().equals(nickname.toLowerCase()) )
                pair.setValue( pair.getValue() + 1 );
        }
        save();
    }

    private static void save(){
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, cacheFileName);
        file.delete();

        createCacheFile();
        for (Map.Entry<User, Integer> pair : usersInfo.entrySet()) {
            appendToCacheFile( pair.getKey(), pair.getValue() );
        }
    }
    
    private static boolean cacheFileExist(){
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, cacheFileName);
        return file.exists();
    }

    private static boolean appendToCacheFile( User user, int level ){
        String fileContents = user.Name + ";" + user.Surname + ";" + user.Gender.toString() + ";" + user.Nickname + ";" + level + ";";
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, cacheFileName);

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter( new FileWriter(file.getAbsoluteFile(), true)));
            writer.append(fileContents + "\n" );
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean createCacheFile(){
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File( sdcard, "MovieTime" );
            dir.mkdir();

            File file = new File(sdcard, cacheFileName);

            file.createNewFile();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
