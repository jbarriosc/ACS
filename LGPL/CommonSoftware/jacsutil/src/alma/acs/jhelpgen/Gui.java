/*
 * Created on Oct 4, 2006 by mschilli
 */
package alma.acs.jhelpgen;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.help.DefaultHelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;




public class Gui {

	
	// ====================================================
	// Support for JavaHelp-enabled applications 
	
	protected URL helpsetURL;
	protected DefaultHelpBroker helpBroker;
	
	public Gui (URL helpsetURL) throws Exception {
		
		this.helpsetURL = helpsetURL;
		
		HelpSet helpSet = new HelpSet(null, helpsetURL);
		if (helpSet == null) {
			throw new HelpSetException("Online Help could not be loaded from "+helpsetURL);
		}
		
		helpBroker = (DefaultHelpBroker)helpSet.createHelpBroker();
	}
	
	public void showHelpBrowser (String topic) {
		helpBroker.setCurrentID(topic);
		showHelpBrowser();
	}
	
	public void showHelpBrowser () {
		helpBroker.setDisplayed(true);
	}

	
	// ====================================================
	// Support for commandline invokation 

	public static void main(String[] args) {
      try {
      	if (args.length < 1)
      		throw new IllegalArgumentException("too few arguments");
      	
      	String name = "/"+args[0] + (args[0].endsWith("/")? "" : "/") + Const.SET_FILENAME;
      	URL url = Gui.class.getResource(name);
      	if (url == null)
      		throw new Exception("helpset not found: "+name);
      	
         Gui inst = new Gui(url);
         inst.showHelpBrowser();
         
		} catch (IllegalArgumentException e) {
			System.err.println("Error: "+e.getMessage());
			System.err.println("Usage: (this) help-dir");
		} catch (Exception e) {
			System.err.println("Error: "+e);
		}
	}
	
	
	// =======================================================
	// Debugging Helpers for class Gen, not public
	
	private Gui() {
		
	}
	
	static void showTree(Gen.AnchorNode n) {
		try {
			Gui inst = new Gui();
			inst.show(n);
			synchronized (inst) {
				inst.wait();
			}
		} catch (Exception e) {}
	}
	
	
	void show (Gen.AnchorNode n) {
		
		DefaultMutableTreeNode r = new DefaultMutableTreeNode(n);
		toTreeNode(n, r);
		JTree t = new JTree(r);

		
		final JFrame f = new JFrame();
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent evt) {
				synchronized (Gui.this) {
					Gui.this.notify();
				}
				f.dispose();
			}
		});
		f.getContentPane().add(new JScrollPane(t));
		f.pack();
		f.setVisible(true);
	}
	
	
	private void toTreeNode (Gen.AnchorNode src, DefaultMutableTreeNode trg) {
		for (Gen.AnchorNode subSrc : src.children) {
			DefaultMutableTreeNode subTrg = new DefaultMutableTreeNode(subSrc);
			trg.add(subTrg);
			toTreeNode(subSrc, subTrg);
		}
	}
	

}


