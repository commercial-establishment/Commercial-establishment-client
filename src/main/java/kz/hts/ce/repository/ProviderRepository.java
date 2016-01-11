package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProviderRepository extends RevisionRepository<Provider, UUID, Integer>, JpaRepository<Provider, UUID> {

    Provider findByUsername(String username);

    Provider findByCompanyName(String name);

    List<Provider> findByRole_Name(String roleName);

    Provider findByUsernameAndBlocked(String username, boolean blocked);

    @Transactional
    @Modifying
    @Query("UPDATE Provider a set a.password = ?1 where a.id = ?2")
    void updatePasswordById(String password, UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Provider a set a.blocked = TRUE where a.id = ?1")
    void lockById(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Provider a set a.blocked = FALSE where a.id = ?1")
    void reestablishById(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Provider a set a.startWorkDate = ?1, a.endWorkDate = ?2 where a.id = ?3")
    void updateStartAndEndWorkDate(Date startWorkDate, Date endWorkDate, UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Provider a set a.endWorkDate = ?1 where a.id = ?2")
    void updateEndWorkDate(Date endWorkDate, UUID id);
}
