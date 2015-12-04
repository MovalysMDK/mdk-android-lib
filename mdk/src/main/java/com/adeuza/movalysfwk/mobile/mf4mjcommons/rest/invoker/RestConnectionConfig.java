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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.log.Logger;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;

/**
 * RestConnectionConfig : configuration to connect a rest webservice
 *
 */
public class RestConnectionConfig {

	/** default mock status code */
	private static final int DEFAULT_MOCK_STATUS_CODE = 200;

	/**
	 * Connection timeout
	 */
	public static final int TIME_OUT = 3000;
	
	/**
	 * Timeout for receiving data
	 */
	public static final int SO_TIME_OUT = 180000;
	
	/**
	 * Timeout for writing data
	 */
	public static final int WRITE_TIME_OUT = 180000;
	
	/**
	 * default web port
	 */
	public static final int DEFAULT_PORT = 80;

	/**
	 * default SSL port
	 */
	public static final int DEFAULT_SSL_PORT = 443;
	
    /**
	 * Constant for slash
	 */
	private static final String SLASH = "/";
	
    /**
     * Protocol
     *
     */
    public enum Protocol {
    	/** http */
        HTTP,
        /** https */
        HTTPS
    }
    
    /**
     * Protocol
     */
    private Protocol protocol ;
	
	/**
	 * Host
	 */
	private String host ;

	/**
	 * Port
	 */
	private int port = DEFAULT_PORT;

	/**
	 * SSL Port
	 */
	private int sslPort = DEFAULT_SSL_PORT;
	
	/**
	 * Path 
	 */
	private String path = null;

	/**
	 * Entry point for web service
	 */
	private String wsEntryPoint = null ;

	/**
	 * command
	 */
	private String command = null;

	/**
	 * the user to use for synchronize
	 */
	private String user = null;
	
	/**
	 * the password to use for synchronize
	 */
	private String password = null;

	/**
	 * proxy url
	 */
	private String proxyHost = null;

	/**
	 * proxy port
	 */
	private int proxyPort = DEFAULT_PORT;

	/**
	 * the user to use for proxy
	 */
	private String proxyUser = null;

	/**
	 * the password to use for proxy
	 */
	private String proxyPassword = null;

	/**
	 * Timeout
	 */
	private int timeout = TIME_OUT ;
	
	/**
	 * Socket timeout
	 */
	private int soTimeout = SO_TIME_OUT ;
	
	/**
	 * Write timeout
	 */
	private int writeTimeout = WRITE_TIME_OUT;
	
	/**
	 * Mock mode
	 */
	private boolean mockMode = false ;
	
	/**
	 * Reason status
	 */
	private int mockStatusCode = DEFAULT_MOCK_STATUS_CODE;
	
	/**
	 * Enable AllowAllHostnameVerifier
	 */
	private boolean allowAllHostnameVerifier = false;
	
    /**
     * Queries parameters
     */
    private List<String> queryParameters = new ArrayList<>();
	
    /**
     * Disable rest auth service
     */
    private boolean restAuthServiceEnabled = true;
    
    /**
     * Header parameters
     */
    private Map<String,String> headers = new HashMap<>();
    
	/**
	 * Constructor
	 *
	 * @param p_sHost hostname
	 * @param p_iPort port
	 * @param p_sPath path
	 * @param p_sWsEntryPoint web service netry point
	 * @param p_sCommand command
	 * @param p_sUser user
	 * @param p_sPassword password
	 * @param p_sProxyHost proxy host
	 * @param p_iProxyPort proxy port
	 * @param p_sProxyUser proxy user
	 * @param p_sProxyPassword proxy password
	 */
	@Deprecated
	public RestConnectionConfig(String p_sHost, int p_iPort, String p_sPath, String p_sWsEntryPoint, String p_sCommand,
			String p_sUser, String p_sPassword, String p_sProxyHost, int p_iProxyPort, String p_sProxyUser,
			String p_sProxyPassword) {
		this(p_sHost, p_iPort, p_sPath, 0, 0, p_sWsEntryPoint, p_sCommand, p_sUser, p_sPassword, p_sProxyHost, p_iProxyPort, p_sProxyUser, p_sProxyPassword);
	}
	
