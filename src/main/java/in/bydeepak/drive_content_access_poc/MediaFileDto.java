package in.bydeepak.drive_content_access_poc;

public class MediaFileDto {
    private String id;
    private String name;
    private String mimeType;
    private String thumbnailLink;
    private String webViewLink;    // For <iframe> playback
    private String webContentLink; // For native HTML5 elements

    public MediaFileDto(String id, String name, String mimeType, String thumbnailLink, String webViewLink, String webContentLink) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.thumbnailLink = thumbnailLink;
        this.webViewLink = webViewLink;
        this.webContentLink = webContentLink;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getMimeType() { return mimeType; }
    public String getThumbnailLink() { return thumbnailLink; }
    public String getWebViewLink() { return webViewLink; }
    public String getWebContentLink() { return webContentLink; }
}
