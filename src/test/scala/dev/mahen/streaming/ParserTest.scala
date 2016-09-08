package dev.mahen.streaming

import org.scalatest.FunSuite
import com.holdenkarau.spark.testing.SharedSparkContext
import java.util.regex.Pattern


class ParserTest extends FunSuite with SharedSparkContext {

  ignore("=============================================") {
    val gl = new GeoIPLookup().getLocationInformation("35.145.178.43")
    println(gl)
    println(gl._1)
    println(gl._2)
    println(gl._3)
    println(gl._4)
    println(gl._5)

  }

  ignore("=========== Test parseLogs ==========") {

    val lines = new ParseLogs("logs/NASA_access_log_Jul95")
  }

  test("== parser==") {

    val record = """35.145.178.43	-	-	[25/Jul/2016:09:10:04	-0800]	"GET	/department/outdoors/products	HTTP/1.1"	200	749	"-"	"Mozilla/5.0	(Windows	NT	6.1;	WOW64)	AppleWebKit/537.36	(KHTML,	like	Gecko)	Chrome/36.0.1985.125	Safari/537.36"
"""
    val regex = """(\d+.\d+.\d+.\d+)\t+([-])\t([-])\t+(\[.+?\])\t+(\".+?\")\t+(\d+)\t+(\d+)\t+(\".?\")\t+(\".+?\")"""
    val p = Pattern.compile(regex)

    println(record.split("\t").apply(0))
    val matcher = p.matcher(record)
    if (matcher.find()) {

      println("Found : " + matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), matcher.group(7), matcher.group(8), matcher.group(9))
    }

    val dateP1 = """(\d+)-(\d+)-(\d+)""".r
    val dateP1(year, month, day) = "2011-07-15"

    println(dateP1 findFirstIn ("2011-07-15"))

    val patt = """(.+?)\t(.*?)""".r
    val patt(ip1, ip2) = "35	1213"

    println(patt findFirstIn ("35	145"))
  }
  
}