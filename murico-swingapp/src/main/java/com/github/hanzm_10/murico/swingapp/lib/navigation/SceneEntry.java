/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.navigation.factory.SceneFactory;
import com.github.hanzm_10.murico.swingapp.lib.navigation.guard.SceneGuard;

/**
 * Represents an entry in the scene manager, containing information about a
 * specific scene.
 *
 * <p>
 * This class is used to define the properties of a scene, including its name,
 * factory for creating the scene, and guard for managing access to the scene.
 */
public class SceneEntry {
	protected final String name;
	protected final SceneFactory factory;
	protected final SceneGuard guard;

	private Pattern regexPatternMatcher;
	protected List<String> parameterKeys;
	protected int dynamicParamCount;

	public static final String DYNAMIC_PARAM_PREFIX = ":";

	public SceneEntry(@NotNull final String name, @NotNull final SceneFactory factory, @NotNull final SceneGuard guard)
			throws IllegalArgumentException {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Scene name cannot be empty");
		}

		if (name.contains("//")) {
			throw new IllegalArgumentException("Scene name cannot contain consecutive slashes");
		}

		if (factory == null) {
			throw new IllegalArgumentException("Scene factory cannot be null");
		}

		if (guard == null) {
			throw new IllegalArgumentException("Scene guard cannot be null");
		}

		this.name = name;
		this.factory = factory;
		this.guard = guard;

		var regexPatternMatcherBuilder = new StringBuilder();
		var parameterKeys = new HashMap<String, String>();
		var dynamicParamCount = 0;

		for (var subSceneName : name.split("/")) {
			if (subSceneName.startsWith(DYNAMIC_PARAM_PREFIX)) {
				regexPatternMatcherBuilder.append("/([^/]+)");
				parameterKeys.put(subSceneName.substring(1), subSceneName.substring(1));
				dynamicParamCount++;
			} else {
				regexPatternMatcherBuilder.append("/").append(subSceneName);
			}
		}

		this.regexPatternMatcher = Pattern.compile("^" + regexPatternMatcherBuilder.substring(1) + "$");
		this.parameterKeys = List.copyOf(parameterKeys.values());
		this.dynamicParamCount = dynamicParamCount;
	}

	public boolean matches(@NotNull final String sceneName) {
		return regexPatternMatcher.matcher(sceneName).matches();
	}

	public Map<String, String> matchMap(@NotNull final String sceneName) {
		var matcher = regexPatternMatcher.matcher(sceneName);

		if (!matcher.matches()) {
			return null;
		}

		var paramsMap = new HashMap<String, String>();

		for (var i = 0; i < dynamicParamCount; i++) {
			paramsMap.put(parameterKeys.get(i), matcher.group(i + 1));
		}

		return paramsMap;
	}

	/**
	 * @return The name of the scene. This is the unparsed name, not the *
	 *         regex-matched name. For example, if the scene name is "/user/:id",
	 *         this method will return "/user/:id".
	 */
	public String getUnMatchedSceneName() {
		return name;
	}

	public SceneFactory getSceneFactory() {
		return factory;
	}

	public SceneGuard getSceneGuard() {
		return guard;
	}
}
