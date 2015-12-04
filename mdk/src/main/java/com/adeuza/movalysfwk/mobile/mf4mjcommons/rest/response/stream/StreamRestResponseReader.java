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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.EntityFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.AbstractEntityHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.gson.Unexposed;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 *
 * Lecture en streaming de la réponse
 * 
 * @param <R> response type
 */
public class StreamRestResponseReader<R extends IRestResponse> implements RestResponseReader<R> {

	/**
	 * Entity Helper
	 */
	private AbstractEntityHelper entityHelper = AbstractEntityHelper.getInstance();

	/**
	 * Map de la conf de serialization par class
	 * Class => Map<Nom de la propriété json => Field de la classe>
	 */
	private Map<Class,Map<String,FieldCacheElement>> mapClassFields = new HashMap<>();

	/**
	 * Définit un response processor par path.
	 * Quand le chemin correspond, le VDNStreamResponseProcessor est invoké (1 ou plusieurs fois en fonction du getPartSize())
	 * avec le résultat en cours. 
	 * Le résultat de la désérialization d'un node ayant un VDNStreamResponseProcessor n'est pas stocké dans l'objet global de réponse
	 * pour que celui-ci puisse être détruit par le GC.
	 */
	private Map<String,StreamResponseProcessor> mapStreamResponseProcessor = new HashMap<>();

	/**
	 * Rest response
	 */
	private Class<R> restResponse ;

	/**
	 * Generic type is restResponse is of type list
	 */
	private Type type ;
	
	/**
	 * 
	 */
	private boolean debug = false ;

	/**
	 * <p>Constructor for StreamRestResponseReader.</p>
	 *
	 * @param p_oRestResponse a {@link java.lang.Class} object.
	 */
	public StreamRestResponseReader( Class<R> p_oRestResponse ) {
		this.restResponse = p_oRestResponse ;
	}
	
	/**
	 * <p>Constructor for StreamRestResponseReader.</p>
	 *
	 * @param p_oRestResponse a {@link java.lang.Class} object.
	 * @param p_oType a {@link java.lang.reflect.Type} object.
	 */
	public StreamRestResponseReader( Class<R> p_oRestResponse, Type p_oType ) {
		this(p_oRestResponse);
		this.type = p_oType;
	}

	/**
	 *
	 */
	private static class Result {
		/**
		 * boolean qui indique si on doit valuer la propriété dans l'objet de reponse globale
		 */
		boolean setResult = false ;
		/**
		 * Resultat de deserialization d'un noeud du json
		 */
		Object result ;
	}

	/**
	 * <p>addStreamResponseProcessor.</p>
	 *
	 * @param p_sPath a {@link java.lang.String} object.
	 * @param p_oVDNStreamResponseProcessor a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream.StreamResponseProcessor} object.
	 */
	public void addStreamResponseProcessor( String p_sPath, StreamResponseProcessor p_oVDNStreamResponseProcessor ) {
		this.mapStreamResponseProcessor.put(p_sPath, p_oVDNStreamResponseProcessor);
	}


	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader#readResponse(Notifier, InputStream, MContext)
	 */
	@Override
	public R readResponse(Notifier p_oNotifier, InputStream p_oInputStream, MContext p_oContext) throws RestException {

		R r_oResponse = null;
		InputStreamReader oInputStreamReader = new InputStreamReader(p_oInputStream);
		try {
			r_oResponse = readResponse(oInputStreamReader, p_oNotifier, p_oContext);
		} finally {
			try {
				oInputStreamReader.close();
			} catch (IOException oIOException ) {
				throw new RestException("StreamRestResponseReader.readResponse error", oIOException);
			}
		}

		return r_oResponse;
	}
	
