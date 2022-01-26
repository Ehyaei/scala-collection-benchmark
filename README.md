Scala Data Collections Performance
================

Scala has different data collections and using the proper objects is
important in optimizing the big data pipelines.

This post tries to study the characteristics of the Scala collection,
such as:

-   Memory Usage,

-   Operations Time.

from the point of view of practical benchmarks. We will try to study the
performance of data collection in two posts; part one related to
`Sequence` collection, the second contains `Maps` and `Sets`. The
diagram below demonstrates all of the collections in the package
`scala.collection`. These are all high-level abstract classes or traits
with both mutable and immutable implementations.

<img src="images/seq.svg" title="Mutable and Immutable Data Collection High-level Abstract Classes or Traits" alt="Mutable and Immutable Data Collection High-level Abstract Classes or Traits" style="display: block; margin: auto;" />

## Sequence Type

The `Seq` trait represents sequences. A sequence is a type of iterable
with a length and elements with fixed index positions beginning at 0.
`Seq` Collection divided to type of immutable and mutable. The following
figure shows all `Seq` collections in package
`scala.collection.immutable`.

<img src="images/Immutable.svg" title="Immutable Seq Data Collections" alt="Immutable Seq Data Collections" style="display: block; margin: auto;" />

And the following figure shows `Seq` collections in package
`scala.collection.mutable`.

<img src="images/Mutable1.svg" title="Mutable Seq Data Collections Part 1" alt="Mutable Seq Data Collections Part 1" style="display: block; margin: auto;" />

<img src="images/Mutable2.svg" title="Mutable Seq Data Collections Part 2" alt="Mutable Seq Data Collections Part 2" style="display: block; margin: auto;" />

Before seeing the collection benchmark tables, it is useful to review
the collection definition and its properties.

<table>
<caption>
Collection Types and Descriptions
</caption>
<thead>
<tr>
<th style="text-align:left;">
Immutability
</th>
<th style="text-align:left;">
Collection
</th>
<th style="text-align:left;">
description
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
List
</td>
<td style="text-align:left;">
A List is a collection that contains immutable data. The Scala List
class holds a sequenced, linear list of items.
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Stream
</td>
<td style="text-align:left;">
The Stream is a lazy list where elements are evaluated only when they
are needed. Streams have the same performance characteristics as lists.
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Vector
</td>
<td style="text-align:left;">
Vectors in Scala are immutable data structures providing random access
for elements and is similar to the list. But, the list has incompetence
of random access of elements.
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Queue
</td>
<td style="text-align:left;">
A Queue is a first-in, first-out (FIFO) data structure. Scala offers
both an immutable queue and a mutable queue. A mutable queue can be
updated or extended in place. It means one can change, add, or remove
elements of a queue as a side effect. Queue is implemented as a pair of
lists. One is used to insert the elements and the second to contain
deleted elements. Elements are added to the first list and removed from
the second list. The two most basic operations of Queue are Enqueue and
Dequeue.
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Stack
</td>
<td style="text-align:left;">
A Stack is a data structure that follows the last-in, first-out(LIFO)
principle. We can add or remove element only from one end called top.
Scala has both mutable and immutable versions of a stack.
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Range
</td>
<td style="text-align:left;">
The Range can be defined as an organized series of uniformly separated
Integers. It is helpful in supplying more strength with fewer methods,
so operations performed here are very quick.
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
String
</td>
<td style="text-align:left;">
A string is a sequence of characters. In Scala, objects of String are
immutable which means they are constant and cannot be changed once
created.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArrayBuffer
</td>
<td style="text-align:left;">
To create a mutable, indexed sequence whose size can change, the
ArrayBuffer class is used. Internally, an ArrayBuffer is an Array of
elements, as well as the store’s current size of the array. When an
element is added to an ArrayBuffer, its size is checked. If the
underlying array isn’t full, then the element is directly added to the
array. If the underlying array is full, then a larger array is
constructed and all the elements are copied to the new array. The key is
that the new array is constructed larger than what is required for the
current addition.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ListBuffer
</td>
<td style="text-align:left;">
The ListBuffer object is convenient when we want to build a list from
front to back. It supports efficient prepend and append operations. The
time taken to convert the ListBuffer into a List is constant.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
StringBuilder
</td>
<td style="text-align:left;">
A String object is immutable. When you need to perform repeated
modifications to a string, we need a StringBuilder class. A
StringBuilder is utilized to append input data to the internal buffer.
Numerous operations like appending data, inserting data, and removing
data are supported in StringBuilder.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
MutableList
</td>
<td style="text-align:left;">
A MutableList consists of a single linked list together with a pointer
that refers to the terminal empty node of that list. This makes list
append a constant time operation because it avoids having to traverse
the list in search for its terminal node.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArraySeq
</td>
<td style="text-align:left;">
Array sequences are mutable sequences of a fixed size that store their
elements internally in an Array\[Object\]. You would typically use an
ArraySeq if you want an array for its performance characteristics, but
you also want to create generic instances of the sequence where you do
not know the type of the elements and you do not have a ClassTag to
provide them at run-time.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArrayStack
</td>
<td style="text-align:left;">
An ArrayStack is a MutableStack that contains a FastList of data.
ArrayStack iterates from top to bottom (LIFO order). The backing data
structure grows and shrinks by 50% at a time, and size is constant.
ArrayStack does not extend Vector, as does the Java Stack, which was one
of the reasons for creating this data structure.
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
Array
</td>
<td style="text-align:left;">
Array is a special mutable kind of collection in Scala. it is a fixed
size data structure that stores elements of the same data type.
</td>
</tr>
</tbody>
</table>

