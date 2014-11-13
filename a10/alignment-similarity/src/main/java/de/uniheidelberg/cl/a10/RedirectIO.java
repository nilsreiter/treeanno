package de.uniheidelberg.cl.a10;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * The methods in this class can be used to temporarily redirect System.err or
 * System.out.
 * 
 * @author reiter
 * 
 */
public class RedirectIO {
	static PrintStream original = null;
	static PrintStream original_err = null;

	public static void redirectOUT() {
		try {
			original = System.out;
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(final int b) {
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetOUT() {
		if (original != null)
			System.setOut(original);
	}

	public static void redirectERR() {
		try {
			original_err = System.err;
			System.setErr(new PrintStream(new OutputStream() {
				@Override
				public void write(final int b) {
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetERR() {
		if (original_err != null)
			System.setErr(original);
	}
}
