package henryvogt.com.chuckjokes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Henry Vogt on 20.06.2016.
 */
public class ChuckJokes extends Activity {

    private final String REQUEST_TAG = "myRequestTag";
    private final String JOKE = "joke";
    private TextView tvJoke;
    private RequestQueue queue;
    private final String url ="https://api.chucknorris.io/jokes/random";
    private String actJoke = "";

    private JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            tvJoke.setText(getResponseString(response));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            tvJoke.setText(R.string.tst_error);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        tvJoke = (TextView) findViewById(R.id.tv_joke);
        queue = Volley.newRequestQueue(ChuckJokes.this);
    }

    public void onButtonClick(View view) {
        jsonObjectRequest.setTag(REQUEST_TAG);
        queue.add(jsonObjectRequest);
    }

    private String getResponseString(JSONObject response) {
        String value = "";
        try {
            value = response.getString("value");
        } catch (JSONException e) {
            tvJoke.setText(R.string.tst_error);
        }
        actJoke = value;
        return value;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(JOKE, actJoke);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        actJoke = savedInstanceState.getString(JOKE);
        tvJoke.setText(actJoke);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(REQUEST_TAG);
        }
    }
}