### Benchmark Codes

We created a Scala project with a sbt for assessment data collection.

``` sbt
// build.sbt
scalaVersion := "2.12.3"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.2"
enablePlugins(PackPlugin)
```

To calculate the size of an object, I find
`org.apache.spark.util.SizeEstimator.estimate`  function is useful. This
function estimates the sizes of Java objects (number of bytes of memory
they occupy).

``` scala
import org.apache.spark.sql._
import org.apache.spark.sql.functions.col
import org.apache.spark.util.SizeEstimator.estimate
import scala.collection.AbstractSeq
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
```

To create a result dataframe and write the result, we use Spark (it is
not necessary).

``` scala
val spark = SparkSession
  .builder()
  .appName("Collection_Benchmark")
  .master("local[2]")
  .getOrCreate()

import spark.implicits._
```

We need a time-elapsing function to calculate run time, so use
’System.nanoTime\` to measure time in nano resolution.

``` scala
def timeElapsing(benchmarkFunction: => Unit, message:Boolean = false)(times:Int = 1): Double = {
  if(message) println("Benchmark: IS Starting ...")
  val startTime = System.nanoTime()
  for (_ <- 0 until times)
    benchmarkFunction
  val endTime = System.nanoTime()
  val timeElapsed = (endTime - startTime).toDouble / times.toDouble
  if(message) println(s"Operation Took $timeElapsed ms on average")
  timeElapsed
}
```

Among all the data collections, only some of them have an `insert`
method. We define `insertTime` function only for these collections, as
you see below.

``` scala
def insertTime(x:AbstractSeq[Int], n:Int, m:Int):Double = x match {
  case x:ArrayBuffer[Int] => timeElapsing(x.updated(m,0))(n)
  case x:ListBuffer[Int] => timeElapsing(x.updated(m,0))(n)
  case x:mutable.MutableList[Int] => timeElapsing(x.updated(m,0))(n)
  case x:mutable.Queue[Int] => timeElapsing(x.updated(m,0))(n)
  case x:mutable.ArrayStack[Int] => timeElapsing(x.updated(m,0))(n)
  case _ => -1
}
```

The main parts of the benchmark are `benchmark***` functions, which
contain the time-elapsed of the main methods.

``` scala
def benchmarkSeq(x:AbstractSeq[Int], n:Int, m:Int): Map[String, Double] = {
  Map(
    "volume" -> estimate(x),
    "head" -> timeElapsing(x.head)(n),
    "tail" -> timeElapsing(x.tail)(n),
    "apply" -> timeElapsing(x.apply(m))(n),
    "update" -> timeElapsing(x.updated(m,0))(n),
    "prepend" -> timeElapsing(0+:x)(n),
    "append" -> timeElapsing(x:+0)(n),
    "insert" -> insertTime(x, n, m)
  )
}
```

Similar to `benchmarkSeq` we define `benchmarkString`,
`benchmarkStringBuilder` and `benchmarkArray` functions.

To calculate correct time elapsing related to Array we define
`Array[Object]`

``` scala
def obj = new Object()
def benchmarkArrayBoxed(x:Array[Object], n:Int, m:Int): Map[String, Double] =  { Map(
  "volume" -> estimate(x),
  "head" -> timeElapsing(x.head)(n),
  "tail" -> timeElapsing(x.tail)(n),
  "apply" -> timeElapsing(x.apply(m))(n),
  "update" -> timeElapsing(x.updated(m,0))(n),
  "prepend" -> timeElapsing(obj+:x)(n),
  "append" -> timeElapsing(x:+obj)(n),
  "insert" -> timeElapsing(x.updated(m,0))(n))
}
```

When determining the size of objects, we consider two measurements: size
and method. Objects with a length of
16<sup>0</sup>, 16<sup>1</sup>, ..., 16<sup>5</sup> are generated to
find their size. For checking the performance of methods, objects with a
size of 10000, 200000, ..., 1000000 are generated.

``` scala
val sizes = ( 0 to 5).map(x => math.pow(16,x).toInt) ++ (1 to 10).map(_*100000)
```

As you can see below, each method is run 100 times on objects, and the
results are collected.

``` scala
val stats = for(s <- sizes) yield {
  val integers = 0 until s
  List(
    ("Immutable_List", integers.toList),
    ("Immutable_Stream", integers.toStream),
    ("Immutable_Vector", integers.toVector),
    ("Immutable_Queue", scala.collection.immutable.Queue(integers: _*)),
    ("Immutable_Range", integers),
    ("Immutable_String", "1" * s),
    ("Mutable_ArrayBuffer", scala.collection.mutable.ArrayBuffer(integers: _*)),
    ("Mutable_ListBuffer", scala.collection.mutable.ListBuffer(integers: _*)),
    ("Mutable_StringBuilder", new scala.collection.mutable.StringBuilder("1" * s)),
    ("Mutable_MutableList", scala.collection.mutable.MutableList(integers: _*)),
    ("Mutable_Queue", scala.collection.mutable.Queue(integers: _*)),
    ("Mutable_ArraySeq", scala.collection.mutable.ArraySeq(integers: _*)),
    ("Mutable_ArrayStack", scala.collection.mutable.ArrayStack(integers: _*)),
    ("Mutable_Array", integers.toArray),
    ("Mutable_Boxed_Array", {
      val boxedArray = new Array[Object](s)
      var i = 0
      while (i < s) {
        boxedArray(i) = obj; i += 1
      }
      boxedArray
    })

  ).map {
    case (c, cl: AbstractSeq[Int]) => Map("size" -> s.toString, "collection" -> c) ++ 
    benchmarkSeq(cl, 100, s - 1).map(x => (x._1, x._2.toString))
    case (c, cl: Array[Object]) => Map("size" -> s.toString, "collection" -> c) ++ 
    benchmarkArrayBoxed(cl, 100, s - 1).map(x => (x._1, x._2.toString))
    case (c, cl: Array[Int]) => Map("size" -> s.toString, "collection" -> c) ++ 
    benchmarkArray(cl, 100, s - 1).map(x => (x._1, x._2.toString))
    case (c, cl: String) => Map("size" -> s.toString, "collection" -> c) ++ 
    benchmarkString(cl, 100, s - 1).map(x => (x._1, x._2.toString))
    case (c, cl: StringBuilder) => Map("size" -> s.toString, "collection" -> c) ++ 
    benchmarkStringBuilder(cl, 100, s - 1).map(x => (x._1, x._2.toString))
  }
}
```

The last step is writing the results as a csv file with `spark.write`.

``` scala
val colNames = stats(0).head.toList.sortBy(_._1).map(_._1)
  .zipWithIndex.map(x => col("value")(x._2).as(x._1))

