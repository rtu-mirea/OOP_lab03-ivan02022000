package main;

public class Path {
    public String[] points;
    public int time;

    public Path(String[] points, int time){
        this.points = points;
        this.time = time;
    }

    public boolean contains(String place){
        return (this.points[0].equals(place) ) || (this.points[1].equals(place));
    }

    public String oppositePoint(String point){
        if (this.points[0].equals(point)) {
            return this.points[1];
        }
        else {
            return this.points[0];
        }
    }

    public String toString(){
        return this.points[0] + " " + this.points[1] + " " + this.time;
    }
}