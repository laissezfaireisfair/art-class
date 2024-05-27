import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.io.path.*

val datasetPath = Path("/mnt/ssd/Active-projects/ArtClass/unpacked")
val resultPath = Path("/mnt/ssd/Active-projects/ArtClass/dataset")

val authorByFilename = mutableMapOf<String, String>()

@ExperimentalPathApi
fun main() {
    initializeDictionaryWithCsv()
    println("Initialised")

    for (i in 1..9)
        moveFolder(datasetPath / "train_$i", resultPath / "train")
    moveFolder(datasetPath / "test", resultPath / "test")
}

fun initializeDictionaryWithCsv() {
    fun parse(line: String) = line
        .split('"')
        .mapIndexed { index, block -> if (index % 2 == 1) block.filterNot { it == ',' } else block }
        .joinToString("")
        .split(',')
        .takeIf { it.size == 12 }
        ?.let { it[0] to it[11] } ?: throw InputMismatchException(line)

    (datasetPath / "all_data_info.csv")
        .toUri()
        .let { File(it) }
        .useLines { lines ->
            lines.drop(1).forEach {
                parse(it).let { (author, filename) -> authorByFilename[filename] = author }
            }
        }
}

@ExperimentalPathApi
fun moveFolder(fromDir: Path, toDir: Path) {
    fromDir.walk().forEach {
        try {
            val authorDir =
                toDir / (authorByFilename[it.name] ?: throw InputMismatchException("No ${it.name} in csv"))
            if (authorDir.notExists())
                authorDir.createDirectories()
            it.moveTo(authorDir / (fromDir.name + it.name))
        } catch (exception: Exception) {
            println(exception)
        }
    }
    println("${fromDir.name} moved")
}