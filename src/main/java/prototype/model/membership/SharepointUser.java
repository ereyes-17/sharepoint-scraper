package prototype.model.membership;

public class SharepointUser {
    protected String domain;
    protected String accountName;
    protected String title;
    protected String email;
    protected boolean isSiteAdmin;
    protected String nameId;
    protected String nameIdIssuer;

    public SharepointUser(String loginName, String title, String email, boolean isSiteAdmin, String nameId, String nameIdIssuer) {
        this.domain = getDomainFromLoginName(loginName);
        this.accountName = getAccountNameFromLoginName(loginName);
        this.title = title;
        this.email = email;
        this.isSiteAdmin = isSiteAdmin;
        this.nameId = nameId;
        this.nameIdIssuer = nameIdIssuer;
    }

    public String getDomain() {
        return domain;
    }

    public String getAccountName() {
        return accountName;
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

    public String getNameId() {
        return nameId;
    }

    public String getNameIdIssuer() {
        return nameIdIssuer;
    }

    private String getDomainFromLoginName(String loginName) {
        try {
            return loginName.split("\\|")[1].split("\\\\")[0];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return loginName.split("\\\\")[0];
        }
    }

    private String getAccountNameFromLoginName(String loginName) {
        try {
            return loginName.split("\\|")[1].split("\\\\")[1];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return loginName.split("\\\\")[1];
        }
    }
}
