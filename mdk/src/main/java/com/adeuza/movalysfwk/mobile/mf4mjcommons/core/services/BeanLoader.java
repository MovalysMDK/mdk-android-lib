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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;

/**
 * <p>Service Loader</p>
 */
public final class BeanLoader {

	/** Constant <code>CLASS_PREFIX="class:"</code> */
	public static final String CLASS_PREFIX = "class:" ;
	
	/** Constant <code>BEANTYPE_PREFIX="type:"</code> */
	public static final String BEANTYPE_PREFIX = "type:" ;

	/** initiale map size */
	private static final int INIT_MAP_SIZE = 1024;
	
	/**
	 * Singleton instance
	 */
	private static BeanLoader instance = null;

	/**
	 * Map interface / keyword => singleton
	 */
	private Map<String, Object> singletonByKey;

	/**
	 * Map interface => implementation class
	 */
	private Map<String, Class<?>> prototypeByKey;
	
	/**
	 * Singleton, so private constructor
	 */
	private BeanLoader() {
		this.singletonByKey = new HashMap<>(INIT_MAP_SIZE);
		this.prototypeByKey = new HashMap<>(INIT_MAP_SIZE);
	}

	/**
	 * Return singleton instance
	 *
	 * @return singleton instance
	 */
	public static BeanLoader getInstance() {
		if (instance == null) {
			instance = new BeanLoader();
		}
		return instance;
	}
	
	/**
	 * <p>Retourne true si le bean dont l'identifiant envoyé en paramètre est définis, false sinon.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean hasDefinition(String p_sKey){
		boolean r_bResult = true;
		if ((!this.singletonByKey.containsKey(p_sKey) || this.singletonByKey.get(p_sKey) == null) 
				&& (!this.prototypeByKey.containsKey(p_sKey) || this.prototypeByKey.get(p_sKey) == null)) {
			r_bResult = false;
		}
		return r_bResult;
	}
	
	/**
	 * <p>hasDefinition.</p>
	 *
	 * @param p_oServiceType a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanType} object.
	 * @return a boolean.
	 */
	public boolean hasDefinition(BeanType p_oServiceType) {
		boolean r_bResult = true;
		if (this.singletonByKey.get(p_oServiceType.getName()) == null 
				&& this.prototypeByKey.get(p_oServiceType.getName()) == null) {
			r_bResult = false;
		}
		return r_bResult;
	}
		
	/**
	 * Return service instance by interface
	 *
	 * @param <T> service interface
	 * @param p_oClass service class
	 * @return service instance to use (singleton)
	 */
	public <T> T getBean(Class<T> p_oClass) {
		return this.getBean(p_oClass.getName(), p_oClass); 
	}

	/**
	 * <p>Return service instance by interface name. </p>
	 *
	 * @param p_sClassName The service class name. It must include the package.
	 * @return service instance to use (singleton)
	 */
	public Object getBean(String p_sClassName) {
		Class<?> oClazz;
		try {
			oClazz = Class.forName(p_sClassName);
		} catch (ClassNotFoundException e) {
			throw new BeanLoaderError("Impossible to found class "+p_sClassName+" : "+e.toString(), e);
		}
		return this.getBean(p_sClassName, oClazz);
	}
	
	/**
	 * <p>Cette classe permet de charger un bean grâce à un mot-clé définit dans la classe ExtBeanType.</p>
	 *
	 * @param p_oServiceType Enumération du BeanType
	 * @param p_oClass Le type du bean retourné par la méthode getBean
	 * @return le bean associé
	 * @param <T> a T object.
	 */
	public <T> T getBean(BeanType p_oServiceType, Class<T> p_oClass) {
		return this.getBean(p_oServiceType.getName() + "_" + p_oClass.getName(), p_oClass);
	}

	/**
	 * <p>Cette classe permet de charger un bean grâce à un mot-clé définit dans la classe ExtBeanType.</p>
	 *
	 * @param p_oServiceType Enumération de ExtBeanType
	 * @return le bean associé
	 */
	public Object getBean(BeanType p_oServiceType) {
		return this.getBeanByType(p_oServiceType.getName());
	}
	
