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
package com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;

/**
 * <p>Permet d'enregistrer une méthode de callback sur l'évènement de rechargement d'un dataloader</p>
 *
 *
 * @param Class&lt;? extends Dataloader&lt;?&gt;&gt; : la classe du dataloader à enregistrer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerOnDataLoaderReload {

	/**
	 * le type du dataloader associé
	 *
	 * @return le type du dataloader associé
	 */
	public Class<? extends Dataloader<?>> value();
}
