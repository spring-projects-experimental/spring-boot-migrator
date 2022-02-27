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
package it.dontesta.spring.example.model.dao.impl;

import it.dontesta.spring.example.model.Horse;
import it.dontesta.spring.example.model.dao.HorseRowMapper;
import it.dontesta.spring.example.model.dao.intf.IHorseDAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HorseDAOImpl implements IHorseDAO {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS HORSE_RECORD ( "
		+ "\nID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"
		+ "\nCHIP_ID VARCHAR(15) UNIQUE NOT NULL,"
		+ "\nNAME VARCHAR(25) NOT NULL,"
		+ "\nAGE INT NOT NULL,"
		+ "\nTYPE VARCHAR(255) NOT NULL,"
		+ "\nCOLOR_MANTLE VARCHAR(255) NOT NULL"
		+ "\n)";

	private static final String SQL_DROP_TABLE = "DROP TABLE HORSE_RECORD";

	private JdbcTemplate jdbcTemplate;

	public HorseDAOImpl() {
	}

	@Override
	public void createHorse(String name, String age, String type,
			String colorMantle, String chipId) {
		jdbcTemplate
				.update("INSERT INTO HORSE_RECORD (NAME,AGE,TYPE,COLOR_MANTLE, CHIP_ID) VALUES (?,?,?,?,?)",
						new Object[] { name, age, type, colorMantle, chipId });
	}
	
	@Override
	public void createTable() {
		jdbcTemplate.update(SQL_CREATE_TABLE);
	}

	@Override
	public void deleteAll() {
		jdbcTemplate.update("DELETE FROM HORSE_RECORD");
	}

	@Override
	public void deleteHorse(String name) {
		jdbcTemplate.update("DELETE FROM HORSE_RECORD WHERE NAME=?",
				new Object[] { name, });
	}

	@Override
	public void dropTable() {
		jdbcTemplate.update(SQL_DROP_TABLE);
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<Horse> selectAll() {
		return jdbcTemplate.query("SELECT * FROM HORSE_RECORD",
				new HorseRowMapper());
	}

	@Override
	public List<Horse> selectHorse(String name) {
		return jdbcTemplate.query("SELECT ID, NAME, AGE, TYPE, COLOR_MANTLE, CHIP_ID FROM HORSE_RECORD WHERE NAME=?",
				new Object[] { name }, new HorseRowMapper());
	}

	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
