package de.lemo.apps.services.internal.jqplot.js;

//
//Copyright 2010 GOT5 (GO Tapestry 5)
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//

/**
* Created By Got5 - https://github.com/got5/tapestry5-jqPlot
* 
*/

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.internal.services.javascript.CoreJavaScriptStack;
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

 public JqPlotJavaScriptStack(@Symbol(SymbolConstants.PRODUCTION_MODE)
                              final boolean productionMode,

                              final AssetSource assetSource)
 {
     this.productionMode = productionMode;

     final Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>()
     {

         public Asset map(String path)
         {
             return assetSource.getExpandedAsset(path);
         }
     };


     final Mapper<String, StylesheetLink> pathToStylesheetLink = F.combine(pathToAsset, JQueryUtils.assetToStylesheetLink);

     

     if (productionMode) {
     
      stylesheetStack = F.flow("classpath:de/lemo/apps/css/jquery.jqplot.min.css")
      .map(pathToStylesheetLink).toList();

         javaScriptStack = F.flow("classpath:de/lemo/apps/js/jqplot/jquery.jqplot.min.js")
         .map(pathToAsset).toList();

     } else {
     
      stylesheetStack = F.flow("classpath:de/lemo/apps/css/jquery.jqplot.css")
      .map(pathToStylesheetLink).toList();

         javaScriptStack = F.flow( "classpath:de/lemo/apps/js/jqplot/jquery.jqplot.js")
         .map(pathToAsset).toList();

     }

 }

 public String getInitialization()
 {
     return productionMode ? null : "Tapestry.DEBUG_ENABLED = true;";
 }

 public List<Asset> getJavaScriptLibraries()
 {
     return javaScriptStack;
 }

 public List<StylesheetLink> getStylesheets()
 {
     return stylesheetStack;
 }

 public List<String> getStacks()
 {
     return Collections.emptyList();
 }

}