import java.nio.file.Path
import kotlin.io.path.*

val datasetRoot = Path("/mnt/ssd/Active-projects/ArtClass/python/dataset")
val badPaths = mutableListOf<Path>()

@ExperimentalPathApi
fun main() {
    detectBadPaths()
    badPaths.forEach { it.deleteExisting() }
}

@ExperimentalPathApi
private fun detectBadPaths() {
    fun isBad(path: Path) = path.name.contains("train".toRegex()) && path.name.contains('_').not()
    datasetRoot.walk().filter { isBad(it) }.forEach { badPaths.add(it) }
}