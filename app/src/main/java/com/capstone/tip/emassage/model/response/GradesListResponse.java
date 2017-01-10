package com.capstone.tip.emassage.model.response;

import com.capstone.tip.emassage.model.data.Grade;

import java.util.List;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class GradesListResponse extends ListResponse {

    private List<Grade> results;

    public List<Grade> getResults() {
        return results;
    }

    public void setResults(List<Grade> results) {
        this.results = results;
    }
}
