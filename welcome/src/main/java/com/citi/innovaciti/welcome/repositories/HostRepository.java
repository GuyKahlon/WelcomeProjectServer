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

/**
 * Created by Liron on 27/04/2014.
 */
public interface HostRepository extends JpaRepository<Host, Long> {

    public List<Host> findByFirstName(String firstName);

    public Page<Host> findByActive(boolean active, Pageable pageable);

    public long countByActive(boolean active);

    public long countByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query("update Host h set h.active =:active where h.id =:hostId")
    public int setHostActiveState(@Param("active") boolean active, @Param("hostId")Long hostId);
}
