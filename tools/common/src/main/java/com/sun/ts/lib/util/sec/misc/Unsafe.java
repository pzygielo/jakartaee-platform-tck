/*
 * Copyright (c) 2000, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.lib.util.sec.misc;

import java.security.*;
import java.lang.reflect.*;

/**
 * A collection of methods for performing low-level, unsafe operations. Although the class and all methods are public,
 * use of this class is limited because only trusted code can obtain instances of it.
 *
 * @author John R. Rose
 * @see #getUnsafe
 */

public final class Unsafe {

    private static native void registerNatives();

    static {
        registerNatives();
        com.sun.ts.lib.util.sec.reflect.Reflection.registerMethodsToFilter(Unsafe.class, "getUnsafe");
    }

    private Unsafe() {
    }

    private static final Unsafe theUnsafe = new Unsafe();

    /**
     * Provides the caller with the capability of performing unsafe operations.
     *
     *
     * The returned <code>Unsafe</code> object should be carefully guarded by the caller, since it can be used to read and
     * write data at arbitrary memory addresses. It must never be passed to untrusted code.
     *
     *
     * Most methods in this class are very low-level, and correspond to a small number of hardware instructions (on typical
     * machines). Compilers are encouraged to optimize these methods accordingly.
     *
     *
     * Here is a suggested idiom for using unsafe operations:
     *
     * <blockquote>
     * 
     * <pre>
     * class MyTrustedClass {
     *   private static final Unsafe unsafe = Unsafe.getUnsafe();
     *   ...
     *   private long myCountAddress = ...;
     *   public int getCount() { return unsafe.getByte(myCountAddress); }
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * (It may assist compilers to make the local variable be <code>final</code>.)
     *
     * @exception SecurityException if a security manager exists and its <code>checkPropertiesAccess</code> method doesn't
     * allow access to the system properties.
     */
    public static Unsafe getUnsafe() {
        Class cc = com.sun.ts.lib.util.sec.reflect.Reflection.getCallerClass(2);
        if (cc.getClassLoader() != null)
            throw new SecurityException("Unsafe");
        return theUnsafe;
    }

    /// peek and poke operations
    /// (compilers should optimize these to memory ops)

    // These work on object fields in the Java heap.
    // They will not work on elements of packed arrays.

    /**
     * Fetches a value from a given Java variable. More specifically, fetches a field or array element within the given
     * object <code>o</code> at the given offset, or (if <code>o</code> is null) from the memory address whose numerical
     * value is the given offset.
     *
     * The results are undefined unless one of the following cases is true:
     * <ul>
     * <li>The offset was obtained from {@link #objectFieldOffset} on the {@link java.lang.reflect.Field} of some Java field
     * and the object referred to by <code>o</code> is of a class compatible with that field's class.
     *
     * <li>The offset and object reference <code>o</code> (either null or non-null) were both obtained via
     * {@link #staticFieldOffset} and {@link #staticFieldBase} (respectively) from the reflective {@link Field}
     * representation of some Java field.
     *
     * <li>The object referred to by <code>o</code> is an array, and the offset is an integer of the form
     * <code>B+N*S</code>, where <code>N</code> is a valid index into the array, and <code>B</code> and <code>S</code> are
     * the values obtained by {@link #arrayBaseOffset} and {@link #arrayIndexScale} (respectively) from the array's class.
     * The value referred to is the <code>N</code><em>th</em> element of the array.
     *
     * </ul>
     *
     * If one of the above cases is true, the call references a specific Java variable (field or array element). However,
     * the results are undefined if that variable is not in fact of the type returned by this method.
     *
     * This method refers to a variable by means of two parameters, and so it provides (in effect) a
     * <em>double-register</em> addressing mode for Java variables. When the object reference is null, this method uses its
     * offset as an absolute address. This is similar in operation to methods such as {@link #getInt(long)}, which provide
     * (in effect) a <em>single-register</em> addressing mode for non-Java variables. However, because Java variables may
     * have a different layout in memory from non-Java variables, programmers should not assume that these two addressing
     * modes are ever equivalent. Also, programmers should remember that offsets from the double-register addressing mode
     * cannot be portably confused with longs used in the single-register addressing mode.
     *
     * @param o Java heap object in which the variable resides, if any, else null
     * @param offset indication of where the variable resides in a Java heap object, if any, else a memory address locating
     * the variable statically
     * @return the value fetched from the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even {@link NullPointerException}
     */
    public native int getInt(Object o, long offset);

