package com.squad.view.helpers.ui_items;

import com.squad.model.FacebookGraphResponse;

public class UserUIItem {

    private FacebookGraphResponse user;

    public UserUIItem(FacebookGraphResponse user) {
        this.user = user;
    }

    public String name() {
        return user.name();
    }

    public String pictureUrl() {
        return user.picture().data().url();
    }

    public String fbId() {
        return user.fbId();
    }
}
