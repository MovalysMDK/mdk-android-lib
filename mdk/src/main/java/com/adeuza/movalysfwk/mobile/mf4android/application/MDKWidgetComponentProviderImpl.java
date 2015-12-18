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

import android.content.Context;
import android.util.Log;

import com.soprasteria.movalysmdk.widget.core.command.WidgetCommand;
import com.soprasteria.movalysmdk.widget.core.exception.MDKWidgetException;
import com.soprasteria.movalysmdk.widget.core.message.MDKMessageFormat;
import com.soprasteria.movalysmdk.widget.core.message.MDKSimpleMessageFormat;
import com.soprasteria.movalysmdk.widget.core.provider.MDKWidgetComponentProvider;
import com.soprasteria.movalysmdk.widget.core.selector.RichSelector;
import com.soprasteria.movalysmdk.widget.core.validator.FormFieldValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Default implementation of the MDKWidgetComponentProvider.
 * <p>Uses the class package and name to create a singleton of the WidgetCommand/Validator
 * to be returned for the widget.</p>
 */
public class MDKWidgetComponentProviderImpl implements MDKWidgetComponentProvider {

    /**
     * Log tag.
     */
    private static final String LOG_TAG = "WidgetComponentProvider";

    /**
     * MDK_ERROR_MESSAGE_FORMAT_KEY.
     */
    private static final String MDK_ERROR_MESSAGE_FORMAT_KEY = "mdk_error_message_format";

    /**
     * MDK_ERROR_MESSAGE_NOT_INSTANCE.
     */
    private static final String MDK_ERROR_MESSAGE_NOT_INSTANCE = "could not instanciate class : \"";

    /**
     * Folder name for validator.
     */
    private static final String MDK_VALIDATORS_DECLARATION_FOLDER = "validators";

    /**
     * A Map of String key and FormFieldValidator instance.
     */
    private Map<String, FormFieldValidator> validatorMap;

    /**
     * A Map of Integer (R.attr.*) and List of FormFieldValidator instance.
     */
    private Map<Integer, List<FormFieldValidator>> validatorListMap;
    /**
     * A Map of String (key) and RichSelector.
     */
    private Map<String, RichSelector> richSelector;

    /**
     * Constructor of the MDKWidgetSimpleComponentProvider.
     */
    public MDKWidgetComponentProviderImpl() {
        this.validatorMap = new HashMap<>();
        this.validatorListMap = new HashMap<>();
        this.richSelector = new HashMap<>();
    }

    /**
     * Builds the default validator keys array <em>R.array.validatorKeys</em>.
     * @param context the Android context
     */
    public void initialize(Context context) {
        String[] validator = new String[] {};
        try {
            validator = getAssetsValidator(context);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error reading validators declaration : " + e.getMessage(), e);
        }

        createValidatorsFromKeys(context, validator);

    }

    /**
     * Read all entries form files in validators folder.
     * @param context the application context
     * @return an array of String containing validators key
     * @throws IOException throw IOException on file read exception
     */
    private String[] getAssetsValidator(Context context) throws IOException {
        String[] listValidatorFiles = context.getAssets().list(MDK_VALIDATORS_DECLARATION_FOLDER);
        Collection<String> keyList = new TreeSet<>();
        for (String file :
                listValidatorFiles) {
            InputStream is = context.getAssets().open(MDK_VALIDATORS_DECLARATION_FOLDER + File.separator + file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                keyList.add(line);
            }
            br.close();
        }
        String[] array = new String[]{};
        return keyList.toArray(array);
    }

    /**
     * Register the validators in the attributes Maps.
     * @param context the Android context
     * @param validatorsKeys a array of String representing the validators key to instanciate
     */
    private void createValidatorsFromKeys(Context context, String[] validatorsKeys) {
        for (String validatorKey : validatorsKeys) {
            FormFieldValidator tmp = this.createValidatorFromKey(context, validatorKey, null);
            if (tmp != null) {
                storeValidator(validatorKey, tmp);
            }
        }
    }

    /**
     * Store validator in maps.
     * @param validatorKey the validator key
     * @param validator the validator instance
     */
    private void storeValidator(String validatorKey, FormFieldValidator validator) {
        // in the key-instance map
        this.validatorMap.put(validatorKey, validator);
        for (int attr: validator.configuration()) {
            // in the attr-list<instance> map
            List<FormFieldValidator> validatorList = this.validatorListMap.get(attr);
            if (validatorList == null) {
                validatorList = new ArrayList<>();
                validatorListMap.put(attr, validatorList);
            }
            validatorList.add(validator);
        }
    }

    /**
     * Create a WidgetCommand instance from the specified key and attribute.
     * <p>Search for the concatenation of baseKey and qualifier in resources
     * and if not exist juste for the baseKey and instantiate the Class specified
     * by this resource.</p>
     *
     * @param context the Android context
     * @param baseKey the base key to find
     * @param qualifier the qualifier to append
     * @return a WidgetCommand instance corresponding to the passed key
     */
    private WidgetCommand createCommandFromKey(Context context, String baseKey, String qualifier) {

        String classPath = findClassPathFromResource(context, baseKey, qualifier);

        WidgetCommand widgetCommand ;
        try {
            widgetCommand = (WidgetCommand) Class.forName(classPath).newInstance();
        } catch ( Exception e) {
            throw new MDKWidgetException(MDK_ERROR_MESSAGE_NOT_INSTANCE + classPath + "\"", e);
        }

        return widgetCommand;
    }



