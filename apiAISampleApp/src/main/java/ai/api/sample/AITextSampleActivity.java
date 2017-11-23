package ai.api.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;


public class AITextSampleActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = AITextSampleActivity.class.getName();
    private Gson gson = GsonFactory.getGson();

    private TextView resultTextView;
    private EditText queryEditText;
    private AIDataService aiDataService;

    private List<Chat> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;

    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aitext_sample);
        //resultTextView = (TextView) findViewById(R.id.resultTextView);
        queryEditText = (EditText) findViewById(R.id.textQuery);

        findViewById(R.id.buttonSend).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvChat);
        mRecyclerView.setScrollContainer(false);
        mChats = new ArrayList<>();

        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initService(final LanguageConfig selectedLanguage) {
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                lang,
                AIConfiguration.RecognitionEngine.System);


        aiDataService = new AIDataService(this, config);
    }


    /*
    * AIRequest should have query OR event
    */
    private void sendRequest() {
        final String queryString = String.valueOf(queryEditText.getText());
        if(!queryString.equals("")) {
            queryEditText.setText("");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.format(cal.getTime());
            Chat model = new Chat("Ben", queryString, 1, sdf.format(cal.getTime()));

            mChats.add(model);
            mRecyclerView.scrollToPosition(mChats.size() - 1);
            mAdapter.notifyItemInserted(mChats.size() - 1);

            final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

                private AIError aiError;

                @Override
                protected AIResponse doInBackground(final String... params) {
                    final AIRequest request = new AIRequest();
                    String query = params[0];

                    if (!TextUtils.isEmpty(query))
                        request.setQuery(query);
                    RequestExtras requestExtras = null;


                    try {
                        return aiDataService.request(request, requestExtras);
                    } catch (final AIServiceException e) {
                        aiError = new AIError(e);
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(final AIResponse response) {
                    if (response != null) {
                        onResult(response);
                    } else {
                        onError(aiError);
                    }
                }
            };

            task.execute(queryString);
        }
    }



    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");
                try {
                    JSONObject res = new JSONObject(gson.toJson(response));
                    JSONObject result = res.optJSONObject("result");
                    JSONObject parameters = result.optJSONObject("parameters");
                    String number = parameters.optString("number");
                    String urun = parameters.optString("urun");
                    String sekil = parameters.optString("sekil");
                    String type = parameters.optString("type");
                    String lat = parameters.optString("lat");
                    String lng = parameters.optString("lng");

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    sdf.format(cal.getTime());
                    String speech = result.optJSONObject("fulfillment").optString("speech");
                    if(!speech.equals("") && lat.equals("")){
                        String newspeech = speech.replace("\\n", System.getProperty("line.separator"));
                        Chat model = new Chat("Çırak", newspeech, 2, sdf.format(cal.getTime()));
                        mChats.add(model);
                    }
                    else if(!lat.equals("")&& !lng.equals("")){
                        Log.d("chatId", "4");
                        Chat model = new Chat("Çırak", speech, lat, lng, 4, sdf.format(cal.getTime()));
                        mChats.add(model);
                    }
                    else {
                        Chat model = new Chat("Çırak", number, urun, sekil, type, 3, sdf.format(cal.getTime()));
                        mChats.add(model);
                    }


                    mRecyclerView.scrollToPosition(mChats.size()-1);
                    mAdapter.notifyItemInserted(mChats.size()-1);
                    queryEditText.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //resultTextView.setText(gson.toJson(response));

                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);
                //TTS.speak(speech);

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }
            }

        });
    }

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonSend:

                initService(Config.languages[0]);
                sendRequest();
                break;

        }
    }
}
