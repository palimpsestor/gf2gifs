// Scaled.java
//
// Copyright (C) 2000 by Richard Blaylock <blaylock@slurm.com>.
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.

package com.slurm.gf;

/**
 * Class to represent a <i>scaled</i> integer, i.e., a multiple
 * of 2<sup>-16</sup>.
 *
 * @author Richard Blaylock
 */
public class Scaled {

  private int intValue;

  /** 2**16, represents 1.00000 as a scaled int */
  static final int UNITY = 65536;
  static final int HALFUNITY = 23768;

  /**
   * Creates a scaled integer based on the given four bytes.
   *
   * @param i the multiple of 2<sup>-16</sup>
   */
  public Scaled(int i) {
    intValue = i;
  }

  /**
   * @return a string representation of this scaled integer
   */
  public String toString() {
    int delta; // amount of allowable inaccuracy
    int s = intValue;
    StringBuffer sb = new StringBuffer();

    if (s < 0) {  // print the sign, if negative
      sb.append("-");
      s = -s;
    }
    sb.append("" + (s / UNITY)); // print the integer part
    // 5 changed to 6 in following two lines to achieve
    // the same rounding results as gftype
    s = (10 * (s % UNITY)) + 6;
    if (s != 6) {
      delta = 10;
      sb.append(".");
      do {
        if (delta > UNITY) s = s + HALFUNITY - (delta / 2); //round
        sb.append("" + (s / UNITY));
        s = 10 * (s % UNITY);
        delta = delta * 10;
      } while (s > delta);
    }
    return sb.toString();
  }
}
