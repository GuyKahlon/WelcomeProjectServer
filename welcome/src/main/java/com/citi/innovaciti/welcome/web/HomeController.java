package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.domain.Host;
import com.citi.innovaciti.welcome.repositories.HostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    @Autowired
    private HostRepository hostRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );

        Host host = new Host();
        host.setFirstName("Liron");
        host.setLastName("Netzer");
        hostRepository.save(host);

        Host host2 = new Host();
        host2.setFirstName("Kfir");
        host2.setLastName("Tishbi");
        hostRepository.save(host2);


        List<Host> hosts = hostRepository.findByFirstName("Liron");
        model.addAttribute("host",hosts.get(0).getFirstName()) ;

       long hostsCount = hostRepository.count();
        model.addAttribute("NumOfHosts",hostsCount) ;

		return "home";
	}
	
}
