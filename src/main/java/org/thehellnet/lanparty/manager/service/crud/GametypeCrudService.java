package org.thehellnet.lanparty.manager.service.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.GametypeServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;

@Service
public class GametypeCrudService extends AbstractCrudService<Gametype, GametypeServiceDTO, GametypeRepository> {

    private static final Logger logger = LoggerFactory.getLogger(TeamCrudService.class);

    public GametypeCrudService(GametypeRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Gametype create(GametypeServiceDTO dto) {
        if (dto.name == null) {
            throw new InvalidDataException("Invalid tournament");
        }

        Gametype gametype = new Gametype(dto.name);
        gametype = repository.save(gametype);
        return gametype;
    }

    @Override
    @Transactional
    public Gametype update(Long id, GametypeServiceDTO dto) {
        Gametype gametype = findById(id);

        boolean changed = false;

        if (dto.name != null) {
            gametype.setName(dto.name);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(gametype);
    }
}
