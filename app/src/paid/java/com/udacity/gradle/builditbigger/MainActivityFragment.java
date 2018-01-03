package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.javaandroidlibrary.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.lang.ref.WeakReference;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static MyApi myApi = null;
    private TextView textViewInstructions;
    private Button buttonTellJoke;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        textViewInstructions = root.findViewById(R.id.instructions_text_view);
        buttonTellJoke = root.findViewById(R.id.button_tell_joke);
        buttonTellJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EndpointsAsyncTask((MainActivity) getActivity()).execute();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (textViewInstructions.getVisibility() == View.GONE) {
            textViewInstructions.setVisibility(View.VISIBLE);
        }

        if (buttonTellJoke.getVisibility() == View.GONE) {
            buttonTellJoke.setVisibility(View.VISIBLE);
        }
    }

    static class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

        private final WeakReference<MainActivity> weakActivity;

        EndpointsAsyncTask(MainActivity mainActivity) {
            weakActivity = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            MainActivity mainActivity = weakActivity.get();
            mainActivity.setIdleState(false);

            TextView textViewInstructions = mainActivity.findViewById(R.id.instructions_text_view);
            textViewInstructions.setVisibility(View.GONE);

            Button buttonTellJoke = mainActivity.findViewById(R.id.button_tell_joke);
            buttonTellJoke.setVisibility(View.GONE);

            ProgressBar progressBar = mainActivity.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            if(myApi == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApi = builder.build();
            }

            try {
                return myApi.getJoke().execute().getData();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            MainActivity mainActivity = weakActivity.get();
            mainActivity.setIdleState(true);

            ProgressBar progressBar = mainActivity.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            if (result == null) {
                return;
            }
            Intent intent = new Intent(mainActivity, JokeActivity.class);
            intent.putExtra(mainActivity.getString(R.string.joke), result);
            mainActivity.startActivity(intent);
        }
    }
}
