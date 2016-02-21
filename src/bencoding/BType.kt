package bencoding

import util.ByteSlice
import java.util.*

sealed class BType {
    class BInt(val value: Int) : BType() {
        override fun equals(other: Any?) = other is BInt && this.value == other.value
    }
    class BString(val slice: ByteSlice) : BType(), Comparable<BString> {
        override fun equals(other: Any?) = other is BString && slice == other.slice
        override fun compareTo(other: BString) = slice.compareTo(other.slice)
    }
    class BList(val values: List<BType>) : BType() {
        override fun equals(other: Any?) = other is BList && other.values.size == values.size && other.values.zip(values).all { it.first == it.second }
    }
    class BDict(val pairs: SortedMap<BString, BType>) : BType() {
        override fun equals(other: Any?) = other is BDict && other.pairs.size == pairs.size && other.pairs.all { pairs[it.key] == it.value }
    }
}


fun String.toBType()  =  BCoder().decode(this.toByteArray())
fun Int.toBInt() : BType = BType.BInt(this)
fun String.toBString() : BType.BString = BType.BString(ByteSlice(this.toByteArray(), 0, this.length))
fun List<BType>.toBList()  = BType.BList(this)
fun SortedMap<BType.BString, BType>.toBDict() = BType.BDict(this)

fun BType.format() : String {
    return when(this){
        is BType.BInt -> this.value.toString()
        is BType.BString -> "\"${this.slice.toString()}\""
        is BType.BList -> "[ ${this.values.map{ it.format() }.joinToString(", ")} ]"
        is BType.BDict -> "{ ${this.pairs.map { it.key.format() + ": " + it.value.format() }.joinToString(", ")} }"
    }
}