package com.dmi.smartux.common.util;




public class Base64 {


// The line separator string of the operating system.

private static final String systemLineSeparator = System.getProperty("line.separator");

// Mapping table from 6-bit nibbles to Base64 characters.
private static final char[] map1 = new char[64];

   static {

      int i=0;
      for (char c='A'; c<='Z'; c++) map1[i++] = c;

      for (char c='a'; c<='z'; c++) map1[i++] = c;

      for (char c='0'; c<='9'; c++) map1[i++] = c;

      map1[i++] = '+'; map1[i++] = '/'; 
      }
// Mapping table from Base64 characters to 6-bit nibbles.
private static final byte[] map2 = new byte[128];


   static {


      for (int i=0; i<map2.length; i++) map2[i] = -1;

      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }

/**

52 	

* Encodes a string into Base64 format.

53 	

* No blanks or line breaks are inserted.

54 	

* @param s  A String to be encoded.

55 	

* @return   A String containing the Base64 encoded data.

56 	

*/


public static String encodeString (String s) {

   return new String(encode(s.getBytes())); }


/**

61 	

* Encodes a byte array into Base 64 format and breaks the output into lines of 76 characters.

62 	

* This method is compatible with <code>sun.misc.BASE64Encoder.encodeBuffer(byte[])</code>.

63 	

* @param in  An array containing the data bytes to be encoded.

64 	

* @return    A String containing the Base64 encoded data, broken into lines.

65 	

*/

public static String encodeLines (byte[] in) {
   return encodeLines(in, 0, in.length, 76, systemLineSeparator); }

	

/**

70 	

* Encodes a byte array into Base 64 format and breaks the output into lines.

71 	

* @param in            An array containing the data bytes to be encoded.

72 	

* @param iOff          Offset of the first byte in <code>in</code> to be processed.

73 	

* @param iLen          Number of bytes to be processed in <code>in</code>, starting at <code>iOff</code>.

74 	

* @param lineLen       Line length for the output data. Should be a multiple of 4.

75 	

* @param lineSeparator The line separator to be used to separate the output lines.

76 	

* @return              A String containing the Base64 encoded data, broken into lines.

77 	

*/


public static String encodeLines (byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {

   int blockLen = (lineLen*3) / 4;

   if (blockLen <= 0) throw new IllegalArgumentException();

   int lines = (iLen+blockLen-1) / blockLen;

   int bufLen = ((iLen+2)/3)*4 + lines*lineSeparator.length();

   StringBuilder buf = new StringBuilder(bufLen);

   int ip = 0;

   while (ip < iLen) {

      int l = Math.min(iLen-ip, blockLen);

      buf.append (encode(in, iOff+ip, l));

      buf.append (lineSeparator);

      ip += l; }

   return buf.toString(); }

/**

93 	

* Encodes a byte array into Base64 format.

94 	

* No blanks or line breaks are inserted in the output.

95 	

* @param in  An array containing the data bytes to be encoded.

96 	

* @return    A character array containing the Base64 encoded data.

97 	

*/
	

public static String encode (byte[] in) {

   return new String(encode(in, 0, in.length)); }

/**

102 	

* Encodes a byte array into Base64 format.

103 	

* No blanks or line breaks are inserted in the output.

104 	

* @param in    An array containing the data bytes to be encoded.

105 	

* @param iLen  Number of bytes to process in <code>in</code>.

106 	

* @return      A character array containing the Base64 encoded data.

107 	

*/


public static char[] encode (byte[] in, int iLen) {

   return encode(in, 0, iLen); }
	

/**

112 	

* Encodes a byte array into Base64 format.

113 	

* No blanks or line breaks are inserted in the output.

114 	

* @param in    An array containing the data bytes to be encoded.

115 	

* @param iOff  Offset of the first byte in <code>in</code> to be processed.

116 	

* @param iLen  Number of bytes to process in <code>in</code>, starting at <code>iOff</code>.

117 	

* @return      A character array containing the Base64 encoded data.

118 	

*/

public static char[] encode (byte[] in, int iOff, int iLen) {
   int oDataLen = (iLen*4+2)/3;       // output length without padding

   int oLen = ((iLen+2)/3)*4;         // output length including padding

   char[] out = new char[oLen];

   int ip = iOff;

   int iEnd = iOff + iLen;

   int op = 0;

   while (ip < iEnd) {

      int i0 = in[ip++] & 0xff;

      int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
      int i2 = ip < iEnd ? in[ip++] & 0xff : 0;

      int o0 = i0 >>> 2;

      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);

      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);

      int o3 = i2 & 0x3F;

      out[op++] = map1[o0];

      out[op++] = map1[o1];


      out[op] = op < oDataLen ? map1[o2] : '='; op++;

      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
   return out; }


/**

141 	

* Decodes a string from Base64 format.

142 	

* No blanks or line breaks are allowed within the Base64 encoded input data.

143 	

* @param s  A Base64 String to be decoded.

144 	

* @return   A String containing the decoded data.

145 	

* @throws   IllegalArgumentException If the input is not valid Base64 encoded data.

146 	

*/

public static String decodeString (String s) {


   return new String(decode(s)); }


/**

151 	

* Decodes a byte array from Base64 format and ignores line separators, tabs and blanks.

152 	

* CR, LF, Tab and Space characters are ignored in the input data.

153 	

* This method is compatible with <code>sun.misc.BASE64Decoder.decodeBuffer(String)</code>.

154 	

* @param s  A Base64 String to be decoded.

155 	

* @return   An array containing the decoded data bytes.

156 	

* @throws   IllegalArgumentException If the input is not valid Base64 encoded data.

157 	

*/

public static byte[] decodeLines (String s) {

   char[] buf = new char[s.length()];

   int p = 0;

   for (int ip = 0; ip < s.length(); ip++) {

      char c = s.charAt(ip);

      if (c != ' ' && c != '\r' && c != '\n' && c != '\t')

         buf[p++] = c; }

   return decode(buf, 0, p); }


/**

168 	

* Decodes a byte array from Base64 format.

169 	

* No blanks or line breaks are allowed within the Base64 encoded input data.

170 	

* @param s  A Base64 String to be decoded.

171 	

* @return   An array containing the decoded data bytes.

172 	

* @throws   IllegalArgumentException If the input is not valid Base64 encoded data.

173 	

*/


public static byte[] decode (String s) {

   return decode(s.toCharArray()); }

/**

178 	

* Decodes a byte array from Base64 format.

179 	

* No blanks or line breaks are allowed within the Base64 encoded input data.

180 	

* @param in  A character array containing the Base64 encoded data.

181 	

* @return    An array containing the decoded data bytes.

182 	

* @throws    IllegalArgumentException If the input is not valid Base64 encoded data.

183 	

*/



public static byte[] decode (char[] in) {



   return decode(in, 0, in.length); }



/**

188 	

* Decodes a byte array from Base64 format.

189 	

* No blanks or line breaks are allowed within the Base64 encoded input data.

190 	

* @param in    A character array containing the Base64 encoded data.

191 	

* @param iOff  Offset of the first character in <code>in</code> to be processed.

192 	

* @param iLen  Number of characters to process in <code>in</code>, starting at <code>iOff</code>.

193 	

* @return      An array containing the decoded data bytes.

194 	

* @throws      IllegalArgumentException If the input is not valid Base64 encoded data.

195 	

*/


public static byte[] decode (char[] in, int iOff, int iLen) {

   if (iLen%4 != 0) throw new IllegalArgumentException ("Length of Base64 encoded input string is not a multiple of 4.");

   while (iLen > 0 && in[iOff+iLen-1] == '=') iLen--;

   int oLen = (iLen*3) / 4;

   byte[] out = new byte[oLen];

   int ip = iOff;

   int iEnd = iOff + iLen;

	

   int op = 0;


   while (ip < iEnd) {

      int i0 = in[ip++];

	

      int i1 = in[ip++];
	

      int i2 = ip < iEnd ? in[ip++] : 'A';



      int i3 = ip < iEnd ? in[ip++] : 'A';


      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)

	

         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");


      int b0 = map2[i0];



      int b1 = map2[i1];



      int b2 = map2[i2];



      int b3 = map2[i3];

	

      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)


         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");



      int o0 = ( b0       <<2) | (b1>>>4);



      int o1 = ((b1 & 0xf)<<4) | (b2>>>2);


      int o2 = ((b2 &   3)<<6) |  b3;

	

      out[op++] = (byte)o0;



      if (op<oLen) out[op++] = (byte)o1;



      if (op<oLen) out[op++] = (byte)o2; }


   return out; }


// Dummy constructor.




} // end class Base64Coder