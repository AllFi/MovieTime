package research.allfi.touchauth.collection;

import java.util.ArrayList;
import java.util.List;

import research.allfi.touchauth.primitives.Position;

public class PositionQueue {
    private List<Position> positions;
    private int maxSize = 10;

    public PositionQueue(){
        positions = new ArrayList<>();
    }

    public void add( Position position ){
        positions.add(position);
        if ( positions.size() > maxSize ){
            positions = positions.subList( positions.size() - maxSize, maxSize );
        }
    }

    public Position findSuitable( long timestamp ){
        Position suitablePosition = null;
        for ( int i = positions.size() - 1; i >= 0; i-- ){
            if ( positions.get(i).Timestamp <= timestamp ){
                suitablePosition = positions.get(i);
                break;
            }
        }
        return suitablePosition;
    }
}
