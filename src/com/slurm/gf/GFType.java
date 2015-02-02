// GFType.java
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
 * Application emulating the behavior of the original
 * <a href="http://www.tug.org/web2c/manual/web2c_10.html#SEC67">gftype</a>
 * program by D.&nbsp;R.&nbsp;Fuchs.
 *
 * @author Richard Blaylock
 */
public class GFType {

  // module 1
  static final String BANNER = "This is a Java port of GFType, Version 3.1";

  static boolean wantsMnemonics;
  static boolean wantsPixels;

  public static void main(String args[]) {

    String gfFileName = "";

    wantsMnemonics = false;
    wantsPixels = false;

    for (int arg = 0; arg < args.length; arg++) {
      if (args[arg].equals("-m")) wantsMnemonics = true;
      else if (args[arg].equals("-p")) wantsPixels = true;
      else if (args[arg].charAt(0) == '-') {
        System.out.println("unknown flag " + args[arg]);
        System.exit(0);
      }
      else gfFileName = args[arg];
    }

    if (gfFileName.equals("")) {
      System.out.println("usage: java [-m] [-p] GFfile");
      System.exit(0);
    }

    // module 3
    System.out.println(BANNER);

    // module 66
    try {
      System.out.println("Options selected: Mnemonic output = " +
                         wantsMnemonics + "; pixel output = " +
                         wantsPixels + ".");
      GFFile gfInputFile = new GFFile(gfFileName);
      GFTypeCallback callback = new GFTypeCallback(wantsMnemonics, wantsPixels);
      GFParser parser = new GFParser(gfInputFile, callback);

      parser.parse();
      gfInputFile.close();

      // module 66
      int totalChars = callback.getTotalChars();
      System.out.print("The file had " + totalChars + " character");
      if (totalChars != 1) System.out.print("s");
      System.out.println(" altogether.");
    }
    catch (FileNotFoundException fnfe) {
      System.out.println("fatal: gf file `" + gfFileName + "' not found.");
      System.exit(0);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

}
