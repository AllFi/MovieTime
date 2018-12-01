package research.allfi.touchauth.collection;

import java.util.HashMap;
import java.util.Map;

import research.allfi.touchauth.primitives.Gesture;
import research.allfi.touchauth.primitives.GestureType;

public class TouchRepository {
    private String userNameAndLevel;
    private String basePath = "MovieTime/CollectedData";
    private Map<GestureType, GesturesBufferedStore> gesturesStores = new HashMap<>();

    public TouchRepository(String userNameAndLevel){
        this.userNameAndLevel = userNameAndLevel;
    }

    public void put( Gesture gesture ){
        GesturesBufferedStore store;
        GestureType type = gesture.gestureType();
        if (!gesturesStores.containsKey(type)){
            store = new GesturesBufferedStore(type, basePath + "/" + userNameAndLevel);
            gesturesStores.put(type, store);
        } else{
            store = gesturesStores.get(type);
        }

        store.put(gesture);
    }

    public void saveAll(){
        for (GesturesBufferedStore store: gesturesStores.values()) {
            store.save();
        }
    }
}
