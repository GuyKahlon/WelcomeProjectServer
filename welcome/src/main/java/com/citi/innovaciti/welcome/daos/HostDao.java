package com.citi.innovaciti.welcome.daos;

import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.repositories.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Liron on 27/04/2014.
 */
@Service
public class HostDao {

    @Autowired
    private HostRepository hostRepository;

    public Host save(Host host) {
        return hostRepository.save(host);
    }

    public List<Host> save(List<Host> hosts) {
        return hostRepository.save(hosts);
    }

    public List<Host> getHosts(int page, int size) {
        return hostRepository.findAll(new PageRequest(page, size)).getContent();
    }

    public List<Host> getActiveHosts(int page, int size) {
        return hostRepository.findByActive(true, new PageRequest(page, size)).getContent();
    }

    public List<Host> getInActiveHosts(int page, int size) {
        return hostRepository.findByActive(false, new PageRequest(page, size)).getContent();
    }

    public long getHostsCount() {
        return hostRepository.count();
    }

    public long getActiveHostsCount() {
        return hostRepository.countByActive(true);
    }

    public Host findById(long id) {
        return hostRepository.findOne(id);
    }

    public void deleteAllHosts() {
        hostRepository.deleteAllInBatch();
    }

    public long countHostsByPhoneNumber(String phoneNumber) {
        return hostRepository.countByPhoneNumber(phoneNumber);
    }

    public int setHostActiveState(boolean active, Long hostId) {
        return hostRepository.setHostActiveState(active, hostId);
    }


    /**
     * check if the given host exists in the DB
     * ("exists" means that either the db contains a record with same firstName+lastName+phoneNumber
     * or with the same firstName+lastName+email)
     * and if so, update it, otherwise create a new host.
     * @param host - host to create/update
     * @return  the id of the host that was created/updated
     */
    public Long createOrUpdateHost(Host host) {

        List<Host> hostsWithSameName = hostRepository.findByFirstNameAndLastName(host.getFirstName(), host.getLastName());

        //the given host is a new host
        if (hostsWithSameName == null || hostsWithSameName.size() == 0) {
            //host doesn't exist, so create it
            Host dbHost = save(host);
            return dbHost.getId();
        }

        //host exist with same properties in the DB, do nothing
        for (Host hostFromDb : hostsWithSameName) {
            if (hostFromDb.equals(host)) {
                //no update is needed
                return hostFromDb.getId();
            }
        }

        //host exist with same properties in the DB, but is deactivated, so activate it
        for (Host hostFromDb : hostsWithSameName) {
            if (hostFromDb.equalsExceptForActive(host)) {
                //update active state to active
                setHostActiveState(true, hostFromDb.getId());
                return hostFromDb.getId();
            }
        }

        //host with same firstName, LastName and email exist in the DB, so "merge" them
        for (Host hostFromDb : hostsWithSameName) {
            if (hostFromDb.getEmail().equals(host.getEmail()) && !hostFromDb.getPhoneNumber().equals(host.getPhoneNumber())) {
                //this is the same host (firstName, LastName and email are equal)
                hostFromDb.merge(host);
                save(hostFromDb);
                return hostFromDb.getId();
            }
        }

        //host with same firstName, LastName and phoneNumber exist in the DB, so "merge" them
        for (Host hostFromDb : hostsWithSameName) {
            if (hostFromDb.getPhoneNumber().equals(host.getPhoneNumber()) && !hostFromDb.getEmail().equals(host.getEmail())) {
                //this is the same host (firstName, LastName and phoneNumber are equal)
                hostFromDb.merge(host);
                save(hostFromDb);
                return hostFromDb.getId();
            }
        }

        //host with same firstName and LastName exist in the DB,
        // but both his email and phoneNumber are different, so create new Host
        Host dbHost = save(host);
        return dbHost.getId();

    }

    public int deactivateHostsNotInIdsSet(Set<Long> hostIds) {
        return hostRepository.deactivateHostsNotInIdsSet(hostIds);
    }


    /**
     *
     * @param hosts - hosts to override with the existing hosts in the DB
     * @return an array in which the
     * first position contains the number of hosts that were either created or updated
     * and the second position contains the number of hosts that were deactivated
     */
    @Transactional
    public int[] overrideDbHostsWithGivenHosts(List<Host> hosts) {

        Set<Long> hostIdsThatWereCreatedOrUpdated = new HashSet<Long>();

        for (Host host : hosts) {

            Long hostId = createOrUpdateHost(host);

            hostIdsThatWereCreatedOrUpdated.add(hostId);

        }

        //deactivate all hosts that exist in the DB, but not in excel
        int deactivatedCount = deactivateHostsNotInIdsSet(hostIdsThatWereCreatedOrUpdated);

        int[] overrideResult = {hostIdsThatWereCreatedOrUpdated.size(), deactivatedCount};

        return overrideResult;


    }
}
