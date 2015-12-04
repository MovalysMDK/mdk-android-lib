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
package com.adeuza.movalysfwk.mobile.mf4android.application;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;


/**
 * <p>Enumération pour la gestion des éléments du R.java Android dans le framework Movalys.</p>
 *
 *
 * @since Annapurna
 */
public enum AndroidApplicationR implements ApplicationR {

	// runtime permission
	/** R.string.fwk_not_mandatory_permission */
	fwk_not_mandatory_permission(ApplicationRGroup.ARRAY, "fwk_not_mandatory_permission"),
	
	//-- ALERTS
	/** R.string.alert_map_missing */
	alert_map_missing(ApplicationRGroup.STRING, "alert_map_missing"),
	/** R.string.alert_market_noapp */
	alert_market_noapp(ApplicationRGroup.STRING, "alert_market_noapp"),
	/** R.string.alert_phonecall_notiddle */
	alert_phonecall_notiddle(ApplicationRGroup.STRING, "alert_phonecall_notiddle"),
	/** R.string.actiondowriteemail_chooser */
	actiondowriteemail_chooser(ApplicationRGroup.STRING, "actiondowriteemail_chooser"),

	/** R.string.close_application_popup_title */
	close_application_popup_title(ApplicationRGroup.STRING, "close_application_popup_title"),
	/** R.string.close_application_popup_text */
	close_application_popup_text(ApplicationRGroup.STRING, "close_application_popup_text"),
	/** R.string.close_application_popup_OK_button */
	close_application_popup_OK_button(ApplicationRGroup.STRING, "close_application_popup_OK_button"),
	/** R.string.close_application_popup_KO_button */
	close_application_popup_KO_button(ApplicationRGroup.STRING, "close_application_popup_KO_button"),

	/** R.screen_parameters */
	screen_parameters(ApplicationRGroup.XML, "screen_parameters"),

	/** R.layout.fwk_screen_redirect_to_parameter_main */
	fwk_screen_redirect_to_parameter_main(ApplicationRGroup.LAYOUT, "fwk_screen_redirect_to_parameter_main"),
	/** R.id.screen_redirect_to_parameter_param_button */
	screen_redirect_to_parameter_param_button(ApplicationRGroup.ID, "screen_redirect_to_parameter_param_button"),
	/** R.id.screen_redirect_to_parameter_stop_button */
	screen_redirect_to_parameter_stop_button(ApplicationRGroup.ID, "screen_redirect_to_parameter_stop_button"),
	
	/** R.layout.fwk_screen_reset_settings_and_exit */
	fwk_screen_reset_settings_and_exit(ApplicationRGroup.LAYOUT, "fwk_screen_reset_settings_and_exit"),	
	/** R.id.screen_reset_settings_and_exit_ok_button */
	screen_reset_settings_and_exit_ok_button(ApplicationRGroup.ID, "screen_reset_settings_and_exit_ok_button"),

	
	
	/** R.id.fwk_screen_question */
	fwk_screen_question(ApplicationRGroup.ID, "screen_question"),
	/** R.id.screen_question_text */
	screen_question_text(ApplicationRGroup.ID, "screen_question_text"),
	/** R.id.screen_question_button_layout */
	screen_question_button_layout(ApplicationRGroup.ID, "screen_question_button_layout"),
	/** R.layout.fwk_component_button_question */
	fwk_component_button_question(ApplicationRGroup.LAYOUT, "fwk_component_button_question"),
	/** R.layout.fwk_component__simplespinner_text*/
	fwk_component__simplespinner_text(ApplicationRGroup.LAYOUT, "fwk_component__simplespinner_text"),
	
	component_customfields__edittext__value(ApplicationRGroup.ID, "component_customfields__edittext__value"),
	
	/** R.layout.fwk_component__simplespinner_spinitem*/
	fwk_component__simplespinner_spinitem(ApplicationRGroup.LAYOUT, "fwk_component__simplespinner_spinitem"),
	/** R.layout.fwk_component__simple_spinner_emptyitem__master*/
	fwk_component__simple_spinner_emptyitem__master(ApplicationRGroup.LAYOUT, "fwk_component__simple_spinner_emptyitem__master"),
	/** R.id.component__simple_spinner_emptyitem__value */
	component__simple_spinner_emptyitem__value(ApplicationRGroup.ID, "simple_spinner_emptyitem__value"),
	/** R.layout.fwk_component__simple_spinner_dropdown_emptyitem__master*/
	fwk_component__simple_spinner_dropdown_emptyitem__master(ApplicationRGroup.LAYOUT, "fwk_component__simple_spinner_dropdown_emptyitem__master"),
	/** R.id.component__simple_spinner_dropdown_emptyitem__master*/
	component__simple_spinner_dropdown_emptyitem__master(ApplicationRGroup.ID, "component__simple_spinner_dropdown_emptyitem__master"),
	/** R.id.simple_spinner_dropdown_emptyitem__value */
	component__simple_spinner_dropdown_emptyitem__value(ApplicationRGroup.ID, "simple_spinner_dropdown_emptyitem__value"),
	/** R.layout.fwk_component__fixedlist__master */
	fwk_component__fixedlist__master(ApplicationRGroup.LAYOUT, "fwk_component__fixedlist__master"),
	/** R.id.component__fixedlist_addButton__button */
	component__fixedlist_addButton__button(ApplicationRGroup.ID, "component__fixedlist_addButton__button"),
	/** R.id.component__fixedlist_title__value */
	component__fixedlist_title__value(ApplicationRGroup.ID, "component__fixedlist_title__value"),
	/** R.id.component__fixedlist_title__group**/
	component__fixedlist_title__group(ApplicationRGroup.ID, "component__fixedlist_title__group"),

	/** R.layout.fwk_component__fixedlist_item__master */
	fwk_component__fixedlist_item__master(ApplicationRGroup.LAYOUT, "fwk_component__fixedlist_item__master"),
	/** R.id.component__fixedlist_item_deleteButton__button */
	component__fixedlist_item_deleteButton__button(ApplicationRGroup.ID, "component__fixedlist_item_deleteButton__button"),
	
	/** R.layout.fwk_component__fixedlist_detail__master */
	fwk_component__fixedlist_detail__master(ApplicationRGroup.LAYOUT, "fwk_component__fixedlist_detail__master"),
	/** R.id.component__fixedlist_detail__master_id */
	component__fixedlist_detail__master_id(ApplicationRGroup.ID, "component__fixedlist_detail__master"),
	/** R.id.component__fixedlist_detail_ok__button */
	component__fixedlist_detail_ok__button(ApplicationRGroup.ID, "component__fixedlist_detail_ok__button"),
	/** R.id.component__fixedlist_detail_ok__button */
	component__fixedlist_detail_okandnext__button(ApplicationRGroup.ID, "component__fixedlist_detail_okandnext__button"),	
	/** R.id.component__fixedlist_detail_cancel__button */
	component__fixedlist_detail_cancel__button(ApplicationRGroup.ID, "component__fixedlist_detail_cancel__button"),

	/** R.layout.fwk_component__richfixedlist_detail__master */
	fwk_component__richfixedlist_detail__master(ApplicationRGroup.LAYOUT, "fwk_component__richfixedlist_detail__master"),

	/** R.layout.fwk_component__flipper_expandable_list__panel */
	fwk_component__flipper_expandable_list__panel(ApplicationRGroup.LAYOUT,"fwk_component__flipper_expandable_list__panel"),
	/** R.id.component__flipper_expandable_list__panel_id */
	component__flipper_expandable_list__panel_id(ApplicationRGroup.ID,"component__flipper_expandable_list__panel"),
	/** R.fwk_component__flipper_expandable_list_item__master */
	fwk_component__flipper_expandable_list_item__master(ApplicationRGroup.LAYOUT,"fwk_component__flipper_expandable_list_item__master"),
	/** R.id.component__flipper_expandable_list_item__master_id */
	component__flipper_expandable_list_item__master_id(ApplicationRGroup.ID,"component__flipper_expandable_list_item__master"),
	/** R.id.flipper_expandable_list_item__list__master_id */
	flipper_expandable_list_item__list__master_id(ApplicationRGroup.ID,"flipper_expandable_list_item__list__master"),
	
