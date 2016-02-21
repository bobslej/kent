package util

public class ByteReader(private val bytes: ByteArray) {
    private var idx = 0
    fun pos() = idx
    fun peek() = bytes[idx]
    fun next() = bytes[idx++]
    fun skip(n: Int = 1) {
        idx+=n
    }
    fun slice(offset: Int, size: Int) = ByteSlice(bytes, offset, size)
}