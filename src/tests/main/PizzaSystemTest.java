package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PizzaSystemTest {
    public PizzaSystem system;

    @Before
    public void before(){
        system = new PizzaSystem();
    }

    @Test
    public void adminTest(){
        assertNotNull(system.login("Admin", "Admin"));
    }

    @Test
    public void adminTestAdd(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        system.addPath("A", "C", 10);
        Assert.assertEquals(system.getAllPlaces(), "A, B, C");
    }

    @Test
    public void adminTestChange(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        Assert.assertEquals(system.addPath("A", "B", 8), "changed");
    }

    @Test
    public void adminTestAddError(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        Assert.assertEquals(system.addPath("A", "A", 8), "error");
    }

    @Test
    public void registerTest(){
        Assert.assertTrue(system.addUser("Ivan", "Ivan2000", "1234", "1234", 0));
    }

    @Test
    public void registerErrorTest1(){
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 0);
        Assert.assertFalse(system.addUser("Ivan", "Ivan2000", "1234", "1234", 0));
    }

    @Test
    public void registerErrorTest2(){
        Assert.assertFalse(system.addUser("Ivan", "Ivan2000", "1234", "123", 1));
    }

    @Test
    public void loginTest(){
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        assertNotNull(system.login("Ivan2000", "1234"));
    }

    @Test
    public void loginTest2(){
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        User user1 = system.login("Admin", "Admin");
        system.exit();
        User user2 = system.login("Ivan2000", "1234");
        assertNotSame(user1, user2);
    }

    @Test
    public void loginTest3(){
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        User user1 = system.login("Admin", "Admin");
        system.exit();
        User user2 = system.login("Ivan2000", "1234");
        Assert.assertEquals(Client.class, user2.getClass());
    }


    @Test
    public void loginTestError(){
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        assertNull(system.login("Ivan2000", "123"));
    }

    @Test
    public void findWay(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        system.addPath("A", "C", 5);
        system.addPath("A", "D", 4);

        system.addPath("B", "E", 1);
        system.addPath("C", "E", 2);
        system.addPath("D", "E", 5);
        system.exit();

        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        system.login("Ivan2000", "1234");
        system.changePlace("B");
        system.getPathTo("A");
        Assert.assertEquals("B, E, C, A", system.getPathTo("A"));
    }

    @Test
    public void findWay1(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        system.addPath("A", "C", 5);

        system.addPath("B", "D", 2);
        system.addPath("C", "D", 5);
        system.exit();

        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        system.login("Ivan2000", "1234");
        system.changePlace("A");
        system.getPathTo("D");
        Assert.assertEquals("A, C, D", system.getPathTo("D"));
    }

    @Test
    public void saveMap(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        system.addPath("A", "C", 5);

        system.addPath("B", "D", 2);
        system.addPath("C", "D", 5);
        system.exit();

        ArrayList<String> expectedMap = system.getMap().getPaths();

        system.save();
        system.load();
        ArrayList<String>  curMap = system.getMap().getPaths();
        Assert.assertEquals(expectedMap , curMap);
    }

    private String userToStr(User user){
        String type = "1";
        if (user.getClass() == Admin.class){
            type = "0";
        }
        return user.name + " " + user.login + " " + user.password + " " + type;
    }

    private ArrayList<String> usersToStr(ArrayList<User> users){
        ArrayList<String> usersStr = new ArrayList<>();
        for (User user: users){
            usersStr.add(userToStr(user));
        }
        return usersStr;
    }

    @Test
    public void saveUsers(){
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        system.addUser("Ivan", "IvanOs", "1234", "1234", 1);

        ArrayList<User> expectedUsers = system.getUsers();

        system.save();
        system.load();
        Assert.assertEquals(usersToStr(expectedUsers) , usersToStr(system.getUsers()));
    }

    @Test
    public void saveMapUsers(){
        system.login("Admin", "Admin");
        system.addPath("A", "B", 10);
        system.addPath("A", "C", 5);

        system.addPath("B", "D", 2);
        system.addPath("C", "D", 5);
        system.exit();
        system.addUser("Ivan", "Ivan2000", "1234", "1234", 1);
        system.addUser("Ivan", "IvanOs", "1234", "1234", 1);

        ArrayList<String> expected = system.getMap().getPaths();
        expected.addAll(usersToStr(system.getUsers()));

        system.save();
        system.load();

        ArrayList<String> current = system.getMap().getPaths();
        current.addAll(usersToStr(system.getUsers()));

        Assert.assertEquals(expected , current);
    }
}