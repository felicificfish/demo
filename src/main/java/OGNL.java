import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 此类主要用于 mapper 文件中非空验证
 *
 * @demo <if test="orderColumn!= null and orderColumn != ''"> <br/>
 * ORDER BY t.${orderColumn} <if test="orderColumn!= null and orderDirection != ''">${orderDirection}</if><br/>
 * </if><br/>
 * <br/>
 * 就可以修改为 ：<br/>
 * <if test="@Ognl@isNotEmpty(orderColumn)"> <br/>
 * ORDER BY t.${orderColumn} <if test="@Ognl@isNotEmpty(orderColumn)">${orderDirection}</if><br/>
 * </if><br/>
 */
public class OGNL {

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            if (((String) obj).length() == 0) {
                return true;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).isEmpty()) {
                return true;
            }
        } else if (obj.getClass().isArray()) {
            if (Array.getLength(obj) == 0) {
                return true;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static boolean isCollection(Object o) {
        return isNotEmpty(o) && ((o instanceof Collection) || (o.getClass().isArray()));
    }

    public static void main(String[] args) {
        String[] s = {"s"};
        System.out.println(isCollection(s));
    }
}
