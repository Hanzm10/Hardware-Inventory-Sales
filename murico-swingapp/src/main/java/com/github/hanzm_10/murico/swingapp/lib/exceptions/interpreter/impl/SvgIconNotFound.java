package com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter.impl;

import com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter.ErrorInterpreter;

public class SvgIconNotFound implements ErrorInterpreter {

	@Override
	public boolean canInterpret(Throwable e) {
		return e.getMessage() != null
				&& (e.getMessage().contains("Icon not found") || e.getMessage().contains("\"xmlBase\" is null"));
	}

	@Override
	public String interpret(Throwable e) {
		return "Svg icon not found. Please check the icon path.";
	}

}
