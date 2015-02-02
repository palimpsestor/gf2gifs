// GF2GIFs.java
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
import java.awt.*;
import java.io.*;

/**
 * Application to convert a generic font file into a set of GIF image
 * files.
 *
 * @author Richard Blaylock
 */
public class GF2GIFs {

  static final String BANNER = "This is a GF2GIFs, version 1.0";

  public static void main(String args[]) {

    String gfFileName = "";
    Color foreground = Color.black;
    Color background = Color.white;
    boolean hasTransparent = false;
    boolean transparentBackground = false;
    boolean transparentForeground = false;
    boolean tightBoundingBoxes = false;
    String filePrefix = "char";
    String targetDirectory = "";

    for (int arg = 0; arg < args.length; arg++) {
      if (args[arg].equals("-fg")) {
        arg++;
        if (arg >= args.length) usageExit();
        try {
          foreground = new Color(Integer.parseInt(args[arg], 16));
        }
        catch (NumberFormatException nfe) {
          usageExit();
        }
      }
      else if (args[arg].equals("-bg")) {
        arg++;
        if (arg >= args.length) usageExit();
        try {
          background = new Color(Integer.parseInt(args[arg], 16));
        }
        catch (NumberFormatException nfe) {
          usageExit();
        }
      }
      else if (args[arg].equals("-tb")) {
        transparentBackground = true;
      }
      else if (args[arg].equals("-tf")) {
        transparentForeground = true;
      }
      else if (args[arg].equals("-tight")) {
        tightBoundingBoxes = true;
      }
      else if (args[arg].equals("-p")) {
        arg++;
        if (arg >= args.length) usageExit();
        filePrefix = args[arg];
      }
      else if (args[arg].equals("-d")) {
        arg++;
        if (arg >= args.length) usageExit();
        targetDirectory = args[arg];
      }
      else if (args[arg].equals("--help")) {
        usageHelpExit();
      }
      else if (args[arg].charAt(0) == '-') {
        System.out.println("unknown flag " + args[arg]);
        usageExit();
      }
      else gfFileName = args[arg];
    }

    if (gfFileName.equals("")) {
      usageExit();
    }
    if (transparentBackground && transparentForeground) {
      System.out.println("Warning: -tb and -tf conflict. Only -tb will be used.");
      transparentForeground = false;
    }
    if (transparentBackground || transparentForeground) {
      hasTransparent = true;
    }

    System.out.println(BANNER);
    try {
      GFFile gfInputFile = new GFFile(gfFileName);
      GF2GIFCallback callback;

      if (!tightBoundingBoxes) {
        PostCommand post = gfInputFile.getPostCommand();
        int minN = post.getMinN();
        int maxN = post.getMaxN();

        callback = new GF2GIFCallback(foreground, background, hasTransparent, transparentBackground, minN, maxN, filePrefix, targetDirectory);
      }
      else {
        callback = new GF2GIFCallback(foreground, background, hasTransparent, transparentBackground, filePrefix, targetDirectory);
      }
      GFParser parser = new GFParser(gfInputFile, callback);

      parser.parse();
      int totalChars = callback.getTotalChars();
      System.out.print("The file had " + totalChars + " character");
      if (totalChars != 1) System.out.print("s");
      System.out.println(" altogether.");
    }
    catch (FileNotFoundException fnfe) {
      System.out.println("fatal: gf file `" + gfFileName + "' not found.");
      System.exit(0);
    }
    catch (GFFileFormatException gfffe) {
      gfffe.printStackTrace();
    }
  }

  private static void usage() {
    System.out.println("usage: java GF2GIFs [-tb] [-tf] [-fg foreground] [-bg background] [-tight] [-p fileprefix] [-d targetdirectory] gffile");
  }

  private static void usageExit() {
    usage();
    System.out.println();
    System.out.println("Try java GF2GIFs --help for more information.");
    System.out.println();
    System.exit(0);
  }

  private static void usageHelpExit() {
    System.out.println(BANNER);
    System.out.println();
    usage();

    System.out.println();
    System.out.println("  -bg background:     use six hex bytes to specify the background color");
    System.out.println("  -fg foreground:     use six hex bytes to specify the foreground color");
    System.out.println("  -d targetdirectory: put the GIF images in the specified directory");
    System.out.println("  -p fileprefix:      GIF file names will be fileprefixNNN.gif,");
    System.out.println("                      where NNN is the character code");
    System.out.println("  -tb:                create GIFs with a transparent background");
    System.out.println("  -tf:                create GIFs with a transparent foreground");
    System.out.println("  -tight:             create GIFs with a tight bounding box around");
    System.out.println("                      each character, rather than constant height");
    System.out.println();

    System.exit(0);
  }

}
