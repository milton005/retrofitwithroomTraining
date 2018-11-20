package mindbees.com.roomwithretrofit;

import java.util.List;

public interface PostCallback {
    void OnPostLoaded(List<Postmodel>postmodels);
    void postinserted();
}
