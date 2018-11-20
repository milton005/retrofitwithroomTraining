package mindbees.com.roomwithretrofit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by User on 26-07-2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MYHOLDER> {
    private LinkInterface linkInterface;
    private Context context;
    int pos = -1;
    String s1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private LayoutInflater inflater = null;
    private static final int TYPE_MORE = 1;
    private static final int TYPE_CONTENT = 2;
    private boolean mIsLoadingFooterAdded = false;
    OnLoadMoreListener onLoadMoreListener;
    private boolean userScrolled = true;
    private ArrayList<ModelPost> list;
    int pastVisiblesItems;
    int visibleItemCount;


    public void SetOnlinkClickListener(LinkInterface linkInterface) {
        this.linkInterface = linkInterface;
    }

    @Override
    public MYHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == TYPE_CONTENT)
            view = inflater.inflate(R.layout.inflate_cardview, parent, false);
        else if (viewType == TYPE_MORE)
            view = inflater.inflate(R.layout.inflate_loading, parent, false);
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_fblist, parent, false);
//        return new FacebookAdapter.MYHOLDER(itemView);
        return new MYHOLDER(view, viewType);
    }

    public PostAdapter(Context context, ArrayList<ModelPost> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        userScrolled = true;

                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    // Now check if userScrolled is true and also check if
                    // the item is end then update recycler view and set
                    // userScrolled to false
                    if (!loading&&userScrolled && (visibleItemCount + pastVisiblesItems) > totalItemCount-6) {
                        userScrolled = false;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        // End has been reached
//                        // Do something
//
//                    }
                }
            });
        }
    }

    public void setItems(ArrayList<ModelPost> list) {
        this.list = list;
    }

    public ModelPost getItem(int position) {
        return list.get(position);
    }

    public ModelPost getObject(int position) {
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(final MYHOLDER holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_MORE:
                break;

            case TYPE_CONTENT:
                String date= list.get(position).getPostedDate();
                String title=list.get(position).getPostTitle();
                String description=list.get(position).getPostDescription();
                String image=list.get(position).getPostImage();
                String post_category=list.get(position).getPostCategory();
                final String link=list.get(position).getPostLink();
                if (date!=null&&!date.isEmpty())
                    holder.mDatetext.setText(Html.fromHtml(date));
                if (title!=null&&!title.isEmpty())
                {
                    holder.mTextHead.setVisibility(View.VISIBLE);
                    holder.mTextHead.setText(Html.fromHtml(title));
                }
                else
                {
                    holder.mTextHead.setVisibility(View.GONE);
                }
                if (post_category!=null&&!post_category.isEmpty())
                {
                    holder.mtextCategory.setText(post_category);
                }
                if (description!=null&&!description.isEmpty())
                {
                    holder.mActive.setVisibility(View.VISIBLE);
                    holder.mActive.addAutoLinkMode(AutoLinkMode.MODE_URL);
                    holder.mActive.setUrlModeColor(ContextCompat.getColor(context,R.color.crimson));

//                            Linkify.addLinks(holder.textView, Linkify.WEB_URLS);
//                            holder.textView.setLinksClickable(false);
//                           holder.Read_more.setVisibility(View.VISIBLE);
//                    Spanned s=Html.fromHtml(description);
                    holder.mActive.setAutoLinkText(String.valueOf(Html.fromHtml(description)));

                    holder.mActive.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                        @Override
                        public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                            linkInterface.OnLinkClick(matchedText);

                        }
                    });
                }
                else
                {
                    holder.mActive.setVisibility(View.GONE);
                }
                if (image!=null&&!image.isEmpty())
                {
                  holder.mImageview.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(list.get(position).getPostImage()).into(holder.mImageview);





                }
                else
                {
                    holder.mProgressBar.setVisibility(View.GONE);
                    holder.mImageview.setVisibility(View.GONE);
                }
                if (link!=null&&!link.isEmpty())
                {
                    holder.mLinklayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.mLinklayout.setVisibility(View.GONE);
                }

                holder.mLinklayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linkInterface.OnLinkClick(list.get(position).getPostLink());
                    }
                });




                break;
        }
    }



    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static String[] extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
//               Log.d(TAG, "URL extracted: " + url);
            links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (list.get(position) != null ? TYPE_CONTENT : TYPE_MORE);
//        return TYPE_CONTENT;
    }

    private void add(ModelPost item) {
        list.add(item);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<ModelPost> items) {
        for (ModelPost item : items) {
            add(item);
        }
    }

    public void remove(ModelPost item) {
        int position = list.indexOf(item);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearAllValues() {
        list.clear();
        notifyDataSetChanged();
    }

    public void clear() {
        mIsLoadingFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isFooterAdded() {
        return mIsLoadingFooterAdded;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoading() {
        mIsLoadingFooterAdded = true;
//        add(new ModelPost());
    }

    public void removeLoading() {
        mIsLoadingFooterAdded = false;
//        if (list.size() > 1) {
//            int position = list.size() - 1;
//            ModelPost item = getItem(position);
//
//            if (item != null) {
//                list.remove(position);
//                notifyItemRemoved(position);
//            }
//        } else {
//            notifyDataSetChanged();
//        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MYHOLDER extends RecyclerView.ViewHolder {

        private TextView mtextCategory;
        private TextView mDatetext;
       private TextView mTextHead;
        private AutoLinkTextView mActive;

        private ImageView mImageview;
        private ProgressBar mProgressBar;
        private FrameLayout mLinklayout;
        private ImageView mShareImage;
        TextView applytext;
        private ImageView mWhatsappImage;

        private TextView mTextSave;
        public MYHOLDER(View itemView, int viewType) {
            super(itemView);


            mtextCategory = (TextView) itemView.findViewById(R.id.textHead);
            mDatetext = (TextView) itemView.findViewById(R.id.textTime);
           mTextHead = (TextView) itemView.findViewById(R.id.textHead1);
            mActive = (AutoLinkTextView) itemView.findViewById(R.id.active);
            applytext=itemView.findViewById(R.id.textApply);
            mLinklayout=itemView.findViewById(R.id.linklayout);


            mImageview = (ImageView) itemView.findViewById(R.id.media_image);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

            mShareImage = (ImageView) itemView.findViewById(R.id.ShareImage);
            mWhatsappImage = (ImageView) itemView.findViewById(R.id.whatsappImage);

            mTextSave = (TextView) itemView.findViewById(R.id.textSave);

        }
    }
}
