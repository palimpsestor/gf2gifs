// GFParser.java
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
import java.io.*;

/**
 * Class to parse a generic font file.
 * <p>
 * This parser is based on the
 * <a href="http://www.tug.org/web2c/manual/web2c_10.html#SEC67">gftype</a>
 * program by D.&nbsp;R.&nbsp;Fuchs.
 *
 * @author Richard Blaylock
 */
public class GFParser {
// Comments of the form "module nn" refer to the module number in
// the original gftype program that corresponds to the given code.

  /**
   * The file from which to read commands. 
   */
  GFFile gffile;

  /**
   * The set of callback functions that determine what happens when
   * the different .gf commands are encountered.
   */
  GFParserCallback callback;

  /**
   * Creates a GFParser object that reads commands from the given
   * file and calls various methods on the given GFParserCallback
   * object according to the commands that are encountered.
   *
   * @param g the file from which to read commands
   * @param c the callback functions to execute when encountering commands
   * @see GFFile
   * @see GFParserCallback
   */
  public GFParser(GFFile g, GFParserCallback c) {
    gffile = g;
    callback = c;
  }

  /**
   * Parses the file, calling methods of the callback as it goes.
   */
  public void parse() {
    try {
      // module 66
      processPreamble();
      Command post = processAllCharacters();
      processPostamble(post);
    }
    catch (GFFileFormatException gffe) {
      gffe.printStackTrace();
    }
  }

  /**
   * Process the preamble of the GF file.
   * The preamble, which comes at the beginning of every GF file,
   * consists of the preamble opcode (247), followed by an identifying
   * number for the GF format (currently 131), followed by a byte k
   * which gives the length of the remainder of the preamble.
   * Those remaining k bytes typically represent a commentary string.
   */
  private void processPreamble() throws GFFileFormatException {
    PreCommand pre = null;

    // module 68
    Command c = gffile.readCommand();
    if (c.getOpCode() != Command.PRE) {
      callback.badGF("First byte isn't start of preamble!");
    }
    try {
      pre = (PreCommand) c;
      int id = pre.getIDByte();
      if (id != Command.GF_ID_BYTE) {
        callback.badGF("identification byte should be " + Command.GF_ID_BYTE +
                       " not " + id);
      }
      callback.handlePreCommand(pre);
    }
    catch (ClassCastException cce) {
      // this should never happen
      cce.printStackTrace();
    }
  }