	/**
	 * <p>Cette classe permet de charger un bean grâce à un mot-clé définit dans la classe ExtBeanType.</p>
	 *
	 * @param p_sServiceType service type
	 * @return le bean associé
	 */
	public Object getBeanByType(String p_sServiceType) {
		Object r_oT = this.singletonByKey.get(p_sServiceType);
		if ( r_oT == null ) {
			r_oT = this.instantiateSingleton(p_sServiceType);
		}
		return r_oT ;
	}
	
	
	/**
	 * <p>Cette classe permet de charger une classe. La méthode de chargement
	 * est indiqué en préfix. Pour le moment, seul class: et type: sont gérés </p>
	 *
	 * @param p_sChaine a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getBeanAutoDetectLoadMethod(String p_sChaine) {
		Object r_oObject = null ;
		if ( p_sChaine.startsWith(CLASS_PREFIX)) {
			r_oObject = getBean(p_sChaine.substring(CLASS_PREFIX.length()));
		}
		else
		if ( p_sChaine.startsWith(BEANTYPE_PREFIX)) {
			r_oObject = getBeanByType(p_sChaine.substring(BEANTYPE_PREFIX.length()));	
		}
		return r_oObject ;
	}
	
	/**
	 * Initialize AndroidBeanLoader
	 *
	 * @param p_oInputStream a {@link java.io.InputStream} object.
	 */
	public void initialize(InputStream p_oInputStream) {

		PropertyResourceBundle oPropertyResourceBundle;
		try {
			oPropertyResourceBundle = new PropertyResourceBundle(p_oInputStream);
			Enumeration<String> enumKeys = oPropertyResourceBundle.getKeys();

			String sKey = null;
			while (enumKeys.hasMoreElements()) {
				sKey = enumKeys.nextElement();
				this.initialize(sKey, oPropertyResourceBundle.getString(sKey));
			}
		}
		catch (IOException oIOException ) {
			throw new BeanLoaderError("BeanLoader: failed reading input stream", oIOException);
		}
	}

	/**
	 * Checks that <code>p_sClassName</code> represents a class and put it into the prototype map.
	 * There is not instantiation here: The iteration on properties does not convert their order definition;
	 * if a singleton must access to a another singleton, the BeanLoader can't garantee the existing of this bean.
	 * => BeanLoader executes instantiation when a getBean is performed.
	 * @param p_sKey the key of the prototype
	 * @param p_sClassName the class of the prototype to initialize
	 */
	private void initialize(String p_sKey, String p_sClassName) {
		try {
			Class<?> oClass = Class.forName(p_sClassName);
			this.prototypeByKey.put(p_sKey, oClass);
		}
		catch (ClassNotFoundException oClassNotFoundException) {
			throw new BeanLoaderError(new StringBuilder()
					.append("BeanLoader: failed to find service implementation class '")
					.append(p_sClassName)
					.append('\'')
					.toString(), oClassNotFoundException);
		}
	}	
	
	/**
	 * <p>Cette classe permet de charger un bean grâce à un mot-clé et un type de retour.</p>
	 * @param <T> type of in/output class
	 * @param p_sKey class of BeanType
	 * @param p_oClass Le type du bean retourné par la méthode getBean
	 * @return le bean associé
	 */
	@SuppressWarnings("unchecked")
	private <T> T getBean (String p_sKey, Class<T> p_oClass) {
		Object r_oT = this.singletonByKey.get(p_sKey);
		if (r_oT == null) {
			if (this.isSingleton(p_oClass)) {
				r_oT = this.instantiateSingleton(p_sKey);
			}
			else {
				r_oT = this.instantiatePrototype(p_sKey);
			}
		}
		return (T) r_oT;
	}

	/**
	 * Returns <code>true</code> if an interface has not "Scope" annotation or Scope value is "SINGLETON" 
	 * 
	 * @param p_oClass an interface
	 * @return <code>true</code> if a class has not "Scope" annotation or Scope value is "SINGLETON" 
	 */
	private boolean isSingleton(Class<?> p_oClass) {
		final Scope oScope = this.findScope(p_oClass);
		return oScope == null || ScopePolicy.SINGLETON == oScope.value();
	}
	
	/**
	 * Find the scope of the parameter class
	 * @param p_oClass the class to find the scope
	 * @return the Scope of the parameter class
	 */
	private Scope findScope(Class<?> p_oClass) {
		Scope r_oScope = p_oClass.getAnnotation(Scope.class);
		if (r_oScope == null) {
			if (p_oClass.getInterfaces().length>0) {
				for(Class<?> oInterface : p_oClass.getInterfaces()) {
					r_oScope = this.findScope(oInterface);
					if (r_oScope != null) {
						break;
					}
				}
			}

			if (r_oScope == null && p_oClass.getSuperclass() != null) {
				r_oScope = this.findScope(p_oClass.getSuperclass());
			}
		}
		return r_oScope;
	}
	
