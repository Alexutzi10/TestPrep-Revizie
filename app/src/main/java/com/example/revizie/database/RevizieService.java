package com.example.revizie.database;

import android.content.Context;

import com.example.revizie.data.Revizie;
import com.example.revizie.network.AsyncTaskRunner;
import com.example.revizie.network.Callback;

import java.util.List;
import java.util.concurrent.Callable;

public class RevizieService {
    private RevizieDao revizieDao;
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();

    public RevizieService (Context context) {
        revizieDao = DatabaseManager.getInstance(context).getRevizieDao();
    }

    public void getAll(Callback<List<Revizie>> callback) {
        Callable<List<Revizie>> callable = () -> revizieDao.getAll();
        asyncTaskRunner.executeAsync(callable, callback);
    }
    
    public void insertAll(List<Revizie> revizii, Callback<List<Revizie>> callback) {
        Callable<List<Revizie>> callable = () -> {
            List<Long> ids = revizieDao.insertAll(revizii);

            for (int i = 0; i < revizii.size(); i++) {
                revizii.get(i).setId(ids.get(i));
            }
            return revizii;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void delete(List<Revizie> revizii, Callback<List<Revizie>> callback) {
        Callable<List<Revizie>> callable = () -> {
            int count = revizieDao.delete(revizii);

            if (count <= 0) {
                return null;
            }
            return revizii;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }
}
