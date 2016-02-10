package com.adeuza.movalysfwk.mobile.mf4android.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.adeuza.movalysfwk.mobile.mf4android.alarm.PushTask;
import com.adeuza.movalysfwk.mobile.mf4android.application.Starter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>The push simulation service for synchronizing Movalys Intervention data.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (2 déc. 2010)
 */
public class PushServiceImpl extends Service implements PushService {

	/**
	 * This is the object that receives interactions from clients.  
	 * See RemoteService for a more complete example.
	 */
    private final IBinder synchroNotifBinder = new PushBinder();

    /**
     * Pool used to perform push actions.
     */
    private ThreadPoolExecutor threadPool;
	
	 /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class PushBinder extends Binder {
    	/**
    	 * <p>
    	 * 	Return the push service.
    	 * </p>
    	 * @return instance of Push service.
    	 */
    	PushServiceImpl getService() {
            return PushServiceImpl.this;
        }
    }

    /** 
     * Construct an object <em>PushService</em>.
     */
    public PushServiceImpl() {
    	super();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
    }

    /** 
	 * {@inheritDoc}
	 */
	@Override
	public IBinder onBind(Intent p_oIntent) {
		return synchroNotifBinder;
	}
	
	private static final int THREAD_QUEUE_CAPACITY = 400;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(){
		super.onCreate();
		this.threadPool = new ThreadPoolExecutor(1, 1, NumericConstants.MILLISEC_2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(THREAD_QUEUE_CAPACITY));
	}

	//Tests de résolution du bug en cas de cloture du service par le système pour besoin de ressources
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// when we loose the Application Singleton : stop the task
		if ( 
			Application.getInstance() == null 
			|| Application.getInstance().getController() == null 
			|| ConfigurationsHandler.getInstance() == null 
			|| BeanLoader.getInstance() == null
		) {
			Starter starter = new Starter(this.getApplicationContext());
			starter.runStandalone();
		}
		this.threadPool.execute(new PushTask());

		return Service.START_NOT_STICKY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDestroy() {
		this.threadPool.shutdownNow();
		super.onDestroy();
	}
    
}
