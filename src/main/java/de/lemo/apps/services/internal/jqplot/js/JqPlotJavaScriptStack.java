/**
 * File ./de/lemo/apps/services/internal/jqplot/js/JqPlotJavaScriptStack.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.services.internal.jqplot.js;

//
// Copyright 2010 GOT5 (GO Tapestry 5)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

/**
 * Created By Got5 - https://github.com/got5/tapestry5-jqPlot
 */

import java.util.Collections;
import java.util.List;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import org.got5.tapestry5.jquery.utils.JQueryUtils;

public class JqPlotJavaScriptStack implements JavaScriptStack {

	public static final String STACK_ID = "jqPlotStack";

	private final boolean productionMode;

	private final List<Asset> javaScriptStack;

	private final List<StylesheetLink> stylesheetStack;

	public JqPlotJavaScriptStack(@Symbol(SymbolConstants.PRODUCTION_MODE) final boolean productionMode,

			final AssetSource assetSource)
	{
		this.productionMode = productionMode;

		final Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>()
		{

			@Override
			public Asset map(final String path)
			{
				return assetSource.getExpandedAsset(path);
			}
		};

		final Mapper<String, StylesheetLink> pathToStylesheetLink = F.combine(pathToAsset,
				JQueryUtils.assetToStylesheetLink);

		if (productionMode) {

			this.stylesheetStack = F.flow("classpath:de/lemo/apps/css/jquery.jqplot.min.css")
					.map(pathToStylesheetLink).toList();

			this.javaScriptStack = F.flow("classpath:de/lemo/apps/js/jqplot/jquery.jqplot.min.js")
					.map(pathToAsset).toList();

		} else {

			this.stylesheetStack = F.flow("classpath:de/lemo/apps/css/jquery.jqplot.css")
					.map(pathToStylesheetLink).toList();

			this.javaScriptStack = F.flow("classpath:de/lemo/apps/js/jqplot/jquery.jqplot.js")
					.map(pathToAsset).toList();

		}

	}

	@Override
	public String getInitialization()
	{
		return this.productionMode ? null : "Tapestry.DEBUG_ENABLED = true;";
	}

	@Override
	public List<Asset> getJavaScriptLibraries()
	{
		return this.javaScriptStack;
	}

	@Override
	public List<StylesheetLink> getStylesheets()
	{
		return this.stylesheetStack;
	}

	@Override
	public List<String> getStacks()
	{
		return Collections.emptyList();
	}

}