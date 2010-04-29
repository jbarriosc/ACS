package com.cosylab.cdb.jdal.logging;

import java.util.logging.Logger;

import org.slf4j.impl.ACSLoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import alma.acs.logging.ClientLogManager;
import alma.acs.logging.config.LogConfig;
import alma.acs.logging.level.AcsLogLevelDefinition;

/**
 * Creates a shared logger called "CDB-RDB" to be used by all hibernate DAL code.
 * Its log level defaults to INFO for local and remote logging.
 * <p>
 * @TODO We should make the log levels configurable in the future.
 * <p>
 * Note 1: The hibernate framework will get its own ACS Loggers via a tapped slf4j framework. 
 * There are two loggers called "hibernate@CDB-RDB" and "hibernateSQL@CDB-RDB".
 * Their log levels are configured here, to DEBUG for SQL logs, and WARNING for other logs,
 * both for local and remote logs.
 * <p>
 * Note 2: Remote logging is disabled for the time being.
 *  
 * @author hsommer
 */
public class AcsLoggerHelper {

	/**
	 * Logger name used by default. Can be changed
	 */
	public static final String loggerNameDefault = "CDB-RDB";
	
	/**
	 * Logger name can be set in {@link #setLoggerName(String)} before calling {@link AcsLoggerHelper#getSharedLogger()}.
	 * Otherwise defaults to {@link #loggerNameDefault}.
	 */
	private static String loggerName = null;
	
	/**
	 * Singleton instance
	 */
	private static AcsLoggerHelper instance;
	
	/**
	 * The instance's logger, to be shared by the whole application using {@link #getSharedLogger()};
	 */
	private Logger sharedLogger;
	
	
	/**
	 * Singleton
	 */
	public static synchronized AcsLoggerHelper getInstance() {
		if (instance == null) {
			if (loggerName == null) {
				loggerName = loggerNameDefault;
			}

			instance = new AcsLoggerHelper(loggerName);
		}
		return instance;
	}
	
	private AcsLoggerHelper(String sharedLoggerName) {
		
		// @TODO instead of the following line, call ClientLogManager#initRemoteLogging(orb, manager, managerHandle, retry)
		// as soon as the service dependency issues are sorted out (same in the CDB and alarm service)
		ClientLogManager.getAcsLogManager().suppressRemoteLogging();
		
		// default config for hibernate loggers (which are created outside of this class)
		LogConfig logConfig = ClientLogManager.getAcsLogManager().getLogConfig();
		String hibernateDefaultLoggerName = ACSLoggerFactory.HIBERNATE_LOGGER_NAME_PREFIX + '@' + sharedLoggerName;
		if (!logConfig.hasCustomConfig(hibernateDefaultLoggerName)) {
			setHibernateLogLevels(AcsLogLevelDefinition.WARNING, AcsLogLevelDefinition.WARNING);
		}
		String hibernateSqlLoggerName = ACSLoggerFactory.HIBERNATE_SQL_LOGGER_NAME_PREFIX + '@' + sharedLoggerName;
		if (!logConfig.hasCustomConfig(hibernateSqlLoggerName)) {
			setHibernateSqlLogLevels(AcsLogLevelDefinition.DEBUG, AcsLogLevelDefinition.DEBUG);
		}
	}
	
	

	/**
	 * Returns a shared Logger, creating on demand.
	 * The logger name will be {@link #loggerNameDefault} or the name given in {@link #setLoggerName(String)} 
	 * prior to the first call to this method.
	 */
	public Logger getSharedLogger() {
		if (sharedLogger == null) {
			sharedLogger = ClientLogManager.getAcsLogManager().getLoggerForApplication(loggerName, true);
		}
		return sharedLogger;
	}

	/**
	 * Sets levels for normal loggers, which otherwise are configured by xsd defaults, $ACS_LOG_STDOUT etc,
	 * but not by this class.
	 */
	public void setDefaultLogLevels(AcsLogLevelDefinition localLevel, AcsLogLevelDefinition remoteLevel) {
		LogConfig logConfig = ClientLogManager.getAcsLogManager().getLogConfig();
		logConfig.setDefaultMinLogLevelLocal(localLevel);
		logConfig.setDefaultMinLogLevel(remoteLevel);
	}
	
	public void setHibernateLogLevels(AcsLogLevelDefinition localLevel, AcsLogLevelDefinition remoteLevel) {
		LogConfig logConfig = ClientLogManager.getAcsLogManager().getLogConfig();
		String name = ACSLoggerFactory.HIBERNATE_LOGGER_NAME_PREFIX + '@' + loggerName;
		logConfig.setMinLogLevelLocal(localLevel, name);
		logConfig.setMinLogLevel(remoteLevel, name);
	}
	
	public void setHibernateSqlLogLevels(AcsLogLevelDefinition localLevel, AcsLogLevelDefinition remoteLevel) {
		LogConfig logConfig = ClientLogManager.getAcsLogManager().getLogConfig();
		String name = ACSLoggerFactory.HIBERNATE_SQL_LOGGER_NAME_PREFIX + '@' + loggerName;
		logConfig.setMinLogLevelLocal(localLevel, name);
		logConfig.setMinLogLevel(remoteLevel, name);
	}
	
	/**
	 * Mainly thought for general hibernate layer test code,
	 * which does not like to use the application logger name "CDB-RDB". 
	 */
	synchronized public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	
	/**
	 * JUnit tests need to call this if they don't want the logger to be reused 
	 * between two tests. For some reason junit's classloader tricks fail here.
	 */
	public void shutdown() {
		try {
			((ACSLoggerFactory)StaticLoggerBinder.getSingleton().getLoggerFactory()).clearDelegateLoggers();
		}
		catch (ClassCastException ex) {
			this.sharedLogger.info("hibernate uses slf4j binding different than for ACS.");
		}
		instance = null;
		loggerName = null;
	}
}
