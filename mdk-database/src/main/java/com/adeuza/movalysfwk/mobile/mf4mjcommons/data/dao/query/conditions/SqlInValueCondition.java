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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.PairValue;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition IN avec les valeurs indiquées</p>
 *
 * <p>SqlInValueCondition est composée</p>
 * <ul>
 *   <li>De la liste des champs pour le IN</li>
 *   <li>De la liste des valeurs</li>
 * </ul>
 *
 * <p>Par défaut, le IN est découpé en bloc de 5 valeurs comme ceci : </p>
 * <pre>
 * {@code
 * CHAMPS1 IN (val1,val2,val3,val4,val5) OR CHAMPS1 IN (val6,val7,val8,val9,val10)...
 * }</pre>
 *
 * <p>Exemple :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * select intervention1.id from mit_intervention intervention1
 * where (intervention1.inventiontypeid, intervention1.code)
 * 	in ((1,'CODE001'),(2,'CODE002'),(3,'CODE003'))
 * }</pre>
 *
 * <p>Le code Java correspondant est le suivant :</p>
 * <pre>
 * {@code
 * List<SqlInField> listInFields = new ArrayList<SqlInField>();
 * listInFields.add(new SqlInField(InterventionField.INTERVENTIONTYPEID, InterventionDao.ALIAS_NAME, SqlType.INTEGER));
 * listInFields.add(new SqlInField(InterventionField.CODE, InterventionDao.ALIAS_NAME, SqlType.VARCHAR));
 *
 * List<Object> listValues = new ArrayList<Object>();
 * listValues.add(1);
 * listValues.add("CODE001");
 * listValues.add(2);
 * listValues.add("CODE002");
 * listValues.add(3);
 * listValues.add("CODE003");
 *
 * oSqlQuery.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.ID );
 * oSqlQuery.addToFrom(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 * oSqlQuery.addToWhere(new SqlInValueCondition(listInFields, listValues));
 * }
 * </pre>
 */
public class SqlInValueCondition extends AbstractSqlInCondition implements Cloneable {

	/**
	 * Nombre de valeurs dans un IN
	 */
	private static final int IN_CLAUSE_PREFERRED_SIZE = 5;
	
	/**
	 * Nombre de valeurs à ajouter pour compléter le dernier IN
	 */
	private int iNbValueToAdd = 0; 
	