	/** {@inheritDoc} */
	@Override
	public R readResponse(Reader p_oReader, Notifier p_oNotifier,
			MContext p_oContext) throws RestException {
		
		long lStart = System.currentTimeMillis();
		R r_oResponse = this.createResponse();
		
		try {
			
			// Init des streams response processors
			for( StreamResponseProcessor oVDNStreamResponseProcessor: this.mapStreamResponseProcessor.values()) {
				oVDNStreamResponseProcessor.initialize(p_oContext);
			}
	
			MJsonMapper oJSonMapper = (MJsonMapper) BeanLoader.getInstance().getBean(MJsonMapper.class);
			MJsonReader oJsonReader = (MJsonReader) BeanLoader.getInstance().getBean(MJsonReader.class);
			oJsonReader.read(p_oReader);

			try {
				if ( List.class.isAssignableFrom(r_oResponse.getClass())) {
					if ( this.type instanceof Class ) {
						if ( this.debug) {
							Application.getInstance().getLog().debug("synchro", "  list treatment: generic type is class");
						}
						Result oResult = null;
						if ( MEntity.class.isAssignableFrom((Class) this.type)) {
							oResult = this.treatGenericTypeIsEntity(r_oResponse, (Class<MEntity>) this.type,
									oJsonReader, oJSonMapper, "root", p_oNotifier, p_oContext, "");
						}
						else {
							oResult = this.treatGenericTypeIsNotEntity(r_oResponse, (Class<?>) this.type,
									oJsonReader, oJSonMapper, "root", p_oNotifier, p_oContext, "");
						}
						((List)r_oResponse).addAll((List)oResult.result);
					}
				}
				else {
					treatJsonObject(r_oResponse, r_oResponse, oJsonReader, oJSonMapper, null, "root", p_oNotifier, p_oContext, "");
				}
			} finally {
				oJsonReader.close();
			}
			
			long lTime = System.currentTimeMillis() - lStart;
			Application.getInstance().getLog().debug("synchro", "Temps de traitement Stream: " + lTime + "ms");
			
		} catch (SynchroException | IOException | JsonException | IllegalAccessException | InstantiationException oException ) {
			throw new RestException("StreamRestResponseReader.readResponse error", oException);
		}
		return r_oResponse;
	}


