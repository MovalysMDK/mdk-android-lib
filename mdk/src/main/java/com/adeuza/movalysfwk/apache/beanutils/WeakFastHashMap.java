/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.apache.beanutils;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * <p>A customized implementation of <code>java.util.HashMap</code> designed
 * to operate in a multithreaded environment where the large majority of
 * method calls are read-only, instead of structural changes.  When operating
 * in "fast" mode, read calls are non-synchronized and write calls perform the
 * following steps:</p>
 * <ul>
 * <li>Clone the existing collection
 * <li>Perform the modification on the clone
 * <li>Replace the existing collection with the (modified) clone
 * </ul>
 * <p>When first created, objects of this class default to "slow" mode, where
 * all accesses of any type are synchronized but no cloning takes place.  This
 * is appropriate for initially populating the collection, followed by a switch
 * to "fast" mode (by calling <code>setFast(true)</code>) after initialization
 * is complete.</p>
 *
 * <p><strong>NOTE</strong>: If you are creating and accessing a
 * <code>HashMap</code> only within a single thread, you should use
 * <code>java.util.HashMap</code> directly (with no synchronization), for
 * maximum performance.</p>
 *
 * <p><strong>NOTE</strong>: <i>This class is not cross-platform.  
 * Using it may cause unexpected failures on some architectures.</i>
 * It suffers from the same problems as the double-checked locking idiom.  
 * In particular, the instruction that clones the internal collection and the 
 * instruction that sets the internal reference to the clone can be executed 
 * or perceived out-of-order.  This means that any read operation might fail 
 * unexpectedly, as it may be reading the state of the internal collection
 * before the internal collection is fully formed.
 * For more information on the double-checked locking idiom, see the
 * <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">
 * Double-Checked Locking Idiom Is Broken Declaration</a>.</p>
 *
 * @since Commons Collections 1.0
 * @version $Revision: 687089 $ $Date: 2008-08-19 17:33:30 +0100 (Tue, 19 Aug 2008) $
 * 
 * @author Craig R. McClanahan
 * @author Stephen Colebourne
 */
class WeakFastHashMap extends HashMap {

    /**
     * The underlying map we are managing.
     */
    private Map map = null;

    /**
     * Are we currently operating in "fast" mode?
     */
    private boolean fast = false;

    // Constructors
    // ----------------------------------------------------------------------

    /**
     * Construct an empty map.
     */
    public WeakFastHashMap() {
        super();
        this.map = createMap();
    }

    /**
     * Construct an empty map with the specified capacity.
     *
     * @param capacity  the initial capacity of the empty map
     */
    public WeakFastHashMap(int capacity) {
        super();
        this.map = createMap(capacity);
    }

    /**
     * Construct an empty map with the specified capacity and load factor.
     *
     * @param capacity  the initial capacity of the empty map
     * @param factor  the load factor of the new map
     */
    public WeakFastHashMap(int capacity, float factor) {
        super();
        this.map = createMap(capacity, factor);
    }

    /**
     * Construct a new map with the same mappings as the specified map.
     *
     * @param map  the map whose mappings are to be copied
     */
    public WeakFastHashMap(Map map) {
        super();
        this.map = createMap(map);
    }


    // Property access
    // ----------------------------------------------------------------------

    /**
     *  Returns true if this map is operating in fast mode.
     *
     *  @return true if this map is operating in fast mode
     */
    public boolean getFast() {
        return (this.fast);
    }

    /**
     *  Sets whether this map is operating in fast mode.
     *
     *  @param fast true if this map should operate in fast mode
     */
    public void setFast(boolean fast) {
        this.fast = fast;
    }


    // Map access
    // ----------------------------------------------------------------------
    // These methods can forward straight to the wrapped Map in 'fast' mode.
    // (because they are query methods)

    /**
     * Return the value to which this map maps the specified key.  Returns
     * <code>null</code> if the map contains no mapping for this key, or if
     * there is a mapping with a value of <code>null</code>.  Use the
     * <code>containsKey()</code> method to disambiguate these cases.
     *
     * @param key  the key whose value is to be returned
     * @return the value mapped to that key, or null
     */
    @Override
	public Object get(Object key) {
        if (fast) {
            return (map.get(key));
        } else {
            synchronized (map) {
                return (map.get(key));
            }
        }
    }

