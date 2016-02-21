package bencoding

import org.junit.Test
import org.junit.Assert.*;
import util.ByteSlice

public class BCoderTests {
    @Test fun decodeInt_zero() {
        assertEquals("i0e".toBType(), BType.BInt(0))
    }

    @Test fun decodeInt_positive() {
        assertEquals("i1e".toBType(), 1.toBInt())
        assertEquals("i10e".toBType(), 10.toBInt())
        assertEquals("i987654321e".toBType(), 987654321.toBInt())
        assertEquals("i1234567890e".toBType(), 1234567890.toBInt())
    }

    @Test fun decodeInt_negative() {
        assertEquals("i-1e".toBType(), (-1).toBInt())
        assertEquals("i-10e".toBType(), (-10).toBInt())
        assertEquals("i-987654321e".toBType(), (-987654321).toBInt())
        assertEquals("i-1234567890e".toBType(), (-1234567890).toBInt())
    }

    @Test fun decodeString_empty() {
        assertEquals("0:".toBType(), "".toBString())
    }

    @Test fun decodeString_random() {
        assertEquals("3:abc".toBType(), "abc".toBString())
        var bytes = arrayOf('3'.toByte(), ':'.toByte(), 0.toByte(), 1.toByte(), (255).toByte()).toByteArray()
        assertEquals(BCoder().decode(bytes), BType.BString(ByteSlice(bytes, 2, 3)))
    }

    @Test fun decodeList_empty() {
        assertEquals("le".toBType(), BType.BList(listOf()))
    }

    @Test fun decodeList_ints() {
        assertEquals("li1ei0ei-1ee".toBType(), listOf(1.toBInt(), 0.toBInt(), (-1).toBInt()).toBList())
    }

    @Test fun decodeList_ints_and_strings() {
        assertEquals("li1e2:101:0i0ei-1e3:a-1e".toBType(), listOf(1.toBInt(), "10".toBString(), "0".toBString(), 0.toBInt(), (-1).toBInt(), "a-1".toBString()).toBList())
    }

    @Test fun decodeList_nested() {
        assertEquals("lli1ee1:al2:-1ee".toBType(), listOf(listOf(1.toBInt()).toBList(), "a".toBString(), listOf("-1".toBString()).toBList()).toBList())
    }

    @Test fun decodeDict_empty() {
        assertEquals("de".toBType(), BType.BDict(sortedMapOf()))
    }

    @Test fun decodeDict_random() {
        assertEquals("d1:a1:b2:bci10ee".toBType(), sortedMapOf("a".toBString() to "b".toBString(), "bc".toBString() to 10.toBInt()).toBDict())
        assertEquals("d1:ali1ee1:bi2ee".toBType(), sortedMapOf("a".toBString() to listOf(1.toBInt()).toBList(), "b".toBString() to 2.toBInt()).toBDict())
        assertEquals("d1:ad1:ci1ee1:bi2ee".toBType(), sortedMapOf("a".toBString() to sortedMapOf("c".toBString() to 1.toBInt()).toBDict(),"b".toBString() to 2.toBInt()).toBDict())
    }
}