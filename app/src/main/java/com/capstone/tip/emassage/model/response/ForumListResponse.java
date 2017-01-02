package com.capstone.tip.emassage.model.response;

import com.capstone.tip.emassage.model.data.Forum;

import java.util.List;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class ForumListResponse extends ListResponse {

    private List<Forum> results;

    public List<Forum> getResults() {
        return results;
    }

    public void setResults(List<Forum> results) {
        this.results = results;
    }
}