	/**
	 * Treat a json object
	 *
	 * @param p_oObject object to value
	 * @param p_oJsonReader json reader
	 * @param oResponse a R object.
	 * @param p_oJsonMapper a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper} object.
	 * @param p_oObjectActualTypeArguments an array of {@link java.lang.reflect.Type} objects.
	 * @param p_sPath a {@link java.lang.String} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_sIndent a {@link java.lang.String} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	protected void treatJsonObject(R oResponse, Object p_oObject, MJsonReader p_oJsonReader, MJsonMapper p_oJsonMapper, 
			Type[] p_oObjectActualTypeArguments, String p_sPath, Notifier p_oNotifier, MContext p_oContext, String p_sIndent ) throws JsonException {
			String sName = null ;
		p_oJsonReader.beginObject();
		while (p_oJsonReader.notEndOfArrayOrObject()) {
			sName = p_oJsonReader.readPropertyName();
			this.treatJsonProperty(oResponse, p_oObject, sName, p_oJsonReader, p_oJsonMapper, p_oObjectActualTypeArguments, 
					p_sPath + '.' + sName, p_oNotifier, p_oContext, p_sIndent);
		}
		p_oJsonReader.endObject();
	}

	/**
	 * Treat a json property
	 *
	 * @param p_oObject object to value
	 * @param p_oObjectActualTypeArguments types generic de l'objet
	 * @param p_sPropertyName json property
	 * @param p_oJsonReader json reader
	 * @param p_sPath current path
	 * @param p_oContext context
	 * @param p_oResponse a R object.
	 * @param p_oJsonMapper a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @param p_sIndent a {@link java.lang.String} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	protected void treatJsonProperty(R p_oResponse, Object p_oObject, String p_sPropertyName, MJsonReader p_oJsonReader, MJsonMapper p_oJsonMapper,
			Type[] p_oObjectActualTypeArguments, String p_sPath, Notifier p_oNotifier, MContext p_oContext, String p_sIndent ) throws JsonException {
		try {
			FieldCacheElement oCacheField = findField(p_oObject, p_sPropertyName, p_oJsonMapper);
			if ( oCacheField != null ) {

				Field oField = oCacheField.getField();
				if ( this.debug ) {
					Application.getInstance().getLog().debug("synchro", p_sIndent+"treat json property: " + p_sPropertyName);
					Application.getInstance().getLog().debug("synchro", p_sIndent+"  corresponding field: " + oField.getName());
					Application.getInstance().getLog().debug("synchro", p_sIndent+"  path: " + p_sPath);
					Application.getInstance().getLog().debug("synchro", p_sIndent+"  type: " + oField.getType().getName());
				}

				Result oResult = new Result();

				if ( p_oJsonReader.isNullValue()) {
					p_oJsonReader.skipValue();
				}
				// Gestion des attributs simples
				else if ( Long.TYPE.isAssignableFrom(oField.getType()) || Long.class.isAssignableFrom(oField.getType())) {
					oResult.result = p_oJsonReader.readLong();
					oResult.setResult = true ;
				}
				else if ( Integer.TYPE.isAssignableFrom(oField.getType()) || Integer.class.isAssignableFrom(oField.getType())) {
					//FIX temporaire
					oResult.result = (int) p_oJsonReader.readDouble();
					//oResult.result = p_oJsonReader.nextInt();
					oResult.setResult = true ;
				}
				else
					if ( oField.getType().equals(String.class)) {
						oResult.result = p_oJsonReader.readString();
						oResult.setResult = true ;
					}
					else
						if ( Boolean.TYPE.isAssignableFrom(oField.getType()) || Boolean.class.isAssignableFrom(oField.getType())) {
							oResult.result = p_oJsonReader.readBoolean();
							oResult.setResult = true ;
						}
						else
							if ( Double.TYPE.isAssignableFrom(oField.getType()) || Double.class.isAssignableFrom(oField.getType())) {
								oResult.result = p_oJsonReader.readDouble();
								oResult.setResult = true ;
							}
							else
								if ( Timestamp.class.isAssignableFrom(oField.getType())) {
									oResult.result = new Timestamp(p_oJsonReader.readLong());
									oResult.setResult = true ;
								}
				//				Gestion des dates transmises sous forme de String.
				//				Le code fut crée pour le projet MesNotesDeFraisAndroid, mais la prise en charge du type s'effectue désormais dans [...]StreamProcessor.processResponsePart()
				//				Le code est conservé pour un besoin futur, sur un autre projet.
				//				else
				//				if ( Date.class.isAssignableFrom(oField.getType())) {
				//					oResult.result = new Date();					
				//					try { oResult.result = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a", Locale.US).parse(p_oJsonReader.nextString()); } 
				//					catch (ParseException e) { e.printStackTrace(); }					
				//					oResult.setResult = true ;
				//				}

				// Gestion des entités
								else
									if ( MEntity.class.isAssignableFrom(oField.getType())) {
										if ( this.debug) {
											Application.getInstance().getLog().debug("synchro", p_sIndent +"  entity treatment");
										}
										Class<MEntity> oEntityClass = (Class<MEntity>) oField.getType();
										MEntity oEntity = entityHelper.getFactoryForEntity(oEntityClass).createInstance();
										treatJsonObject(p_oResponse, oEntity, p_oJsonReader, p_oJsonMapper, null, p_sPath, p_oNotifier, p_oContext, p_sIndent + "  ");
										oResult.result = oEntity ;
										oResult.setResult = true ;
									}

				// Gestion des Enum
									else 
										if ( Enum.class.isAssignableFrom(oField.getType())) {

											Class<? extends Enum> enumType = (Class<? extends Enum>) oField.getType();
											Enum oEnum = enumType.getEnumConstants()[0];
											oResult.result = oEnum.fromBaseId(p_oJsonReader.readInt());			
											oResult.setResult = true ;
										}

				// Gestion des listes
										else
											if ( List.class.isAssignableFrom(oField.getType())) {
												ParameterizedType oParameterizedType = (ParameterizedType) oField.getGenericType();
												Type oGenericType = oParameterizedType.getActualTypeArguments()[0];
												if ( oGenericType instanceof Class ) {
													if ( this.debug) {
														Application.getInstance().getLog().debug("synchro", p_sIndent + "  list treatment: generic type is class");
													}
													if ( MEntity.class.isAssignableFrom((Class) oGenericType)) {
														oResult = this.treatGenericTypeIsEntity(p_oResponse, (Class<MEntity>) oGenericType,
																p_oJsonReader, p_oJsonMapper, p_sPath, p_oNotifier, p_oContext, p_sIndent);
													}
													else {
														oResult = this.treatGenericTypeIsNotEntity(p_oResponse, (Class<MEntity>) oGenericType,
																p_oJsonReader, p_oJsonMapper, p_sPath, p_oNotifier, p_oContext, p_sIndent);
													}
												}
												else if ( oGenericType instanceof TypeVariable ) {
													if ( this.debug) {
														Application.getInstance().getLog().debug("synchro", p_sIndent + "  list treatment: generic type is TypeVariable");
													}
													if ( MEntity.class.isAssignableFrom((Class)p_oObjectActualTypeArguments[0])) {
														oResult = this.treatGenericTypeIsEntity(p_oResponse, (Class<MEntity>)p_oObjectActualTypeArguments[0], 
																p_oJsonReader, p_oJsonMapper, p_sPath, p_oNotifier, p_oContext, p_sIndent);
													}
													else {
														Class<?> oClass = (Class<?>) p_oObjectActualTypeArguments[0];
														if ( this.debug) {
															Application.getInstance().getLog().debug("synchro", p_sIndent+"  not entity class: " + oClass.getName());
														}
														oResult = this.treatGenericTypeIsNotEntity(p_oResponse, oClass,
																p_oJsonReader, p_oJsonMapper, p_sPath, p_oNotifier, p_oContext, p_sIndent);
													}
												} else {
													Application.getInstance().getLog().debug("synchro", "skip value, unkown generic type, element type: " + oGenericType.getClass().getName() + ", path=" + p_sPath);
													p_oJsonReader.skipValue();
												}
											}

				// Objet (not list)
											else {
												Object oObject = oField.getType().newInstance();
												if ( oField.getGenericType() instanceof ParameterizedType) {
													if ( this.debug) {
														Application.getInstance().getLog().debug("synchro", p_sIndent +"  object treatment: generic type is a ParameterizedType");
													}
													ParameterizedType oParameterizedType = (ParameterizedType) oField.getGenericType();
													this.treatJsonObject(p_oResponse, oObject, p_oJsonReader, p_oJsonMapper, 
															oParameterizedType.getActualTypeArguments(), p_sPath, p_oNotifier, p_oContext, p_sIndent + "  ");
												}
												else {
													if ( this.debug) {
														Application.getInstance().getLog().debug("synchro", p_sIndent +"  object treatment: generic type is not a ParameterizedType");
													}
													this.treatJsonObject(p_oResponse, oObject, p_oJsonReader, p_oJsonMapper, null, p_sPath, p_oNotifier, p_oContext, p_sIndent + "  ");
												}
												oResult.result = oObject ;
												oResult.setResult = true ;
											}

				if ( oResult.setResult ) {
					oCacheField.getSetter().invoke(p_oObject, oResult.result);
				}
			}
			else {
				// N'est pas forcément une erreur (le champ peut etre Unexposed)
				Application.getInstance().getLog().debug("synchro", "Can't find field: " + p_sPropertyName + ", path: " + p_sPath
						+ ", object class: " + p_oObject.getClass().getName());
				p_oJsonReader.skipValue();
			}
		} catch( IOException | IllegalArgumentException | SecurityException | IllegalAccessException | 
				InvocationTargetException | InstantiationException | 
				SynchroException | RestException oException ) {
			throw new JsonException("Json error, path: " + p_sPath, oException);
		}
	}

	/**
	 * <p>treatGenericTypeIsEntity.</p>
	 *
	 * @param p_oEntityClass a {@link java.lang.Class} object.
	 * @param p_oJsonReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonReader} object.
	 * @param p_oGSonMapper a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper} object.
	 * @param p_sPath a {@link java.lang.String} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_sIndent a {@link java.lang.String} object.
	 * @param p_oResponse a R object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream.StreamRestResponseReader.Result} object.
	 * @throws java.io.IOException if any.
	 * @throws SynchroException if any.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 */
	protected Result treatGenericTypeIsEntity(R p_oResponse, Class<MEntity> p_oEntityClass, 
			MJsonReader p_oJsonReader, MJsonMapper p_oGSonMapper, String p_sPath, Notifier p_oNotifier, MContext p_oContext, String p_sIndent) throws IOException, SynchroException, JsonException {
		Result oListResult = new Result();
		if ( this.debug) {
			Application.getInstance().getLog().debug("synchro", p_sIndent +"  list entities");
			Application.getInstance().getLog().debug("synchro", p_sIndent+"  entity class: " + p_oEntityClass.getName());
		}
		EntityFactory<MEntity> oEntityFactory = entityHelper.getFactoryForEntity(p_oEntityClass);
		List<MEntity> listEntities = new ArrayList<MEntity>();
		p_oJsonReader.beginArray();
		StreamResponseProcessor oProcessor = this.mapStreamResponseProcessor.get(p_sPath);
		if ( oProcessor != null ) {
			oProcessor.onStartLoop(p_oResponse, p_oNotifier, p_oContext);
		}

		MEntity oEntity = null ;
		while( !p_oJsonReader.isEndArray()) {
			oEntity = oEntityFactory.createInstance();
			treatJsonObject(p_oResponse, oEntity, p_oJsonReader, p_oGSonMapper, null, p_sPath, p_oNotifier, p_oContext, p_sIndent + "  ");
			listEntities.add(oEntity);
			if ( oProcessor != null && listEntities.size() % oProcessor.getPartSize() == 0 ) {
				oProcessor.processResponsePart(listEntities, p_oResponse, p_oNotifier, p_oContext);
				listEntities.clear();
			}
		}
		p_oJsonReader.endArray();
		if ( oProcessor != null ) {
			oProcessor.processResponsePart(listEntities, p_oResponse, p_oNotifier, p_oContext);
			oProcessor.onEndLoop(p_oResponse, p_oNotifier, p_oContext);
		}
		else {
			oListResult.result = listEntities ;
			oListResult.setResult = true ;
		}
		return oListResult;
	}