	/**
	 * Return the bean associated with the entry key
	 * @param <T> the type of return been
	 * @param p_sKey the key of the bean (in property file)
	 * @return the associated bean
	 */
	private <T> T instantiateSingleton(String p_sKey) {
		T r_oSingleton = this.<T>instantiatePrototype(p_sKey);
		this.singletonByKey.put(p_sKey, r_oSingleton);

		if (Initializable.class.isAssignableFrom(r_oSingleton.getClass())) {
			((Initializable) r_oSingleton).initialize();
		}

		return r_oSingleton;
	}

	/**
	 * <p>instantiatePrototype.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public <T> T instantiatePrototype(String p_sKey) {
		T r_oInstance = null;

		@SuppressWarnings("unchecked")
		Class<T> oClass = (Class<T>) this.prototypeByKey.get(p_sKey);
		if (oClass == null) {
			throw new BeanLoaderError("Prototype implementation not found for key " + p_sKey + "[" + this.prototypeByKey.size() + "]");
		}
		else {
			try {
				r_oInstance = oClass.newInstance();
			}
			catch (InstantiationException oInstantiationException) {
				throw new BeanLoaderError(new StringBuilder()
						.append("BeanLoader: failed to instanciate service implementation class '")
						.append(oClass.getName())
						.append('\'')
						.toString(), oInstantiationException);
			}
			catch (IllegalAccessException oIllegalAccessException) {
				throw new BeanLoaderError(new StringBuilder()
						.append("BeanLoader: failed to access service implementation class '")
						.append(oClass.getName())
						.append('\'')
						.toString(), oIllegalAccessException);
			}
		}
		return r_oInstance;
	}
	
	/**
	 * Return bean class with type.
	 * @param p_sKey key
	 * @return bean class
	 */
	public <T extends Class<?>> T getBeanClassByType(String p_sKey) {
		T r_oClass = (T) this.prototypeByKey.get(p_sKey);
		if (r_oClass == null) {
			throw new BeanLoaderError("Prototype implementation not found for key " 
						+ p_sKey + "[" + this.prototypeByKey.size() + "]");
		}
		return r_oClass;
	}
	
	/**
	 * Instantiate a prototype using a constructor which will take the arguments sent as parameters.
	 * This would raise a {@link BeanLoaderError} should an error happen during the process.
	 * @param p_sKey the key of the prototype as in your beans file
	 * @param p_oConstructorArgsType the list of arguments' classes
	 * @param p_oConstructorArgs the list of arguments' values
	 * @param <T> a T object.
	 * @return the instance of the prototype
	 */
	public <T> T instantiatePrototypeFromConstructor(String p_sKey, Class<?>[] p_oConstructorArgsType, Object[] p_oConstructorArgs) {
		T r_oInstance = null;
		@SuppressWarnings("unchecked")
		Class<T> oClass = (Class<T>) this.prototypeByKey.get(p_sKey);
		if (oClass == null) {
			throw new BeanLoaderError("Prototype implementation not found for key " + p_sKey + "[" + this.prototypeByKey.size() + "]");
		} else {
			try {
				Constructor<T> oConstructor = oClass.getConstructor(p_oConstructorArgsType);
				r_oInstance = oConstructor.newInstance(p_oConstructorArgs);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new BeanLoaderError(new StringBuilder()
						.append("BeanLoader: failed to find constructor on class '")
						.append(oClass.getName())
						.append('\'')
						.toString(), e);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new BeanLoaderError(new StringBuilder()
						.append("BeanLoader: failed to instanciate class '")
						.append(oClass.getName())
						.append('\'')
						.append(" via constructor")
						.toString(), e);
			}
		}
		return r_oInstance;
	}
	
	/**
	 * Get all singleton of type p_oInterface
	 *
	 * @param p_oInterface the type of singleton
	 * @return a list of singleton
	 * @param <T> a T object.
	 */
	public <T> List<T> getSingletons(Class<T> p_oInterface) {
		List<T> r_oSingletons = new ArrayList<T>();
		for(Object object : this.singletonByKey.values()) {
			if (p_oInterface.isAssignableFrom(object.getClass())) {
				r_oSingletons.add((T) object);
			}
		}
		return r_oSingletons;
	}

	/**
	 * Clears all singleton references.
	 */
	public void clear() {
		this.singletonByKey.clear();
		this.prototypeByKey.clear();
	}
}
