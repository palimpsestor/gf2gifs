// PostCommand.java
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
 * Encapsulates a post command and its parameters.
 */
public class PostCommand extends Command {

  int p;
  int ds;
  int cs;
  int hppp;
  int vppp;
  int min_m;
  int max_m;
  int min_n;
  int max_n;

  public PostCommand(int l, int o, int p, int ds, int cs, int hppp, int vppp,
                     int min_m, int max_m, int min_n, int max_n) {
    super(l, o);
    this.p = p;
    this.ds = ds;
    this.cs = cs;
    this.hppp = hppp;
    this.vppp = vppp;
    this.min_m = min_m;
    this.max_m = max_m;
    this.min_n = min_n;
    this.max_n = max_n;
  }

  public int getPostSpecialPointer() {
    return p;
  }

  public int getDesignSize() {
    return ds;
  }

  public int getCheckSum() {
    return cs;
  }

  public int getHPPP() {
    return hppp;
  }

  public int getVPPP() {
    return vppp;
  }

  public int getMinM() {
    return min_m;
  }

  public int getMaxM() {
    return max_m;
  }

  public int getMinN() {
    return min_n;
  }

  public int getMaxN() {
    return max_n;
  }

}
