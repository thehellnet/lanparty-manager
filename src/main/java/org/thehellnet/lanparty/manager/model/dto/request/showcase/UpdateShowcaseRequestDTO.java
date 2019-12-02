package org.thehellnet.lanparty.manager.model.dto.request.showcase;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;

public class UpdateShowcaseRequestDTO extends ShowcaseRequestDTO {

    public String tag;
    public String name;
    public PaneMode mode;
    public Long tournament;
    public Long match;
    public String lastAddress;
    public DateTime lastContact;
}
