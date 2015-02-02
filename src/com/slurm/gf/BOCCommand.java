// BOCCommand.java
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
 * Encapsulates a boc command and its parameters.
 */
public class BOCCommand extends Command {

  private int characterCode;
  private int previousCharacterPointer;
  private int minM;
  private int maxM;
  private int minN;
  private int maxN;

  public BOCCommand(int l, int o, int c, int p,
                    int minM, int maxM, int minN, int maxN) {
    super(l, o);
    characterCode = c;
    previousCharacterPointer = p;
    this.minM = minM;
    this.maxM = maxM;
    this.minN = minN;
    this.maxN = maxN;
  }

  public int getCharacterCode() {
    return characterCode;
  }

  public int getPreviousCharacterPointer() {
    return previousCharacterPointer;
  }

  public int getMinM() {
    return minM;
  }

  public int getMaxM() {
    return maxM;
  }

  public int getMinN() {
    return minN;
  }

  public int getMaxN() {
    return maxN;
  }

}
