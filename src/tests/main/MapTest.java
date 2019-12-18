package main;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MapTest {
    Map map;

    @Before
    public void setUp(){
        map = new Map();
        map.addPath("A","B", 10);
        map.addPath("A","C", 11);
        map.addPath("A","D", 12);

        map.addPath("B","C", 20);
        map.addPath("C","D", 15);

        map.addPath("B","E", 7);
        map.addPath("C","E", 5);
        map.addPath("D","E", 6);
    }

    @Test
    public void getAllPlaces(){
        ArrayList<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");
        expected.add("E");
        ArrayList<String> actual = map.getAllPlaces();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void generateShortPath1() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("C");
        expected.add("E");
        ArrayList<String> actual = map.generateShortPath("A", "E");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void generateShortPath2() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("C");
        expected.add("E");
        expected.add("D");
        ArrayList<String> actual = map.generateShortPath("C", "D");
        Assert.assertEquals(expected, actual);
    }
}