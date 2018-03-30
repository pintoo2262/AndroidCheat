
package com.app.noan.model.instagram;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EdgeMediaToCaption_ {

    @SerializedName("edges")
    @Expose
    private List<Edge___> edges = null;

    public List<Edge___> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge___> edges) {
        this.edges = edges;
    }

}
