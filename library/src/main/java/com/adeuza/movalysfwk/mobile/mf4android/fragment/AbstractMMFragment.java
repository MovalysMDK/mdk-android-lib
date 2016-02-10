package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.Starter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ListenerIdentifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>The base fragment class of the Movalys Framework.</p>
 * <p>This class is used as a base to parse Framework listeners and action.</p>
 * @author abelliard
 *
 */
public class AbstractMMFragment extends Fragment implements ListenerIdentifier {
	
	/** application */
	protected AndroidApplication application;
	private boolean destroyed = false;
	
	public static final AtomicInteger oGeneratedUniqueTag = new AtomicInteger(1000);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAttach(Activity p_oActivity) {
		super.onAttach(p_oActivity);
		if (Application.getInstance() == null || ConfigurationsHandler.getInstance() == null || BeanLoader.getInstance() == null) {
			Log.d(Application.LOG_TAG,"[onAttach] Redémarrage de l'application !");
			Starter starter = new Starter(p_oActivity);
			starter.runStandalone();
		}
		
		this.application = (AndroidApplication) AndroidApplication.getInstance();
		if(this.application != null) {
			this.application.analyzeClassOf( this ) ;
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem p_oMenuItem) {
		boolean r_bHandled = true;
		try {
			ClassAnalyse oClassAnalyze = Application.getInstance().getClassAnalyser().getClassAnalyse(this);
			Method oListenerMethod = oClassAnalyze.getMethodOfMenuItemEventListener(p_oMenuItem.getItemId());
			if ( oListenerMethod != null ) {
				oListenerMethod.invoke(this) ;
			}
			else {
				// récupération de l'id du menu
				// le nom de la méthode à appeler est située après le dernier  "_"
				String sMenuId = ((AndroidApplication) Application.getInstance()).getAndroidIdStringByIntKey(p_oMenuItem.getItemId());
				// A2A_DEV mettre en cache ....
				
				String sMenu = sMenuId.substring(sMenuId.lastIndexOf('_') + 1);
				Method oMethod = Application.getInstance().getController().getClass().getMethod(sMenu, 
						new Class<?>[] { Screen.class} );
				oMethod.invoke(Application.getInstance().getController(), AbstractMMFragment.this);
				r_bHandled = true;
			}
		} catch (SecurityException e) {
			throw new MobileFwkException(e);
		} catch (NoSuchMethodException e) {
			// call super
			r_bHandled = super.onOptionsItemSelected(p_oMenuItem);
		} catch (IllegalArgumentException e) {
			throw new MobileFwkException(e);
		} catch (IllegalAccessException e) {
			throw new MobileFwkException(e);
		} catch (InvocationTargetException e) {
			throw new MobileFwkException(e);
		}
		return r_bHandled;
	}
	
	/**
	 * Lance une action depuis l'écran courant
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn les paramètres d'entrée de l'action
	 */
	public void launchAction(Class<? extends Action<?,?,?,?>> p_oActionClass, ActionParameter p_oParameterIn) {
		((AbstractMMActivity) this.getActivity()).launchAction(p_oActionClass, p_oParameterIn);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Application.getInstance().addActiveDisplayList(this);
	}
	
	@Override
	public void onStop() {
		Application.getInstance().removeActiveDisplayFromList(this);
		super.onStop();
	}
	
	@Override
	public void onDetach() {
		
		if (!this.destroyed) {
			localDestroy();
		}
		super.onDetach();
	}

	private void localDestroy() {

		Application.getInstance().removeListenerFromAction(this);
		Application.getInstance().removeListenerFromDataLoader(this);
		Application.getInstance().removeListenerFromEventManager(this);

		this.destroyed = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueId() {
		String rUniqueId = this.getClass().getSimpleName();
		
		if (this.getTag() != null) {
			rUniqueId.concat(this.getTag());
		}
		
		return rUniqueId;
	}

}
