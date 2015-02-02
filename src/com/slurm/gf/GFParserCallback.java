// GFParserCallback.java
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
 * Abstract class for handling commands encountered while parsing
 * generic font files.
 * <p>
 * The various commands of the generic font language and their meanings
 * are defined in the original
 * <a href="http://www.tug.org/web2c/manual/web2c_10.html#SEC67">gftype</a>
 * program.
 *
 * @author Richard Blaylock
 * @see GFParser
 * @see GFFile
 */
public abstract class GFParserCallback {

  /**
   * respond to an error from which the parser cannot recover
   */
  public abstract void badGF(String msg) throws GFFileFormatException;

  /**
   * respond to an error encountered during parsing
   */
  public abstract void error(int label, String msg);

  /**
   * respond to any &quot;special&quot; commands that occur
   * before a character or before the post command
   */
  public abstract void startSpecialCommands(int location);

  /**
   * respond to a paint command
   */
  public abstract void handlePaintCommand(PaintCommand c);

  /**
   * respond to a boc command
   */
  public abstract void handleBOCCommand(BOCCommand c);

  /**
   * respond to an eoc command
   */
  public abstract void handleEOCCommand(EOCCommand c);

  /**
   * respond to a skip command
   */
  public abstract void handleSkipCommand(SkipCommand c);

  /**
   * respond to a newrow command
   */
  public abstract void handleNewRowCommand(NewRowCommand c);

  /**
   * respond to an xxx command
   */
  public abstract void handleXXXCommand(XXXCommand c);

  /**
   * respond to a yyy command
   */
  public abstract void handleYYYCommand(YYYCommand c);

  /**
   * respond to a noop command
   */
  public abstract void handleNoOpCommand(NoOpCommand c);

  /**
   * respond to a charloc command
   */
  public abstract void handleCharLocCommand(CharLocCommand c);

  /**
   * respond to a pre command
   */
  public abstract void handlePreCommand(PreCommand c);

  /**
   * respond to a post command
   */
  public abstract void handlePostCommand(PostCommand c);

  /**
   * respond to a postpost command
   */
  public abstract void handlePostPostCommand(PostPostCommand c);

  /**
   * respond to an undefined command
   */
  public abstract void handleUndefinedCommand(UndefinedCommand c);

  /**
   * respond to an arbitrary command, based on its opcode
   */
  protected final void handleCommand(Command c) {
    int o = c.getOpCode();
    try {
      if ((Command.PAINT_0 <= o) && (o <= Command.PAINT3))
        handlePaintCommand((PaintCommand)c);
      else
      if ((o == Command.BOC) || (o == Command.BOC1))
        handleBOCCommand((BOCCommand)c);
      else
      if (o == Command.EOC)
        handleEOCCommand((EOCCommand)c);
      else
      if ((Command.SKIP0 <= o) && (o <= Command.SKIP3))
        handleSkipCommand((SkipCommand)c);
      else
      if ((Command.NEW_ROW_0 <= o) && (o <= Command.NEW_ROW_164))
        handleNewRowCommand((NewRowCommand)c);
      else
      if ((Command.XXX1 <= o) && (o <= Command.XXX4))
        handleXXXCommand((XXXCommand)c);
      else
      if (o == Command.YYY)
        handleYYYCommand((YYYCommand)c);
      else
      if ((o == Command.CHAR_LOC) || (o == Command.CHAR_LOC0))
        handleCharLocCommand((CharLocCommand)c);
      else
      if (o == Command.PRE)
        handlePreCommand((PreCommand)c);
      else
      if (o == Command.POST)
        handlePostCommand((PostCommand)c);
      else
      if (o == Command.POST_POST)
        handlePostPostCommand((PostPostCommand)c);
      else
        handleUndefinedCommand((UndefinedCommand)c);
    }
    catch (ClassCastException cce) {
      // shouldn't happen...
      cce.printStackTrace();
    }
  
  }
}
