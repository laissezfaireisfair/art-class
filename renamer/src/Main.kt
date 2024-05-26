import java.nio.file.Path
import kotlin.io.path.*

val datasetRoot = Path("/mnt/ssd/Active-projects/ArtClass/python/dataset")
val badPaths = mutableListOf<Path>()

@ExperimentalPathApi
fun main() {
    detectBadPaths()
}

@ExperimentalPathApi
private fun detectBadPaths() {
    fun isBad(path: Path) = path.pathString.any { !it.isDigit() && !it.isLetter() && "/_.".contains(it).not() }
    datasetRoot.walk().filter { isBad(it) }.forEach { badPaths.add(it) }
}