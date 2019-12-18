package main;

public class Client extends User{
    private String place;

    public Client(String name, String login, String password){
        super(name, login, password);
        this.place = "";
    }

    public void setPlace(String place){
        this.place = place;
    }

    public String getPlace(){
        return place;
    }
}
