
public class NotTranspilableException extends Exception {
	public NotTranspilableException() {
		super("This NRL feature cannot be transpiled into valid Chicago BPF");
	}
}
