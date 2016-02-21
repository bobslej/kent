package util

import util.ascii.MinPrintable

class ByteSlice(private val bytes: ByteArray, private val offset: Int, val size: Int) : Comparable<ByteSlice> {
    operator fun get(idx: Int) : Byte = bytes[idx + offset]
    override fun toString() = if ((offset..offset+size-1).all { bytes[it] >= MinPrintable}) (offset..offset+size-1).map { bytes[it].toChar() }.joinToString("") else "($size bytes)"
    override fun equals(other: Any?) = other is ByteSlice && other.size == size && (0..size-1).all{ other[it] == this[it] }
    override fun compareTo(other: ByteSlice) = (0..Math.min(size, other.size)-1).map { this[it] - other[it] }.firstOrNull { it != 0 } ?: size - other.size
}
