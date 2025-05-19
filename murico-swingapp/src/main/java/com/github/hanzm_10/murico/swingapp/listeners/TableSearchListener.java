package com.github.hanzm_10.murico.swingapp.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.filter.TableFilter;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class TableSearchListener<T> implements DocumentListener {
	private static final Logger LOGGER = MuricoLogger.getLogger(TableSearchListener.class);
	private @NotNull final TableFilter<T> tableFilter;

	public TableSearchListener(@NotNull final TableFilter<T> tableFilter) {
		this.tableFilter = tableFilter;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		search(e);
	}

	public TableFilter<T> getTableFilter() {
		return tableFilter;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		search(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		search(e);
	}

	private void search(DocumentEvent e) {
		try {
			tableFilter.setGlobalFuzzyFilter(e.getDocument().getText(0, e.getDocument().getLength()));
		} catch (BadLocationException e1) {
			LOGGER.log(Level.SEVERE, "Failed to update table search listener", e1);
		}
	}

}
