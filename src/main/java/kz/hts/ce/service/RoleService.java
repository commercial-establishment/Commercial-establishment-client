package kz.hts.ce.service;

import kz.hts.ce.entity.Role;
import kz.hts.ce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, RoleRepository> {

    @Autowired
    protected RoleService(RoleRepository repository) {
        super(repository);
    }

    public Role findByName(String name){
        return repository.findByName(name);
    }
}
