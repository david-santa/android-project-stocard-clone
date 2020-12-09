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
//        int idTest = object.getInt("id");
//        int idCentru = Integer.parseInt(object.getJSONObject("centru").getString("idCentru"));
//        int nrDoctori = Integer.parseInt(object.getJSONObject("centru").getString("numarDoctori"));
//        float pretTest = (float)object.getJSONObject("centru").getLong("pretTest");
//        String strada = object.getJSONObject("centru").getJSONObject("contactCentru").getString("strada");
//        int numar = object.getJSONObject("centru").getJSONObject("contactCentru").getInt("numar");
//        String email = object.getJSONObject("centru").getJSONObject("contactCentru").getString("email");
//        String telefon = object.getJSONObject("centru").getJSONObject("contactCentru").getString("telefon");
//        String numePacient = object.getJSONObject("pacient").getString("numePacient");
//        String prenumePacient = object.getJSONObject("pacient").getString("prenumePacient");
//        String dataTest = object.getJSONObject("pacient").getString("dataTest");
//        String CNP = object.getJSONObject("pacient").getString("CNP");
//        boolean rezultat1 = object.getJSONObject("rezultate").getBoolean("SARS-CoV-2");
//        boolean rezultat2 = object.getJSONObject("rezultate").getBoolean("Gena RdRp");
//        boolean rezultat3 = object.getJSONObject("rezultate").getBoolean("Gena E");
//        boolean rezultat4 = object.getJSONObject("rezultate").getBoolean("Anticorpi");
//        FidelityCard hatz = new FidelityCard(idTest,idCentru,nrDoctori,pretTest,strada,numar,email,telefon,numePacient,prenumePacient,dataTest,CNP,rezultat1,rezultat2,rezultat3,rezultat4);
        int id = object.getInt("id");
        String name = object.getString("name");
        JSONObject attributes = object.getJSONObject("attributes");
        String cardHolderName = attributes.getString("cardHolderName");
        String barcodeValue = attributes.getString("barcodeValue");

        FidelityCard card = new FidelityCard(id,name,cardHolderName,barcodeValue);
        return card;
    }
}
