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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;

/**
 * <p>Référence à un champs</p>
 *
 *
 */
public class SqlField implements Cloneable {

	/**
	 * Nom du champs
	 */
	private String name ;
	
	/**
	 * Alias de la table du champs 
	 */
	private String tablePrefix ;
	
	/**
	 * Alias du champs
	 */
	private int columnIndex = -1 ;

	/**
	 * SqlField référencé uniquement par son nom, pas préfixé avec l'alias de la table
	 *
	 * @param p_sName nom du champs
	 */
	public SqlField( String p_sName ) {
		this.name = p_sName ;
	}
	
	/**
	 * SqlField référencé uniquement par son nom, pas préfixé avec l'alias de la table
	 *
	 * @param p_oName champs
	 */
	public SqlField( Field p_oName ) {
		this.name = p_oName.name();
		this.columnIndex = p_oName.getColumnIndex();
	}
	
	/**
	 * SqlField référencé uniquement par son nom préfixé avec l'alias de la table
	 *
	 * @param p_oName champs
	 * @param p_sAlias alias de la table du champs
	 */
	public SqlField( Field p_oName, String p_sAlias ) {
		this.name = p_oName.name();
		this.columnIndex = p_oName.getColumnIndex();
		this.tablePrefix = p_sAlias ;
	}
	
	/**
	 * SqlField référencé uniquement par son nom préfixé avec l'alias de la table
	 *
	 * @param p_sName nom du champs
	 * @param p_sAlias nom de l'alias de la table
	 */
	public SqlField( String p_sName, String p_sAlias ) {
		this.name = p_sName ;
		this.tablePrefix = p_sAlias ;
	}

	/**
	 * SqlField référencé uniquement par son nom préfixé avec l'alias de la table
	 *
	 * @param p_sName nom du champs
	 * @param p_sTablePrefix nom de l'alias de la table
	 * @param p_iColumnIndex index de la colonne utilisé pour généré l'alias de la colonne
	 */
	protected SqlField( String p_sName, String p_sTablePrefix, int p_iColumnIndex) {
		this.name = p_sName ;
		this.tablePrefix = p_sTablePrefix ;
		this.columnIndex = p_iColumnIndex ;
	}
	
	/**
	 * Retourne l'objet name
	 *
	 * @return nom du champs
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retourne le prefix de la table
	 *
	 * @return prefix de la table
	 */
	public String getTablePrefix() {
		return this.tablePrefix;
	}
	
	/**
	 * clone de l'instance
	 *
	 * @see java.lang.Object#clone()
	 * @return clone de l'instance
	 */
	@Override
	public SqlField clone() {
		return new SqlField(this.name,this.tablePrefix, this.columnIndex);
	}

	/**
	 * Génère le SQL de la référence du champs
	 *
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {
		appendToSql(p_oSql, false, p_oToSqlContext);
	}
	
	/**
	 * Génère le SQL de la référence du champs
	 *
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public void toSqlWithFieldAlias( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {	
		appendToSql(p_oSql, true, p_oToSqlContext);
	}

		
	/**
	 * Génère le SQL de la référence du champs
	 *
	 * @param p_oStringBuilder StringBuilder à compléter
	 * @param p_bWithAlias a boolean.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	protected void appendToSql( StringBuilder p_oStringBuilder, boolean p_bWithAlias, ToSqlContext p_oToSqlContext) {
		
		StringBuilder sFieldAlias = null ;
		if ( p_bWithAlias && this.columnIndex != -1 ) {
			sFieldAlias = new StringBuilder(this.tablePrefix);
			sFieldAlias.append('_');
			sFieldAlias.append(Integer.toString(this.columnIndex));
		}

		this.appendFieldToSql(p_oStringBuilder, this.name, sFieldAlias, p_oToSqlContext);
	}

	/**
	 * TODO Décrire la méthode toSql de la classe SqlField
	 *
	 * @param p_oStringBuilder a {@link java.lang.StringBuilder} object.
	 * @param p_sFieldName a {@link java.lang.String} object.
	 * @param p_sFieldAlias a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	protected void appendFieldToSql( StringBuilder p_oStringBuilder, 
			String p_sFieldName, StringBuilder p_sFieldAlias, ToSqlContext p_oToSqlContext ) {
		
		StringBuilder sFieldName = new StringBuilder();
		if ( this.tablePrefix != null ) {
			sFieldName.append(this.tablePrefix);
			sFieldName.append('.');
		}
		sFieldName.append(p_sFieldName);
		
		p_oStringBuilder.append(sFieldName.toString());
		
		if ( p_sFieldAlias != null ) {
				
			p_oStringBuilder.append(' ');
			p_oStringBuilder.append(SqlKeywords.AS);
			p_oStringBuilder.append(' ');
			p_oStringBuilder.append(p_sFieldAlias);
			
			p_oToSqlContext.registerFieldAlias( sFieldName.toString(), p_sFieldAlias.toString());
		}
	}
	
}
