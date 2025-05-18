package com.github.hanzm_10.murico.swingapp.lib.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class TableFilter<T> {
	private static class NamedRowFilter extends RowFilter<TableModel, Integer> {
		@NotNull
		final String name;
		@NotNull
		final RowFilter<TableModel, Integer> delegate;

		NamedRowFilter(@NotNull final String name, @NotNull final RowFilter<TableModel, Integer> delegate) {
			this.name = name;
			this.delegate = delegate;
		}

		@Override
		public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
			return delegate.include(entry);
		}
	}

	private @NotNull final TableRowSorter<TableModel> rowSorter;
	private @NotNull final List<RowFilter<TableModel, Integer>> filters;

	public TableFilter(TableRowSorter<TableModel> rowSorter) {
		this.rowSorter = rowSorter;
		this.filters = new ArrayList<RowFilter<TableModel, Integer>>();
	}

	private void applyFilters() {
		if (filters.isEmpty()) {
			rowSorter.setRowFilter(null);
			return;
		}

		var actualFilters = new ArrayList<RowFilter<TableModel, Integer>>();

		for (var nf : filters.stream().map((f) -> (NamedRowFilter) f).toList()) {
			actualFilters.add(nf.delegate);
		}

		rowSorter.setRowFilter(RowFilter.andFilter(actualFilters));
	}

	public void clear() {
		filters.clear();
		applyFilters();
	}

	private void removeFilter(@NotNull final String filterName) {
		filters.removeIf((f) -> {
			return f instanceof NamedRowFilter namedRowFilter && namedRowFilter.name.equals(filterName);
		});
	}

	public void setColumnFilter(@Range(from = 0, to = Integer.MAX_VALUE) final int columnIndex,
			@NotNull final String query) {
		var filterName = "col" + columnIndex;

		removeFilter(filterName);

		if (query != null && !query.isEmpty()) {
			filters.add(
					new NamedRowFilter(filterName, RowFilter.regexFilter("(?i)" + Pattern.quote(query), columnIndex)));
		}

		applyFilters();
	}

	public void setGlobalFuzzyFilter(@NotNull final String query) {
		removeFilter("fuzzy");

		if (query != null && !query.isEmpty()) {
			filters.add(new NamedRowFilter("fuzzy", new FuzzyFilter(query)));
		}

		applyFilters();
	}
}
