package mindbees.com.roomwithretrofit;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by alahammad on 10/4/17.
 */


public class LocalCacheManager {
    private static final String DB_NAME = "database-name";
    private Context context;
    private static LocalCacheManager _instance;
    private AppDatabase db;

    public static LocalCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new LocalCacheManager(context);
        }
        return _instance;
    }

    public LocalCacheManager(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }
    public void getPOSts(final PostCallback databaseCallback) {
        db.postDao().getall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Postmodel>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Postmodel> users) throws Exception {
                databaseCallback.OnPostLoaded(users);

            }
        });
    }
    public void insertpost(final PostCallback callback, final List<Postmodel>postmodels)
    {
      Completable.fromAction(new Action() {
          @Override
          public void run() throws Exception {
              db.postDao().insertAllOrders(postmodels);
          }
      }) .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onComplete() {
              callback.postinserted();

          }

          @Override
          public void onError(Throwable e) {

          }
      });
    }

}