	/**
	 * Constructor
	 *
	 * @param p_sHost hostname
	 * @param p_iPort port
	 * @param p_sPath path
	 * @param p_iTimeOut timeout
	 * @param p_iSoTimeOut secondary timeout
	 * @param p_sWsEntryPoint web service entry point
	 * @param p_sCommand command
	 * @param p_sUser user
	 * @param p_sPassword password
	 * @param p_sProxyHost proxy host
	 * @param p_iProxyPort proxy port
	 * @param p_sProxyUser proxy user
	 * @param p_sProxyPassword proxy password
	 */
	@Deprecated
	public RestConnectionConfig(String p_sHost, int p_iPort, String p_sPath, int p_iTimeOut, int p_iSoTimeOut, String p_sWsEntryPoint, String p_sCommand,
			String p_sUser, String p_sPassword, String p_sProxyHost, int p_iProxyPort, String p_sProxyUser,
			String p_sProxyPassword) {	
		this(p_sHost, p_iPort, DEFAULT_SSL_PORT, p_sPath, p_iTimeOut, p_iSoTimeOut, p_sWsEntryPoint, p_sCommand, 
				p_sUser, p_sPassword, p_sProxyHost, p_iProxyPort, p_sProxyUser, p_sProxyPassword);
	}
	
	/**
	 * Constructor
	 *
	 * @param p_sHost hostname
	 * @param p_iPort port
	 * @param p_iSslPort ssl port
	 * @param p_sPath path
	 * @param p_iTimeOut timeout
	 * @param p_iSoTimeOut secondary timeout
	 * @param p_sWsEntryPoint web service entry point
	 * @param p_sCommand command
	 * @param p_sUser user
	 * @param p_sPassword password
	 * @param p_sProxyHost proxy host
	 * @param p_iProxyPort proxy port
	 * @param p_sProxyUser proxy user
	 * @param p_sProxyPassword proxy password
	 */
	public RestConnectionConfig(String p_sHost, int p_iPort, int p_iSslPort, String p_sPath, int p_iTimeOut, int p_iSoTimeOut, String p_sWsEntryPoint, String p_sCommand,
			String p_sUser, String p_sPassword, String p_sProxyHost, int p_iProxyPort, String p_sProxyUser,
			String p_sProxyPassword) {
		super();
		this.host = p_sHost;
		this.port = p_iPort;
		this.sslPort = p_iSslPort;
		this.path = p_sPath;
		
		if (p_iTimeOut <= 0) {
			this.timeout = TIME_OUT;
		} else {
			this.timeout = p_iTimeOut;
		}
		
		if (p_iSoTimeOut <= 0) {
			this.soTimeout = SO_TIME_OUT;
		} else {
			this.soTimeout = p_iSoTimeOut;
		}
		
		this.wsEntryPoint = p_sWsEntryPoint;
		this.command = p_sCommand;
		this.user = p_sUser;
		this.password = p_sPassword;
		this.proxyHost = p_sProxyHost;
		this.proxyPort = p_iProxyPort;
		this.proxyUser = p_sProxyUser;
		this.proxyPassword = p_sProxyPassword;
	}

	/**
	 * Returns true if a proxy is set on connection
	 *
	 * @return true if a proxy is set on connection
	 */
	public boolean isProxy() {
		return this.proxyHost != null ;
	}
	
    /**
     * Returns the protocol
     *
     * @return the protocol
     */
    public Protocol getProtocol() {
        return protocol;
    }
	
	/**
	 * Returns true if a proxy password is set on connection
	 *
	 * @return true if a proxy password is set on connection
	 */
	public boolean isProxyAuth() {
		return this.proxyUser != null && this.proxyPassword != null ;
	}
	
	/**
	 * Logs the connection configuration
	 *
	 * @param p_oLogger the logger to use
	 */
	public void logConfig( Logger p_oLogger ) {
		if ( p_oLogger.isDebugLevel()) {
			p_oLogger.debug("synchro", "host: " + this.host);
			p_oLogger.debug("synchro", "port: " + this.port);
			p_oLogger.debug("synchro", "ssl port: " + this.sslPort);
			p_oLogger.debug("synchro", "path: " + this.path);
			p_oLogger.debug("synchro", "web service entry point parameter : " + this.wsEntryPoint);
			p_oLogger.debug("synchro", "command : " + this.command);
			p_oLogger.debug("synchro", "timeout : " + this.timeout);
			p_oLogger.debug("synchro", "soTimeout : " + this.soTimeout);
			p_oLogger.debug("synchro", "login: " + this.user);
			p_oLogger.debug("synchro", "password: " + this.password);
			p_oLogger.debug("synchro", "proxy host: " + this.host);
			p_oLogger.debug("synchro", "proxy port: " + this.proxyPort);
			p_oLogger.debug("synchro", "proxy login: " + this.proxyUser);
			p_oLogger.debug("synchro", "proxy password: " + this.proxyPassword);
			
		}
	}

