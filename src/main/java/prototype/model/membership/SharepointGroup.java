package prototype.model.membership;

import java.util.List;

public class SharepointGroup {
    protected String loginName;
    protected String title;
    protected String description;
    protected String ownerTitle;
    protected boolean allowMembersEditMembership;
    protected boolean allowRequestToJoinLeave;
    protected boolean autoAcceptRequestToJoinLeave;
    protected boolean onlyAllowMembersViewMembership;
    protected List<SharepointUser> sharepointUsers;

    public SharepointGroup(String loginName,
                           String title,
                           String description,
                           String ownerTitle,
                           boolean allowMembersEditMembership,
                           boolean allowRequestToJoinLeave,
                           boolean autoAcceptRequestToJoinLeave,
                           boolean onlyAllowMembersViewMembership,
                           List<SharepointUser> sharepointUsers) {
        this.loginName = loginName;
        this.title = title;
        this.description = description;
        this.ownerTitle = ownerTitle;
        this.allowMembersEditMembership = allowMembersEditMembership;
        this.allowRequestToJoinLeave = allowRequestToJoinLeave;
        this.autoAcceptRequestToJoinLeave = autoAcceptRequestToJoinLeave;
        this.onlyAllowMembersViewMembership = onlyAllowMembersViewMembership;
        this.sharepointUsers = sharepointUsers;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerTitle() {
        return ownerTitle;
    }

    public boolean isAllowMembersEditMembership() {
        return allowMembersEditMembership;
    }

    public boolean isAllowRequestToJoinLeave() {
        return allowRequestToJoinLeave;
    }

    public boolean isAutoAcceptRequestToJoinLeave() {
        return autoAcceptRequestToJoinLeave;
    }

    public boolean isOnlyAllowMembersViewMembership() {
        return onlyAllowMembersViewMembership;
    }

    public List<SharepointUser> getSharepointUsers() {
        return sharepointUsers;
    }
}
