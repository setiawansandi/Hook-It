package sp.com.p2020358assignment;

public class Weather {
    private String areaName;
    private double lat;
    private double lon;
    private String forecast;

    public Weather(){
    }

    public Weather(String areaName, double lat, double lon, String forecast) {
        this.areaName = areaName;
        this.lat = lat;
        this.lon = lon;
        this.forecast = forecast;
    }

    public String getAreaName() {
        return areaName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getForecast() {
        return forecast;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "areaName='" + areaName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", forecast='" + forecast + '\'' +
                '}';
    }
}