	/**
	 * Returns the host
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * returns the connection port
	 *
	 * @return the connection port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * return the ssl port (if used)
	 *
	 * @return the ssl port
	 */
	public int getSslPort() {
		return sslPort;
	}
	
	/**
	 * returns the connection path
	 *
	 * @return the connection path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * returns the web service entry point
	 *
	 * @return the web service entry point
	 */
	public String getWsEntryPoint() {
		return wsEntryPoint;
	}

	/**
	 * returns the connection command
	 *
	 * @return the connection command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * returns the connection user
	 *
	 * @return the connection user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * returns the connection password
	 *
	 * @return the connection password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * returns the proxy host
	 *
	 * @return the proxy host
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * returns the proxy port
	 *
	 * @return the proxy port
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * returns the proxy user
	 *
	 * @return the proxy user
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * returns the proxy password
	 *
	 * @return the proxy password
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	/**
	 * return the timeout value
	 *
	 * @return the timeout value
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * return the secondary timeout value
	 *
	 * @return secondary timeout value
	 */
	public int getSoTimeout() {
		return soTimeout;
	}
	
	/**
	 * Return Write timeout
	 *
	 * @return write timeout
	 */
	public int getWriteTimeout() {
		return this.writeTimeout;
	}

	/**
	 * returns whether mock mode is set
	 *
	 * @return true if mock mode is set
	 */
	public boolean isMockMode() {
		return mockMode;
	}
	
	/**
	 * <p>Getter for the field <code>queryParameters</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getQueryParameters() {
       return this.queryParameters;
	}

	/**
	 * <p>allowAllHostnameVerifierEnabled.</p>
	 *
	 * @return a boolean.
	 */
	public boolean allowAllHostnameVerifierEnabled() {
		return allowAllHostnameVerifier;
	}

    /**
     * Is rest auth service enabled
     *
     * @return a boolean.
     */
    public boolean isRestAuthServiceEnabled() {
    	return this.restAuthServiceEnabled;
    }
    
    /**
     * Return headers
     *
     * @return headers
     */
    public Map<String, String> getHeaders() {
		return this.headers;
	}

	/**
	 * sets whether mock mode should be used
	 *
	 * @param p_bMockMode true to set mock mode
	 */
	public void setMockMode(boolean p_bMockMode) {
		this.mockMode = p_bMockMode;
	}

	/**
	 * return the mock mode status
	 *
	 * @return the mock mode status
	 */
	public int getMockStatusCode() {
		return mockStatusCode;
	}

	/**
	 * set the mock mode status
	 *
	 * @param p_iMockStatusCode mock mode status
	 */
	public void setMockStatusCode(int p_iMockStatusCode) {
		this.mockStatusCode = p_iMockStatusCode;
	}

	/**
	 * Set host
	 * @param p_sHost host
	 */
	private void setHost(String p_sHost) {
		this.host = p_sHost;
	}

	/**
	 * Set port
	 * @param p_iPort port
	 */
	private void setPort(int p_iPort) {
		this.port = p_iPort;
	}

	/**
	 * Set path (path to app in the url)
	 * @param p_sPath path
	 */
	private void setPath(String p_sPath) {
		this.path = p_sPath;
	}

	/**
	 * Set command
	 * @param p_sCommand command
	 */
	public void setCommand(String p_sCommand) {
		this.command = p_sCommand;
	}

	/**
	 * Set proxy password
	 * @param p_sPassword password
	 */
	private void setPassword(String p_sPassword) {
		this.password = p_sPassword;
	}

	/**
	 * Set proxy host
	 * @param p_sProxyHost proxy host 
	 */
	private void setProxyHost(String p_sProxyHost) {
		this.proxyHost = p_sProxyHost;
	}

	/**
	 * Set proxy port
	 * @param p_iProxyPort proxy port
	 */
	private void setProxyPort(int p_iProxyPort) {
		this.proxyPort = p_iProxyPort;
	}

	/**
	 * Set proxy user
	 * @param p_sProxyUser proxy user
	 */
	private void setProxyUser(String p_sProxyUser) {
		this.proxyUser = p_sProxyUser;
	}

	/**
	 * Set proxy password
	 * @param p_sProxyPassword password
	 */
	private void setProxyPassword(String p_sProxyPassword) {
		this.proxyPassword = p_sProxyPassword;
	}

	/**
	 * Set webservice entry point ( ex: /services)
	 * @param p_sWsEntryPoint entry point
	 */
	private void setWsEntryPoint(String p_sWsEntryPoint) {
		this.wsEntryPoint = p_sWsEntryPoint;
	}

