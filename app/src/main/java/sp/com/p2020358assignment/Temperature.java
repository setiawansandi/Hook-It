package sp.com.p2020358assignment;

public class Temperature {
    private double temp;
    private double lat;
    private double lon;

    public Temperature(double temp, double lat, double lon) {
        this.temp = temp;
        this.lat = lat;
        this.lon = lon;
    }

    public double getTemp() {
        return temp;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
