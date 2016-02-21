package bencoding

import util.ByteReader
import util.ascii.getInt
import util.ascii.isDigit
import java.util.*

class BCoder {

    fun decode(bytes: ByteArray) : BType {
        val reader = ByteReader(bytes)
        return decode(reader, reader.peek())
    }

    private fun decode(reader: ByteReader, code: Byte) : BType {
        if(code.isDigit()) {
            return decodeString(reader)
        }
        reader.skip()
        return when (code) {
            IntCode -> BType.BInt(reader.getInt(ValueEndCode))
            ListCode -> decodeList(reader)
            DictCode -> decodeDict(reader)
            else -> throw BEncodingException("unsupported code $code")
        }
    }

    private fun decodeString(reader: ByteReader) : BType.BString {
        val size = reader.getInt(StringStartCode)
        val offset = reader.pos()
        reader.skip(size)
        return BType.BString(reader.slice(offset, size))
    }

    private fun decodeList(reader: ByteReader) : BType.BList {
        val list = ArrayList<BType>()
        while(true) {
            val code = reader.peek()
            when(code) {
                ValueEndCode -> {
                    reader.skip()
                    return BType.BList(list)
                }
                else -> list.add(decode(reader, code))
            }
        }
    }

    private fun decodeDict(reader: ByteReader) : BType.BDict {
        val dict = TreeMap<BType.BString, BType>()
        while(true) {
            val code = reader.peek()
            when(code) {
                ValueEndCode -> {
                    reader.next()
                    return BType.BDict(dict)
                }
                else -> {
                    val key = decodeString(reader)
                    dict.put(key, decode(reader, reader.peek()))
                }
            }
        }
    }

    private val IntCode = 'i'.toByte()
    private val ListCode = 'l'.toByte()
    private val DictCode = 'd'.toByte()
    private val StringStartCode = ':'.toByte()
    private val ValueEndCode = 'e'.toByte()
}