	/**
	 * Set user login
	 * @param p_sUser user login
	 */
	private void setUser(String p_sUser) {
		this.user = p_sUser;
	}

	/**
	 * Set timeout
	 * @param p_iTimeout timeout
	 */
	private void setTimeout(int p_iTimeout) {
		this.timeout = p_iTimeout;
	}

	/**
	 * Set so timeout
	 * @param p_iSoTimeout so timeout
	 */
	private void setSoTimeout(int p_iSoTimeout) {
		this.soTimeout = p_iSoTimeout;
	}

	/**
	 * Define ssl port
	 * @param p_iSslPort ssl port
	 */
	private void setSslPort(int p_iSslPort) {
		this.sslPort = p_iSslPort;
	}

    /**
     * Set protocol
     * @param p_oProtocol host
     */
    private void setProtocol(Protocol p_oProtocol) {
        this.protocol = p_oProtocol;
    }
    
    /**
     * Enable AllowAllHostnameVerifier
     */
    private void enableAllowAllHostnameVerifier() {
    	this.allowAllHostnameVerifier = true;
    }
    
    /**
     * Disable rest auth service
     */
    private void disableRestAuthService() {
    	this.restAuthServiceEnabled = false;
    }
    
    /**
     * Add header
     * @param p_sKey
     * @param p_sValue
     */
    private void addHeader( String p_sKey, String p_sValue ) {
    	this.headers.put(p_sKey, p_sValue);
    }
	
	/**
	 * Build to create an instance of RestConnectionConfig
	 *
	 */
	public static class Builder {

		/**
		 * Rest connection config instance
		 */
		private RestConnectionConfig restConnectionConfig;
		
