package com.smartelectronics.filimo.model;

public class MovieModel {

    private String ImageUrl;
    private String persianMovieTitle;
    private String englishMovieTitle;
    private String movieYear;
    private String archiveId;
    private String objectID;
    private String ranking;
    private String filmSummary;
    private String videoUrl;


    public MovieModel(String ImageUrl,
                      String persianMovieTitle,
                      String englishMovieTitle,
                      String movieYear,
                      String archiveId,
                      String objectID,
                      String ranking,
                      String filmSummary,
                      String videoUrl){

        this.ImageUrl = ImageUrl;
        this.persianMovieTitle = persianMovieTitle;
        this.englishMovieTitle = englishMovieTitle;
        this.movieYear = movieYear;
        this.archiveId = archiveId;
        this.objectID  = objectID;
        this.ranking   = ranking;
        this.filmSummary = filmSummary;
        this.videoUrl    = videoUrl;
    }

    public MovieModel(String ImageUrl, String persianMovieTitle, String englishMovieTitle, String movieYear, String archiveId, String objectID) {
        this.ImageUrl = ImageUrl;
        this.persianMovieTitle = persianMovieTitle;
        this.englishMovieTitle = englishMovieTitle;
        this.movieYear = movieYear;
        this.archiveId = archiveId;
        this.objectID  = objectID;
    }

    public MovieModel(String ImageUrl, String persianMovieTitle, String englishMovieTitle, String archiveId) {
        this.ImageUrl = ImageUrl;
        this.persianMovieTitle = persianMovieTitle;
        this.englishMovieTitle = englishMovieTitle;
        this.archiveId = archiveId;

    }

    public String getImageUrl() {
        return ImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getPersianMovieTitle() {
        return persianMovieTitle;
    }
    public void setPersianMovieTitle(String persianMovieTitle) {
        this.persianMovieTitle = persianMovieTitle;
    }

    public String getEnglishMovieTitle() {
        return englishMovieTitle;
    }
    public void setEnglishMovieTitle(String englishMovieTitle) {
        this.englishMovieTitle = englishMovieTitle;
    }

    public String getMovieYear() {
        return movieYear;
    }
    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public java.lang.String getArchiveId() {
        return archiveId;
    }

    public String getObjectID() {
        return objectID;
    }
    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getRanking() {
        return ranking;
    }
    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getFilmSummary() {
        return filmSummary;
    }
    public void setFilmSummary(String filmSummary) {
        this.filmSummary = filmSummary;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
