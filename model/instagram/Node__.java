
package com.app.noan.model.instagram;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Node__ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("edge_media_to_caption")
    @Expose
    private EdgeMediaToCaption_ edgeMediaToCaption;
    @SerializedName("shortcode")
    @Expose
    private String shortcode;
    @SerializedName("edge_media_to_comment")
    @Expose
    private EdgeMediaToComment_ edgeMediaToComment;
    @SerializedName("taken_at_timestamp")
    @Expose
    private Integer takenAtTimestamp;
    @SerializedName("dimensions")
    @Expose
    private Dimensions_ dimensions;
    @SerializedName("display_url")
    @Expose
    private String displayUrl;
    @SerializedName("edge_liked_by")
    @Expose
    private EdgeLikedBy_ edgeLikedBy;
    @SerializedName("edge_media_preview_like")
    @Expose
    private EdgeMediaPreviewLike_ edgeMediaPreviewLike;
    @SerializedName("owner")
    @Expose
    private Owner_ owner;
    @SerializedName("thumbnail_src")
    @Expose
    private String thumbnailSrc;
    @SerializedName("thumbnail_resources")
    @Expose
    private List<ThumbnailResource_> thumbnailResources = null;
    @SerializedName("is_video")
    @Expose
    private Boolean isVideo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EdgeMediaToCaption_ getEdgeMediaToCaption() {
        return edgeMediaToCaption;
    }

    public void setEdgeMediaToCaption(EdgeMediaToCaption_ edgeMediaToCaption) {
        this.edgeMediaToCaption = edgeMediaToCaption;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public EdgeMediaToComment_ getEdgeMediaToComment() {
        return edgeMediaToComment;
    }

    public void setEdgeMediaToComment(EdgeMediaToComment_ edgeMediaToComment) {
        this.edgeMediaToComment = edgeMediaToComment;
    }

    public Integer getTakenAtTimestamp() {
        return takenAtTimestamp;
    }

    public void setTakenAtTimestamp(Integer takenAtTimestamp) {
        this.takenAtTimestamp = takenAtTimestamp;
    }

    public Dimensions_ getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions_ dimensions) {
        this.dimensions = dimensions;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public EdgeLikedBy_ getEdgeLikedBy() {
        return edgeLikedBy;
    }

    public void setEdgeLikedBy(EdgeLikedBy_ edgeLikedBy) {
        this.edgeLikedBy = edgeLikedBy;
    }

    public EdgeMediaPreviewLike_ getEdgeMediaPreviewLike() {
        return edgeMediaPreviewLike;
    }

    public void setEdgeMediaPreviewLike(EdgeMediaPreviewLike_ edgeMediaPreviewLike) {
        this.edgeMediaPreviewLike = edgeMediaPreviewLike;
    }

    public Owner_ getOwner() {
        return owner;
    }

    public void setOwner(Owner_ owner) {
        this.owner = owner;
    }

    public String getThumbnailSrc() {
        return thumbnailSrc;
    }

    public void setThumbnailSrc(String thumbnailSrc) {
        this.thumbnailSrc = thumbnailSrc;
    }

    public List<ThumbnailResource_> getThumbnailResources() {
        return thumbnailResources;
    }

    public void setThumbnailResources(List<ThumbnailResource_> thumbnailResources) {
        this.thumbnailResources = thumbnailResources;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }

}
