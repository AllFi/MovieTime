package research.allfi.touchauth.primitives;

import java.util.ArrayList;
import java.util.List;

public class Gesture {
    public List<List<Touch>> Touches;

    public Gesture(){
        Touches = new ArrayList<List<Touch>>();
        for (int i =0; i<2; i++){
            Touches.add(null);
        }
    }

    public void addPoint(int touchId, double x, double y, double size, long timestamp, Position position){
        if ( Touches.get(touchId) == null){
            Touches.set(touchId, new ArrayList<Touch>());
            Touches.get(touchId).add(new Touch(x,y,size,timestamp,position));
        } else{
            Touches.get(touchId).add(new Touch(x,y,size,timestamp, position));
        }
        return;
    }

    public GestureType gestureType(){
        List<Touch> touches = Touches.get(0);
        double deltaX = touches.get(touches.size()-1).X - touches.get(0).X;
        double deltaY = touches.get(touches.size()-1).Y - touches.get(0).Y;

        if ( Math.abs(deltaY) > Math.abs(deltaX) ){
            if (deltaY > 0){
                return GestureType.UP_TO_DOWN;
            } else{
                return GestureType.DOWN_TO_UP;
            }
        } else{
            if (deltaX > 0){
                return GestureType.RIGHT_TO_LEFT;
            } else{
                return GestureType.LEFT_TO_RIGHT;
            }
        }
    }
}