stats.flatten.map(x => x.toList.sortBy(_._1).map(_._2))
  .toDF.select(colNames:_*)
  .coalesce(1).write.option("header","true").mode("overwrite")
  .csv("./collection_seq_size_benchmark.csv")
```

### Object Size in Memory

The benchmark data is now available! The table below displays the
expected size of various collections of different sizes in bytes.

<table>
<caption>
Estimated Size of Scala Collections\[Int\] In Different Size (in bytes)
</caption>
<thead>
<tr>
<th style="text-align:left;">
Immutability
</th>
<th style="text-align:left;">
Collection
</th>
<th style="text-align:right;">
1
</th>
<th style="text-align:right;">
16
</th>
<th style="text-align:right;">
256
</th>
<th style="text-align:right;">
4,096
</th>
<th style="text-align:right;">
65,536
</th>
<th style="text-align:right;">
1,048,576
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
List
</td>
<td style="text-align:right;">
56
</td>
<td style="text-align:right;">
656
</td>
<td style="text-align:right;">
10256
</td>
<td style="text-align:right;">
163856
</td>
<td style="text-align:right;">
2621456
</td>
<td style="text-align:right;">
41943056
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Queue
</td>
<td style="text-align:right;">
80
</td>
<td style="text-align:right;">
680
</td>
<td style="text-align:right;">
10280
</td>
<td style="text-align:right;">
163880
</td>
<td style="text-align:right;">
2621480
</td>
<td style="text-align:right;">
41943080
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Range
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
40
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Stream
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
120
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
String
</td>
<td style="text-align:right;">
48
</td>
<td style="text-align:right;">
72
</td>
<td style="text-align:right;">
552
</td>
<td style="text-align:right;">
8232
</td>
<td style="text-align:right;">
131112
</td>
<td style="text-align:right;">
2097192
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Vector
</td>
<td style="text-align:right;">
216
</td>
<td style="text-align:right;">
456
</td>
<td style="text-align:right;">
5448
</td>
<td style="text-align:right;">
84744
</td>
<td style="text-align:right;">
1353192
</td>
<td style="text-align:right;">
21648072
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
Array
</td>
<td style="text-align:right;">
24
</td>
<td style="text-align:right;">
80
</td>
<td style="text-align:right;">
1040
</td>
<td style="text-align:right;">
16400
</td>
<td style="text-align:right;">
262160
</td>
<td style="text-align:right;">
4194320
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
Array\[Object\]
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
336
</td>
<td style="text-align:right;">
5136
</td>
<td style="text-align:right;">
80400
</td>
<td style="text-align:right;">
1310160
</td>
<td style="text-align:right;">
20970320
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArrayBuffer
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
360
</td>
<td style="text-align:right;">
5160
</td>
<td style="text-align:right;">
80424
</td>
<td style="text-align:right;">
1310184
</td>
<td style="text-align:right;">
20970344
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArraySeq
</td>
<td style="text-align:right;">
64
</td>
<td style="text-align:right;">
360
</td>
<td style="text-align:right;">
5160
</td>
<td style="text-align:right;">
80424
</td>
<td style="text-align:right;">
1310184
</td>
<td style="text-align:right;">
20970344
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArrayStack
</td>
<td style="text-align:right;">
64
</td>
<td style="text-align:right;">
360
</td>
<td style="text-align:right;">
5160
</td>
<td style="text-align:right;">
80424
</td>
<td style="text-align:right;">
1310184
</td>
<td style="text-align:right;">
20970344
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ListBuffer
</td>
<td style="text-align:right;">
88
</td>
<td style="text-align:right;">
688
</td>
<td style="text-align:right;">
10288
</td>
<td style="text-align:right;">
163888
</td>
<td style="text-align:right;">
2621488
</td>
<td style="text-align:right;">
41943088
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
MutableList
</td>
<td style="text-align:right;">
88
</td>
<td style="text-align:right;">
688
</td>
<td style="text-align:right;">
10288
</td>
<td style="text-align:right;">
163888
</td>
<td style="text-align:right;">
2621488
</td>
<td style="text-align:right;">
41943088
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
Queue
</td>
<td style="text-align:right;">
88
</td>
<td style="text-align:right;">
688
</td>
<td style="text-align:right;">
10288
</td>
<td style="text-align:right;">
163888
</td>
<td style="text-align:right;">
2621488
</td>
<td style="text-align:right;">
41943088
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
StringBuilder
</td>
<td style="text-align:right;">
96
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
600
</td>
<td style="text-align:right;">
8280
</td>
<td style="text-align:right;">
131160
</td>
<td style="text-align:right;">
2097240
</td>
</tr>
</tbody>
</table>

The average memory size of each object is calculated and shown below.

![](images/unnamed-chunk-19-1.png)<!-- -->

### Methods Performance

Before seeing the benchmark result, it is better to have an overview of
the methods that are applied to objects. The below table has more
details.

<table>
<caption>
Operations That Are Tested on Sequence Types
</caption>
<thead>
<tr>
<th style="text-align:left;">
operations
</th>
<th style="text-align:left;">
description
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
head
</td>
<td style="text-align:left;">
Selecting the first element of the sequence.
</td>
</tr>
<tr>
<td style="text-align:left;">
tail
</td>
<td style="text-align:left;">
Producing a new sequence that consists of all elements except the first
one.
</td>
</tr>
<tr>
<td style="text-align:left;">
apply
</td>
<td style="text-align:left;">
Indexing.
</td>
</tr>
<tr>
<td style="text-align:left;">
update
</td>
<td style="text-align:left;">
Functional update (with updated) for immutable sequences, side-effecting
update (with update for mutable sequences).
</td>
</tr>
<tr>
<td style="text-align:left;">
prepend
</td>
<td style="text-align:left;">
Adding an element to the front of the sequence. For immutable sequences,
this produces a new sequence. For mutable sequences it modifies the
existing sequence.
</td>
</tr>
<tr>
<td style="text-align:left;">
append
</td>
<td style="text-align:left;">
Adding an element and the end of the sequence. For immutable sequences,
this produces a new sequence. For mutable sequences it modifies the
existing sequence.
</td>
</tr>
<tr>
<td style="text-align:left;">
insert
</td>
<td style="text-align:left;">
Inserting an element at an arbitrary position in the sequence. This is
only supported directly for mutable sequences.
</td>
</tr>
</tbody>
</table>

We can find performance characteristics of Scala collections in the
[Scala
documents](https://docs.scala-lang.org/overviews/collections/performance-characteristics.html).
The Scala performance table is provided below for comparison with the
empirical results.

<table>
<caption>
Performance characteristics of sequence types
</caption>
<thead>
<tr>
<th style="text-align:left;">
Immutability
</th>
<th style="text-align:left;">
Collection
</th>
<th style="text-align:left;">
head
</th>
<th style="text-align:left;">
tail
</th>
<th style="text-align:left;">
apply
</th>
<th style="text-align:left;">
update
</th>
<th style="text-align:left;">
prepend
</th>
<th style="text-align:left;">
append
</th>
<th style="text-align:left;">
insert
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
List
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
NA
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Stream
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
NA
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Vector
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
NA
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Stack
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Queue
</td>
<td style="text-align:left;">
aC
</td>
<td style="text-align:left;">
aC
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
NA
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
Range
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
NA
</td>
<td style="text-align:left;">
NA
</td>
<td style="text-align:left;">
NA
</td>
<td style="text-align:left;">
NA
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
String
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
NA
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArrayBuffer
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
aC
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ListBuffer
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
StringBuilder
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
aC
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
MutableList
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
Queue
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
ArraySeq
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">

-   </td>
    <td style="text-align:left;">

    -   </td>
        <td style="text-align:left;">

        -   </td>
            </tr>
            <tr>
            <td style="text-align:left;">
            Mutable
            </td>
            <td style="text-align:left;">
            Stack
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            L
            </td>
            </tr>
            <tr>
            <td style="text-align:left;">
            Mutable
            </td>
            <td style="text-align:left;">
            ArrayStack
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            aC
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            L
            </td>
            </tr>
            <tr>
            <td style="text-align:left;">
            Mutable
            </td>
            <td style="text-align:left;">
            Array
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            L
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">
            C
            </td>
            <td style="text-align:left;">

            -   </td>
                <td style="text-align:left;">

                -   </td>
                    <td style="text-align:left;">

                    -   </td>
                        </tr>
                        </tbody>
                        </table>

<table>
<caption>
Performance characteristics of sequence types
</caption>
<thead>
<tr>
<th style="text-align:left;">
performance
</th>
<th style="text-align:left;">
description
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
The operation takes (fast) constant time.
</td>
</tr>
<tr>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
The operation takes effectively constant time, but this might depend on
some assumptions such as maximum length of a vector or distribution of
hash keys.
</td>
</tr>
<tr>
<td style="text-align:left;">
aC
</td>
<td style="text-align:left;">
The operation takes amortized constant time. Some invocations of the
operation might take longer, but if many operations are performed on
average only constant time per operation is taken.
</td>
</tr>
<tr>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
The operation takes time proportional to the logarithm of the collection
size.
</td>
</tr>
<tr>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
The operation is linear, that is it takes time proportional to the
collection size.
</td>
</tr>
<tr>
<td style="text-align:left;">

-   </td>
    <td style="text-align:left;">
    The operation is not supported.
    </td>
    </tr>
    </tbody>
    </table>

The performance results of each method and collection are plotted as a
scatter plot. We add a regression line to the plot to see the growth
rate. The plot below shows the performance of the immutable collection.

![Immutable Collection Methods
Performance](images/unnamed-chunk-24-1.svg)

A similar plot is plotted for mutable collection.

![Mutable Collection Methods Performance](images/unnamed-chunk-25-1.svg)

In this post, we try to understand the size and performance of the
`Sets` and `Maps` data collection. In the first, we review the structure
of the Mutable and Immutabla `Sets` and `Maps`collections.

<img src="images/map_set.svg" title="Immutable Maps and Sets Data Collection" alt="Immutable Maps and Sets Data Collection" style="display: block; margin: auto;" />

<img src="images/maps.svg" title="Mutable Maps Data Collection" alt="Mutable Maps Data Collection" style="display: block; margin: auto;" />

<img src="images/sets.svg" title="Mutable Sets Data Collection" alt="Mutable Sets Data Collection" style="display: block; margin: auto;" />
A quick review of each collection application may be found in the table
below.

<table>
<caption>
Maps and Sets Data Collections
</caption>
<thead>
<tr>
<th style="text-align:left;">
Collection
</th>
<th style="text-align:left;">
description
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
HashSet
</td>
<td style="text-align:left;">
A concrete implementation of Set semantics is HashSet. The element’s
hashCode will be used as a key in the HashSet, allowing for a quick
lookup of the element’s value. HashSet has immutable and mutable type
</td>
</tr>
<tr>
<td style="text-align:left;">
HashMap
</td>
<td style="text-align:left;">
The Scala Collection includes mutable and immutable HashMap. It’s used
to save and return a map of elements. A HashMap is a Hash Table data
structure that stores a collection of key and value pairs. It implements
Map in its most basic form.
</td>
</tr>
<tr>
<td style="text-align:left;">
TreeSet
</td>
<td style="text-align:left;">
A set is a data structure that allows us to store distinct components.
The Set does not provide element ordering, but a TreeSet will create
items in a specific order. TreeSet includes two types in Scala:
scala.collection.mutable.TreeSet and scala.collection.immutable.TreeSet.
</td>
</tr>
<tr>
<td style="text-align:left;">
TreeMap
</td>
<td style="text-align:left;">
TreeMap is useful when performing range queries or traversing in order,
whereas the map does not keep order. If you only need key lookups and
don’t care in which order key-values are traversed, Map will suffice,
which will generally have better performance.
</td>
</tr>
<tr>
<td style="text-align:left;">
BitSet
</td>
<td style="text-align:left;">
Bitsets are collections of non-negative integers that are expressed as
64-bit words with variable-size arrays of bits. The greatest number
stored in a bitset determines its memory footprint. There are two
versions of BitSet in Scala: scala.collection.immutable.BitSet and
scala.collection.mutable.BitSet.
</td>
</tr>
<tr>
<td style="text-align:left;">
ListMap
</td>
<td style="text-align:left;">
A ListMap is a collection of key and value pairs where the keys are
backed by a List data structure. ListMap collections are used only for a
small number of elements.
</td>
</tr>
<tr>
<td style="text-align:left;">
WeakHashMap
</td>
<td style="text-align:left;">
A weak hash map is a special kind of hash map where the garbage
collector does not follow links from the map to the keys stored in it.
This means that a key and its associated value will disappear from the
map if there is no other reference to that key. Weak hash maps are
useful for tasks such as caching, where you want to re-use an expensive
function’s result if the function is called again on the same key.
</td>
</tr>
</tbody>
</table>

### Benchmark Codes

The benchmark codes in this section are more similar to the `Seq`
Collection benchmark codes from a previous post. Only the benchmark
functions for `Sets` and `Maps` are different. The `Map` benchmark code
can be found here.

``` scala
  def benchmarkMap(x:scala.collection.Map[Int,Int], n:Int, m:Int): Map[String, Double] = {
    Map(
      "volume" -> estimate(x),
      "lookup" -> timeElapsing(x.get(m))(n),
      "add" -> timeElapsing(x ++ Map((m,m)))(n),
      "remove" -> timeElapsing(x-0)(n),
      "min" -> timeElapsing(x.minBy(_._2)._1)(n)
    )
  }
