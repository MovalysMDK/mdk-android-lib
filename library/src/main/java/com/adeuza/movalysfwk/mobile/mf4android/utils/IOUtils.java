package com.adeuza.movalysfwk.mobile.mf4android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Input/Output utils class
 * @author lmichenaud
 * 
 */
public final class IOUtils {

	/** Taille max du tableau de bytes */
	private static final int ARRAY_BYTES_SIZE = 4096;

	/**
	 * Constructor
	 */
	private IOUtils(){
		//Nothing To Do
	}
	
	/**
	 * Copy a file from src to dest
	 * @param p_oSrc file to copy
	 * @param p_oDest resulting copied file
	 * @throws IOException copy failure
	 */
	public static void copyFile(File p_oSrc, File p_oDest) throws IOException {
		FileChannel oSource = new FileInputStream(p_oSrc).getChannel();
		try {
			FileChannel oDest = new FileOutputStream(p_oDest).getChannel();
			try {
				oDest.transferFrom(oSource, 0, oSource.size());
			} finally {
				oDest.close();
			}
		} finally {
			oSource.close();
		}
	}
	
	/**
	 * Convert a InputStream to a byte array
	 * @param p_oInput input stream
	 * @return the byte array corresponding to the param input stream
	 * @throws IOException conversion failure
	 */
	public static byte[] toByteArray(InputStream p_oInput) throws IOException {
		ByteArrayOutputStream oOutput = new ByteArrayOutputStream();
		byte[] oBuffer = new byte[ARRAY_BYTES_SIZE];
		int iNbRead = 0;
		while (-1 != (iNbRead = p_oInput.read(oBuffer))) {
			oOutput.write(oBuffer, 0, iNbRead);
		}
		return oOutput.toByteArray();
	}
}