    /**
     * Stores a value into a given Java variable.
     *
     * The first two parameters are interpreted exactly as with {@link #getInt(Object, long)} to refer to a specific Java
     * variable (field or array element). The given value is stored into that variable.
     *
     * The variable must be of the same type as the method parameter <code>x</code>.
     *
     * @param o Java heap object in which the variable resides, if any, else null
     * @param offset indication of where the variable resides in a Java heap object, if any, else a memory address locating
     * the variable statically
     * @param x the value to store into the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even {@link NullPointerException}
     */
    public native void putInt(Object o, long offset, int x);

    /**
     * Fetches a reference value from a given Java variable.
     * 
     * @see #getInt(Object, long)
     */
    public native Object getObject(Object o, long offset);

    /**
     * Stores a reference value into a given Java variable.
     *
     * Unless the reference <code>x</code> being stored is either null or matches the field type, the results are undefined.
     * If the reference <code>o</code> is non-null, car marks or other store barriers for that object (if the VM requires
     * them) are updated.
     * 
     * @see #putInt(Object, int, int)
     */
    public native void putObject(Object o, long offset, Object x);

    /** @see #getInt(Object, long) */
    public native boolean getBoolean(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putBoolean(Object o, long offset, boolean x);

    /** @see #getInt(Object, long) */
    public native byte getByte(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putByte(Object o, long offset, byte x);

    /** @see #getInt(Object, long) */
    public native short getShort(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putShort(Object o, long offset, short x);

    /** @see #getInt(Object, long) */
    public native char getChar(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putChar(Object o, long offset, char x);

    /** @see #getInt(Object, long) */
    public native long getLong(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putLong(Object o, long offset, long x);

    /** @see #getInt(Object, long) */
    public native float getFloat(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putFloat(Object o, long offset, float x);

    /** @see #getInt(Object, long) */
    public native double getDouble(Object o, long offset);

    /** @see #putInt(Object, int, int) */
    public native void putDouble(Object o, long offset, double x);

    /**
     * This method, like all others with 32-bit offsets, was native in a previous release but is now a wrapper which simply
     * casts the offset to a long value. It provides backward compatibility with bytecodes compiled against 1.4.
     * 
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public int getInt(Object o, int offset) {
        return getInt(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putInt(Object o, int offset, int x) {
        putInt(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public Object getObject(Object o, int offset) {
        return getObject(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putObject(Object o, int offset, Object x) {
        putObject(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public boolean getBoolean(Object o, int offset) {
        return getBoolean(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putBoolean(Object o, int offset, boolean x) {
        putBoolean(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public byte getByte(Object o, int offset) {
        return getByte(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putByte(Object o, int offset, byte x) {
        putByte(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public short getShort(Object o, int offset) {
        return getShort(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putShort(Object o, int offset, short x) {
        putShort(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public char getChar(Object o, int offset) {
        return getChar(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putChar(Object o, int offset, char x) {
        putChar(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public long getLong(Object o, int offset) {
        return getLong(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putLong(Object o, int offset, long x) {
        putLong(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public float getFloat(Object o, int offset) {
        return getFloat(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putFloat(Object o, int offset, float x) {
        putFloat(o, (long) offset, x);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public double getDouble(Object o, int offset) {
        return getDouble(o, (long) offset);
    }

    /**
     * @deprecated As of 1.4.1, cast the 32-bit offset argument to a long. See {@link #staticFieldOffset}.
     */
    @Deprecated
    public void putDouble(Object o, int offset, double x) {
        putDouble(o, (long) offset, x);
    }

    // These work on values in the C heap.

    /**
     * Fetches a value from a given memory address. If the address is zero, or does not point into a block obtained from
     * {@link #allocateMemory}, the results are undefined.
     *
     * @see #allocateMemory
     */
    public native byte getByte(long address);

    /**
     * Stores a value into a given memory address. If the address is zero, or does not point into a block obtained from
     * {@link #allocateMemory}, the results are undefined.
     *
     * @see #getByte(long)
     */
    public native void putByte(long address, byte x);

    /** @see #getByte(long) */
    public native short getShort(long address);

    /** @see #putByte(long, byte) */
    public native void putShort(long address, short x);

    /** @see #getByte(long) */
    public native char getChar(long address);

    /** @see #putByte(long, byte) */
    public native void putChar(long address, char x);

    /** @see #getByte(long) */
    public native int getInt(long address);

    /** @see #putByte(long, byte) */
    public native void putInt(long address, int x);

    /** @see #getByte(long) */
    public native long getLong(long address);

    /** @see #putByte(long, byte) */
    public native void putLong(long address, long x);

    /** @see #getByte(long) */
    public native float getFloat(long address);

    /** @see #putByte(long, byte) */
    public native void putFloat(long address, float x);

    /** @see #getByte(long) */
    public native double getDouble(long address);

    /** @see #putByte(long, byte) */
    public native void putDouble(long address, double x);

    /**
     * Fetches a native pointer from a given memory address. If the address is zero, or does not point into a block obtained
     * from {@link #allocateMemory}, the results are undefined.
     *
     *
     * If the native pointer is less than 64 bits wide, it is extended as an unsigned number to a Java long. The pointer may
     * be indexed by any given byte offset, simply by adding that offset (as a simple integer) to the long representing the
     * pointer. The number of bytes actually read from the target address maybe determined by consulting
     * {@link #addressSize}.
     *
     * @see #allocateMemory
     */
    public native long getAddress(long address);

    /**
     * Stores a native pointer into a given memory address. If the address is zero, or does not point into a block obtained
     * from {@link #allocateMemory}, the results are undefined.
     *
     *
     * The number of bytes actually written at the target address maybe determined by consulting {@link #addressSize}.
     *
     * @see #getAddress(long)
     */
    public native void putAddress(long address, long x);

    /// wrappers for malloc, realloc, free:

    /**
     * Allocates a new block of native memory, of the given size in bytes. The contents of the memory are uninitialized;
     * they will generally be garbage. The resulting native pointer will never be zero, and will be aligned for all value
     * types. Dispose of this memory by calling {@link #freeMemory}, or resize it with {@link #reallocateMemory}.
     *
     * @throws IllegalArgumentException if the size is negative or too large for the native size_t type
     *
     * @throws OutOfMemoryError if the allocation is refused by the system
     *
     * @see #getByte(long)
     * @see #putByte(long, byte)
     */
    public native long allocateMemory(long bytes);

    /**
     * Resizes a new block of native memory, to the given size in bytes. The contents of the new block past the size of the
     * old block are uninitialized; they will generally be garbage. The resulting native pointer will be zero if and only if
     * the requested size is zero. The resulting native pointer will be aligned for all value types. Dispose of this memory
     * by calling {@link #freeMemory}, or resize it with {@link #reallocateMemory}. The address passed to this method may be
     * null, in which case an allocation will be performed.
     *
     * @throws IllegalArgumentException if the size is negative or too large for the native size_t type
     *
     * @throws OutOfMemoryError if the allocation is refused by the system
     *
     * @see #allocateMemory
     */
    public native long reallocateMemory(long address, long bytes);

    /**
     * Sets all bytes in a given block of memory to a fixed value (usually zero).
     */
    public native void setMemory(long address, long bytes, byte value);

    /**
     * Sets all bytes in a given block of memory to a copy of another block.
     */
    public native void copyMemory(long srcAddress, long destAddress, long bytes);

    /**
     * Disposes of a block of native memory, as obtained from {@link #allocateMemory} or {@link #reallocateMemory}. The
     * address passed to this method may be null, in which case no action is taken.
     *
     * @see #allocateMemory
     */
    public native void freeMemory(long address);

    /// random queries

    /**
     * This constant differs from all results that will ever be returned from {@link #staticFieldOffset},
     * {@link #objectFieldOffset}, or {@link #arrayBaseOffset}.
     */
    public static final int INVALID_FIELD_OFFSET = -1;

    /**
     * Returns the offset of a field, truncated to 32 bits. This method is implemented as follows: <blockquote>
     * 
     * <pre>
     * public int fieldOffset(Field f) {
     *     if (Modifier.isStatic(f.getModifiers()))
     *         return (int) staticFieldOffset(f);
     *     else
     *         return (int) objectFieldOffset(f);
     * }
     * </pre>
     * 
     * </blockquote>
     * 
     * @deprecated As of 1.4.1, use {@link #staticFieldOffset} for static fields and {@link #objectFieldOffset} for
     * non-static fields.
     */
    @Deprecated
    public int fieldOffset(Field f) {
        if (Modifier.isStatic(f.getModifiers()))
            return (int) staticFieldOffset(f);
        else
            return (int) objectFieldOffset(f);
    }

    /**
     * Returns the base address for accessing some static field in the given class. This method is implemented as follows:
     * <blockquote>
     * 
     * <pre>
     * public Object staticFieldBase(Class c) {
     *     Field[] fields = c.getDeclaredFields();
     *     for (int i = 0; i < fields.length; i++) {
     *         if (Modifier.isStatic(fields[i].getModifiers())) {
     *             return staticFieldBase(fields[i]);
     *         }
     *     }
     *     return null;
     * }
     * </pre>
     * 
     * </blockquote>
     * 
     * @deprecated As of 1.4.1, use {@link #staticFieldBase(Field)} to obtain the base pertaining to a specific
     * {@link Field}. This method works only for JVMs which store all statics for a given class in one place.
     */
    @Deprecated
    public Object staticFieldBase(Class c) {
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                return staticFieldBase(fields[i]);
            }
        }
        return null;
    }

    /**
     * Report the location of a given field in the storage allocation of its class. Do not expect to perform any sort of
     * arithmetic on this offset; it is just a cookie which is passed to the unsafe heap memory accessors.
     *
     *
     * Any given field will always have the same offset and base, and no two distinct fields of the same class will ever
     * have the same offset and base.
     *
     *
     * As of 1.4.1, offsets for fields are represented as long values, although the Sun JVM does not use the most
     * significant 32 bits. However, JVM implementations which store static fields at absolute addresses can use long
     * offsets and null base pointers to express the field locations in a form usable by {@link #getInt(Object,long)}.
     * Therefore, code which will be ported to such JVMs on 64-bit platforms must preserve all bits of static field offsets.
     * 
     * @see #getInt(Object, long)
     */
    public native long staticFieldOffset(Field f);

    /**
     * Report the location of a given static field, in conjunction with {@link #staticFieldBase}.
     *
     * Do not expect to perform any sort of arithmetic on this offset; it is just a cookie which is passed to the unsafe
     * heap memory accessors.
     *
     *
     * Any given field will always have the same offset, and no two distinct fields of the same class will ever have the
     * same offset.
     *
     *
     * As of 1.4.1, offsets for fields are represented as long values, although the Sun JVM does not use the most
     * significant 32 bits. It is hard to imagine a JVM technology which needs more than a few bits to encode an offset
     * within a non-array object, However, for consistency with other methods in this class, this method reports its result
     * as a long value.
     * 
     * @see #getInt(Object, long)
     */
    public native long objectFieldOffset(Field f);

    /**
     * Report the location of a given static field, in conjunction with {@link #staticFieldOffset}.
     *
     * Fetch the base "Object", if any, with which static fields of the given class can be accessed via methods like
     * {@link #getInt(Object, long)}. This value may be null. This value may refer to an object which is a "cookie", not
     * guaranteed to be a real Object, and it should not be used in any way except as argument to the get and put routines
     * in this class.
     */
    public native Object staticFieldBase(Field f);

    /**
     * Ensure the given class has been initialized. This is often needed in conjunction with obtaining the static field base
     * of a class.
     */
    public native void ensureClassInitialized(Class c);

    /**
     * Report the offset of the first element in the storage allocation of a given array class. If {@link #arrayIndexScale}
     * returns a non-zero value for the same class, you may use that scale factor, together with this base offset, to form
     * new offsets to access elements of arrays of the given class.
     *
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public native int arrayBaseOffset(Class arrayClass);

    /**
     * Report the scale factor for addressing elements in the storage allocation of a given array class. However, arrays of
     * "narrow" types will generally not work properly with accessors like {@link #getByte(Object, int)}, so the scale
     * factor for such classes is reported as zero.
     *
     * @see #arrayBaseOffset
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public native int arrayIndexScale(Class arrayClass);

    /**
     * Report the size in bytes of a native pointer, as stored via {@link #putAddress}. This value will be either 4 or 8.
     * Note that the sizes of other primitive types (as stored in native memory blocks) is determined fully by their
     * information content.
     */
    public native int addressSize();

    /**
     * Report the size in bytes of a native memory page (whatever that is). This value will always be a power of two.
     */
    public native int pageSize();

    /// random trusted operations from JNI:

    /**
     * Tell the VM to define a class, without security checks. By default, the class loader and protection domain come from
     * the caller's class.
     */
    public native Class defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain);

    public native Class defineClass(String name, byte[] b, int off, int len);

    /**
     * Allocate an instance but do not run any constructor. Initializes the class if it has not yet been.
     */
    public native Object allocateInstance(Class cls) throws InstantiationException;

    /** Lock the object. It must get unlocked via {@link #monitorExit}. */
    public native void monitorEnter(Object o);

    /**
     * Unlock the object. It must have been locked via {@link #monitorEnter}.
     */
    public native void monitorExit(Object o);

    /**
     * Tries to lock the object. Returns true or false to indicate whether the lock succeeded. If it did, the object must be
     * unlocked via {@link #monitorExit}.
     */
    public native boolean tryMonitorEnter(Object o);

    /** Throw the exception without telling the verifier. */
    public native void throwException(Throwable ee);

    /**
     * Atomically update Java variable to <code>x</code> if it is currently holding <code>expected</code>.
     * 
     * @return <code>true</code> if successful
     */
    public final native boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);

    /**
     * Atomically update Java variable to <code>x</code> if it is currently holding <code>expected</code>.
     * 
     * @return <code>true</code> if successful
     */
    public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x);

    /**
     * Atomically update Java variable to <code>x</code> if it is currently holding <code>expected</code>.
     * 
     * @return <code>true</code> if successful
     */
    public final native boolean compareAndSwapLong(Object o, long offset, long expected, long x);

    /**
     * Fetches a reference value from a given Java variable, with volatile load semantics. Otherwise identical to
     * {@link #getObject(Object, long)}
     */
    public native Object getObjectVolatile(Object o, long offset);

    /**
     * Stores a reference value into a given Java variable, with volatile store semantics. Otherwise identical to
     * {@link #putObject(Object, long, Object)}
     */
    public native void putObjectVolatile(Object o, long offset, Object x);

    /** Volatile version of {@link #getInt(Object, long)} */
    public native int getIntVolatile(Object o, long offset);

    /** Volatile version of {@link #putInt(Object, long, int)} */
    public native void putIntVolatile(Object o, long offset, int x);

    /** Volatile version of {@link #getBoolean(Object, long)} */
    public native boolean getBooleanVolatile(Object o, long offset);

    /** Volatile version of {@link #putBoolean(Object, long, boolean)} */
    public native void putBooleanVolatile(Object o, long offset, boolean x);

    /** Volatile version of {@link #getByte(Object, long)} */
    public native byte getByteVolatile(Object o, long offset);

    /** Volatile version of {@link #putByte(Object, long, byte)} */
    public native void putByteVolatile(Object o, long offset, byte x);

    /** Volatile version of {@link #getShort(Object, long)} */
    public native short getShortVolatile(Object o, long offset);

    /** Volatile version of {@link #putShort(Object, long, short)} */
    public native void putShortVolatile(Object o, long offset, short x);

    /** Volatile version of {@link #getChar(Object, long)} */
    public native char getCharVolatile(Object o, long offset);

    /** Volatile version of {@link #putChar(Object, long, char)} */
    public native void putCharVolatile(Object o, long offset, char x);

    /** Volatile version of {@link #getLong(Object, long)} */
    public native long getLongVolatile(Object o, long offset);

    /** Volatile version of {@link #putLong(Object, long, long)} */
    public native void putLongVolatile(Object o, long offset, long x);

    /** Volatile version of {@link #getFloat(Object, long)} */
    public native float getFloatVolatile(Object o, long offset);

    /** Volatile version of {@link #putFloat(Object, long, float)} */
    public native void putFloatVolatile(Object o, long offset, float x);

    /** Volatile version of {@link #getDouble(Object, long)} */
    public native double getDoubleVolatile(Object o, long offset);

    /** Volatile version of {@link #putDouble(Object, long, double)} */
    public native void putDoubleVolatile(Object o, long offset, double x);

    /**
     * Version of {@link #putObjectVolatile(Object, long, Object)} that does not guarantee immediate visibility of the store
     * to other threads. This method is generally only useful if the underlying field is a Java volatile (or if an array
     * cell, one that is otherwise only accessed using volatile accesses).
     */
    public native void putOrderedObject(Object o, long offset, Object x);

    /** Ordered/Lazy version of {@link #putIntVolatile(Object, long, int)} */
    public native void putOrderedInt(Object o, long offset, int x);

    /** Ordered/Lazy version of {@link #putLongVolatile(Object, long, long)} */
    public native void putOrderedLong(Object o, long offset, long x);

    /**
     * Unblock the given thread blocked on <code>park</code>, or, if it is not blocked, cause the subsequent call to
     * <code>park</code> not to block. Note: this operation is "unsafe" solely because the caller must somehow ensure that
     * the thread has not been destroyed. Nothing special is usually required to ensure this when called from Java (in which
     * there will ordinarily be a live reference to the thread) but this is not nearly-automatically so when calling from
     * native code.
     * 
     * @param thread the thread to unpark.
     *
     */
    public native void unpark(Object thread);

    /**
     * Block current thread, returning when a balancing <code>unpark</code> occurs, or a balancing <code>unpark</code> has
     * already occurred, or the thread is interrupted, or, if not absolute and time is not zero, the given time nanoseconds
     * have elapsed, or if absolute, the given deadline in milliseconds since Epoch has passed, or spuriously (i.e.,
     * returning for no "reason"). Note: This operation is in the Unsafe class only because <code>unpark</code> is, so it
     * would be strange to place it elsewhere.
     */
    public native void park(boolean isAbsolute, long time);

    /**
     * Gets the load average in the system run queue assigned to the available processors averaged over various periods of
     * time. This method retrieves the given <code>nelem</code> samples and assigns to the elements of the given
     * <code>loadavg</code> array. The system imposes a maximum of 3 samples, representing averages over the last 1, 5, and
     * 15 minutes, respectively.
     *
     * @params loadavg an array of double of size nelems
     * @params nelems the number of samples to be retrieved and must be 1 to 3.
     *
     * @return the number of samples actually retrieved; or -1 if the load average is unobtainable.
     */
    public native int getLoadAverage(double[] loadavg, int nelems);
}