```

Similar to `Map`, we define a benchmark function for `Set`.

``` scala
  def benchmarkSet(x:scala.collection.Set[Int], n:Int, m:Int): Map[String, Double] = {
    Map(
      "volume" -> estimate(x),
      "lookup" -> timeElapsing(x.contains(m))(n),
      "add" -> timeElapsing(x ++ Map((m,m)))(n),
      "remove" -> timeElapsing(x-0)(n),
      "min" -> timeElapsing(x.min)(n)
    )
  }
```

In the below code, the definition of each collection can be found.

``` scala
  val stats: Seq[List[Map[String, String]]] = for(s <- sizes) yield {
    val integers = 0 until s
    List(
      ("Immutable_HashMap", integers.zipWithIndex.toMap),
      ("Immutable_TreeMap", scala.collection.immutable.TreeMap(integers.zipWithIndex:_*)),
      ("Immutable_ListMap",scala.collection.immutable.ListMap(integers.zipWithIndex:_*)),
      ("Mutable_HashMap", scala.collection.mutable.HashMap(integers.zipWithIndex:_*)),
      ("Mutable_WeakHashMap",scala.collection.mutable.WeakHashMap(integers.zipWithIndex:_*))
    ).map(x => {
      Map("size" -> s.toString, "collection" -> x._1) ++ 
        benchmarkMap(x._2, 100, s).map(x => (x._1, x._2.toString))
    }) ++  List(
      ("Immutable_HashSet", integers.toSet),
      ("Immutable_TreeSet", scala.collection.immutable.TreeSet(integers:_*)),
      ("Immutable_BitSet", scala.collection.immutable.BitSet(integers:_*)),
      ("Mutable_HashSet", scala.collection.mutable.HashSet(integers:_*)),
      ("Mutable_BitSet", scala.collection.mutable.BitSet(integers:_*)),
      ("Mutable_TreeSet", scala.collection.mutable.TreeSet(integers:_*))
    ).map(x => {
      Map("size" -> s.toString, "collection" -> x._1) ++ 
        benchmarkSet(x._2, 100, s).map(x => (x._1, x._2.toString))
    })

  }
