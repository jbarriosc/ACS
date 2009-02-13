/**
 * 
 */
package cl.utfsm.samplingSystemUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import alma.ACSErrTypeCommon.CouldntAccessComponentEx;
import alma.ACSErrTypeCommon.TypeNotSupportedEx;
import alma.acs.util.IsoDateFormat;
import alma.acs.util.UTCUtility;

import cl.utfsm.samplingSystemUI.core.DataItem;

/**
 * Printer class that acts takes the data that comming from the sampling manager, and do something with it. <br />
 * In this case, the FilePrinter class dumps the data to a file.
 * @author Arturo Hoffstadt Urrutia <ahoffsta[AT]inf.utfsm.cl>
 *
 */
public class FilePrinter extends DataPrinter {
	
	private long samplesCounter;
	private String filename="";
	private FileWriter file;
	private BufferedWriter writer;
	private IsoDateFormat formater;
	private boolean stopped;

	/**
	 * Constructor, initialize the sampling counter and creates a BeanMemoryWidget for representation.
	 * @param ssg Sampling System Gui reference.
	 */
	public FilePrinter(SamplingSystemGUI ssg) {
		super(ssg);
		samplesCounter=0;
		widget=new BeanMemoryWidget();
		formater = new IsoDateFormat();
		stopped = true;
	}

	/**
	 * Since the samples are not stored in memory, nothing can be returned. 
	 */
	public ArrayList<DataItem> getSamples() {
		return null;
	}
	
	/**
	 * Return the name of the file where the data is being dumped to, or the last file used.
	 * @return
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Starts the sampling, connecting to ACS Manager and the Sampling Manager.
	 * @throws CouldntAccessComponentEx Component wasn't available at the time.
	 * @throws TypeNotSupportedEx Sampling Manager specific exception. Some types are currently not supported in acssamp.
	 */
	public void startSample() throws CouldntAccessComponentEx, TypeNotSupportedEx {
		super.startSample();
		stopped = false;		
	}

	/**
	 * This method should be called after stopping the sampling.
	 */
	public void postProcessing() {
		stopped = true;
		widget.resetSampleCount();
		samplesCounter = 0;
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void updateValue(DataItem item) {
		if(!stopped){
			samplesCounter++;
			if( samplesCounter == 1){
				openFile();
				String line = "\"Timestamp ISO Format\";" + component + "_" + property;
				try {
					writer.write(line + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
			widget.updateValues(item.getTime(), item.getValue(), 0);
			String line = "" + formater.format(new Date(UTCUtility.utcOmgToJava(item.getTime()))) + ";"+ item.getValue();
			try {
				writer.write(line + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void setComponent(String component) {
		super.setComponent(component);
		widget.setValues(component, property, 0);
	}

	public void setProperty(String property) {
		super.setProperty(property);
		widget.setValues(component, property, 0);
	}
	
	public void setComponentAvailable(boolean available,String reason) {
		super.setComponentAvailable(available,reason);
		widget.setComponentAvailable(available,reason, 0);
	}
	
	private boolean openFile(){
		IsoDateFormat fo = new IsoDateFormat();
		filename = component + "_" + property + "_" + getFrecuency() + "_" + fo.format(new Date()) +".csv";
		try{
			file = new FileWriter( filename );
		}catch( IOException e ){
			e.printStackTrace();
			return false;
		}
		writer = new BufferedWriter( file );
		return true;
	}

}
