package research.allfi.movietime.users;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String str;

    Gender( String s ){
        str = s;
    }

    public String toString(){
        return str;
    }

    public static Gender fromString( String s ){
        return s.toLowerCase().equals("male") ? MALE : FEMALE;
    }
}
