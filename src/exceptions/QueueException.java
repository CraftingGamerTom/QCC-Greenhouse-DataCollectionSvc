
package exceptions;

public class QueueException extends Exception {

	private static final long serialVersionUID = 1L;

	public QueueException(String string) {
		super(string);
	}
	public QueueException(Exception e) {
		super(e);
	}
	public QueueException(String s, Throwable e) {
		super(s, e);
	}
}
