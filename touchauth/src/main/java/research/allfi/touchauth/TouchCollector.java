package research.allfi.touchauth;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import research.allfi.touchauth.collection.TouchRepository;
import research.allfi.touchauth.primitives.Gesture;
import research.allfi.touchauth.primitives.Position;
import research.allfi.touchauth.collection.PositionQueue;

public class TouchCollector {
    private PositionQueue positionQueue;
    private Gesture curGesture;
    private TouchRepository repository;

    public TouchCollector(String userNameAndLevel) {
        positionQueue = new PositionQueue();
        repository = new TouchRepository( userNameAndLevel );
    }

    public void onMotionEvent(MotionEvent event){
        Position position = positionQueue.findSuitable(System.currentTimeMillis());

        int actionMask = event.getActionMasked();
        int pointerCount = event.getPointerCount();

        switch (actionMask) {
            case MotionEvent.ACTION_DOWN: // первое касание
                curGesture = new Gesture();
                curGesture.addPoint(event.getPointerId(0),
                        event.getX(0),
                        event.getY(0),
                        event.getSize(0),
                        System.currentTimeMillis(),
                        position);
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                for (int i = 0; i < pointerCount; i++) {
                    curGesture.addPoint(event.getPointerId(i),
                            event.getX(i),
                            event.getY(i),
                            event.getSize(i),
                            System.currentTimeMillis(),
                            position);
                }
                break;
            case MotionEvent.ACTION_UP: // прерывание последнего касания
                if ( curGesture.Touches.get(0).size() > 0 )
                    repository.put(curGesture);
                return;
            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                break;
            case MotionEvent.ACTION_MOVE: // движение
                for (int i = 0; i < pointerCount; i++) {
                    curGesture.addPoint(event.getPointerId(i),
                            event.getX(i),
                            event.getY(i),
                            event.getSize(i),
                            System.currentTimeMillis(),
                            position);
                }
                break;
        }
        return;
    }

    public void onSensorEvent(SensorEvent event){
        if ( event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR )
            return;
        positionQueue.add(Position.fromSensorEvent(event, System.currentTimeMillis()));
    }

    public void saveAll(){
        repository.saveAll();
    }
}
