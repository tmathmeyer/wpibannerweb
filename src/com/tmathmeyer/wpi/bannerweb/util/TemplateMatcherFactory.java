package com.tmathmeyer.wpi.bannerweb.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class TemplateMatcherFactory
{
    /**
     * 
     * @param typesAndContstructors a map from class name to {Class, Constructor-args}
     * @return a template matcher with the matching properties provided
     */
    public static <T> TemplateMatcher<T> createTemplateMatcher(Map<String, Entry<Class<? extends T>, Class<?>>> typesAndContstructors)
    {
        try
        {
            Map<String, Entry<Class<?>, Constructor<? extends T>>> constructors = new HashMap<>();
            for(Entry<String, Entry<Class<? extends T>, Class<?>>> bind : typesAndContstructors.entrySet())
            {
                final Entry<Class<? extends T>, Class<?>> type = bind.getValue();
                final Constructor<? extends T> constructor = type.getKey().getConstructor(type.getValue());
                constructors.put(bind.getKey(), new Entry<Class<?>, Constructor<? extends T>>() {

                    @Override
                    public Class<?> getKey()
                    {
                        return type.getValue();
                    }
                    @Override
                    public Constructor<? extends T> getValue()
                    {
                        return constructor;
                    }
                    @Override
                    public Constructor<? extends T> setValue(Constructor<? extends T> object)
                    {
                        throw new RuntimeException("cannot set value of immutable Entry!");
                    }
                });
            }
            return new TemplateMatcher<T>(constructors);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        return null; // throw an exception
    }
    
    public static class TemplateMatcher<T>
    {
        private final Map<String, Entry<Class<?>, Constructor<? extends T>>> constructors;
        
        public TemplateMatcher(Map<String, Entry<Class<?>, Constructor<? extends T>>> constructors)
        {
            this.constructors = constructors;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Template<T> getTemplate( Entry<Object, Class> ... objectsForTypes)
        {
            Map<Class<?>, Object> classmap = new HashMap<>();
            Map<String, Entry<Object, Constructor<? extends T>>> stringToLazyInstantiation = new HashMap<>();
            for(Entry<Object, Class> o : objectsForTypes)
            {
                classmap.put(o.getValue(), o.getKey());
            }
            for(final Entry<String, Entry<Class<?>, Constructor<? extends T>>> entry : constructors.entrySet())
            {
                stringToLazyInstantiation.put(entry.getKey(), new Entry<Object, Constructor<? extends T>>(){

                    @Override
                    public Object getKey()
                    {
                        return entry.getValue().getKey();
                    }

                    @Override
                    public Constructor<? extends T> getValue()
                    {
                        return entry.getValue().getValue();
                    }

                    @Override
                    public Constructor<? extends T> setValue(Constructor<? extends T> object)
                    {
                        throw new RuntimeException("cannot set value of immutable Entry!");
                    }
                    
                });
            }
            return new Template<>(stringToLazyInstantiation);
        }
    }
    
    public static class Template<T>
    {
        private final Map<String, Entry<Object, Constructor<? extends T>>> lazy;
        
        public Template(Map<String, Entry<Object, Constructor<? extends T>>> lazy)
        {
            this.lazy = lazy;
        }
        
        public T match(String input)
        {
            TemplateParser json = new Gson().fromJson(input, TemplateParser.class);
            
            return parse(json);
        }

        public T parse(TemplateParser json)
        {
            T t = create(json.type);
            if (json.methods != null)
            {
                for(MethodExecutor me : json.methods)
                {
                    if (me.call != null)
                    {
                        for(Argument a : me.call)
                        {
                            try
                            {
                                Method m = t.getClass().getMethod(me.name, a.getTypeClass(this));
                                m.invoke(t, a.getTypeObject());
                            }
                            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return t;
        }

        private T create(String type)
        {
            Entry<Object, Constructor<? extends T>> e = lazy.get(type);
            try
            {
                return e.getValue().newInstance(e.getKey());
            }
            catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
            {
                e1.printStackTrace();
                return null;
            }
        }
    }
    
    static class TemplateParser
    {
        public String type;
        public List<MethodExecutor> methods;
    }
    
    static class MethodExecutor
    {
        public String name;
        public List<Argument> call;
    }
    
    static class Argument
    {
        public String sValue;
        public Integer iValue;
        public TemplateParser oValue;
        
        private Object typeObject;
        
        public <T> Class<?> getTypeClass(Template<T> temp)
        {
            if (sValue != null)
            {
                typeObject = sValue;
                return String.class;
            }
            if (iValue != null)
            {
                typeObject = iValue;
                return Integer.class;
            }
            if (oValue != null)
            {
                typeObject = temp.parse(oValue);
                return typeObject.getClass();
            }
            return null;
        }

        public Object getTypeObject()
        {
            return typeObject;
        }
    }
    
    

    public static <S, K extends S> Entry<Class<? extends S>, Class<?>> createEntry(Class<S> s, final Class<K> k, final Class<?> v)
    {
        return new Entry<Class<? extends S>, Class<?>>(){
            @Override
            public Class<K> getKey()
            {
                return k;
            }
            @Override
            public Class<?> getValue()
            {
                return v;
            }
            @Override
            public Class<?> setValue(Class<?> arg0)
            {
                throw new RuntimeException("cannot set value of immutable Entry!");
            }
        };
    }
    
    public static <K, V> Entry<K, V> createEntry(final K k, final V v)
    {
        return new Entry<K, V>() {

            @Override
            public K getKey()
            {
                return k;
            }

            @Override
            public V getValue()
            {
                return v;
            }

            @Override
            public V setValue(V object)
            {
                throw new RuntimeException("cannot set value of immutable Entry!");
            }
        };
    }
}
