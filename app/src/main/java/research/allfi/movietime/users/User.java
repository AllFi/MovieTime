package research.allfi.movietime.users;

public class User {
    public String Name;
    public String Surname;
    public Gender Gender;
    public String Nickname;

    public User( String name, String surname, Gender gender, String nickname ){
        Name = name;
        Surname = surname;
        Gender = gender;
        Nickname = nickname;
    }
}
