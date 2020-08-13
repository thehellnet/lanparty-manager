package org.thehellnet.lanparty.manager.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.repository.ShowcaseRepository;
import org.thehellnet.lanparty.manager.service.ShowcaseService;

@Component("job-showcase-nextPane")
public class ShowcaseNextPaneJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ShowcaseNextPaneJob.class);

    private final ShowcaseRepository showcaseRepository;
    private final ShowcaseService showcaseService;

    @Autowired
    public ShowcaseNextPaneJob(ShowcaseRepository showcaseRepository,
                               ShowcaseService showcaseService) {
        this.showcaseRepository = showcaseRepository;
        this.showcaseService = showcaseService;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long showcaseId = context.getMergedJobDataMap().getLong("showcaseId");
        Showcase showcase = showcaseRepository.findById(showcaseId).orElseThrow();
        showcaseService.showNextPane(showcase);
    }
}
