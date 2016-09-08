package dev.mahen.streaming

/**
 * @author mqp29
 */

import com.maxmind.geoip.LookupService
import java.io.File
import org.apache.commons.validator.routines.InetAddressValidator

/**
 * Augments the ip address with location
 */
class GeoIPLookup {

  /**
   * @return Tuple(countryName,city,areaCode,countryCode,region)
   */
  def getLocationInformation(ipAddress:String): (String, String, Int, String, String) = {

    val GeoIpFile = this.getClass.getClassLoader.getResource("GeoLiteCity.dat");

    val geoIpF = Thread.currentThread().getContextClassLoader().getResource("GeoLiteCity.dat")

    //    println("GeoIpFile.getPath : " + IOUtils.toString(GeoIpFile))

    //    val cl = new LookupService(GeoIpFile.getPath, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE)

    //    val cl = new LookupService(GeoIpFile.getFile, LookupService.GEOIP_MEMORY_CACHE)

    val cl = new LookupService(new File("/tmp/GeoLiteCity.dat"), LookupService.GEOIP_MEMORY_CACHE)

    println("IP Input : " + ipAddress)
    
    val notIn = List("102.242.18.229") //not sure anout this IP. Somthing  fishy

    //validate the ip
    if (InetAddressValidator.getInstance.isValid(ipAddress) && !notIn.contains(ipAddress)) {

      //get the location    
      val location = cl.getLocation(ipAddress)
      cl.close()
      
      //return a tuple
      (location.countryName, location.city, location.area_code, location.countryCode, location.region)

    } else {
      (null, null, 0, null, null)

    }

  }
  
}

object GeoIPLookup {
  
  def apply() = new GeoIPLookup
}


  
 
