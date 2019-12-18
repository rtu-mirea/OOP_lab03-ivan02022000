package main;

import java.util.ArrayList;

public class Map {
    private ArrayList<Path> paths;
    private ArrayList<Way> ways_to_place;

    private class Way {
        public ArrayList<String> places;
        public int time;

        public Way(String place){
            this.places = new ArrayList<>();
            this.places.add(place);
            this.time = 0;
        }

        public Way(Way way){
            this.places = new ArrayList<>(way.places);
            this.time = way.time;
        }

        public void add(String place, int time){
            this.places.add(place);
            this.time += time;
        }

        public boolean contains(String place){
            for (String name: places){
                if (name.equals(place)){
                    return true;
                }
            }
            return false;
        }

        public String lastPlace(){
            return places.get(places.size() - 1);
        }
    }

    public String addPath(String place1, String place2, int time){
        if (place1.equals(place2) || time <= 0){
            return "error";
        }

        for (Path path: paths){
            if (path.contains(place1) && path.contains(place2)){
                path.time = time;
                return "changed";
            }
        }

        String[] places = new String[2];
        places[0] = place1;
        places[1] = place2;
        Path newPath = new Path(places, time);
        this.paths.add(newPath);

        return "added";
    }

    public String delPath(String place1, String place2){
        if (place1.equals(place2)){
            return "error";
        }
        for (Path path: paths){
            if (path.contains(place1) && path.contains(place2)){
                paths.remove(path);
                return "deleted";
            }
        }
        return "error";
    }

    public ArrayList<String> getAllPlaces(){
        ArrayList<String> places = new ArrayList<>();
        for (Path path: this.paths) {
            for (String place: path.points){
                if (!places.contains(place)){
                    places.add(place);
                }
            }
        }
        return places;
    }


    public ArrayList<String> getPaths(){
        ArrayList<String> pathsStr = new ArrayList<>();
        for (Path path:  paths){
            pathsStr.add(path.toString());
        }
        return pathsStr;
    }


    private ArrayList<Path> findPathsFromPlace(String place){
        ArrayList<Path> paths_from_place = new ArrayList<>();
        for (Path path: this.paths) {
            if (path.contains(place)){
                paths_from_place.add(path);
            }
        }
        return paths_from_place;
    }

    private void findWays(Way cur_way, String target){
        String lastPlace = cur_way.lastPlace();
        if (lastPlace.equals(target)){
            ways_to_place.add(cur_way);
        }
        else
        {
            ArrayList<Path> paths_from_last_place = findPathsFromPlace(lastPlace);
            for (Path path:  paths_from_last_place){
                String new_point = path.oppositePoint(lastPlace);
                if (!cur_way.contains(new_point)){
                    Way new_way = new Way(cur_way);
                    new_way.add(new_point, path.time);
                    this.findWays(new_way, target);
                }
            }
        }
    }

    private ArrayList<String> shortestWay(){
        if (ways_to_place.size() <= 0){
            return null;
        }
    int min_time = ways_to_place.get(0).time;
    Way min_way = new Way("");
        for (Way way: ways_to_place){
        if (way.time <= min_time){
            min_time = way.time;
            min_way = way;
        }
    }
        return min_way.places;
}

    public ArrayList<String> generateShortPath(String place1, String place2){
        Way cur_way = new Way(place1);
        findWays(cur_way, place2);
        ArrayList<String> shortestWay = this.shortestWay();
        this.ways_to_place = new ArrayList<>();
        return shortestWay;
    }

    public Map(){
        this.paths = new ArrayList<>();
        this.ways_to_place = new ArrayList<>();
    }
}