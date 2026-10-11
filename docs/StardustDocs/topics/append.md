[//]: # (title: append)

Adds one or several rows to [`DataFrame`](DataFrame.md)

Values are assigned in column order, row by row. The total number of values must be a multiple of the
number of columns.

A list supplied for a column group must contain exactly one value per column in that group, in column order.
Shorter and longer lists throw `IllegalArgumentException`; the message identifies the group and the expected
and actual sizes. The same validation applies to lists supplied for nested column groups.

A [`DataRow`](DataRow.md) supplied for a column group is matched by column name: missing columns produce
`null`, extra columns are ignored, and column order does not affect the result.

```kotlin
df.append(
    "Mike", 15,
    "John", 17, 
    "Bill", 30,
)
```

If the [compiler plugin](Compiler-Plugin.md) is enabled, a typesafe overload of `append` is available for `@DataSchema` classes.

```kotlin
@DataSchema
data class Person(val name: String, val age: Int)
```

```kotlin
val df = dataFrameOf(
    Person("Mike", 15),
    Person("John", 17),
)

df.append(Person("Bill", 30))
```

**Related operations**: [](appendDuplicate.md)
