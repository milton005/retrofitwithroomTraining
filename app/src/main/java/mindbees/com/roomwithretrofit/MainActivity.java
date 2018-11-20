package mindbees.com.roomwithretrofit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PostCallback{
    private RecyclerView recyclerView;
    private ArrayList<ModelPost> list=new ArrayList<>();
    PostAdapter adapter;
    private LinearLayoutManager llm;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;
    private boolean isLoadMore = false;
    private boolean paginationEnable = true;
    private int offset = 0;
    private int limit=10;
    int pageCount = 0;
    private int totalCount = 0;
    Handler handler;
    String token=null;

    int k=0;
    SwipeRefreshLayout swipeRefreshLayout;

    String Category_id="";
    private boolean is_end=false;
    ProgressDialog dialog;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=new ArrayList<>();
        initUi();
        offset=0;
        limit=10;

        initAdapter();
//        mInterstitialAd = new InterstitialAd(getActivity());
//        mInterstitialAd.setAdUnitId("ca-app-pub-7758621182027160/4564496045");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());

//
            dialog=new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(true);
            dialog.show();

            getPOsts(Category_id,offset,limit);

    }
    private void initAdapter() {
        adapter=new PostAdapter(this,list,recyclerView);
        recyclerView.setAdapter(adapter);
        try {
            LocalCacheManager.getInstance(this).getPOSts(this);
        }catch (Exception e)
        {

        }
        handler=new Handler();
        adapter.setOnLoadMoreListener(new PostAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mIsLastPage) {
                    offset = limit;
                    limit = limit + 10;
                    isLoadMore = true;
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            getPOsts(Category_id, offset, limit);
                        }
                    }, 100);
                }




//                loadMoreItems();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setLoaded();
                mIsLastPage=false;
                list.clear();

                adapter.notifyDataSetChanged();
                offset=0;
                limit=10;

                getPOsts(Category_id,offset,limit);
            }
        });


    }
    private void initUi() {
        progressBar=   (ProgressBar) findViewById(R.id.loadItemsLayout_recyclerView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview2);
        swipeRefreshLayout=findViewById(R.id.swipeRefresh);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
//       recyclerView.setOnScrollListener(mRecyclerViewOnScrollListener);
//        recyclerView.setItemAnimator(new Sid);
    }
    public void getPOsts(String category_id, int offset, final int limit)
    {

        HashMap<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));
        if (category_id != null && !category_id.isEmpty())
            params.put("post_category", category_id);
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<List<ModelPost>> call = apiService.getPosts(params);
        call.enqueue(new Callback<List<ModelPost>>() {
            @Override
            public void onResponse(Call<List<ModelPost>> call, Response<List<ModelPost>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                    }



                        adapter.setLoaded();
                        mIsLastPage=false;
                        List<ModelPost> listm=response.body();
                        if (listm != null && listm.size() > 0) {

//                                if (isLoadMore) {
//                                    try {
//                                        if (list != null && list.size() > 0) {
//                                            adapter.setLoaded();
//                                            list.remove(list.size() - 1);
//                                            adapter.notifyItemRemoved(list.size());
//                                        }
//                                    } catch (Exception e) {
//
//                                    }
////                                    adapter.setLoaded();
//
//                                }
                            List<Postmodel>postmodels=new ArrayList<>();
                            for (int i=0;i<listm.size();i++)
                            {
                                String id=listm.get(i).getPostId();
                                String title=listm.get(i).getPostTitle();
                                String category=listm.get(i).getPostCategory();
                                String link=listm.get(i).getPostLink();
                                String image=listm.get(i).getPostImage();
                                String date=listm.get(i).getPostedDate();
                                String description=listm.get(i).getPostDescription();

                                Postmodel postmodel=new Postmodel(id,title,description,image,link,category,date);
                                postmodels.add(postmodel);
                            }
                            if (postmodels!=null&&postmodels.size()>0)
                            {
                                LocalCacheManager.getInstance(MainActivity.this).insertpost(MainActivity.this,postmodels);
                            }

                            paginationEnable = true;
//                            list.addAll(listm);
//                            adapter.notifyDataSetChanged();

                        } else {
                            mIsLastPage=true;

                        }




                } else {


                }

            }

            @Override
            public void onFailure(Call<List<ModelPost>> call, Throwable t) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                }
                Toast.makeText(MainActivity.this,"നിരവധി ആളുകൾ ഒരേ സമയം ഉപയോഗിക്കുന്നതിനാൽ Server Busy ആണ് അല്പസമയം കഴിഞ്ഞു വീണ്ടും ശ്രമിക്കുക",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void OnPostLoaded(List<Postmodel> postmodels) {
        for (int i=0;i<postmodels.size();i++) {
            ModelPost modelPost = new ModelPost();
            modelPost.setPostCategory(postmodels.get(i).getPostCategory());
            modelPost.setPostId(postmodels.get(i).getPostId());
            modelPost.setPostTitle(postmodels.get(i).getPostTitle());
            modelPost.setPostedDate(postmodels.get(i).getPostedDate());
            modelPost.setPostDescription(postmodels.get(i).getPostDescription());
            modelPost.setPostLink(postmodels.get(i).getPostLink());
            modelPost.setPostImage(postmodels.get(i).getPostImage());
            list.add(modelPost);

        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void postinserted() {
        LocalCacheManager.getInstance(this).getPOSts(this);
    }
}
