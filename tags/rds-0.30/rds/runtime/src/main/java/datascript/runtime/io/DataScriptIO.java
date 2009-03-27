package datascript.runtime.io;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import datascript.runtime.CallChain;
import datascript.runtime.DataScriptError;

/**
 * Convenience class for reading and writing DataScript objects from and to 
 * byte arrays.
 * <p>
 * Usage:
 * <pre>
 * MyType myType1;
 * byte[] blob = DataScriptIO.write(myType1);
 * 
 * MyType myType2 = DataScriptIO.read(MyType.class, blob);
 * </pre>
 * 
 * @author HWellmann
 */
public class DataScriptIO
{
    /**
     * Takes a DataScript object, writes it to a byte array stream and returns
     * the resulting byte array.
     * 
     * @param <E>   DataScript class generated by rds
     * @param obj   a DataScript object of the given class
     * @return byte array with a serialized version of the given object
     */
    public static <E extends Writer> byte[] write(E obj)
    {
        try
        {
            ByteArrayBitStreamWriter writer = new ByteArrayBitStreamWriter();
            obj.write(writer, new CallChain());
            writer.close();
            return writer.toByteArray();
        }
        catch (Exception exc)
        {
            throw new DataScriptError(exc);
        }
    }
    
    /**
     * A "virtual constructor", building a DataScript object of a given class, 
     * reading from a byte array.
     * 
     * @param <E>        DataScript class generated by rds
     * @param clazz      the Class instance of the given class
     * @param byteArray  byte array to be read
     * @return  DataScript object of the given class.
     */
    public static <E> E read(Class<E> clazz, byte[] byteArray)
    {
        try
        {
            ByteArrayBitStreamReader reader = new ByteArrayBitStreamReader(
                    byteArray);
            Constructor<E> constructor = clazz
                    .getConstructor(BitStreamReader.class);
            E e = constructor.newInstance(reader);
            return e;
        }
        catch (Exception exc)
        {
            throw new DataScriptError(exc);
        }
    }

    /**
     * A "virtual constructor", building a DataScript object of a given class, 
     * reading from a byte array and passing additional arguments to the
     * DataScript object constructor.
     * 
     * @param <E>        DataScript class generated by rds
     * @param clazz      the Class instance of the given class
     * @param byteArray  byte array to be read
     * @param args       additional constructor arguments
     * @return  DataScript object of the given class.
     */
    public static <E> E read(Class<E> clazz, byte[] byteArray, Object... args)
    {
        try
        {
            ByteArrayBitStreamReader reader = new ByteArrayBitStreamReader(
                    byteArray);
            
            // build argument array
            ArrayList<Object> argList = new ArrayList<Object>();
            argList.add(reader);
            for (Object arg : args)
            {
                argList.add(arg);
            }
            
            // find a matching constructor for these arguments
            Constructor<E> constructor = findConstructor(clazz, argList);
            if (constructor == null)
            {
                throw new DataScriptError("no matching constructor");
            }
            
            Object[] argArray = new Object[argList.size()];
            argArray = argList.toArray(argArray);
                        
            E e = constructor.newInstance(argArray);
            return e;
        }
        catch (Exception exc)
        {
            throw new DataScriptError(exc);
        }
    }
    
    /**
     * Finds a constructor for a given class matching a given argument list.
     * For primitive paramter types, a boxed argument will be regarded as a 
     * a match, e.g. an Integer argument will match an int paramter.
     * 
     * @param <E>     class to be constructed
     * @param clazz   Class instance of this class
     * @param argList argument list for constructor
     * @return matching constructor, or null if there is no match
     */
    @SuppressWarnings("unchecked")
    private static <E> Constructor<E> findConstructor(Class<E> clazz, List<Object> argList)
    {
        Constructor<?>[] constructors = clazz.getConstructors();

        // Iterate over constructors and match arguments
        for (Constructor<?> constr : constructors)
        {
            Class<?>[] parameterTypes = constr.getParameterTypes();
            
            // No match if number of arguments is not equal to number of 
            // parameters
            if (argList.size() != parameterTypes.length)
                continue;

            // Now try to match each argument
            boolean match = true;            
            for (int i = 0; i < argList.size(); i++)
            {
                Class<?> type = parameterTypes[i];
                Object arg = argList.get(i);
                if (!isAssignableFrom(type,arg.getClass()))
                {
                    match = false;
                }
            }
            if (match)
                return (Constructor<E>) constr;
        }
        return null;
    }
    
    /**
     * Checks if left hand class is assignable from right hand class. Primitive
     * types are regarded as assignable from the corresponding boxed type.
     * @param left   left hand side of assignment
     * @param right  right hand side of assignment
     * @return true iff classes are assignable
     */
    @SuppressWarnings("unchecked")
    private static boolean isAssignableFrom(Class<?> left, Class<?> right)
    {
        if (left.isPrimitive())
        {
            if (left == Byte.TYPE)
                return right == Byte.class;
            else if (left == Character.TYPE)
                return right == Character.class;
            else if (left == Integer.TYPE)
                return right == Integer.class;
            else if (left == Long.TYPE)
                return right == Long.class;
            else if (left == Short.TYPE)
                return right == Short.class;
            else if (left == Boolean.TYPE)
                return right == Boolean.class;
            else if (left == Float.TYPE)
                return right == Float.class;
            else if (left == Double.TYPE)
                return right == Double.class;
            else
                return false;
        }
        else
            return left.isAssignableFrom(right);
    }
    
}
