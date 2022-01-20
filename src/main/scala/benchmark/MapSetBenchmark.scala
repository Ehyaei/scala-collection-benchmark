package benchmark

import org.apache.spark.sql._
import org.apache.spark.sql.functions.col
import org.apache.spark.util.SizeEstimator.estimate
import Utils.timeElapsing
import scala.collection.immutable


object MapSetBenchmark extends App{

  val spark: SparkSession = SparkSession.builder()
    .appName("Collection_Benchmark")
    .master("local[2]")
    .getOrCreate()

  def benchmarkMap(x:scala.collection.Map[Int,Int], n:Int, m:Int): Map[String, Double] = {
    Map(
      "volume" -> estimate(x),
      "lookup" -> timeElapsing(x.get(m))(n),
      "add" -> timeElapsing(x ++ Map((m,m)))(n),
      "remove" -> timeElapsing(x-0)(n),
      "min" -> timeElapsing(x.minBy(_._2)._1)(n)
    )
  }

  def benchmarkSet(x:scala.collection.Set[Int], n:Int, m:Int): Map[String, Double] = {
    Map(
      "volume" -> estimate(x),
      "lookup" -> timeElapsing(x.contains(m))(n),
      "add" -> timeElapsing(x ++ Map((m,m)))(n),
      "remove" -> timeElapsing(x-0)(n),
      "min" -> timeElapsing(x.min)(n)
    )
  }

  import spark.implicits._

  val sizes: Seq[Int] = ( 0 to 5).map(x => math.pow(16  ,x).toInt) ++ (1 to 10).map(_*100000)

  val stats: Seq[List[Map[String, String]]] = for(s <- sizes) yield {
    val integers = 0 until s
    List(
      ("Immutable_HashMap", integers.zipWithIndex.toMap),
      ("Immutable_TreeMap", scala.collection.immutable.TreeMap(integers.zipWithIndex:_*)),
      ("Immutable_ListMap",scala.collection.immutable.ListMap(integers.zipWithIndex:_*)),
      ("Mutable_HashMap", scala.collection.mutable.HashMap(integers.zipWithIndex:_*)),
      ("Mutable_WeakHashMap",scala.collection.mutable.WeakHashMap(integers.zipWithIndex:_*))
    ).map(x => {
      Map("size" -> s.toString, "collection" -> x._1) ++ benchmarkMap(x._2, 100, s).map(x => (x._1, x._2.toString))
    }) ++  List(
      ("Immutable_HashSet", integers.toSet),
      ("Immutable_TreeSet", scala.collection.immutable.TreeSet(integers:_*)),
      ("Immutable_BitSet", scala.collection.immutable.BitSet(integers:_*)),
      ("Mutable_HashSet", scala.collection.mutable.HashSet(integers:_*)),
      ("Mutable_BitSet", scala.collection.mutable.BitSet(integers:_*)),
      ("Mutable_TreeSet", scala.collection.mutable.TreeSet(integers:_*))
    ).map(x => {
      Map("size" -> s.toString, "collection" -> x._1) ++ benchmarkSet(x._2, 100, s).map(x => (x._1, x._2.toString))
    })

  }

  val colNames: immutable.Seq[Column] = stats.head.head.toList.sortBy(_._1).map(_._1)
    .zipWithIndex.map(x => col("value")(x._2).as(x._1))

  stats.flatten.map(x => x.toList.sortBy(_._1).map(_._2))
    .toDF.select(colNames:_*)
    .coalesce(1).write.option("header","true").mode("overwrite")
    .csv("./collection_Set_Map_size_benchmark.csv")

  spark.stop()

}
