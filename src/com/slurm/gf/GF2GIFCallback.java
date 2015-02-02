// GF2GIFCallback.java
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
import java.awt.*;
import java.awt.image.*;
import Acme.JPM.Encoders.*;

/**
 * Set of callback functions to create GIF images of characters
 * as they are encountered while parsing a generic font file.
 *
 * @author Richard Blaylock
 */
public class GF2GIFCallback extends GFParserCallback {

  int m, n;
  int paintSwitch;
  int totalChars;

  static final int BLACK = 1;
  static final int WHITE = 0;

  IndexColorModel colorModel;

  int width;
  int height;
  int minM, maxM, minN, maxN;
  int minHeight;
  int maxHeight;
  int adjustedMinHeight;
  int adjustedMaxHeight;

  int pixels[];

  int characterCode;
  MemoryImageSource mis;
  ImageEncoder encoder;
  String filePrefix;

  /**
   * Creates a new GF2GIFCallback.
   *
   * @param foreground the foreground color of the characters in the GIF images.
   * @param background the background color of the characters in the GIF images.
   * @param hasTransparent whether background or foreground should be
   * transparent.
   * @param transparentBackground whether the transparent color should be the
   * background color or the foreground color.
   * @param minHeight the lower bound to use for row numbers in the pixel
   * arrays of characters.
   * @param maxHeight the upper bound to use for row numbers in the pixel
   * arrays of characters.
   * @param filePrefix the prefix of the resulting GIF output files.
   * @param targetDirectory the directory in which to put the GIF files.
   */
  public GF2GIFCallback(Color foreground, Color background, boolean hasTransparent, boolean transparentBackground, int minHeight, int maxHeight, String filePrefix, String targetDirectory) {
    paintSwitch = WHITE;
    m = 0;
    n = 0;
    totalChars = 0;

    width = 0;
    height = 0;

    byte[] redMap = { (byte)(background.getRed()),
                      (byte)(foreground.getRed()) };
    byte[] greenMap = { (byte)(background.getGreen()),
                        (byte)(foreground.getGreen()) };
    byte[] blueMap = { (byte)(background.getBlue()),
                       (byte)(foreground.getBlue()) };
    if (hasTransparent) {
      if (transparentBackground) {
        colorModel = new IndexColorModel(1, 2, redMap, greenMap, blueMap, 0);
      }
      else {
        colorModel = new IndexColorModel(1, 2, redMap, greenMap, blueMap, 1);
      }
    }
    else {
      colorModel = new IndexColorModel(1, 2, redMap, greenMap, blueMap);
    }

    this.minHeight = minHeight;
    this.maxHeight = maxHeight;

    if (!targetDirectory.equals("")) {
      this.filePrefix = targetDirectory + File.separatorChar + filePrefix;
    }
    else {
      this.filePrefix = filePrefix;
    }
  }

  public GF2GIFCallback(Color foreground, Color background, boolean hasTransparent, boolean transparentBackground, String filePrefix, String targetDirectory) {
    this(foreground, background, hasTransparent, transparentBackground, Integer.MAX_VALUE, Integer.MIN_VALUE, filePrefix, targetDirectory);
  }

  public GF2GIFCallback(Color foreground, Color background, boolean hasTransparent, boolean transparentBackground) {
    this(foreground, background, hasTransparent, transparentBackground, Integer.MAX_VALUE, Integer.MIN_VALUE, "char", "");
  }

  public GF2GIFCallback(Color foreground, Color background, boolean hasTransparent) {
    this(foreground, background, hasTransparent, true);
  }

  public GF2GIFCallback(Color foreground, Color background) {
    this(foreground, background, false);
  }

  public GF2GIFCallback() {
    this(Color.black, Color.white);
  }

  /**
   * @return the total number of characters encountered in the GF file.
   */
  public int getTotalChars() {
    return totalChars;
  }

  public void badGF(String msg) throws GFFileFormatException {
    System.out.println(" Bad GF file: " + msg + "!");
    throw new GFFileFormatException();
  }
  
  public void error(int label, String s) {
    System.out.println(label + ":" + s);
  }

  public void startSpecialCommands(int loc) {
  }

  public void handlePaintCommand(PaintCommand paint) {
    int l, r, cursor;
    int p = paint.getNumPixels();

    // advance m first
    m = m + p;
    // paint pixels m - p through m - 1
    // in row n of the subarray
    if (paintSwitch == BLACK) {
      l = m - p;
      r = m - 1;
      cursor = l;
      while (cursor <= r) {
        pixels[width * n + cursor] = BLACK;
        cursor++;
      }
    }
    paintSwitch = 1 - paintSwitch;
  }

  public void handleBOCCommand(BOCCommand boc) {
    characterCode = boc.getCharacterCode();
    System.out.print("[" + characterCode + "]");
    totalChars++;

    m = 0;
    n = 0;
    paintSwitch = WHITE;

    minM = boc.getMinM();
    maxM = boc.getMaxM();
    minN = boc.getMinN();
    maxN = boc.getMaxN();
    adjustedMinHeight = minHeight;
    if (adjustedMinHeight > minN) adjustedMinHeight = minN;
    adjustedMaxHeight = maxHeight;
    if (adjustedMaxHeight < maxN) adjustedMaxHeight = maxN;

    width = (maxM - minM) + 1;
    height = (maxN - minN) + 1;

    pixels = new int[width * height];

    // clear the array
    for (int i = 0; i < width * height; i++) {
      pixels[i] = WHITE;
    }
  }

  public void handleEOCCommand(EOCCommand eoc) {

    int[] extendedPixels = pixels;
    int extendedHeight = height;

    try {

      if ((adjustedMinHeight != minN) || (adjustedMaxHeight != maxN)) {
        extendedHeight = (adjustedMaxHeight - adjustedMinHeight) + 1;
        extendedPixels = new int[width * extendedHeight];
        int i;
        for (i = 0; i < width * extendedHeight; i++) {
          extendedPixels[i] = WHITE;
        }
        int offset = (adjustedMaxHeight - maxN) * width;

        // copy pixels into extended array
        for (i = 0; i < width * height; i++) {
          extendedPixels[i + offset] = pixels[i];
        }
      }

      mis = new MemoryImageSource(width, extendedHeight, colorModel, extendedPixels, 0, width);
    
      encoder = new GifEncoder(mis, new FileOutputStream(filePrefix + characterCode + ".gif"));
      encoder.encode();
    }
    catch (FileNotFoundException fnfe) {
      System.out.println("! could not create file " + filePrefix + characterCode + ".gif");
      System.exit(0);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void handleSkipCommand(SkipCommand skip) {
    // translate a SKIP command
    int p = skip.getRows() - 1;
    n = n + p + 1;
    m = 0;
    paintSwitch = WHITE;
  }

  public void handleNewRowCommand(NewRowCommand newrow) {
    // translate a NEW_ROW command
    int p = newrow.getOffset();
    p = newrow.getOpCode() - Command.NEW_ROW_0;
    n++;
    m = p;
    paintSwitch = BLACK;
  }

  public void handleXXXCommand(XXXCommand xxx) {
  }

  public void handleYYYCommand(YYYCommand c) {
  }

  public void handleNoOpCommand(NoOpCommand c) {
  }

  public void handleCharLocCommand(CharLocCommand c) {
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
  }

  public void handlePostCommand(PostCommand c) {
    System.out.println();
  }

  public void handlePostPostCommand(PostPostCommand c) {
  }

  public void handleUndefinedCommand(UndefinedCommand c) {
  }

}
