package org.thehellnet.lanparty.manager.model.template;

import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thymeleaf.context.Context;

@HtmlTemplate(category = "mail", name = "appuser-registration-confirm")
public class AppUserRegistrationConfirmTemplate extends Template {

    private final AppUser appUser;
    private final String link;

    public AppUserRegistrationConfirmTemplate(AppUser appUser, String link) {
        this.appUser = appUser;
        this.link = link;
    }

    @Override
    public Context toContext() {
        Context context = new Context();
        context.setVariable("appUser", appUser);
        context.setVariable("link", link);
        return context;
    }
}
