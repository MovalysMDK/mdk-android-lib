/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

/**
 * <p>Enumération des mots clés SQL</p>
 *
 *
 * @since 2.5
 */
public enum SqlKeywords {

	/**
	 * Mot clé Count
	 */
	COUNT("COUNT"),
	/**
	 * Mot clé NOT IN
	 */
	NOT_IN("NOT IN"),
	/**
	 * Mot clé IN
	 */
	IN("IN"),
	/**
	 * Mot clé FROM
	 */
	FROM("FROM"),
	/**
	 * Mot clé ON
	 */
	ON("ON"),
	/**
	 * Mot clé LEFT OUTER JOIN
	 */
	LEFT_OUTER_JOIN("LEFT OUTER JOIN"),
	/**
	 * Mot clé INNER JOIN
	 */
	INNER_JOIN("INNER JOIN"),
	/**
	 * Mot clé DISTINCT
	 */
	DISTINCT("DISTINCT"), 
	/**
	 * Mot clé SELECT
	 */
	SELECT("SELECT"), 
	/**
	 * Mot clé WHERE
	 */
	WHERE("WHERE"),
	/**
	 * Mot clé WHERE
	 */
	HAVING("HAVING"), 
	/**
	 * Mot clé INSERT
	 */
	INSERT("INSERT"),
	/**
	 * Mot clé UPDATE
	 */
	UPDATE("UPDATE"),
	/**
	 * Mot clé INTO
	 */
	INTO("INTO"),
	/**
	 * Mot clé VALUES
	 */
	VALUES("VALUES"),
	/**
	 * Mot clé SET
	 */
	SET("SET"), 
	/**
	 * Mot clé BETWEEN
	 */
	BETWEEN("BETWEEN"), 
	/**
	 * Mot clé LIKE
	 */
	LIKE("LIKE"),
	/**
	 * Mot clé DELETE
	 */
	DELETE("DELETE"), 
	/**
	 * Mot clé NULL
	 */
	NULL("NULL"), 
	/**
	 * Mot clé AS
	 */
	AS("AS"),
	/**
	 * Mot clé UNION
	 */
	UNION("UNION"),
	/**
	 * Mot clé UNION ALL
	 */
	UNION_ALL("UNION ALL");

	/**
	 * Nom en SQL
	 */
	private String name;

	/**
	 * Contructor
	 * 
	 * @param p_sName mot clé SQL à utiliser
	 */
	private SqlKeywords(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Retourne le token SQL en format SQL
	 *
	 * @see java.lang.Enum#toString()
	 * @return le token en format SQL
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
