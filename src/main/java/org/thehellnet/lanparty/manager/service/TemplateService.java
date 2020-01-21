package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.thehellnet.utility.ResourceUtility;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class TemplateService {

    public String render(String templateCategory, String templateName, Context context) {
        String templatePath = String.format("templates/%s/%s.html", templateCategory, templateName);
        String template = ResourceUtility.getInstance(templatePath).getResourceContent();
        if (template == null) {
            return null;
        }

        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(template, context);
    }
}
