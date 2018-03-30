
package com.app.noan.model.instagram;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EdgeHashtagToTopPosts {

    @SerializedName("edges")
    @Expose
    private List<Edge__> edges = null;

    public List<Edge__> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge__> edges) {
        this.edges = edges;
    }

}
