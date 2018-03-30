
package com.app.noan.model.instagram;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Node {

    @SerializedName("comments_disabled")
    @Expose
    private Boolean commentsDisabled;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("edge_media_to_caption")
    @Expose
    private EdgeMediaToCaption edgeMediaToCaption;
    @SerializedName("shortcode")
    @Expose
    private String shortcode;
    @SerializedName("edge_media_to_comment")
    @Expose
    private EdgeMediaToComment edgeMediaToComment;
    @SerializedName("taken_at_timestamp")
    @Expose
    private Integer takenAtTimestamp;
    @SerializedName("dimensions")
    @Expose
    private Dimensions dimensions;
    @SerializedName("display_url")
    @Expose
    private String displayUrl;
    @SerializedName("edge_liked_by")
    @Expose
    private EdgeLikedBy edgeLikedBy;
    @SerializedName("edge_media_preview_like")
    @Expose
    private EdgeMediaPreviewLike edgeMediaPreviewLike;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("thumbnail_src")
    @Expose
    private String thumbnailSrc;
    @SerializedName("thumbnail_resources")
    @Expose
    private List<ThumbnailResource> thumbnailResources = null;
    @SerializedName("is_video")
    @Expose
    private Boolean isVideo;

    public Boolean getCommentsDisabled() {
        return commentsDisabled;
    }

    public void setCommentsDisabled(Boolean commentsDisabled) {
        this.commentsDisabled = commentsDisabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EdgeMediaToCaption getEdgeMediaToCaption() {
        return edgeMediaToCaption;
    }

    public void setEdgeMediaToCaption(EdgeMediaToCaption edgeMediaToCaption) {
        this.edgeMediaToCaption = edgeMediaToCaption;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public EdgeMediaToComment getEdgeMediaToComment() {
        return edgeMediaToComment;
    }

    public void setEdgeMediaToComment(EdgeMediaToComment edgeMediaToComment) {
        this.edgeMediaToComment = edgeMediaToComment;
    }

    public Integer getTakenAtTimestamp() {
        return takenAtTimestamp;
    }

    public void setTakenAtTimestamp(Integer takenAtTimestamp) {
        this.takenAtTimestamp = takenAtTimestamp;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public EdgeLikedBy getEdgeLikedBy() {
        return edgeLikedBy;
    }

    public void setEdgeLikedBy(EdgeLikedBy edgeLikedBy) {
        this.edgeLikedBy = edgeLikedBy;
    }

    public EdgeMediaPreviewLike getEdgeMediaPreviewLike() {
        return edgeMediaPreviewLike;
    }

    public void setEdgeMediaPreviewLike(EdgeMediaPreviewLike edgeMediaPreviewLike) {
        this.edgeMediaPreviewLike = edgeMediaPreviewLike;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getThumbnailSrc() {
        return thumbnailSrc;
    }

    public void setThumbnailSrc(String thumbnailSrc) {
        this.thumbnailSrc = thumbnailSrc;
    }

    public List<ThumbnailResource> getThumbnailResources() {
        return thumbnailResources;
    }

    public void setThumbnailResources(List<ThumbnailResource> thumbnailResources) {
        this.thumbnailResources = thumbnailResources;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }

}
