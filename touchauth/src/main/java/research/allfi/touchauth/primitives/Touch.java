package research.allfi.touchauth.primitives;

public class Touch {
    public double X;
    public double Y;
    public double Size;
    public long Time;
    public Position Position;

    public Touch(double x, double y, double size, long time, Position position){
        X = x;
        Y = y;
        Size = size;
        Time = time;
        Position = position;
    }

    public static String getCSVHeader(){
        return "X;Y;Size;Time;" + research.allfi.touchauth.primitives.Position.getCSVHeader();
    }

    public String toCSVString(){
        return String.format( "%f;%f;%f;%d;", X, Y, Size, Time) + Position.toCSVString();
    }
}
