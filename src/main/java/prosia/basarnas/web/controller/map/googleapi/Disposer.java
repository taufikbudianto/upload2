/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

/**
 *
 * @author PROSIA
 */
import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
//import org.apache.commons.lang.ArrayUtils;

public class Disposer {
    private static List<Class> exceptClazz = new ArrayList<Class>();
    private static Class formContainedMenuClass;
    
    public static void disposeObject(Object theObject) {

        if (theObject == null) {
            return;
        }

        Class clazz = theObject.getClass();
        Field[] fields = null;
        Method method = null;


        // get all field include parent class
        Class tempClass = clazz;
        while (tempClass != null) {
            //fields = (Field[]) ArrayUtils.addAll(fields, tempClass.getDeclaredFields());
            fields = null;
            tempClass = tempClass.getSuperclass();
            if (tempClass != null && (tempClass.equals(JFrame.class) || tempClass.equals(JPanel.class) || tempClass.equals(JInternalFrame.class) || tempClass.equals(JDialog.class) || tempClass.equals(JComponent.class) || tempClass.equals(Component.class))) {
                tempClass = null;
            }
        }


        for (int i = 0; i < fields.length; i++) {
            try {
                if (Modifier.isStatic(fields[i].getModifiers()) || isSubClassOrImplementingTheInterface(fields[i].getType(), JComponent.class)) {
                    continue;
                } else if (fields[i].getType().isPrimitive()) {
                    continue;
                }else if(Modifier.isFinal(fields[i].getModifiers())){
                    continue;
                }
                boolean flag = false;
                for (Class c : exceptClazz) {
                    if (fields[i].getType().equals(c)) {
                        flag = true;
                    }
                }
                if (flag) {
                    continue;
                }
                if (fields[i].getType().equals(Collection.class) || fields[i].getType().equals(Map.class)) {
                    method = getDeclaringMethod(clazz, getGetMethodName(fields[i].getName()));
                    
                    Object obj = method.invoke(theObject);
                    if (obj instanceof Map) {
                        if (obj != null) {
                            Map map = (Map) obj;
                            map.clear();
                            makeNullObject(fields[i].getName(), fields[i].getType(), clazz, theObject);
                        }
                    } else if (obj instanceof Collection) {
                        if (obj != null) {
                            Collection collection = (Collection) obj;
                            collection.clear();
                            makeNullObject(fields[i].getName(), fields[i].getType(), clazz, theObject);
                        }
                    }
                } else if (isSubClassOrImplementingTheInterface(fields[i].getType(), Map.class) || isSubClassOrImplementingTheInterface(fields[i].getType(), Collection.class)) {
                    method = getDeclaringMethod(clazz, getGetMethodName(fields[i].getName()));
                    Object obj = method.invoke(theObject);
                    if (obj instanceof Map) {
                        if (obj != null) {
                            Map map = (Map) obj;
                            map.clear();
                            makeNullObject(fields[i].getName(), fields[i].getType(), clazz, theObject);
                        }
                    } else if (obj instanceof Collection) {
                        if (obj != null) {
                            Collection collection = (Collection) obj;
                            collection.clear();
                            makeNullObject(fields[i].getName(), fields[i].getType(), clazz, theObject);
                        }
                    }
                }else if (isSubClassOrImplementingTheInterface(fields[i].getType(), formContainedMenuClass)){
                    Object obj = method.invoke(theObject);
                    if(obj != null){
                        Disposer.disposeObject(obj);
                        makeNullObject(fields[i].getName(), fields[i].getType(), clazz, theObject);
                    }
                }else {
                    makeNullObject(fields[i].getName(), fields[i].getType(), clazz, theObject);
                }
            } catch (NoSuchMethodException nsme) {
//                System.out.println("variable is private and setter not found : " + nsme.getMessage());
            } catch (IllegalAccessException iae) {
//                System.out.println("iae error disposer message : " + iae.getMessage());
            } catch (InvocationTargetException ite) {
//                System.out.println("ite error disposer message : " + ite.getMessage());
            } catch (Exception e) {
//                System.out.println("error disposer message : " + e.getMessage());
            }
        }
    }
    
    private static String getGetMethodName(String field) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    private static String getSetMethodName(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    
    /**
     * 
     */
    private static boolean isSubClassOrImplementingTheInterface(Class clazz, Class superClassOrInterface) {
        if (clazz.getSuperclass() != null) {
            if (clazz.getSuperclass().equals(superClassOrInterface)) {
                return true;
            } else {
                if (isImplementingInterface(clazz, superClassOrInterface)) {
                    return true;
                } else {
                    return isSubClassOrImplementingTheInterface(clazz.getSuperclass(), superClassOrInterface);
                }
            }
        } else if (clazz.getInterfaces() != null) {
            return isImplementingInterface(clazz, superClassOrInterface);
        } else {
            return false;
        }
    }

    
    private static boolean isImplementingInterface(Class interFace, Class superInterface) {
        if (interFace.getInterfaces() != null) {
            for (Class intrfc : interFace.getInterfaces()) {
                if (intrfc.equals(superInterface)) {
                    return true;
                } else {
                    return isImplementingInterface(intrfc, superInterface);
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * the field must have setter method and the setter method name must standard JavaBeans 
     * 
     * @param fieldName - the name of the field 
     * @param clazz - the type which contained the field
     * @param theObject - the instance which contained the field
     * 
     */
    private static void makeNullObject(String fieldName, Class fieldClass, Class clazz, Object theObject) throws Exception {
        Method method = getDeclaringMethod(clazz, getSetMethodName(fieldName), fieldClass);
        Object obj = null;
        method.invoke(theObject, obj);
    }

    public static List<Class> getExceptClazz() {
        return exceptClazz;
    }

    public static void setExceptClazz(List<Class> exceptClazz) {
        Disposer.exceptClazz = exceptClazz;
    }

    public static void setFormContainedMenuClass(Class formContainedMenuClass) {
        Disposer.formContainedMenuClass = formContainedMenuClass;
    }
    
    
    /**
     * find out Method in declaring specified class it allow inheritance
     */
    public static Method getDeclaringMethod(Class declaringSub, String methodName, Class... parameterType){
        Method result = null;
        Class classTemp = declaringSub;
        result = getDeclaringMethodSafe(declaringSub, methodName, parameterType);
        if(result == null){
            // for handle boolean getter method
            methodName = "is" + methodName.substring(4, methodName.length());
            result = getDeclaringMethodSafe(classTemp, methodName);
        }
        return result;
    }
    
    
    private static Method getDeclaringMethodSafe(Class declaringSub, String methodName, Class... parameterType){
        Method result = null;
        try {
            if(parameterType == null){
                result = declaringSub.getDeclaredMethod(methodName);
            }else{
                result = declaringSub.getDeclaredMethod(methodName, parameterType);
            }
        } catch (NoSuchMethodException e) {
            declaringSub = declaringSub.getSuperclass();
            if(declaringSub != null){
                result = getDeclaringMethodSafe(declaringSub, methodName);
            }
        }
        return result;
    }
}
