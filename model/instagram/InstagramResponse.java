
package com.app.noan.model.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstagramResponse {

    @SerializedName("graphql")
    @Expose
    private Graphql graphql;

    public Graphql getGraphql() {
        return graphql;
    }

    public void setGraphql(Graphql graphql) {
        this.graphql = graphql;
    }

}