    /**
     * Return a classPath corresponding to the concatenation of base key and qualifier.
     * <p>search for the concatenation of basekey and qualifier (if the qualifier is not null).
     * If the concatenation is not found or if the qualifier is null search for the baseKey
     * in string resources.</p>
     * @param context the Android context
     * @param baseKey the base key
     * @param qualifier the qualifier
     * @return a String containing the ClassPath of the given resources key
     */
    private String findClassPathFromResource(Context context, String baseKey, String qualifier) {
        String classPath = null;
        // case with qualifier
        if (qualifier != null) {
            classPath = findStringFromResourceName(context, baseKey + "_" + qualifier);
            if (classPath == null) {
                Log.d(LOG_TAG, "no string resource define for :" + baseKey + "_" + qualifier + " but qualifier was defined");
            }
        }
        // case without qualifier
        if (classPath == null) {
            classPath = findStringFromResourceName(context, baseKey);
        }
        // create instance
        if (classPath == null) {
            throw new MDKWidgetException("no string resource define for :"+baseKey);
        }

        return classPath;
    }

    /**
     * Return a String for the resource name.
     * @param context the Android context
     * @param resourceStringName the string name
     * @return a string matching the name in the Android resources
     */
    private String findStringFromResourceName(Context context, String resourceStringName) {
        int resourceId = context.getResources().getIdentifier(resourceStringName, "string", context.getPackageName());
        if (resourceId != 0) {
            return context.getString(resourceId);
        }
        return null;
    }

    @Override
    public WidgetCommand getCommand(String baseKey, String qualifier, Context context) {
        return createCommandFromKey(context, baseKey, qualifier);
    }

    @Override
    public FormFieldValidator getValidator(String baseKey, String qualifier, Context context) {
        return createValidatorFromKey(context, baseKey, qualifier);
    }

    @Override
    public MDKMessageFormat getErrorMessageFormat(Context context) {
        MDKMessageFormat errorMessageFormat;

        // Check the existence of a custom error message formatter resource
        String classPath = findStringFromResourceName(context, MDK_ERROR_MESSAGE_FORMAT_KEY);

        if (classPath != null) {
            try {
                // Try to instantiate the class found in android resource
                errorMessageFormat = (MDKMessageFormat) Class.forName(classPath).newInstance();
            } catch (Exception e) {
                throw new MDKWidgetException(MDK_ERROR_MESSAGE_NOT_INSTANCE + classPath + "\"", e);
            }
        } else {
            // Default error message formatter
            errorMessageFormat = new MDKSimpleMessageFormat();
        }

        return errorMessageFormat;

    }

    /**
     * Create a validator from the key.
     * @param context the context
     * @param baseKey the base key
     * @param qualifier the qualifier
     * @return validator the validator
     */
    private FormFieldValidator createValidatorFromKey(Context context, String baseKey, String qualifier) {

        FormFieldValidator<?> validator;

        String classPath = findClassPathFromResource(context, baseKey, qualifier);

        try {
            validator = (FormFieldValidator<?>) Class.forName(classPath).newInstance();
        } catch (Exception e) {
            throw new MDKWidgetException(MDK_ERROR_MESSAGE_NOT_INSTANCE + classPath + "\"", e);
        }

        return validator;
    }

    @Override
    public List<FormFieldValidator> getValidators(Set<Integer> attributes) {
        List<FormFieldValidator> rValidators = new ArrayList<>();

        for (Integer attr : attributes) {
            List<FormFieldValidator> list = this.validatorListMap.get(attr);
            if (list != null) {
                rValidators.addAll(list);
            }
        }

        return rValidators;
    }

    @Override
    public FormFieldValidator getValidator(String key) {
        return this.validatorMap.get(key);
    }

    @Override
    public void registerValidator(Context context, FormFieldValidator<?> validator) {
        if (validator != null
                && this.validatorMap.get(validator.getIdentifier(context)) == null ) {
            storeValidator(validator.getIdentifier(context), validator);
        } else {
            Log.w(LOG_TAG, "try to add a null validator or to replace a validator identifier");
        }
    }

    @Override
    public RichSelector getRichValidator(Context context, String key) {
        RichSelector selector = this.richSelector.get(key);
        if (selector == null) {
            String classPath = findClassPathFromResource(context, key, null);
            try {
                Class<RichSelector> selectorClass = (Class<RichSelector>) Class.forName(classPath);
                selector = selectorClass.newInstance();
                this.richSelector.put(key, selector);
            } catch (ClassNotFoundException e) {
                Log.e(LOG_TAG, "RichSelector not found : ", e);
            } catch (InstantiationException e) {
                Log.e(LOG_TAG, "RichSelector no empty constructor : ", e);
            } catch (IllegalAccessException e) {
                Log.e(LOG_TAG, "RichSelector protected or private constructor : ", e);
            }
        }
        return selector;
    }

    @Override
    public void registerRichSelector(String key, RichSelector selector) {
        if (selector != null
                && this.validatorMap.get(key) == null ) {
            this.richSelector.put(key, selector);
        } else {
            Log.w(LOG_TAG, "try to add a null richSelector or to replace a richSelector key");
        }
    }
}
