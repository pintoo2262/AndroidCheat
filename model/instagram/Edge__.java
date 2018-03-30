
package com.app.noan.model.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Edge__ {

    @SerializedName("node")
    @Expose
    private Node__ node;

    public Node__ getNode() {
        return node;
    }

    public void setNode(Node__ node) {
        this.node = node;
    }

}