	/** R.id.flipper_expandable_list__btn_next__button */
	flipper_expandable_list__btn_next__button(ApplicationRGroup.ID,"flipper_expandable_list__btn_next__button"),
	/** R.id.flipper_expandable_list__btn_previous__button */
	flipper_expandable_list__btn_previous__button(ApplicationRGroup.ID,"flipper_expandable_list__btn_previous__button"),

	/** R.id.flipper_expandable_list__title__layout */
	flipper_expandable_list__title__layout(ApplicationRGroup.ID,"flipper_expandable_list__title__layout"),
	
	/** R.layout.fwk_component_position_dialog */
	fwk_component_position_dialog(ApplicationRGroup.LAYOUT, "fwk_component_position_dialog"),
	/** R.string.component_position__positionDialog__title */
	component_position__positionDialog__title(ApplicationRGroup.STRING, "component_position__positionDialog__title"),
	/** R.id.component_position__positionDialog_latitude__value */
	component_position__positionDialog_latitude__value(ApplicationRGroup.ID, "component_position__positionDialog_latitude__value"),
	/** R.id.component_position__positionDialog_longitude__value */
	component_position__positionDialog_longitude__value(ApplicationRGroup.ID, "component_position__positionDialog_longitude__value"),
	/** R.id.component_position__positionDialog_accuracy__value */
	component_position__positionDialog_accuracy__value(ApplicationRGroup.ID, "component_position__positionDialog_accuracy__value"),
	/** R.id.component_position__positionDialog_ok__button */
	component_position__positionDialog_ok__button(ApplicationRGroup.ID, "component_position__positionDialog_ok__button"),
	/** R.id.component_position__positionDialog_cancel__button */
	component_position__positionDialog_cancel__button(ApplicationRGroup.ID, "component_position__positionDialog_cancel__button"),

	/** R.layout.component__multi_selected_expandable_list__panel - panel d'un composant <code>MMMultiSelectedExpandableListView</code> */
	fwk_component__multi_selected_expandable_list__panel(ApplicationRGroup.LAYOUT, "fwk_component__multi_selected_expandable_list__panel"),
	/** R.id.component__multi_selected_expandable_list_view__panel - le composant <code>MMExpandableListView</code> contenu dans un composant <code>MMMultiSelectedExpandableListView</code>*/
	component__multi_selected_expandable_list_view__panel(ApplicationRGroup.ID, "component__multi_selected_expandable_list_view__panel"),
	/** R.id.component__multi_selected_expandable_list_selected_item_view__panel - la fixedList des items sélectionnés dans un composant <code>MMMultiSelectedExpandableListView</code>*/
	component__multi_selected_expandable_list_selected_item_view__panel(ApplicationRGroup.ID, "component__multi_selected_expandable_list_selected_item_view__panel"),
	/** R.id.component__multi_selected_expandable_list_item__content - le contenu de l'item de dernier niveau de l'expendable liste du composant <code>MMMultiSelectedExpandableListView</code>*/
	component__multi_selected_expandable_list_item__content(ApplicationRGroup.ID, "component__multi_selected_expandable_list_item__content"),
	/** R.id.component__multi_selected_expandable_list_item__master - dernier item de l'expendableListView du composant <code>MMMultiSelectedExpandableListView</code> 
	 * permettant l'affichage d'une checkbox supplémentaire dans la vue. */
	component__multi_selected_expandable_list_item__master(ApplicationRGroup.ID, "component__multi_selected_expandable_list_item__master"),
	/** R.id.vmdocumentedredevablepanellistdocumentsimpl__select - checkbox de sélection d'un item */
	vmdocumentedredevablepanellistdocumentsimpl__select(ApplicationRGroup.ID, "vmdocumentedredevablepanellistdocumentsimpl__select"),
	

	
	/** R.string.component_position__mapDialog__message */
	component_position__mapDialog__message(ApplicationRGroup.STRING, "component_position__mapDialog__message"),
	/** R.string.component_position__mapDialog_Coordinates__label */
	component_position__mapDialog_Coordinates__label(ApplicationRGroup.STRING, "component_position__mapDialog_Coordinates__label"),
	/** R.string.component_position__mapDialog_Address__label */
	component_position__mapDialog_Address__label(ApplicationRGroup.STRING, "component_position__mapDialog_Address__label"),
	/** R.string.component_position__mapDialog__title */
	component_position__mapDialog__title(ApplicationRGroup.STRING, "component_position__mapDialog__title"),
	
	
	/** R.layout.fwk_component_btprint_dialog */
	fwk_component_btprint_dialog(ApplicationRGroup.LAYOUT, "fwk_component_btprint_dialog"),
	/** R.string.component_btprint__btprintDialog__title */
	component_btprint__btprintDialog__title(ApplicationRGroup.STRING, "component_btprint__btprintDialog__title"),
	/** R.id.component_btprint__btprintDialog_message__value */
	component_btprint__btprintDialog_message__value(ApplicationRGroup.ID, "component_btprint__btprintDialog_message__value"),
	/** R.id.component_btprint__btprintDialog_error__value */
	component_btprint__btprintDialog_error__value(ApplicationRGroup.ID, "component_btprint__btprintDialog_error__value"),
	/** R.id.component_btprint__btprintDialog_reference__value */
	component_btprint__btprintDialog_reference__value(ApplicationRGroup.ID, "component_btprint__btprintDialog_reference__value"),
	/** R.id.component_btprint__btprintDialog_reference__label */
	component_btprint__btprintDialog_reference__labelui(ApplicationRGroup.ID, "component_btprint__btprintDialog_reference__label"),
	/** R.string.component_btprint__btprintDialog_error__label */
	component_btprint__btprintDialog_error__label(ApplicationRGroup.STRING, "component_btprint__btprintDialog_error__label"),
	/** R.string.component_btprint__btprintDialog_reference__label */
	component_btprint__btprintDialog_reference__label(ApplicationRGroup.STRING, "component_btprint__btprintDialog_reference__label"),
	/** R.id.component_btprint__btprintDialog_close__button */
	component_btprint__btprintDialog_close__button(ApplicationRGroup.ID, "component_btprint__btprintDialog_close__button"),
	/** R.string.component_btprint__btprintDialog__label__error */
	component_btprint__btprintDialog_UNKNOWN__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_UNKNOWN__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_ERR_INIT__error */
	component_btprint__btprintDialog_PRINTER_ERR_INIT__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_ERR_INIT__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_ERR_OVERFLOW__error */
	component_btprint__btprintDialog_PRINTER_ERR_OVERFLOW__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_ERR_OVERFLOW__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_ERR_UNSUPORTED__error */
	component_btprint__btprintDialog_PRINTER_ERR_UNSUPORTED__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_ERR_UNSUPORTED__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_ERR_BUFFER_FULL__error */
	component_btprint__btprintDialog_PRINTER_ERR_BUFFER_FULL__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_ERR_BUFFER_FULL__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_ERR_OPEN_CONN__error */
	component_btprint__btprintDialog_PRINTER_ERR_OPEN_CONN__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_ERR_OPEN_CONN__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_ERR_READ_DATA__error */
	component_btprint__btprintDialog_PRINTER_ERR_READ_DATA__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_ERR_READ_DATA__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_STATUS_COVEROPEN__error */
	component_btprint__btprintDialog_PRINTER_STATUS_COVEROPEN__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_STATUS_COVEROPEN__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_STATUS_EMPTYPAPER__error */
	component_btprint__btprintDialog_PRINTER_STATUS_EMPTYPAPER__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_STATUS_EMPTYPAPER__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_STATUS_ERROR__error */
	component_btprint__btprintDialog_PRINTER_STATUS_ERROR__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_STATUS_ERROR__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_PWR_LOW__error */
	component_btprint__btprintDialog_PRINTER_PWR_LOW__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_PWR_LOW__error"),
	/** R.string.component_btprint__btprintDialog_PRINTER_PWR_SMALL__error */
	component_btprint__btprintDialog_PRINTER_PWR_SMALL__error(ApplicationRGroup.STRING, "component_btprint__btprintDialog_PRINTER_PWR_SMALL__error"),
	