    /**
     * Return the number of key-value mappings in this map.
     * 
     * @return the current size of the map
     */
    @Override
	public int size() {
        if (fast) {
            return (map.size());
        } else {
            synchronized (map) {
                return (map.size());
            }
        }
    }

    /**
     * Return <code>true</code> if this map contains no mappings.
     * 
     * @return is the map currently empty
     */
    @Override
	public boolean isEmpty() {
        if (fast) {
            return (map.isEmpty());
        } else {
            synchronized (map) {
                return (map.isEmpty());
            }
        }
    }

    /**
     * Return <code>true</code> if this map contains a mapping for the
     * specified key.
     *
     * @param key  the key to be searched for
     * @return true if the map contains the key
     */
    @Override
	public boolean containsKey(Object key) {
        if (fast) {
            return (map.containsKey(key));
        } else {
            synchronized (map) {
                return (map.containsKey(key));
            }
        }
    }

    /**
     * Return <code>true</code> if this map contains one or more keys mapping
     * to the specified value.
     *
     * @param value  the value to be searched for
     * @return true if the map contains the value
     */
    @Override
	public boolean containsValue(Object value) {
        if (fast) {
            return (map.containsValue(value));
        } else {
            synchronized (map) {
                return (map.containsValue(value));
            }
        }
    }

    // Map modification
    // ----------------------------------------------------------------------
    // These methods perform special behaviour in 'fast' mode.
    // The map is cloned, updated and then assigned back.
    // See the comments at the top as to why this won't always work.

    /**
     * Associate the specified value with the specified key in this map.
     * If the map previously contained a mapping for this key, the old
     * value is replaced and returned.
     *
     * @param key  the key with which the value is to be associated
     * @param value  the value to be associated with this key
     * @return the value previously mapped to the key, or null
     */
    @Override
	public Object put(Object key, Object value) {
        if (fast) {
            synchronized (this) {
                Map temp = cloneMap(map);
                Object result = temp.put(key, value);
                map = temp;
                return (result);
            }
        } else {
            synchronized (map) {
                return (map.put(key, value));
            }
        }
    }

    /**
     * Copy all of the mappings from the specified map to this one, replacing
     * any mappings with the same keys.
     *
     * @param in  the map whose mappings are to be copied
     */
    @Override
	public void putAll(Map in) {
        if (fast) {
            synchronized (this) {
                Map temp =  cloneMap(map);
                temp.putAll(in);
                map = temp;
            }
        } else {
            synchronized (map) {
                map.putAll(in);
            }
        }
    }

    /**
     * Remove any mapping for this key, and return any previously
     * mapped value.
     *
     * @param key  the key whose mapping is to be removed
     * @return the value removed, or null
     */
    @Override
	public Object remove(Object key) {
        if (fast) {
            synchronized (this) {
                Map temp = cloneMap(map);
                Object result = temp.remove(key);
                map = temp;
                return (result);
            }
        } else {
            synchronized (map) {
                return (map.remove(key));
            }
        }
    }

    /**
     * Remove all mappings from this map.
     */
    @Override
	public void clear() {
        if (fast) {
            synchronized (this) {
                map = createMap();
            }
        } else {
            synchronized (map) {
                map.clear();
            }
        }
    }

    // Basic object methods
    // ----------------------------------------------------------------------
    
