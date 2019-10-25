package org.thehellnet.lanparty.manager.model.dto.request.showcase;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.constant.ShowcaseMode;

public class UpdateShowcaseRequestDTO extends ShowcaseRequestDTO {

    public String name;
    public ShowcaseMode mode;
    public Long tournament;
    public Long match;
    public String lastAddress;
    public DateTime lastContact;
}
