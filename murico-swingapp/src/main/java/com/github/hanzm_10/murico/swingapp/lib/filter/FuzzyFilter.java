package com.github.hanzm_10.murico.swingapp.lib.filter;

import java.util.Locale;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.jetbrains.annotations.NotNull;

public class FuzzyFilter extends RowFilter<TableModel, Integer> {
	private @NotNull final String query;
	private @NotNull final JaroWinklerSimilarity fuzzyFinder;
	private @NotNull final float threshold;

	public FuzzyFilter(@NotNull final String query) {
		this.query = query.toLowerCase(Locale.ENGLISH);
		this.threshold = 0.7f;
		this.fuzzyFinder = new JaroWinklerSimilarity();
	}

	@Override
	public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
		if (query == null || query.isBlank()) {
			return true;
		}

		for (var i = 0; i < entry.getValueCount(); ++i) {
			var val = entry.getValue(i);

			if (val == null) {
				continue;
			}

			if (val instanceof String) {
				var cell = val.toString().toLowerCase(Locale.ENGLISH);
				var similarity = fuzzyFinder.apply(query, cell);

				if (similarity >= threshold) {
					return true;
				}
			}
		}

		return false;
	}
}