	/**
	 * Liste des valeurs
	 */
	private List<?> values;
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, liste des valeurs
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 * @param p_listValues liste des valeurs
	 */
	public SqlInValueCondition(String p_sFieldName, String p_sAlias, SqlType p_oSqlType, List<?> p_listValues ) {
		setField(p_sFieldName, p_sAlias, p_oSqlType);
		this.values = p_listValues;
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, liste des valeurs
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 * @param p_listValues liste des valeurs
	 */
	public SqlInValueCondition(Field p_oField, String p_sAlias, SqlType p_oSqlType, List<?> p_listValues ) {
		setField(p_oField, p_sAlias, p_oSqlType);
		this.values = p_listValues;
	}
	
	/**
	 * Constructeur avec champs, liste des valeurs
	 *
	 * @param p_oSqlInField champs du IN
	 * @param p_listValues liste des valeurs
	 */
	public SqlInValueCondition(SqlInField p_oSqlInField, List<?> p_listValues ) {
		this.getListFields().add( p_oSqlInField);
		this.values = p_listValues;
	}

	/**
	 * Constructeur avec liste des champs, liste des valeurs
	 *
	 * @param p_listFields liste des champs
	 * @param p_listValues liste des valeurs
	 */
	public SqlInValueCondition(List<SqlInField> p_listFields, List<?> p_listValues) {
		this.setFields(p_listFields);
		this.values = p_listValues;
	}
	
	/**
	 * Constructeur avec liste des champs, alias de la table du champs, liste des valeurs
	 *
	 * @param p_listFields tableau de couples Champs/Type
	 * @param p_sAlias alias de la table des champs
	 * @param p_listValues liste des valeurs
	 */
	public SqlInValueCondition(PairValue<Field, SqlType>[] p_listFields, String p_sAlias, List<?> p_listValues) {
		for( PairValue<Field, SqlType> oPairValue: p_listFields) {
			this.getListFields().add( new SqlInField(oPairValue.getKey().name(), p_sAlias, oPairValue.getValue()));
		}
		this.values = p_listValues;
	}

	/**
	 * Constructeur avec liste des champs, liste des valeurs
	 *
	 * @param p_listFields tableau de couples Champs/Type
	 * @param p_listValues liste des valeurs
	 */
	public SqlInValueCondition(PairValue<Field, SqlType>[] p_listFields, List<?> p_listValues) {
		for( PairValue<Field, SqlType> oPairValue: p_listFields) {
			this.getListFields().add( new SqlInField(oPairValue.getKey().name(), oPairValue.getValue()));
		}
		this.values = p_listValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {

		toSql(p_oSql, true );
	}

	/**
	 * Append SQL to StringBuilder
	 * @param p_oSql sql
	 * @param p_bFieldNameWithAlias use alias for field name
	 */
	private void toSql(StringBuilder p_oSql, boolean p_bFieldNameWithAlias) {
		if (!this.values.isEmpty()) {
			int iNbFields;
			
			String sFieldName = buildFieldName(p_bFieldNameWithAlias).toString();
			
			String sFieldValue = getFieldValue();

			int iNbTupleInClause = 0;
			boolean bFirst = true;
			iNbFields = getNbFields();
			int iNbTUpples = this.values.size() / iNbFields;

			if ( (double)iNbTUpples / IN_CLAUSE_PREFERRED_SIZE > 1 ) {
				p_oSql.append('(');
			}
			
			for (int i = 0; i < iNbTUpples;i++) {	
				// Au bout de IN_CLAUSE_PREFERRED_SIZE éléments on insère une clause ") OR FIELD IN ("
				// ou AND FIELD NOT IN dans le cas où on veut faire un NOT IN ... 
				if (iNbTupleInClause < IN_CLAUSE_PREFERRED_SIZE && iNbTupleInClause > 0) {
					p_oSql.append(',');
				} else {
					iNbTupleInClause = 0;
				}

				// .. Insertion de la clause FIELD IN ()
				if (iNbTupleInClause == 0) {
					if (!bFirst) {
						if (isInverse()  ) {
							p_oSql.append(") AND ");
						}
						else  {
							p_oSql.append(") OR ");
						}
					}
					p_oSql.append(sFieldName);
					p_oSql.append(' ');
					if ( isInverse() ) {
						p_oSql.append(SqlKeywords.NOT_IN);
					} else {
						p_oSql.append(SqlKeywords.IN);
					}
					p_oSql.append(" ("); // Ouverture d'une clause IN
					bFirst = false;
				}

				// Compteur du nombre de valeur dans la clause ()
				iNbTupleInClause = iNbTupleInClause + 1;
				p_oSql.append(sFieldValue);
			}
						
			this.iNbValueToAdd = IN_CLAUSE_PREFERRED_SIZE - iNbTupleInClause;
			// On ajoute le padding (avec la dernière valeur de la liste) pour obtenir un IN multiple de IN_CLAUSE_PREFERRED_SIZE
			for (int iIndexValueToAdd = 0; iIndexValueToAdd < (this.iNbValueToAdd); iIndexValueToAdd++) {
				p_oSql.append(',');
				p_oSql.append(sFieldValue);
			}
			if (!bFirst) {
				p_oSql.append(')'); // Si au moins un IN, fermeture de la derniere clause IN générée
			}

			if ( (double)iNbTUpples / IN_CLAUSE_PREFERRED_SIZE > 1 ) {
				p_oSql.append(')');
			}
		}
	}

	/**
	 * Returns the field value
	 * @return the field value
	 */
	private String getFieldValue() {
		int iNbFields = getNbFields();
		String sFieldValue;
		if (iNbFields == 1) {
			sFieldValue = "?";
		} else {
			// Construction du format unitaire d'un upple : ? ou (?,?)
			StringBuilder sTuppleFormat = new StringBuilder();
			sTuppleFormat.append('(');
			for (int iRank=0 ; iRank<getListFields().size() ; iRank++) {
				sTuppleFormat.append('?');
				if (--iNbFields != 0) {
					sTuppleFormat.append(',');
				}
			}
			sTuppleFormat.append(')');
			sFieldValue = sTuppleFormat.toString();
		}
		return sFieldValue;
	}

	/**
	 * Build the field name
	 * @param p_bFieldNameWithAlias true if the field has an alias
	 * @return the field name
	 */
	private StringBuilder buildFieldName(boolean p_bFieldNameWithAlias) {
		StringBuilder sBuildFieldName = new StringBuilder();
		// Construction du t-upples CHAMPS1 ou (CHAMPS1,CHAMPS2)
		if (getNbFields() > 1) {
			sBuildFieldName.append('(');
		}
		int iNbFields = getNbFields();
		for (SqlInField oSqlInField : getListFields()) {
			if ( p_bFieldNameWithAlias && oSqlInField.getAlias() != null ) {
				sBuildFieldName.append(oSqlInField.getAlias());
				sBuildFieldName.append('.');
			}
			sBuildFieldName.append(oSqlInField.getFieldName());
			if (--iNbFields != 0) {
				sBuildFieldName.append(',');
			}
		}
		if (getNbFields() > 1) {
			sBuildFieldName.append(')');
		}
		return sBuildFieldName;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind the values of the In
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		if(!this.values.isEmpty()){
			Iterator<?> iterValues = this.values.iterator();
			while( iterValues.hasNext()) {
				for( SqlInField oSqlInField : getListFields()) {
					p_oStatementBinder.bind(iterValues.next(), oSqlInField.getSqlType());
				}
			}
			
			int iNbFields = this.getNbFields();
			for (int iCpt = 1; iCpt <= this.iNbValueToAdd; iCpt++) {
				int iIndex = 0;
				for (int iRank=0 ; iRank<getListFields().size() ; iRank++) {
					p_oStatementBinder.bind(this.values.get(iIndex), this.getListFields().get(iIndex % iNbFields).getSqlType());
					iIndex = iIndex +1;
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractSqlCondition clone() {
		List<SqlInField> listSqlInFields = new ArrayList<SqlInField>();
		for( SqlInField oSqlInField : this.getListFields()) {
			listSqlInFields.add( oSqlInField.clone());
		}
		AbstractSqlInCondition r_oSqlCondition = new SqlInValueCondition(listSqlInFields, this.values );
		r_oSqlCondition.setInverse(isInverse());
		return r_oSqlCondition ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		toSql(p_oSql, false );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		if(!this.values.isEmpty()){
			Iterator<?> iterValues = this.values.iterator();
			while( iterValues.hasNext()) {
				for( SqlInField oSqlInField : getListFields()) {
					p_oSqlArgsBuilder.addValue(iterValues.next(), oSqlInField.getSqlType());
				}
			}
			
			int iNbFields = this.getNbFields();
			for (int iCpt = 1; iCpt <= this.iNbValueToAdd; iCpt++) {
				for( int iIndex = 0 ; iIndex < getListFields().size(); iIndex++) {
					p_oSqlArgsBuilder.addValue(this.values.get(iIndex), 
						this.getListFields().get(iIndex % iNbFields).getSqlType());
				}
			}
		}
	}

}
