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
package it.dontesta.spring.example.model.dao;

import it.dontesta.spring.example.model.Horse;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HorseRowMapper  implements RowMapper<Horse>{

	private static final String CHIP_ID_COLUMN_LABEL = "CHIP_ID";
	private static final String NAME_COLUMN_LABEL = "NAME";
	private static final String AGE_COLUMN_LABEL = "AGE";
	private static final String TYPE_COLUMN_LABEL = "TYPE";
	private static final String COLOR_MANTLE_COLUMN_LABEL = "COLOR_MANTLE";

	public HorseRowMapper() {
	}

	public Horse mapRow(ResultSet resultSet, int arg1) throws SQLException {
		Horse horse = new Horse();
		horse.setChipID(resultSet.getString(CHIP_ID_COLUMN_LABEL));
		horse.setName(resultSet.getString(NAME_COLUMN_LABEL));
		horse.setAge(resultSet.getString(AGE_COLUMN_LABEL));
		horse.setColorMantle(resultSet.getString(COLOR_MANTLE_COLUMN_LABEL));
		horse.setType(resultSet.getString(TYPE_COLUMN_LABEL));
		return horse;
	}
}