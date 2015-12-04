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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>TODO Décrire la classe AbstractOrder</p>
 *
 *
 */
public abstract class AbstractOrder implements Order {

	/**
	 * Champs sur lequel porte le tri 
	 */
	private String order ;

	/**
	 * Nom de l'alias de la table
	 */
	private String tableAliasName ;

	/**
	 * The collate mode to apply to the order
	 */
	private Collate collate;
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#clone()
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Order} object.
	 */
	@Override
	public abstract Order clone();

	/**
	 * TODO Décrire la méthode getSort de la classe AbstractOrder
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected abstract String getSort();

	/**
	 * Constructeur avec nom du champs
	 * <p>Constructeur private car l'utilisateur doit passer par la méthode of pour l'instancier</p>
	 *
	 * @param p_oField champs sur lequel s'applique le tri
	 */
	protected AbstractOrder( Field p_oField ) {
		this.order = p_oField.name();
	}
	
	/**
	 * Constructor
	 *
	 * @param p_oField field
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	protected AbstractOrder( Field p_oField, Collate p_oCollate ) {
		this.order = p_oField.name();
		this.collate = p_oCollate;
	}
	
	/**
	 * Constructeur avec nom du champs
	 * <p>Constructeur private car l'utilisateur doit passer par la méthode of pour l'instancier</p>
	 *
	 * @param p_oField champs sur lequel s'applique le tri
	 * @param p_sTableAliasName alias de la table
	 */
	protected AbstractOrder( Field p_oField, String p_sTableAliasName ) {
		this.order = p_oField.name();
		this.tableAliasName = p_sTableAliasName ;
	}
	
	/**
	 * Constructor
	 *
	 * @param p_oField field
	 * @param p_sTableAliasName alias
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	protected AbstractOrder( Field p_oField, String p_sTableAliasName, Collate p_oCollate ) {
		this.order = p_oField.name();
		this.tableAliasName = p_sTableAliasName ;
		this.collate = p_oCollate;
	}

	/**
	 * Constructeur avec le champs
	 * <p>Constructeur private car l'utilisateur doit passer par la méthode of pour l'instancier</p>
	 *
	 * @param p_sOrder ordre
	 */
	protected AbstractOrder( String p_sOrder ) {
		this.order = p_sOrder ;
	}

	/**
	 * Constructor
	 *
	 * @param p_sOrder order
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	protected AbstractOrder( String p_sOrder, Collate p_oCollate ) {
		this.order = p_sOrder ;
		this.collate = p_oCollate;
	}
	
	/**
	 * Constructeur avec le champs
	 * <p>Constructeur private car l'utilisateur doit passer par la méthode of pour l'instancier</p>
	 *
	 * @param p_sOrder ordre
	 * @param p_sTableAliasName alias de la table
	 */
	protected AbstractOrder( String p_sOrder, String p_sTableAliasName) {
		this.order = p_sOrder ;
		this.tableAliasName = p_sTableAliasName ;
	}
	
	/**
	 * Constructor
	 *
	 * @param p_sOrder order
	 * @param p_sTableAliasName alias
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	protected AbstractOrder( String p_sOrder, String p_sTableAliasName, Collate p_oCollate) {
		this.order = p_sOrder ;
		this.tableAliasName = p_sTableAliasName ;
		this.collate = p_oCollate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendToSQL( StringBuilder p_oSql, ToSqlContext p_oSqlContext ) {

		StringBuilder sOrder = new StringBuilder();
		if( this.tableAliasName != null ) {
			sOrder.append(this.tableAliasName);
			sOrder.append('.');
		}
		sOrder.append(this.order);

		String sFieldAlias = p_oSqlContext.getFieldAlias(sOrder.toString());
		if ( sFieldAlias != null ) {
			p_oSql.append(sFieldAlias);
		}
		else {
			p_oSql.append(sOrder.toString());
		}
		
		if (this.collate != null) {
			this.collate.appendToSql(p_oSql, p_oSqlContext);
		}
		
		p_oSql.append( getSort());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverOrderBy( StringBuilder p_oSql ) {

		p_oSql.append(this.order);
		
		if (this.collate != null) {
			this.collate.appendToSql(p_oSql, null);
		}
		
		p_oSql.append(getSort());
	}

	/**
	 * Retourne l'objet order
	 *
	 * @return Objet order
	 */
	protected String getOrder() {
		return this.order;
	}

	/**
	 * Retourne l'objet tableAliasName
	 *
	 * @return Objet tableAliasName
	 */
	protected String getTableAliasName() {
		return this.tableAliasName;
	}
	
	/**
	 * Returns the collate mode of the order by
	 *
	 * @return the collate mode of the order by
	 * @since 3.1.1
	 */
	protected Collate getCollate() {
		return this.collate;
	}
}
