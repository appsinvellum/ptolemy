package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestRounding extends GroovyTestCase {

  BigDecimal fract = 0.0019

      
  void testRounding() {
    assert PtolemyProjector.round(fract) == 0.002
    assert PtolemyProjector.round(fract * -1) == -0.002
    
    assert PtolemyProjector.round(fract + 1) == 1.002
    assert PtolemyProjector.round((fract + 1) * -1) == -1.002


    assert PtolemyProjector.round(fract + 10) == 10.002
    assert PtolemyProjector.round((fract + 10) * -1) == -10.002

    
    assert PtolemyProjector.round(fract + 100) == 100.002
    assert PtolemyProjector.round((fract + 100) * -1) == -100.002
    
    assert shouldFail {
      PtolemyProjector.round(fract + 1000)
    }
    assert shouldFail {
      PtolemyProjector.round((fract + 1000) * -1)
    }
  }



}
