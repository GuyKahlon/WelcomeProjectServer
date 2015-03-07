package com.citi.innovaciti.welcome.daos;

import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.repositories.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Liron on 27/04/2014.
 */
@Service
public class HostDao {

    @Autowired
    private HostRepository hostRepository;

    public Host save(Host host){
        return hostRepository.save(host);
    }

    public List<Host> save(List<Host> hosts){
        return hostRepository.save(hosts);
    }

    public List<Host> getHosts(int page, int size){
        return hostRepository.findAll(new PageRequest(page,size)).getContent();
    }

    public List<Host> getActiveHosts(int page, int size){
        return hostRepository.findByActive(true, new PageRequest(page, size)).getContent();
    }

    public List<Host> getInActiveHosts(int page, int size){
        return hostRepository.findByActive(false, new PageRequest(page,size)).getContent();
    }

    public long getHostsCount(){
        return hostRepository.count();
    }

    public long getActiveHostsCount(){
        return hostRepository.countByActive(true);
    }

    public Host findById(long id){
        return hostRepository.findOne(id);
    }

    public void deleteAllHosts(){
        hostRepository.deleteAllInBatch();
    }

    public long countHostsByPhoneNumber(String phoneNumber) {
        return hostRepository.countByPhoneNumber(phoneNumber);
    }

    public int setHostActiveState(boolean active, Long hostId){
       return hostRepository.setHostActiveState(active, hostId);
    }
}
