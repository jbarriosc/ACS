/*
 * ALMA - Atacama Large Millimiter Array (c) European Southern Observatory, 2007
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package alma.acsplugins.alarmsystem.gui.statusline;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import alma.acsplugins.alarmsystem.gui.ConnectionListener;
import alma.acsplugins.alarmsystem.gui.table.AlarmCounter;
import alma.acsplugins.alarmsystem.gui.table.AlarmGUIType;
import alma.acsplugins.alarmsystem.gui.table.AlarmTableModel;

/**
 * The status line showing info to the user
 * 
 * @author acaproni
 *
 */
public class StatusLine extends JPanel implements ActionListener, ConnectionListener {
	
	/**
	 *  The possible states of the connection of the category client
	 *  
	 * @author acaproni
	 *
	 */
	private enum ConnectionStatus {
		CONNECTED("Connected","/console-connected.png"),
		CONNECTING("Connecting","/console-connecting.png"),
		DISCONNECTED("Disconnected","/console-disconnected.png");
		
		// The icon for each connection state
		public final ImageIcon icon;
		
		// The tooltip for each connection state
		public String tooltip;
		
		/**
		 * The constructor that load the icon.
		 * 
		 * @param iconName The file containing the connection icon
		 */
		private ConnectionStatus(String tooltip, String iconName) {
			icon=new ImageIcon(this.getClass().getResource(iconName));
			this.tooltip=tooltip;
		}
		
	};
	
	// The counters showing the number of alarms
	private CounterWidget[] counters = new CounterWidget[AlarmGUIType.values().length];
	
	// The table model
	private final AlarmTableModel tableModel;
	
	// The time to refresh the values shown by the StatusLine
	private Timer timer=null;
	private static final int TIMER_INTERVAL=2000;
	
	// The label showing the icon and the tooltip for the status of the connection
	private JLabel connectionLbl = new JLabel();

	/**
	 * Constructor
	 */
	public StatusLine(AlarmTableModel model) {
		if (model==null) {
			throw new IllegalArgumentException("The AlarmTableModel can't be null");
		}
		tableModel=model;
		initialize();
		
		setConnectionState(ConnectionStatus.DISCONNECTED);
	}
	
	/**
	 * Init the status line
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		
		// Add the panel with the widgets at the left side
		JPanel widgetsPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		widgetsPnl.setBorder(BorderFactory.createLoweredBevelBorder());
		
		// Build the text fields	
		for (int t=0; t<counters.length; t++) {
			counters[t]=new CounterWidget(
					AlarmGUIType.values()[t],
					tableModel.getAlarmCounter(AlarmGUIType.values()[t]),
					tableModel);
			
			// Add the widget
			widgetsPnl.add(counters[t].getComponent());
		}
		add(widgetsPnl,BorderLayout.WEST);
		
		// Add the label with the connection status to the right
		JPanel connectionPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		connectionPnl.setBorder(BorderFactory.createLoweredBevelBorder());
		connectionPnl.add(connectionLbl);
		add(connectionPnl,BorderLayout.EAST);
	}
	
	/** 
	 * Start the thread to update values
	 */
	public void start() {
		if (timer!=null) {
			throw new IllegalStateException("Already started");
		}
		timer = new Timer(TIMER_INTERVAL, this);
		timer.setRepeats(true);
		timer.addActionListener(this);
		timer.start();
	}
	
	/** 
	 * Start the thread to update values
	 */
	public void stop() {
		timer.stop();
		timer.removeActionListener(this);
		timer=null;
	}
	
	/** 
	 * Pause the thread to update values
	 */
	public void pause() {
		timer.stop();
	}
	
	/** 
	 * Resume the thread to update values
	 */
	public void resume() {
		timer.start();
	}

	/**
	 * @see ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==timer) {
			for (CounterWidget cnt: counters) {
				cnt.update();
			}
		}
	}

	/**
	 * Set the icon and tooltip for the connected state
	 * 
	 * @see alma.acsplugins.alarmsystem.gui.ConnectionListener#connected()
	 */
	@Override
	public void connected() {
		setConnectionState(ConnectionStatus.CONNECTED);
	}

	/**
	 * Set the icon and tooltip for the connecting state
	 * 
	 * @see alma.acsplugins.alarmsystem.gui.ConnectionListener#connecting()
	 */
	@Override
	public void connecting() {
		setConnectionState(ConnectionStatus.CONNECTING);
	}

	/**
	 * Set the icon and tooltip for the disconnected state
	 * 
	 * @see alma.acsplugins.alarmsystem.gui.ConnectionListener#disconnected()
	 */
	@Override
	public void disconnected() {
		setConnectionState(ConnectionStatus.DISCONNECTED);
	}
	
	/**
	 * Set the icon and the tooltip of the connection label.
	 * 
	 * @param state The state of the connection
	 */
	private void setConnectionState(ConnectionStatus state) {
		if (state==null) {
			throw new IllegalArgumentException("The state can't be null");
		}
		class SetConnState extends Thread {
			public ConnectionStatus status;
			public void run() {
				connectionLbl.setIcon(status.icon);
				connectionLbl.setToolTipText(status.tooltip);
			}
		}
		SetConnState thread = new SetConnState();
		thread.status=state;
		SwingUtilities.invokeLater(thread);
	}
}