	/** R.layout.fwk_component_double_edittext */
	fwk_component_double_edittext(ApplicationRGroup.LAYOUT, "fwk_component_double_edittext"),
	/** R.id.component_double__double__edit */
	component_double__double__edit(ApplicationRGroup.ID, "component_double__double__edit"),
	/** R.id.component_double__date__button */
	component_double__button(ApplicationRGroup.ID, "component_double__button"),
//	/** R.id.component_double__time__button**/
//	component_double_dialog__positive__button(ApplicationRGroup.ID, "component_double_dialog__positive__button"),
//	/** R.id.component_double_dialog_negative_button__label**/
//	component_double_dialog__negative__button(ApplicationRGroup.ID, "component_double_dialog__negative__button"),

	/** R.layout.fwk_component_double_dialog */
	fwk_component_double_dialog(ApplicationRGroup.LAYOUT, "fwk_component_double_dialog"),
	/** R.string.component_double__doubleDialog__title */
	component_double__doubleDialog__title(ApplicationRGroup.STRING, "component_double__doubleDialog__title"), 
	/** R.id.component_doublepicker__picker */
	component_doublepicker__picker(ApplicationRGroup.ID, "component_doublepicker__picker"), 
	
	/** R.layout.fwk_component_duration_edittext */
	fwk_component_duration_edittext(ApplicationRGroup.LAYOUT, "fwk_component_duration_edittext"),
	/** R.id.component_duration__duration__edit */
	component_duration__duration__edit(ApplicationRGroup.ID, "component_duration__duration__edit"),
	/** R.id.component_duration__date__button */
	component_duration__button(ApplicationRGroup.ID, "component_duration__button"),

	/** R.layout.fwk_component_duration_dialog */
	fwk_component_duration_dialog(ApplicationRGroup.LAYOUT, "fwk_component_duration_dialog"),
	/** R.string.component_duration__durationDialog__title */
	component_duration__durationDialog__title(ApplicationRGroup.STRING, "component_duration__durationDialog__title"), 
	/** R.id.component_durationpicker__picker */
	component_durationpicker__picker(ApplicationRGroup.ID, "component_durationpicker__picker"), 
	
	/** R.layout.fwk_component_datetime_edittext */
	fwk_component_datetime_edittext(ApplicationRGroup.LAYOUT, "fwk_component_datetime_edittext"),
	/** R.id.component_datetime__datetime__edit */
	component_datetime__datetime__edit(ApplicationRGroup.ID, "component_datetime__datetime__edit"),
	/** R.id.component_datetime__date__button */
	component_datetime__date__button(ApplicationRGroup.ID, "component_datetime__date__button"),
	/** R.id.component_datetime__time__button */
	component_datetime__time__button(ApplicationRGroup.ID, "component_datetime__time__button"),
	/** R.string.component_datetime__time__button**/
	component_datetime_dialog__positivebutton__label(ApplicationRGroup.STRING, "component_datetime_dialog__positivebutton__label"),
	/** R.string.component_datetime_dialog_negative_button__label**/
	component_datetime_dialog__negativebutton__label(ApplicationRGroup.STRING, "component_datetime_dialog__negativebutton__label"),
	/** R.string.error_component_datatime_picker_bad_date**/
	error_component_datatime_picker_bad_date(ApplicationRGroup.STRING, "error_component_datatime_picker_bad_date"),
	/** composant MMEmailTextViewGroup */
	/** R.layout.fwk_component_email_textview */
	fwk_component_email_textview(ApplicationRGroup.LAYOUT, "fwk_component_email_textview"),
	/** R.id.component_email__email__value */
	component_email__email__value(ApplicationRGroup.ID, "component_email__email__value"),
	/** R.id.component_email__email__button */
	component_email__email__button(ApplicationRGroup.ID, "component_email__email__button"),
	
	/** composant MMEmailSimpleEditTextGroup */
	/** R.layout.fwk_component_email_simpletextedit */
	fwk_component_email_simpletextedit(ApplicationRGroup.LAYOUT, "fwk_component_email_simpletextedit"),
	/** composant MMEmailSimpleViewTextGroup */
	/** R.layout.fwk_component_email_simpletextview */
	fwk_component_email_simpletextview(ApplicationRGroup.LAYOUT, "fwk_component_email_simpletextview"),
	
	/** R.layout.fwk_component_multiautocomplete_edittext */
	fwk_component_multiautocomplete_edittext(ApplicationRGroup.LAYOUT, "fwk_component_multiautocomplete_edittext"),
	/** R.id.component_multiautocomplete__editText__edit */
	component_multiautocomplete__editText__edit(ApplicationRGroup.ID, "component_multiautocomplete__editText__edit"),
	/** R.id.component_multiautocomplete__btnErase__button */
	component_multiautocomplete__btnErase__button(ApplicationRGroup.ID, "component_multiautocomplete__btnErase__button"),
	/** R.id.component_multiautocomplete__btnSpeackNow__button */
	component_multiautocomplete__btnSpeackNow__button(ApplicationRGroup.ID, "component_multiautocomplete__btnSpeackNow__button"),

	/** composant dialogue de modif de texte long*/
	/** R.layout.fwk_component_photo_photocommand_layout */
	fwk_component_edittextdialog_edittextcommand_layout(ApplicationRGroup.LAYOUT,"fwk_component_edittextdialog_edittextcommand_layout"),
	/** R.id.component_multiautocomplete__editText__edit */
	component_edittextdialog__textview__value(ApplicationRGroup.ID, "component_edittextdialog__textview__value"),
	/** R.id.component_multiautocomplete__btnErase__button */
	component_edittextdialog__erase__button(ApplicationRGroup.ID, "component_edittextdialog__erase__button"),
	/** R.id.component_multiautocomplete__btnSpeackNow__button */
	component_edittextdialog__speaknow__button(ApplicationRGroup.ID, "component_edittextdialog__speaknow__button"),
	/** R.id.component_multiautocomplete__btnEdit__button */
	component_edittextdialog__ok__button(ApplicationRGroup.ID, "component_edittextdialog__ok__button"),
	/** R.id.component_multiautocomplete__btnEdit__button */
	component_edittextdialog__cancel__button(ApplicationRGroup.ID, "component_edittextdialog__cancel__button"),
	/** R.drawable.component_multiautocomplete__background */
	component_multiautocomplete__background(ApplicationRGroup.DRAWABLE, "bg_edittext_normal"),
		
	/** R.layout.fwk_component_phone_textview */
	fwk_component_phone_textview(ApplicationRGroup.LAYOUT, "fwk_component_phone_textview"),
	/** R.layout.fwk_component_phone_editview */
	fwk_component_phone_editview(ApplicationRGroup.LAYOUT, "fwk_component_phone_editview"),
	/** R.id.component_phonenumber__phonenumber__value */
	component_phonenumber__phonenumber__value(ApplicationRGroup.ID, "component_phonenumber__phonenumber__value"),
	/** R.id.component_phonenumber__phonenumber__button */
	component_phonenumber__phonenumber__button(ApplicationRGroup.ID, "component_phonenumber__phonenumber__button"),

