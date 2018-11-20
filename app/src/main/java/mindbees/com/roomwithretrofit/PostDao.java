package mindbees.com.roomwithretrofit;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface PostDao {
    @Query("SELECT * FROM posts")
    Maybe<List<Postmodel>>getall();
    @Insert
    void insertAll(Postmodel... postmodels);

    @Delete
    void delete(Postmodel postmodel);
    @Query("SELECT * FROM posts WHERE post_id LIKE :post_id")
    Maybe<Postmodel>  getPostbyId(String post_id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOrders(List<Postmodel> order);
    @Update
    public  void update(Postmodel postmodel);

    @Update
    public void updateUsers(Postmodel... postmodels);
}
