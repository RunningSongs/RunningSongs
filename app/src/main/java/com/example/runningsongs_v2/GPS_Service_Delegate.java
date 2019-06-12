package com.example.runningsongs_v2;

import com.google.android.gms.maps.model.LatLng;

/**  Klasa reprezentująca serwis GPS pobierający aktualną pozycję użytkownika
 *
 */

public interface GPS_Service_Delegate {
    void newLocationReceived(LatLng location);
}
