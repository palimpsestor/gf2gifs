// SimpleGFParserCallback.java
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
 * Simple concrete implementation of GFParserCallback that can be used
 * for testing a GFParser.
 *
 * @author Richard Blaylock
 * @see GFParserCallback
 * @see GFParser
 */
public class SimpleGFParserCallback extends GFParserCallback {

  public void badGF(String msg) throws GFFileFormatException {
    System.out.println(" Bad GF file: " + msg + "!");
    throw new GFFileFormatException();
  }
  
  public void error(int label, String s) {
    System.out.println(label + ":" + s);
  }

  public void startSpecialCommands(int loc) {
  }

  public void handlePaintCommand(PaintCommand c) {
    System.out.println("paint: " + c.getOpCode());
  }

  public void handleBOCCommand(BOCCommand c) {
    System.out.println("===== NEW CHAR: " + c.getCharacterCode() + " =====");
    System.out.println("boc: " + c.getOpCode());
  }

  public void handleEOCCommand(EOCCommand c) {
    System.out.println("eoc: " + c.getOpCode());
  }

  public void handleSkipCommand(SkipCommand c) {
    System.out.println("skip: " + c.getOpCode());
  }

  public void handleNewRowCommand(NewRowCommand c) {
    System.out.println("new row: " + c.getOpCode());
  }

  public void handleXXXCommand(XXXCommand c) {
    System.out.println("xxx: " + c.getOpCode());
  }

  public void handleYYYCommand(YYYCommand c) {
    System.out.println("yyy: " + c.getOpCode());
  }

  public void handleNoOpCommand(NoOpCommand c) {
    System.out.println("no op: " + c.getOpCode());
  }

  public void handleCharLocCommand(CharLocCommand c) {
    System.out.println("char loc: " + c.getOpCode());
  }

  public void handlePreCommand(PreCommand c) {
    System.out.println("pre: " + c.getOpCode());
  }

  public void handlePostCommand(PostCommand c) {
    System.out.println("post: " + c.getOpCode());
  }

  public void handlePostPostCommand(PostPostCommand c) {
    System.out.println("post post: " + c.getOpCode());
  }

  public void handleUndefinedCommand(UndefinedCommand c) {
    System.out.println("undefined: " + c.getOpCode());
  }

}
