package com.example.proiectechiparacheta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<FidelityCard> fromJson(String json) {
        try {
            JSONArray array = new JSONArray(json);
            return citesteCarduri(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<FidelityCard> citesteCarduri(JSONArray array) throws JSONException {
        List<FidelityCard> carduri = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            FidelityCard card = citesteCard(array.getJSONObject(i));
            carduri.add(card);
        }
        return carduri;
    }

    private static FidelityCard citesteCard(JSONObject object) throws JSONException {

        int id = object.getInt("id");
        String name = object.getString("name");
        JSONObject attributes = object.getJSONObject("attributes");
        String cardHolderName = attributes.getString("cardHolderName");
        String barcodeValue = attributes.getString("barcodeValue");

        FidelityCard card = new FidelityCard(id,name,cardHolderName,barcodeValue);
        return card;
    }
}
