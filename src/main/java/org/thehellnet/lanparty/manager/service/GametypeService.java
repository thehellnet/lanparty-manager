package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;

import java.util.List;

@Service
public class GametypeService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final GametypeRepository gametypeRepository;

    @Autowired
    public GametypeService(GametypeRepository gametypeRepository) {
        this.gametypeRepository = gametypeRepository;
    }

    @Transactional
    public Gametype create(String name) {
        if (name == null) {
            throw new InvalidDataException("Invalid tournament");
        }

        Gametype gametype = new Gametype(name);
        gametype = gametypeRepository.save(gametype);
        return gametype;
    }

    @Transactional(readOnly = true)
    public Gametype get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Gametype> getAll() {
        return gametypeRepository.findAll();
    }

    @Transactional
    public Gametype update(Long id, String name) {
        Gametype gametype = findById(id);

        boolean changed = false;

        if (name != null) {
            gametype.setName(name);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return gametypeRepository.save(gametype);
    }

    @Transactional
    public void delete(Long id) {
        Gametype gametype = findById(id);
        gametypeRepository.delete(gametype);
    }

    @Transactional(readOnly = true)
    public Gametype findById(Long id) {
        Gametype gametype = gametypeRepository.findById(id).orElse(null);
        if (gametype == null) {
            throw new NotFoundException();
        }
        return gametype;
    }
}
