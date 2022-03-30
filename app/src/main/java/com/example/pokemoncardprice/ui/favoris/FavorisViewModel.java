package com.example.pokemoncardprice.ui.favoris;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.pokemoncardprice.models.CardItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FavorisViewModel extends AndroidViewModel {
    private MutableLiveData<List<CardItem>> mCards;

    public FavorisViewModel(Application application) {
        super(application);
        mCards = new MutableLiveData<>();
    }

    public MutableLiveData<List<CardItem>> getCardInfo() {
        mCards = retrieveData(mCards,"");
        return mCards;
    }

    private MutableLiveData<List<CardItem>> retrieveData(MutableLiveData<List<CardItem>> mCards, String id){
        List<CardItem> cardItems = new ArrayList<>();
        String jsonString = read(getApplication().getApplicationContext(), "data.json");
        if(jsonString!=null){
            JSONObject obj;
            try {
                obj = new JSONObject(jsonString);
                JSONArray array=obj.getJSONArray("data");
                for(int i=0;i<array.length();i++){
                    String extensionUrl = array.getJSONObject(i).getString("extensionImage").replace("\"","");
                    CardItem card = new CardItem(array.getJSONObject(i).getString("id"),array.getJSONObject(i).getString("name"),
                            array.getJSONObject(i).getString("extension"),extensionUrl,array.getJSONObject(i).getJSONArray("prices").getJSONObject(0).getString("prix"),array.getJSONObject(i).getJSONArray("prices").getJSONObject(0).getString("date"), array.getJSONObject(i).getString("releasedDate"));
                    cardItems.add(card);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCards.setValue(cardItems);
        }
        return mCards;
    }


    public String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

}
