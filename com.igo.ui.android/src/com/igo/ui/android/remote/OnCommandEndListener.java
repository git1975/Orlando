package com.igo.ui.android.remote;

public interface OnCommandEndListener {
	/**
	 * Handler of server response event
	 * @param command - server Command
	 * @param result - abstract result
	 */
	public void OnCommandEnd(Command command, Object result);
	/**
	 * return hash of last data portion
	 * @return
	 */
	public String getLastHash();
}
