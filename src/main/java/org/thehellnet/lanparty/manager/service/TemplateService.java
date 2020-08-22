package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.exception.service.InvalidTemplateException;
import org.thehellnet.lanparty.manager.model.template.HtmlTemplate;
import org.thehellnet.lanparty.manager.model.template.Template;
import org.thehellnet.utility.ResourceUtility;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    public String render(Template template) {
        HtmlTemplate annotation = template.getClass().getAnnotation(HtmlTemplate.class);
        if (annotation == null) {
            throw new InvalidTemplateException("Template not annotated with HtmlTemplate annotation");
        }

        String category = annotation.category();
        String name = annotation.name();
        Context context = template.toContext();

        String templatePath = String.format("templates/%s/%s.html", category, name);
        String rawContent = ResourceUtility.getResourceContent(templatePath);
        if (rawContent == null) {
            return null;
        }

        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(rawContent, context);
    }
}
