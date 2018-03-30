
package com.app.noan.model.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hashtag {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_top_media_only")
    @Expose
    private Boolean isTopMediaOnly;
    @SerializedName("edge_hashtag_to_media")
    @Expose
    private EdgeHashtagToMedia edgeHashtagToMedia;
    @SerializedName("edge_hashtag_to_top_posts")
    @Expose
    private EdgeHashtagToTopPosts edgeHashtagToTopPosts;
    @SerializedName("edge_hashtag_to_content_advisory")
    @Expose
    private EdgeHashtagToContentAdvisory edgeHashtagToContentAdvisory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsTopMediaOnly() {
        return isTopMediaOnly;
    }

    public void setIsTopMediaOnly(Boolean isTopMediaOnly) {
        this.isTopMediaOnly = isTopMediaOnly;
    }

    public EdgeHashtagToMedia getEdgeHashtagToMedia() {
        return edgeHashtagToMedia;
    }

    public void setEdgeHashtagToMedia(EdgeHashtagToMedia edgeHashtagToMedia) {
        this.edgeHashtagToMedia = edgeHashtagToMedia;
    }

    public EdgeHashtagToTopPosts getEdgeHashtagToTopPosts() {
        return edgeHashtagToTopPosts;
    }

    public void setEdgeHashtagToTopPosts(EdgeHashtagToTopPosts edgeHashtagToTopPosts) {
        this.edgeHashtagToTopPosts = edgeHashtagToTopPosts;
    }

    public EdgeHashtagToContentAdvisory getEdgeHashtagToContentAdvisory() {
        return edgeHashtagToContentAdvisory;
    }

    public void setEdgeHashtagToContentAdvisory(EdgeHashtagToContentAdvisory edgeHashtagToContentAdvisory) {
        this.edgeHashtagToContentAdvisory = edgeHashtagToContentAdvisory;
    }

}
