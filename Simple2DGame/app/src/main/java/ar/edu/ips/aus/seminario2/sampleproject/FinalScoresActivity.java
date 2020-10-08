package ar.edu.ips.aus.seminario2.sampleproject;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalScoresActivity extends ListActivity {

    ListView playersView;
    private List<Map<String, String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_final_scores);
        playersView = (ListView) findViewById(android.R.id.list);

        ListAdapter adapter = new SimpleAdapter(this,
                data,
                R.layout.player_score,
                new String[]{"name", "points"},
                new int[]{R.id.player_name, R.id.player_points});
        playersView.setAdapter(adapter);

        ScoreDatabase database = ScoreDatabase.getDatabase(this);
        RetrieveScoresTask scoresTask = new RetrieveScoresTask(this, adapter, database, data);
        scoresTask.execute();

    }
}


class RetrieveScoresTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ListAdapter adapter;
    private ScoreDatabase db;
    List<Map<String, String>> data;

    public RetrieveScoresTask(Context context, ListAdapter adapter,
                              ScoreDatabase db, List<Map<String, String>> data) {
        this.context = context;
        this.db = db;
        this.adapter = adapter;
        this.data = data;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Score> scores = db.scoreDAO().getAll();
        List<Map<String, String>> result = new ArrayList<>();
        for (Score score:scores) {
            Map<String, String> scoreMap = new HashMap<String, String>();
            scoreMap.put("name", score.getPlayerName());
            scoreMap.put("points", String.valueOf(score.getPoints()));
            data.add(scoreMap);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ((SimpleAdapter) adapter).notifyDataSetChanged();
    }
}