	/**
	 * <p>treatGenericTypeIsNotEntity.</p>
	 *
	 * @param p_oObjectClass a {@link java.lang.Class} object.
	 * @param p_oJsonReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonReader} object.
	 * @param p_sPath a {@link java.lang.String} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_sIndent a {@link java.lang.String} object.
	 * @param p_oResponse a R object.
	 * @param p_oJSonMapper a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper} object.
	 * @param p_oNotifier a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream.StreamRestResponseReader.Result} object.
	 * @throws java.io.IOException if any.
	 * @throws SynchroException if any
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.JsonException if any.
	 * @throws java.lang.IllegalAccessException if any.
	 * @throws java.lang.InstantiationException if any.
	 * @throws RestException if any.
	 */
	protected Result treatGenericTypeIsNotEntity(R p_oResponse, Class<?> p_oObjectClass, 
			MJsonReader p_oJsonReader, MJsonMapper p_oJSonMapper, String p_sPath, Notifier p_oNotifier, MContext p_oContext, String p_sIndent) throws IOException, SynchroException, JsonException, InstantiationException, IllegalAccessException, RestException {
		Result oListResult = new Result();
		if ( this.debug) {
			Application.getInstance().getLog().debug("synchro", p_sIndent+"  not entity class: " + p_oObjectClass.getName());
		}
		if ( !p_oObjectClass.getName().startsWith("java.lang") && !p_oObjectClass.getName().startsWith("java.sql.Timestamp")) {
			List<Object> listObjects = new ArrayList<Object>();
			StreamResponseProcessor oProcessor = this.mapStreamResponseProcessor.get(p_sPath);
			p_oJsonReader.beginArray();
			if ( oProcessor != null ) {
				oProcessor.onStartLoop(p_oResponse, p_oNotifier, p_oContext);
			}

			Object oObject = null ;
			while( !p_oJsonReader.isEndArray()) {
				oObject = p_oJSonMapper.fromJson("{}", p_oObjectClass);
				treatJsonObject(p_oResponse, oObject, p_oJsonReader, p_oJSonMapper, null, p_sPath, p_oNotifier, p_oContext, p_sIndent + "  ");
				listObjects.add(oObject);
				if ( oProcessor != null && listObjects.size() % oProcessor.getPartSize() == 0 ) {
					oProcessor.processResponsePart(listObjects, p_oResponse, p_oNotifier, p_oContext);
					listObjects.clear();
				}
			}
			if ( oProcessor != null ) {
				oProcessor.processResponsePart(listObjects, p_oResponse, p_oNotifier, p_oContext);
				oProcessor.onEndLoop(p_oResponse, p_oNotifier, p_oContext);
			}
			else {
				oListResult.result = listObjects ;
				oListResult.setResult = true ;
			}
			p_oJsonReader.endArray();
		}
		else {
			p_oJsonReader.beginArray();
			if ( Long.class.isAssignableFrom(p_oObjectClass)) {
				oListResult.result = p_oJsonReader.readListOfLong();
				oListResult.setResult = true ;
			}
			else if ( String.class.isAssignableFrom(p_oObjectClass)) {
				oListResult.result = p_oJsonReader.readListOfString();
				oListResult.setResult = true ;
			}
			else if ( Boolean.class.isAssignableFrom(p_oObjectClass)) {
				oListResult.result = p_oJsonReader.readListOfBoolean();
				oListResult.setResult = true ;
			}
			else if ( Double.class.isAssignableFrom(p_oObjectClass)) {
				oListResult.result = p_oJsonReader.readListOfDouble();
				oListResult.setResult = true ;
			}
			else if ( Integer.class.isAssignableFrom(p_oObjectClass)) {
				oListResult.result = p_oJsonReader.readListOfInteger();
				oListResult.setResult = true ;
			}
			else {
				Application.getInstance().getLog().debug("synchro", p_sIndent + "  !! skip list of : " + p_oObjectClass.getName() + ", path=" + p_sPath);
				while( !p_oJsonReader.isEndArray()) {
					p_oJsonReader.skipValue();
				}
			}
			p_oJsonReader.endArray();
		}
		return oListResult;
	}

