package ar.edu.ips.aus.seminario2.sampleproject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ScoreDAO {

    @Insert
    void insert(Score score);

    @Update
    void update(Score score);

    @Delete
    void delete(Score score);

    @Query("SELECT * FROM score WHERE player_id = :id")
    Score findScoreById(String id);

    @Query("SELECT * FROM score ORDER BY points DESC")
    List<Score> getAll();
}
