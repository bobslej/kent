import bencoding.BCoder
import bencoding.format
import java.io.File

fun main(args: Array<String>) {
    val decoder = BCoder()
    val bytes = File("C:\\Users\\occ\\Downloads\\tor.torrent").readBytes()
    println(decoder.decode(bytes).format())
    //println(BFormatter().toString("d1:ad1:ci1ee1:bi2ee".toBType()))
    //println(BFormatter().toString("d1:ali1ee1:bi2ee".toBType()))
}