	/**
	 * <p>findField.</p>
	 *
	 * @param p_oObject a {@link java.lang.Object} object.
	 * @param p_sPropertyName a {@link java.lang.String} object.
	 * @param p_oGsonMapper a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream.FieldCacheElement} object.
	 * @throws JsonException if any.
	 */
	protected FieldCacheElement findField( Object p_oObject, String p_sPropertyName, MJsonMapper p_oGsonMapper ) throws JsonException {
		return getPropertyMap(p_oObject, p_oGsonMapper).get(p_sPropertyName);
	}

	/**
	 * <p>getPropertyMap.</p>
	 *
	 * @param p_oObject a {@link java.lang.Object} object.
	 * @param p_oJSonMapper a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.MJsonMapper} object.
	 * @return a {@link java.util.Map} object.
	 * @throws JsonException if any
	 */
	protected Map<String,FieldCacheElement> getPropertyMap( Object p_oObject, MJsonMapper p_oJSonMapper) throws JsonException {
		
		String sSerializationName = null ;
		String sMethodName = null ;
		Method oMethod = null ;
		FieldCacheElement oFieldCacheElement = null ;

		Map<String,FieldCacheElement> r_mapPropertyMap = mapClassFields.get(p_oObject.getClass());
		if ( r_mapPropertyMap == null ) {
			r_mapPropertyMap = new HashMap<>();
			mapClassFields.put(p_oObject.getClass(), r_mapPropertyMap);
			Class<?> oCurrentClass = p_oObject.getClass();
			while(oCurrentClass != null && oCurrentClass != Object.class) {
				for( Field oField: oCurrentClass.getDeclaredFields()) {
					// Le field ne doit pas etre unexposed
					
					if ( oField.getAnnotation(Unexposed.class) == null ) {
						sSerializationName = p_oJSonMapper.getSerializedName(oField);

						sMethodName = "set" + StringUtils.capitalize( oField.getName());
						
						try {
							oMethod = oCurrentClass.getDeclaredMethod(sMethodName, oField.getType());
						} catch (NoSuchMethodException | SecurityException e) {
							if (this.debug) {
								Application.getInstance().getLog().debug("Error when tying to find ", sMethodName + " on field " + oField.getName() + " of type " + oField.getType());
							}
							//throw new JsonException("Json error, find method: " + sMethodName,e);
						}

						oFieldCacheElement = new FieldCacheElement(oField, oMethod);
						r_mapPropertyMap.put(sSerializationName, oFieldCacheElement );
					}
				}
				oCurrentClass = oCurrentClass.getSuperclass();
			}
		}
		return r_mapPropertyMap ;
	}

	/**
	 * <p>Setter for the field <code>debug</code>.</p>
	 *
	 * @param p_bDebug a boolean.
	 */
	public void setDebug(boolean p_bDebug) {
		this.debug = p_bDebug;
	}


	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponseReader#createResponse()
	 */
	@Override
	public R createResponse() {
		R r_oResponse = null;
		try {
			r_oResponse = this.restResponse.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return r_oResponse;
	}
}