  /**
   * Parses the individual characters in the file.
   * returns the PostCommand that follows the last character.
   *
   * @return the Post command that follows the last character
   */
  private Command processAllCharacters() throws GFFileFormatException {
    Command c;
    // we initialize c here just to satisfy the compiler
    c = new NoOpCommand(0, 0);
    int o;

    try {
      // module 69
      do {
        callback.startSpecialCommands((int)gffile.getFilePointer());
        c = passNoOpCommands();
        o = c.getOpCode();
        if (o != Command.POST) {
          if ((o != Command.BOC) && (o != Command.BOC1)) {
            callback.badGF("byte " +
                           c.getLocation() +
                           " is not boc (" + o + ")");
          }
          callback.handleBOCCommand((BOCCommand)c);
          if (!doChar()) callback.badGF("char ended unexpectedly");
        }
      } while (o != Command.POST);
    }
    catch (ClassCastException cce) {
      cce.printStackTrace();
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return c;
  }

  /**
   * Passes commands until finding one that is not a NoOp,
   * XXX, or YYY.
   *
   * @return the first Command found that is not a NoOp, XXX, or YYY.
   */
  private Command passNoOpCommands() throws GFFileFormatException {
    Command c;
    int o;

    // module 70
    do {
      c = gffile.readCommand();
      o = c.getOpCode();
      if ((o == Command.YYY) ||
          ((Command.XXX1 <= o) && (o <= Command.XXX4)) ||
          (o == Command.NO_OP)) {
        callback.handleCommand(c);
        o = Command.NO_OP;
      }
    } while (o == Command.NO_OP);

    return c;
  }

  /**
   * Handles a sequence of Paint commands.
   * 
   * @param first the first Paint command in the sequence.
   */
  private Command readPaintSequence(Command first) throws GFFileFormatException {
    Command c = first;
    try {
      do {
        callback.handlePaintCommand((PaintCommand)c);
        c = gffile.readCommand(); 
      } while (c.getOpCode() <= Command.PAINT3);
    }
    catch (ClassCastException cce) {
      cce.printStackTrace();
    }
    return c;
  }

  /**
   * Handles a the commands in the file corresponding to a single
   * character.  The assumption on entry is that we've just parsed a
   * BOC command.
   */
  private boolean doChar() throws GFFileFormatException {
    // module 49
    boolean aok = true;

    try {
      boolean foundEOC;
      foundEOC = false;

      Command c;
      int o;
      int a;
      while (!foundEOC) {
        c = gffile.readCommand();
        o = c.getOpCode();

        // module 50
        if (gffile.eof()) {
          callback.badGF("the file ended prematurely");
        }

        // module 51
        if (o <= Command.PAINT3) c = readPaintSequence(c);

        o = c.getOpCode();
        a = c.getLocation();

        // module 52
        if (o == Command.PRE) {
          charError(a, "preamble command within a character!");
        }
        else if ((o == Command.POST) || (o == Command.POST_POST)) {
          charError(a, "postamble command within a character!");
        }
        else if ((o == Command.BOC) || (o == Command.BOC1)) {
          charError(a, "boc occurred before eoc!");
        }

        // module 51
        else if ((o == Command.CHAR_LOC) || (o == Command.CHAR_LOC0)) {
          // this is technically incorrect but we are trying
          // to be consistent with the .web implementation of gftype
          error(a, "undefined command " + o + "!");
        }
        else if (o > Command.POST_POST) {
          error(a, "undefined command " + o + "!");
        }

        // module 52
        else if (o == Command.EOC) foundEOC = true;

        // modules 51 (for skip, new_row, xxx, and yyy commands)
        // if we got this far without a GFBadCharException then
        // this command must be okay
        callback.handleCommand(c);
      }
    }
    catch (GFBadCharException gfbce) {
      aok = false;
    }
    return aok;
  }

  /**
   * Process the postamble of the GF file.
   * The postamble consists of a Post command, followed by a set
   * of CharLoc commands (one for each character in the file), followed
   * in turn by a PostPost command to wrap things up.
   */
  private void processPostamble(Command post) throws GFFileFormatException {
    try {
      callback.handlePostCommand((PostCommand)post);
      int postLoc = post.getLocation();

      Command c;
      int o;

      // module 65
      // process char locs in the postamble
      do {
        c = gffile.readCommand();
        o = c.getOpCode();
        if ((o == Command.CHAR_LOC) || (o == Command.CHAR_LOC0)) {
          callback.handleCharLocCommand((CharLocCommand)c);
          // set o just to have a chance to get out of the loop.
          // note that legitimate NoOpCommands in this part
          // of the file will be lost (as they are by
          // the original gftype), since we don't call callback.handleCommand
          // for them.
          o = Command.NO_OP;
        }
      } while (o == Command.NO_OP);

      // module 64
      if (o != Command.POST_POST) {
        error(c.getLocation(), "should be postpost!");
      }
      PostPostCommand postpost = (PostPostCommand)c;
      callback.handlePostPostCommand(postpost);
      int q = postpost.getPostLoc();
      if (q != postLoc) {
        error(postpost.getLocation(),
                       "postamble pointer should be " + postLoc +
                       " not " + q + "!");
      }

      int m = gffile.readByteAsInt();
      if (m != Command.GF_ID_BYTE) {
        error(postpost.getLocation(),
                       "identification byte should be " + Command.GF_ID_BYTE +
                       ", not " + m + "!");
      }

      int k = (int)gffile.getFilePointer();
      m = gffile.readByteAsInt();

      while ((m == Command.GF_POST_SIG) && !gffile.eof()) {
        m = gffile.readByteAsInt();
      }
      if (!gffile.eof()) {
        callback.badGF("signature in byte " + (gffile.getFilePointer() - 1) +
                       " should be " + Command.GF_POST_SIG);
      }
      else if (gffile.getFilePointer() < k + 4) {
        error(postpost.getLocation(),
              "not enough signature bytes at end of file!");
      }

    }
    catch (ClassCastException cce) {
      // shouldn't happen
      cce.printStackTrace();
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }

  }

  /**
   * A convenience method that just calls the callback's error().
   */
  private void error(int label, String s) {
    callback.error(label, s);
  }

  /**
   * Throws a GFBadCharException when encountering an error during
   * character processing.
   */
  private void charError(int label, String s) throws GFBadCharException {
    error(label, s);
    throw new GFBadCharException(s);
  }

}
