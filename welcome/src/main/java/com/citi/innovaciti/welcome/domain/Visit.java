package com.citi.innovaciti.welcome.domain;


import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 26/11/14
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "visits", indexes = {
        @Index(name = "time_idx", columnList = "time")
})
public class Visit {

    @Id
    @GeneratedValue
    private long id;

    private long time;

    @ManyToOne
    private Host host;

    @ManyToOne
    private Guest guest;


    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}
