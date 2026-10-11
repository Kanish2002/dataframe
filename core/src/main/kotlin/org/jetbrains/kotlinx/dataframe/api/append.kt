package org.jetbrains.kotlinx.dataframe.api

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.columns.values
import org.jetbrains.kotlinx.dataframe.impl.api.updateWith
import org.jetbrains.kotlinx.dataframe.ncol
import org.jetbrains.kotlinx.dataframe.nrow

// region DataFrame

/**
 * Adds one or several rows to this [DataFrame], assigning [values] in column order, row by row.
 *
 * A list supplied for a column group must contain exactly one value per column in the group, in column order.
 * This also applies to lists supplied for nested column groups.
 * A [org.jetbrains.kotlinx.dataframe.DataRow] supplied for a column group is matched by column name:
 * missing columns produce `null`, extra columns are ignored, and column order does not affect the result.
 *
 * @param values The values of the rows to append, in row order.
 * @return A [DataFrame] containing the existing rows followed by the appended rows.
 * @throws IllegalArgumentException If the number of values is not a multiple of the number of columns,
 * non-empty values are supplied for a dataframe with no columns, a list supplied for a column group has
 * an incorrect size, or a value is incompatible with its column.
 */
public fun <T> DataFrame<T>.append(vararg values: Any?): DataFrame<T> {
    if (values.isEmpty()) return this

    val ncol = ncol
    require(ncol > 0) {
        "Cannot append values to a DataFrame with no columns"
    }
    require(values.size % ncol == 0) {
        "Invalid number of arguments. Multiple of $ncol is expected, but actual was: ${values.size}"
    }
    val newRows = values.size / ncol
    return columns().mapIndexed { colIndex, col ->
        val newValues = (0 until newRows).map { values[colIndex + it * ncol] }
        col.updateWith(col.values + newValues)
    }.toDataFrame().cast()
}

public fun <T> DataFrame<T>.appendNulls(numberOfRows: Int = 1): DataFrame<T> {
    require(numberOfRows >= 0)
    if (numberOfRows == 0) return this
    if (ncol == 0) return DataFrame.empty(nrow + numberOfRows).cast()
    return columns().map { col ->
        col.updateWith(col.values + arrayOfNulls(numberOfRows))
    }.toDataFrame().cast()
}

// endregion
