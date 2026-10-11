package org.jetbrains.kotlinx.dataframe.io

import io.kotest.matchers.shouldBe
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.NullabilityOptions
import org.junit.Test
import kotlin.io.path.toPath

class ParquetIterableTest {
    private val url = ParquetIterableTest::class.java.classLoader.getResource("test.arrow.parquet")!!
    private val path = url.toURI().toPath()

    @Test
    fun `read paths without spread operator`() {
        DataFrame.readParquet(listOf(path, path)) shouldBe DataFrame.readParquet(path, path)
    }

    @Test
    fun `read files without spread operator`() {
        val file = path.toFile()
        DataFrame.readParquet(listOf(file, file)) shouldBe DataFrame.readParquet(file, file)
    }

    @Test
    fun `read urls without spread operator`() {
        DataFrame.readParquet(listOf(url, url)) shouldBe DataFrame.readParquet(url, url)
    }

    @Test
    fun `read strings without spread operator`() {
        val location = path.toString()
        DataFrame.readParquet(listOf(location, location)) shouldBe DataFrame.readParquet(location, location)
    }

    @Test
    fun `iterable is consumed once`() {
        var iterations = 0
        val paths = Iterable {
            iterations++
            check(iterations == 1)
            listOf(path, path).iterator()
        }

        DataFrame.readParquet(paths).rowsCount() shouldBe 600
        iterations shouldBe 1
    }

    @Test
    fun `all overloads forward nullability and batch size`() {
        val nullability = NullabilityOptions.Widening
        val expected = DataFrame.readParquet(path, nullability = nullability, batchSize = 17)

        DataFrame.readParquet(listOf(path), nullability = nullability, batchSize = 17) shouldBe expected
        DataFrame.readParquet(listOf(path.toFile()), nullability = nullability, batchSize = 17) shouldBe expected
        DataFrame.readParquet(listOf(url), nullability = nullability, batchSize = 17) shouldBe expected
        DataFrame.readParquet(listOf(path.toString()), nullability = nullability, batchSize = 17) shouldBe expected
    }
}
