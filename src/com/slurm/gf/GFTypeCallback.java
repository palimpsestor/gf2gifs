// GFTypeCallback.java
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
 * This implemenation of GFParserCallback responds to generic font
 * commands in such a way as to reproduce the behavior of the original
 * <a href="http://www.tug.org/web2c/manual/web2c_10.html#SEC67">gftype</a>
 * program.
 *
 * @author Richard Blaylock
 */
public class GFTypeCallback extends GFParserCallback {

// Comments of the form "module nn" refer to the module number in
// the original gftype program that corresponds to the given code.

  // module 5

  /** vertical extent of pixel image array */
  static final int MAX_ROW = 79;

  /** horizontal extent of pixel image array */
  static final int MAX_COL = 79;

  // module 25

  /** controls mnemonic output */
  boolean wantsMnemonics;

  /** controls pixel output */
  boolean wantsPixels;

  // module 35

  /** current column number */
  int m;

  /** current row number */
  int n;

  boolean paintSwitch;

  // module 36
  static final boolean BLACK = false;
  static final boolean WHITE = true;

  // module 37
  boolean[][] imageArray = new boolean[MAX_COL + 1][MAX_ROW + 1];

  // module 39
  int maxSubrow, maxSubcol;

  // module 46
  /** total number of characters seen so far */
  int totalChars;

  /** correct character location pointer */
  int[] charPtr = new int[256];

  /** char_ptr for next character */
  int gfPrevPtr;

  // module 41
  int minMStated, maxMStated, minNStated, maxNStated;
  int maxMObserved, maxNObserved;
  int maxMOverall, maxNOverall, minMOverall, minNOverall;

  boolean inPaintSequence;
  float pixRatio;

  /** Creates a GFTypeCallback object with the specified choice of output.
   *
   * @param mnemonics whether or not mnemonic output is desired
   * @param pixels whether or not pixel output is desired
   */
  public GFTypeCallback(boolean mnemonics, boolean pixels) {
    wantsMnemonics = mnemonics;
    wantsPixels = pixels;
    gfPrevPtr = 0;
    paintSwitch = WHITE;
    m = 0;
    n = 0;
    minMStated = 0;
    maxMStated = 0;
    minNStated = 0;
    maxNStated = 0;
    inPaintSequence = false;
    pixRatio = 0;

    // module 47
    totalChars = 0;
    for (int i = 0; i < 256; i++) {
      charPtr[i] = -1;
    }

    // module 63
    minMOverall = Integer.MAX_VALUE;
    maxMOverall = Integer.MIN_VALUE;
    minNOverall = Integer.MAX_VALUE;
    maxNOverall = Integer.MIN_VALUE;
  }

  // module 7
  public void badGF(String msg) throws GFFileFormatException {
    System.out.println("Bad GF file: " + msg + "!");
    throw new GFFileFormatException();
  }
  
  public void startSpecialCommands(int loc) {
    // module 69
    gfPrevPtr = loc;
  }

  public void handlePaintCommand(PaintCommand paint) {
    int l, r;
    int p = paint.getNumPixels();

    // module 56
    if ((wantsMnemonics) && (!inPaintSequence)) {
      System.out.print(" paint ");
    }
    // module 57
    if (wantsMnemonics) {
      if (paintSwitch == WHITE) System.out.print("(" + p + ")");
      else System.out.print(p);
    }
    m = m + p;

    if (m > maxMObserved) maxMObserved = m - 1;
    if (wantsPixels) {

      // module 58
      if (paintSwitch == BLACK) {
        if (n <= maxSubrow) {
          l = m - p;
          r = m - 1;
          if (r > maxSubcol) r = maxSubcol;
//  for clarity, could use something OTHER than m to step from l to r here...
          m = l;
          while (m <= r) {
            imageArray[m][n] = BLACK;
            m++;
          }
          m = l + p;
        } // if n <= maxSubrow
      }  // if paintSwitch == BLACK
    }  // if wantsPixels

    paintSwitch = !paintSwitch;
    inPaintSequence = true;
  }

  public void handleBOCCommand(BOCCommand boc) {
    // module 71
    totalChars++;

    int characterCode = boc.getCharacterCode();
    int c = characterCode % 256;
    if (c < 0) c = c + 256;

    int p = boc.getPreviousCharacterPointer();

    minMStated = boc.getMinM();
    maxMStated = boc.getMaxM();
    minNStated = boc.getMinN();
    maxNStated = boc.getMaxN();

    System.out.println();
    System.out.print(boc.getLocation() + ": beginning of char " + c);
    if (characterCode != c)
      System.out.println(" with extension " + ((characterCode -c) / 256));
    if (wantsMnemonics) {
      System.out.print(": " + minMStated + "<=m<=" + maxMStated + " ");
      System.out.println(minNStated + "<=n<=" + maxNStated);
    }
    maxMObserved = -1;

    if (charPtr[c] != p) {
      error(boc.getLocation(),
            "(previous character pointer should be " + charPtr[c] +
            ", not " + p + "!");
    }
    else if (p > 0) {
      if (wantsMnemonics)
        System.out.println("(previous character with the same code started at byte " + p + ")");
    }
    charPtr[c] = gfPrevPtr;

    if (wantsMnemonics) System.out.print("(initially n=" + maxNStated + ")");
    if (wantsPixels) clearImage();

    m = 0;
    n = 0;
    paintSwitch = WHITE;

    inPaintSequence = false;
  }

