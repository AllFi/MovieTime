package research.allfi.touchauth.primitives;

public enum GestureType {
    DOWN_TO_UP("DownToUp"), UP_TO_DOWN("UpToDown"), LEFT_TO_RIGHT("LeftToRight"), RIGHT_TO_LEFT("RightToLeft");

    private final String value;
    GestureType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static GestureType fromString(String string){
        switch(string){
            case "DownToUp":
                return DOWN_TO_UP;
            case "UpToDown":
                return UP_TO_DOWN;
            case "LeftToRight":
                return LEFT_TO_RIGHT;
            case "RightToLeft":
                return RIGHT_TO_LEFT;
        }
        return DOWN_TO_UP;
    }
}
