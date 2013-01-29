/**
 * 
 */
package de.lemo.apps.services.internal;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;

import de.lemo.apps.entities.Course;

/**
 * @author johndoe
 *
 */
public class GenericValueEncoder<T> implements ValueEncoder<T> {

        private PropertyAdapter idFieldAdapter = null;
        private List<T> list;

        public GenericValueEncoder(List<T> list, String idField, PropertyAccess access) {
                if (idField != null && !idField.equalsIgnoreCase("null")){
                        if(list.size() > 0){
                                this.idFieldAdapter = access.getAdapter(list.get(0).getClass()).getPropertyAdapter(idField);
                        }
                }
                this.list = list;
        }

        @Override
        public String toClient(T obj) {
                if (idFieldAdapter == null) {
                        return nvl(obj);
                } else {
                        return nvl(idFieldAdapter.get(obj));
                }
        }

        @Override
        public T toValue(String string) {
        if (idFieldAdapter == null) {
            for (T obj : list) {
                if (nvl(obj).equals(string)) return obj;
            }
        } else {
            for (T obj : list) {
                if (nvl(idFieldAdapter.get(obj)).equals(string)) return obj;
            }
        }
        return null;
    }
        
        private String nvl(Object o) {
        if (o == null)
            return "";
        else
            return o.toString();
    }
}


