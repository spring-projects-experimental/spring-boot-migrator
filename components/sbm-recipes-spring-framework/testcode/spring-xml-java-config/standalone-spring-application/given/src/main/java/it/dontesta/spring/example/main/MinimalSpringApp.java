/*
 * #%L
 * Standalone Spring Application
 * %%
 * Copyright (C) 2014 Antonio Musarra's Blog
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
/**
 * 
 */
package it.dontesta.spring.example.main;

import it.dontesta.spring.example.config.SpringContext;
import it.dontesta.spring.example.model.Horse;
import it.dontesta.spring.example.model.dao.intf.IHorseDAO;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author musarra
 *
 */
public class MinimalSpringApp {

	static Log log = LogFactory.getLog(MinimalSpringApp.class.getName());
	
	public static void main(String[] args) throws Exception {
		
	    GenericApplicationContext ctx = SpringContext.getContext();
	    BeanFactory factory = ctx;
	    IHorseDAO horseDAO = (IHorseDAO) factory.getBean("horseDAOImpl"); 
	    
	    log.info("Create a Horse table...");
	    horseDAO.createTable();
	    
	    log.info("Adding Horse data object...");
	    horseDAO.createHorse("Shirus", "9", "Quarab", "Grigio", 
	    		RandomStringUtils.random(15, false, true));
	    horseDAO.createHorse("Eclisse", "13", "Appalousa", "Pezzato con coperta", 
	    		RandomStringUtils.random(15, false, true));
	    horseDAO.createHorse("Morgana", "6", "Maremmana", "Baia", 
	    		RandomStringUtils.random(15, false, true));
	    horseDAO.createHorse("Macchia", "12", "Appalousa", "Morello", 
	    		RandomStringUtils.random(15, false, true));
	    
	    log.info("Retrieving data..");
		for(Horse currentHorse : horseDAO.selectAll()){
			log.info("From DB: " + currentHorse.toString());
		}

		log.info("Deleting record macchia and viewing...");
		horseDAO.deleteHorse("macchia");
		for(Horse currentHorse : horseDAO.selectAll()){
			log.info("From DB: " + currentHorse.toString());
		}

		log.info("Adding a new record and viewing...");
		horseDAO.createHorse("Furia", "10", "Appalousa", "Morello",
				RandomStringUtils.random(15, false, true));
		for(Horse currentHorse : horseDAO.selectAll()){
			log.info("From DB: " + currentHorse.toString());
		}

		log.info("Deleting everything and viewing...");
		horseDAO.deleteAll();
		for(Horse currentHorse : horseDAO.selectAll()){
			log.info("From DB: " + currentHorse.toString());
		}
		
	    log.info("Drop a Horse table...");
	    horseDAO.dropTable();

	}
}
