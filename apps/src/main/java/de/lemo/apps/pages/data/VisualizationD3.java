package de.lemo.apps.pages.data;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import de.lemo.apps.components.JqPlot;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;


import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="visualizationTitle")
public class VisualizationD3 {
	
	@Property
	private BreadCrumbInfo breadCrumb;
	
	private List<List<XYDataItem>> testData;

    @Cached
    public List getTestData()
    {
        List<List<XYDataItem>> dataList = CollectionFactory.newList();
        List<XYDataItem> list1 = CollectionFactory.newList();
        List<XYDataItem> list2 = CollectionFactory.newList();

        list1.add(new XYDataItem(0, 0));
        list1.add(new XYDataItem(6, 1));
        list1.add(new XYDataItem(12, 3));
        list1.add(new XYDataItem(18, 5));
        list1.add(new XYDataItem(24, 2));

        list2.add(new XYDataItem(0, 1));
        list2.add(new XYDataItem(6, 2));
        list2.add(new XYDataItem(12, 7));
        list2.add(new XYDataItem(18, 13.5));
        list2.add(new XYDataItem(24, 10));

        dataList.add(list1);
        dataList.add(list2);

        return dataList;
    }
	
	@Cached
    public List getTestPieData()
    {
        List<List<TextValueDataItem>> dataList = CollectionFactory.newList();
        List<TextValueDataItem> list1 = CollectionFactory.newList();
      
        list1.add(new TextValueDataItem("Mozilla Firefox",12));
        list1.add(new TextValueDataItem("Google Chrome", 9));
        list1.add(new TextValueDataItem("Safari (Webkit)",14));
        list1.add(new TextValueDataItem("Internet Explorer", 16));
        list1.add(new TextValueDataItem("Opera", 2));

      
        dataList.add(list1);
      
        return dataList;
    }
}
