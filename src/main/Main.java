package main;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static PizzaSystem system;

    private static boolean isInt(String string){
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private static void addPath(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите название пункта 1");
        String place1 = input.nextLine();
        System.out.println("Введите название пункта 2");
        String place2 = input.nextLine();
        System.out.println("Введите время перемещения между пунктами 1 и 2");
        String time = "";
        while (!isInt(time)){
            time = input.nextLine();
            if (!isInt(time)){
                System.out.println("Введите целое число");
            }
        }
        switch (system.addPath(place1, place2, Integer.parseInt(time))){
            case ("error"):
                System.out.println("Ошибка, путь не добавлен!");
                break;
            case ("changed"):
                System.out.println("Путь успешно изменён!");
                break;
            case ("added"):
                System.out.println("Путь успешно добавлен!");
                break;
        }
    }
    private static void adminMenu(){
        System.out.println("Меню администратра:\n" +
                "Добавить/изменить маршрут - 1 \n" +
                "Показать все места        - 2 \n" +
                "Выход                     - 0 \n" +
                "Показать меню             - help");
    }
    private static void adminChose(){
        Scanner input = new Scanner(System.in);
        String choice = "";
        while (!choice.equals("0")){
            choice = input.nextLine();
            switch (choice) {
                case ("1"):
                    addPath();
                    break;
                case("2"):
                    System.out.println(system.getAllPlaces());
                    break;
                case("0"):
                    system.exit();
                    mainMenu();
                    break;
                case("help"):
                    adminMenu();
                    break;
                default:
                    System.out.println("Ошибка ввода");
                    break;
            }
        }
    }
    private static void adminProfile(User user){
        System.out.println("Добро пожаловать " + user.name + "!");
        System.out.println("Вы имеете права администратора.");
        adminMenu();
        adminChose();
    }

    private static void changePlace(User user){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите название пункта");
        String place = input.nextLine();
        switch (system.changePlace(place)){
            case ("error"):
                System.out.println("Ошибка!");
                break;
            case("success"):
                System.out.println("Успешно изменено!");
                break;
            case ("unknown place"):
                System.out.println("Введено неизвестное место!");
                break;
        }
    }
    private static void findPath(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите название пункта назначения");
        String place = input.nextLine();
        String way  = system.getPathTo(place);
        if (way == null){
            System.out.println("Ошибка нахождения маршрута!");
        }
        else {
            System.out.println("Найденный маршрут: " + way);
        }
    }
    private static void clientMenu(){
        System.out.println("Меню клиента:\n" +
                "Изменить местоположение   - 1 \n" +
                "Показать все места        - 2 \n" +
                "Проложить маршрут         - 3 \n" +
                "Выход                     - 0 \n" +
                "Показать меню             - help");
    }
    private static void clientChose(User user){
        Scanner input = new Scanner(System.in);
        String choice = "";
        while (!choice.equals("0")){
            choice = input.nextLine();
            switch (choice) {
                case ("1"):
                    changePlace(user);
                    break;
                case("2"):
                    System.out.println(system.getAllPlaces());
                    break;
                case("3"):
                    findPath();
                    break;
                case("0"):
                    system.exit();
                    mainMenu();
                    break;
                case("help"):
                    clientMenu();
                    break;
                default:
                    System.out.println("Ошибка ввода");
                    break;
            }
        }
    }
    private static void clientProfile(User user){
        System.out.println("Добро пожаловать " + user.name + "!");
        System.out.println("Ваше текущее местоположение: " + user.getPlace());
        clientMenu();
        clientChose(user);
    }

    private static void register(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите имя");
        String name = input.nextLine();
        System.out.println("Введите логин");
        String login = input.nextLine();
        System.out.println("Введите пароль");
        String password = input.nextLine();
        System.out.println("Повторите пароль");
        String repeat = input.nextLine();
        if (system.addUser(name,login, password, repeat, 1)){
            System.out.println("Успешная регистрация!");
        }
        else {
            System.out.println("Ошибка регистрации!");
        }
    }

    private static void login(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите логин");
        String login = input.nextLine();
        System.out.println("Введите пароль");
        String password = input.nextLine();
        User user = system.login(login, password);
        if (user != null){
            if (user.getClass() == Admin.class){
                adminProfile(user);
            }
            else{
                clientProfile(user);
            }
        }
        else{
            System.out.println("Пользователь не найден!");
        }
    }

    private static void mainMenu(){
        System.out.println("Главное меню:\n" +
                "Регистрация       - 1 \n" +
                "Вход в систему    - 2 \n" +
                "Закрыть программу - 0 \n" +
                "Показать меню     - help");
    }
    private static void mainChose(){
        Scanner input = new Scanner(System.in);
        String choice = "";
        while (!choice.equals("0")){
            choice = input.nextLine();
            switch (choice) {
                case ("1"):
                    register();
                    break;
                case("2"):
                    login();
                    break;
                case("0"):
                    break;
                case("help"):
                    mainMenu();
                    break;
                default:
                    System.out.println("Ошибка ввода");
            }
        }
    }
    public static void main(String[] args) {
        system = new PizzaSystem();
        ArrayList<String> loadRes = system.load();
        if (loadRes.contains("map")){
            System.out.println("Ошибка загрузки карты!");
        }
        if (loadRes.contains("users")){
            System.out.println("Ошибка загрузки пользователей!");
        }
        mainMenu();
        mainChose();
        system.save();
    }
}
