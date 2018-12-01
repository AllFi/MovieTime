package research.allfi.touchauth.collection;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import research.allfi.touchauth.primitives.Gesture;
import research.allfi.touchauth.primitives.GestureType;
import research.allfi.touchauth.primitives.Touch;

// Осторожно! Legacy Code
class GesturesBufferedStore {
    private String LOG_TAG = "GesturesStore";

    private String storeFileName;
    private String storeFolder;
    private static long maxNumberOfGesturesInMemory = 10;

    private String buffer = "";
    private long numberOfGesturesInMemory;
    private long numberOfStoredGestures;

    public GesturesBufferedStore(GestureType type, String relativePath) {
        storeFolder = relativePath;
        storeFileName = "touches_" + type.toString() + ".csv";
        numberOfGesturesInMemory = 0;

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + storeFolder);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, storeFileName);
        if ( !sdFile.exists() ){
            numberOfStoredGestures = 0;
            buffer +=  "GestureId;TouchId;" + Touch.getCSVHeader() + "\n";
        }
        else{
            numberOfStoredGestures = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(sdFile.getPath()));
                //скипаем хэдер
                br.readLine();
                String line = br.readLine();
                while (line != null) {
                    numberOfStoredGestures = Long.parseLong(line.split(";")[0]);
                    line = br.readLine();
                }
                numberOfStoredGestures++;
                br.close();
            } catch(IOException exception){
                numberOfStoredGestures = 0;
            }
            Log.d(LOG_TAG, "Количество накопленных жестов: " + numberOfStoredGestures);
        }
    }

    // кладет жест в хранилище определенного типа жестов
    public boolean put(Gesture gesture){
        long GestureId = numberOfStoredGestures + numberOfGesturesInMemory;
        for (int i=0; i<2; i++){
            if (gesture.Touches.get(i) != null){
                for (int j = 0; j < gesture.Touches.get(i).size(); j++){
                    buffer += GestureId + ";" + String.valueOf(i) +  ";" + gesture.Touches.get(i).get(j).toCSVString() + "\n";
                }
            } else{
                break;
            }
        }
        numberOfGesturesInMemory++;
        if (numberOfGesturesInMemory >= maxNumberOfGesturesInMemory){
            save();
        }
        return true;
    }

    // сохраняет данные определенного типа на диск
    public void save() {
        if (numberOfGesturesInMemory == 0){
            return;
        }

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + storeFolder);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, storeFileName);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile, true));
            PrintWriter pw = new PrintWriter(bw);
            pw.print(buffer);
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            numberOfStoredGestures += numberOfGesturesInMemory;
            numberOfGesturesInMemory = 0;
            buffer = "";
            pw.close();
        } catch (IOException ex) {
            Log.d(LOG_TAG, "Не удалось сохранить файл на SD с ошибкой: " + ex.getLocalizedMessage());
        }
    }
}

