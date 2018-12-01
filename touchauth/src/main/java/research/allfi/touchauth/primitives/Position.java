package research.allfi.touchauth.primitives;

import android.hardware.SensorEvent;

public class Position {
    public float RotationX;
    public float RotationY;
    public float RotationZ;
    public float Scalar;
    public long Timestamp;

    public Position( float rotationX, float rotationY, float rotationZ, float scalar, long timestamp ){
        this.RotationX = rotationX;
        this.RotationY = rotationY;
        this.RotationZ = rotationZ;
        this.Scalar = scalar;
        this.Timestamp = timestamp;
    }

    public static Position fromSensorEvent(SensorEvent event, long timeInMillis){
        return new Position(event.values[0],event.values[1], event.values[2], event.values[3], timeInMillis);
    }

    public static String getCSVHeader(){
        return "RotationX;RotationY;RotationZ;ScalarRotation;";
    }

    public String toCSVString(){
        return String.format( "%f;%f;%f;%f;", RotationX, RotationY, RotationZ, Scalar );
    }
}
