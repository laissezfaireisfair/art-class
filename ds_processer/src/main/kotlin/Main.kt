package and.pac

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.io.path.*

val datasetPath = Path("/mnt/ssd/Active-projects/ArtClass/dataset")
val resultPath = Path("/mnt/ssd/Active-projects/ArtClass/dataset_shaped_and_filtered")
const val minimumImagesPerClass = 10

val authorByFilename = mutableMapOf<String, String>()
val countByAuthor = mutableMapOf<String, Int>()

@ExperimentalPathApi
fun main() {
    populateDictionariesWithCsv()
    println("Initialised")

    moveFolder(datasetPath / "train", resultPath / "train")
    moveFolder(datasetPath / "test", resultPath / "test")
}

fun populateDictionariesWithCsv() = csvReader().open(File((datasetPath / "all_data_info.csv").toUri())) {
    readAllAsSequence().drop(1).forEach { authorByFilename[it[11]] = it[0] }
    authorByFilename.forEach { (_, author) -> countByAuthor[author] = (countByAuthor[author] ?: 1) + 1 }
}

@ExperimentalPathApi
fun moveFolder(fromDir: Path, toDir: Path) {
    fromDir.walk().forEach {
        try {
            val author = authorByFilename[it.name] ?: throw InputMismatchException("No ${it.name} in csv")
            if (countByAuthor[author]!! < minimumImagesPerClass) return@forEach
            with(toDir / author) {
                if (notExists()) createDirectories()
                it.copyTo(this / (fromDir.name + it.name))
            }
        } catch (exception: Exception) {
            println(exception)
        }
    }
    println("${fromDir.name} moved")
}