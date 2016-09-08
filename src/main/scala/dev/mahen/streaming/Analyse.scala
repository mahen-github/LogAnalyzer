package dev.mahen.streaming

import java.util.regex.Pattern

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

/**
 * parse a l
import dev.mahen.streaming.GeoIPLookupines of Log
 */
case class Line(
  ip: String,
  client: String,
  user: String,
  date: String,
  request: String,
  status_code: String,
  bytes: String,
  referrer: String,
  ua: String,
  countryName: String,
  city: String,
  area_code: Int,
  countryCode: String,
  region: String){
  
   override def toString() = ip +","+client+","+user+","+ date+","+request+","+status_code + ","+ bytes+","+referrer+","+ua+
   ","+countryName+","+city+","+area_code+","+ countryCode+","+region    
  
}
object Analyse {
  
   private var files = ""
  def main(args: Array[String]) {

    val directory = args(0)

    files = args(1)

    val sparkContext = new SparkContext(new SparkConf().setAppName("WebLogsStreaming"))
    val sparkStreamingContext = new StreamingContext(sparkContext, Seconds(25))
    //Create File Stream 
    val fileStream = sparkStreamingContext.textFileStream(directory)

    //DStream[String] => Line
    val data = parseLine(fileStream)
    data.foreach(println(_))

    //Actions on DStream
    data.map {x => x.toString()}.saveAsTextFiles(args(1))
    sparkStreamingContext.start() // Start the computation
    sparkStreamingContext.awaitTermination() // Wait for the computation to terminate

  }

  def parseLine(fileStream: DStream[String]): DStream[Line] = {

    //Regex Pattern to parse the Logs
    val regex = """(\d+.\d+.\d+.\d+)\t+([-])\t([-])\t+(\[.+?\])\t+(\".+?\")\t+(\d+)\t+(\d+)\t+(\".?\")\t+(\".+?\")"""
    val p = Pattern.compile(regex)

    //DStream
    val dataV = fileStream.map { line =>
      val matcher = p.matcher(line)
      if (matcher.find) {
        val glVal = GeoIPLookup().getLocationInformation(matcher.group(1))
        
        Line(matcher.group(1), 
            matcher.group(2), 
            matcher.group(3), 
            matcher.group(4), 
            matcher.group(5), 
            matcher.group(6), 
            matcher.group(7), 
            matcher.group(8), 
            matcher.group(9), 
            glVal._1,
            glVal._2,
            glVal._3, 
            glVal._4, 
            glVal._5)
      } else Line("", "", "", "", "", "", "", "", "", "", "", 0, "", "")
    }
    dataV
  }
}