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
package it.dontesta.spring.example.model.dao.intf;

import it.dontesta.spring.example.model.Horse;

import java.util.List;

public abstract interface IHorseDAO {
	public abstract void createHorse(String name, String age, String type, String colorMantle, String chipId);
    public abstract List<Horse> selectHorse(String name);
    public abstract List<Horse> selectAll();
    public abstract void deleteHorse(String name);
    public abstract void deleteAll();
    public abstract void createTable();
    public abstract void dropTable();
}