  public void handleEOCCommand(EOCCommand eoc) {
    // module 52
    showMnemonic(eoc, "eoc");
    System.out.println();

    // module 69
    maxNObserved = n;

    if (wantsPixels) printImage();

    // module 72
    maxMObserved = minMStated + maxMObserved + 1;
    n = maxNStated - maxNObserved;
    if (minMStated < minMOverall) minMOverall = minMStated;
    if (maxMObserved > maxMOverall) maxMOverall = maxMObserved;

    if (n < minNOverall) minNOverall = n;
    if (maxNStated > maxNOverall) maxNOverall = maxNStated;
    if (maxMObserved > maxMStated)
      System.out.println("The previous character should have had max m >= " +
              maxMObserved + "!");
    if (n > maxNStated)
      System.out.println("The previous character should have had min n >= " +
              n + "!");

    inPaintSequence = false;
  }

  // module 60
  public void handleSkipCommand(SkipCommand skip) {
    int p = skip.getRows() - 1;
    int o = skip.getOpCode();

    showMnemonic(skip, "skip" + ((o - Command.SKIP1 + 1) % 4) + " " + p);
    n = n + p + 1;
    m = 0;
    paintSwitch = WHITE;
    if (wantsMnemonics) {
      System.out.print(" (n=" + (maxNStated - n) + ")");
    }

    inPaintSequence = false;
  }

  // module 59
  public void handleNewRowCommand(NewRowCommand newRow) {
    int p = newRow.getOffset();
    showMnemonic(newRow, "newrow " + p);
    n++;
    m = p;
    paintSwitch = BLACK;
    if (wantsMnemonics) {
      System.out.print(" (n=" + (maxNStated - n) + ")");
    }

    inPaintSequence = false;
  }

  // module 53
  public void handleXXXCommand(XXXCommand xxx) {
    showMnemonic(xxx, "xxx '");
    int len = xxx.getXLength();
    int xbytes[] = xxx.getXBytes();

    if (len < 0) nlError(xxx, "string of negative length!");

    // module 54
    boolean badChar = false;

    int qByte;

    StringBuffer sb = new StringBuffer();
    for (len = 0; len < xbytes.length; len++) {
      qByte = xbytes[len];
      if ((qByte < (int)' ') || (qByte > (int)'~')) badChar = true;
      sb.append((char)qByte);
    }
    sb.append("'");

    if (wantsMnemonics) System.out.print(sb.toString());
    if (badChar) nlError(xxx, "non-ASCII character in xxx command!");

    inPaintSequence = false;
  }

  // module 55
  public void handleYYYCommand(YYYCommand yyy) {
    int[] yb = yyy.getYBytes();
    int p = 0;

    if (yb[0] < 128) p = (((((yb[0] * 256) + yb[1]) * 256) + yb[2]) * 256) + yb[3];
    else p = ((((((yb[0] - 256) * 256) + yb[1]) * 256) + yb[2]) * 256) + yb[3];
    Scaled s = new Scaled(p);
    // module 55
    showMnemonic(yyy, "yyy " + p + " (");
    if (wantsMnemonics) System.out.print(s + ")");
    inPaintSequence = false;
  }

  public void handleNoOpCommand(NoOpCommand noop) {
    // module 52
    showMnemonic(noop, "noop ");

    inPaintSequence = false;
  }

  public void handleCharLocCommand(CharLocCommand charLoc) {
    // module 65
    int c = charLoc.getCharacterResidue();
    int u = charLoc.getDX();
    int v = charLoc.getDY();
    Scaled uScaled = new Scaled(u);
    int width = charLoc.getWidth();
    Scaled wScaled = new Scaled(java.lang.Math.round(width * pixRatio));
    int p = charLoc.getBeginPointer();
    

    System.out.print("Character " + c + ": dx " + u + " (" + uScaled);
    if (v != 0) {
      Scaled vScaled = new Scaled(v);
      System.out.print("), dy " + v + " (" + vScaled);
    }
    System.out.print("), width " + width + " (" + wScaled);
    System.out.println("), loc " + p);

    if (charPtr[c] == 0) {
      error(charLoc.getLocation(), "duplicate locator for this character!");
    }
    else if (p != charPtr[c]) {
      error(charLoc.getLocation(), "character location should be " +
            charPtr[c] + "!");
    }
    charPtr[c] = 0;

    inPaintSequence = false;
  }

