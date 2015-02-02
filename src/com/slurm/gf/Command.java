// Command.java
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
 * Abstract class to encapsulate a command in the generic font
 * &quot;language&quot;"
 * Concrete subclasses allow access to the fields of the various commands.
 * This class also defines the opcode constants specified in the language.
 *
 * @author Richard Blaylock
 */

public abstract class Command {

  static final int PAINT_0 = 0;
  static final int PAINT_1 = 1;
  static final int PAINT_2 = 2;
  static final int PAINT_3 = 3;
  static final int PAINT_4 = 4;
  static final int PAINT_5 = 5;
  static final int PAINT_6 = 6;
  static final int PAINT_7 = 7;
  static final int PAINT_8 = 8;
  static final int PAINT_9 = 9;
  static final int PAINT_10 = 10;
  static final int PAINT_11 = 11;
  static final int PAINT_12 = 12;
  static final int PAINT_13 = 13;
  static final int PAINT_14 = 14;
  static final int PAINT_15 = 15;
  static final int PAINT_16 = 16;
  static final int PAINT_17 = 17;
  static final int PAINT_18 = 18;
  static final int PAINT_19 = 19;
  static final int PAINT_20 = 20;
  static final int PAINT_21 = 21;
  static final int PAINT_22 = 22;
  static final int PAINT_23 = 23;
  static final int PAINT_24 = 24;
  static final int PAINT_25 = 25;
  static final int PAINT_26 = 26;
  static final int PAINT_27 = 27;
  static final int PAINT_28 = 28;
  static final int PAINT_29 = 29;
  static final int PAINT_30 = 30;
  static final int PAINT_31 = 31;
  static final int PAINT_32 = 32;
  static final int PAINT_33 = 33;
  static final int PAINT_34 = 34;
  static final int PAINT_35 = 35;
  static final int PAINT_36 = 36;
  static final int PAINT_37 = 37;
  static final int PAINT_38 = 38;
  static final int PAINT_39 = 39;
  static final int PAINT_40 = 40;
  static final int PAINT_41 = 41;
  static final int PAINT_42 = 42;
  static final int PAINT_43 = 43;
  static final int PAINT_44 = 44;
  static final int PAINT_45 = 45;
  static final int PAINT_46 = 46;
  static final int PAINT_47 = 47;
  static final int PAINT_48 = 48;
  static final int PAINT_49 = 49;
  static final int PAINT_50 = 50;
  static final int PAINT_51 = 51;
  static final int PAINT_52 = 52;
  static final int PAINT_53 = 53;
  static final int PAINT_54 = 54;
  static final int PAINT_55 = 55;
  static final int PAINT_56 = 56;
  static final int PAINT_57 = 57;
  static final int PAINT_58 = 58;
  static final int PAINT_59 = 59;
  static final int PAINT_60 = 60;
  static final int PAINT_61 = 61;
  static final int PAINT_62 = 62;
  static final int PAINT_63 = 63;
  static final int PAINT1 = 64;
  static final int PAINT2 = 65;
  static final int PAINT3 = 66;
  static final int BOC = 67;
  static final int BOC1 = 68;
  static final int EOC = 69;
  static final int SKIP0 = 70;
  static final int SKIP1 = 71;
  static final int SKIP2 = 72;
  static final int SKIP3 = 73;
  static final int NEW_ROW_0 = 74;
  static final int NEW_ROW_1 = 75;
  static final int NEW_ROW_2 = 76;
  static final int NEW_ROW_3 = 77;
  static final int NEW_ROW_4 = 78;
  static final int NEW_ROW_5 = 79;
  static final int NEW_ROW_6 = 80;
  static final int NEW_ROW_7 = 81;
  static final int NEW_ROW_8 = 82;
  static final int NEW_ROW_9 = 83;
  static final int NEW_ROW_10 = 84;
  static final int NEW_ROW_11 = 85;
  static final int NEW_ROW_12 = 86;
  static final int NEW_ROW_13 = 87;
  static final int NEW_ROW_14 = 88;
  static final int NEW_ROW_15 = 89;
  static final int NEW_ROW_16 = 90;
  static final int NEW_ROW_17 = 91;
  static final int NEW_ROW_18 = 92;
  static final int NEW_ROW_19 = 93;
  static final int NEW_ROW_20 = 94;
  static final int NEW_ROW_21 = 95;
  static final int NEW_ROW_22 = 96;
  static final int NEW_ROW_23 = 97;
  static final int NEW_ROW_24 = 98;
  static final int NEW_ROW_25 = 99;
  static final int NEW_ROW_26 = 100;
  static final int NEW_ROW_27 = 101;
  static final int NEW_ROW_28 = 102;
  static final int NEW_ROW_29 = 103;
  static final int NEW_ROW_30 = 104;
  static final int NEW_ROW_31 = 105;
  static final int NEW_ROW_32 = 106;
  static final int NEW_ROW_33 = 107;
  static final int NEW_ROW_34 = 108;
  static final int NEW_ROW_35 = 109;
  static final int NEW_ROW_36 = 110;
  static final int NEW_ROW_37 = 111;
  static final int NEW_ROW_38 = 112;
  static final int NEW_ROW_39 = 113;
  static final int NEW_ROW_40 = 114;
  static final int NEW_ROW_41 = 115;
  static final int NEW_ROW_42 = 116;
  static final int NEW_ROW_43 = 117;
  static final int NEW_ROW_44 = 118;
  static final int NEW_ROW_45 = 119;
  static final int NEW_ROW_46 = 120;
  static final int NEW_ROW_47 = 121;
  static final int NEW_ROW_48 = 122;
  static final int NEW_ROW_49 = 123;
  static final int NEW_ROW_50 = 124;
  static final int NEW_ROW_51 = 125;
  static final int NEW_ROW_52 = 126;
  static final int NEW_ROW_53 = 127;
  static final int NEW_ROW_54 = 128;
  static final int NEW_ROW_55 = 129;
  static final int NEW_ROW_56 = 130;
  static final int NEW_ROW_57 = 131;
  static final int NEW_ROW_58 = 132;
  static final int NEW_ROW_59 = 133;
  static final int NEW_ROW_60 = 134;
  static final int NEW_ROW_61 = 135;
  static final int NEW_ROW_62 = 136;
  static final int NEW_ROW_63 = 137;
  static final int NEW_ROW_64 = 138;
  static final int NEW_ROW_65 = 139;
  static final int NEW_ROW_66 = 140;
  static final int NEW_ROW_67 = 141;
  static final int NEW_ROW_68 = 142;
  static final int NEW_ROW_69 = 143;
  static final int NEW_ROW_70 = 144;
  static final int NEW_ROW_71 = 145;
  static final int NEW_ROW_72 = 146;
  static final int NEW_ROW_73 = 147;
  static final int NEW_ROW_74 = 148;
  static final int NEW_ROW_75 = 149;
  static final int NEW_ROW_76 = 150;
  static final int NEW_ROW_77 = 151;
  static final int NEW_ROW_78 = 152;
  static final int NEW_ROW_79 = 153;
  static final int NEW_ROW_80 = 154;
  static final int NEW_ROW_81 = 155;
  static final int NEW_ROW_82 = 156;
  static final int NEW_ROW_83 = 157;
  static final int NEW_ROW_84 = 158;
  static final int NEW_ROW_85 = 159;
  static final int NEW_ROW_86 = 160;
  static final int NEW_ROW_87 = 161;
  static final int NEW_ROW_88 = 162;
  static final int NEW_ROW_89 = 163;
  static final int NEW_ROW_90 = 164;
  static final int NEW_ROW_91 = 165;
  static final int NEW_ROW_92 = 166;
  static final int NEW_ROW_93 = 167;
  static final int NEW_ROW_94 = 168;
  static final int NEW_ROW_95 = 169;
  static final int NEW_ROW_96 = 170;
  static final int NEW_ROW_97 = 171;
  static final int NEW_ROW_98 = 172;
  static final int NEW_ROW_99 = 173;
  static final int NEW_ROW_100 = 174;
  static final int NEW_ROW_101 = 175;
  static final int NEW_ROW_102 = 176;
  static final int NEW_ROW_103 = 177;
  static final int NEW_ROW_104 = 178;
  static final int NEW_ROW_105 = 179;
  static final int NEW_ROW_106 = 180;
  static final int NEW_ROW_107 = 181;
  static final int NEW_ROW_108 = 182;
  static final int NEW_ROW_109 = 183;
  static final int NEW_ROW_110 = 184;
  static final int NEW_ROW_111 = 185;
  static final int NEW_ROW_112 = 186;
  static final int NEW_ROW_113 = 187;
  static final int NEW_ROW_114 = 188;
  static final int NEW_ROW_115 = 189;
  static final int NEW_ROW_116 = 190;
  static final int NEW_ROW_117 = 191;
  static final int NEW_ROW_118 = 192;
  static final int NEW_ROW_119 = 193;
  static final int NEW_ROW_120 = 194;
  static final int NEW_ROW_121 = 195;
  static final int NEW_ROW_122 = 196;
  static final int NEW_ROW_123 = 197;
  static final int NEW_ROW_124 = 198;
  static final int NEW_ROW_125 = 199;
  static final int NEW_ROW_126 = 200;
  static final int NEW_ROW_127 = 201;
  static final int NEW_ROW_128 = 202;
  static final int NEW_ROW_129 = 203;
  static final int NEW_ROW_130 = 204;
  static final int NEW_ROW_131 = 205;
  static final int NEW_ROW_132 = 206;
  static final int NEW_ROW_133 = 207;
  static final int NEW_ROW_134 = 208;
  static final int NEW_ROW_135 = 209;
  static final int NEW_ROW_136 = 210;
  static final int NEW_ROW_137 = 211;
  static final int NEW_ROW_138 = 212;
  static final int NEW_ROW_139 = 213;
  static final int NEW_ROW_140 = 214;
  static final int NEW_ROW_141 = 215;
  static final int NEW_ROW_142 = 216;
  static final int NEW_ROW_143 = 217;
  static final int NEW_ROW_144 = 218;
  static final int NEW_ROW_145 = 219;
  static final int NEW_ROW_146 = 220;
  static final int NEW_ROW_147 = 221;
  static final int NEW_ROW_148 = 222;
  static final int NEW_ROW_149 = 223;
  static final int NEW_ROW_150 = 224;
  static final int NEW_ROW_151 = 225;
  static final int NEW_ROW_152 = 226;
  static final int NEW_ROW_153 = 227;
  static final int NEW_ROW_154 = 228;
  static final int NEW_ROW_155 = 229;
  static final int NEW_ROW_156 = 230;
  static final int NEW_ROW_157 = 231;
  static final int NEW_ROW_158 = 232;
  static final int NEW_ROW_159 = 233;
  static final int NEW_ROW_160 = 234;
  static final int NEW_ROW_161 = 235;
  static final int NEW_ROW_162 = 236;
  static final int NEW_ROW_163 = 237;
  static final int NEW_ROW_164 = 238;
  static final int XXX1 = 239;
  static final int XXX2 = 240;
  static final int XXX3 = 241;
  static final int XXX4 = 242;
  static final int YYY = 243;
  static final int NO_OP = 244;
  static final int CHAR_LOC = 245;
  static final int CHAR_LOC0 = 246;
  static final int PRE = 247;
  static final int POST = 248;
  static final int POST_POST = 249;

  static final int GF_ID_BYTE = 131;
  static final int GF_POST_SIG = 223;

  /**
   * The zero-based location (byte number) of this command within
   * the GF file.
   */
  private int location;


  /**
   * The opcode of this command. 
   */
  private int opcode;

  /**
   * Creates a Command object with the specified location and opcode.
   *
   * @param l the byte location of this Command within the GF file.
   * @param o the opcode of this Command.
   */
  public Command(int l, int o) {
    location = l;
    opcode = o;
  }

  /**
   * @return the opcode of this Command.
   */
  public int getOpCode() {
    return opcode;
  }

  /**
   * @return the byte location of this Command within the GF file.
   */
  public int getLocation() {
    return location;
  }
}
