package main;
import java.io.*;
import java.util.ArrayList;

public class PizzaSystem {
    private Map map;
    private ArrayList<User> users;
    private User current_user;

    public PizzaSystem(){
        this.map = new Map();
        this.users = new ArrayList<>();
        this.current_user = null;
        this.addUser("Admin", "Admin", "Admin", "Admin", 0);
    }

    private boolean userExists(String login){
        for (User user: users){
            if (user.login.equals(login)){
                return true;
            }
        }
        return false;
    }

    public boolean addUser(String name, String login, String password, String repeat, int type){
        if (!password.equals(repeat)){
            return false;
        }
        if (userExists(login)){
            return false;
        }
        if (!(type == 0 || type == 1)){
            return false;
        }
        if (type == 0){
            User admin = new Admin(name, login, password);
            this.users.add(admin);
            return true;
        }
        else {
            User client = new Client(name, login, password);
            this.users.add(client);
            return true;
        }
    }

    public User login(String login, String password){
        for (User user: users){
            if ((user.login.equals(login))&&(user.password.equals(password))){
                this.current_user = user;
                return user;
            }
        }
        return null;
    }

    public void exit(){
        this.current_user = null;
    }

    public String changePlace(String place){
        if (current_user.getClass() != Client.class){
            return "error";
        }
        if (place.isEmpty()){
            return "error";
        }
        if (map.getAllPlaces().contains(place)){
            current_user.setPlace(place);
            return "success";
        }
        else{
            return "unknown place";
        }
    }

    public String addPath(String place1, String place2, int time){
        if (this.current_user.getClass() == Admin.class){
            return map.addPath(place1, place2, time);
        }
        else{
            return "access error";
        }
    }

    public String getPathTo(String target){
        if (this.current_user.getClass() == Client.class){
            String curPlace = current_user.getPlace();
            if (map.getAllPlaces().contains(curPlace)){
                return placesToString(map.generateShortPath(curPlace, target));
            }
        }
        return null;
    }

    private String placesToString(ArrayList<String> places){
        if (places.size() <= 0){
            return "";
        }
        String placesStr = "";
        for (String place : places){
            placesStr += place + ", ";
        }
        placesStr = placesStr.substring(0, placesStr.length() - 2);
        return placesStr;
    }

    public String getAllPlaces(){
        return placesToString(map.getAllPlaces());
    }

    public Map getMap(){
        return map;
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    private ArrayList<String> readFile(String fileName){
        ArrayList<String> strings = new ArrayList<>();
        File file = new File(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String string;
                while ((string = in.readLine()) != null) {
                    strings.add(string);
                }
            } finally {
                in.close();
            }
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        return strings;
    }

    private boolean exists(String fileName){
        File file = new File(fileName);
        if (!file.exists()){
            return false;
        }
        return true;
    }

    private void saveMap(){
        try (FileWriter writer = new FileWriter("map_save.txt", false)) {
            ArrayList<String> paths = map.getPaths();
            for (String path: paths){
                writer.write(path);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Map loadMap(){
        ArrayList<String> fileStr;
        if (exists("map_save.txt")){
            fileStr = readFile("map_save.txt");
        }
        else{
            return null;
        }
        Map newMap = new Map();
        for (String string: fileStr){
            String[] params = string.split(" ");

            if (params.length != 3) return null;
            try {
                Integer.valueOf(params[2]);
            } catch (NumberFormatException e){
                return null;
            }
            newMap.addPath(params[0], params[1], Integer.parseInt(params[2]));
        }
        return newMap;
    }

    private void saveUsers(){
        try (FileWriter writer = new FileWriter("users_save.txt", false)) {
            for (User user: users){
                String type = "1";
                if (user.getClass() == Admin.class) {
                    type = "0";
                }
                writer.write(user.name + " " + user.login + " " + user.password + " " + type);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private ArrayList<User> loadUsers(){
        ArrayList<String> fileStr;
        if (exists("users_save.txt")){
            fileStr = readFile("users_save.txt");
        }
        else{
            return null;
        }
        ArrayList<User> newUsers = new ArrayList<>();
        for (String string: fileStr){
            String[] params = string.split(" ");

            if (params.length != 4) return null;
            try {
                Integer.valueOf(params[3]);
            } catch (NumberFormatException e){
                return null;
            }

            if (Integer.parseInt(params[3]) == 1){
                User user = new Client(params[0], params[1], params[2]);
                newUsers.add(user);
            }
            else if (Integer.parseInt(params[3]) == 0){
                User user = new Admin(params[0], params[1], params[2]);
                newUsers.add(user);
            }
            else{
                return null;
            }
        }
        return newUsers;
    }

    public void save() {
        saveMap();
        saveUsers();
    }

    public ArrayList<String> load(){
        map = loadMap();
        users = loadUsers();
        ArrayList<String> result = new ArrayList<>();
        if (map == null){
            result.add("map");
        }
        if (users == null){
            result.add("users");
        }
        return result;
    }
}