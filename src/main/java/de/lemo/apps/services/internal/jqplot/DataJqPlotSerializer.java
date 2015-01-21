/**
 * File ./src/main/java/de/lemo/apps/services/internal/jqplot/DataJqPlotSerializer.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package de.lemo.apps.services.internal.jqplot;

import org.apache.tapestry5.json.JSONArray;

/**
 * Created By Got5 - https://github.com/got5/tapestry5-jqPlot
 */

public interface DataJqPlotSerializer {

	JSONArray toJSONArray();

}
