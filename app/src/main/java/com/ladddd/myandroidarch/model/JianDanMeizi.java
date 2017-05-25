package com.ladddd.myandroidarch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JianDanMeizi {

  public String status;

  @SerializedName("current_page")
  public int currentPage;

  @SerializedName("total_comments")
  public int totalComments;

  @SerializedName("page_count")
  public int pageCount;

  public int count;

  public List<JianDanMeiziData> comments;

  public class JianDanMeiziData {

    @SerializedName("comment_ID")
    public String commentID;

    @SerializedName("comment_author")
    public String commentAuthor;

    @SerializedName("comment_date")
    public String commentDate;

    @SerializedName("text_content")
    public String textContent;

    @SerializedName("vote_positive")
    public String votePositive;

    @SerializedName("vote_negative")
    public String voteNegative;

    @SerializedName("comment_counts")
    public String commentCounts;

    @SerializedName("comment_agent")
    public String commentAgent;

    public List<String> pics;
  }
}
