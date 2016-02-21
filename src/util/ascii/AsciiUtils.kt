package util.ascii

import util.ByteReader

private val MinDigit = '0'.toByte()
private val MaxDigit = '9'.toByte()
public val MinPrintable = ' '.toByte()
private val Minus = '-'.toByte()

fun ByteReader.getInt(endCode: Byte) : Int {
    val sign = this.next()
    var value = getDigit(if (sign == Minus) this.next() else sign)
    while(true) {
        val digitChar = this.next()
        if (digitChar == endCode)
            break;
        value *= 10
        value += getDigit(digitChar)
    }
    return if (sign == Minus) -value else value
}

fun Byte.isDigit()  = this >= MinDigit && this <= MaxDigit

private fun getDigit(byte: Byte) : Int =
        if (byte.isDigit())
            byte - MinDigit
        else
            throw IllegalArgumentException("digit expected, got $byte")