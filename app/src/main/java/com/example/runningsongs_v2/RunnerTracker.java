package com.example.runningsongs_v2;


import java.io.Serializable;
import java.util.List;

/**  Klasa reprezentująca pojedynczą sesję treningowa
 *
 */

public class RunnerTracker implements Serializable {

    private int _id;  /**< \Unikatowy identyfikator */
    private double _distance;  /**< \Dystans */
    private String _Date;  /**< \Data */
    private String _time;  /**< \Czas trwania */
    private List<GeoStamp> _geoStamps;  /**< \Lista obiektów GeoStamp */
    private List<SongStamp> _songStamps;  /**< \Lista obiektów SongStamp */

    public RunnerTracker() {

    }

    /** \brief Konstruktor parametryczny tworzący obiekt na podstawie parametrów.
     *
     * Tu trochę bardziej wymyślny opis
     * @param id Identyfikator
     * @param distance Dystans
     * @param date Data
     * @param time Czas trwania
     */

    public RunnerTracker(int id, double distance, String date,String time) {
        this._id = id;
        this._distance = distance;
        this._Date = date;
        this._time=time;
    }

    /** \brief Konstruktor parametryczny tworzący obiekt na podstawie parametrów.
     *
     * Tu trochę bardziej wymyślny opis
     * @param distance Dystans
     * @param date Data
     * @param time Czas trwania
     */

    public RunnerTracker(double distance, String date,String time) {
        this._distance = distance;
        this._Date = date;
        this._time=time;
    }

    /** \brief Konstruktor parametryczny tworzący obiekt na podstawie parametrów.
     *
     * Tu trochę bardziej wymyślny opis
     * @param distance Dystans
     * @param date Data
     * @param time Czas trwania
     * @param songStamps Utwory
     * @param geoStamps Pozycje
     */

    public RunnerTracker(double distance, String date, String time, List<SongStamp> songStamps, List<GeoStamp> geoStamps) {
        this._distance = distance;
        this._Date = date;
        this._time=time;
        _songStamps = songStamps;
        _geoStamps = geoStamps;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setRunnerTrackerDistance(int distance) {
        this._distance = distance;
    }

    public double getRunnerTrackerDistance() {
        return this._distance;
    }
    public void setRunnerTrackerTime(String time) {
        this._time= time;
    }

    public String getRunnerTrackerTime() {
        return this._time;
    }

    public void setRunnerTrackerDate(String date ) {
        this._Date = date;
    }

    public String getRunnerTrackerDate() {
        return this._Date;
    }

    public List<SongStamp> getSongStamps() { return this._songStamps; }

    public List<GeoStamp> getGeoStamps() { return this._geoStamps; }
}
