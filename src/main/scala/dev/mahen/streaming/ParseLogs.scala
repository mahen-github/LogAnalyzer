package dev.mahen.streaming

/**
 * Class Not used
 * parses the Logs,
 * Sessionize,
 * Identifies 
 * 	UserAgents
 * 	Bots
 * 	
 */

class ParseLogs(val line: String) {

  def parse() {

    val logFileStream = this.getClass.getClassLoader().getResourceAsStream("logs/sample_iis.log");

    val lines = scala.io.Source.fromInputStream(logFileStream).getLines()
    
 

    while (lines.hasNext) {
      println(lines.next())
    }

  }

}