package de.ustu.ims.reiter.treeanno;

public class Perm {
	public static final int NO_ACCESS = 0;
	public static final int READ_ACCESS = 10;
	public static final int WRITE_ACCESS = 20;
	public static final int PADMIN_ACCESS = 25;
	public static final int ADMIN_ACCESS = 30;

	public int getNOACCESS() {
		return NO_ACCESS;
	}

	public int getREADACCESS() {
		return READ_ACCESS;
	}

	public int getWRITEACCESS() {
		return WRITE_ACCESS;
	}

	public int getPADMINACCESS() {
		return PADMIN_ACCESS;
	}

	public int getADMINACCESS() {
		return ADMIN_ACCESS;
	}
}
