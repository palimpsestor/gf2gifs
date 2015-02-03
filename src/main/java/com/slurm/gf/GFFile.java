// GFFile.java
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
 * Utility class for reading data out of a binary file that
 * represents a generic font.
 * <p>
 * This class extends RandomAccessFile so that it can easily extract
 * the information in the &quot;postamble&quot;, which (naturally enough)
 * occurs at the end.
 *
 * @author Richard Blaylock
 */
public class GFFile extends RandomAccessFile {

  /**
   * Creates a GF file stream to read from the file with the specified name.
   *
   * @param s the name of the file
   */
  public GFFile(String s) throws FileNotFoundException {
    super(s, "r");
  }

  /**
   * Tests for end of file.
   * It seems like we should be able to do this just by seeking one
   * more byte and checking for an EOFException, but apparently we
   * have to be trickier.
   */
  public boolean eof() {
    boolean retVal = false;
    int b = 0;
    try {
      b = read();
      if (b == -1) retVal = true;
      else
        seek(getFilePointer() - 1);
    }
    catch (EOFException eofe) {
      eofe.printStackTrace();
      retVal = true;
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
      retVal = true;
    }
    return retVal;
  }

  /**
   * Reads a single byte from the file and returns it as an int.
   *
   * @return the int value of the byte read
   */
  public int readByteAsInt() {
    // module 24
    int retVal = 0;
    if (!eof()) {
      try {
        retVal = read();
      }
      catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    return retVal;
  }

  /**
   * Reads two bytes from the file and interprets them as an int.
   *
   * @return the int value of the two bytes read
   */
  public int readTwoBytesAsInt() {
    // module 24
    int retVal = 0;
    try {
      int a = read();
      int b = read();
      retVal = (a * 256) + b;
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return retVal;
  }

  /**
   * Reads three bytes from the file and interprets them as an int.
   *
   * @return the int value of the three bytes read
   */
  public int readThreeBytesAsInt() {
    // module 24
    int retVal = 0;
    try {
      int a = read();
      int b = read();
      int c = read();
      retVal = (((a * 256) + b) * 256) + c;
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return retVal;
  }

  /**
   * Reads four bytes from the file and interprets them as an int.
   *
   * @return the int value of the four bytes read
   */
  public int readFourBytesAsInt() {
    // module 24
    int retVal = 0;
    try {
      int a = read();
      int b = read();
      int c = read();
      int d = read();
      if (a < 128) retVal = (((((a * 256) + b) * 256) + c) * 256) + d;
      else retVal = ((((((a - 256) * 256) + b) * 256) + c) * 256) + d;
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return retVal;
  }

  /**
   * Reads a GF opcode and its associated parameters.
   * This method is basically a big switch based on the opcode,
   * which determines how many more bytes to read and which concrete
   * subclass of Command to stuff them into.
   *
   * @return a Command object encapsulating the opcode and its parameters
   * @see Command
   */
  public Command readCommand() throws GFFileFormatException {
    int o = readByteAsInt();
    int loc = 0;
    try {
      loc = (int)getFilePointer() - 1;
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    Command retVal = new NoOpCommand(loc, o);

    if ((Command.PAINT_0 <= o) && (o <= Command.PAINT_63)) {
      int d = o - Command.PAINT_0;
      retVal = new PaintCommand(loc, o, d);
    }
    else if (o == Command.PAINT1) {
      int d = readByteAsInt();
      retVal = new PaintCommand(loc, o, d);
    }
    else if (o == Command.PAINT2) {
      int d = readTwoBytesAsInt();
      retVal = new PaintCommand(loc, o, d);
    }
    else if (o == Command.PAINT3) {
      int d = readThreeBytesAsInt();
      retVal = new PaintCommand(loc, o, d);
    }
    else if (o == Command.BOC) {
      int c = readFourBytesAsInt();
      int p = readFourBytesAsInt();
      int minM = readFourBytesAsInt();
      int maxM = readFourBytesAsInt();
      int minN = readFourBytesAsInt();
      int maxN = readFourBytesAsInt();
      retVal = new BOCCommand(loc, o, c, p, minM, maxM, minN, maxN);
    }
    else if (o == Command.BOC1) {
      int c = readByteAsInt();
      int delM = readByteAsInt();
      int maxM = readByteAsInt();
      int delN = readByteAsInt();
      int maxN = readByteAsInt();
      retVal = new BOCCommand(loc, o, c, -1, maxM - delM, maxM, maxN - delN, maxN);
    }
    else if (o == Command.EOC) {
      retVal = new EOCCommand(loc, o);
    }
    else if (o == Command.SKIP0) {
      retVal = new SkipCommand(loc, o, 1);
    }
    else if (o == Command.SKIP1) {
      int d = readByteAsInt();
      retVal = new SkipCommand(loc, o, d + 1);
    }
    else if (o == Command.SKIP2) {
      int d = readTwoBytesAsInt();
      retVal = new SkipCommand(loc, o, d + 1);
    }
    else if (o == Command.SKIP3) {
      int d = readThreeBytesAsInt();
      retVal = new SkipCommand(loc, o, d + 1);
    }
    else if ((Command.NEW_ROW_0 <= o) && (o <= Command.NEW_ROW_164)) {
      int offset = o - Command.NEW_ROW_0;
      retVal = new NewRowCommand(loc, o, offset);
    }
    else if ((Command.XXX1 <= o) && (o <= Command.XXX4)) {
      int k = 0;
      switch (o) {
        case Command.XXX1:
          k = readByteAsInt(); 
          break;
        case Command.XXX2:
          k = readTwoBytesAsInt(); 
          break;
        case Command.XXX3:
          k = readThreeBytesAsInt(); 
          break;
        case Command.XXX4:
          k = readFourBytesAsInt(); 
          break;
      }
      int[] xbytes = new int[k];
      int i;
      for (i = 0; i < k; i++) {
        xbytes[i] = readByteAsInt();
      }
      retVal = new XXXCommand(loc, o, k, xbytes);
    }
    else if (o == Command.YYY) {
      int[] ybytes = new int[4];
      int i;
      for (i = 0; i < 4; i++) {
        ybytes[i] = readByteAsInt();
      }
      retVal = new YYYCommand(loc, o, ybytes);
    }
    else if (o == Command.NO_OP) {
      retVal = new NoOpCommand(loc, o);
    }
    else if (o == Command.CHAR_LOC) {
      int c = readByteAsInt();
      int dx = readFourBytesAsInt();
      int dy = readFourBytesAsInt();
      int w = readFourBytesAsInt();
      int p = readFourBytesAsInt();
      retVal = new CharLocCommand(loc, o, c, dx, dy, w, p);
    }
    else if (o == Command.CHAR_LOC0) {
      int c = readByteAsInt();
      int dm = readByteAsInt();
      int w = readFourBytesAsInt();
      int p = readFourBytesAsInt();
      retVal = new CharLocCommand(loc, o, c, 65536 * dm, 0, w, p);
    }
    else if (o == Command.PRE) {
      int id = readByteAsInt();
      int k = readByteAsInt();
      int[] prebytes = new int[k];
      int i;
      for (i = 0; i < k; i++) {
        prebytes[i] = readByteAsInt();
      }
      retVal = new PreCommand(loc, o, id, prebytes);
    }
    else if (o == Command.POST) {
      int p = readFourBytesAsInt();
      int ds = readFourBytesAsInt();
      int cs = readFourBytesAsInt();
      int hppp = readFourBytesAsInt();
      int vppp = readFourBytesAsInt();
      int min_m = readFourBytesAsInt();
      int max_m = readFourBytesAsInt();
      int min_n = readFourBytesAsInt();
      int max_n = readFourBytesAsInt();
      retVal = new PostCommand(loc, o, p, ds, cs, hppp, vppp,
                                   min_m, max_m, min_n, max_n);
    }
    else if (o == Command.POST_POST) {
      int postLoc = readFourBytesAsInt();
      retVal = new PostPostCommand(loc, o, postLoc);
    }
    return retVal;
  }

  /**
   * Reads the (hopefully unique) Post command in this file.
   * <p>
   * The method used is suggested in (module 19 of) the original
   * <a href="http://www.tug.org/web2c/manual/web2c_10.html#SEC67">gftype</a>
   * program:<ul><li>read backwards over pad bytes
   * until finding the format id byte, and the postpost opcode</li>
   * <li>back up four bytes and read the postloc value</li>
   * <li>seek to the postloc and read the post command</li></ul>
   *
   * @return the PostCommand at the end of the file
   */
  public PostCommand getPostCommand() throws GFFileFormatException {
    PostCommand retVal = new PostCommand(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    try {
      // leave a trail of breadcrumbs
      long savePos = getFilePointer();

      // go to the end of the file
      long pos = length() - 1;
      seek(pos);

      // read backwards over 223's and id byte
      int b = readByteAsInt();
      while (b == Command.GF_POST_SIG) {
        pos--;
        seek(pos);
        b = readByteAsInt();
      }
      if (b != Command.GF_ID_BYTE) {
        throw new GFFileFormatException("bad id byte: " + b);
      }

      // read the postLoc
      pos = pos - 4;
      seek(pos);
      int postLoc = readFourBytesAsInt();

      // now we have it
      seek(postLoc);
      try {
        retVal = (PostCommand)(readCommand());
      }
      catch (ClassCastException cce) {
        throw new GFFileFormatException("command at " + postLoc + " is not post");
      }

      // go back to wherever we came from
      seek(savePos);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return retVal;
  }
      
}