    /**
     * Compare the specified object with this list for equality.  This
     * implementation uses exactly the code that is used to define the
     * list equals function in the documentation for the
     * <code>Map.equals</code> method.
     *
     * @param o  the object to be compared to this list
     * @return true if the two maps are equal
     */
    @Override
	public boolean equals(Object o) {
        // Simple tests that require no synchronization
        if (o == this) {
            return (true);
        } else if (!(o instanceof Map)) {
            return (false);
        }
        Map mo = (Map) o;

        // Compare the two maps for equality
        if (fast) {
            if (mo.size() != map.size()) {
                return (false);
            }
            Iterator i = map.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry e = (Map.Entry) i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                if (value == null) {
                    if (!(mo.get(key) == null && mo.containsKey(key))) {
                        return (false);
                    }
                } else {
                    if (!value.equals(mo.get(key))) {
                        return (false);
                    }
                }
            }
            return (true);
            
        } else {
            synchronized (map) {
                if (mo.size() != map.size()) {
                    return (false);
                }
                Iterator i = map.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry e = (Map.Entry) i.next();
                    Object key = e.getKey();
                    Object value = e.getValue();
                    if (value == null) {
                        if (!(mo.get(key) == null && mo.containsKey(key))) {
                            return (false);
                        }
                    } else {
                        if (!value.equals(mo.get(key))) {
                            return (false);
                        }
                    }
                }
                return (true);
            }
        }
    }

    /**
     * Return the hash code value for this map.  This implementation uses
     * exactly the code that is used to define the list hash function in the
     * documentation for the <code>Map.hashCode</code> method.
     * 
     * @return suitable integer hash code
     */
    @Override
	public int hashCode() {
        if (fast) {
            int h = 0;
            Iterator i = map.entrySet().iterator();
            while (i.hasNext()) {
                h += i.next().hashCode();
            }
            return (h);
        } else {
            synchronized (map) {
                int h = 0;
                Iterator i = map.entrySet().iterator();
                while (i.hasNext()) {
                    h += i.next().hashCode();
                }
                return (h);
            }
        }
    }

    /**
     * Return a shallow copy of this <code>FastHashMap</code> instance.
     * The keys and values themselves are not copied.
     * 
     * @return a clone of this map
     */
    @Override
	public Object clone() {
        WeakFastHashMap results = null;
        if (fast) {
            results = new WeakFastHashMap(map);
        } else {
            synchronized (map) {
                results = new WeakFastHashMap(map);
            }
        }
        results.setFast(getFast());
        return (results);
    }

    // Map views
    // ----------------------------------------------------------------------
    
    /**
     * Return a collection view of the mappings contained in this map.  Each
     * element in the returned collection is a <code>Map.Entry</code>.
     * @return the set of map Map entries
     */
    @Override
	public Set entrySet() {
        return new EntrySet();
    }

    /**
     * Return a set view of the keys contained in this map.
     * @return the set of the Map's keys
     */
    @Override
	public Set keySet() {
        return new KeySet();
    }

    /**
     * Return a collection view of the values contained in this map.
     * @return the set of the Map's values
     */
    @Override
	public Collection values() {
        return new Values();
    }

    // Abstractions on Map creations (for subclasses such as WeakFastHashMap)
    // ----------------------------------------------------------------------

    protected Map createMap() {
        return new WeakHashMap();
    }

    protected Map createMap(int capacity) {
        return new WeakHashMap(capacity);
    }

    protected Map createMap(int capacity, float factor) {
        return new WeakHashMap(capacity, factor);
    }
    
    protected Map createMap(Map map) {
        return new WeakHashMap(map);
    }
    
    protected Map cloneMap(Map map) {
        return createMap(map);
    }

    // Map view inner classes
    // ----------------------------------------------------------------------

    /**
     * Abstract collection implementation shared by keySet(), values() and entrySet().
     */
    private abstract class CollectionView implements Collection {

        public CollectionView() {
        }

        protected abstract Collection get(Map map);
        protected abstract Object iteratorNext(Map.Entry entry);


        @Override
		public void clear() {
            if (fast) {
                synchronized (WeakFastHashMap.this) {
                    map = createMap();
                }
            } else {
                synchronized (map) {
                    get(map).clear();
                }
            }
        }

        @Override
		public boolean remove(Object o) {
            if (fast) {
                synchronized (WeakFastHashMap.this) {
                    Map temp = cloneMap(map);
                    boolean r = get(temp).remove(o);
                    map = temp;
                    return r;
                }
            } else {
                synchronized (map) {
                    return get(map).remove(o);
                }
            }
        }

        @Override
		public boolean removeAll(Collection o) {
            if (fast) {
                synchronized (WeakFastHashMap.this) {
                    Map temp = cloneMap(map);
                    boolean r = get(temp).removeAll(o);
                    map = temp;
                    return r;
                }
            } else {
                synchronized (map) {
                    return get(map).removeAll(o);
                }
            }
        }

        @Override
		public boolean retainAll(Collection o) {
            if (fast) {
                synchronized (WeakFastHashMap.this) {
                    Map temp = cloneMap(map);
                    boolean r = get(temp).retainAll(o);
                    map = temp;
                    return r;
                }
            } else {
                synchronized (map) {
                    return get(map).retainAll(o);
                }
            }
        }

        @Override
		public int size() {
            if (fast) {
                return get(map).size();
            } else {
                synchronized (map) {
                    return get(map).size();
                }
            }
        }


        @Override
		public boolean isEmpty() {
            if (fast) {
                return get(map).isEmpty();
            } else {
                synchronized (map) {
                    return get(map).isEmpty();
                }
            }
        }

        @Override
		public boolean contains(Object o) {
            if (fast) {
                return get(map).contains(o);
            } else {
                synchronized (map) {
                    return get(map).contains(o);
                }
            }
        }

        @Override
		public boolean containsAll(Collection o) {
            if (fast) {
                return get(map).containsAll(o);
            } else {
                synchronized (map) {
                    return get(map).containsAll(o);
                }
            }
        }

        @Override
		public Object[] toArray(Object[] o) {
            if (fast) {
                return get(map).toArray(o);
            } else {
                synchronized (map) {
                    return get(map).toArray(o);
                }
            }
        }

        @Override
		public Object[] toArray() {
            if (fast) {
                return get(map).toArray();
            } else {
                synchronized (map) {
                    return get(map).toArray();
                }
            }
        }


        @Override
		public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (fast) {
                return get(map).equals(o);
            } else {
                synchronized (map) {
                    return get(map).equals(o);
                }
            }
        }

        @Override
		public int hashCode() {
            if (fast) {
                return get(map).hashCode();
            } else {
                synchronized (map) {
                    return get(map).hashCode();
                }
            }
        }

        @Override
		public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
		public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        @Override
		public Iterator iterator() {
            return new CollectionViewIterator();
        }

        private class CollectionViewIterator implements Iterator {

            private Map expected;
            private Map.Entry lastReturned = null;
            private Iterator iterator;

            public CollectionViewIterator() {
                this.expected = map;
                this.iterator = expected.entrySet().iterator();
            }
 
            @Override
			public boolean hasNext() {
                if (expected != map) {
                    throw new ConcurrentModificationException();
                }
                return iterator.hasNext();
            }

            @Override
			public Object next() {
                if (expected != map) {
                    throw new ConcurrentModificationException();
                }
                lastReturned = (Map.Entry)iterator.next();
                return iteratorNext(lastReturned);
            }

            @Override
			public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                if (fast) {
                    synchronized (WeakFastHashMap.this) {
                        if (expected != map) {
                            throw new ConcurrentModificationException();
                        }
                        WeakFastHashMap.this.remove(lastReturned.getKey());
                        lastReturned = null;
                        expected = map;
                    }
                } else {
                    iterator.remove();
                    lastReturned = null;
                }
            }
        }
    }

    /**
     * Set implementation over the keys of the FastHashMap
     */
    private class KeySet extends CollectionView implements Set {
    
        @Override
		protected Collection get(Map map) {
            return map.keySet();
        }
    
        @Override
		protected Object iteratorNext(Map.Entry entry) {
            return entry.getKey();
        }
    
    }
    
    /**
     * Collection implementation over the values of the FastHashMap
     */
    private class Values extends CollectionView {
    
        @Override
		protected Collection get(Map map) {
            return map.values();
        }
    
        @Override
		protected Object iteratorNext(Map.Entry entry) {
            return entry.getValue();
        }
    }
    
    /**
     * Set implementation over the entries of the FastHashMap
     */
    private class EntrySet extends CollectionView implements Set {
    
        @Override
		protected Collection get(Map map) {
            return map.entrySet();
        }
    
        @Override
		protected Object iteratorNext(Map.Entry entry) {
            return entry;
        }
    
    }

}