```

### Object Size in Memory

The benchmark information is now ready. The table below shows the
estimated size in bytes of various collections of varied sizes.

<table>
<caption>
Estimated Size of Scala Collections\[Int\] In Different Size (in bytes)
</caption>
<thead>
<tr>
<th style="text-align:left;">
Immutability
</th>
<th style="text-align:left;">
Collection
</th>
<th style="text-align:right;">
1
</th>
<th style="text-align:right;">
16
</th>
<th style="text-align:right;">
256
</th>
<th style="text-align:right;">
4,096
</th>
<th style="text-align:right;">
65,536
</th>
<th style="text-align:right;">
1,048,576
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
HashMap
</td>
<td style="text-align:right;">
40
</td>
<td style="text-align:right;">
1136
</td>
<td style="text-align:right;">
24304
</td>
<td style="text-align:right;">
430384
</td>
<td style="text-align:right;">
7050528
</td>
<td style="text-align:right;">
111206912
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
HashSet
</td>
<td style="text-align:right;">
32
</td>
<td style="text-align:right;">
744
</td>
<td style="text-align:right;">
14184
</td>
<td style="text-align:right;">
235944
</td>
<td style="text-align:right;">
3906968
</td>
<td style="text-align:right;">
60877432
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
ListMap
</td>
<td style="text-align:right;">
56
</td>
<td style="text-align:right;">
656
</td>
<td style="text-align:right;">
12304
</td>
<td style="text-align:right;">
227344
</td>
<td style="text-align:right;">
3667984
</td>
<td style="text-align:right;">
58718224
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
TreeMap
</td>
<td style="text-align:right;">
88
</td>
<td style="text-align:right;">
808
</td>
<td style="text-align:right;">
14376
</td>
<td style="text-align:right;">
260136
</td>
<td style="text-align:right;">
4192296
</td>
<td style="text-align:right;">
67106856
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
TreeSet
</td>
<td style="text-align:right;">
104
</td>
<td style="text-align:right;">
824
</td>
<td style="text-align:right;">
12344
</td>
<td style="text-align:right;">
196664
</td>
<td style="text-align:right;">
3145784
</td>
<td style="text-align:right;">
50331704
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
HashMap
</td>
<td style="text-align:right;">
160
</td>
<td style="text-align:right;">
824
</td>
<td style="text-align:right;">
14328
</td>
<td style="text-align:right;">
268848
</td>
<td style="text-align:right;">
4412704
</td>
<td style="text-align:right;">
63585176
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
HashSet
</td>
<td style="text-align:right;">
200
</td>
<td style="text-align:right;">
568
</td>
<td style="text-align:right;">
7848
</td>
<td style="text-align:right;">
112568
</td>
<td style="text-align:right;">
2097144
</td>
<td style="text-align:right;">
32883416
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
TreeSet
</td>
<td style="text-align:right;">
120
</td>
<td style="text-align:right;">
960
</td>
<td style="text-align:right;">
14400
</td>
<td style="text-align:right;">
229440
</td>
<td style="text-align:right;">
3670080
</td>
<td style="text-align:right;">
58720320
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
WeakHashMap
</td>
<td style="text-align:right;">
344
</td>
<td style="text-align:right;">
1248
</td>
<td style="text-align:right;">
17856
</td>
<td style="text-align:right;">
259784
</td>
<td style="text-align:right;">
98542768
</td>
<td style="text-align:right;">
67101968
</td>
</tr>
</tbody>
</table>

The average memory size of each object is calculated and shown below.

![](images/unnamed-chunk-37-1.png)<!-- -->

### Methods Performance

The table below contains a list of the data collection methods used.

<table>
<caption>
Operations That Are Tested on Maps ans Sets
</caption>
<thead>
<tr>
<th style="text-align:left;">
operations
</th>
<th style="text-align:left;">
description
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
lookup
</td>
<td style="text-align:left;">
Testing whether an element is contained in set, or selecting a value
associated with a key.
</td>
</tr>
<tr>
<td style="text-align:left;">
add
</td>
<td style="text-align:left;">
Adding a new element to a set or key/value pair to a map.
</td>
</tr>
<tr>
<td style="text-align:left;">
remove
</td>
<td style="text-align:left;">
Removing an element from a set or a key from a map.
</td>
</tr>
<tr>
<td style="text-align:left;">
min
</td>
<td style="text-align:left;">
The smallest element of the set, or the smallest key of a map.
</td>
</tr>
</tbody>
</table>

Scala collection performance characteristics can be found in the [Scala
documents](https://docs.scala-lang.org/overviews/collections/performance-characteristics.html).
For comparison with the empirical results, the Scala performance table
is given below.

<table>
<caption>
Performance Characteristics of Immutable Maps and Sets
</caption>
<thead>
<tr>
<th style="text-align:left;">
Immutability
</th>
<th style="text-align:left;">
Collection
</th>
<th style="text-align:left;">
lookup
</th>
<th style="text-align:left;">
add
</th>
<th style="text-align:left;">
remove
</th>
<th style="text-align:left;">
min
</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
HashSet/HashMap
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
TreeSet/TreeMap
</td>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
Log
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
BitSet
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
eC1
</td>
</tr>
<tr>
<td style="text-align:left;">
Immutable
</td>
<td style="text-align:left;">
ListMap
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
HashSet/HashMap
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
WeakHashMap
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
eC
</td>
<td style="text-align:left;">
L
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
BitSet
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
aC
</td>
<td style="text-align:left;">
C
</td>
<td style="text-align:left;">
eC
</td>
</tr>
<tr>
<td style="text-align:left;">
Mutable
</td>
<td style="text-align:left;">
TreeSet
</td>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
Log
</td>
<td style="text-align:left;">
Log
</td>
</tr>
</tbody>
</table>

The performance results of each method and collection are shown using a
scatter plot. We add a regression line to the plot to see the growth
rate.

![Immutable Collection Methods
Performance](images/unnamed-chunk-41-1.svg)

A similar plot is plotted for mutable collection.

![Mutable Collection Methods Performance](images/unnamed-chunk-42-1.svg)

# Refrences

-   [Performance
    Characteristics](https://docs.scala-lang.org/overviews/collections/performance-characteristics.html)
-   [Benchmarking Scala
    Collections](https://www.lihaoyi.com/post/BenchmarkingScalaCollections.html)
