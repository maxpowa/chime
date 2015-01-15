package com.maxpowa.chime.data;

public class UserConfiguration {

    private boolean invisible = false;
    private boolean allowingRequests = true;
    private boolean allowingJoins = true;
    private boolean broadcastingStatus = true;

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isAllowingRequests() {
        return allowingRequests;
    }

    public void setAllowingRequests(boolean allowRequests) {
        this.allowingRequests = allowRequests;
    }

    public boolean isAllowingJoins() {
        return allowingJoins;
    }

    public void setAllowingJoins(boolean allowJoins) {
        this.allowingJoins = allowJoins;
    }

    public boolean isBroadcastingStatus() {
        return broadcastingStatus;
    }

    public void setBroadcastingStatus(boolean broadcastStatus) {
        this.broadcastingStatus = broadcastStatus;
    }

}
