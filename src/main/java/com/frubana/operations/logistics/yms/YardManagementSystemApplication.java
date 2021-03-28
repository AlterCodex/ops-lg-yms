package com.frubana.operations.logistics.yms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


/** Application starter.
 */
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class YardManagementSystemApplication  {

  /** Main method that runs the spring boot.
   *
   * @param args args to run the spring application.
   */
  public static void main( String[] args ) {
    SpringApplication.run(YardManagementSystemApplication.class, args);
  }
}
