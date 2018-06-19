package com.example.android.bakeapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.bakeapp.interfaces.TaskCompleted;
import com.example.android.bakeapp.models.Ingredient;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class JsonAsyncTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

    private static final String LOG_TAG = JsonAsyncTask.class.getSimpleName();

    private static final String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String JSON_FILE = "baking.json";
    private static final String UTF = "UTF-8";

    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";
    private static final String ERROR_PARSING_JSON = "Error parsing JSON";
    private static final String ERROR_OPENING_FILE = "Error opening file";
    private static final String ERROR_CLOSING_INPUT_STREAM = "Error closing input stream";
    private static final String ERROR_READING_FROM_INPUT_STREAM = "Error reading from input stream";
    private static final String ERROR_GETTING_UNBLOCKED_BYTES_AMOUNT = "Error getting unblocked bytes amount";

    private static final String EMPTY = "";

    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_INGREDIENTS_QUANTITY = "quantity";
    private static final String RECIPE_INGREDIENTS_MEASURE = "measure";
    private static final String RECIPE_INGREDIENTS_INGREDIENT = "ingredient";
    private static final String RECIPE_STEPS = "steps";
    private static final String RECIPE_STEP_ID = "id";
    private static final String RECIPE_STEP_SHORT_DESCRIPTION = "shortDescription";
    private static final String RECIPE_STEP_DESCRIPTION = "description";
    private static final String RECIPE_STEP_VIDEOURL = "videoURL";
    private static final String RECIPE_STEP_IMAGEURL = "thumbnailURL";

    private TaskCompleted mTaskCompleted;
    private Context mContext;

    public JsonAsyncTask(TaskCompleted taskCompleted, Context context) {
        mTaskCompleted = taskCompleted;
        mContext = context;
    }

    // json parser method
    private static JSONArray parseJSON(Context context) {
        JSONArray jsonArray = null;

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(JSON_FILE);
        } catch (IOException e) {
            Log.e(LOG_TAG, ERROR_OPENING_FILE);
        }

        int jsonSize = 0;

        try {
            jsonSize = inputStream.available();
        } catch (IOException e) {
            Log.e(LOG_TAG, ERROR_GETTING_UNBLOCKED_BYTES_AMOUNT);
        }

        byte[] jsonString = new byte[jsonSize];

        try {
            inputStream.read(jsonString);
        } catch (IOException e) {
            Log.e(LOG_TAG, ERROR_READING_FROM_INPUT_STREAM);
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, ERROR_CLOSING_INPUT_STREAM);
        }

        try {
            jsonArray = new JSONArray(new String(jsonString, UTF));
        } catch (JSONException e) {
            Log.e(LOG_TAG, ERROR_PARSING_JSON);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... strings) {
        BufferedReader reader = null;
        String jsonResponse = EMPTY;
        HttpURLConnection urlConnection = null;
        InputStream inputStream;

        // tries fetching data from the web
        try {

            URL url = createUrl();

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                StringBuilder output = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }

                if (output.length() == 0) {
                    return null;
                }

                jsonResponse = output.toString();

            } else {
                Log.e(LOG_TAG, ERROR_RESPONSE_CODE + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, ERROR_RETRIEVING_DATA, e);

            // no internet connection therefore getting local data
            JSONArray jsonArray = parseJSON(mContext);
            try {
                return getJsonData(jsonArray.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, ERROR_CLOSING_STREAM, e);
                }
            }
        }

        try {
            return getJsonData(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private URL createUrl() {
        Uri builtUri = Uri.parse(JSON_URL).buildUpon().build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Recipe> getJsonData(String json) throws JSONException {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        Recipe recipe;

        JSONArray mJsonArray = new JSONArray(json);
        Ingredient mIngredient = null;
        Step mStep = null;

        for (int i = 0; i < mJsonArray.length(); i++) {
            ArrayList<Step> stepsList = new ArrayList<>();
            ArrayList<Ingredient> ingredientsList = new ArrayList<>();

            int recipeId = mJsonArray.getJSONObject(i).getInt(RECIPE_ID);
            String recipeName = mJsonArray.getJSONObject(i).optString(RECIPE_NAME);

            String recipeServings = mJsonArray.getJSONObject(i).optString(RECIPE_SERVINGS);
            String recipeImage = mJsonArray.getJSONObject(i).optString(RECIPE_IMAGE);

            JSONArray ingredientsJson = mJsonArray.getJSONObject(i).getJSONArray(RECIPE_INGREDIENTS);
            for (int j = 0; j < ingredientsJson.length(); j++) {
                double quantity = ingredientsJson.getJSONObject(j).getDouble(RECIPE_INGREDIENTS_QUANTITY);
                String measure = ingredientsJson.getJSONObject(j).getString(RECIPE_INGREDIENTS_MEASURE);
                String ingredient = ingredientsJson.getJSONObject(j).getString(RECIPE_INGREDIENTS_INGREDIENT);

                mIngredient = new Ingredient(quantity, measure, ingredient);
                ingredientsList.add(mIngredient);

            }

            JSONArray stepsJson = mJsonArray.getJSONObject(i).getJSONArray(RECIPE_STEPS);
            for (int j = 0; j < stepsJson.length(); j++) {
                int id = stepsJson.getJSONObject(j).getInt(RECIPE_STEP_ID);
                String shortDescription = stepsJson.getJSONObject(j).getString(RECIPE_STEP_SHORT_DESCRIPTION);
                String description = stepsJson.getJSONObject(j).getString(RECIPE_STEP_DESCRIPTION);
                String videoURL = stepsJson.getJSONObject(j).getString(RECIPE_STEP_VIDEOURL);
                String thumbnailURL = stepsJson.getJSONObject(j).getString(RECIPE_STEP_IMAGEURL);

                mStep = new Step(id, shortDescription, description, videoURL, thumbnailURL);
                stepsList.add(mStep);
            }

            recipe = new Recipe(recipeId, recipeName, recipeServings, recipeImage, ingredientsList, stepsList);
            recipeList.add(recipe);
        }

        return recipeList;

    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipeList) {
        super.onPostExecute(recipeList);
        mTaskCompleted.onTaskCompleted(recipeList);
    }
}