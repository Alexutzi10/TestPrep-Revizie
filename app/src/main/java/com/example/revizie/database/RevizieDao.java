package com.example.revizie.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.revizie.data.Revizie;

import java.util.List;

@Dao
public interface RevizieDao {
    @Query("SELECT * FROM revizii")
    List<Revizie> getAll();

    @Insert
    List<Long> insertAll(List<Revizie> revizii);

    @Delete
    int delete(List<Revizie> revizii);
}
