package youzi.com.sharelove.modal.sql.sqlbase;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "APP_INFO".
 */
public class AppInfo {

    private Long id;
    private String TOKEN;

    public AppInfo() {
    }

    public AppInfo(Long id) {
        this.id = id;
    }

    public AppInfo(Long id, String TOKEN) {
        this.id = id;
        this.TOKEN = TOKEN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

}