  public void handlePreCommand(PreCommand c) {
    int[] commentBytes = c.getCommentBytes();
    int len = commentBytes.length;
    StringBuffer csBuffer = new StringBuffer(len);
    int i;
    for (i = 0; i < len; i++) {
      csBuffer.append((char)commentBytes[i]);
    }
    System.out.println("'" + csBuffer.toString() + "'");
    inPaintSequence = false;
  }

  public void handlePostCommand(PostCommand post) {
    // module 62
    int ds = post.getDesignSize();
    int cs = post.getCheckSum();
    int hppp = post.getHPPP();
    int vppp = post.getVPPP();

    int p = post.getPostSpecialPointer();
    Scaled dsScaled = new Scaled(ds / 16);
    Scaled hpppScaled = new Scaled(hppp);
    Scaled vpppScaled = new Scaled(vppp);
    int minM = post.getMinM();
    int maxM = post.getMaxM();
    int minN = post.getMinN();
    int maxN = post.getMaxN();

    // module 66
    System.out.println();
    // module 61
    int postLoc = post.getLocation();
    System.out.print("Postamble starts at byte " + postLoc);
    if (postLoc == gfPrevPtr) System.out.println(".");
    else {
      System.out.print(", after special info at byte ");
      System.out.println(gfPrevPtr + ".");
    }
    if (p != gfPrevPtr) {
      error(postLoc, "backpointer in byte " + (postLoc + 1) +
                     " should be " + gfPrevPtr + " not " + p + "!");
    }
    System.out.println("design size = " + ds + " (" + dsScaled + "pt)");
    System.out.println("check sum = " + cs);
    System.out.println("hppp = " + hppp + " (" + hpppScaled + ")");
    System.out.println("vppp = " + vppp + " (" + vpppScaled + ")");
    pixRatio = ((float)ds/1048576)*((float)hppp/1048576);
    System.out.println("min m = " + minM + ", max m = " + maxM);
    System.out.println("min n = " + minN + ", max n = " + maxN);

    inPaintSequence = false;
  }

  public void handlePostPostCommand(PostPostCommand postpost) {
    // module 64
    for (int k = 0; k < 256; k++) {
      if (charPtr[k] > 0) {
        error(postpost.getLocation(), "missing locator for character " + k + "!");
      }
    }

    inPaintSequence = false;
  }

  public void handleUndefinedCommand(UndefinedCommand c) {
    System.out.println("undefined: " + c.getOpCode());
    inPaintSequence = false;
  }

  /**
   * @return the total number of characters encountered in the file
   */
  public int getTotalChars() {
    return totalChars;
  }

  // module 50
  void showLabel(int label, String s) {
    System.out.print(label + ": " + s);
  }

  // module 50
  void showMnemonic(Command c, String s) {
    if (wantsMnemonics) {
      System.out.println();
      showLabel(c.getLocation(), s);
    }
  }

  // module 50
  public void error(int label, String s) {
    showLabel(label, "! " + s);
    System.out.println();
  }

  // module 50
  public void nlError(Command c, String s) {
    System.out.println();
    showLabel(c.getLocation(), "! " + s);
    System.out.println();
  }

  // module 38
  void clearImage() {
    int m, n;

    maxSubcol = maxMStated - minMStated - 1;
    if (maxSubcol > MAX_COL) maxSubcol = MAX_COL;
    maxSubrow = maxNStated - minNStated ;
    if (maxSubrow > MAX_ROW) maxSubrow = MAX_ROW;

    n = 0;
    while (n <= maxSubrow) {
      m = 0;
      while (m <= maxSubcol) {
        imageArray[m][n] = WHITE;
        m++;
      }
      n++;
    }
  }

  void printImage() {
    int spaceCount;

    // module 42
    if ((maxMObserved > MAX_COL) || (maxNObserved > MAX_ROW)) {
      System.out.println("(The character is too large to be displayed in full.)");
    }
    if (maxSubcol > maxMObserved) maxSubcol = maxMObserved;
    if (maxSubrow > maxNObserved) maxSubrow = maxNObserved;

    // module 41
    if (maxSubcol >= 0) {
      // module 43
      // print asterisk patterns for rows 0 to maxSubrow
      System.out.println(".<--This pixel's lower left corner is at (" +
              minMStated + "," + (maxNStated + 1) +
              ") in METAFONT coordinates");
      n = 0;
      while (n <= maxSubrow) {
        m = 0;
        spaceCount = 0;
        while (m <= maxSubcol) {
          if (imageArray[m][n] == WHITE) spaceCount++;
          else {
            while (spaceCount > 0) {
              System.out.print(" ");
              spaceCount--;
            }
            System.out.print("*");
          }
          m++;
        }
        System.out.println();
        n++;
      }
      System.out.println(".<--This pixel's upper left corner is at (" +
                         minMStated + "," + (maxNStated - maxSubrow) +
                         ") in METAFONT coordinates");
    }
    else System.out.println("(The character is entirely blank.)");
  }

}
