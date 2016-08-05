package com.isatimur.poroute.service;
//A1IzaSyDNqSWwekGZ6fIxKYFG8Eb2uDf7DRI4l51Q
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by developer on 8/4/16.
 */
@Service("commands")
public class CommandsService {

    public List<String> getCommandsByLocation(WebSocketSession session, LatLng location) {
        final ArrayList<String> resultCommands = new ArrayList<>();
        GeoApiContext context = new GeoApiContext().setApiKey("");

        DirectionsApiRequest request = DirectionsApi.newRequest(context)
            .origin(location)
            .destination("40.768608, -73.985125")
            .mode(TravelMode.WALKING)
            .units(Unit.METRIC);
        request.setCallback(new PendingResult.Callback<DirectionsResult>() {

            @Override public void onResult(DirectionsResult result) {
                String resultMsg = Arrays.stream(result.routes)
                    .flatMap(i -> Arrays.stream(i.legs))
                    .flatMap(i -> Arrays.stream(i.steps))
                    .peek(p -> System.out.println(p.htmlInstructions + " : " + p.distance + " : " + p.duration + " : " + p.startLocation + " : " + p.endLocation))
                    .map(p -> p.htmlInstructions + " : " + p.distance + " : " + p.duration + " : " + p.startLocation + " : " + p.endLocation)
                    .collect(Collectors.joining("||"));

                try {
                    session.sendMessage(new TextMessage(resultMsg));

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override public void onFailure(Throwable throwable) {
                System.out.println("Failure");
            }

        });
        return resultCommands;
    }

    private enum DIRECTION_ALIAS {
        LEFT("A"),
        RIGHT("D"),
        NORTH("W"),
        SOUTH("S"),
        SOUTHWEST("SA"),
        SOUTHEAST("SD"),
        NORTHWEST("WA"),
        NORTHEAST("WD");

        private String keyboardSymbol;

        DIRECTION_ALIAS(String keyboardSymbol) {
            this.keyboardSymbol = keyboardSymbol;
        }

        public String getKeyboardSymbol() {
            return keyboardSymbol;
        }
    }

}