		/**
		 * Constructor
		 */
		public Builder() {
			this.restConnectionConfig = new RestConnectionConfig(
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_HOST), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_PORT), 
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_URLSERVER_PATH), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_TIMEOUT), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_URLSERVER_SOTIMEOUT), 
					Application.getInstance().getWsEntryPoint(), 
					StringUtils.EMPTY, 
					Application.getInstance().getStringSetting(Application.SETTING_LOGIN), 
					Application.getInstance().getStringSetting(Application.SETTING_PASSWORD), 
					Application.getInstance().getStringSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_HOST), 
					Application.getInstance().getIntSetting(Application.SETTING_COMPUTE_PROXYURLSERVER_PORT), 
					Application.getInstance().getStringSetting(Application.SETTING_PROXYLOGIN), 
					Application.getInstance().getStringSetting(Application.SETTING_PROXYPASSWORD));
		}
	
        /**
         * Define server host
         * @param p_sUrl host
         * @return builder instance
         * @throws RestException if any
         */
        public Builder appUrl( String p_sUrl ) throws RestException {
            URL oUrl = null;
			try {
				oUrl = new URL(p_sUrl);

	            if ( oUrl.getProtocol().equalsIgnoreCase("http")) {
	                this.restConnectionConfig.setProtocol(Protocol.HTTP);
	            } else  if ( oUrl.getProtocol().equalsIgnoreCase("https")) {
	                this.restConnectionConfig.setProtocol(Protocol.HTTPS);
	            } else {
	                throw new MalformedURLException("only http and https protocol are supported.");
	            }
	            this.restConnectionConfig.setHost(oUrl.getHost());
	
	            if ( oUrl.getProtocol().equalsIgnoreCase("http")) {
	                int iPort = oUrl.getPort();
	                if ( iPort == -1 ) {
	                    iPort = RestConnectionConfig.DEFAULT_PORT;
	                }
	                this.restConnectionConfig.setPort(iPort);
	            }
	            else if ( oUrl.getProtocol().equalsIgnoreCase("https")) {
	                int iPort = oUrl.getPort();
	                if ( iPort == -1 ) {
	                    iPort = RestConnectionConfig.DEFAULT_SSL_PORT;
	                }
	                this.restConnectionConfig.setSslPort(iPort);
	            }
	
	            String sPath = oUrl.getPath();
	            if (sPath == null || sPath.length() == 0) {
	                sPath = SLASH;
	            }
	            this.restConnectionConfig.setPath(sPath);
            
			} catch (MalformedURLException oMalformedURLException) {
				throw new RestException("Url is not valid", oMalformedURLException);
			}
            
            return this;
        }
		
        /**
         * Define server host
         * @param p_oProtocol host
         * @return builder instance
         */
        public Builder protocol( Protocol p_oProtocol ) {
            this.restConnectionConfig.setProtocol(p_oProtocol);
            return this;
        }
		
		/**
		 * Define server host
		 * @param p_sHost host
		 * @return builder instance
		 */
		public Builder host( String p_sHost ) {
			this.restConnectionConfig.setHost(p_sHost);
			return this;
		}
		
		/**
		 * Define server port
		 * @param p_iPort port
		 * @return builder
		 */
		public Builder port( int p_iPort ) {
			this.restConnectionConfig.setPort(p_iPort);
			return this;
		}
		
		/**
		 * Define ssl server port
		 * @param p_iPort port
		 * @return builder
		 */
		public Builder sslPort( int p_iPort ) {
			this.restConnectionConfig.setSslPort(p_iPort);
			return this;
		}
		
		/**
		 * Define path (ex: /services)
		 * @param p_sPath path
		 * @return builder
		 */
		public Builder path( String p_sPath ) {
			this.restConnectionConfig.setPath(p_sPath);
			return this;
		}
		
		/**
		 * Sets the entry point
		 * @param p_sEntryPoint the entry point
		 * @return builder
		 */
		public Builder entryPoint( String p_sEntryPoint ) {
			this.restConnectionConfig.setWsEntryPoint(p_sEntryPoint);
			return this;
		}
		
		/**
		 * Define command (the string added to path)
		 * @param p_sCommand command
		 * @return builder
		 */
		public Builder command( String p_sCommand ) {
			this.restConnectionConfig.setCommand(p_sCommand);
			return this;
		}
		
		/**
		 * Sets login and password
		 * @param p_sLogin login
		 * @param p_sPassword password
		 * @return builder
		 */
		public Builder auth( String p_sLogin, String p_sPassword ) {
			this.restConnectionConfig.setUser(p_sLogin);
			this.restConnectionConfig.setPassword(p_sPassword);
			return this;
		}
		
		/**
		 * Define timeout
		 * @param p_iTimeout timeout
		 * @return builder
		 */
		public Builder timeout( int p_iTimeout ) {
			this.restConnectionConfig.setTimeout(p_iTimeout);
			return this;
		}
		
		/**
		 * Define so timeout
		 * @param p_iSoTimeout so timeout
		 * @return builder
		 */
		public Builder soTimeout( int p_iSoTimeout ) {
			this.restConnectionConfig.setSoTimeout(p_iSoTimeout);
			return this;
		}
		
		/**
		 * Define write timeout
		 * @param p_iWriteTimeout so timeout
		 * @return builder
		 */
		public Builder writeTimeout( int p_iWriteTimeout ) {
			this.restConnectionConfig.setSoTimeout(p_iWriteTimeout);
			return this;
		}
		
		/**
		 * Define proxy
		 * @param p_sProxyHost proxy host
		 * @param p_iProxyPort proxy port
		 * @return builder
		 */
		public Builder proxy( String p_sProxyHost, int p_iProxyPort ) {
			this.restConnectionConfig.setProxyHost(p_sProxyHost);
			this.restConnectionConfig.setProxyPort(p_iProxyPort);
			return this;
		}
		
		/**
		 * Define proxy login and password
		 * @param p_sLogin login
		 * @param p_sPassword password
		 * @return builder
		 */
		public Builder proxyAuth( String p_sLogin, String p_sPassword ) {
			this.restConnectionConfig.setProxyUser(p_sLogin);
			this.restConnectionConfig.setProxyPassword(p_sPassword);
			return this;
		}
		
        /**
         * Define query parameters
         * @param p_sQueryParameter query parameter
         * @return builder
         */
        public Builder queryParameter( String p_sQueryParameter ) {
            this.restConnectionConfig.getQueryParameters().add(p_sQueryParameter);
            return this;
        }
		
        /**
         * Enable AllowAllHostnameVerifier
         * @return builder
         */
        public Builder allowAllHostnameVerifier() {
            this.restConnectionConfig.enableAllowAllHostnameVerifier();
            return this;
        }
        
        /**
         * Disable rest auth service
         * @return builder
         */
        public Builder disableRestAuthService() {
            this.restConnectionConfig.disableRestAuthService();
            return this;
        }
        
        /**
         * Adds a key / value pair on the header
         * @param p_sKey the key
         * @param p_sValue the value
         * @return builder
         */
        public Builder header( String p_sKey, String p_sValue ) {
            this.restConnectionConfig.addHeader(p_sKey, p_sValue);
            return this;
        }
        
		/**
		 * Build and return RestConnectionConfig
		 * @return RestConnectionConfig
		 */
		public RestConnectionConfig build() {
			return this.restConnectionConfig;
		}
	}
}
