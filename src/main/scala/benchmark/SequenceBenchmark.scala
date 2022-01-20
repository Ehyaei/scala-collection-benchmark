package benchmark

import org.apache.spark.sql._
import org.apache.spark.sql.functions.col
import org.apache.spark.util.SizeEstimator.estimate
import scala.collection.AbstractSeq
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import Utils.timeElapsing


object SequenceBenchmark extends App {

  val spark = SparkSession.builder()
    .appName("Collection_Benchmark")
    .master("local[2]")
    .getOrCreate()

  import spark.implicits._



  def insertTime(x:AbstractSeq[Int], n:Int, m:Int):Double = x match {
    case x:ArrayBuffer[Int] => timeElapsing(x.updated(m,0))(n)
    case x:ListBuffer[Int] => timeElapsing(x.updated(m,0))(n)
    case x:mutable.MutableList[Int] => timeElapsing(x.updated(m,0))(n)
    case x:mutable.Queue[Int] => timeElapsing(x.updated(m,0))(n)
    case x:mutable.ArrayStack[Int] => timeElapsing(x.updated(m,0))(n)
    case _ => -1
  }

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

  def benchmarkString(x:String, n:Int, m:Int): Map[String, Double] = {
    Map(
      "volume" -> estimate(x),
      "head" -> timeElapsing(x.head)(n),
      "tail" -> timeElapsing(x.tail)(n),
      "apply" -> timeElapsing(x.apply(m))(n),
      "update" -> timeElapsing(x.updated(m,0))(n),
      "prepend" -> timeElapsing("0"+x)(n),
      "append" -> timeElapsing(x+"0")(n),
      "insert" -> -1
    )
  }

  def benchmarkStringBuilder(x:StringBuilder, n:Int, m:Int): Map[String, Double] = {
    Map(
      "volume" -> estimate(x),
      "head" -> timeElapsing(x.head)(n),
      "tail" -> timeElapsing(x.tail)(n),
      "apply" -> timeElapsing(x.apply(m))(n),
      "update" -> timeElapsing(x.updated(m,0))(n),
      "prepend" -> timeElapsing("0"+x)(n),
      "append" -> timeElapsing(x+"0")(n),
      "insert" -> timeElapsing(x.updated(m,0))(n)
    )
  }

  def benchmarkArray(x:Array[Int], n:Int, m:Int): Map[String, Double] =  { Map(
      "volume" -> estimate(x),
      "head" -> timeElapsing(x.head)(n),
      "tail" -> timeElapsing(x.tail)(n),
      "apply" -> timeElapsing(x.apply(m))(n),
      "update" -> timeElapsing(x.updated(m,0))(n),
      "prepend" -> timeElapsing(0+:x)(n),
      "append" -> timeElapsing(x:+0)(n),
      "insert" -> timeElapsing(x.updated(m,0))(n))
}

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

  val sizes = ( 0 to 5).map(x => math.pow(16,x).toInt) ++ (1 to 10).map(_*100000)

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

  val colNames = stats(0).head.toList.sortBy(_._1).map(_._1)
    .zipWithIndex.map(x => col("value")(x._2).as(x._1))

  stats.flatten.map(x => x.toList.sortBy(_._1).map(_._2))
    .toDF.select(colNames:_*)
    .coalesce(1).write.option("header","true").mode("overwrite")
    .csv("./collection_size_benchmark.csv")

  spark.stop()
}
