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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * Classe représentant une liste de messages.
 *
 */
@Scope(ScopePolicy.PROTOTYPE)
public class MessagesList implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8698410813156354246L;
	
	/**
	 * Liste stockant nos messages.
	 */
	private Map<MessageLevel, List<MessageContainer>> messages;

	/**
	 * Constructeur. Création d'une liste (non null) vide (on s'assure ainsi
	 * que la liste des messages ne soit jamais null)
	 */
	public MessagesList() {
		this.messages = new HashMap<MessageLevel, List<MessageContainer>>();
	}

	/**
	 * Retourne vrai si il existe au moins un message de niveau
	 * <code>p_levelId</code> dans la liste des messages. S'arrête au premier
	 * message correspondant au critère trouvé
	 *
	 * @return Un message d'un niveau particulier est présent dans notre liste.
	 * @param p_oLevel a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel} object.
	 */
	public boolean hasMessagesByLevel(MessageLevel p_oLevel) {
		return this.messages.containsKey(p_oLevel)
				&& !this.messages.get(p_oLevel).isEmpty();
	}

	/**
	 * Vérifie si notre liste de messages contient un message d'erreur
	 *
	 * @return Vrai si un message d'erreur a été trouvé.
	 */
	public boolean hasErrors() {
		return hasMessagesByLevel(MessageLevel.ERROR);
	}

	/**
	 * Permet d'ajouter un message juste en définissant les caractéristiques
	 *
	 * @param p_oMessage Objet représentant le message à ajouter - contient les données de typage du message (niveau, id, etc.)
	 * @param p_t_sArguments a {@link java.lang.String} object.
	 */
	public void addMessage(ItfMessage p_oMessage, String... p_t_sArguments) {
		this.addMessage(p_oMessage, p_oMessage.getSource(), p_t_sArguments);
	}

	/**
	 * Permet d'ajouter un message juste en définissant les caractéristiques
	 *
	 * @param p_oMessage Objet représentant le message à ajouter - contient les données de typage du message (niveau, id, etc.)
	 */
	public void addMessage(ItfMessage p_oMessage) {
		this.addMessage(p_oMessage, p_oMessage.getSource(), null);
	}

	/**
	 * Permet d'ajouter un message juste en définissant les caractéristiques
	 *
	 * @param p_oMessage Objet représentant le message à ajouter - contient les données de typage du message (niveau, id, etc.)
	 * @param p_iIndexErrorElement la position de l'élément devant contenir le message
	 */
	public void addMessage(ItfMessage p_oMessage, int p_iIndexErrorElement) {
		String sSource = p_oMessage.getSource() + "§" + String.valueOf(p_iIndexErrorElement);
		this.addMessage(p_oMessage, sSource, null);
	}

	/**
	 * Permet d'ajouter un message juste en définissant les caractéristiques
	 *
	 * @param p_oMessage Objet représentant le message à ajouter - contient les données de typage du message (niveau, id, etc.)
	 * @param p_iIndexErrorElement la position de l'élément devant contenir le message
	 * @param p_t_sArguments a {@link java.lang.String} object.
	 */
	public void addMessage(ItfMessage p_oMessage, int p_iIndexErrorElement, String... p_t_sArguments) {
		String sSource = p_oMessage.getSource() + "§" + String.valueOf(p_iIndexErrorElement);
		this.addMessage(p_oMessage, sSource, p_t_sArguments);
	}

	/**
	 * Permet d'ajouter un message juste en définissant les caractéristiques
	 * @param p_oMessage Objet représentant le message à ajouter - contient les données de typage du message (niveau, id, etc.)
	 * @param p_sSource la classe d'ou provient le message - si null, on utilise celle de p_oMessage
	 * @param p_sOptionalDescription texte donnant une explication à l'erreur - peut être à null
	 * @param p_mapFormatArgs maps de chaines qui peuvent êtres présentes dans le message (peux aider à la compréhension) - peut être à null
	 */
	private void addMessage(ItfMessage p_oMessage, String p_sSource, String[] p_t_sArguments) {
		this.addMessage(new MessageContainer(p_oMessage, p_sSource, p_t_sArguments));
	}

	private void addMessage(MessageContainer p_oMessage) {
		List<MessageContainer> listMessages = this.messages.get(p_oMessage.message.getLevel());
		if (listMessages == null) {
			listMessages = new ArrayList<MessageContainer>();
			this.messages.put(p_oMessage.message.getLevel(), listMessages);
		}

		listMessages.add(p_oMessage);
	}

	/**
	 * Permet d'ajouter une liste de messages à la liste courante
	 *
	 * @param p_oMessages a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList} object.
	 */
	public void addMessages(MessagesList p_oMessages){
		if (p_oMessages != null) {
			for (Collection<MessageContainer> oMessages: p_oMessages.messages.values()) {
				for (MessageContainer oMessage : oMessages) {
					this.addMessage(oMessage);
				}
			}
		}
	}

	/**
	 * Permet de savoir si la liste est vide des messages est vide
	 *
	 * @return a boolean.
	 */
	public boolean isEmpty() {
		return this.messages.size() == 0;
	}
	
	/**
	 * Vide la liste des messages.
	 */
	public void clear() {
		this.messages.clear();
	}
	/**
	 * Retourne une liste de message du niveau paramétré
	 *
	 * @param p_oLevelOfMessages niveau d'erreur souhaité
	 * @return null si aucun message , la liste des message sinon
	 */
	public List<MessageContainer> getMessagesContainerByLevel( MessageLevel p_oLevelOfMessages){
		return this.messages.get(p_oLevelOfMessages);
	}
	/**
	 * Retourne une liste de message du niveau paramétré
	 *
	 * @param p_oLevelOfMessages niveau d'erreur souhaité
	 * @return null si aucun message , la liste des message sinon
	 */
	public String getSourceMessagesByLevel( MessageLevel p_oLevelOfMessages){
		StringBuilder r_sConcatOfMsg = new StringBuilder();
		List<MessageContainer> oMessages= this.messages.get(p_oLevelOfMessages);
		if ( oMessages == null || oMessages.isEmpty() ){
			return null ; 
		} else {
			for ( MessageContainer oMsg : oMessages){
				r_sConcatOfMsg.append(oMsg.getSource());
			}
		}
		return r_sConcatOfMsg.toString() ;
	}

	/**
	 * <p>getMessagesByLevel.</p>
	 *
	 * @param p_oLevelOfMessages a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessageLevel} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<ApplicationR> getMessagesByLevel(MessageLevel p_oLevelOfMessages) {
		return null;
	}

	/**
	 * <p>copy.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MessagesList} object.
	 */
	public MessagesList copy() {
		MessagesList result = BeanLoader.getInstance().getBean(MessagesList.class);
		for(Entry<MessageLevel, List<MessageContainer>> oEntry : this.messages.entrySet()) {
			for(MessageContainer oMessage : oEntry.getValue()) {
				result.addMessage(oMessage);
			}
		}
		return result;
	}

//	/**
//	 * Description textuelle de l'instance
//	 * @see java.lang.Object#toString()
//	 * @return représentation sous forme de chaine du message
//	 */
//	public String toString(){//EXT_TRAD
//		StringBuilder r_oString = new StringBuilder();
//		r_oString.append("Messages list : ");
//		Iterator<List<MessageContainer>> iterMessagesByLevel	= this.messages.values().iterator();
//		Iterator<MessageContainer> iterMessages					= null;
//		MessageContainer oMessage								= null;
//		while (iterMessagesByLevel.hasNext()) {
//			iterMessages = iterMessagesByLevel.next().iterator();
//			while (iterMessages.hasNext()) {
//				oMessage = iterMessages.next();
//				r_oString.append("\n").append(oMessage.toString());
//			}
//		}
//		return r_oString.toString();
//	}
}