	/** Composant édition URL */
	fwk_component_url_editview(ApplicationRGroup.LAYOUT, "fwk_component_url_editview"),
	/** Composant visualisation URL */
	fwk_component_url_textview(ApplicationRGroup.LAYOUT, "fwk_component_url_textview"),
	/** R.id.component_phonenumber__phonenumber__value */
	component_url__url__value(ApplicationRGroup.ID, "component_url__url__value"),
	/** R.id.component_phonenumber__phonenumber__button */
	component_url__url__button(ApplicationRGroup.ID, "component_url__url__button"),

	/** composant photo*/
	/** R.layout.fwk_component_photo_photocommand_layout */
	fwk_component_photo_photocommand_layout(ApplicationRGroup.LAYOUT,"fwk_component_photo_photocommand_layout"),
	/** R.drawable.component__photoview__nophoto */
	component__photoview__nophoto(ApplicationRGroup.DRAWABLE,"component__photoview__nophoto"),
	/** R.drawable.component__photoview__unloadedphoto */
	component__photoview__unloadedphoto(ApplicationRGroup.DRAWABLE,"component__photoview__unloadedphoto"),
	/** R.id.component_photo__camera__button */
	component_photo__camera__button(ApplicationRGroup.ID,"component_photo__camera__button"),
	/** R.id.component_photo__attachment__button */
	component_photo__attachment__button(ApplicationRGroup.ID,"component_photo__attachment__button"),
	/** R.id.component_photo__erase__button */
	component_photo__erase__button(ApplicationRGroup.ID,"component_photo__erase__button"),
	/** R.id.component_photo__image_photo__value */
	component_photo__image_photo__value(ApplicationRGroup.ID, "component_photo__image_photo__value"),
	/**R.id.component_photo__SlidingDrawer__group*/
	component_photo__SlidingDrawer__group(ApplicationRGroup.ID, "component_photo__SlidingDrawer__group"),
	/** R.id.component_photo__name__edit */
	component_photo__name__edit(ApplicationRGroup.ID, "component_photo__name__edit"),
	/** R.id.component_photo__description__edit */
	component_photo__description__edit(ApplicationRGroup.ID, "component_photo__description__edit"),
	/** R.id.component_photo__position__value */
	component_photo__position__value(ApplicationRGroup.ID, "component_photo__position__value"),
	/** R.id.component_photo__date__value */
	component_photo__date__value(ApplicationRGroup.ID, "component_photo__date__value"),
	/** R.id.component_photo__button_cancel__button */
	component_photo__button_cancel__button (ApplicationRGroup.ID, "component_photo__button_cancel__button"), 
	/** R.id.component_photo__button_ok__button */
	component_photo__button_ok__button (ApplicationRGroup.ID, "component_photo__button_ok__button"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__choose_photo__title(ApplicationRGroup.STRING,"component_photo__choose_photo__title"),
	/**R.string.component_photo__description_default__label*/
	component_photo__default_description__value(ApplicationRGroup.STRING,"component_photo__default_description__value"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__load__notfound(ApplicationRGroup.STRING,"component_photo__load__notfound"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__load__downloadfailed(ApplicationRGroup.STRING,"component_photo__load__downloadfailed"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__external_shared__message(ApplicationRGroup.STRING,"component_photo__external_shared__message"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__external_notready__message(ApplicationRGroup.STRING,"component_photo__external_notready__message"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__external_readonly__message(ApplicationRGroup.STRING,"component_photo__external_readonly__message"),
	/** R.string.component_photo__choose_photo__label */
	component_photo__external_mounting__message(ApplicationRGroup.STRING,"component_photo__external_mounting__message"),
	
	
	/** composant photo thumbnail*/
	/** R.layout.fwk_component_photo_thumbnail_layout */
	fwk_component_photo_thumbnail_layout(ApplicationRGroup.LAYOUT,"fwk_component_photo_thumbnail_layout"),
	/** R.drawable.component_photo_thumbnail__nophoto */
	component_photo_thumbnail__nophoto(ApplicationRGroup.DRAWABLE,"component__photo_thumbnail__nophoto"),
	/** R.id.component_photo_thumbnail__image_photo__value */
	component_photo_thumbnail__image_photo__value(ApplicationRGroup.ID, "component_photo_thumbnail__image_photo__value"),
	/** R.id.component_photo_thumbnail__name__value */
	component_photo_thumbnail__name__value(ApplicationRGroup.ID, "component_photo_thumbnail__name__value"),
	/** R.id.component_photo_thumbnail__description__value */
	component_photo_thumbnail__description__value(ApplicationRGroup.ID, "component_photo_thumbnail__description__value"),
	/** R.id.component_photo_thumbnail__date__value */
	component_photo_thumbnail__date__value(ApplicationRGroup.ID, "component_photo_thumbnail__date__value"),
	
	/** R.layout.fwk_component_position_textview */
	fwk_component_position_textview(ApplicationRGroup.LAYOUT, "fwk_component_position_textview"),
	/** R.layout.fwk_component_position_edittext */
	fwk_component_position_edittext(ApplicationRGroup.LAYOUT, "fwk_component_position_edittext"),
	/** R.id.component_position__position_map__button */
	component_position__position_map__button(ApplicationRGroup.ID, "component_position__position_map__button"),
	/** R.id.component_position__position_gps__button */
	component_position__position_gps__button(ApplicationRGroup.ID, "component_position__position_gps__button"),
	
	/** R.id.component_position__position_latitude__label */
	component_position__position_latitude__label(ApplicationRGroup.ID, "component_position__position_latitude__label"),
	/** R.id.component_position__position_longitude__label */
	component_position__position_longitude__label(ApplicationRGroup.ID, "component_position__position_longitude__label"),
	/** R.id.component_position__position_latitude__value */
	component_position__position_latitude__value(ApplicationRGroup.ID, "component_position__position_latitude__value"),
	/** R.id.component_position__position_longitude__value */
	component_position__position_longitude__value(ApplicationRGroup.ID, "component_position__position_longitude__value"),

	
	/** R.layout.fwk_component_scanbarcode_edittext */
	fwk_component_scanbarcode_edittext(ApplicationRGroup.LAYOUT, "fwk_component_scanbarcode_edittext"),
	/** R.id.component_scanbarcode__scanbarcode__edit */
	component_scanbarcode__scanbarcode__edit(ApplicationRGroup.ID, "component_scanbarcode__scanbarcode__edit"),
	/** R.id.component_scanbarcode__scanbarcode__textformat */
	component_scanbarcode__scanbarcode__textformat(ApplicationRGroup.ID, "component_scanbarcode__scanbarcode__textformat"),
	/** R.id.component_scanbarcode__scanbarcode__button */
	component_scanbarcode__scanbarcode__button(ApplicationRGroup.ID, "component_scanbarcode__scanbarcode__button"),
	
	/** R.layout.fwk_component_signature_imageview */
	fwk_component_signature_imageview(ApplicationRGroup.LAYOUT, "fwk_component_signature_imageview"),
	/** R.id.component_signature__signature__image */
	component_signature__signature__image(ApplicationRGroup.ID, "component_signature__signature__image"),
	/** R.id.component_signature__error */
	component_signature__error(ApplicationRGroup.ID, "component_signature__error"),
	/** R.layout.fwk_component_signature_dialog */
	fwk_component_signature_dialog(ApplicationRGroup.LAYOUT, "fwk_component_signature_dialog"),
	/** R.string.component_signature__signatureDialog_title__label */
	component_signature__signatureDialog__title(ApplicationRGroup.STRING, "component_signature__signatureDialog__title"),
	/** R.id.component_signature__signatureDialog__gesture */
	component_signature__signatureDialog__gesture(ApplicationRGroup.ID, "component_signature__signatureDialog__gesture"),
	/** R.id.component_signature__signatureDialog__button_table_OK__button */
	component_signature__dialog__button_ok(ApplicationRGroup.ID, "component_signature__signatureDialog__button_table_OK__button"),
	/** R.id.component_signature__signatureDialog__button_table_KO__button */
	component_signature__dialog__button_ko(ApplicationRGroup.ID, "component_signature__signatureDialog__button_table_KO__button"),
	/** R.id.component_signature__signatureDialog_erase__button */
	component_signature__dialog__button_erase(ApplicationRGroup.ID, "component_signature__signatureDialog_erase__button"),

	/** R.string.import_database_popup_title */
	import_database_popup_title(ApplicationRGroup.STRING, "import_database_popup_title"),
	/** R.string.import_database_popup_OK_button */
	import_database_popup_OK_button(ApplicationRGroup.STRING, "import_database_popup_OK_button"),
	/** R.string.import_database_popup_KO_button */
	import_database_popup_KO_button(ApplicationRGroup.STRING, "import_database_popup_KO_button"),
	/** R.string.import_database_exit_application */
	import_database_exit_application(ApplicationRGroup.STRING, "import_database_exit_application"),
	/** R.string.import_database_confirm_import */
	import_database_confirm_import(ApplicationRGroup.STRING, "import_database_confirm_import"),
	/** R.string.import_database_filenotfound */
	import_database_filenotfound(ApplicationRGroup.STRING, "import_database_filenotfound"),
	/** R.string.import_database_nosdcard */
	import_database_nosdcard(ApplicationRGroup.STRING, "import_database_nosdcard"),
	
	/**R.id.component_sliding_drawer__sliding_drawer_up__button*/
	component_sliding_drawer__sliding_drawer_up__button(ApplicationRGroup.DRAWABLE, "sliding_drawer_up"),
	/**R.id.component_sliding_drawer__sliding_drawer_down__button*/
	component_sliding_drawer__sliding_drawer_down__button(ApplicationRGroup.DRAWABLE, "sliding_drawer_down"),
	
//	/** R.drawable.component_workspace_background */
//	component_workspace_background(ApplicationRGroup.DRAWABLE, "component_workspace_background"),
	
	
	/** R.string.alert_gps_disabled */
	alert_gps_disabled(ApplicationRGroup.STRING, "alert_gps_disabled"),
	/** R.string.alert_connection_disable */
	alert_connection_disable(ApplicationRGroup.STRING, "alert_connection_disable"),

	/** R.string.alert_scancodebar_install */
	alert_scancodebar_install(ApplicationRGroup.STRING, "alert_scancodebar_install"),
	/** R.string.alert_scancodebar_missing */
	alert_scancodebar_missing(ApplicationRGroup.STRING, "alert_scancodebar_missing"),
	/** R.string.generic_message_yes */
	generic_message_yes(ApplicationRGroup.STRING, "generic_message_yes"),
	/** R.string.generic_message_no */
	generic_message_no(ApplicationRGroup.STRING, "generic_message_no"),
	/** R.string.generic_message_close */
	generic_message_close(ApplicationRGroup.STRING, "generic_message_close"),
	/** R.string.generic_message_cancel */
	generic_message_cancel(ApplicationRGroup.STRING, "generic_message_cancel"),
	/** R.string.generic_message_cancel */
	generic_message_ok(ApplicationRGroup.STRING, "generic_message_ok"),
	/** R.style.detail_label */
	detail_label(ApplicationRGroup.STYLE, "detail_label"),
	/** R.style.detail_edit */
	detail_edit(ApplicationRGroup.STYLE, "detail_edit"),
	/** R.style.detail_value */
	detail_value(ApplicationRGroup.STYLE, "detail_value"),

//	/** R.Theme */
//	Theme(ApplicationRGroup.STYLE, "Theme"),
//	/** R.Theme_Dialog */
//	ThemeDialog(ApplicationRGroup.STYLE, "Theme_Dialog"),
	/** R.ThemeCustomDialog */
	ThemeCustomDialog(ApplicationRGroup.STYLE, "ThemeCustomDialog"),
//	/** R.ThemeCustomSearchSpinnerDialog */
//	ThemeCustomSearchSpinnerDialog(ApplicationRGroup.STYLE, "ThemeCustomSearchSpinnerDialog"),
	
	/** R.layout.fwk_custom_dialog */
	fwk_action_loader_dialog(ApplicationRGroup.LAYOUT, "fwk_action_loader_dialog"),
	
	/** R.layout.fwk_custom_dialog */
	fwk_custom_dialog(ApplicationRGroup.LAYOUT, "fwk_custom_dialog"),
//	/** R.id.custom_dialog_title_text */
//	custom_dialog_title_text(ApplicationRGroup.ID, "custom_dialog_title_text"),
//	/** R.id.custom_dialog_icon */
//	custom_dialog_icon(ApplicationRGroup.ID, "custom_dialog_icon"),
//	/** R.id.custom_dialog_positive_button */
//	custom_dialog_positive_button(ApplicationRGroup.ID, "custom_dialog_positive_button"),
//	/** R.id.custom_dialog_negative_button */
//	custom_dialog_negative_button(ApplicationRGroup.ID, "custom_dialog_negative_button"),
//	/** R.id.custom_dialog_cancel_button */
//	custom_dialog_cancel_button(ApplicationRGroup.ID, "custom_dialog_cancel_button"),
	/** R.id.custom_dialog_content_text */
	custom_dialog_content_text(ApplicationRGroup.ID, "custom_dialog_content_text"),
	/** R.id.custom_dialog_content_layout */
	custom_dialog_content_layout(ApplicationRGroup.ID, "custom_dialog_content_layout"),

	/** R.drawable.textfield_selector */
	textfield_selector(ApplicationRGroup.DRAWABLE, "textfield_selector"),
	/** R.detail_valuecolor */
	detail_valuecolor(ApplicationRGroup.COLOR, "detail_value"),

	/** R.menu_base */
	menu_base(ApplicationRGroup.MENU, "base"),
	/** R.menu_startup */
	menu_startup(ApplicationRGroup.MENU, "startup"),

	/** R.id.menu_base_doStopApplicationStartup */
	menu_base_doStopApplicationStartup(ApplicationRGroup.ID, "menu_base_doStopApplicationStartup"),
	/** R.id.menu_base_doRestartApplicationStartup */
	menu_base_doRestartApplicationStartup(ApplicationRGroup.ID, "menu_base_doRestartApplicationStartup"),
	
	/** R.id.menu_base_configuration_during_application_stop */
	menu_base_configuration_during_application_stop(ApplicationRGroup.ID, "menu_base_configuration_during_application_stop"),
	/** R.id.menu_base_configuration_during_application_stop_doDeleteSerializedConfiguration */
	menu_base_configuration_during_application_stop_doDeleteSerializedConfiguration(ApplicationRGroup.ID, "menu_base_configuration_during_application_stop_doDeleteSerializedConfiguration"),
	/** R.id.menu_base_configuration_during_application_stop_doResetSetting */
	menu_base_configuration_during_application_stop_doResetSetting(ApplicationRGroup.ID, "menu_base_configuration_during_application_stop_doResetSetting"),
	/** R.id.menu_base_configuration_during_application_stop_doResetDataBase */
	menu_base_configuration_during_application_stop_doResetDataBase(ApplicationRGroup.ID, "menu_base_configuration_during_application_stop_doResetDataBase"),
	
	/** R.menu_base_configuration */
	menu_base_configuration(ApplicationRGroup.ID, "menu_base_configuration"),
	/** R.id.menu_base_configuration_doDisplaySetting */
	menu_base_configuration_doDisplaySetting(ApplicationRGroup.ID, "menu_base_configuration_doDisplaySetting"),
	/** R.id.menu_base_configuration_doResetSetting */
	menu_base_configuration_doResetSetting(ApplicationRGroup.ID, "menu_base_configuration_doResetSetting"),
	/** R.id.menu_base_configuration_doResetDataBase */
	menu_base_configuration_doResetDataBase(ApplicationRGroup.ID, "menu_base_configuration_doResetDataBase"),
	/** R.id.menu_base_configuration_doSendReport */
	menu_base_configuration_doSendReport(ApplicationRGroup.ID, "menu_base_configuration_doSendReport"),
	/** R.id.menu_base_configuration_doSendReport */
	menu_base_configuration_doImportDatabase(ApplicationRGroup.ID, "menu_base_configuration_doImportDatabase"),
	/** R.id.menu_base_doDisplayExitApplicationDialog */
	menu_base_doDisplayExitApplicationDialog(ApplicationRGroup.ID, "menu_base_doDisplayExitApplicationDialog"),

	/** R.layout.fwk_number_picker */
	fwk_number_picker(ApplicationRGroup.LAYOUT, "fwk_number_picker"),
	/** R.layout.fwk_number_picker */
	fwk_numberhorizontal_picker(ApplicationRGroup.LAYOUT, "fwk_numberhorizontal_picker"),
	/** R.id.component_numberpicker__increment__button */
	component_numberpicker__increment__button(ApplicationRGroup.ID, "component_numberpicker__increment__button"),
	/** R.id.component_numberpicker__decrement__button */
	component_numberpicker__decrement__button(ApplicationRGroup.ID, "component_numberpicker__decrement__button"),
	/** R.id.component_numberpicker__number__edit */
	component_numberpicker__number__edit(ApplicationRGroup.ID, "component_numberpicker__number__edit"),
	
	/** R.layout.fwk_screen_splash */
	fwk_screen_splash(ApplicationRGroup.LAYOUT, "fwk_screen_splash"),
	/** R.id.screen_splash_label */
	screen_splash_label(ApplicationRGroup.ID, "splash_version"),
	/** R.screen_splash_version */
	screen_splash_version(ApplicationRGroup.STRING, "splash_version"),
	/** R.id.screen_splash_progress */
	screen_splash_progress(ApplicationRGroup.ID, "splash_progress"),
	/** R.id.screen_splash_logo */
	screen_splash_logo(ApplicationRGroup.ID, "splash_logo"),
	/** R.id.screen_splash_copyright */
	screen_splash_copyright(ApplicationRGroup.ID, "splash_copyright"),
	/** R.string.splashScreen_logoCopyright_hidden */
	splashScreen_logoCopyright_hidden(ApplicationRGroup.STRING, "splashScreen_logoCopyright_hidden"),	
	
	
	/** R.drawable.field_separation_default */
	field_separation_default(ApplicationRGroup.DRAWABLE, "field_separation_default"),
	/** R.drawable.section_title_default */
	section_title_default(ApplicationRGroup.DRAWABLE, "section_title_default"),
	
	/** R.drawable.synchronize_green_status */
	synchronize_green_status(ApplicationRGroup.DRAWABLE, "synchronize_green_status"),
	/** R.string.ongoing_synchro_notification_tricker_text */
	ongoing_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ongoing_synchro_notification_tricker_text"),
	/** R.string.ongoing_synchro_notification_content_text */
	ongoing_synchro_notification_content_text(ApplicationRGroup.STRING, "ongoing_synchro_notification_content_text"),

	/** R.string.ok_synchro_notification_tricker_text */
	ok_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ok_synchro_notification_tricker_text"),
	/** R.string.ok_synchro_notification_content_text */
	ok_synchro_notification_content_text(ApplicationRGroup.STRING, "ok_synchro_notification_content_text"),
	
	/** R.drawable.synchronize_black_status */
	synchronize_black_status(ApplicationRGroup.DRAWABLE, "synchronize_black_status"),
	/** R.string.crash_synchro_notification_tricker_text */
	crash_synchro_notification_tricker_text(ApplicationRGroup.STRING, "crash_synchro_notification_tricker_text"),
	/** R.string.crash_synchro_notification_content_text */
	crash_synchro_notification_content_text(ApplicationRGroup.STRING, "crash_synchro_notification_content_text"),
	
	/** R.drawable.synchronize_red_status */
	synchronize_red_status(ApplicationRGroup.DRAWABLE, "synchronize_red_status"),
	/** R.string.ko_authentification_synchro_notification_tricker_text */
	ko_authentification_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ko_authentification_synchro_notification_tricker_text"),
	/** R.string.ko_authentification_synchro_notification_content_text */
	ko_authentification_synchro_notification_content_text(ApplicationRGroup.STRING, "ko_authentification_synchro_notification_content_text"),
	
	/** R.string.ko_licence_synchro_notification_tricker_text */
	ko_licence_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ko_licence_synchro_notification_tricker_text"),
	/** R.string.ko_licence_synchro_notification_content_text */
	ko_licence_synchro_notification_content_text(ApplicationRGroup.STRING, "ko_licence_synchro_notification_content_text"),
	
	/** R.string.ko_connected_synchro_notification_tricker_text */
	ko_connected_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ko_connected_synchro_notification_tricker_text"),
	/** R.string.ko_connected_synchro_notification_content_text */
	ko_connected_synchro_notification_content_text(ApplicationRGroup.STRING, "ko_connected_synchro_notification_content_text"),
	
	/** R.string.ko_join_synchro_notification_tricker_text */
	ko_join_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ko_join_synchro_notification_tricker_text"),
	/** R.string.ko_join_synchro_notification_content_text */
	ko_join_synchro_notification_content_text(ApplicationRGroup.STRING, "ko_join_synchro_notification_content_text"),
	
	/** R.drawable.synchronize_blue_orange */
	synchronize_blue_orange(ApplicationRGroup.DRAWABLE, "synchronize_blue_orange"),
	/** R.string.launch_synchro_double_notification_tricker_text */
	launch_synchro_double_notification_tricker_text(ApplicationRGroup.STRING, "launch_synchro_double_notification_tricker_text"),
	/** R.string.launch_synchro_double_notification_content_text */
	launch_synchro_double_notification_content_text(ApplicationRGroup.STRING, "launch_synchro_double_notification_content_text"),
	
	/** R.drawable.synchronize_blue */
	synchronize_blue(ApplicationRGroup.DRAWABLE, "synchronize_blue"),
	/** R.string.launch_synchro_mm2bo_notification_tricker_text */
	launch_synchro_mm2bo_notification_tricker_text(ApplicationRGroup.STRING, "launch_synchro_mm2bo_notification_tricker_text"),
	/** R.string.launch_synchro_mm2bo_notification_content_text */
	launch_synchro_mm2bo_notification_content_text(ApplicationRGroup.STRING, "launch_synchro_mm2bo_notification_content_text"),
	
	/** R.drawable.synchronize_orange */
	synchronize_orange(ApplicationRGroup.DRAWABLE, "synchronize_orange"),
	/** R.string.launch_synchro_bo2mm_notification_tricker_text */
	launch_synchro_bo2mm_notification_tricker_text(ApplicationRGroup.STRING, "launch_synchro_bo2mm_notification_tricker_text"),
	/** R.string.launch_synchro_bo2mm_notification_content_text */
	launch_synchro_bo2mm_notification_content_text(ApplicationRGroup.STRING, "launch_synchro_bo2mm_notification_content_text"),
	
	/** R.layout.fwk_screen_synchronisation_state */
	fwk_screen_synchronisation_state(ApplicationRGroup.LAYOUT, "fwk_screen_synchronisation_state"),
	/** R.id.screen_synchro_global_progress */
	screen_synchro_global_progress(ApplicationRGroup.ID, "screen_synchro_global_progress"),
	/** R.id.screen_synchro_detail_progress */
	screen_synchro_detail_progress(ApplicationRGroup.ID, "screen_synchro_detail_progress"),
	/** R.id.screen_synchro_detail_progress_text1 */
	screen_synchro_detail_progress_text1(ApplicationRGroup.ID, "screen_synchro_progress_text"),
	/** R.id.screen_synchro_detail_progress_text2 */
	screen_synchro_detail_progress_text2(ApplicationRGroup.ID, "screen_synchro_progress_text2"),
	
	/** R.id.menu_base_doClassicSynchronisation */
	menu_base_doClassicSynchronisation(ApplicationRGroup.ID, "menu_base_doClassicSynchronisation"),
	
	/** R.string.lose_form_modifications_title */
	lose_form_modifications_title(ApplicationRGroup.STRING, "lose_form_modifications_title"),
	/** R.string.lose_form_modifications_text */
	lose_form_modifications_text(ApplicationRGroup.STRING, "lose_form_modifications_text"),
	/** R.string.lose_form_modifications_ok_button */
	lose_form_modifications_ok_button(ApplicationRGroup.STRING, "lose_form_modifications_ok_button"),
	/** R.string.lose_form_modifications_ko_button */ 
	lose_form_modifications_ko_button(ApplicationRGroup.STRING, "lose_form_modifications_ko_button"),
	/** R.string.lose_form_modifications_cancel_button */ 
	lose_form_modifications_cancel_button(ApplicationRGroup.STRING, "lose_form_modifications_cancel_button"),
	/** R.string.ws_entrypoint */
	ws_entrypoint(ApplicationRGroup.STRING, "ws_entrypoint"), 
	/** R.string.display_setting_window */
	display_setting_window(ApplicationRGroup.STRING, "display_setting_window"), 

	/** R.string.screen_synchro_activity_name */
	screen_synchro_activity_name(ApplicationRGroup.STRING, "screen_synchro_activity_name"),
	/** R.string.screen_synchronisation_request */
	screen_synchronisation_request(ApplicationRGroup.STRING, "screen_synchronisation_request"),
	
	/** R.string.screen_initFailed_noConnectionFailure */
	screen_initFailed_noConnectionFailure(ApplicationRGroup.STRING, "screen_initFailed_noConnectionFailure"),
	/** R.string.screen_initFailed_brokenSyncFailure */
	screen_initFailed_brokenSyncFailure(ApplicationRGroup.STRING, "screen_initFailed_brokenSyncFailure"),
	/** R.string.screen_initFailed_errorInSyncFailure */
	screen_initFailed_errorInSyncFailure(ApplicationRGroup.STRING, "screen_initFailed_errorInSyncFailure"),
	/** R.string.screen_initFailed_firstSyncIsRequired */
	screen_initFailed_firstSyncIsRequired(ApplicationRGroup.STRING, "screen_initFailed_firstSyncIsRequired"),
	
	/** R.string.screen_synchronisation_failure_button */
	screen_synchronisation_failure_button(ApplicationRGroup.STRING, "screen_synchronisation_failure_button"),
	/** R.string.screen_synchronisation_delayedsync_failure_title */
	screen_synchronisation_delayedsync_failure_title(ApplicationRGroup.STRING,"screen_synchronisation_delayedsync_failure_title"),
	/** R.string.screen_synchronisation_delayedsync_failure */
	screen_synchronisation_delayedsync_failure(ApplicationRGroup.STRING,"screen_synchronisation_delayedsync_failure"),
	
	/** R.string.screen_synchronisation_incompatibles_times_failure_title */
	screen_synchronisation_incompatibles_times_failure_title(ApplicationRGroup.STRING, "screen_synchronisation_incompatibles_times_failure_title"),
	/** R.string.screen_synchronisation_incompatibles_times_failure */
	screen_synchronisation_incompatibles_times_failure(ApplicationRGroup.STRING, "screen_synchronisation_incompatibles_times_failure"),
	/** R.string.screen_synchronisation_incompatibles_times_failure */
	screen_synchronisation_incompatibles_times_failure_force_close_application(ApplicationRGroup.STRING, "screen_synchronisation_incompatibles_times_failure_force_close_application"),
	
	/** R.string.screen_synchronisation_authentication_failure_title */
	screen_synchronisation_authentication_failure_title(ApplicationRGroup.STRING,"screen_synchronisation_authentication_failure_title"),
	/** R.string.screen_synchronisation_authentication_failure */
	screen_synchronisation_authentication_failure(ApplicationRGroup.STRING,"screen_synchronisation_authentication_failure"),
	
	
	/** Custom search spinner */

	/** R.layout.fwk_search_spinner_item__master*/
	fwk_search_spinner_item__master(ApplicationRGroup.LAYOUT,"fwk_search_spinner_item__master"),
	/** R.id.search_spinner_text_value */
	search_spinner_text_value(ApplicationRGroup.ID,"search_spinner_text_master"),
	/** R.layout.fwk_custom_popup */
	fwk_custom_popup(ApplicationRGroup.LAYOUT,"fwk_custom_popup"),
	/** R.id.custom_popup_list_items */
	custom_popup_list_items(ApplicationRGroup.ID,"custom_popup_list_items"),
	/** R.id.filtre_section */
	filtre_section(ApplicationRGroup.ID,"filtre_section"),
	/** R.id.custom_popup__SectionTitle */
	custom_popup__SectionTitle(ApplicationRGroup.ID,"custom_popup__SectionTitle"),
	/** R.drawable.btn_dropdown_normal - Rendu graphique du composant MMSpinner */
	btn_dropdown_normal(ApplicationRGroup.DRAWABLE,"btn_dropdown_normal"),
	/** R.string.searchSpinnerDialogTitle */
	searchSpinnerDialogTitle(ApplicationRGroup.STRING,"searchSpinnerDialogTitle"),
	
	fwk_notification_custom_layout(ApplicationRGroup.LAYOUT,"fwk_notification_custom_layout"),
	notification_image(ApplicationRGroup.ID,"notification_image"),
	notification_title(ApplicationRGroup.ID,"notification_title"),
	notification_text(ApplicationRGroup.ID,"notification_text"),
	
	application_name(ApplicationRGroup.STRING,"app_name"),

	fwk_password_dialog_to_import_database(ApplicationRGroup.LAYOUT,"fwk_password_dialog_to_import_database"),
	dialog_confirmPasswordForImportDatabase_content_edit(ApplicationRGroup.ID,"dialog_confirmPasswordForImportDatabase_content_edit"),
	dialog_confirmPasswordForImportDatabase_ok_button(ApplicationRGroup.ID,"dialog_confirmPasswordForImportDatabase_ok_button"),
	dialog_confirmPasswordForImportDatabase_ko_button(ApplicationRGroup.ID,"dialog_confirmPasswordForImportDatabase_ko_button"),
	dialog_confirmPasswordForImportDatabase_error_text(ApplicationRGroup.STRING,"dialog_confirmPasswordForImportDatabase_error_text"),
	
	progress_indeterminate_drawable(ApplicationRGroup.DRAWABLE,"progress_indeterminate"),

	//alert_error(ApplicationRGroup.DRAWABLE,"alert_error"),

	dialog_synchronization_title(ApplicationRGroup.STRING, "activityname_synchronizationState"),

	fwk_component_double_picker(ApplicationRGroup.LAYOUT, "fwk_component_double_picker"),
	component_doublepicker__int__picker(ApplicationRGroup.ID, "component_doublepicker__int__picker"),
	component_doublepicker__digit__picker(ApplicationRGroup.ID, "component_doublepicker__digit__picker"),
	
	fwk_component_duration_picker(ApplicationRGroup.LAYOUT, "fwk_component_duration_picker"),
	component_durationpicker__hours__picker(ApplicationRGroup.ID, "component_durationpicker__hours__picker"),
	component_durationpicker__minutes__picker(ApplicationRGroup.ID, "component_durationpicker__minutes__picker"),
	
	fwk_component_number_picker(ApplicationRGroup.LAYOUT, "fwk_component_number_picker"),	
	component_numberpicker__number__label(ApplicationRGroup.ID, "component_numberpicker__number__label"),
	component_numberpicker__int__picker(ApplicationRGroup.ID, "component_numberpicker__int__picker"),
	
	/** Libellé. */
	fwk_component__label(ApplicationRGroup.LAYOUT, "fwk_component__label"),
	/** Champ texte. */
	fwk_component__edittext(ApplicationRGroup.LAYOUT, "fwk_component__edittext"),
	/** Champ non modifiable. */
	fwk_component__valuetext(ApplicationRGroup.LAYOUT, "fwk_component__valuetext"),
	/** Champ texte multiline. */
	fwk_component__multilinetext(ApplicationRGroup.LAYOUT, "fwk_component__multilinetext"),

	/** Composant seek bar. */
	fwk_component_double_seekbar(ApplicationRGroup.LAYOUT, "fwk_component_double_seekbar"),	
	component_seekbar__double__value(ApplicationRGroup.ID, "component_seekbar__double__value"),
	component_seekbar__double__seekbar(ApplicationRGroup.ID, "component_seekbar__double__seekbar"),
	fwk_component_integer_seekbar(ApplicationRGroup.LAYOUT, "fwk_component_integer_seekbar"),	
	component_seekbar__integer__value(ApplicationRGroup.ID, "component_seekbar__integer__value"),
	component_seekbar__integer__seekbar(ApplicationRGroup.ID, "component_seekbar__integer__seekbar"),
	fwksearchactivity__screendetail__master(ApplicationRGroup.LAYOUT, "fwksearchactivity__screendetail__master"),
	fwk_searchactivity_resultpanel__master(ApplicationRGroup.ID, "fwk_searchactivity_resultpanel__master"),
	fwk_searchactivity_criteriapanel__master(ApplicationRGroup.ID, "fwk_searchactivity_criteriapanel__master"),

	/** Layout webbrowser. */
	fwk_component__webbrowser(ApplicationRGroup.LAYOUT, "fwk_component__webbrowser"),
	component__webbrowser_mainwebview(ApplicationRGroup.ID, "component__webbrowser_mainwebview"),
	component__webbrowser_progressbar(ApplicationRGroup.ID, "component__webbrowser_progressbar"),
	component__webbrowser_loading_layout(ApplicationRGroup.ID, "component__webbrowser_loading_layout"),
	component__webbrowser_loading_text(ApplicationRGroup.ID, "component__webbrowser_loading_text"),
	component__webbrowser_alert_message_title(ApplicationRGroup.STRING, "component__webbrowser_alert_message_title"),
	component__webbrowser_location_msg_ko(ApplicationRGroup.STRING, "component__webbrowser_location_msg_ko"),
	component__webbrowser_location_msg_one(ApplicationRGroup.STRING, "component__webbrowser_location_msg_one"),
	component__webbrowser_location_msg_ok(ApplicationRGroup.STRING, "component__webbrowser_location_msg_ok"),
	component__webbrowser_location_msg_message(ApplicationRGroup.STRING, "component__webbrowser_location_msg_message"),
	component__webbrowser_location_msg_sensor(ApplicationRGroup.STRING, "component__webbrowser_location_msg_sensor"),
	component__webbrowser_location_msg_title(ApplicationRGroup.STRING, "component__webbrowser_location_msg_title"),
	component__webbrowser_loading_msg(ApplicationRGroup.STRING, "component__webbrowser_loading_msg"),

	
	/** DatePicker & TimePicker Dialog */
	fwk_dialog_datepicker(ApplicationRGroup.LAYOUT, "fwk_dialog_datepicker"),
	dialog__datepicker__picker(ApplicationRGroup.ID, "dialog__datepicker__picker"),
	fwk_dialog_timepicker(ApplicationRGroup.LAYOUT, "fwk_dialog_timepicker"),
	dialog__timepicker__picker(ApplicationRGroup.ID, "dialog__timepicker__picker"),
	
	
	/** Commons Dialogs button titles */
	/** R.string.component_dialog_fragment__positivebutton__label**/
	component_dialog_fragment__positivebutton__label(ApplicationRGroup.STRING, "component_dialog_fragment__positivebutton__label"),
	/** R.string.component_dialog_fragment__negativebutton__label**/
	component_dialog_fragment__negativebutton__label(ApplicationRGroup.STRING, "component_dialog_fragment__negativebutton__label"),
	
	/** ContentDescription */
	content_description_ok(ApplicationRGroup.STRING, "content_description_ok"),
	content_description_cancel(ApplicationRGroup.STRING, "content_description_cancel"),
	content_description_valid(ApplicationRGroup.STRING, "content_description_valid"),
	content_description_erase(ApplicationRGroup.STRING, "content_description_erase"),
	content_description_speaknow(ApplicationRGroup.STRING, "content_description_speaknow"),
	content_description_map(ApplicationRGroup.STRING, "content_description_map"),
	content_description_gps(ApplicationRGroup.STRING, "content_description_gps"),
	content_description_scanbarcode(ApplicationRGroup.STRING, "content_description_scanbarcode"),
	
	/** Specific Dialogs button titles */
	/** R.string.component_dialog_fragment_signature__erasebutton__label**/
	component_dialog_fragment_signature__erasebutton__label(ApplicationRGroup.STRING, "component_dialog_fragment_signature__erasebutton__label"),

	
	/** MMBarcodeScanner */
	component_barcodescanner_layout(ApplicationRGroup.LAYOUT, "fwk_component__barcodescanner"),
	component_barcodescanner_preferences(ApplicationRGroup.XML, "fwk_component_barcodescanner_preferences"),
	component_barcodescanner_color_viewfinder_mask(ApplicationRGroup.COLOR, "viewfinder_mask"),
	component_barcodescanner_color_result_view(ApplicationRGroup.COLOR, "result_view"),
	component_barcodescanner_color_result_points(ApplicationRGroup.COLOR, "result_points"),
	component_barcodescanner_color_viewfinder_laser(ApplicationRGroup.COLOR, "viewfinder_laser"),
	component_barcodescanner_color_possible_result_points(ApplicationRGroup.COLOR, "possible_result_points"),
	component_barcodescanner_id_preview_view(ApplicationRGroup.ID, "preview_view"),
	component_barcodescanner_id_viewfinder_view(ApplicationRGroup.ID, "viewfinder_view"),
	component_barcodescanner_message_decode_succeeded(ApplicationRGroup.ID, "decode_succeeded"),
	component_barcodescanner_message_decode_failed(ApplicationRGroup.ID, "decode_failed"),
	component_barcodescanner_message_decode(ApplicationRGroup.ID, "decode"),
	component_barcodescanner_message_launch_product_query(ApplicationRGroup.ID, "launch_product_query"),
	component_barcodescanner_message_restart_preview(ApplicationRGroup.ID, "restart_preview"),
	component_barcodescanner_message_return_scan_result(ApplicationRGroup.ID, "return_scan_result"),
	component_barcodescanner_message_quit(ApplicationRGroup.ID, "quit"),
	
	/** Dialog search title*/
	component_search_dialog_title(ApplicationRGroup.STRING, "component_search_dialog_title"), 
	
	fwk_mandatory_string(ApplicationRGroup.STRING, "fwk_mandatory_string"),
	;
	
	/** 
	 * Le groupe auquel appartient la clé.
	 * STRING, ID, DRAWABLE, etc. 
	 */
	private ApplicationRGroup group = null;
	
	/** la clé pour faire le lien avec le xml et le R */
	private String key = null;
	
	/**
	 * Création d'un nouveau lien Android/Framework pour les Resources du R
	 * @param p_oGroup le groupe
	 * @param p_sKey la clé
	 */
	private AndroidApplicationR(ApplicationRGroup p_oGroup, String p_sKey) {
		this.group = p_oGroup;
		this.key = p_sKey;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRGroup getGroup() {
		return this.group;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return this.key;
	}
}
