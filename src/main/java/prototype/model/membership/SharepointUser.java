package prototype.model.membership;

public class SharepointUser {
    protected String loginName;
    protected String title;
    protected String email;
    protected boolean isSiteAdmin;
    protected String nameIdIssuer;

    public SharepointUser(String loginName, String title, String email, boolean isSiteAdmin, String nameIdIssuer) {
        this.loginName = loginName;
        this.title = title;
        this.email = email;
        this.isSiteAdmin = isSiteAdmin;
        this.nameIdIssuer = nameIdIssuer;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSiteAdmin() {
        return isSiteAdmin;
    }

    public String getNameIdIssuer() {
        return nameIdIssuer;
    }
}
