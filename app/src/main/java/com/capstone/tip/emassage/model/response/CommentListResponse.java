package com.capstone.tip.emassage.model.response;

import com.capstone.tip.emassage.model.data.Comment;

import java.util.List;

/**
 * Created by Cholo Mia on 12/29/2016.
 */

public class CommentListResponse extends ListResponse {

    private List<Comment> results;

    public List<Comment> getResults() {
        return results;
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }
}
