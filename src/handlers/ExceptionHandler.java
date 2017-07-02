package handlers;

import java.net.UnknownHostException;

/**
 * A class for handling exceptions. It is here where we should decide if we
 * notify the administrator and what to say.
 * 
 * ~~~ We need a plan for if Raspberry pi will send the e-mails or server. ~~~~
 * 
 * @version 0.0.1
 * @author Thomas Rokicki
 *
 */
public class ExceptionHandler {

	private String output;
	private Exception theException;

	/**
	 * Handles our special exceptions with a string for the reader to
	 * understand.
	 * 
	 * @param e
	 *            the Exception
	 * @param output
	 */
	ExceptionHandler(Exception e, String output) {
		this.output = output;
		this.theException = e;
	}

	/**
	 * Handles UnknownHostException
	 * 
	 * @param e
	 */
	ExceptionHandler(UnknownHostException e, String output) {
		this.output = "";
		this.theException = e;
	}

	public void sendErrorNotification() {
		printErrorToConsole();
		notifyAdminByText();
		notifyAdminByEmail();
		notifySenderOfError();
	}
	/**
	 * Prints the error to console.
	 */
	public void printErrorToConsole() {
		System.out.println("******************************************");
		System.out.println("**************  STACKTRACE  **************");
		System.out.println("******************************************");
		System.out.println("Error: \n" + output);

		theException.printStackTrace();
	}

	public void notifyAdminByText() {
		System.out.println("******************************************");
		System.out.println("***  Unimplemented Error Notification  ***");
		System.out.println("******************************************");
		theException.printStackTrace();
	}

	public void notifyAdminByEmail() {
		System.out.println("******************************************");
		System.out.println("***  Unimplemented Error Notification  ***");
		System.out.println("******************************************");
		theException.printStackTrace();
	}

	public void notifySenderOfError() {
		System.out.println("******************************************");
		System.out.println("***  Unimplemented Error Notification  ***");
		System.out.println("******************************************");
		theException.printStackTrace();
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Exception getTheException() {
		return theException;
	}

	public void setTheException(Exception theException) {
		this.theException = theException;
	}
}
