package com.citi.innovaciti.welcome.repositories;

import com.citi.innovaciti.welcome.domain.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Liron on 27/04/2014.
 */
public interface HostRepository extends JpaRepository<Host, Long> {

    public List<Host> findByFirstName(String firstName);

    public List<Host> findByFirstNameAndLastName(String firstName, String lastName);

    public Page<Host> findByActiveOrderByFirstNameAscLastNameAsc(boolean active, Pageable pageable);

    public long countByActive(boolean active);

    public long countByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query("update Host h set h.active =:active where h.id =:hostId")
    public int setHostActiveState(@Param("active") boolean active, @Param("hostId")Long hostId);


    @Modifying
    @Transactional
    @Query("update Host h set h.active = false where h.id not in :hostIds")
    public int deactivateHostsNotInIdsSet(@Param("hostIds")Set<Long> hostIds);
}
